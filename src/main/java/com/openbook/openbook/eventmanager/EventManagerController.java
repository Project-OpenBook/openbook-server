package com.openbook.openbook.eventmanager;

import com.openbook.openbook.eventmanager.dto.BoothMangeData;
import com.openbook.openbook.global.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventManagerController {

    private final EventManagerService eventManagerService;
    @GetMapping("/events/{eventId}/managed/booths")
    public ResponseEntity<PageResponse<BoothMangeData>> getBoothManagePage(@RequestParam(defaultValue = "all") String status,
                                                                           @PathVariable Long eventId,
                                                                           @PageableDefault(size = 10) Pageable pageable){
        return ResponseEntity.ok(PageResponse.of(eventManagerService.getBoothMangeData(status, eventId, pageable)));
    }
}
