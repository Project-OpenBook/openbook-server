package com.openbook.openbook.event.repository;

import com.openbook.openbook.event.entity.EventTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventTagRepository extends JpaRepository<EventTag, Long> {
    @Query("SELECT t FROM EventTag t WHERE t.linkedEvent.id=:eventId")
    List<EventTag> findAllByLinkedEventId(Long eventId);

}
