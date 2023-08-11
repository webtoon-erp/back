package com.erp.webtoon.repository;

import com.erp.webtoon.domain.Request;
import com.erp.webtoon.domain.RequestDt;
import com.erp.webtoon.fixture.RequestDtFixture;
import com.erp.webtoon.fixture.RequestFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static com.erp.webtoon.fixture.RequestDtFixture.*;
import static com.erp.webtoon.fixture.RequestFixture.*;

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
        request.addRequestDt(requestDt);
        // when
        Request save = requestRepository.save(request);
        List<Request> requestList = requestRepository.findAll();
        // then
        Assertions.assertThat(requestList).contains(save);
        Assertions.assertThat(request.getRequestDts().get(0)).isEqualTo(requestDt);
    }
}