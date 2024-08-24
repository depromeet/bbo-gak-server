INSERT INTO users (deleted, created_at, update_at, user_id, dtype, email, login_id, name, password, role, job)
VALUES (false, '2024-07-24 21:27:20.000000', '2024-07-24 21:27:21.000000', 1, 'AuthTestUser', 'test', 'test', 'test',
        'test', 'USER', 'DEVELOPER');

INSERT INTO recruit_season (recruit_season_id, name, user_id)
VALUES (1, 'test', 1);

INSERT INTO recruit (recruit_id, created_at, deleted, update_at, recruit_status, site_url, title,
                     recruit_season_id, user_id)
VALUES (1, '2024-08-14 23:10:09.000000', false, '2024-08-14 23:10:12.000000', 'APPLICATION_COMPLETED', 'test', 'test',
        1, 1);

-- my info card
INSERT INTO card (deleted, copy_flag, access_time, card_id, created_at, update_at, user_id, content, title, recruit_id)
VALUES (false, false, '2024-07-24 21:22:04.000000', 1, '2024-07-24 21:22:07.000000', '2024-07-24 21:22:08.000000', 1,
        'test_contents', 'test_title', null);
INSERT INTO card (deleted, copy_flag, access_time, card_id, created_at, update_at, user_id, content, title, recruit_id)
VALUES (false, false, '1970-01-01 00:00:00.001000', 2, '1970-01-01 00:00:00.001000', '1970-01-01 00:00:00.001000', 1,
        'testc', 'testc', null);
INSERT INTO card (deleted, copy_flag, access_time, card_id, created_at, update_at, user_id, content, title, recruit_id)
VALUES (false, false, '1970-01-01 00:00:00.001000', 3, '1970-01-01 00:00:00.001000', '1970-01-01 00:00:00.001000', 1,
        'testc', 'testt', null);
-- recruit card
INSERT INTO card (deleted, copy_flag, access_time, card_id, created_at, update_at, user_id, content, title, recruit_id)
VALUES (false, true, '2024-07-24 21:22:04.000000', 4, '2024-07-24 21:22:07.000000', '2024-07-24 21:22:08.000000', 1,
        'test_contents', 'test_title', 1);
INSERT INTO card (deleted, copy_flag, access_time, card_id, created_at, update_at, user_id, content, title, recruit_id)
VALUES (false, true, '2024-07-24 21:22:04.000000', 5, '2024-07-24 21:22:07.000000', '2024-07-24 21:22:08.000000', 1,
        'test_contents', 'test_title', 1);
INSERT INTO card (deleted, copy_flag, access_time, card_id, created_at, update_at, user_id, content, title, recruit_id)
VALUES (false, true, '2024-07-24 21:22:04.000000', 6, '2024-07-24 21:22:07.000000', '2024-07-24 21:22:08.000000', 1,
        'test_contents', 'test_title', 1);

INSERT INTO tag (tag_id, name, tag_type, job)
VALUES (1, '스프링', 'CAPABILITY', 'DEVELOPER');
INSERT INTO tag (tag_id, name, tag_type, job)
VALUES (2, '리액트', 'CAPABILITY', 'DEVELOPER');
INSERT INTO tag (tag_id, name, tag_type, job)
VALUES (3, '봉사활동', 'PERSONALITY', 'DESIGNER');

