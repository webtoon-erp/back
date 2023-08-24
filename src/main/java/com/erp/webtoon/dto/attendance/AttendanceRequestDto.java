package com.erp.webtoon.dto.attendance;

import com.erp.webtoon.domain.Attendance;
import com.erp.webtoon.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRequestDto {

    @Builder.Default
    private int attendMonth = LocalDate.now().getMonthValue(); // 기준월

    @Builder.Default
    private String attendDate = LocalDate.now().toString();  //  기준일

    @NotBlank
    private String attendType;  // 근태타입

    @CreationTimestamp
    private LocalDateTime attendTime; // 시간

    @NotBlank
    private String employeeId;

    public Attendance toEntity(User user) {
        return Attendance.builder()
                .attendMonth(attendMonth)
                .attendDate(attendDate)
                .attendType(attendType)
                .attendTime(attendTime)
                .user(user)
                .build();
    }

}
