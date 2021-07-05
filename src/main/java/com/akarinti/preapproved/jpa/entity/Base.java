package com.akarinti.preapproved.jpa.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Base entity class
 */
@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class)    // Must be temporarily commented when unit testing with Junit (otherwise Exception: sun.reflect.annotation.TypeNotPresentExceptionProxy)
@DynamicUpdate
@Data
public abstract class Base implements Serializable {

	private static final long serialVersionUID = -7369920601847524273L;

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
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_CREATED", updatable = false)
	private Date creationDate;

    @CreatedBy
    @Column(name = "CREATED_BY", length = 30, updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_MODIFIED")
    private Date modificationDate;

    @LastModifiedBy
    @Column(name = "MODIFIED_BY", length = 30)
    private String modifiedBy;

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
        this.creationDate = new Date();
    }

    @PreUpdate
    public void preUpdate(){
        this.modificationDate = new Date();
    }

}
