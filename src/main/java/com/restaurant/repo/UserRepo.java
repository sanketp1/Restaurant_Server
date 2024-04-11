package com.restaurant.repo;

import com.restaurant.entity.User;
import com.restaurant.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findFirstByEmail(String email);

    User findByUserRole(UserRole role);

}
