package com.openbook.openbook.event.repository;

import com.openbook.openbook.event.entity.EventReviewImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventReviewImageRepository extends JpaRepository<EventReviewImage, Long> {

    @Query("SELECT i FROM EventReviewImage i WHERE i.linkedReview.id=:linkedReviewId ORDER BY i.imageOrder")
    List<EventReviewImage> findAllByLinkedReviewId(Long linkedReviewId);
}
