package com.openbook.openbook.booth.repository;

import com.openbook.openbook.booth.entity.BoothReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothReviewRepository extends JpaRepository<BoothReview, Long> {
}
