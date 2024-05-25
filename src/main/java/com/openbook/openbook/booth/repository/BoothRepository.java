package com.openbook.openbook.booth.repository;

import com.openbook.openbook.booth.dto.BoothStatus;
import com.openbook.openbook.booth.entity.Booth;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothRepository extends JpaRepository<Booth, Long> {
    @Query(value = "SELECT * FROM booth WHERE linked_event_id = :eventId ORDER BY FIELD(status, 'WAITING', 'APPROVE', 'REJECT'), registered_at", nativeQuery = true)
    Page<Booth> findAllBoothByEventId(Pageable pageable, @Param(value = "eventId") Long eventId);

    @Query(value = "SELECT b FROM Booth b WHERE b.linkedEvent.id = :eventId and b.status = :status ORDER BY b.registeredAt")
    Page<Booth> findAllBoothByLinkedEventAndStatus(Pageable pageable, Long eventId, BoothStatus boothStatus);
}