INSERT INTO card_type (card_type_id, card_type_value, card_id, deleted, update_at, created_at)
VALUES (1, 'EXPERIENCE', 1, false, '2024-07-24 21:26:28.000000', '2024-07-24 21:26:28.000000');
INSERT INTO card_type (card_type_id, card_type_value, card_id, deleted, update_at, created_at)
VALUES (2, 'PERSONAL_STATEMENT', 1, false, '2024-07-24 21:26:28.000000', '2024-07-24 21:26:28.000000');
INSERT INTO card_type (card_type_id, card_type_value, card_id, deleted, update_at, created_at)
VALUES (3, 'INTERVIEW_QUESTION', 2, false, '2024-07-24 21:26:28.000000', '2024-07-24 21:26:28.000000');
INSERT INTO card_type (card_type_id, card_type_value, card_id, deleted, update_at, created_at)
VALUES (4, 'EXPERIENCE', 2, false, '2024-07-24 21:26:28.000000', '2024-07-24 21:26:28.000000');
INSERT INTO card_type (card_type_id, card_type_value, card_id, deleted, update_at, created_at)
VALUES (5, 'INTERVIEW_QUESTION', 1, false, '2024-07-24 21:26:28.000000', '2024-07-24 21:26:28.000000');
INSERT INTO card_type (card_type_id, card_type_value, card_id, deleted, update_at, created_at)
VALUES (6, 'INTERVIEW_QUESTION', 2, false, '2024-07-24 21:26:28.000000', '2024-07-24 21:26:28.000000');
INSERT INTO card_type (card_type_id, card_type_value, card_id, deleted, update_at, created_at)
VALUES (7, 'DOCUMENT_PREPARING', 4, false, '2024-07-24 21:26:28.000000', '2024-07-24 21:26:28.000000');
INSERT INTO card_type (card_type_id, card_type_value, card_id, deleted, update_at, created_at)
VALUES (8, 'INTERVIEW_PREPARING', 4, false, '2024-07-24 21:26:28.000000', '2024-07-24 21:26:28.000000');
INSERT INTO card_type (card_type_id, card_type_value, card_id, deleted, update_at, created_at)
VALUES (9, 'ASSIGNMENT_PREPARING', 4, false, '2024-07-24 21:26:28.000000', '2024-07-24 21:26:28.000000');
INSERT INTO card_type (card_type_id, card_type_value, card_id, deleted, update_at, created_at)
VALUES (10, 'INTERVIEW_PREPARING', 5, false, '2024-07-24 21:26:28.000000', '2024-07-24 21:26:28.000000');
INSERT INTO card_type (card_type_id, card_type_value, card_id, deleted, update_at, created_at)
VALUES (11, 'INTERVIEW_PREPARING', 6, false, '2024-07-24 21:26:28.000000', '2024-07-24 21:26:28.000000');
INSERT INTO card_type (card_type_id, card_type_value, card_id, deleted, update_at, created_at)
VALUES (12, 'ASSIGNMENT_PREPARING', 6, false, '2024-07-24 21:26:28.000000', '2024-07-24 21:26:28.000000');

INSERT INTO card_copy_info (card_copy_info_id, created_at, deleted, update_at, copied_time, card_id)
VALUES (1, '2024-08-14 23:20:56.000000', false, '2024-08-14 23:20:59.000000', '2024-08-14 23:21:01.000000', 4);
INSERT INTO card_copy_info (card_copy_info_id, created_at, deleted, update_at, copied_time, card_id)
VALUES (2, '2024-08-14 23:20:56.000000', false, '2024-08-14 23:20:59.000000', '2024-08-14 23:21:01.000000', 5);
INSERT INTO card_copy_info (card_copy_info_id, created_at, deleted, update_at, copied_time, card_id)
VALUES (3, '2024-08-14 23:20:56.000000', false, '2024-08-14 23:20:59.000000', '2024-08-14 23:21:01.000000', 6);


INSERT INTO card_tag (deleted, card_id, card_tag_id, created_at, tag_id, update_at)
VALUES (false, 1, 1, '2024-07-24 21:26:28.000000', 1, '2024-07-24 21:26:40.000000');
INSERT INTO card_tag (deleted, card_id, card_tag_id, created_at, tag_id, update_at)
VALUES (false, 1, 2, '2024-07-24 21:26:29.000000', 2, '2024-07-24 21:26:41.000000');
INSERT INTO card_tag (deleted, card_id, card_tag_id, created_at, tag_id, update_at)
VALUES (false, 2, 3, '2024-07-24 21:26:31.000000', 2, '2024-07-24 21:26:42.000000');

INSERT INTO card_memo (deleted, card_id, card_memo_id, created_at, content, update_at)
VALUES (false, 1, 1, '2024-07-24 21:26:31.000000', 'test contents 111', '2024-07-24 21:26:42.000000');
INSERT INTO card_memo (deleted, card_id, card_memo_id, created_at, content, update_at)
VALUES (false, 1, 2, '2024-07-24 21:26:31.000000', 'test contents 222', '2024-07-24 21:26:42.000000');
INSERT INTO card_memo (deleted, card_id, card_memo_id, created_at, content, update_at)
VALUES (false, 1, 3, '2024-07-24 21:26:31.000000', 'test contents 222', '2024-07-24 21:26:42.000000');
INSERT INTO card_memo (deleted, card_id, card_memo_id, created_at, content, update_at)
VALUES (false, 2, 4, '2024-07-24 21:26:31.000000', 'test contents 111', '2024-07-24 21:26:42.000000');
INSERT INTO card_memo (deleted, card_id, card_memo_id, created_at, content, update_at)
VALUES (false, 2, 5, '2024-07-24 21:26:31.000000', 'test contents 222', '2024-07-24 21:26:42.000000');
INSERT INTO card_memo (deleted, card_id, card_memo_id, created_at, content, update_at)
VALUES (false, 3, 6, '2024-07-24 21:26:31.000000', 'test contents 333', '2024-07-24 21:26:42.000000');




