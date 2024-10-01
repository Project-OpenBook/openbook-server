package com.openbook.openbook.repository.event;

import com.openbook.openbook.domain.event.EventReview;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventReviewRepository extends JpaRepository<EventReview, Long> {

    boolean existsByReviewerIdAndLinkedEventId(Long reviewerId, Long linkedEventId);

    Slice<EventReview> findByLinkedEventId(long eventId, Pageable pageable);
}
