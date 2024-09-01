package com.openbook.openbook.event.repository;

import com.openbook.openbook.event.entity.EventReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventReviewRepository extends JpaRepository<EventReview, Long> {
}
