package org.thivernale.orderservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.thivernale.orderservice.domain.models.Address;
import org.thivernale.orderservice.domain.models.Customer;
import org.thivernale.orderservice.domain.models.OrderStatus;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_id_gen")
    @SequenceGenerator(name = "orders_id_gen", sequenceName = "order_id_seq")
    private Long id;

    @Column(name = "order_number", nullable = false)
    private String orderNumber;

    @Column(name = "username", nullable = false)
    private String username;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "customer_name")),
        @AttributeOverride(name = "email", column = @Column(name = "customer_email")),
        @AttributeOverride(name = "phone", column = @Column(name = "customer_phone")),
    })
    private Customer customer;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "line1", column = @Column(name = "delivery_address_line1")),
        @AttributeOverride(name = "line2", column = @Column(name = "delivery_address_line2")),
        @AttributeOverride(name = "city", column = @Column(name = "delivery_address_city")),
        @AttributeOverride(name = "state", column = @Column(name = "delivery_address_state")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "delivery_address_zip_code")),
        @AttributeOverride(name = "country", column = @Column(name = "delivery_address_country")),
    })
    private Address deliveryAddress;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String comments;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems = new LinkedHashSet<>();
}
