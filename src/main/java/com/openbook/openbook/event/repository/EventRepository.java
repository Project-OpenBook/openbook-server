package com.openbook.openbook.event.repository;

import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.dto.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "SELECT * FROM event ORDER BY FIELD(status, 'WAITING', 'APPROVE', 'REJECT'), registered_at", nativeQuery = true)
    Page<Event> findAllRequested(Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.status=:status ORDER BY e.registeredAt")
    Page<Event> findAllRequestedByStatus(Pageable pageable, EventStatus status);

}
