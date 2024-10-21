package com.openbook.openbook.api.user;


import com.openbook.openbook.api.ResponseMessage;
import com.openbook.openbook.api.SliceResponse;
import com.openbook.openbook.api.user.request.BookmarkRequest;
import com.openbook.openbook.api.user.response.BookmarkResponse;
import com.openbook.openbook.domain.user.dto.BookmarkType;
import com.openbook.openbook.service.user.BookmarkService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/bookmark")
    public Map<String, Boolean> bookmarked(Authentication authentication,
                                           @RequestParam(value = "type") BookmarkType type,
                                           @RequestParam(value = "resourceId") long resourceId) {
        Boolean bookmark = bookmarkService.isUserBookmark(Long.parseLong(authentication.getName()), type, resourceId);
        return Map.of("bookmark", bookmark);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/bookmark")
    public ResponseMessage bookmark(Authentication authentication,
                                    @Valid @RequestBody BookmarkRequest request) {
        bookmarkService.createBookmark(Long.parseLong(authentication.getName()), request);
        return new ResponseMessage("북마크에 성공했습니다.");
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/bookmark")
    public ResponseMessage bookmarkCancel(Authentication authentication,
                                          @Valid @RequestBody BookmarkRequest request) {
        bookmarkService.deleteBookmark(Long.parseLong(authentication.getName()), request);
        return new ResponseMessage("북마크 취소에 성공했습니다.");
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/bookmark-list")
    public SliceResponse<BookmarkResponse> findBookmarkList(Authentication authentication,
                                                            @RequestParam(value = "type") String request,
                                                            Pageable pageable) {
        return SliceResponse.of(bookmarkService
                .findBookmarkList(Long.parseLong(authentication.getName()), request, pageable)
                .map(BookmarkResponse::of)
        );
    }

}
