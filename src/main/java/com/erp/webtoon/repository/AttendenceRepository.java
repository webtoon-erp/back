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

}
