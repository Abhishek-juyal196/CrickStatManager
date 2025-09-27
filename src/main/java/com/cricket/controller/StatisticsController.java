package com.cricket.controller;

import com.cricket.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@Tag(name = "Statistics & Awards", description = "APIs for cricket statistics and awards")
public class StatisticsController {
    
    @Autowired
    private StatisticsService statisticsService;
    
    @GetMapping("/players/{playerId}/batting")
    @Operation(summary = "Get player batting statistics", description = "Retrieves batting statistics for a specific player")
    public ResponseEntity<Map<String, Object>> getPlayerBattingStats(@PathVariable Long playerId) {
        Map<String, Object> stats = statisticsService.getPlayerBattingStats(playerId);
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/players/{playerId}/bowling")
    @Operation(summary = "Get player bowling statistics", description = "Retrieves bowling statistics for a specific player")
    public ResponseEntity<Map<String, Object>> getPlayerBowlingStats(@PathVariable Long playerId) {
        Map<String, Object> stats = statisticsService.getPlayerBowlingStats(playerId);
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/leagues/{leagueId}/leaderboards/batting")
    @Operation(summary = "Get batting leaderboard", description = "Retrieves the batting leaderboard for a specific league")
    public ResponseEntity<List<Map<String, Object>>> getBattingLeaderboard(@PathVariable Long leagueId) {
        List<Map<String, Object>> leaderboard = statisticsService.getBattingLeaderboard(leagueId);
        return ResponseEntity.ok(leaderboard);
    }
    
    @GetMapping("/leagues/{leagueId}/leaderboards/bowling")
    @Operation(summary = "Get bowling leaderboard", description = "Retrieves the bowling leaderboard for a specific league")
    public ResponseEntity<List<Map<String, Object>>> getBowlingLeaderboard(@PathVariable Long leagueId) {
        List<Map<String, Object>> leaderboard = statisticsService.getBowlingLeaderboard(leagueId);
        return ResponseEntity.ok(leaderboard);
    }
    
    @GetMapping("/teams/{teamId}/stats")
    @Operation(summary = "Get team statistics", description = "Retrieves overall statistics for a specific team")
    public ResponseEntity<Map<String, Object>> getTeamStats(@PathVariable Long teamId) {
        Map<String, Object> stats = statisticsService.getTeamStats(teamId);
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/leagues/{leagueId}/standings")
    @Operation(summary = "Get league standings", description = "Retrieves the points table for a specific league")
    public ResponseEntity<List<Map<String, Object>>> getLeagueStandings(@PathVariable Long leagueId) {
        List<Map<String, Object>> standings = statisticsService.getLeagueStandings(leagueId);
        return ResponseEntity.ok(standings);
    }
}
