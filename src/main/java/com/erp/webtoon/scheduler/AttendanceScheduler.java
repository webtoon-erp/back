package com.erp.webtoon.scheduler;

import com.erp.webtoon.service.AttendanceService;
import com.erp.webtoon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttendanceScheduler {

    private final AttendanceService attendanceService;
    private final UserService userService;

    @Scheduled(cron = "0 0 8 ? * MON-FRI") // 주중 8시에 실행
    public void addDayOffData() {
        userService.reduceDayOff(attendanceService.addDayOffData());
    }

}
