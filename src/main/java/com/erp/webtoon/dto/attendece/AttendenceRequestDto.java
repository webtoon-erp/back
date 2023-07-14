package com.erp.webtoon.dto.attendece;

import com.erp.webtoon.domain.Attendence;
import com.erp.webtoon.domain.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
@Data
public class AttendenceRequestDto {

    @NotBlank
    String attendDate;  //  기준일

    @NotBlank
    String attendType;  // 근태타입

    LocalDateTime attendTime; // 시간

    @NotBlank
    Long user_id;

    public Attendence toEntity(User user) {
        return Attendence.builder()
                .attendDate(attendDate)
                .attendType(attendType)
                .attendTime(attendTime)
                .user(user)
                .build();
    }

}
