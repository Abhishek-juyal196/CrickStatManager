-- Insert sample leagues
INSERT INTO leagues (name, start_date, end_date, status) VALUES
('IPL 2024', '2024-03-22', '2024-05-26', 'UPCOMING'),
('World Cup 2024', '2024-10-05', '2024-11-19', 'UPCOMING'),
('County Championship 2024', '2024-04-05', '2024-09-28', 'ONGOING');

-- Insert sample teams for IPL 2024
INSERT INTO teams (name, logo_url, league_id) VALUES
('Mumbai Indians', 'https://example.com/mi-logo.png', 1),
('Chennai Super Kings', 'https://example.com/csk-logo.png', 1),
('Royal Challengers Bangalore', 'https://example.com/rcb-logo.png', 1),
('Delhi Capitals', 'https://example.com/dc-logo.png', 1);

-- Insert sample players for Mumbai Indians
INSERT INTO players (name, date_of_birth, batting_style, bowling_style, team_id) VALUES
('Rohit Sharma', '1987-04-30', 'RIGHT_HANDED', 'RIGHT_ARM_OFF_SPIN', 1),
('Suryakumar Yadav', '1990-09-14', 'RIGHT_HANDED', 'RIGHT_ARM_LEG_SPIN', 1),
('Jasprit Bumrah', '1993-12-06', 'RIGHT_HANDED', 'RIGHT_ARM_FAST', 1),
('Hardik Pandya', '1993-10-11', 'RIGHT_HANDED', 'RIGHT_ARM_MEDIUM', 1);

-- Insert sample players for Chennai Super Kings
INSERT INTO players (name, date_of_birth, batting_style, bowling_style, team_id) VALUES
('MS Dhoni', '1981-07-07', 'RIGHT_HANDED', 'RIGHT_ARM_MEDIUM', 2),
('Ravindra Jadeja', '1988-12-06', 'LEFT_HANDED', 'LEFT_ARM_ORTHODOX', 2),
('Ruturaj Gaikwad', '1997-01-31', 'RIGHT_HANDED', 'RIGHT_ARM_OFF_SPIN', 2),
('Deepak Chahar', '1992-08-07', 'RIGHT_HANDED', 'RIGHT_ARM_MEDIUM', 2);

-- Insert sample players for Royal Challengers Bangalore
INSERT INTO players (name, date_of_birth, batting_style, bowling_style, team_id) VALUES
('Virat Kohli', '1988-11-05', 'RIGHT_HANDED', 'RIGHT_ARM_MEDIUM', 3),
('Faf du Plessis', '1984-07-13', 'RIGHT_HANDED', 'RIGHT_ARM_LEG_SPIN', 3),
('Glenn Maxwell', '1988-10-14', 'RIGHT_HANDED', 'RIGHT_ARM_OFF_SPIN', 3),
('Mohammed Siraj', '1994-03-13', 'RIGHT_HANDED', 'RIGHT_ARM_FAST', 3);

-- Insert sample players for Delhi Capitals
INSERT INTO players (name, date_of_birth, batting_style, bowling_style, team_id) VALUES
('Rishabh Pant', '1997-10-04', 'LEFT_HANDED', 'RIGHT_ARM_OFF_SPIN', 4),
('Prithvi Shaw', '1999-11-09', 'RIGHT_HANDED', 'RIGHT_ARM_OFF_SPIN', 4),
('Axar Patel', '1994-01-20', 'LEFT_HANDED', 'LEFT_ARM_ORTHODOX', 4),
('Anrich Nortje', '1993-11-16', 'RIGHT_HANDED', 'RIGHT_ARM_FAST', 4);

-- Insert sample matches
INSERT INTO matches (league_id, team1_id, team2_id, venue, match_date, status) VALUES
(1, 1, 2, 'Wankhede Stadium, Mumbai', '2024-03-22', 'SCHEDULED'),
(1, 3, 4, 'M. Chinnaswamy Stadium, Bangalore', '2024-03-23', 'SCHEDULED'),
(1, 2, 3, 'MA Chidambaram Stadium, Chennai', '2024-03-24', 'SCHEDULED'),
(1, 1, 4, 'Arun Jaitley Stadium, Delhi', '2024-03-25', 'SCHEDULED');
