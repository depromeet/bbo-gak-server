INSERT INTO users (deleted, created_at, update_at, user_id, dtype, email, login_id, name, password, role, job)
VALUES (false, '2024-07-24 21:27:20.000000', '2024-07-24 21:27:21.000000', 1, 'AuthTestUser', null, 'test', 'test',
        'test', 'USER', 'DEVELOPER');

-- 다음으로 recruit_season 테이블에 데이터를 삽입합니다.
INSERT INTO recruit_season (recruit_season_id, name, user_id)
VALUES (1, '2024 상반기', 1);
INSERT INTO recruit_season (recruit_season_id, name, user_id)
VALUES (2, '2024 하반기', 1);

INSERT INTO recruit (recruit_id, title, site_url, recruit_status, recruit_season_id, user_id, created_at, update_at,
                     deleted)
VALUES (1, 'Title for one day left', 'http://example.com/1', 'DOCUMENT_PASSED', 1, 1, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP, false),
       (2, 'Title for more than one day left', 'http://example.com/2', 'PREPARATION_IN_PROGRESS', 2, 1,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- RecruitSchedule 테이블에 데이터 삽입
INSERT INTO recruit_schedule (recruit_schedule_id, recruit_id, recruit_schedule_stage, dead_line)
VALUES (1, 1, 'FIRST_INTERVIEW', CURRENT_DATE + 1), -- 하루 남은 스케줄
       (2, 2, 'CLOSING_DOCUMENT', CURRENT_DATE + 2); -- 하루 이상 남은 스케줄