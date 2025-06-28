package org.thivernale.catalogservice.web.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thivernale.catalogservice.domain.PagedResult;
import org.thivernale.catalogservice.domain.ProductDto;
import org.thivernale.catalogservice.domain.ProductNotFoundException;
import org.thivernale.catalogservice.domain.ProductService;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "API for browsing product catalog")
class ProductController {
    private final ProductService productService;
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @GetMapping
    PagedResult<ProductDto> getProducts(@RequestParam(name = "pageNo", defaultValue = "1") int pageNo) {
        logger.info("Getting Products for page {}", pageNo);
        return productService.getProducts(pageNo);
    }

    @GetMapping("/{code}")
    ResponseEntity<ProductDto> findByCode(@PathVariable("code") String code) {
        return productService.findByCode(code)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> ProductNotFoundException.forCode(code));
    }
}
