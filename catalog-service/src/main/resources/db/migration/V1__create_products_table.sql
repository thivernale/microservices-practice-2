CREATE SEQUENCE product_id_seq START WITH 1 increment by 50;

CREATE TABLE products
(
    id          bigint default nextval('product_id_seq') not null,
    code        varchar(255) unique                       not null,
    name        varchar(255)                              not null,
    description text,
    image_url   text,
    price       numeric                                   not null,
    primary key (id)
);
