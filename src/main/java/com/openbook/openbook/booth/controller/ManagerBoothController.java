package com.openbook.openbook.booth.controller;

import com.openbook.openbook.booth.controller.response.BoothManageData;
import com.openbook.openbook.booth.service.ManagerBoothService;
import com.openbook.openbook.global.dto.SliceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ManagerBoothController {
    private final ManagerBoothService managerBoothService;

    @GetMapping("manage/booths")
    public ResponseEntity<SliceResponse<BoothManageData>> getManagedBooth(Authentication authentication,
                                                                          @PageableDefault(size = 6)Pageable pageable,
                                                                          @RequestParam(defaultValue = "ALL") String status){
        return ResponseEntity.ok(SliceResponse.of(managerBoothService.getManagedBoothList(Long.valueOf(authentication.getName()), pageable, status)));
    }
}
