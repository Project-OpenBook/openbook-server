package com.openbook.openbook.repository.user;


import com.openbook.openbook.domain.user.Alarm;
import com.openbook.openbook.domain.user.User;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    boolean existsByReceiverId(Long receiverId);
    Optional<Alarm> findById(Long id);
    Slice<Alarm> findAllByReceiver(Pageable pageable, User receiver);
    @Modifying
    @Query("DELETE FROM Alarm a WHERE a.receiver.id=:receiverId")
    void deleteAllByReceiverId(Long receiverId);

}
