package com.cricket.dto;

import com.cricket.entity.League;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class LeagueDTO {
    
    private Long id;
    
    @NotBlank(message = "League name is required")
    private String name;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    @NotNull(message = "End date is required")
    private LocalDate endDate;
    
    private League.LeagueStatus status;
    
    public LeagueDTO() {}
    
    public LeagueDTO(League league) {
        this.id = league.getId();
        this.name = league.getName();
        this.startDate = league.getStartDate();
        this.endDate = league.getEndDate();
        this.status = league.getStatus();
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
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public League.LeagueStatus getStatus() {
        return status;
    }
    
    public void setStatus(League.LeagueStatus status) {
        this.status = status;
    }
}
