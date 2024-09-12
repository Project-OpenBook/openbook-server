package com.openbook.openbook.repository.booth;

import com.openbook.openbook.domain.booth.BoothNotice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoothNoticeRepository extends JpaRepository<BoothNotice, Long> {
    Slice<BoothNotice> findByLinkedBoothId(Long linkedBoothId, Pageable pageable);
}
