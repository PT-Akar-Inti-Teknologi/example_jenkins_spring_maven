package com.akarinti.preapproved.jpa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "SLIK")
@DynamicUpdate
@Data
public class SLIK extends Base {

    @Column(name = "REQUEST_ID")
    private String requestId;

    @Column(name = "PEMOHON")
    private boolean isPemohon;

    @Column(name = "TGL_REQUEST")
    private LocalDate tglRequest;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "USER_BCA_ID")
    private UserBCA userBCA;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "APLIKASI_ID")
    private Aplikasi aplikasi;

    // do we need to define per field or save as json?
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "JSON_SLIK")
    private String jsonSLIK;

    // TODO: define column

}
