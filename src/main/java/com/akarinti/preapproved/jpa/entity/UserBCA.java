package com.akarinti.preapproved.jpa.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "USER_BCA")
@DynamicUpdate
@Data
public class UserBCA extends Base {

    @Column(name = "USER_ID_PIC", length = 32)
    private String userIdPic;

    @Column(name = "FULL_NAME", nullable = false, length = 256)
    private String fullName;

    @Column(name = "PHONE_NUMBER", length = 32)
    private String phoneNumber;

    @Column(name = "EXTENSION")
    private String extension;

    @Column(name = "OFFICE_CODE", length = 32)
    private String officeCode;

    @Column(name = "OFFICE_NAME")
    private String officeName;

    @Column(name = "DEPT_CODE")
    private String deptCode;

    @Column(name = "DEPT_NAME")
    private String deptName;

    @Column(name = "SUB_DEPT_CODE")
    private String subDeptCode;

    @Column(name = "SUB_DEPT_NAME")
    private String subDeptName;

    @Column(name = "JOB_TITLE_CODE")
    private String jobTitleCode;

    @Column(name = "JOB_TITLE_NAME")
    private String jobTitleName;

}
