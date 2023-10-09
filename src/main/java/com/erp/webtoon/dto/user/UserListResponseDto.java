package com.erp.webtoon.dto.user;

import com.erp.webtoon.domain.User;
import lombok.Data;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;
import java.net.URL;

@Data
public class UserListResponseDto {

    private UrlResource photo;
    private String position;
    private String name;

    public UserListResponseDto(User user, String photo) throws MalformedURLException {
        this.position = user.getPosition();
        this.name = user.getName();
        this.photo = new UrlResource("http://146.56.98.153:8080" + photo);
    }
}
