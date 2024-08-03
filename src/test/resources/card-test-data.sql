delete
from card_tag;
delete
from tag;
delete
from card;
delete
from users;

INSERT INTO users (deleted, created_at, update_at, user_id, dtype, email, login_id, name, password, role)
VALUES (false, '2024-07-24 21:27:20.000000', '2024-07-24 21:27:21.000000', 1, '1', null, 'test', 'test', 'test',
        'USER');

INSERT INTO card (deleted, copy_flag, access_time, card_id, created_at, update_at, user_id, content, title, card_type)
VALUES (false, false, '2024-07-24 21:22:04.000000', 1, '2024-07-24 21:22:07.000000', '2024-07-24 21:22:08.000000', 1,
        'test_contents', 'test_title', 'EXPERIENCE');
INSERT INTO card (deleted, copy_flag, access_time, card_id, created_at, update_at, user_id, content, title, card_type)
VALUES (false, false, '1970-01-01 00:00:00.001000', 2, '1970-01-01 00:00:00.001000', '1970-01-01 00:00:00.001000', 1,
        'testc',
        'testc', 'EXPERIENCE');
INSERT INTO card (deleted, copy_flag, access_time, card_id, created_at, update_at, user_id, content, title, card_type)
VALUES (false, false, '1970-01-01 00:00:00.001000', 3, '1970-01-01 00:00:00.001000', '1970-01-01 00:00:00.001000', 1,
        'testc',
        'testt', 'PERSONAL_STATEMENT');

INSERT INTO tag (tag_id, name, tag_type)
VALUES (1, '스프링', 'CAPABILITY');
INSERT INTO tag (tag_id, name, tag_type)
VALUES (2, '리액트', 'CAPABILITY');
INSERT INTO tag (tag_id, name, tag_type)
VALUES (3, '봉사활동
', 'PERSONALITY');

INSERT INTO card_tag (deleted, card_id, card_tag_id, created_at, tag_id, update_at)
VALUES (false, 1, 1, '2024-07-24 21:26:28.000000', 1, '2024-07-24 21:26:40.000000');
INSERT INTO card_tag (deleted, card_id, card_tag_id, created_at, tag_id, update_at)
VALUES (false, 1, 2, '2024-07-24 21:26:29.000000', 2, '2024-07-24 21:26:41.000000');
INSERT INTO card_tag (deleted, card_id, card_tag_id, created_at, tag_id, update_at)
VALUES (false, 2, 3, '2024-07-24 21:26:31.000000', 2, '2024-07-24 21:26:42.000000');
