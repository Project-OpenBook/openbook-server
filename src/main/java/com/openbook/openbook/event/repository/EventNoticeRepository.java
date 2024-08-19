package com.openbook.openbook.event.repository;

import com.openbook.openbook.event.entity.EventNotice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EventNoticeRepository extends JpaRepository<EventNotice, Long> {

    Slice<EventNotice> findByLinkedEventId(Long linkedEventId, Pageable pageable);

}
