package com.cricket.controller;

import com.cricket.dto.BallDTO;
import com.cricket.dto.MatchDTO;
import com.cricket.dto.ScorecardDTO;
import com.cricket.entity.*;
import com.cricket.service.MatchService;
import com.cricket.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/matches")
@Tag(name = "Match Management", description = "APIs for managing cricket matches and scoring")
public class MatchController {
    
    @Autowired
    private MatchService matchService;
    
    @Autowired
    private StatisticsService statisticsService;
    
    @PostMapping
    @Operation(summary = "Create a new match", description = "Creates a new cricket match with the provided details")
    public ResponseEntity<MatchDTO> createMatch(@Valid @RequestBody MatchDTO matchDTO) {
        Match match = new Match();
        match.setVenue(matchDTO.getVenue());
        match.setMatchDate(matchDTO.getMatchDate());
        match.setStatus(matchDTO.getStatus());
        
        Match createdMatch = matchService.createMatch(match);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MatchDTO(createdMatch));
    }
    
    @GetMapping
    @Operation(summary = "Get all matches", description = "Retrieves a list of all cricket matches")
    public ResponseEntity<List<MatchDTO>> getAllMatches() {
        List<Match> matches = matchService.getAllMatches();
        List<MatchDTO> matchDTOs = matches.stream()
                .map(MatchDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(matchDTOs);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get match by ID", description = "Retrieves a specific match by its ID")
    public ResponseEntity<MatchDTO> getMatchById(@PathVariable Long id) {
        return matchService.getMatchById(id)
                .map(match -> ResponseEntity.ok(new MatchDTO(match)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}/scorecard")
    @Operation(summary = "Get match scorecard", description = "Retrieves the live scorecard for a specific match")
    public ResponseEntity<ScorecardDTO> getMatchScorecard(@PathVariable Long id) {
        MatchScorecard scorecard = matchService.getMatchScorecard(id);
        return ResponseEntity.ok(new ScorecardDTO(scorecard));
    }
    
    @PostMapping("/{id}/start")
    @Operation(summary = "Start match", description = "Starts a match (changes status to IN_PROGRESS)")
    public ResponseEntity<MatchDTO> startMatch(@PathVariable Long id) {
        Match startedMatch = matchService.startMatch(id);
        return ResponseEntity.ok(new MatchDTO(startedMatch));
    }
    
    @PostMapping("/{id}/complete")
    @Operation(summary = "Complete match", description = "Completes a match (changes status to COMPLETED)")
    public ResponseEntity<MatchDTO> completeMatch(@PathVariable Long id) {
        Match completedMatch = matchService.completeMatch(id);
        return ResponseEntity.ok(new MatchDTO(completedMatch));
    }
    
    @PostMapping("/{id}/innings/{inningsNumber}/balls")
    @Operation(summary = "Add ball to innings", description = "Adds ball-by-ball data to a specific innings")
    public ResponseEntity<BallDTO> addBall(@PathVariable Long id, 
                                         @PathVariable Integer inningsNumber,
                                         @Valid @RequestBody BallDTO ballDTO) {
        Ball ball = new Ball();
        ball.setRuns(ballDTO.getRuns());
        ball.setIsWicket(ballDTO.getIsWicket());
        ball.setWicketType(ballDTO.getWicketType());
        ball.setExtraType(ballDTO.getExtraType());
        ball.setExtraRuns(ballDTO.getExtraRuns());
        ball.setIsWide(ballDTO.getIsWide());
        ball.setIsNoBall(ballDTO.getIsNoBall());
        ball.setIsBye(ballDTO.getIsBye());
        ball.setIsLegBye(ballDTO.getIsLegBye());
        
        Ball addedBall = matchService.addBall(id, inningsNumber, ball);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BallDTO(addedBall));
    }
    
    @PutMapping("/{id}/undo")
    @Operation(summary = "Undo last ball", description = "Undoes the last ball scored in the match")
    public ResponseEntity<Void> undoLastBall(@PathVariable Long id) {
        matchService.undoLastBall(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{id}/man-of-the-match")
    @Operation(summary = "Get man of the match", description = "Calculates and returns the man of the match for a specific match")
    public ResponseEntity<Map<String, Object>> getManOfTheMatch(@PathVariable Long id) {
        Long manOfTheMatchId = statisticsService.calculateManOfTheMatch(id);
        if (manOfTheMatchId != null) {
            return ResponseEntity.ok(Map.of("manOfTheMatchId", manOfTheMatchId));
        } else {
            return ResponseEntity.ok(Map.of("message", "No man of the match determined yet"));
        }
    }
}
