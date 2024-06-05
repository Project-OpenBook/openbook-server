package com.openbook.openbook.booth.repository;

import com.openbook.openbook.booth.dto.BoothStatus;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.event.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface BoothRepository extends JpaRepository<Booth, Long> {
    @Query(value = "SELECT * FROM booth where linked_event_id =:eventId ORDER BY FIELD(status, 'WAITING', 'APPROVE', 'REJECT'), registered_at", nativeQuery = true)
    Page<Booth> findAllBoothByEventId(Pageable pageable, @Param(value = "eventId") Long eventId);

    @Query(value = "SELECT b FROM Booth b where b.linkedEvent.id =:eventId and b.status =:boothStatus ORDER BY b.registeredAt")
    Page<Booth> findAllBoothByEventIdAndStatus(Pageable pageable, Long eventId, BoothStatus boothStatus);

    @Query("SELECT b FROM Booth b WHERE b.status=:boothStatus")
    Slice<Booth> findAllByStatus(BoothStatus boothStatus, Pageable pageable);
    
    int countByLinkedEvent(Event linkedEvent);
}