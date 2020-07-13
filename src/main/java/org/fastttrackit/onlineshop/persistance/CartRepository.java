package org.fastttrackit.onlineshop.persistance;

import org.fastttrackit.onlineshop.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
