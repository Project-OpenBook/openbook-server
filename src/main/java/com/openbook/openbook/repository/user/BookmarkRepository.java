package com.openbook.openbook.repository.user;

import com.openbook.openbook.domain.user.Bookmark;
import com.openbook.openbook.domain.user.dto.BookmarkType;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByUserIdAndResourceIdAndBookmarkType(long userId, long resourceId, BookmarkType type);

    Optional<Bookmark> findByUserIdAndResourceIdAndBookmarkType(Long user_id, Long resourceId, BookmarkType bookmarkType);

    Slice<Bookmark> findAllByUserIdAndBookmarkType(long userId, BookmarkType type, Pageable pageable);

}
