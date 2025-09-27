package com.cricket.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "match_scorecards")
public class MatchScorecard {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Match is required")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;
    
    @Column(name = "team1_runs")
    private Integer team1Runs = 0;
    
    @Column(name = "team1_wickets")
    private Integer team1Wickets = 0;
    
    @Column(name = "team1_overs")
    private Double team1Overs = 0.0;
    
    @Column(name = "team2_runs")
    private Integer team2Runs = 0;
    
    @Column(name = "team2_wickets")
    private Integer team2Wickets = 0;
    
    @Column(name = "team2_overs")
    private Double team2Overs = 0.0;
    
    @Column(name = "winning_team_id")
    private Long winningTeamId;
    
    @Column(name = "win_margin")
    private String winMargin;
    
    @Column(name = "man_of_the_match_id")
    private Long manOfTheMatchId;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public MatchScorecard() {}
    
    public MatchScorecard(Match match) {
        this.match = match;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Match getMatch() {
        return match;
    }
    
    public void setMatch(Match match) {
        this.match = match;
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
