package com.openbook.openbook.booth.controller;

import com.openbook.openbook.booth.controller.request.BoothRegistrationRequest;
import com.openbook.openbook.booth.controller.response.BoothBasicData;
import com.openbook.openbook.booth.controller.response.BoothDetail;
import com.openbook.openbook.booth.service.UserBoothService;
import com.openbook.openbook.global.dto.ResponseMessage;
import com.openbook.openbook.global.dto.SliceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booths")
public class UserBoothController {

    private final UserBoothService userBoothService;
    @PostMapping
    public ResponseEntity <ResponseMessage>  registration(Authentication authentication, @Valid BoothRegistrationRequest request){
        userBoothService.boothRegistration(Long.valueOf(authentication.getName()), request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseMessage("신청 완료 되었습니다."));

    }

    @GetMapping
    public ResponseEntity<SliceResponse<BoothBasicData>> getBooths(@PageableDefault(size = 6)Pageable pageable){
        return ResponseEntity.ok(SliceResponse.of(userBoothService.getBoothBasicData(pageable)));
    }

    @GetMapping("/{boothId}")
    public ResponseEntity<BoothDetail> getBooth(Authentication authentication, @PathVariable Long boothId){
        return ResponseEntity.ok(userBoothService.getBoothDetail(Long.valueOf(authentication.getName()), boothId));
    }

    @GetMapping("/search")
    public ResponseEntity<SliceResponse<BoothBasicData>> searchBoothName(@PageableDefault(size = 6) Pageable pageable,
                                                                         @RequestParam(value = "type") String searchType,
                                                                         @RequestParam(value = "query") String query){
        return ResponseEntity.ok(SliceResponse.of(userBoothService.searchBoothBy(pageable, searchType, query)));
    }
}
