package com.akarinti.preapproved.jpa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Cache;
import javax.persistence.*;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "HOLIDAY")
@DynamicUpdate
@Data
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="org.hibernate.cache.StandardQueryCache")
public class Holiday extends Base {

    @Column(name="TANGGAL")
    @Temporal(TemporalType.DATE)
    private Date tanggal;

    @Column(name="DESCRIPTION")
    private String description;

}
