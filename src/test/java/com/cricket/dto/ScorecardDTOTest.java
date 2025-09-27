package com.cricket.dto;

import com.cricket.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ScorecardDTOTest {

    private MatchScorecard scorecard;
    private Match match;
    private Team team1;
    private Team team2;
    private League league;

    @BeforeEach
    void setUp() {
        // Create test data
        league = new League();
        league.setId(1L);
        league.setName("Test League");

        team1 = new Team();
        team1.setId(1L);
        team1.setName("Team 1");
        team1.setLeague(league);

        team2 = new Team();
        team2.setId(2L);
        team2.setName("Team 2");
        team2.setLeague(league);

        match = new Match();
        match.setId(1L);
        match.setLeague(league);
        match.setTeam1(team1);
        match.setTeam2(team2);
        match.setVenue("Test Venue");
        match.setMatchDate(LocalDate.now());

        scorecard = new MatchScorecard();
        scorecard.setId(1L);
        scorecard.setMatch(match);
        scorecard.setTeam1Runs(150);
        scorecard.setTeam1Wickets(5);
        scorecard.setTeam1Overs(20.0);
        scorecard.setTeam2Runs(140);
        scorecard.setTeam2Wickets(8);
        scorecard.setTeam2Overs(19.5);
        scorecard.setWinningTeamId(1L);
        scorecard.setWinMargin("10 runs");
    }

    @Test
    void constructor_WithValidScorecard_ShouldCreateDTO() {
        // When
        ScorecardDTO dto = new ScorecardDTO(scorecard);

        // Then
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(1L, dto.getMatchId());
        assertEquals(150, dto.getTeam1Runs());
        assertEquals(5, dto.getTeam1Wickets());
        assertEquals(20.0, dto.getTeam1Overs());
        assertEquals(140, dto.getTeam2Runs());
        assertEquals(8, dto.getTeam2Wickets());
        assertEquals(19.5, dto.getTeam2Overs());
        assertEquals(1L, dto.getWinningTeamId());
        assertEquals("10 runs", dto.getWinMargin());
        assertEquals("Team 1", dto.getTeam1Name());
        assertEquals("Team 2", dto.getTeam2Name());
        assertEquals("Team 1", dto.getWinningTeamName());
    }

    @Test
    void constructor_WithNullMatch_ShouldHandleGracefully() {
        // Given
        scorecard.setMatch(null);

        // When
        ScorecardDTO dto = new ScorecardDTO(scorecard);

        // Then
        assertNotNull(dto);
        assertNull(dto.getMatchId());
        assertNull(dto.getTeam1Name());
        assertNull(dto.getTeam2Name());
        assertNull(dto.getWinningTeamName());
    }

    @Test
    void constructor_WithNullTeams_ShouldHandleGracefully() {
        // Given
        match.setTeam1(null);
        match.setTeam2(null);

        // When
        ScorecardDTO dto = new ScorecardDTO(scorecard);

        // Then
        assertNotNull(dto);
        assertNull(dto.getTeam1Name());
        assertNull(dto.getTeam2Name());
        assertNull(dto.getWinningTeamName());
    }

    @Test
    void defaultConstructor_ShouldCreateEmptyDTO() {
        // When
        ScorecardDTO dto = new ScorecardDTO();

        // Then
        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getMatchId());
    }
}
