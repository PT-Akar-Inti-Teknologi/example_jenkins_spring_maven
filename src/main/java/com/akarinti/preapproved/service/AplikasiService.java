package com.akarinti.preapproved.service;

import com.akarinti.preapproved.dto.DataResponseDTO;
import com.akarinti.preapproved.dto.MetaPaginationDTO;
import com.akarinti.preapproved.dto.ResultDTO;
import com.akarinti.preapproved.dto.StatusCodeMessageDTO;
import com.akarinti.preapproved.dto.apiresponse.BCAErrorResponse;
import com.akarinti.preapproved.dto.apiresponse.BCAOauth2Response;
import com.akarinti.preapproved.dto.nlo.RequestCBASDTO;
import com.akarinti.preapproved.dto.nlo.RequestCBASPayloadDTO;
import com.akarinti.preapproved.dto.rumahsaya.ApplicationDataRequestDTO;
import com.akarinti.preapproved.dto.rumahsaya.RumahSayaDTO;
import com.akarinti.preapproved.dto.rumahsaya.RumahSayaCreateResponseDTO;
import com.akarinti.preapproved.dto.rumahsaya.RumahSayaResponseRequestDTO;
import com.akarinti.preapproved.jpa.entity.ActivityLog;
import com.akarinti.preapproved.jpa.entity.Aplikasi;
import com.akarinti.preapproved.jpa.entity.SLIK;
import com.akarinti.preapproved.jpa.entity.UserBCA;
import com.akarinti.preapproved.jpa.predicate.AplikasiPredicate;
import com.akarinti.preapproved.jpa.repository.ActivityLogRepository;
import com.akarinti.preapproved.jpa.repository.AplikasiRepository;
import com.akarinti.preapproved.jpa.repository.SLIKRepository;
import com.akarinti.preapproved.utils.FileUtil;
import com.akarinti.preapproved.utils.HelperUtil;
import com.akarinti.preapproved.utils.WebServiceUtil;
import com.akarinti.preapproved.utils.exception.CustomException;
import com.akarinti.preapproved.utils.exception.StatusCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.querydsl.core.BooleanBuilder;
import kong.unirest.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.akarinti.preapproved.jpa.entity.QAplikasi.aplikasi;

@Slf4j
@Service
@Transactional
public class AplikasiService {

    @Value("${rumahsaya.clientId}")
    String rumahSayaClientId;

    @Value("${rumahsaya.url}")
    String rumahSayaUrl;

    @Value("${hcp.url}")
    String hcpUrl;

    @Autowired
    AplikasiRepository aplikasiRepository;

    @Autowired
    ActivityLogRepository activityLogRepository;

    @Autowired
    HelperUtil helperService;

    @Autowired
    FileUtil fileUtil;

    @Autowired
    SignInService signInService;

    @Autowired
    SLIKRepository SLIKRepository;

    @Transactional
    public RumahSayaCreateResponseDTO createData(RumahSayaDTO rumahSayaDTO) {
        Aplikasi aplikasi = Aplikasi.fromDTO(rumahSayaDTO);
        aplikasiRepository.save(aplikasi);

        return new RumahSayaCreateResponseDTO(true);
    }

    private void requestCBAS(RequestCBASDTO dataDTO, String secureIdAplikasi, UserBCA userBCA) {
        Aplikasi aplikasi = aplikasiRepository.findBySecureId(secureIdAplikasi);
        log.info("aplikasi: "+ aplikasi);
        if (aplikasi != null) {
            // TODO: generate random requestId
            String requestId = String.valueOf(new Date().getTime());
            String appType = "CCOS";
            String product = "BOPAKPR";
            String custType = "A";
            String name1 = dataDTO.getName();
            String gender = dataDTO.getGender();

            LocalDate tglLahir = dataDTO.getDob();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dob = tglLahir.format(formatter);

            String ktp = dataDTO.getKtp();

            RequestCBASPayloadDTO requestCBASPayloadDTO = RequestCBASPayloadDTO.fromEntity(requestId, appType, product, custType, name1, gender, dob, ktp);

            NLOService.requestCBAS(requestCBASPayloadDTO);

            SLIK slik = new SLIK();
            slik.setAplikasi(aplikasi);
            slik.setRequestId(requestId);
            slik.setUserBCA(userBCA);
            slik.setPemohon(dataDTO.isApplicant());
            slik.setTglRequest(LocalDate.now());
            SLIKRepository.save(slik);
        }
    }

