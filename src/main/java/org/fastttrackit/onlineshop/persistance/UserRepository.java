package org.fastttrackit.onlineshop.persistance;

import org.fastttrackit.onlineshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
