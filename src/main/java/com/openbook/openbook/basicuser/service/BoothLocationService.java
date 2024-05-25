package com.openbook.openbook.basicuser.service;

import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.BoothLocation;
import com.openbook.openbook.booth.repository.BoothLocationRepository;
import com.openbook.openbook.event.entity.EventLayoutArea;
import com.openbook.openbook.event.dto.EventLayoutAreaStatus;
import com.openbook.openbook.event.repository.EventLayoutAreaRepository;
import com.openbook.openbook.global.exception.OpenBookException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoothLocationService {
    private final EventLayoutAreaRepository eventLayoutAreaRepository;
    private final BoothLocationRepository boothLocationRepository;

    @Transactional
    public void boothLocationApplication(List<Long> locations){
        for(Long layoutId : locations){
            Optional<EventLayoutArea> eventLayoutArea = eventLayoutAreaRepository.findById(layoutId);
            EventLayoutArea layoutArea = eventLayoutArea.get();
            if(layoutArea.getStatus().equals(EventLayoutAreaStatus.EMPTY)){
                layoutArea.updateStatus(EventLayoutAreaStatus.WAITING);
            }else{
                throw new OpenBookException(HttpStatus.INTERNAL_SERVER_ERROR, "이미 예약 된 자리 입니다.");
            }
        }
    }

    @Transactional
    public void createBoothLocation(Booth booth, List<Long> locations){
        for(Long layoutId : locations){
            Optional<EventLayoutArea> eventLayoutArea = eventLayoutAreaRepository.findById(layoutId);
            EventLayoutArea layoutArea = eventLayoutArea.get();
            if(layoutArea.getStatus().equals(EventLayoutAreaStatus.EMPTY)){
                BoothLocation boothLocation = BoothLocation.builder()
                        .booth(booth)
                        .eventLayoutArea(layoutArea)
                        .build();
                boothLocationRepository.save(boothLocation);
            }else{
                throw new OpenBookException(HttpStatus.INTERNAL_SERVER_ERROR, "이미 예약 된 자리 입니다.");
            }
        }
    }
}