package com.openbook.openbook.booth.repository;

import com.openbook.openbook.booth.entity.BoothNotice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoothNoticeRepository extends JpaRepository<BoothNotice, Long> {
    Slice<BoothNotice> findByLinkedBoothIdOrderByRegisteredAtDesc(Long linkedBoothId, Pageable pageable);
}
