package org.thivernale.orderservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.thivernale.orderservice.domain.models.OrderEventType;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "order_events")
class OrderEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_events_id_gen")
    @SequenceGenerator(name = "order_events_id_gen", sequenceName = "order_event_id_seq")
    private Long id;

    @Column(nullable = false)
    private String orderNumber;

    @Column(nullable = false, unique = true)
    private String eventId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    //@Convert(converter = OrderEventTypeConverter.class)
    private OrderEventType type;

    @Column(nullable = false)
    private String payload;

    /*@Column(nullable = false, columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private String payloadJsonb;*/

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private static class OrderEventTypeConverter implements AttributeConverter<OrderEventType, String> {
        @Override
        public String convertToDatabaseColumn(OrderEventType attribute) {
            return attribute.name();
        }

        @Override
        public OrderEventType convertToEntityAttribute(String dbData) {
            return OrderEventType.valueOf(dbData);
        }
    }
}
