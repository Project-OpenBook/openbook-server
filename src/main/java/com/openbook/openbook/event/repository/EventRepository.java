package com.openbook.openbook.event.repository;

import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.dto.EventStatus;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findById(Long id);

    @Query(value = "SELECT * FROM event ORDER BY FIELD(status, 'WAITING', 'APPROVE', 'REJECT'), registered_at", nativeQuery = true)
    Page<Event> findAllRequested(Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.status=:status ORDER BY e.registeredAt")
    Page<Event> findAllRequestedByStatus(Pageable pageable, EventStatus status);

    // USER
    @Query("SELECT e FROM Event e WHERE e.status = 'APPROVE'")
    Slice<Event> findAllApproved(Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.status = 'APPROVE' AND current_date BETWEEN e.openDate AND e.closeDate")
    Slice<Event> findAllOngoing(Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.status = 'APPROVE' "
            + "AND current_date BETWEEN e.boothRecruitmentStartDate AND e.boothRecruitmentEndDate")
    Slice<Event> findAllRecruiting(Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.status = 'APPROVE' AND e.closeDate <= current_date")
    Slice<Event> findAllTerminated(Pageable pageable);

}
