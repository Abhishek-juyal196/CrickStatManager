package com.cricket.controller;

import com.cricket.dto.PlayerDTO;
import com.cricket.dto.TeamDTO;
import com.cricket.entity.Player;
import com.cricket.entity.Team;
import com.cricket.service.TeamService;
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
@RequestMapping("/api/teams")
@Tag(name = "Team Management", description = "APIs for managing cricket teams")
public class TeamController {
    
    @Autowired
    private TeamService teamService;
    
    @PostMapping
    @Operation(summary = "Create a new team", description = "Creates a new cricket team with the provided details")
    public ResponseEntity<TeamDTO> createTeam(@Valid @RequestBody TeamDTO teamDTO) {
        Team team = new Team();
        team.setName(teamDTO.getName());
        team.setLogoUrl(teamDTO.getLogoUrl());
        
        Team createdTeam = teamService.createTeam(team);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TeamDTO(createdTeam));
    }
    
    @GetMapping
    @Operation(summary = "Get all teams", description = "Retrieves a list of all cricket teams")
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        List<Team> teams = teamService.getAllTeams();
        List<TeamDTO> teamDTOs = teams.stream()
                .map(TeamDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(teamDTOs);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get team by ID", description = "Retrieves a specific team by its ID")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Long id) {
        return teamService.getTeamById(id)
                .map(team -> ResponseEntity.ok(new TeamDTO(team)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}/players")
    @Operation(summary = "Get team players", description = "Retrieves all players belonging to a specific team")
    public ResponseEntity<List<PlayerDTO>> getTeamPlayers(@PathVariable Long id) {
        List<Player> players = teamService.getTeamPlayers(id);
        List<PlayerDTO> playerDTOs = players.stream()
                .map(PlayerDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(playerDTOs);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update team", description = "Updates an existing team with new details")
    public ResponseEntity<TeamDTO> updateTeam(@PathVariable Long id, @Valid @RequestBody TeamDTO teamDTO) {
        Team team = new Team();
        team.setName(teamDTO.getName());
        team.setLogoUrl(teamDTO.getLogoUrl());
        
        Team updatedTeam = teamService.updateTeam(id, team);
        return ResponseEntity.ok(new TeamDTO(updatedTeam));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete team", description = "Deletes a team by its ID")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/players")
    @Operation(summary = "Add player to team", description = "Adds a new player to the specified team")
    public ResponseEntity<PlayerDTO> addPlayerToTeam(@PathVariable Long id, @Valid @RequestBody PlayerDTO playerDTO) {
        Player player = new Player();
        player.setName(playerDTO.getName());
        player.setDateOfBirth(playerDTO.getDateOfBirth());
        player.setBattingStyle(playerDTO.getBattingStyle());
        player.setBowlingStyle(playerDTO.getBowlingStyle());
        
        Player addedPlayer = teamService.addPlayerToTeam(id, player);
        return ResponseEntity.status(HttpStatus.CREATED).body(new PlayerDTO(addedPlayer));
    }
    
    @DeleteMapping("/{teamId}/players/{playerId}")
    @Operation(summary = "Remove player from team", description = "Removes a player from the specified team")
    public ResponseEntity<Void> removePlayerFromTeam(@PathVariable Long teamId, @PathVariable Long playerId) {
        teamService.removePlayerFromTeam(teamId, playerId);
        return ResponseEntity.noContent().build();
    }
}
