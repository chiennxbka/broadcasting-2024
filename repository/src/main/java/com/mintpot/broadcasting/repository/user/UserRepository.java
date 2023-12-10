package com.mintpot.broadcasting.repository.user;

import com.mintpot.broadcasting.common.entities.User;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @ReadOnlyProperty
    Optional<User> findByUsername(String username);

    @ReadOnlyProperty
    Optional<User> findByEmail(String email);

    @ReadOnlyProperty
    Optional<User> findByPhone(String phone);

    boolean existsByUsername(String username);
}
