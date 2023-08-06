package com.erp.webtoon.repository;

import com.erp.webtoon.domain.Attendence;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.attendece.IndividualAttenedenceListDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
    @Query(value = "SELECT SEC_TO_TIME(TIMESTAMPDIFF(SECOND, START.ATTEND_TIME, END.ATTEND_TIME)) " +
            "FROM ATTENDENCE START, ATTENDENCE END " +
            "WHERE START.ATTEND_DATE = END.ATTEND_DATE " +
            "AND START.USER_ID = END.USER_ID " +
            "AND START.ATTEND_TYPE = 'START' " +
            "AND END.ATTEND_TYPE = 'END' " +
            "AND WEEK(NOW()) = WEEK(START.ATTEND_DATE, 3) " +
            "AND START.USER_ID = :userId " +
            "GROUP BY WEEK(START.ATTEND_DATE, 3)", nativeQuery = true)

    String findIndividualWeeklyTotalTime(@Param("userId") Long userId);

    // 개인 이번주 초과 근무시간
    @Query(value = "SELECT SEC_TO_TIME(SUM(TIMESTAMPDIFF(SECOND, CONCAT(END.ATTEND_DATE, ' 18:00:00'), END.ATTEND_TIME))) " +
            "FROM ATTENDENCE START, ATTENDENCE END " +
            "WHERE START.ATTEND_DATE = END.ATTEND_DATE " +
            "AND START.USER_ID = END.USER_ID " +
            "AND START.ATTEND_TYPE = 'START' " +
            "AND END.ATTEND_TYPE = 'END' " +
            "AND WEEK(NOW()) = WEEK(START.ATTEND_DATE, 3) " +
            "AND START.USER_ID = :userId " +
            "GROUP BY WEEK(START.ATTEND_DATE, 3)", nativeQuery = true)

    String findIndividualWeeklyOverTime(@Param("user") Long userId);

    // 개인 이번달 누적 근무시간
    @Query(value = "SELECT SEC_TO_TIME(SUM(TIMESTAMPDIFF(SECOND, START.ATTEND_TIME, END.ATTEND_TIME))) " +
            "FROM ATTENDENCE START, ATTENDENCE END " +
            "WHERE START.ATTEND_DATE = END.ATTEND_DATE " +
            "AND START.USER_ID = END.USER_ID " +
            "AND START.ATTEND_TYPE = 'START' " +
            "AND END.ATTEND_TYPE = 'END' " +
            "AND MONTH(NOW()) = MONTH(START.ATTEND_DATE) " +
            "AND START.USER_ID = :userId " +
            "GROUP BY MONTH(START.ATTEND_DATE)", nativeQuery = true)

    String findIndividualMonthlyTotalTime(@Param("user") Long userId);

    // 개인 이번달 초과 근무시간
    @Query(value = "SELECT SEC_TO_TIME(SUM(TIMESTAMPDIFF(SECOND, CONCAT(END.ATTEND_DATE, ' 18:00:00'), END.ATTEND_TIME))) " +
            "FROM ATTENDENCE START, ATTENDENCE END " +
            "WHERE START.ATTEND_DATE = END.ATTEND_DATE " +
            "AND START.USER_ID = END.USER_ID " +
            "AND START.ATTEND_TYPE = 'START' " +
            "AND END.ATTEND_TYPE = 'END' " +
            "AND MONTH(NOW()) = MONTH(START.ATTEND_DATE) " +
            "AND START.USER_ID = :userId " +
            "GROUP BY MONTH(START.ATTEND_DATE)", nativeQuery = true)

    String findIndividualMonthlyOverTime(@Param("user") Long userId);




}
