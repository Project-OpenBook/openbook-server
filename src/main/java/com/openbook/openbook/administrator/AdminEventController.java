package com.openbook.openbook.administrator;


import com.openbook.openbook.administrator.dto.AdminEventData;
import com.openbook.openbook.administrator.dto.request.EventStatusUpdateRequest;
import com.openbook.openbook.global.dto.PageResponse;
import com.openbook.openbook.global.dto.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminEventController {

    private final AdminEventService adminEventService;

    @PreAuthorize("authentication.name == '1'")
    @GetMapping("/admin/events")
    public ResponseEntity<PageResponse<AdminEventData>> getEventPage(@RequestParam(defaultValue = "all") String status,
                                                                     @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(PageResponse.of(adminEventService.getRequestedEvents(pageable, status)));
    }

    @PreAuthorize("authentication.name == '1'")
    @PutMapping("/admin/events/{eventId}/status")
    public ResponseEntity<ResponseMessage> changeEventStatus (@PathVariable Long eventId,
                                                              @RequestBody EventStatusUpdateRequest request) {
        adminEventService.changeEventStatus(eventId, request.status());
        return ResponseEntity.ok(new ResponseMessage("행사 상태가 변경되었습니다."));
    }

}



