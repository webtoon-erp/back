package com.erp.webtoon.dto.user;

import com.erp.webtoon.domain.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class UserResponseDto {

    private String employeeId; // 사번

    private String name;    // 이름

    private String email;   // 이메일

    private String tel; // 전화번호

    private LocalDate birthDate;   // 생년월일

    private String deptName;    //부서명

    private int teamNum;    // 팀 번호

    private String position;    // 직급

    private LocalDate joinDate;    // 입사날짜

    private int dayOff;     // 연차개수

    private String photo;

    private List<QualificationResponseDto> qualifications;     // 자격증들

    public static UserResponseDto of(User findUser, List<QualificationResponseDto> qualificationList) {

        if (findUser.getFile() == null){
            return UserResponseDto.builder()
                    .employeeId(findUser.getEmployeeId())
                    .name(findUser.getName())
                    .email(findUser.getEmail())
                    .tel(findUser.getTel())
                    .birthDate(findUser.getBirthDate())
                    .deptName(findUser.getDeptName())
                    .teamNum(findUser.getTeamNum())
                    .position(findUser.getPosition())
                    .joinDate(findUser.getJoinDate())
                    .dayOff(findUser.getDayOff())
                    .photo(null)
                    .qualifications(qualificationList)
                    .build();
        }
        return UserResponseDto.builder()
                .employeeId(findUser.getEmployeeId())
                .name(findUser.getName())
                .email(findUser.getEmail())
                .tel(findUser.getTel())
                .birthDate(findUser.getBirthDate())
                .deptName(findUser.getDeptName())
                .teamNum(findUser.getTeamNum())
                .position(findUser.getPosition())
                .joinDate(findUser.getJoinDate())
                .dayOff(findUser.getDayOff())
                .photo(findUser.getFile().getFileName())
                .qualifications(qualificationList)
                .build();
    }
}
