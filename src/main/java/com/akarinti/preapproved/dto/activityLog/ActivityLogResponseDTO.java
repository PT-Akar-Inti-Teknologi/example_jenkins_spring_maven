package com.akarinti.preapproved.dto.activityLog;

import com.akarinti.preapproved.jpa.entity.ActivityLog;
import com.akarinti.preapproved.jpa.entity.UserBCA;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Data
public class ActivityLogResponseDTO {

    @JsonProperty("secure_id")
    private String secureId;

    @JsonProperty("nama_nasabah")
    private String namaNasabah;

    @JsonProperty("email_nasabah")
    private String emailNasabah;

    @JsonProperty("user_id_bca")
    private String userIdBCA;

    @JsonProperty("created_time")
    private Long createdTime;

    public static ActivityLogResponseDTO fromEntity(ActivityLog activityLog) {
        return new ActivityLogResponseDTO(
                activityLog.getSecureId(),
                activityLog.getAplikasi().getNamaLengkap(),
                activityLog.getAplikasi().getEmail(),
                activityLog.getUserBCA().getUserIdPic(),
                activityLog.getCreationDate()
            );
    }

    public ActivityLogResponseDTO(String secureId,
                                  String namaNasabah,
                                  String emailNasabah,
                                  String userIdBCA,
                                  LocalDateTime createdTime
                        ) {
        this.secureId = secureId;
        this.namaNasabah = namaNasabah;
        this.emailNasabah = emailNasabah;
        this.userIdBCA = userIdBCA;
        this.createdTime = createdTime.toInstant(ZoneOffset.ofHours(7)).getEpochSecond();
    }

    public ActivityLogResponseDTO() {
    }
}
