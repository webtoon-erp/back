package com.erp.webtoon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(name = "user_id")
    private Long id;

    private String employeeId; // 사번

    @JsonIgnore
    private String password;    // 비밀번호

    private String name;    // 이름

    private String deptCode;    //부서코드

    private String deptName;    //부서명

    private int teamNum;    // 팀 번호

    private String position;    // 직급

    private String email;   // 이메일

    private String tel; // 전화번호

    private LocalDate birthDate;   // 생년월일

    private LocalDate joinDate;    // 입사날짜

    private String status; // 상태

    private boolean usable;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();     // 접근 권한 (로그인 시 설정 아마도,,)

    private int dayOff;     // 연차개수

    @OneToMany(mappedBy = "rcvUser", cascade = CascadeType.ALL)
    private List<Message> rcvMessages = new ArrayList<>();     // 메세지 수신 목록

    @OneToMany(mappedBy = "sendUser", cascade = CascadeType.ALL)
    private List<Message> sendMessages = new ArrayList<>();     // 메세지 발신 목록

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Attendance> attendances = new ArrayList<>();  // 근태 목록

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Pay> pays = new ArrayList<>();    // 유저의 급여 목록

    @OneToMany(mappedBy = "reqUser", cascade = CascadeType.ALL)
    private List<Request> requests = new ArrayList<>();     // 요청한 요청들

    @OneToMany(mappedBy = "itUser", cascade = CascadeType.ALL)
    private List<Request> itRequests = new ArrayList<>();   // 담당 요청들

    @OneToMany(mappedBy = "writeUser", cascade = CascadeType.ALL)
    private List<Document> docs = new ArrayList<>();     // 작성 문서 목록

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<DocumentRcv> documentRcvs = new ArrayList<>();     // 수신 문서들

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Qualification> qualifications = new ArrayList<>();     // 자격증들

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notice> notices = new ArrayList<>();   // 작성한 공지사항 목록

    @Builder
    public User(String employeeId, String password, String name, String deptCode, String deptName,int teamNum,
                String position, String email, String tel, LocalDate birthDate, LocalDate joinDate, int dayOff, boolean usable) {
        this.employeeId = employeeId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.tel = tel;
        this.birthDate = birthDate;
        this.deptName = deptName;
        this.deptCode = deptCode;
        this.teamNum = teamNum;
        this.position = position;
        this.joinDate = joinDate;
        this.dayOff = dayOff;
        this.usable = usable;
    }

    public void updateInfo(String password, String name, String deptCode, String deptName, int teamNum, String position, String email, String tel, LocalDate birthDate) {
        this.password = password;
        this.name = name;
        this.deptCode = deptCode;
        this.deptName = deptName;
        this.teamNum = teamNum;
        this.position = position;
        this.email = email;
        this.tel = tel;
        this.birthDate = birthDate;
    }

    public void updatePassword(String password){
        this.password = password;
    }

    public void registerQualification(List<Qualification> qualificationList){
        this.qualifications = qualificationList;
    }

    public void addRole(String role) {
        if(role != null) {
            roles.add(role);
        }
    }

    public void addDayoff() {
        this.dayOff++;
    }

    public void changeUsable() {
        this.usable = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return employeeId;
    }

    @Override
    public String getPassword() {
        return password;
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
