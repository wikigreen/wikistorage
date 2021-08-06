package com.wikigreen.wikistorage.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Event extends BaseEntity {
    @Column(name = "event_type")
    @Enumerated(value = EnumType.STRING)
    private EventType eventType;

    @Column(name = "event_date")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", referencedColumnName = "id")
    private File file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;
}
