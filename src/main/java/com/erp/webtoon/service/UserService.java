package com.erp.webtoon.service;

import com.erp.webtoon.domain.File;
import com.erp.webtoon.domain.LogoutAccessToken;
import com.erp.webtoon.domain.RefreshToken;
import com.erp.webtoon.dto.user.NewPasswordDto;
import com.erp.webtoon.dto.user.QualificationDeleteRequestDto;
import com.erp.webtoon.dto.user.QualificationModifyRequestDto;
import com.erp.webtoon.token.LogoutAccessTokenService;
import com.erp.webtoon.token.RefreshTokenService;
import com.erp.webtoon.token.TokenProvider;
import com.erp.webtoon.domain.Qualification;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.token.TokenResponseDto;
import com.erp.webtoon.dto.user.QualificationRequestDto;
import com.erp.webtoon.dto.user.QualificationResponseDto;
import com.erp.webtoon.dto.user.RegisterQualificationResponse;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
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
    private final FileService fileService;

    /**
     * 신규 직원 추가
     */

    @Transactional
    public void addNewCome(UserRequestDto userRequestDto, MultipartFile file) throws IOException {
        String encodedPassword = passwordEncoder.encode("1234");
        User user = userRequestDto.toEntity(encodedPassword);
        user.addRole("USER");

        if (file != null && !file.isEmpty()) {
            File savedFile = fileService.save(file);

            if (savedFile != null) {
                savedFile.updateFileUser(user);
                user.addPhoto(savedFile);
            }
        }

        userRepository.save(user);
    }

    /**
     * 회원 조회
     */
    public UserResponseDto find(String employeeId) {
        User findUser = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사번입니다."));

        if(findUser.isUsable() == false) {
            throw new EntityNotFoundException("퇴사한 직원입니다.");
        }

        List<Qualification> qualifications = findUser.getQualifications();
        List<QualificationResponseDto> qualificationList = qualifications.stream()
                .map(qualification -> QualificationResponseDto.from(qualification)).collect(Collectors.toList());

        return UserResponseDto.of(findUser, qualificationList);
    }

    /**
     * 회원 수정(사번이랑, 연차빼고)
     */
    @Transactional
    public void update(UserUpdateDto userUpdateDto) {
        User updateUser = userRepository.findByEmployeeId(userUpdateDto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사번입니다."));

        if(updateUser.isUsable() == false) {
            throw new EntityNotFoundException("퇴사한 직원입니다.");
        }

        String encodedPassword = passwordEncoder.encode(userUpdateDto.getPassword());

        updateUser.updateInfo(encodedPassword, userUpdateDto.getName(), userUpdateDto.getDeptCode(), userUpdateDto.getDeptName(), userUpdateDto.getTeamNum(), userUpdateDto.getPosition(),
                userUpdateDto.getEmail(), userUpdateDto.getTel(), userUpdateDto.getBirthDate());
    }

    /**
     * 퇴사 및 은퇴사원 상태값 변경
     */
    @Transactional
    public void retire(String employeeId) {
        User user = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사번입니다."));

        user.changeUsable();
    }

    /**
     * 회원 카드뷰 조회 (페이징 처리)
     */
    public List<UserListResponseDto> getCardView(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.Direction.ASC, "id");

        List<UserListResponseDto> userList = userRepository.findAll(pageable).stream()
                .filter(user -> user.isUsable() == true)
                .map(u -> {
                    try {
                        if(u.getFile() == null){
                            return new UserListResponseDto(u, null);
                        }
                        return new UserListResponseDto(u, fileService.getFullPath(u.getFile().getFileName()));
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        return userList;
    }

    /**
     * 자격증 추가 (인사팀에서 진행)
     */
    @Transactional
    public List<RegisterQualificationResponse> registerQualification(List<QualificationRequestDto> qualificationRequestList) {
        List<Qualification> qualificationList = new ArrayList<>();
        List<RegisterQualificationResponse> responseList = new ArrayList<>();
        User user = new User();

        for (QualificationRequestDto qualificationRequestDto : qualificationRequestList) {
            user = userRepository.findByEmployeeId(qualificationRequestDto.getEmployeeId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사번입니다."));

            if(user.isUsable() == false) {
                throw new EntityNotFoundException("퇴사한 직원입니다.");
            }

            Qualification qualification = qualificationRepository.save(Qualification.builder()
                    .qlfcDate(qualificationRequestDto.getQlfcDate())
                    .content(qualificationRequestDto.getContent())
                    .qlfcType(qualificationRequestDto.getQlfcType())
                    .qlfcPay(qualificationRequestDto.getQlfcPay())
                    .user(user)
                    .build());
            qualificationList.add(qualification);

            responseList.add(RegisterQualificationResponse.builder().QualificationId(qualification.getId()).createdAt(LocalDate.now()).build());
        }

        user.registerQualification(qualificationList);

        return responseList;
    }

    /**
     * 자격증 수정
     */
    @Transactional
    public void updateQualification(List<QualificationModifyRequestDto> qualificationRequestList) {
        for (QualificationModifyRequestDto qualificationModifyRequestDto : qualificationRequestList) {
            Qualification qualification = qualificationRepository.findById(qualificationModifyRequestDto.getQualificationId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 자격증입니다."));
            qualification.updateInfo(qualificationModifyRequestDto.getQlfcType(),
                    qualificationModifyRequestDto.getContent(), qualificationModifyRequestDto.getQlfcDate(), qualificationModifyRequestDto.getQlfcPay());
        }
    }

    /**
     * 자격증 삭제
     */
    @Transactional
    public void deleteQualification(List<QualificationDeleteRequestDto> deleteRequestList) {
        for (QualificationDeleteRequestDto deleteRequest : deleteRequestList) {
            qualificationRepository.deleteById(deleteRequest.getQualificationId());
        }
    }

    /**
     * 로그인 하는 시점에 토큰을 생성해서 반환하는 메소드 (로그인을 하는 시점에 토큰이 생성된다)
     * @param employeeId 사용자 이메일
     * @param password 사용자 비밀번호
     * @return 발급한 토큰 정보
     */
    @Transactional
    public TokenResponseDto issueToken(String employeeId, String password) {
        User user = userRepository.findByEmployeeId(employeeId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사번입니다."));

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
     * 비밀번호 초기화 & 슬랙 알림 메시지
     */
    @Transactional
    public void resetPassword(String employeeId) throws Exception {
        User user = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사번입니다."));
        String tempPassword = getTempPassword();
        String msg = "안녕하세요. 피어나툰ERP 임시비밀번호 안내 관련 메시지 입니다." + " 회원님의 임시 비밀번호는 "
                + tempPassword + " 입니다." + "로그인 후에 비밀번호를 변경을 해주세요";
        slackService.sendSlackChannel(msg, employeeId);
        updatePassword(tempPassword, user);
    }

    /**
     * 임시 비밀번호로 업데이트
     */
    private void updatePassword(String password, User user) {
        String encodedPassword = passwordEncoder.encode(password);
        user.updatePassword(encodedPassword);
    }

    /**
     * 랜덤함수로 임시 비밀번호 구문 만들기
     */
    private String getTempPassword() {
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

    @Transactional
    public void changePassword(NewPasswordDto dto) {
        User user = userRepository.findByEmployeeId(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사번입니다."));
        String newPassword = dto.getPassword();
        updatePassword(newPassword, user);
    }

    /**
     * 연차 증가
     */
    @Scheduled(cron = "0 0 02 1 * ?")
    public void addDayoff() {
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            user.addDayoff();
        }
    }

    public void logout(String accessToken) {
        String resolvedAccessToken = tokenProvider.resolveToken(accessToken);
        String employeeId = tokenProvider.parseToken(resolvedAccessToken);
        Long remainTime = tokenProvider.getRemainTime(resolvedAccessToken);

        refreshTokenService.deleteByEmployeeId(employeeId);
        logoutAccessTokenService.saveLogoutAccessToken(LogoutAccessToken.from(resolvedAccessToken, remainTime));
    }

    private User getUserFromAccessToken(String accessToken) throws Exception {
        String resolvedAccessToken = tokenProvider.resolveToken(accessToken);
        String employeeId = tokenProvider.parseToken(resolvedAccessToken);

        User user =  userRepository.findByEmployeeId(employeeId).
                orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사번입니다."));

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

    public void reduceDayOff(List<User> users) {
        users.forEach(User::reduceDayOff);
    }
}
