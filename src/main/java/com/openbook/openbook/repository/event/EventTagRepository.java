package com.openbook.openbook.repository.event;

import com.openbook.openbook.domain.event.Event;
import com.openbook.openbook.domain.event.EventTag;
import com.openbook.openbook.domain.event.dto.EventStatus;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventTagRepository extends JpaRepository<EventTag, Long> {
    @Query("SELECT t FROM EventTag t WHERE t.linkedEvent.id=:eventId")
    List<EventTag> findAllByLinkedEventId(Long eventId);

    @Query("SELECT distinct t.linkedEvent FROM EventTag t WHERE t.name LIKE %:name% AND t.linkedEvent.status=:status")
    Slice<Event> findLinkedEventsByNameAndEventStatus(Pageable pageable, String name, EventStatus status);

}
