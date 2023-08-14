package com.erp.webtoon.service;

import com.erp.webtoon.domain.LogoutAccessToken;
import com.erp.webtoon.domain.RefreshToken;
import com.erp.webtoon.dto.token.LogoutResponseDto;
import com.erp.webtoon.token.LogoutAccessTokenService;
import com.erp.webtoon.token.RefreshTokenService;
import com.erp.webtoon.token.TokenProvider;
import com.erp.webtoon.domain.Qualification;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.token.TokenResponseDto;
import com.erp.webtoon.dto.user.QualificationRequestDto;
import com.erp.webtoon.dto.user.QualificationResponseDto;
import com.erp.webtoon.dto.user.RegisterQualificationResponse;
import com.erp.webtoon.dto.user.SlackRequestDto;
import com.erp.webtoon.dto.user.UserListResponseDto;
import com.erp.webtoon.dto.user.UserRequestDto;
import com.erp.webtoon.dto.user.UserResponseDto;
import com.erp.webtoon.dto.user.UserUpdateDto;
import com.erp.webtoon.repository.QualificationRepository;
import com.erp.webtoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final QualificationRepository qualificationRepository;
    private final RefreshTokenService refreshTokenService;
    private final LogoutAccessTokenService logoutAccessTokenService;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final SlackService slackService;

    /**
     * 신규 직원 추가
     */

    @Transactional
    public void addNewCome(UserRequestDto userRequestDto){
        User user = User.builder()
                .employeeId(userRequestDto.getEmployeeId())
                .password(passwordEncoder.encode(userRequestDto.getPassword()))
                .name(userRequestDto.getName())
                .email(userRequestDto.getEmail())
                .tel(userRequestDto.getTel())
                .birthDate(userRequestDto.getBirthDate())
                .deptName(userRequestDto.getDeptName())
                .deptCode(userRequestDto.getDeptCode())
                .teamNum(userRequestDto.getTeamNum())
                .position(userRequestDto.getPosition())
                .joinDate(userRequestDto.getJoinDate())
                .dayOff(userRequestDto.getDayOff())
                .usable(true)
                .build();
        userRepository.save(user);
    }

    /**
     * 회원 조회
     */
    public UserResponseDto find(String employeeId) {
        User findUser = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사번입니다."));

        List<Qualification> qualifications = findUser.getQualifications();
        List<QualificationResponseDto> qualificationList = new ArrayList<>();
        for (Qualification qualification: qualifications) {
            QualificationResponseDto qfresponse = QualificationResponseDto.builder()
                    .qlfcType(qualification.getQlfcType())
                    .content(qualification.getContent())
                    .qlfcDate(qualification.getQlfcDate())
                    .build();
            qualificationList.add(qfresponse);
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
                .qualifications(qualificationList)
                .build();
    }

    /**
     * 회원 수정
     */
    @Transactional
    public void update(UserUpdateDto dto) {
        User updateUser = userRepository.findByEmployeeId(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사번입니다."));

        updateUser.updateInfo(dto.getEmployeeId(), dto.getPassword(), dto.getName(), dto.getDeptCode(), dto.getDeptName(), dto.getTeamNum(), dto.getPosition(),
                dto.getEmail(), dto.getTel(), dto.getBirthDate(), dto.getDayOff());
    }

    /**
     * 회원 카드뷰 조회 (페이징 처리)
     */
    public List<UserListResponseDto> getCardView(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.Direction.ASC, "id");

        List<UserListResponseDto> userList = userRepository.findAll(pageable).stream()
                .map(UserListResponseDto::new)
                .collect(Collectors.toList());

        return userList;
    }

    /**
     * 자격증 추가 (인사팀에서 진행)
     */
    public List<RegisterQualificationResponse> registerQualification(List<QualificationRequestDto> qualificationRequestDtoList){
        List<RegisterQualificationResponse> registerqualificationList = new ArrayList<>();
        List<Qualification> qualificationList = new ArrayList<>();

        for (QualificationRequestDto qualificationRequestDto : qualificationRequestDtoList) {
            Qualification qualification = qualificationRepository.save(Qualification.builder()
                    .sortSequence(qualificationRequestDto.getSortSequence())
                    .qlfcDate(qualificationRequestDto.getQlfcDate())
                    .content(qualificationRequestDto.getContent())
                    .qlfcType(qualificationRequestDto.getQlfcType())
                    .qlfcPay(qualificationRequestDto.getQlfcPay())
                    .build());
            qualificationList.add(qualification);

            User user = userRepository.findByEmployeeId(qualificationRequestDto.getEmployeeId())
                    .orElseThrow(() -> new EntityNotFoundException("No Such User"));
            user.registerQualification(qualificationList);
        }

        for (Qualification qualification : qualificationList) {
            RegisterQualificationResponse register = RegisterQualificationResponse.builder()
                    .QualificationId(qualification.getId())
                    .createdAt(LocalDate.now())
                    .build();
            registerqualificationList.add(register);
        }

        return registerqualificationList;
    }

    /**
     * 로그인 하는 시점에 토큰을 생성해서 반환하는 메소드 (로그인을 하는 시점에 토큰이 생성된다)
     * @param employeeId 사용자 이메일
     * @param password 사용자 비밀번호
     * @return 발급한 토큰 정보
     */
    @Transactional
    public TokenResponseDto issueToken(String employeeId, String password) {
        User user = userRepository.findByEmployeeId(employeeId).orElseThrow(() -> new EntityNotFoundException("사용자가 없습니다."));

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 잘못되었습니다.");
        }

        if (!user.isUsable()) {
            throw new IllegalArgumentException("유효시간이 지났습니다.");
        }

        return tokenProvider.generateToken(user);
    }


    @Transactional
    public TokenResponseDto reissueToken(String accessToken, String refreshToken) throws Exception {
        User user = getUserFromAccessToken(accessToken);
        RefreshToken redisRefreshToken = refreshTokenService.findByEmployeeId(user.getId());
        if (redisRefreshToken == null) {
            throw new IllegalArgumentException("refreshToken을 찾을 수 없습니다.");
        }

        if (!refreshToken.equals(redisRefreshToken.getRefreshToken())) {
            log.info("refreshToken = {}", refreshToken);
            log.info("redisRefreshToken = {}", redisRefreshToken.getRefreshToken());
            throw new IllegalArgumentException("refreshToken이 일치하지 않습니다.");
        }

        return tokenProvider.generateToken(user);
    }

    /**
     * 메일 내용을 생성하고 임시 비밀번호로 회원 비밀번호를 변경
     */
    public SlackRequestDto createMailAndChangePassword(String userEmail){
        String tempPassword = getTempPassword();
        SlackRequestDto dto = new SlackRequestDto();
        dto.setEmail(userEmail);
        dto.setTitle("Cocolo 임시비밀번호 안내 이메일 입니다.");
        dto.setMessage("안녕하세요. Cocolo 임시비밀번호 안내 관련 이메일 입니다." + " 회원님의 임시 비밀번호는 "
                + tempPassword + " 입니다." + "로그인 후에 비밀번호를 변경을 해주세요");
        updatePassword(tempPassword,userEmail);
        return dto;
    }

    /**
     * 비밀번호 초기화 & 슬랙 알림 메시지
     */
    public void resetPassword(String employeeId) {
        String tempPassword = getTempPassword();
        String msg = "안녕하세요. 피어나툰ERP 임시비밀번호 안내 관련 메시지 입니다." + " 회원님의 임시 비밀번호는 "
                + tempPassword + " 입니다." + "로그인 후에 비밀번호를 변경을 해주세요";
        slackService.sendSlackChannel(msg, employeeId);
        updatePassword(tempPassword,employeeId);
    }

    /**
     * 임시 비밀번호로 업데이트
     */
    public void updatePassword(String password, String userEmail){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("사원을 찾을 수 없습니다."));
        user.updatePassword(password);
    }

    /**
     * 랜덤함수로 임시 비밀번호 구문 만들기
     */
    public String getTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }

    public LogoutResponseDto logout(String accessToken) {
        String resolvedAccessToken = tokenProvider.resolveToken(accessToken);
        Long employeeId = tokenProvider.parseToken(resolvedAccessToken);
        Long remainTime = tokenProvider.getRemainTime(resolvedAccessToken);

        refreshTokenService.deleteByEmployeeId(employeeId);
        logoutAccessTokenService.saveLogoutAccessToken(LogoutAccessToken.from(resolvedAccessToken, remainTime));

        return LogoutResponseDto.builder()
                .employeeId(employeeId)
                .build();
    }

    private void checkPassword(String loginPassword, String savedPassword) {
        if(!passwordEncoder.matches(loginPassword, savedPassword)){
            throw new RuntimeException("Password is not matched");
        }
    }

    public User getUserFromAccessToken(String accessToken) throws Exception {
        String resolvedAccessToken = tokenProvider.resolveToken(accessToken);
        Long memberId = tokenProvider.parseToken(resolvedAccessToken);

        User user =  userRepository.findById(memberId).
                orElseThrow(() -> new EntityNotFoundException("사원이 없습니다."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            log.info("auth = {}", authentication);
            throw new Exception("인증이 되지 않습니다.");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (!userDetails.getUsername().equals(user.getUsername())) {
            throw new Exception("권한이 없습니다.");
        }

        return user;
    }
}
