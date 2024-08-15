package com.server.bbo_gak.domain.notification.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.server.bbo_gak.domain.notification.dao.NotificationRepository;
import com.server.bbo_gak.domain.notification.dto.response.NotificationGetNumResponse;
import com.server.bbo_gak.domain.notification.dto.response.NotificationGetResponse;
import com.server.bbo_gak.domain.notification.entity.Notification;
import com.server.bbo_gak.domain.notification.entity.NotificationType;
import com.server.bbo_gak.domain.notification.service.NotificationScheduler;
import com.server.bbo_gak.global.AbstractRestDocsTests;
import com.server.bbo_gak.global.RestDocsFactory;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@Sql("/notification-test-data.sql")
public class NotificationControllerTest extends AbstractRestDocsTests {

    private static final String DEFAULT_URL = "/api/v1/notifications";

    @Autowired
    private NotificationScheduler notificationScheduler;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private RestDocsFactory restDocsFactory;

    @BeforeEach
    void setUp() {
        notificationScheduler.executeAtMidnight();
    }

    @Nested
    class 알림개수_조회 {

        @Test
        public void 성공() throws Exception {
            NotificationGetNumResponse response = new NotificationGetNumResponse(1L);

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL + "/num", null, HttpMethod.GET, objectMapper))
                .andExpect(status().isOk())
                .andDo(
                    restDocsFactory.getSuccessResource("[GET] 알림 개수 조회 성공", "알림 개수 조회", "Notification", null,
                        response));

            assertEquals(1, notificationRepository.findAll().size());
        }
    }

    @Nested
    class 알림리스트_조회 {

        private NotificationGetResponse response;

        @BeforeEach
        void setUp() {
            Notification notification = Notification.builder()
                .title("New Job Alert")
                .message("A new job matching your profile has been posted.")
                .isRead(false)
                .type(NotificationType.RECRUIT)
                .referenceId(12345L)
                .build();

            try {
                // 리플렉션을 통해 id 필드에 접근하여 값 설정
                Field idField = Notification.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(notification, 1L);  // id 값을 1로 설정

                // 리플렉션을 통해 createdDate 필드에 접근하여 값 설정
                Field createdDateField = Notification.class.getSuperclass().getDeclaredField("createdDate");
                createdDateField.setAccessible(true);
                createdDateField.set(notification, LocalDateTime.now());

                response = NotificationGetResponse.from(notification);

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        @Test
        public void 성공() throws Exception {
            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL, null, HttpMethod.GET, objectMapper))
                .andExpect(status().isOk())
                .andDo(
                    restDocsFactory.getSuccessResourceList("[GET] 알림 리스트 조회 성공", "알림 리스트 조회", "Notification",
                        List.of(), List.of(response)));
        }
    }

    @Nested
    class 알림읽음표시_업데이트 {

        @Test
        public void 성공() throws Exception {
            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL, null, HttpMethod.PUT, objectMapper))
                .andExpect(status().isOk())
                .andDo(
                    restDocsFactory.getSuccessResourceList("[PUT] 알림 읽음 표시 업데이트 성공", "알림 읽음 표시 업데이트", "Notification",
                        List.of(), List.of()));
            Notification notification = notificationRepository.findById(1L).get();
            System.out.println(notification.getTitle());
            assertTrue(notification.isRead());
        }
    }
}
