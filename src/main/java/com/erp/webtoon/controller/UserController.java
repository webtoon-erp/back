package com.erp.webtoon.controller;

import com.erp.webtoon.dto.token.LogoutResponseDto;
import com.erp.webtoon.dto.token.TokenResponseDto;

import com.erp.webtoon.dto.user.LoginRequestDto;
import com.erp.webtoon.dto.user.QualificationDeleteRequestDto;
import com.erp.webtoon.dto.user.QualificationModifyRequestDto;
import com.erp.webtoon.dto.user.QualificationRequestDto;
import com.erp.webtoon.dto.user.UserListResponseDto;
import com.erp.webtoon.dto.user.UserRequestDto;
import com.erp.webtoon.dto.user.UserResponseDto;
import com.erp.webtoon.dto.user.UserUpdateDto;
import com.erp.webtoon.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity add(@Valid @RequestBody UserRequestDto userRequestDto) {
        userService.addNewCome(userRequestDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody LoginRequestDto loginDto) {
        return userService.issueToken(loginDto.getEmployeeId(), loginDto.getPassword());
    }

    @PostMapping("/reissue")
    public TokenResponseDto reissue(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken, @CookieValue("refresh_token") String refreshToken) throws Exception {
        return userService.reissueToken(accessToken, refreshToken);
    }

    @PostMapping("/tempPassword")
    public String issueTempPassword(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) throws Exception {
        userService.resetPassword(accessToken);

        return "/users/login";
    }

    @PostMapping("/logout")
    public LogoutResponseDto logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {
        return userService.logout(accessToken);
    }

    /**
     * 직원 개별 조회
     */
    @GetMapping("/{employeeId}")
    public ResponseEntity singleView(@PathVariable String employeeId) {
        UserResponseDto userResponseDto = userService.find(employeeId);
        return ResponseEntity.ok(userResponseDto);
    }

    /**
     * 직원조회 -> 카드뷰
     */
    @GetMapping
    public ResponseEntity cardView(@RequestParam("page") int page) {
        List<UserListResponseDto> dtos = userService.getCardView(page);

        return ResponseEntity.ok(dtos);
    }

    /**
     * 직원 정보 수정
     */
    @PatchMapping
    public void update(@RequestBody UserUpdateDto dto) {
        userService.update(dto);
    }

    @PatchMapping("{employeeId}")
    public ResponseEntity retire(@PathVariable String employeeId) {
        userService.retire(employeeId);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 자격증 추가(인사팀)
     */
    @PostMapping("/qualification")
    public ResponseEntity registerQualification(@RequestBody List<QualificationRequestDto> qualificationRequestDtoList) {
        userService.registerQualification(qualificationRequestDtoList);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 자격증 수정
     */
    @PatchMapping("/qualification")
    public ResponseEntity modifyQualification(@RequestBody List<QualificationModifyRequestDto> qualificationModifyRequestDtoList) {
        userService.updateQualification(qualificationModifyRequestDtoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 자격증 삭제
     */
    @DeleteMapping("/qualification")
    public ResponseEntity deleteQualification(@RequestBody List<QualificationDeleteRequestDto> qualificationDeleteRequestDtoList) {
        userService.deleteQualification(qualificationDeleteRequestDtoList);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}

