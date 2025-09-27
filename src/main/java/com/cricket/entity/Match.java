package com.cricket.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matches")
public class Match {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "League is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id", nullable = false)
    private League league;
    
    @NotNull(message = "Team 1 is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team1_id", nullable = false)
    private Team team1;
    
    @NotNull(message = "Team 2 is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team2_id", nullable = false)
    private Team team2;
    
    @NotBlank(message = "Venue is required")
    @Column(nullable = false)
    private String venue;
    
    @NotNull(message = "Match date is required")
    @Column(name = "match_date", nullable = false)
    private LocalDate matchDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchStatus status = MatchStatus.SCHEDULED;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Innings> innings = new ArrayList<>();
    
    @OneToOne(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MatchScorecard scorecard;
    
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
    public Match() {}
    
    public Match(League league, Team team1, Team team2, String venue, LocalDate matchDate) {
        this.league = league;
        this.team1 = team1;
        this.team2 = team2;
        this.venue = venue;
        this.matchDate = matchDate;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public League getLeague() {
        return league;
    }
    
    public void setLeague(League league) {
        this.league = league;
    }
    
    public Team getTeam1() {
        return team1;
    }
    
    public void setTeam1(Team team1) {
        this.team1 = team1;
    }
    
    public Team getTeam2() {
        return team2;
    }
    
    public void setTeam2(Team team2) {
        this.team2 = team2;
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
    
    public MatchStatus getStatus() {
        return status;
    }
    
    public void setStatus(MatchStatus status) {
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
    
    public List<Innings> getInnings() {
        return innings;
    }
    
    public void setInnings(List<Innings> innings) {
        this.innings = innings;
    }
    
    public MatchScorecard getScorecard() {
        return scorecard;
    }
    
    public void setScorecard(MatchScorecard scorecard) {
        this.scorecard = scorecard;
    }
    
    public enum MatchStatus {
        SCHEDULED, IN_PROGRESS, COMPLETED, ABANDONED, CANCELLED
    }
}
