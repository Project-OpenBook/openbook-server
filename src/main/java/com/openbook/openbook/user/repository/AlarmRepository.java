package com.openbook.openbook.user.repository;


import com.openbook.openbook.user.entity.Alarm;
import com.openbook.openbook.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Slice<Alarm> findAllByReceiver(Pageable pageable, User receiver);

}
