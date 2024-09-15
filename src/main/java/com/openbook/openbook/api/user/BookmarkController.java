package com.openbook.openbook.api.user;


import com.openbook.openbook.api.ResponseMessage;
import com.openbook.openbook.api.user.request.BookmarkRequest;
import com.openbook.openbook.service.user.BookmarkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/bookmark")
    public ResponseMessage bookmark(Authentication authentication,
                                    @Valid @RequestBody BookmarkRequest request) {
        bookmarkService.createBookmark(Long.parseLong(authentication.getName()), request);
        return new ResponseMessage("북마크에 성공했습니다.");
    }

}
