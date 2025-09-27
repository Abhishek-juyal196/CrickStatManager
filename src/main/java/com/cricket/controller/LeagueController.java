package com.cricket.controller;

import com.cricket.dto.LeagueDTO;
import com.cricket.entity.League;
import com.cricket.service.LeagueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/leagues")
@Tag(name = "League Management", description = "APIs for managing cricket leagues")
public class LeagueController {
    
    @Autowired
    private LeagueService leagueService;
    
    @PostMapping
    @Operation(summary = "Create a new league", description = "Creates a new cricket league with the provided details")
    public ResponseEntity<LeagueDTO> createLeague(@Valid @RequestBody LeagueDTO leagueDTO) {
        League league = new League();
        league.setName(leagueDTO.getName());
        league.setStartDate(leagueDTO.getStartDate());
        league.setEndDate(leagueDTO.getEndDate());
        league.setStatus(leagueDTO.getStatus());
        
        League createdLeague = leagueService.createLeague(league);
        return ResponseEntity.status(HttpStatus.CREATED).body(new LeagueDTO(createdLeague));
    }
    
    @GetMapping
    @Operation(summary = "Get all leagues", description = "Retrieves a list of all cricket leagues")
    public ResponseEntity<List<LeagueDTO>> getAllLeagues() {
        List<League> leagues = leagueService.getAllLeagues();
        List<LeagueDTO> leagueDTOs = leagues.stream()
                .map(LeagueDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(leagueDTOs);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get league by ID", description = "Retrieves a specific league by its ID")
    public ResponseEntity<LeagueDTO> getLeagueById(@PathVariable Long id) {
        return leagueService.getLeagueById(id)
                .map(league -> ResponseEntity.ok(new LeagueDTO(league)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update league", description = "Updates an existing league with new details")
    public ResponseEntity<LeagueDTO> updateLeague(@PathVariable Long id, @Valid @RequestBody LeagueDTO leagueDTO) {
        League league = new League();
        league.setName(leagueDTO.getName());
        league.setStartDate(leagueDTO.getStartDate());
        league.setEndDate(leagueDTO.getEndDate());
        league.setStatus(leagueDTO.getStatus());
        
        League updatedLeague = leagueService.updateLeague(id, league);
        return ResponseEntity.ok(new LeagueDTO(updatedLeague));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete league", description = "Deletes a league by its ID")
    public ResponseEntity<Void> deleteLeague(@PathVariable Long id) {
        leagueService.deleteLeague(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/start")
    @Operation(summary = "Start league", description = "Starts a league (changes status to ONGOING)")
    public ResponseEntity<LeagueDTO> startLeague(@PathVariable Long id) {
        League startedLeague = leagueService.startLeague(id);
        return ResponseEntity.ok(new LeagueDTO(startedLeague));
    }
    
    @PostMapping("/{id}/complete")
    @Operation(summary = "Complete league", description = "Completes a league (changes status to COMPLETED)")
    public ResponseEntity<LeagueDTO> completeLeague(@PathVariable Long id) {
        League completedLeague = leagueService.completeLeague(id);
        return ResponseEntity.ok(new LeagueDTO(completedLeague));
    }
    
    @GetMapping("/{id}/standings")
    @Operation(summary = "Get league standings", description = "Retrieves the points table for a specific league")
    public ResponseEntity<List<Object>> getLeagueStandings(@PathVariable Long id) {
        // This would be implemented in StatisticsService
        return ResponseEntity.ok(List.of());
    }
}
