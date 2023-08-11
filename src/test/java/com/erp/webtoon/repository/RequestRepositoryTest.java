package com.erp.webtoon.repository;

import com.erp.webtoon.domain.Request;
import com.erp.webtoon.domain.RequestDt;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static com.erp.webtoon.fixture.RequestDtFixture.*;
import static com.erp.webtoon.fixture.RequestFixture.*;
import static org.assertj.core.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(showSql = false)
class RequestRepositoryTest {

    @Autowired
    private RequestRepository requestRepository;

    @Test
    @DisplayName("요청 저장 테스트")
    void saveRequest() {
        // given
        Request request = FIRST_REQUEST.생성(1L);
        RequestDt requestDt = FIRST_REQUESTDT.생성(1L);
        requestDt.setRequest(request);
        // when
        Request save = requestRepository.save(request);
        List<Request> requestList = requestRepository.findAll();
        // then
        assertThat(requestList).contains(save);
        assertThat(request.getRequestDts().get(0)).isEqualTo(requestDt);
    }

    @Test
    @DisplayName("id로 요청 찾기 테스트")
    void findById() {
        // given
        Request request = requestRepository.save(FIRST_REQUEST.생성(1L));
        // when
        Request request1 = requestRepository.findById(1L).orElseThrow(() -> new EntityNotFoundException("요청을 찾을 수 없습니다."));
        // then
        assertThat(request1).isEqualTo(request);
    }
}