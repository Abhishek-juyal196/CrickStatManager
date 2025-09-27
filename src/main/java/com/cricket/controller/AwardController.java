package com.cricket.controller;

import com.cricket.service.AwardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/awards")
@Tag(name = "Awards & Recognition", description = "APIs for cricket awards and recognition")
public class AwardController {
    
    @Autowired
    private AwardService awardService;
    
    @GetMapping("/matches/{matchId}/man-of-the-match")
    @Operation(summary = "Get man of the match", description = "Calculates and returns the man of the match for a specific match")
    public ResponseEntity<Map<String, Object>> getManOfTheMatch(@PathVariable Long matchId) {
        Long manOfTheMatchId = awardService.calculateManOfTheMatch(matchId);
        if (manOfTheMatchId != null) {
            return ResponseEntity.ok(Map.of("manOfTheMatchId", manOfTheMatchId));
        } else {
            return ResponseEntity.ok(Map.of("message", "No man of the match determined yet"));
        }
    }
    
    @GetMapping("/leagues/{leagueId}/orange-cap")
    @Operation(summary = "Get Orange Cap leaderboard", description = "Retrieves the Orange Cap (top run scorers) leaderboard for a league")
    public ResponseEntity<List<Map<String, Object>>> getOrangeCap(@PathVariable Long leagueId) {
        List<Map<String, Object>> orangeCap = awardService.calculateOrangeCap(leagueId);
        return ResponseEntity.ok(orangeCap);
    }
    
    @GetMapping("/leagues/{leagueId}/purple-cap")
    @Operation(summary = "Get Purple Cap leaderboard", description = "Retrieves the Purple Cap (top wicket takers) leaderboard for a league")
    public ResponseEntity<List<Map<String, Object>>> getPurpleCap(@PathVariable Long leagueId) {
        List<Map<String, Object>> purpleCap = awardService.calculatePurpleCap(leagueId);
        return ResponseEntity.ok(purpleCap);
    }
    
    @GetMapping("/leagues/{leagueId}/tournament-awards")
    @Operation(summary = "Get tournament awards", description = "Retrieves all tournament awards for a specific league")
    public ResponseEntity<Map<String, Object>> getTournamentAwards(@PathVariable Long leagueId) {
        Map<String, Object> awards = awardService.calculateTournamentAwards(leagueId);
        return ResponseEntity.ok(awards);
    }
    
    @GetMapping("/leagues/{leagueId}/team-awards")
    @Operation(summary = "Get team awards", description = "Retrieves team awards for a specific league")
    public ResponseEntity<Map<String, Object>> getTeamAwards(@PathVariable Long leagueId) {
        Map<String, Object> teamAwards = awardService.calculateTeamAwards(leagueId);
        return ResponseEntity.ok(teamAwards);
    }
    
    @GetMapping("/leagues/{leagueId}/century-makers")
    @Operation(summary = "Get century makers", description = "Retrieves list of players who have scored centuries in the league")
    public ResponseEntity<List<Map<String, Object>>> getCenturyMakers(@PathVariable Long leagueId) {
        List<Map<String, Object>> centuryMakers = awardService.calculateCenturyMakers(leagueId);
        return ResponseEntity.ok(centuryMakers);
    }
    
    @GetMapping("/leagues/{leagueId}/five-wicket-hauls")
    @Operation(summary = "Get five-wicket haul takers", description = "Retrieves list of players who have taken five-wicket hauls in the league")
    public ResponseEntity<List<Map<String, Object>>> getFiveWicketHaulTakers(@PathVariable Long leagueId) {
        List<Map<String, Object>> fiveWicketHauls = awardService.calculateFiveWicketHaulTakers(leagueId);
        return ResponseEntity.ok(fiveWicketHauls);
    }
}
