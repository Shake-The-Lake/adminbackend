package ch.fhnw.shakethelakebackend.model.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.util.Date;

/**
 *
 * Entity for activity types
 *
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Where(clause = "deleted=false")
public class ActivityType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Embedded
    private LocalizedString name;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "en", column = @Column(name = "description_en")),
        @AttributeOverride(name = "de", column = @Column(name = "description_de")),
        @AttributeOverride(name = "swissGerman", column = @Column(name = "description_swiss_german")) })
    private LocalizedString description;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "en", column = @Column(name = "checklist_en")),
        @AttributeOverride(name = "de", column = @Column(name = "checklist_de")),
        @AttributeOverride(name = "swissGerman", column = @Column(name = "checklist_swiss_german")) })
    private LocalizedString checklist;

    private String icon;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;


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
    private void setDefaults() {
        if (this.createdBy == null) {
            this.createdBy = "TempUser";
        }
        if (this.updatedBy == null) {
            this.updatedBy = "TempUser";
        }
    }
}
