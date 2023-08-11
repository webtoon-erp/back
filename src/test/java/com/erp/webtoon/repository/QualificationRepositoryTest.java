package com.erp.webtoon.repository;

import com.erp.webtoon.domain.Qualification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static com.erp.webtoon.fixture.QualificationFixture.*;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(showSql = false)
class QualificationRepositoryTest {

    @Autowired
    private QualificationRepository qualificationRepository;

    @Test
    @DisplayName("저장 메소드 테스트")
    void saveQualification() {
        // given
        Qualification qualification = FIRST_QUALIFICATION.생성(1L);
        Qualification save = qualificationRepository.save(qualification);
        // when
        List<Qualification> list = qualificationRepository.findAll();
        // then
        Assertions.assertThat(list).contains(save);
    }
}