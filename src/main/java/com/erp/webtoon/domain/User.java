package com.erp.webtoon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(name = "user_id")
    private Long id;

    private String employeeId; // 사번

    private String loginId; // 아이디

    @JsonIgnore
    private String password;    // 비밀번호

    private String name;    // 이름

    private String deptCode;    //부서코드

    private String deptName;    //부서명

    private int teamNum;    // 팀 번호

    private String position;    // 직급

    private String email;   // 이메일

    private String tel; // 전화번호

    private String birthDate;   // 생년월일

    private String joinDate;    // 입사날짜

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();     // 접근 권한 (로그인 시 설정 아마도,,)

    private int dayOff;     // 연차개수

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();     // 메세지 목록

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Attendence> attendences  = new ArrayList<>();  // 근태 목록

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Pay> pays = new ArrayList<>();    // 유저의 급여 목록

    @OneToMany(mappedBy = "reqUser", cascade = CascadeType.ALL)
    private List<Request> requests = new ArrayList<>();     // 요청한 요청들

    @OneToMany(mappedBy = "itUser", cascade = CascadeType.ALL)
    private List<Request> itRequests = new ArrayList<>();   // 담당 요청들

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<DocumentRcv> documentRcvs = new ArrayList<>();     // 수신 문서들

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private RefreshToken refreshToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Qualification> qualifications = new ArrayList<>();     // 자격증들

    public void updateInfo(String loginId, String password, String name, String deptCode, String deptName, int teamNum, String position, String email, String tel, String birthDate
            , int dayOff) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.deptCode = deptCode;
        this.deptName = deptName;
        this.teamNum = teamNum;
        this.position = position;
        this.email = email;
        this.tel = tel;
        this.birthDate = birthDate;
        this.dayOff = dayOff;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
