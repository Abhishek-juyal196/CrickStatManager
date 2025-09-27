package com.cricket.dto;

import com.cricket.entity.Ball;
import jakarta.validation.constraints.NotNull;

public class BallDTO {
    
    private Long id;
    
    @NotNull(message = "Batsman ID is required")
    private Long batsmanId;
    
    @NotNull(message = "Bowler ID is required")
    private Long bowlerId;
    
    private Integer runs = 0;
    
    private Boolean isWicket = false;
    
    private Ball.WicketType wicketType;
    
    private Ball.ExtraType extraType;
    
    private Integer extraRuns = 0;
    
    private Boolean isWide = false;
    
    private Boolean isNoBall = false;
    
    private Boolean isBye = false;
    
    private Boolean isLegBye = false;
    
    public BallDTO() {}
    
    public BallDTO(Ball ball) {
        this.id = ball.getId();
        this.batsmanId = ball.getBatsman().getId();
        this.bowlerId = ball.getBowler().getId();
        this.runs = ball.getRuns();
        this.isWicket = ball.getIsWicket();
        this.wicketType = ball.getWicketType();
        this.extraType = ball.getExtraType();
        this.extraRuns = ball.getExtraRuns();
        this.isWide = ball.getIsWide();
        this.isNoBall = ball.getIsNoBall();
        this.isBye = ball.getIsBye();
        this.isLegBye = ball.getIsLegBye();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getBatsmanId() {
        return batsmanId;
    }
    
    public void setBatsmanId(Long batsmanId) {
        this.batsmanId = batsmanId;
    }
    
    public Long getBowlerId() {
        return bowlerId;
    }
    
    public void setBowlerId(Long bowlerId) {
        this.bowlerId = bowlerId;
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
    
    public Ball.WicketType getWicketType() {
        return wicketType;
    }
    
    public void setWicketType(Ball.WicketType wicketType) {
        this.wicketType = wicketType;
    }
    
    public Ball.ExtraType getExtraType() {
        return extraType;
    }
    
    public void setExtraType(Ball.ExtraType extraType) {
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
}
