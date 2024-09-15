package com.openbook.openbook.service.user;


import com.openbook.openbook.api.user.request.BookmarkRequest;
import com.openbook.openbook.domain.user.Bookmark;
import com.openbook.openbook.domain.user.User;
import com.openbook.openbook.domain.user.dto.BookmarkType;
import com.openbook.openbook.repository.user.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final UserService userService;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public void createBookmark(long userId, BookmarkRequest request) {
        User user = userService.getUserOrException(userId);
        bookmarkRepository.save( Bookmark.builder()
                .user(user)
                .bookmarkType(BookmarkType.fromString(request.type()))
                .resourceId(request.resourceId())
                .alarmSet(request.alarmSet() == null || request.alarmSet())
                .build()
        );
    }

}
