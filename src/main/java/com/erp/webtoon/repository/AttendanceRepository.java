package com.erp.webtoon.repository;

import com.erp.webtoon.domain.Attendance;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.attendance.IndividualAttendanceListDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query("SELECT FUNCTION('WEEK', a1.attendDate, 3), a1.attendDate, a1.attendTime, a2.attendTime, " +
            "FUNCTION('SEC_TO_TIME', FUNCTION('TIMESTAMPDIFF', 'SECOND', a1.attendTime, a2.attendTime)) " +
            "FROM Attendance a1, Attendance a2 " +
            "WHERE a1.attendType = 'START' " +
            "AND a2.attendType = 'END' " +
            "AND a1.attendDate = a2.attendDate " +
            "AND a1.user = a2.user " +
            "AND FUNCTION('MONTH', a1.attendDate) = FUNCTION('MONTH', CURRENT_TIMESTAMP) " +
            "AND a1.user = :user")
    List<IndividualAttendanceListDto> findIndividualAttendance(@Param("user") User user);

    // 개인 이번주 누적 근무시간
    @Query(value = "SELECT SUM(TIMESTAMPDIFF(SECOND, START.attend_time, END.attend_time)) " +
            "FROM attendance START, attendance END " +
            "WHERE START.attend_date = END.attend_date " +
            "AND START.user_id = END.user_id " +
            "AND START.attend_type = 'START' " +
            "AND END.attend_type = 'END' " +
            "AND WEEK(NOW()) = WEEK(START.attend_date, 3) " +
            "AND START.user_id = :userId ", nativeQuery = true)
    Long findIndividualWeeklyTotalTime(@Param("userId") Long userId);

    // 개인 이번주 초과 근무시간
    @Query(value = "SELECT SUM(TIMESTAMPDIFF(SECOND, CONCAT(END.attend_date, ' 18:00:00'), END.attend_time)) " +
            "FROM attendance START, attendance END " +
            "WHERE START.attend_date = END.attend_date " +
            "AND START.user_id = END.user_id " +
            "AND START.attend_type = 'START' " +
            "AND END.attend_type = 'END' " +
            "AND WEEK(NOW()) = WEEK(START.attend_date, 3) " +
            "AND START.user_id = :userId ", nativeQuery = true)

    Long findIndividualWeeklyOverTime(@Param("userId") Long userId);

    // 개인 이번달 누적 근무시간
    @Query(value = "SELECT SUM(TIMESTAMPDIFF(SECOND, START.attend_time, END.attend_time)) " +
            "FROM attendance START, attendance END " +
            "WHERE START.attend_date = END.attend_date " +
            "AND START.user_id = END.user_id " +
            "AND START.attend_type = 'START' " +
            "AND END.attend_type = 'END' " +
            "AND MONTH(NOW()) = MONTH(START.attend_date) " +
            "AND START.user_id = :userId ", nativeQuery = true)

    Long findIndividualMonthlyTotalTime(@Param("userId") Long userId);

    // 개인 이번달 초과 근무시간
    @Query(value = "SELECT SUM(TIMESTAMPDIFF(SECOND, CONCAT(END.attend_date, ' 18:00:00'), END.attend_time)) " +
            "FROM attendance START, attendance END " +
            "WHERE START.attend_date = END.attend_date " +
            "AND START.user_id = END.user_id " +
            "AND START.attend_type = 'START' " +
            "AND END.attend_type = 'END' " +
            "AND MONTH(NOW()) = MONTH(START.attend_date) " +
            "AND START.user_id = :userId " , nativeQuery = true)

    Long findIndividualMonthlyOverTime(@Param("userId") Long userId);

    // 전체 근태 조회
    List<Attendance> findByAttendDateAndAttendType(String attendDate, String attendType);

    // 조건 : 기준일, 근태타입, 유저
    List<Attendance> findByAttendDateAndAttendTypeAndUser(String attendDate, String attendType, User user);

    // 조건 : 기준월, 근태타입
    List<Attendance> findByAttendMonthAndAttendType(int attendMonth, String attendType);

    // 조건 : 기준월, 근태 타입, User
    List<Attendance> findByAttendMonthAndAttendTypeAndUserIn(int attendMonth, String attendType, List<User> userList);

}
