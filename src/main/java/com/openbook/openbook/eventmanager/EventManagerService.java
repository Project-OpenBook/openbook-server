package com.openbook.openbook.eventmanager;

import com.openbook.openbook.booth.dto.BoothStatus;
import com.openbook.openbook.booth.repository.BoothRepository;
import com.openbook.openbook.eventmanager.dto.BoothMangeData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventManagerService {

    private final BoothRepository boothRepository;

    @Transactional(readOnly = true)
    public Page<BoothMangeData> getBoothMangeData(String status, Long eventId, Pageable pageable){
        if(status.equals("all")){
            return boothRepository.findAllBoothByEventId(pageable, eventId).map(BoothMangeData::of);
        }

        return boothRepository.findAllBoothByLinkedEventAndStatus(pageable, eventId, BoothStatus.valueOf(status)).map(BoothMangeData::of);
    }
}
