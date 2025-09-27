package com.cricket.dto;

import com.cricket.entity.Team;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class TeamDTO {
    
    private Long id;
    
    @NotBlank(message = "Team name is required")
    private String name;
    
    private String logoUrl;
    
    @NotNull(message = "League ID is required")
    private Long leagueId;
    
    private List<PlayerDTO> players;
    
    public TeamDTO() {}
    
    public TeamDTO(Team team) {
        this.id = team.getId();
        this.name = team.getName();
        this.logoUrl = team.getLogoUrl();
        this.leagueId = team.getLeague().getId();
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
    
    public String getLogoUrl() {
        return logoUrl;
    }
    
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
    
    public Long getLeagueId() {
        return leagueId;
    }
    
    public void setLeagueId(Long leagueId) {
        this.leagueId = leagueId;
    }
    
    public List<PlayerDTO> getPlayers() {
        return players;
    }
    
    public void setPlayers(List<PlayerDTO> players) {
        this.players = players;
    }
}
