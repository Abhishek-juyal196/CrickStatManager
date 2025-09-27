package com.cricket.dto;

import com.cricket.entity.MatchScorecard;
import com.cricket.entity.Match;
import com.cricket.entity.Team;
import jakarta.validation.constraints.NotNull;

public class ScorecardDTO {
    
    private Long id;
    
    @NotNull(message = "Match ID is required")
    private Long matchId;
    
    private Integer team1Runs;
    private Integer team1Wickets;
    private Double team1Overs;
    private Integer team2Runs;
    private Integer team2Wickets;
    private Double team2Overs;
    private Long winningTeamId;
    private String winMargin;
    private Long manOfTheMatchId;
    private String team1Name;
    private String team2Name;
    private String winningTeamName;
    private String manOfTheMatchName;
    
    public ScorecardDTO() {}
    
    public ScorecardDTO(MatchScorecard scorecard) {
        this.id = scorecard.getId();
        this.matchId = scorecard.getMatch() != null ? scorecard.getMatch().getId() : null;
        this.team1Runs = scorecard.getTeam1Runs();
        this.team1Wickets = scorecard.getTeam1Wickets();
        this.team1Overs = scorecard.getTeam1Overs();
        this.team2Runs = scorecard.getTeam2Runs();
        this.team2Wickets = scorecard.getTeam2Wickets();
        this.team2Overs = scorecard.getTeam2Overs();
        this.winningTeamId = scorecard.getWinningTeamId();
        this.winMargin = scorecard.getWinMargin();
        this.manOfTheMatchId = scorecard.getManOfTheMatchId();
        
        // Safe access to team names with null checks
        if (scorecard.getMatch() != null) {
            Match match = scorecard.getMatch();
            this.team1Name = getTeamName(match.getTeam1());
            this.team2Name = getTeamName(match.getTeam2());
            
            // Set winning team name if available
            if (this.winningTeamId != null) {
                if (isWinningTeam(match.getTeam1(), this.winningTeamId)) {
                    this.winningTeamName = getTeamName(match.getTeam1());
                } else if (isWinningTeam(match.getTeam2(), this.winningTeamId)) {
                    this.winningTeamName = getTeamName(match.getTeam2());
                }
            }
        }
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getMatchId() {
        return matchId;
    }
    
    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }
    
    public Integer getTeam1Runs() {
        return team1Runs;
    }
    
    public void setTeam1Runs(Integer team1Runs) {
        this.team1Runs = team1Runs;
    }
    
    public Integer getTeam1Wickets() {
        return team1Wickets;
    }
    
    public void setTeam1Wickets(Integer team1Wickets) {
        this.team1Wickets = team1Wickets;
    }
    
    public Double getTeam1Overs() {
        return team1Overs;
    }
    
    public void setTeam1Overs(Double team1Overs) {
        this.team1Overs = team1Overs;
    }
    
    public Integer getTeam2Runs() {
        return team2Runs;
    }
    
    public void setTeam2Runs(Integer team2Runs) {
        this.team2Runs = team2Runs;
    }
    
    public Integer getTeam2Wickets() {
        return team2Wickets;
    }
    
    public void setTeam2Wickets(Integer team2Wickets) {
        this.team2Wickets = team2Wickets;
    }
    
    public Double getTeam2Overs() {
        return team2Overs;
    }
    
    public void setTeam2Overs(Double team2Overs) {
        this.team2Overs = team2Overs;
    }
    
    public Long getWinningTeamId() {
        return winningTeamId;
    }
    
    public void setWinningTeamId(Long winningTeamId) {
        this.winningTeamId = winningTeamId;
    }
    
    public String getWinMargin() {
        return winMargin;
    }
    
    public void setWinMargin(String winMargin) {
        this.winMargin = winMargin;
    }
    
    public Long getManOfTheMatchId() {
        return manOfTheMatchId;
    }
    
    public void setManOfTheMatchId(Long manOfTheMatchId) {
        this.manOfTheMatchId = manOfTheMatchId;
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
    
    public String getWinningTeamName() {
        return winningTeamName;
    }
    
    public void setWinningTeamName(String winningTeamName) {
        this.winningTeamName = winningTeamName;
    }
    
    public String getManOfTheMatchName() {
        return manOfTheMatchName;
    }
    
    public void setManOfTheMatchName(String manOfTheMatchName) {
        this.manOfTheMatchName = manOfTheMatchName;
    }
    
    // Helper method to safely get team name
    private String getTeamName(Team team) {
        return team != null ? team.getName() : null;
    }
    
    // Helper method to check if team is winning team
    private boolean isWinningTeam(Team team, Long winningTeamId) {
        return team != null && winningTeamId != null && team.getId().equals(winningTeamId);
    }
}
