INSERT INTO users (deleted, created_at, update_at, user_id, dtype, email, login_id, name, password, role, job)
VALUES (false, '2024-07-24 21:27:20.000000', '2024-07-24 21:27:21.000000', 1, 'AuthTestUser', 'test', 'test', 'test',
        'test', 'USER', 'DEVELOPER'),
       (false, '2024-07-24 21:27:20.000000', '2024-07-24 21:27:21.000000', 2, 'AuthTestUser', 'test', 'test', 'test',
        'test', 'USER', 'DEVELOPER'),
       (false, '2024-07-24 21:27:20.000000', '2024-07-24 21:27:21.000000', 3, 'AuthTestUser', 'test', 'test', 'test',
        'test', 'USER', 'DEVELOPER')
;

-- Season 테이블에 데이터 삽입
INSERT INTO recruit_season (recruit_season_id, name, user_id, deleted, update_at, created_at)
VALUES (1, '2024 상반기', 1, false, '2024-07-24 21:27:20.000000', '2024-07-24 21:27:21.000000'),
       (2, '2024 하반기', 1, false, '2024-07-24 21:27:20.000000', '2024-07-24 21:27:21.000000'),
       (3, '2024 상반기', 2, false, '2024-07-24 21:27:20.000000', '2024-07-24 21:27:21.000000'),
       (4, '2024 하반기', 2, false, '2024-07-24 21:27:20.000000', '2024-07-24 21:27:21.000000'),
       (5, '2024 상반기', 3, false, '2024-07-24 21:27:20.000000', '2024-07-24 21:27:21.000000'),
       (6, '2024 하반기', 3, false, '2024-07-24 21:27:20.000000', '2024-07-24 21:27:21.000000')
;

-- Recruit 테이블에 데이터 삽입
INSERT INTO recruit (recruit_id, title, site_url, recruit_status, recruit_season_id, user_id, created_at, update_at,
                     deleted)
VALUES (1, 'Title for one day left', 'http://example.com/1', 'DOCUMENT_PASSED', 1, 1, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP, false),
       (2, 'Title for more than one day left', 'http://example.com/2', 'PREPARATION_IN_PROGRESS', 2, 1,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
       (3, 'Title for more than one day left', 'http://example.com/2', 'PREPARATION_IN_PROGRESS', 3, 2,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
       (4, 'Title for more than one day left', 'http://example.com/2', 'PREPARATION_IN_PROGRESS', 3, 2,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
       (5, 'Title for more than one day left', 'http://example.com/2', 'PREPARATION_IN_PROGRESS', 4, 2,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
       (6, 'Title for more than one day left', 'http://example.com/2', 'PREPARATION_IN_PROGRESS', 5, 3,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
       (7, 'Title for more than one day left', 'http://example.com/2', 'PREPARATION_IN_PROGRESS', 6, 3,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false)
;

-- RecruitSchedule 테이블에 데이터 삽입
INSERT INTO recruit_schedule (recruit_schedule_id, recruit_id, recruit_schedule_stage, dead_line, deleted, update_at, created_at)
VALUES (1, 1, 'FIRST_INTERVIEW', TIMESTAMPADD(DAY, 3, CURRENT_TIMESTAMP), false, '2024-07-24 21:26:28.000000', '2024-07-24 21:26:28.000000'),
       (2, 4, 'FIRST_INTERVIEW', TIMESTAMPADD(DAY, 3, CURRENT_TIMESTAMP), false, '2024-07-24 21:26:28.000000', '2024-07-24 21:26:28.000000'),
       (3, 5, 'FIRST_INTERVIEW', TIMESTAMPADD(DAY, 1, CURRENT_TIMESTAMP), false, '2024-07-24 21:26:28.000000', '2024-07-24 21:26:28.000000'),
       (4, 6, 'FIRST_INTERVIEW', TIMESTAMPADD(DAY, 2, CURRENT_TIMESTAMP), false, '2024-07-24 21:26:28.000000', '2024-07-24 21:26:28.000000'),
       (5, 6, 'SECOND_INTERVIEW', TIMESTAMPADD(DAY, 3, CURRENT_TIMESTAMP), false, '2024-07-24 21:26:28.000000', '2024-07-24 21:26:28.000000'),
       (6, 7, 'FIRST_INTERVIEW', TIMESTAMPADD(DAY, -2, CURRENT_TIMESTAMP), false, '2024-07-24 21:26:28.000000', '2024-07-24 21:26:28.000000'),
       (7, 7, 'SECOND_INTERVIEW', TIMESTAMPADD(DAY, -1, CURRENT_TIMESTAMP), false, '2024-07-24 21:26:28.000000', '2024-07-24 21:26:28.000000'),
       (8, 7, 'FINAL_INTERVIEW', TIMESTAMPADD(DAY, 1, CURRENT_TIMESTAMP), false, '2024-07-24 21:26:28.000000', '2024-07-24 21:26:28.000000')
;

INSERT INTO card (deleted, copy_flag, access_time, card_id, created_at, update_at, user_id, content, title, recruit_id)
VALUES (false, false, '2024-07-24 21:22:04.000000', 1, '2024-07-24 21:22:07.000000', '2024-07-24 21:22:08.000000', 1,
        'test_contents', 'test_title', 1);
INSERT INTO card (deleted, copy_flag, access_time, card_id, created_at, update_at, user_id, content, title, recruit_id)
VALUES (false, false, '1970-01-01 00:00:00.001000', 2, '1970-01-01 00:00:00.001000', '1970-01-01 00:00:00.001000', 1,
        'testc', 'testc', 1);
INSERT INTO card (deleted, copy_flag, access_time, card_id, created_at, update_at, user_id, content, title, recruit_id)
VALUES (false, false, '1970-01-01 00:00:00.001000', 3, '1970-01-01 00:00:00.001000', '1970-01-01 00:00:00.001000', 1,
        'testc', 'testt', 1);