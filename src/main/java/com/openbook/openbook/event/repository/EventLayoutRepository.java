package com.openbook.openbook.event.repository;

import com.openbook.openbook.event.entity.EventLayout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventLayoutRepository extends JpaRepository<EventLayout, Long> {
}
