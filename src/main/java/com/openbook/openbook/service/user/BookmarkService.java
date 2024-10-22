package com.openbook.openbook.service.user;


import com.openbook.openbook.api.user.request.BookmarkRequest;
import com.openbook.openbook.domain.user.Bookmark;
import com.openbook.openbook.domain.user.User;
import com.openbook.openbook.domain.user.dto.BookmarkType;
import com.openbook.openbook.exception.ErrorCode;
import com.openbook.openbook.exception.OpenBookException;
import com.openbook.openbook.repository.user.BookmarkRepository;
import com.openbook.openbook.service.booth.BoothService;
import com.openbook.openbook.service.event.EventService;
import com.openbook.openbook.service.user.dto.BookmarkDto;
import java.awt.print.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final UserService userService;
    private final EventService eventService;
    private final BoothService boothService;

    private final BookmarkRepository bookmarkRepository;

    public boolean isUserBookmark(long userId, BookmarkType type, long resourceId) {
        return bookmarkRepository.existsByUserIdAndResourceIdAndBookmarkType(userId, resourceId, type);
    }

    @Transactional
    public void createBookmark(long userId, BookmarkRequest request) {
        User user = userService.getUserOrException(userId);
        BookmarkType type = BookmarkType.fromString(request.type());
        if(isUserBookmark(userId, type, request.resourceId())) {
            throw new OpenBookException(ErrorCode.ALREADY_BOOKMARK);
        }
        bookmarkRepository.save( Bookmark.builder()
                .user(user)
                .bookmarkType(type)
                .resourceId(request.resourceId())
                .alarmSet(request.alarmSet() == null || request.alarmSet())
                .build()
        );
    }

    @Transactional(readOnly = true)
    public Slice<BookmarkDto> findBookmarkList(long userId, String request, Pageable pageable) {
        userService.getUserOrException(userId);
        BookmarkType type = BookmarkType.fromString(request);
        return bookmarkRepository.findAllByUserIdAndBookmarkType(userId, type, pageable).map(bookmark -> {
            if (type==BookmarkType.EVENT) {
                return BookmarkDto.of(bookmark, eventService.getEventById(bookmark.getResourceId()), null);
            } else {
                return BookmarkDto.of(bookmark, null, boothService.getBoothById(bookmark.getResourceId()));
            }
        });
    }

    @Transactional
    public void deleteBookmark(long userId, BookmarkRequest request) {
        Bookmark bookmark = bookmarkRepository.findByUserIdAndResourceIdAndBookmarkType(
                userId, request.resourceId(), BookmarkType.fromString(request.type())).orElseThrow(()->
                new OpenBookException(ErrorCode.BOOKMARK_NOT_FOUND)
        );
        bookmarkRepository.delete(bookmark);
    }

}
