package com.openbook.openbook.repository.event;

import com.openbook.openbook.domain.event.EventLayout;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventLayoutRepository extends JpaRepository<EventLayout, Long> {
    Optional<EventLayout> findById(Long id);
}
