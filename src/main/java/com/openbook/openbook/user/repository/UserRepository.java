package com.openbook.openbook.user.repository;


import com.openbook.openbook.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
}
