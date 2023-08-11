package com.erp.webtoon.repository;

import com.erp.webtoon.domain.User;
import com.erp.webtoon.fixture.UserFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityNotFoundException;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(showSql = false)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("이메일로 사원 찾기 테스트")
    void findByEmail() {
        // given
        User user = userRepository.save(UserFixture.FIRST_USER.생성(1L));
        // when
        User user1 = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("사원을 찾을 수 없습니다."));
        // then
        Assertions.assertThat(user1).isEqualTo(user);
    }
}