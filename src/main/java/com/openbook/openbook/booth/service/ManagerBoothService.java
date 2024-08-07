package com.openbook.openbook.booth.service;

import com.openbook.openbook.booth.controller.response.BoothAreaData;
import com.openbook.openbook.booth.controller.response.BoothManageData;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.BoothArea;
import com.openbook.openbook.booth.entity.dto.BoothAreaStatus;
import com.openbook.openbook.booth.entity.dto.BoothStatus;
import com.openbook.openbook.booth.service.core.BoothAreaService;
import com.openbook.openbook.booth.service.core.BoothService;
import com.openbook.openbook.booth.service.core.BoothTagService;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.user.entity.User;
import com.openbook.openbook.user.service.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerBoothService {
    private final UserService userService;
    private final BoothService boothService;
    private final BoothTagService boothTagService;
    private final BoothAreaService boothAreaService;


    @Transactional(readOnly = true)
    public Slice<BoothManageData> getManagedBoothList(Long managerId, Pageable pageable, String status){
        userService.getUserOrException(managerId);
        Slice<Booth> booths = (status.equals("ALL"))
                ? boothService.getAllManagedBooths(pageable)
                : boothService.getAllManagedBoothsByStatus(pageable, managerId, BoothStatus.valueOf(status));

        return booths.map(booth -> {
            List<BoothAreaData> boothAreas = boothAreaService.getBoothAreasByBoothId(booth.getId())
                    .stream()
                    .map(BoothAreaData::of)
                    .collect(Collectors.toList());
            return BoothManageData.of(booth, boothAreas, boothTagService.getBoothTag(booth.getId()));
        });
    }

    @Transactional
    public void deleteBooth(Long userId, Long boothId){
        User user = userService.getUserOrException(userId);
        Booth booth = boothService.getBoothOrException(boothId);
        if(user != booth.getManager()){
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }

        List<BoothArea> boothAreaList = boothAreaService.getBoothAreasByBoothId(boothId);
        boothService.deleteBooth(booth);
        boothAreaService.disconnectBooth(boothAreaList);
    }
}
