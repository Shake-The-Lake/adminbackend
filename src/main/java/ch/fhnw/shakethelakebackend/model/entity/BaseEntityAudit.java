package ch.fhnw.shakethelakebackend.model.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Where(clause = "deleted=false")
public class BaseEntityAudit {
    @CreatedBy
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "firebaseToken", column = @Column(name = "created_by_user_id")),
        @AttributeOverride(name = "firebaseUserName", column = @Column(name = "created_by_user_name"))
    })
    private User createdByUser;

    @LastModifiedBy
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "firebaseToken", column = @Column(name = "updated_by_user_id")),
        @AttributeOverride(name = "firebaseUserName", column = @Column(name = "updated_by_user_name"))
    })
    private User updatedByUser;

    private String createdBy;
    private String updatedBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    private boolean deleted = Boolean.FALSE;

    @PrePersist
    public void prePersist() {
        if (createdByUser != null) {
            this.createdBy = createdByUser.getFirebaseUserName();
            preUpdate();
        }
    }

    @PreUpdate
    public void preUpdate() {
        if (updatedByUser != null) {
            this.updatedBy = updatedByUser.getFirebaseUserName();
        }
    }

}
