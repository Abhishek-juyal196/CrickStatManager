package com.cricket.dto;

import com.cricket.entity.Player;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class PlayerDTO {
    
    private Long id;
    
    @NotBlank(message = "Player name is required")
    private String name;
    
    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;
    
    private Player.BattingStyle battingStyle;
    
    private Player.BowlingStyle bowlingStyle;
    
    @NotNull(message = "Team ID is required")
    private Long teamId;
    
    public PlayerDTO() {}
    
    public PlayerDTO(Player player) {
        this.id = player.getId();
        this.name = player.getName();
        this.dateOfBirth = player.getDateOfBirth();
        this.battingStyle = player.getBattingStyle();
        this.bowlingStyle = player.getBowlingStyle();
        this.teamId = player.getTeam().getId();
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
    
    public Player.BattingStyle getBattingStyle() {
        return battingStyle;
    }
    
    public void setBattingStyle(Player.BattingStyle battingStyle) {
        this.battingStyle = battingStyle;
    }
    
    public Player.BowlingStyle getBowlingStyle() {
        return bowlingStyle;
    }
    
    public void setBowlingStyle(Player.BowlingStyle bowlingStyle) {
        this.bowlingStyle = bowlingStyle;
    }
    
    public Long getTeamId() {
        return teamId;
    }
    
    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
}
