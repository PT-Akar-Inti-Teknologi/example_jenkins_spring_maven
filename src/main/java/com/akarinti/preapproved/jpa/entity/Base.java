package com.akarinti.preapproved.jpa.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * Base entity class
 */
@MappedSuperclass
@DynamicUpdate
@Data
public abstract class Base implements Serializable {

	protected Base() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
	protected Integer id;

    /**
     * Random id for security reason
     */
    @Column(name = "SECURE_ID", unique = true, length = 36)
    private String secureId;

    @CreatedDate
	@Column(name = "DATE_CREATED", updatable = false)
	private LocalDateTime creationDate;

    @LastModifiedDate
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime modificationDate;

	@Version
	@Column(name = "VERSION")
	private Integer version = 0;

    @Column(name = "DELETED")
    private Boolean deleted;

    @PrePersist
    public void prePersist() {
        if (this.secureId == null) {
            this.secureId = UUID.randomUUID().toString();
        }
        this.deleted = false;
        this.creationDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        this.modificationDate = LocalDateTime.now();
    }

}
