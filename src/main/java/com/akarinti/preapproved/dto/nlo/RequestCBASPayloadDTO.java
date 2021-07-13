package com.akarinti.preapproved.dto.nlo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestCBASPayloadDTO {

    @JsonProperty(value="request_id")
    private String requestId;

    @JsonProperty(value="app_type")
    private String appType;

    @JsonProperty(value="product")
    private String product;

    @JsonProperty(value="cust_type")
    private String custType;

    @JsonProperty(value="name1")
    private String name1;

    @JsonProperty(value="name2")
    private String name2;

    @JsonProperty(value="name3")
    private String name3;

    @JsonProperty(value="name4")
    private String name4;

    @JsonProperty(value="name5")
    private String name5;

    @JsonProperty(value="name6")
    private String name6;

    @JsonProperty(value="name7")
    private String name7;

    @JsonProperty(value="name8")
    private String name8;

    @JsonProperty(value="name9")
    private String name9;

    @JsonProperty(value="gender")
    private String gender;

    @JsonProperty(value="dob")
    private String dob;

    @JsonProperty(value="pob")
    private String pob;

    @JsonProperty(value="ktp")
    private String ktp;

    @JsonProperty(value="npwp")
    private String npwp;

    @JsonProperty(value="company")
    private String company;

    @JsonProperty(value="without_pdf")
    private String without_pdf;

    @JsonProperty(value="req_purpose")
    private String reqPurpose;

}
