truncate table orders cascade;
alter sequence order_id_seq restart with 100;
alter sequence order_item_id_seq restart with 100;

insert into public.orders (id, order_number, username, customer_name, customer_email, customer_phone,
                           delivery_address_line1, delivery_address_line2, delivery_address_city,
                           delivery_address_state, delivery_address_zip_code, delivery_address_country, status,
                           comments, created_at, updated_at)
values (2, '5b36246b-e7a1-431f-99db-08daafe9bc5b', 'username', 'User TEST', 'user@example.com', '0000000000', '111',
        null, 'Aberdeen', 'WA', '00', 'US', 'NEW', null, null, null),
       (3, 'aecceefc-dbb0-4653-9021-39195f658065', 'username', 'User TEST2', 'user2@example.com', '0000000000', '222',
        null, 'Aberdeen', 'WA', '002', 'US', 'NEW', null, null, null);
insert into public.order_items (id, code, name, price, quantity, order_id)
values (52, 'book_004', 'Book 4', 0, 1, 2),
       (53, 'book_003', 'Book 3', 0, 1, 2),
       (54, 'book_003', 'Book 3', 2, 2, 3),
       (55, 'book_004', 'Book 4', 0, 1, 3);
