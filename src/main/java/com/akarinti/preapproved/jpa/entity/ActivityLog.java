package com.akarinti.preapproved.jpa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "ACTIVITY_LOG")
@DynamicUpdate
@Data
public class ActivityLog extends Base {

    @Column(name = "status")
    private String status;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "USER_BCA_ID")
    private UserBCA userBCA;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "APLIKASI_ID")
    private Aplikasi aplikasi;

}
