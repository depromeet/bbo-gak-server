
delete
from refresh_token;

delete
from notification;

delete
from recruit_schedule;

delete
from recruit;

delete
from recruit_season;

delete
from users;

INSERT INTO users (deleted, created_at, update_at, user_id, dtype, email, login_id, name, password, role, job)
VALUES (false, '2024-07-24 21:27:20.000000', '2024-07-24 21:27:21.000000', 1, 'AuthTestUser', 'email', 'test', 'test',
        'test123', 'USER', 'DEVELOPER');

INSERT INTO users( deleted, created_at, update_at, user_id, dtype, role, oauth_id, name, email, provider, job)
VALUES(false, '2024-07-24 21:27:20.000000', '2024-07-24 21:27:21.000000', 2, 'User', 'USER', 'oauthId', 'name', 'email', 'GOOGLE', 'DEVELOPER');

INSERT INTO refresh_token (id, token)
VALUES (1, 'abcd1234');
