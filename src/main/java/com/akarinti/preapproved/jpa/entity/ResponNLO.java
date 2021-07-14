package com.akarinti.preapproved.jpa.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "RESPON_NLO")
@DynamicUpdate
@Data
public class ResponNLO extends Base {

    @Column(name = "REQUEST_ID")
    private boolean requestId;

    @Column(name = "PEMOHON")
    private boolean isPemohon;

    @Column(name = "TGL_REQUEST")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tglRequest;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "USER_BCA_ID")
    private UserBCA userBCA;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "RUMAHSAYA_ID")
    private RumahSaya rumahSaya;

    // do we need to define per field or save as json?
    @Column(name = "JSON_SLIK")
    private String jsonSLIK;

    // TODO: define column

}
