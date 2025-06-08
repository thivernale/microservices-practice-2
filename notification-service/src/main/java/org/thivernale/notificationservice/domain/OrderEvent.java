package org.thivernale.notificationservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_events")
public class OrderEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_events_id_gen")
    @SequenceGenerator(name = "order_events_id_gen", sequenceName = "order_event_id_seq")
    private Long id;

    @Size(max = 255)
    @Column(nullable = false, unique = true)
    private String eventId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public OrderEvent(String eventId) {
        this.eventId = eventId;
    }
}
