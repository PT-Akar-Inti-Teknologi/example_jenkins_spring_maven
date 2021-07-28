package com.akarinti.preapproved.service;

import com.akarinti.preapproved.dto.StatusCodeMessageDTO;
import com.akarinti.preapproved.jpa.entity.Holiday;
import com.akarinti.preapproved.jpa.repository.HolidayRepository;
import com.akarinti.preapproved.utils.exception.CustomException;
import com.akarinti.preapproved.utils.exception.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class DataManagementService {
    @Value("${holiday.path}")
    String holidayPath;

    @Autowired
    HolidayRepository holidayRepository;

    public Boolean syncHoliday() {
        InputStream inputStream = null;
        try {
            File file = new File(holidayPath);
            inputStream = new FileInputStream(file);
//            List<String> lines = FileUtils.readLines(f, "UTF-8");
            List<String> lines = this.readFromInputStream(inputStream);
            for (String sample : lines)
            {
//                System.out.println(sample);
                String[] dataSplit = sample.split("\\|;\\|");
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Date tanggal;
                try{
                    tanggal = formatter.parse(dataSplit[0]);
                }
                catch(Exception e)
                {
                    log.error("ERROR: " + e);
                    throw new CustomException(StatusCode.INVALID_ARGUMENT, new StatusCodeMessageDTO("format tanggal salah", "invalid date format"));
                }
                Holiday getHoliday = holidayRepository.findByTanggal(tanggal);
                if(getHoliday==null) {
                    Holiday holiday = new Holiday();
                    holiday.setTanggal(tanggal);
                    holiday.setDescription(dataSplit[1]);
                    holidayRepository.saveAndFlush(holiday);
                } else {
                    getHoliday.setTanggal(tanggal);
                    getHoliday.setDescription(dataSplit[1]);
                    holidayRepository.saveAndFlush(getHoliday);
                }
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    private List<String> readFromInputStream(InputStream inputStream) throws IOException {
        List<String> stringList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                stringList.add(line);
            }
        }
        return stringList;
    }
}
