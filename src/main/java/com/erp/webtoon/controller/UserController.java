package com.erp.webtoon.controller;

import com.erp.webtoon.dto.token.TokenResponseDto;

import com.erp.webtoon.dto.user.LoginRequestDto;
import com.erp.webtoon.dto.user.NewPasswordDto;
import com.erp.webtoon.dto.user.QualificationDeleteRequestDto;
import com.erp.webtoon.dto.user.QualificationModifyRequestDto;
import com.erp.webtoon.dto.user.QualificationRequestDto;
import com.erp.webtoon.dto.user.RegisterQualificationResponse;
import com.erp.webtoon.dto.user.UserListResponseDto;
import com.erp.webtoon.dto.user.UserRequestDto;
import com.erp.webtoon.dto.user.UserResponseDto;
import com.erp.webtoon.dto.user.UserUpdateDto;
import com.erp.webtoon.service.FileService;
import com.erp.webtoon.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    private final FileService fileService;

    @PostMapping
    public ResponseEntity add(@Valid @RequestPart("dto") UserRequestDto userRequestDto, @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        userService.addNewCome(userRequestDto, file);
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

    @PostMapping("/tempPassword/{employeeId}")
    public ResponseEntity issueTempPassword(@PathVariable String employeeId) throws Exception {
        userService.resetPassword(employeeId);
        return ResponseEntity.ok("임시 비밀번호 발급에 성공했습니다.");
    }

    @PostMapping("/newPassword")
    public ResponseEntity changePassword(@RequestBody NewPasswordDto dto) throws Exception {
        userService.changePassword(dto);
        return ResponseEntity.ok("비밀번호 변경에 성공했습니다.");
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {
        userService.logout(accessToken);
        return ResponseEntity.ok("로그아웃에 성공했습니다.");
    }

    /**
     * 직원 개별 조회
     */
    @GetMapping("/{employeeId}")
    public ResponseEntity singleView(@PathVariable String employeeId) throws MalformedURLException {

        UserResponseDto userResponseDto = userService.find(employeeId);
        if(userResponseDto.getPhoto() == null) {
            return ResponseEntity.ok(new Result(null, userResponseDto));
        }

        UrlResource photo = new UrlResource("http://146.56.98.153:8080" +fileService.getFullPath(userResponseDto.getPhoto()));
        return ResponseEntity.ok(new Result(photo.getURL(), userResponseDto));
    }

    /**
     * 직원조회 -> 카드뷰
     */
    @GetMapping
    public ResponseEntity cardView() {
        List<UserListResponseDto> dtos = userService.getCardView();

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
    public List<RegisterQualificationResponse> registerQualification(@RequestBody List<QualificationRequestDto> qualificationRequestDtoList) {
        return userService.registerQualification(qualificationRequestDtoList);
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

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T resource;
        private T info;
    }
}