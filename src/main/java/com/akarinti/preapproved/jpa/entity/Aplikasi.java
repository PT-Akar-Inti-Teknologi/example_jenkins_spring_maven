package com.akarinti.preapproved.jpa.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "APLIKASI")
@DynamicUpdate
@Data
public class Aplikasi extends Base {

    @OneToOne
    @JoinColumn(name = "RUMAHSAYA_ID", nullable = false)
    private RumahSaya rumahSaya;

    // TODO: need to define response data from platform generator here?
}
