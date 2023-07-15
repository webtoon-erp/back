package com.erp.webtoon.service;

import com.erp.webtoon.domain.Attendence;
import com.erp.webtoon.domain.Message;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.attendece.AttendenceRequestDto;
import com.erp.webtoon.dto.message.MessageRequestDto;
import com.erp.webtoon.repository.AttendenceRepository;
import com.erp.webtoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AttendenceService {

    private final AttendenceRepository attendenceRepository;
    private final UserRepository userRepository;

    /*
        출근 & 퇴근
     */
    public void addAttendence(AttendenceRequestDto dto) throws IOException {

        User user = userRepository.findById(dto.getUser_id())
                .orElseThrow(() -> new EntityNotFoundException("해당 직원의 정보가 존재하지 않습니다."));

        Attendence attendence = dto.toEntity(user);
        attendenceRepository.save(attendence);

    }


}
