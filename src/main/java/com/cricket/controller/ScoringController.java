package com.cricket.controller;

import com.cricket.service.ScoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/scoring")
@Tag(name = "Real-time Scoring", description = "APIs for real-time cricket scoring and statistics")
public class ScoringController {
    
    @Autowired
    private ScoringService scoringService;
    
    @GetMapping("/innings/{inningsId}/current-run-rate")
    @Operation(summary = "Get current run rate", description = "Calculates the current run rate for a specific innings")
    public ResponseEntity<Map<String, Object>> getCurrentRunRate(@PathVariable Long inningsId) {
        double runRate = scoringService.calculateCurrentRunRate(inningsId);
        return ResponseEntity.ok(Map.of("currentRunRate", runRate));
    }
    
    @GetMapping("/innings/{inningsId}/required-run-rate")
    @Operation(summary = "Get required run rate", description = "Calculates the required run rate for chasing a target")
    public ResponseEntity<Map<String, Object>> getRequiredRunRate(@PathVariable Long inningsId, 
                                                                @RequestParam int targetRuns) {
        double requiredRunRate = scoringService.calculateRequiredRunRate(inningsId, targetRuns);
        return ResponseEntity.ok(Map.of("requiredRunRate", requiredRunRate));
    }
    
    @GetMapping("/innings/{inningsId}/partnership")
    @Operation(summary = "Get partnership", description = "Calculates partnership between two batsmen")
    public ResponseEntity<Map<String, Object>> getPartnership(@PathVariable Long inningsId,
                                                             @RequestParam Long batsman1Id,
                                                             @RequestParam Long batsman2Id) {
        Map<String, Object> partnership = scoringService.calculatePartnership(inningsId, batsman1Id, batsman2Id);
        return ResponseEntity.ok(partnership);
    }
    
    @GetMapping("/innings/{inningsId}/over-progression")
    @Operation(summary = "Get over progression", description = "Calculates over progression and ball count for an innings")
    public ResponseEntity<Map<String, Object>> getOverProgression(@PathVariable Long inningsId) {
        Map<String, Object> progression = scoringService.calculateOverProgression(inningsId);
        return ResponseEntity.ok(progression);
    }
    
    @GetMapping("/matches/{matchId}/statistics")
    @Operation(summary = "Get match statistics", description = "Calculates real-time match statistics")
    public ResponseEntity<Map<String, Object>> getMatchStatistics(@PathVariable Long matchId) {
        Map<String, Object> statistics = scoringService.calculateMatchStatistics(matchId);
        return ResponseEntity.ok(statistics);
    }
    
    @GetMapping("/teams/{teamId}/net-run-rate")
    @Operation(summary = "Get team net run rate", description = "Calculates net run rate for a team in a league")
    public ResponseEntity<Map<String, Object>> getTeamNetRunRate(@PathVariable Long teamId,
                                                                @RequestParam Long leagueId) {
        double netRunRate = scoringService.calculateNetRunRate(teamId, leagueId);
        return ResponseEntity.ok(Map.of("netRunRate", netRunRate));
    }
}
