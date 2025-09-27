package com.cricket.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "players")
public class Player {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Player name is required")
    @Column(nullable = false)
    private String name;
    
    @NotNull(message = "Date of birth is required")
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "batting_style")
    private BattingStyle battingStyle;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "bowling_style")
    private BowlingStyle bowlingStyle;
    
    @NotNull(message = "Team is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "batsman", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ball> ballsAsBatsman = new ArrayList<>();
    
    @OneToMany(mappedBy = "bowler", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ball> ballsAsBowler = new ArrayList<>();
    
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
    public Player() {}
    
    public Player(String name, LocalDate dateOfBirth, Team team) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.team = team;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public BattingStyle getBattingStyle() {
        return battingStyle;
    }
    
    public void setBattingStyle(BattingStyle battingStyle) {
        this.battingStyle = battingStyle;
    }
    
    public BowlingStyle getBowlingStyle() {
        return bowlingStyle;
    }
    
    public void setBowlingStyle(BowlingStyle bowlingStyle) {
        this.bowlingStyle = bowlingStyle;
    }
    
    public Team getTeam() {
        return team;
    }
    
    public void setTeam(Team team) {
        this.team = team;
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
    
    public List<Ball> getBallsAsBatsman() {
        return ballsAsBatsman;
    }
    
    public void setBallsAsBatsman(List<Ball> ballsAsBatsman) {
        this.ballsAsBatsman = ballsAsBatsman;
    }
    
    public List<Ball> getBallsAsBowler() {
        return ballsAsBowler;
    }
    
    public void setBallsAsBowler(List<Ball> ballsAsBowler) {
        this.ballsAsBowler = ballsAsBowler;
    }
    
    public enum BattingStyle {
        RIGHT_HANDED, LEFT_HANDED
    }
    
    public enum BowlingStyle {
        RIGHT_ARM_FAST, RIGHT_ARM_MEDIUM, RIGHT_ARM_SPIN,
        LEFT_ARM_FAST, LEFT_ARM_MEDIUM, LEFT_ARM_SPIN,
        RIGHT_ARM_LEG_SPIN, LEFT_ARM_ORTHODOX, RIGHT_ARM_OFF_SPIN
    }
}
