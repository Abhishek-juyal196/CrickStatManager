-- Create leagues table
CREATE TABLE leagues (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'UPCOMING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create teams table
CREATE TABLE teams (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    logo_url VARCHAR(500),
    league_id BIGINT NOT NULL REFERENCES leagues(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create players table
CREATE TABLE players (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    batting_style VARCHAR(50),
    bowling_style VARCHAR(50),
    team_id BIGINT NOT NULL REFERENCES teams(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create matches table
CREATE TABLE matches (
    id BIGSERIAL PRIMARY KEY,
    league_id BIGINT NOT NULL REFERENCES leagues(id) ON DELETE CASCADE,
    team1_id BIGINT NOT NULL REFERENCES teams(id) ON DELETE CASCADE,
    team2_id BIGINT NOT NULL REFERENCES teams(id) ON DELETE CASCADE,
    venue VARCHAR(255) NOT NULL,
    match_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create innings table
CREATE TABLE innings (
    id BIGSERIAL PRIMARY KEY,
    match_id BIGINT NOT NULL REFERENCES matches(id) ON DELETE CASCADE,
    batting_team_id BIGINT NOT NULL REFERENCES teams(id) ON DELETE CASCADE,
    bowling_team_id BIGINT NOT NULL REFERENCES teams(id) ON DELETE CASCADE,
    innings_number INTEGER NOT NULL,
    total_runs INTEGER NOT NULL DEFAULT 0,
    wickets INTEGER NOT NULL DEFAULT 0,
    overs DECIMAL(5,2) NOT NULL DEFAULT 0.0,
    status VARCHAR(20) NOT NULL DEFAULT 'NOT_STARTED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create balls table
CREATE TABLE balls (
    id BIGSERIAL PRIMARY KEY,
    innings_id BIGINT NOT NULL REFERENCES innings(id) ON DELETE CASCADE,
    over_number INTEGER NOT NULL,
    ball_number INTEGER NOT NULL,
    batsman_id BIGINT NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    bowler_id BIGINT NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    runs INTEGER NOT NULL DEFAULT 0,
    is_wicket BOOLEAN NOT NULL DEFAULT FALSE,
    wicket_type VARCHAR(50),
    extra_type VARCHAR(50),
    extra_runs INTEGER DEFAULT 0,
    is_wide BOOLEAN DEFAULT FALSE,
    is_no_ball BOOLEAN DEFAULT FALSE,
    is_bye BOOLEAN DEFAULT FALSE,
    is_leg_bye BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create match_scorecards table
CREATE TABLE match_scorecards (
    id BIGSERIAL PRIMARY KEY,
    match_id BIGINT NOT NULL REFERENCES matches(id) ON DELETE CASCADE,
    team1_runs INTEGER DEFAULT 0,
    team1_wickets INTEGER DEFAULT 0,
    team1_overs DECIMAL(5,2) DEFAULT 0.0,
    team2_runs INTEGER DEFAULT 0,
    team2_wickets INTEGER DEFAULT 0,
    team2_overs DECIMAL(5,2) DEFAULT 0.0,
    winning_team_id BIGINT,
    win_margin VARCHAR(100),
    man_of_the_match_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX idx_teams_league_id ON teams(league_id);
CREATE INDEX idx_players_team_id ON players(team_id);
CREATE INDEX idx_matches_league_id ON matches(league_id);
CREATE INDEX idx_matches_team1_id ON matches(team1_id);
CREATE INDEX idx_matches_team2_id ON matches(team2_id);
CREATE INDEX idx_innings_match_id ON innings(match_id);
CREATE INDEX idx_innings_batting_team_id ON innings(batting_team_id);
CREATE INDEX idx_innings_bowling_team_id ON innings(bowling_team_id);
CREATE INDEX idx_balls_innings_id ON balls(innings_id);
CREATE INDEX idx_balls_batsman_id ON balls(batsman_id);
CREATE INDEX idx_balls_bowler_id ON balls(bowler_id);
CREATE INDEX idx_match_scorecards_match_id ON match_scorecards(match_id);

-- Create triggers for updated_at timestamps
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_leagues_updated_at BEFORE UPDATE ON leagues FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_teams_updated_at BEFORE UPDATE ON teams FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_players_updated_at BEFORE UPDATE ON players FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_matches_updated_at BEFORE UPDATE ON matches FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_innings_updated_at BEFORE UPDATE ON innings FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_balls_updated_at BEFORE UPDATE ON balls FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_match_scorecards_updated_at BEFORE UPDATE ON match_scorecards FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
