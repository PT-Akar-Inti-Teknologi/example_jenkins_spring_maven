package com.akarinti.preapproved.dto.nlo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RequestCBASDTO {

    private String name;

    private String gender;

    private LocalDate dob;

    private String ktp;

    private boolean isApplicant;

    public static RequestCBASDTO fromEntity(String name, String gender, LocalDate dob, String ktp, boolean isApplicant) {
        return new RequestCBASDTO(name, gender, dob, ktp, isApplicant);
    }

    public RequestCBASDTO(String name, String gender, LocalDate dob, String ktp, boolean isApplicant) {
        this.name = name;
        this.gender = gender;
        this.dob = dob;
        this.ktp = ktp;
        this.isApplicant = isApplicant;
    }

}
