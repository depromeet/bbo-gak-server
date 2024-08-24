INSERT INTO users (deleted, created_at, update_at, user_id, dtype, email, login_id, name, password, role, job)
VALUES (false, '2024-07-24 21:27:20.000000', '2024-07-24 21:27:21.000000', 1, 'AuthTestUser', 'email', 'test', 'test',
        'test123', 'USER', 'DEVELOPER'),
       (false, '2024-07-24 21:27:20.000000', '2024-07-24 21:27:21.000000', 2, 'AuthTestUser', 'email', 'test', 'test',
        'test123', 'USER', 'DEVELOPER');

INSERT INTO recruit_season (recruit_season_id, name, user_id)
VALUES (1, '2024 상반기', 2),
       (2, '2024 하반기', 2),
       (3, '2025 상반기', 2);





