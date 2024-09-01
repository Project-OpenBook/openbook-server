package com.openbook.openbook.event.repository;

import com.openbook.openbook.event.entity.EventReview;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventReviewRepository extends JpaRepository<EventReview, Long> {

    Slice<EventReview> findByLinkedEventId(long eventId, Pageable pageable);
}
