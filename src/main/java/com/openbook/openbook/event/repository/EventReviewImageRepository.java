package com.openbook.openbook.event.repository;

import com.openbook.openbook.event.entity.EventReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventReviewImageRepository extends JpaRepository<EventReviewImage, Long> {
}
