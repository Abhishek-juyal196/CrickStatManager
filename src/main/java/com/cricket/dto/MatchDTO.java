package com.cricket.dto;

import com.cricket.entity.Match;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class MatchDTO {
    
    private Long id;
    
    @NotNull(message = "League ID is required")
    private Long leagueId;
    
    @NotNull(message = "Team 1 ID is required")
    private Long team1Id;
    
    @NotNull(message = "Team 2 ID is required")
    private Long team2Id;
    
    @NotBlank(message = "Venue is required")
    private String venue;
    
    @NotNull(message = "Match date is required")
    private LocalDate matchDate;
    
    private Match.MatchStatus status;
    
    private String team1Name;
    private String team2Name;
    
    public MatchDTO() {}
    
    public MatchDTO(Match match) {
        this.id = match.getId();
        this.leagueId = match.getLeague().getId();
        this.team1Id = match.getTeam1().getId();
        this.team2Id = match.getTeam2().getId();
        this.venue = match.getVenue();
        this.matchDate = match.getMatchDate();
        this.status = match.getStatus();
        this.team1Name = match.getTeam1().getName();
        this.team2Name = match.getTeam2().getName();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getLeagueId() {
        return leagueId;
    }
    
    public void setLeagueId(Long leagueId) {
        this.leagueId = leagueId;
    }
    
    public Long getTeam1Id() {
        return team1Id;
    }
    
    public void setTeam1Id(Long team1Id) {
        this.team1Id = team1Id;
    }
    
    public Long getTeam2Id() {
        return team2Id;
    }
    
    public void setTeam2Id(Long team2Id) {
        this.team2Id = team2Id;
    }
    
    public String getVenue() {
        return venue;
    }
    
    public void setVenue(String venue) {
        this.venue = venue;
    }
    
    public LocalDate getMatchDate() {
        return matchDate;
    }
    
    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }
    
    public Match.MatchStatus getStatus() {
        return status;
    }
    
    public void setStatus(Match.MatchStatus status) {
        this.status = status;
    }
    
    public String getTeam1Name() {
        return team1Name;
    }
    
    public void setTeam1Name(String team1Name) {
        this.team1Name = team1Name;
    }
    
    public String getTeam2Name() {
        return team2Name;
    }
    
    public void setTeam2Name(String team2Name) {
        this.team2Name = team2Name;
    }
}