    @Transactional
    public DataResponseDTO processApplication(ApplicationDataRequestDTO applicationDataRequestDTO) {
        UserBCA userBCA = signInService.getUser();
        Aplikasi aplikasi = aplikasiRepository.findBySecureId(applicationDataRequestDTO.getSecureId());
        if (aplikasi == null) throw new CustomException(StatusCode.NOT_FOUND, new StatusCodeMessageDTO("data aplikasi tidak ditemukan", "application data not found"));

        RequestCBASDTO dataDTO = RequestCBASDTO.fromEntity(aplikasi.getNamaLengkap(), aplikasi.getJenisKelamin(), aplikasi.getTanggalLahir(), aplikasi.getNik(), true);
        requestCBAS(dataDTO, applicationDataRequestDTO.getSecureId(), userBCA);

        boolean hasSpouse = (!aplikasi.getNamaLengkapPasangan().isEmpty() && aplikasi.getTanggalLahirPasangan() != null && !aplikasi.getNikPasangan().isEmpty());
        if (hasSpouse) {
            RequestCBASDTO dataSpouseDTO = RequestCBASDTO.fromEntity(aplikasi.getNamaLengkapPasangan(), aplikasi.getJenisKelaminPasangan(), aplikasi.getTanggalLahirPasangan(), aplikasi.getNikPasangan(), false);
            requestCBAS(dataSpouseDTO, applicationDataRequestDTO.getSecureId(), userBCA);
        }
        aplikasi.setStatus("PENDING_CBAS");
        aplikasiRepository.save(aplikasi);

        ActivityLog activityLog = new ActivityLog();
        activityLog.setUserBCA(userBCA);
        activityLog.setAplikasi(aplikasi);
        activityLog.setStatus("PROCESSED");
        activityLogRepository.save(activityLog);

        return new DataResponseDTO(true);
    }

    public DataResponseDTO rejectApplication(ApplicationDataRequestDTO applicationDataRequestDTO) {
        UserBCA userBCA = signInService.getUser();
        Aplikasi aplikasi = aplikasiRepository.findBySecureId(applicationDataRequestDTO.getSecureId());
        if (aplikasi == null) throw new CustomException(StatusCode.NOT_FOUND, new StatusCodeMessageDTO("data aplikasi tidak ditemukan", "application data not found"));

        RumahSayaResponseRequestDTO rumahSayaResponseRequestDTO = new RumahSayaResponseRequestDTO();
        // TODO: update flag
        //        rumahSayaResponseRequestDTO.setFlag("Data dan KTP tidak sesuai");
        rumahSayaResponseRequestDTO.setFlag("REJECTED");
        rumahSayaResponseRequestDTO.setAppDataId(aplikasi.getAppDataID());

        responseRumahSaya(rumahSayaResponseRequestDTO);

        ActivityLog activityLog = new ActivityLog();
        activityLog.setUserBCA(userBCA);
        activityLog.setAplikasi(aplikasi);
        activityLog.setStatus("REJECTED");
        activityLogRepository.save(activityLog);

        aplikasi.setStatus("REJECTED");

        return new DataResponseDTO(true);
    }

    @SneakyThrows
    @Transactional
    public ResultDTO getData(String search, String status, Pageable pageable) {
        Page<Aplikasi> aplikasiList;

        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("created_time", "creationDate");
        columnMapping.put("nama_lengkap", "namaLengkap");
        columnMapping.put("service_level", "creationDate");
        Sort.Direction sortDirection = Sort.Direction.ASC;
        String sortProperty = "created_time";
        for (Sort.Order order: pageable.getSort()) {
            sortProperty = order.getProperty();
            sortDirection = order.getDirection();
        }
        Sort sort = Sort.by(sortDirection, columnMapping.get(sortProperty));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        if(search != null || status != null) {
            BooleanBuilder builder = new BooleanBuilder();
            builder.and(AplikasiPredicate.aplikasiWithStatus(status)).and(AplikasiPredicate.aplikasiWithSearch(search));
            aplikasiList = aplikasiRepository.findAll(builder, pageable);
        }  else {
            aplikasiList = aplikasiRepository.findAll(pageable);
        }

        List<RumahSayaDTO> rumahSayaDTOList = new ArrayList<>();
        for (Aplikasi aplikasi: aplikasiList.getContent()) {
            Date createdDate = Date.from(aplikasi.getCreationDate().atZone(ZoneId.of("GMT+7")).toInstant());
//
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
//            String dateInString = "2021-07-21 17:00";
//            Date date = formatter.parse(dateInString);

            long remainingSL = helperService.getServiceLevelRemaining(createdDate);
            RumahSayaDTO rumahSayaDTO = RumahSayaDTO.fromEntity(aplikasi);
            rumahSayaDTO.setSisaSL(remainingSL);
            rumahSayaDTOList.add(rumahSayaDTO);
        }
        return new ResultDTO(new MetaPaginationDTO(aplikasiList), rumahSayaDTOList);

    }

