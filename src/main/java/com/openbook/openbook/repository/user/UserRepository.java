package com.openbook.openbook.repository.user;


import com.openbook.openbook.domain.user.dto.UserRole;
import com.openbook.openbook.domain.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByRole(UserRole role);
}
