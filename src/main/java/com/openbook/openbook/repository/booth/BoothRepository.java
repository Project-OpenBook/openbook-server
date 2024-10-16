package com.openbook.openbook.repository.booth;

import com.openbook.openbook.domain.booth.dto.BoothStatus;
import com.openbook.openbook.domain.booth.Booth;
import com.openbook.openbook.domain.event.Event;
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
    Slice<Booth> findAllByStatus(Pageable pageable, BoothStatus boothStatus);

    Slice<Booth> findAllByLinkedEventIdAndStatus(Pageable pageable, Long eventId, BoothStatus boothStatus);

    int countByLinkedEvent(Event linkedEvent);

    @Query("SELECT b FROM Booth b WHERE b.name LIKE %:boothName% AND b.status =:boothStatus")
    Slice<Booth> findAllByNameAndStatus(Pageable pageable, String boothName, BoothStatus boothStatus);

    @Query("SELECT b FROM Booth b WHERE b.manager.id=:managerId AND b.status=:boothStatus")
    Slice<Booth> findAllByManagerIdAndStatus(Pageable pageable, Long managerId, BoothStatus boothStatus);

    @Query("SELECT b FROM Booth b WHERE b.manager.id=:managerId")
    Slice<Booth> findAllByManagerId(Pageable pageable, Long managerId);
}