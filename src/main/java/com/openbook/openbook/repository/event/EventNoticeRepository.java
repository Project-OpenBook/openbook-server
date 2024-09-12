package com.openbook.openbook.repository.event;

import com.openbook.openbook.domain.event.EventNotice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EventNoticeRepository extends JpaRepository<EventNotice, Long> {

    Slice<EventNotice> findByLinkedEventId(Long linkedEventId, Pageable pageable);

}
