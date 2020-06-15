package org.fastttrackit.onlineshop.persistance;

import org.fastttrackit.onlineshop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
