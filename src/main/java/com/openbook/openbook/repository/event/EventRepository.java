package com.openbook.openbook.repository.event;

import com.openbook.openbook.domain.event.Event;
import com.openbook.openbook.domain.event.dto.EventStatus;
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

    @Query("SELECT e FROM Event e WHERE e.status=:status ORDER BY e.registeredAt")
    Page<Event> findAllByStatus(Pageable pageable, EventStatus status);

    @Query("SELECT e FROM Event e WHERE e.manager.id=:managerId")
    Slice<Event> findAllByManagerId(Pageable pageable, Long managerId);

    @Query("SELECT e FROM Event e WHERE e.manager.id=:managerId AND e.status=:status")
    Slice<Event> findAllByManagerIdAndStatus(Pageable pageable, Long managerId, EventStatus status);

    @Query("SELECT e FROM Event e WHERE e.status = :status AND e.name LIKE %:name% ")
    Slice<Event> findAllByNameAndStatus(Pageable pageable, String name, EventStatus status);

    @Query("SELECT e FROM Event e WHERE e.status = 'APPROVE' AND current_date BETWEEN e.openDate AND e.closeDate")
    Slice<Event> findAllOngoing(Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.status = 'APPROVE' "
            + "AND current_date BETWEEN e.boothRecruitmentStartDate AND e.boothRecruitmentEndDate")
    Slice<Event> findAllRecruiting(Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.status = 'APPROVE' AND e.closeDate < current_date")
    Slice<Event> findAllTerminated(Pageable pageable);

}
