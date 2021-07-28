package com.akarinti.preapproved.dto.authentication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileUserDTO {

    @JsonProperty(value = "user_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userId;

    @JsonProperty(value = "user_name")
    private String userName;

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "full_name")
    private String fullName;

    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    @JsonProperty(value = "role_code")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String roleCode;

    @JsonProperty(value = "office_code")
    private String officeCode;

    @JsonProperty(value = "office_name")
    private String officeName;

    @JsonProperty(value = "dept_code")
    private String deptCode;

    @JsonProperty(value = "dept_name")
    private String deptName;

    @JsonProperty(value = "sub_dept_code")
    private String subDeptCode;

    @JsonProperty(value = "sub_dept_name")
    private String subDeptName;

    @JsonProperty(value = "job_title_code")
    private String jobTitleCode;

    @JsonProperty(value = "job_title_name")
    private String jobTitleName;
}
