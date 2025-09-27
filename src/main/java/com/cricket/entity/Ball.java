package com.cricket.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "balls")
public class Ball {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Innings is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "innings_id", nullable = false)
    private Innings innings;
    
    @Column(name = "over_number", nullable = false)
    private Integer overNumber;
    
    @Column(name = "ball_number", nullable = false)
    private Integer ballNumber;
    
    @NotNull(message = "Batsman is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batsman_id", nullable = false)
    private Player batsman;
    
    @NotNull(message = "Bowler is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bowler_id", nullable = false)
    private Player bowler;
    
    @Column(nullable = false)
    private Integer runs = 0;
    
    @Column(name = "is_wicket", nullable = false)
    private Boolean isWicket = false;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "wicket_type")
    private WicketType wicketType;
    
    @Enumerated(EnumType.STRING)
    private ExtraType extraType;
    
    @Column(name = "extra_runs")
    private Integer extraRuns = 0;
    
    @Column(name = "is_wide")
    private Boolean isWide = false;
    
    @Column(name = "is_no_ball")
    private Boolean isNoBall = false;
    
    @Column(name = "is_bye")
    private Boolean isBye = false;
    
    @Column(name = "is_leg_bye")
    private Boolean isLegBye = false;
    
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
    public Ball() {}
    
    public Ball(Innings innings, Integer overNumber, Integer ballNumber, Player batsman, Player bowler) {
        this.innings = innings;
        this.overNumber = overNumber;
        this.ballNumber = ballNumber;
        this.batsman = batsman;
        this.bowler = bowler;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Innings getInnings() {
        return innings;
    }
    
    public void setInnings(Innings innings) {
        this.innings = innings;
    }
    
    public Integer getOverNumber() {
        return overNumber;
    }
    
    public void setOverNumber(Integer overNumber) {
        this.overNumber = overNumber;
    }
    
    public Integer getBallNumber() {
        return ballNumber;
    }
    
    public void setBallNumber(Integer ballNumber) {
        this.ballNumber = ballNumber;
    }
    
    public Player getBatsman() {
        return batsman;
    }
    
    public void setBatsman(Player batsman) {
        this.batsman = batsman;
    }
    
    public Player getBowler() {
        return bowler;
    }
    
    public void setBowler(Player bowler) {
        this.bowler = bowler;
    }
    
    public Integer getRuns() {
        return runs;
    }
    
    public void setRuns(Integer runs) {
        this.runs = runs;
    }
    
    public Boolean getIsWicket() {
        return isWicket;
    }
    
    public void setIsWicket(Boolean isWicket) {
        this.isWicket = isWicket;
    }
    
    public WicketType getWicketType() {
        return wicketType;
    }
    
    public void setWicketType(WicketType wicketType) {
        this.wicketType = wicketType;
    }
    
    public ExtraType getExtraType() {
        return extraType;
    }
    
    public void setExtraType(ExtraType extraType) {
        this.extraType = extraType;
    }
    
    public Integer getExtraRuns() {
        return extraRuns;
    }
    
    public void setExtraRuns(Integer extraRuns) {
        this.extraRuns = extraRuns;
    }
    
    public Boolean getIsWide() {
        return isWide;
    }
    
    public void setIsWide(Boolean isWide) {
        this.isWide = isWide;
    }
    
    public Boolean getIsNoBall() {
        return isNoBall;
    }
    
    public void setIsNoBall(Boolean isNoBall) {
        this.isNoBall = isNoBall;
    }
    
    public Boolean getIsBye() {
        return isBye;
    }
    
    public void setIsBye(Boolean isBye) {
        this.isBye = isBye;
    }
    
    public Boolean getIsLegBye() {
        return isLegBye;
    }
    
    public void setIsLegBye(Boolean isLegBye) {
        this.isLegBye = isLegBye;
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
    
    public enum WicketType {
        BOWLED, CAUGHT, LBW, RUN_OUT, STUMPED, HIT_WICKET, HANDLED_BALL, OBSTRUCTING_FIELD, TIMED_OUT, RETIRED_HURT
    }
    
    public enum ExtraType {
        WIDE, NO_BALL, BYE, LEG_BYE, PENALTY
    }
}
