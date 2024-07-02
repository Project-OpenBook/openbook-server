package com.openbook.openbook.booth.service;


import com.openbook.openbook.booth.dto.BoothDTO;
import com.openbook.openbook.booth.dto.BoothStatus;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.repository.BoothRepository;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.global.exception.OpenBookException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoothService {

    private final BoothRepository boothRepository;

    public Booth getBoothOrException(Long boothId){
        return boothRepository.findById(boothId).orElseThrow(() ->
                new OpenBookException(HttpStatus.NOT_FOUND, "부스 정보를 찾을 수 없습니다."));
    }

    public void createBooth(BoothDTO booth) {
        boothRepository.save(
          Booth.builder()
                  .linkedEvent(booth.linkedEvent())
                  .manager(booth.manager())
                  .name(booth.name())
                  .description(booth.description())
                  .mainImageUrl(booth.mainImageUrl())
                  .accountBankName(booth.accountBankName())
                  .accountNumber(booth.accountNumber())
                  .openTime(booth.openTime())
                  .closeTime(booth.closeTime())
                  .build()
        );
    }

    public Slice<Booth> getBoothsByStatus(Pageable pageable, BoothStatus status) {
        return boothRepository.findAllByStatus(pageable, status);
    }

    public int getBoothCountByEvent(Event event) {
        return boothRepository.countByLinkedEvent(event);
    }
}
