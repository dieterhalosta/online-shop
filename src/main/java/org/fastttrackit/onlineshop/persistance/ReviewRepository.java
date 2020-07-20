package org.fastttrackit.onlineshop.persistance;

import org.fastttrackit.onlineshop.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {


    // Queries by nested properties
    Page<Review> findByProductId(long productId, Pageable pageable);
}
