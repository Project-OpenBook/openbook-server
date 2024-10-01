package com.openbook.openbook.repository.booth;

import com.openbook.openbook.domain.booth.BoothReview;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothReviewRepository extends JpaRepository<BoothReview, Long> {

    boolean existsByReviewerIdAndLinkedBoothId(Long reviewerId, Long linkedBoothId);

    Slice<BoothReview> findBoothReviewsByLinkedBoothId(long boothId, Pageable pageable);
}
