package com.cricket.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "innings")
public class Innings {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Match is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;
    
    @NotNull(message = "Batting team is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batting_team_id", nullable = false)
    private Team battingTeam;
    
    @NotNull(message = "Bowling team is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bowling_team_id", nullable = false)
    private Team bowlingTeam;
    
    @Column(name = "innings_number", nullable = false)
    private Integer inningsNumber;
    
    @Column(name = "total_runs", nullable = false)
    private Integer totalRuns = 0;
    
    @Column(name = "wickets", nullable = false)
    private Integer wickets = 0;
    
    @Column(name = "overs", nullable = false)
    private Double overs = 0.0;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InningsStatus status = InningsStatus.NOT_STARTED;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "innings", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ball> balls = new ArrayList<>();
    
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
    public Innings() {}
    
    public Innings(Match match, Team battingTeam, Team bowlingTeam, Integer inningsNumber) {
        this.match = match;
        this.battingTeam = battingTeam;
        this.bowlingTeam = bowlingTeam;
        this.inningsNumber = inningsNumber;
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
    
    public Team getBattingTeam() {
        return battingTeam;
    }
    
    public void setBattingTeam(Team battingTeam) {
        this.battingTeam = battingTeam;
    }
    
    public Team getBowlingTeam() {
        return bowlingTeam;
    }
    
    public void setBowlingTeam(Team bowlingTeam) {
        this.bowlingTeam = bowlingTeam;
    }
    
    public Integer getInningsNumber() {
        return inningsNumber;
    }
    
    public void setInningsNumber(Integer inningsNumber) {
        this.inningsNumber = inningsNumber;
    }
    
    public Integer getTotalRuns() {
        return totalRuns;
    }
    
    public void setTotalRuns(Integer totalRuns) {
        this.totalRuns = totalRuns;
    }
    
    public Integer getWickets() {
        return wickets;
    }
    
    public void setWickets(Integer wickets) {
        this.wickets = wickets;
    }
    
    public Double getOvers() {
        return overs;
    }
    
    public void setOvers(Double overs) {
        this.overs = overs;
    }
    
    public InningsStatus getStatus() {
        return status;
    }
    
    public void setStatus(InningsStatus status) {
        this.status = status;
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
    
    public List<Ball> getBalls() {
        return balls;
    }
    
    public void setBalls(List<Ball> balls) {
        this.balls = balls;
    }
    
    public enum InningsStatus {
        NOT_STARTED, IN_PROGRESS, COMPLETED, DECLARED
    }
}
