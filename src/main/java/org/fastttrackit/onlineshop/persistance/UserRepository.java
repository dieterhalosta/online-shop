package org.fastttrackit.onlineshop.persistance;

import org.fastttrackit.onlineshop.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    //JPQL syntax
    @Query(value = "SELECT user FROM User user WHERE (:partialFirstName IS null OR user.firstName LIKE %:partialFirstName%) AND (:partialLastName IS null OR user.lastName >= :partialLastName)")
    Page<User> findByOptionalCriteria(String partialFirstName, String partialLastName, Pageable pageable);
}

