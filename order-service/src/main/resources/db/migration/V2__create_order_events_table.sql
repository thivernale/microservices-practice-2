CREATE SEQUENCE IF NOT EXISTS order_event_id_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE order_events
(
    id           BIGINT default nextval('order_event_id_seq') NOT NULL,
    order_number VARCHAR(255)                                 NOT NULL references orders (order_number),
    event_id     VARCHAR(255)                                 NOT NULL unique,
    type         VARCHAR(255)                                 NOT NULL,
    payload      text                                         NOT NULL,
    created_at   TIMESTAMP                                    NOT NULL,
    updated_at   TIMESTAMP,
    PRIMARY KEY (id)
);
