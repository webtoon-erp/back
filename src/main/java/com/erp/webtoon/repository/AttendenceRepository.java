package com.erp.webtoon.repository;

import com.erp.webtoon.domain.Attendence;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.attendece.IndividualAttenedenceListDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendenceRepository extends JpaRepository<Attendence, Long> {

    @Query("SELECT FUNCTION('WEEK', a1.attendDate, 3), a1.attendDate, a1.attendTime, a2.attendTime, " +
            "FUNCTION('SEC_TO_TIME', FUNCTION('TIMESTAMPDIFF', 'SECOND', a1.attendTime, a2.attendTime)) " +
            "FROM Attendence a1, Attendence a2 " +
            "WHERE a1.attendType = 'START' " +
            "AND a2.attendType = 'END' " +
            "AND a1.attendDate = a2.attendDate " +
            "AND a1.user = a2.user " +
            "AND FUNCTION('MONTH', a1.attendDate) = FUNCTION('MONTH', CURRENT_TIMESTAMP) " +
            "AND a1.user = :user")

    List<IndividualAttenedenceListDto> findIndividualAttendence(@Param("user") User user);

    // 개인 이번주 누적 근무시간
    @Query(value = "SELECT SEC_TO_TIME(TIMESTAMPDIFF(SECOND, START.attend_time, END.attend_time)) " +
            "FROM attendence START, attendence END " +
            "WHERE START.attend_date = END.attend_date " +
            "AND START.user_id = END.user_id " +
            "AND START.attend_type = 'START' " +
            "AND END.attend_type = 'END' " +
            "AND WEEK(NOW()) = WEEK(START.attend_date, 3) " +
            "AND START.user_id = :userId " +
            "GROUP BY WEEK(START.attend_date, 3)", nativeQuery = true)

    String findIndividualWeeklyTotalTime(@Param("userId") Long userId);

    // 개인 이번주 초과 근무시간
    @Query(value = "SELECT SEC_TO_TIME(SUM(TIMESTAMPDIFF(SECOND, CONCAT(END.attend_date, ' 18:00:00'), END.attend_time))) " +
            "FROM attendence START, attendence END " +
            "WHERE START.attend_date = END.attend_date " +
            "AND START.user_id = END.user_id " +
            "AND START.attend_type = 'START' " +
            "AND END.attend_type = 'END' " +
            "AND WEEK(NOW()) = WEEK(START.attend_date, 3) " +
            "AND START.user_id = :userId " +
            "GROUP BY WEEK(START.attend_date, 3)", nativeQuery = true)

    String findIndividualWeeklyOverTime(@Param("user") Long userId);

    // 개인 이번달 누적 근무시간
    @Query(value = "SELECT SEC_TO_TIME(SUM(TIMESTAMPDIFF(SECOND, START.attend_time, END.attend_time))) " +
            "FROM attendence START, attendence END " +
            "WHERE START.attend_date = END.attend_date " +
            "AND START.user_id = END.user_id " +
            "AND START.attend_type = 'START' " +
            "AND END.attend_type = 'END' " +
            "AND MONTH(NOW()) = MONTH(START.attend_date) " +
            "AND START.user_id = :userId " +
            "GROUP BY MONTH(START.attend_date)", nativeQuery = true)

    String findIndividualMonthlyTotalTime(@Param("user") Long userId);

    // 개인 이번달 초과 근무시간
    @Query(value = "SELECT SEC_TO_TIME(SUM(TIMESTAMPDIFF(SECOND, CONCAT(END.attend_date, ' 18:00:00'), END.attend_time))) " +
            "FROM attendence START, attendence END " +
            "WHERE START.attend_date = END.attend_date " +
            "AND START.user_id = END.user_id " +
            "AND START.attend_type = 'START' " +
            "AND END.attend_type = 'END' " +
            "AND MONTH(NOW()) = MONTH(START.attend_date) " +
            "AND START.user_id = :userId " +
            "GROUP BY MONTH(START.attend_date)", nativeQuery = true)

    String findIndividualMonthlyOverTime(@Param("user") Long userId);

    // 전체 근태 조회
    List<Attendence> findByAttendDateAndAttendType(String attendDate, String attendType);

    // 조건 : 기준월, 근태 타입, User
    List<Attendence> findByAttendMonthAndAttendTypeAndUserIn(int attendMonth, String attendType, List<User> userList);


}
