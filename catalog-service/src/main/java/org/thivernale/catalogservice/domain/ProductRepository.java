package org.thivernale.catalogservice.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByCode(String code);
}
