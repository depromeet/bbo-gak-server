INSERT INTO users (deleted, created_at, update_at, user_id, dtype, email, login_id, name, password, role)
VALUES (false, '2024-07-24 21:27:20.000000', '2024-07-24 21:27:21.000000', 1, 'AuthTestUser', 'test', 'test', 'test',
        'test', 'USER'),
       (false, '2024-07-24 21:27:20.000000', '2024-07-24 21:27:21.000000', 2, 'AuthTestUser', 'test', 'test', 'test',
        'test', 'USER'),
       (false, '2024-07-24 21:27:20.000000', '2024-07-24 21:27:21.000000', 3, 'AuthTestUser', 'test', 'test', 'test',
        'test', 'USER')
;

-- Season 테이블에 데이터 삽입
INSERT INTO recruit_season (recruit_season_id, name, user_id)
VALUES (1, '2024 상반기', 1),
       (2, '2024 하반기', 1),
       (3, '2024 상반기', 2),
       (4, '2024 하반기', 2),
       (5, '2024 상반기', 3),
       (6, '2024 하반기', 3)
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
INSERT INTO recruit_schedule (recruit_schedule_id, recruit_id, recruit_schedule_stage, dead_line)
VALUES (1, 1, 'FIRST_INTERVIEW', '2024-12-31'),
       (2, 4, 'FIRST_INTERVIEW', TIMESTAMPADD(DAY, 3, CURRENT_TIMESTAMP)),
       (3, 5, 'FIRST_INTERVIEW', TIMESTAMPADD(DAY, 1, CURRENT_TIMESTAMP)),
       (4, 6, 'FIRST_INTERVIEW', TIMESTAMPADD(DAY, 2, CURRENT_TIMESTAMP)),
       (5, 6, 'SECOND_INTERVIEW', TIMESTAMPADD(DAY, 3, CURRENT_TIMESTAMP)),
       (6, 7, 'FIRST_INTERVIEW', TIMESTAMPADD(DAY, -2, CURRENT_TIMESTAMP)),
       (7, 7, 'SECOND_INTERVIEW', TIMESTAMPADD(DAY, -1, CURRENT_TIMESTAMP)),
       (8, 7, 'FINAL_INTERVIEW', TIMESTAMPADD(DAY, 1, CURRENT_TIMESTAMP))
;