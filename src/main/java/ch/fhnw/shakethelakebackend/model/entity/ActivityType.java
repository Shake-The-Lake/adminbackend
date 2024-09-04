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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
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

    @ManyToOne
    @JoinColumn(name = "icon_id")
    private Icon icon;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

}
