
package com.addiction.user.userCigaretteHistory.service;

import com.addiction.IntegrationTestSupport;
import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.userCigarette.service.UserCigaretteService;
import com.addiction.user.userCigarette.service.request.ChangeType;
import com.addiction.user.userCigarette.service.request.UserCigaretteChangeServiceRequest;
import com.addiction.user.userCigaretteHistory.document.CigaretteHistoryDocument;
import com.addiction.user.userCigaretteHistory.enums.PeriodType;
import com.addiction.user.userCigaretteHistory.service.response.UserCigaretteHistoryCalenderResponse;
import com.addiction.user.userCigaretteHistory.service.response.UserCigaretteHistoryGraphResponse;
import com.addiction.user.userCigaretteHistory.service.response.UserCigaretteHistoryLastestResponse;
import com.addiction.user.userCigaretteHistory.service.response.UserCigaretteHistoryResponse;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class UserCigaretteHistoryServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserCigaretteService userCigaretteService;
    @Autowired
    private UserCigaretteHistoryService userCigaretteHistoryService;
    @Autowired
    private org.springframework.data.mongodb.core.MongoTemplate mongoTemplate;

    @AfterEach
    public void tearDown() {
        userCigaretteRepository.deleteAllInBatch();
        // MongoDB의 모든 cigarette_history 컬렉션 삭제
        mongoTemplate.getCollectionNames().stream()
                .filter(name -> name.startsWith("cigarette_history_"))
                .forEach(mongoTemplate::dropCollection);
    }

    @DisplayName("유저의 마지막 흡연 기록을 조회한다.")
    @Test
    void 유저의_마지막_흡연_기록을_조회한다() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        userRepository.save(user);

        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(user.getId()));

        UserCigaretteChangeServiceRequest request1 = UserCigaretteChangeServiceRequest.builder()
                .changeType(ChangeType.ADD)
                .address("서울시 강남구")
                .build();
        userCigaretteService.changeCigarette(request1);

        UserCigaretteChangeServiceRequest request2 = UserCigaretteChangeServiceRequest.builder()
                .changeType(ChangeType.ADD)
                .address("서울시 송파구")
                .build();
        userCigaretteService.changeCigarette(request2);

        // when
        UserCigaretteHistoryLastestResponse result = userCigaretteHistoryService.findLastestByUserId();

        // then
        assertThat(result)
                .extracting("address")
                .isEqualTo("서울시 송파구");
    }

    @DisplayName("흡연 히스토리를 MongoDB에 저장한다.")
    @Test
    void 흡연_히스토리를_MongoDB에_저장한다() {
        // given
        String monthStr = "202510";
        String dateStr = "20251015";
        Long userId = 1L;
        Integer smokeCount = 5;
        Long avgPatienceTime = 3600L;

        List<CigaretteHistoryDocument.History> historyList = List.of(
                CigaretteHistoryDocument.History.builder()
                        .address("서울시 강남구")
                        .smokeTime(LocalDateTime.now())
                        .smokePatienceTime(3600L)
                        .build()
        );

        // when
        userCigaretteHistoryService.save(monthStr, dateStr, userId, smokeCount, avgPatienceTime, historyList);

        // then
        CigaretteHistoryDocument result = userCigaretteHistoryRepository.findByDateAndUserId(dateStr, userId);
        assertThat(result).isNotNull();
        assertThat(result.getMonth()).isEqualTo(monthStr);
        assertThat(result.getDate()).isEqualTo(dateStr);
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getSmokeCount()).isEqualTo(smokeCount);
        assertThat(result.getAvgPatienceTime()).isEqualTo(avgPatienceTime);
        assertThat(result.getHistory()).hasSize(1);
    }

    @DisplayName("특정 월의 흡연 히스토리를 캘린더 형식으로 조회한다 - 당일 데이터 포함")
    @Test
    void 특정_월의_흡연_히스토리를_캘린더_형식으로_조회한다_당일_데이터_포함() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        userRepository.save(user);

        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(user.getId()));

        // 과거 데이터 MongoDB에 저장
        String pastDate = "20251010";
        userCigaretteHistoryService.save("202510", pastDate, user.getId(), 3, 2000L,
                List.of(CigaretteHistoryDocument.History.builder()
                        .address("서울시 강남구")
                        .smokeTime(LocalDateTime.now().minusDays(5))
                        .smokePatienceTime(2000L)
                        .build())
        );

        // 당일 데이터 RDBMS에 추가
        UserCigaretteChangeServiceRequest request = UserCigaretteChangeServiceRequest.builder()
                .changeType(ChangeType.ADD)
                .address("서울시 송파구")
                .build();
        userCigaretteService.changeCigarette(request);

        String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));

        // when
        List<UserCigaretteHistoryCalenderResponse> results = userCigaretteHistoryService.findCalendarByDate(currentMonth);

        // then
        assertThat(results).hasSizeGreaterThanOrEqualTo(1);

        // 당일 데이터 확인
        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        assertThat(results)
                .filteredOn(r -> r.getDate().equals(today))
                .hasSize(1)
                .first()
                .extracting("count")
                .isEqualTo(1);
    }

    @DisplayName("특정 날짜의 상세 흡연 히스토리를 조회한다 - 당일 데이터")
    @Test
    void 특정_날짜의_상세_흡연_히스토리를_조회한다_당일_데이터() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        userRepository.save(user);

        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(user.getId()));

        // 당일 데이터 추가
        UserCigaretteChangeServiceRequest request1 = UserCigaretteChangeServiceRequest.builder()
                .changeType(ChangeType.ADD)
                .address("서울시 강남구")
                .build();
        userCigaretteService.changeCigarette(request1);

        UserCigaretteChangeServiceRequest request2 = UserCigaretteChangeServiceRequest.builder()
                .changeType(ChangeType.ADD)
                .address("서울시 송파구")
                .build();
        userCigaretteService.changeCigarette(request2);

        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);

        // when
        List<UserCigaretteHistoryResponse> results = userCigaretteHistoryService.findHistoryByDate(today);

        // then
        assertThat(results).hasSize(2);
        assertThat(results)
                .extracting("address")
                .containsExactly("서울시 강남구", "서울시 송파구");
    }

    @DisplayName("특정 날짜의 상세 흡연 히스토리를 조회한다 - 과거 데이터")
    @Test
    void 특정_날짜의_상세_흡연_히스토리를_조회한다_과거_데이터() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        userRepository.save(user);

        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(user.getId()));

        // 과거 데이터 MongoDB에 저장
        String pastDate = "20251010";
        List<CigaretteHistoryDocument.History> histories = List.of(
                CigaretteHistoryDocument.History.builder()
                        .address("서울시 강남구")
                        .smokeTime(LocalDateTime.now().minusDays(5))
                        .smokePatienceTime(2000L)
                        .build(),
                CigaretteHistoryDocument.History.builder()
                        .address("서울시 송파구")
                        .smokeTime(LocalDateTime.now().minusDays(5))
                        .smokePatienceTime(3000L)
                        .build()
        );
        userCigaretteHistoryService.save("202510", pastDate, user.getId(), 2, 2500L, histories);

        // when
        List<UserCigaretteHistoryResponse> results = userCigaretteHistoryService.findHistoryByDate(pastDate);

        // then
        assertThat(results).hasSize(2);
        assertThat(results)
                .extracting("address")
                .containsExactly("서울시 강남구", "서울시 송파구");
    }

    @DisplayName("기간별 그래프 데이터를 조회한다 - 당일 데이터 포함")
    @Test
    void 기간별_그래프_데이터를_조회한다_당일_데이터_포함() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        userRepository.save(user);

        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(user.getId()));

        // 과거 데이터 MongoDB에 저장
        String pastDate = LocalDate.now().minusDays(3).format(DateTimeFormatter.BASIC_ISO_DATE);
        String pastMonth = LocalDate.now().minusDays(3).format(DateTimeFormatter.ofPattern("yyyyMM"));
        userCigaretteHistoryService.save(pastMonth, pastDate, user.getId(), 5, 3000L,
                List.of(CigaretteHistoryDocument.History.builder()
                        .address("서울시 강남구")
                        .smokeTime(LocalDateTime.now().minusDays(3))
                        .smokePatienceTime(3000L)
                        .build())
        );

        // 당일 데이터 RDBMS에 추가
        UserCigaretteChangeServiceRequest request1 = UserCigaretteChangeServiceRequest.builder()
                .changeType(ChangeType.ADD)
                .address("서울시 송파구")
                .build();
        userCigaretteService.changeCigarette(request1);

        UserCigaretteChangeServiceRequest request2 = UserCigaretteChangeServiceRequest.builder()
                .changeType(ChangeType.ADD)
                .address("서울시 강남구")
                .build();
        userCigaretteService.changeCigarette(request2);

        // when
        UserCigaretteHistoryGraphResponse results = userCigaretteHistoryService.findGraphByPeriod(PeriodType.WEEKLY);

        // then
        assertThat(results).isNotNull();
        assertThat(results.getCigarette()).isNotNull();
        assertThat(results.getPatient()).isNotNull();

        // 당일 데이터가 포함되어 있는지 확인
        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        assertThat(results.getCigarette().getDate())
                .filteredOn(d -> d.getDate().equals(today))
                .hasSize(1)
                .first()
                .extracting("value")
                .isEqualTo(2L);
    }

    @DisplayName("기간별 그래프 데이터를 조회한다 - 당일 데이터만 있는 경우")
    @Test
    void 기간별_그래프_데이터를_조회한다_당일_데이터만_있는_경우() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        userRepository.save(user);

        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(user.getId()));

        // 당일 데이터만 추가
        for (int i = 0; i < 3; i++) {
            UserCigaretteChangeServiceRequest request = UserCigaretteChangeServiceRequest.builder()
                    .changeType(ChangeType.ADD)
                    .address("서울시 강남구")
                    .build();
            userCigaretteService.changeCigarette(request);
        }

        // when
        UserCigaretteHistoryGraphResponse results = userCigaretteHistoryService.findGraphByPeriod(PeriodType.WEEKLY);

        // then
        assertThat(results).isNotNull();
        assertThat(results.getCigarette().getAvgCigaretteCount()).isEqualTo(3);
        assertThat(results.getCigarette().getDate()).hasSize(1);

        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        assertThat(results.getCigarette().getDate())
                .first()
                .extracting("date", "value")
                .containsExactly(today, 3L);
    }
}
