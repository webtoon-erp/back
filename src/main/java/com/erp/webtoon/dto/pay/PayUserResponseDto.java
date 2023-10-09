package com.erp.webtoon.dto.pay;

import com.erp.webtoon.domain.User;
import lombok.Data;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

@Data
public class PayUserResponseDto {
    private String employeeId; // 사번

    private String name;    // 이름

    private String email;   // 이메일

    private String deptName;    //부서명

    private String position;    // 직급

    private LocalDate joinDate; // 입사

    private String tel; // 전화번호

    private URL photo;

    public PayUserResponseDto(User user, String photo) throws MalformedURLException {
        this.employeeId = user.getEmployeeId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.deptName = user.getDeptName();
        this.position = user.getPosition();
        this.joinDate = user.getJoinDate();
        this.tel = user.getTel();
        this.photo = new UrlResource("http://146.56.98.153:8080" + photo).getURL();
    }
}