    @SneakyThrows
    public Map<String, Object> downloadImage(String secureIdAplikasi, String fileName) {
        Aplikasi aplikasi = aplikasiRepository.findBySecureId(secureIdAplikasi);
        if (aplikasi == null) throw new CustomException(StatusCode.NOT_FOUND, new StatusCodeMessageDTO("data aplikasi tidak ditemukan", "application data not found"));

        Map<String, Object> resultMap = new HashMap<>();
        byte[] image;
        try {
            StringBuilder fileUrl = new StringBuilder();
            fileUrl.append(hcpUrl);
            fileUrl.append("/");
            fileUrl.append(aplikasi.getAppDataID());
            fileUrl.append("/");
            fileUrl.append(fileName);
            InputStream inputStream = fileUtil.getFileHCP(fileUrl.toString());
            String contentType = "application/octet-stream";
            resultMap.put("contentType", contentType);

            image = IOUtils.toByteArray(inputStream);
            resultMap.put("byte", image);

        } catch (Exception ex) {
            throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR);
            //Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);a
        }
        return resultMap;
    }

    public ResultDTO overview() {
        long statusNew = aplikasiRepository.countByStatusIgnoreCase("NEW");
        long statusPending = aplikasiRepository.countByStatusIgnoreCase("PENDING_CBAS");
        long statusError = aplikasiRepository.countByStatusIgnoreCase("ERROR_CBAS");

        HashMap<String, Long> overview = new HashMap<>();
        overview.put("new", statusNew);
        overview.put("pending", statusPending);
        overview.put("error", statusError);
        return new ResultDTO(overview);
    }

    public void responseRumahSaya(RumahSayaResponseRequestDTO rumahSayaResponseRequestDTO) {
        BCAOauth2Response bcaOauth2Response = WebServiceUtil.getBCAOauth();
        String authorization = "Bearer " + bcaOauth2Response.getAccess_token();

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(rumahSayaResponseRequestDTO);
        } catch (Exception e) {
            log.error("ERROR: "+ e);
            throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR);
        }
        HttpResponse<JsonNode> response;
        try {
            Unirest.config().verifySsl(WebServiceUtil.getVerifySSL());
            response = Unirest
                    .post(rumahSayaUrl)
                    .header("Content-Type", "application/json")
                    .header("Authorization", authorization)
                    .header("client-id", rumahSayaClientId)
                    .body(jsonBody)
                    .asJson();
            String apiResponse = response.getBody().toString();
            if (response.getStatus() != 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                BCAErrorResponse bcaErrorResponse = objectMapper.readValue(apiResponse, BCAErrorResponse.class);
                String errorMessage = (String) bcaErrorResponse.getError_message().get("indonesian");
                throw new RuntimeException(errorMessage);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode node = objectMapper.readValue(apiResponse, ObjectNode.class);
            com.fasterxml.jackson.databind.JsonNode errorSchema = node.get("error_schema");
            String errorCode = errorSchema.get("error_code").toString();
            if (!errorCode.equals("WBF-00-000")) {
                String errorMessage = errorSchema.get("error_message").get("indonesian").toString();
                log.info("errorMessage: "+ errorMessage);
                throw new RuntimeException(errorMessage);
            }
        } catch (UnirestException | JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load url: " + rumahSayaUrl);
        }
    }

    public DataResponseDTO retryErrorCBAS() {
       List<Aplikasi> aplikasiList = aplikasiRepository.findAllByStatusIgnoreCase("ERROR_CBAS");

       for(Aplikasi aplikasi: aplikasiList) {
           ApplicationDataRequestDTO applicationDataRequestDTO = new ApplicationDataRequestDTO();
           applicationDataRequestDTO.setSecureId(aplikasi.getSecureId());

           DataResponseDTO dataResponseDTO = processApplication(applicationDataRequestDTO);
           log.info("dataReponseDTO: "+ dataResponseDTO);
       }

        DataResponseDTO dataResponseDTO = new DataResponseDTO();
        dataResponseDTO.setSuccess(true);
        return dataResponseDTO;
    }
}
