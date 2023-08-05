package com.erp.webtoon.dto.attendece;

import com.erp.webtoon.domain.Attendence;
import com.erp.webtoon.domain.User;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
@Data
public class AttendenceRequestDto {

    @NotBlank
    private String attendDate;  //  기준일

    @NotBlank
    private String attendType;  // 근태타입

    @CreationTimestamp
    private LocalDateTime attendTime; // 시간

    @NotBlank
    private String employeeId;

    public Attendence toEntity(User user) {
        return Attendence.builder()
                .attendDate(attendDate)
                .attendType(attendType)
                .attendTime(attendTime)
                .user(user)
                .build();
    }

}
