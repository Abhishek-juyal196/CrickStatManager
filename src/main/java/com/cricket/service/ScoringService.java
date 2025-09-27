package com.cricket.service;

import com.cricket.entity.*;
import com.cricket.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@Transactional
public class ScoringService {
    
    @Autowired
    private BallRepository ballRepository;
    
    @Autowired
    private InningsRepository inningsRepository;
    
    @Autowired
    private MatchRepository matchRepository;
    
    @Autowired
    private MatchScorecardRepository scorecardRepository;
    
    @Autowired
    private StatisticsService statisticsService;
    
    /**
     * Calculate Current Run Rate (CRR) for an innings
     */
    public double calculateCurrentRunRate(Long inningsId) {
        Innings innings = inningsRepository.findById(inningsId)
                .orElseThrow(() -> new RuntimeException("Innings not found"));
        
        if (innings.getOvers() == 0) {
            return 0.0;
        }
        
        return innings.getTotalRuns() / innings.getOvers();
    }
    
    /**
     * Calculate Required Run Rate (RRR) for chasing team
     */
    public double calculateRequiredRunRate(Long inningsId, int targetRuns) {
        Innings innings = inningsRepository.findById(inningsId)
                .orElseThrow(() -> new RuntimeException("Innings not found"));
        
        int runsNeeded = targetRuns - innings.getTotalRuns();
        double oversRemaining = 20.0 - innings.getOvers(); // Assuming 20-over match
        
        if (oversRemaining <= 0) {
            return 0.0;
        }
        
        return runsNeeded / oversRemaining;
    }
    
    /**
     * Calculate partnership between two batsmen
     */
    public Map<String, Object> calculatePartnership(Long inningsId, Long batsman1Id, Long batsman2Id) {
        List<Ball> balls = ballRepository.findByInningsIdOrderByOverAndBall(inningsId);
        
        int partnershipRuns = 0;
        int partnershipBalls = 0;
        boolean partnershipActive = false;
        
        for (Ball ball : balls) {
            if (ball.getBatsman().getId().equals(batsman1Id) || ball.getBatsman().getId().equals(batsman2Id)) {
                if (!partnershipActive) {
                    partnershipActive = true;
                }
                
                if (!ball.getIsWide() && !ball.getIsNoBall()) {
                    partnershipBalls++;
                }
                
                partnershipRuns += ball.getRuns();
                
                if (ball.getIsWicket()) {
                    partnershipActive = false;
                    break;
                }
            }
        }
        
        Map<String, Object> partnership = new HashMap<>();
        partnership.put("runs", partnershipRuns);
        partnership.put("balls", partnershipBalls);
        partnership.put("strikeRate", partnershipBalls > 0 ? (double) partnershipRuns / partnershipBalls * 100 : 0.0);
        partnership.put("active", partnershipActive);
        
        return partnership;
    }
    
    /**
     * Calculate over progression and validate ball count
     */
    public Map<String, Object> calculateOverProgression(Long inningsId) {
        List<Ball> balls = ballRepository.findByInningsIdOrderByOverAndBall(inningsId);
        
        int totalBalls = 0;
        int validBalls = 0;
        int currentOver = 0;
        int currentBall = 0;
        
        for (Ball ball : balls) {
            totalBalls++;
            if (!ball.getIsWide() && !ball.getIsNoBall()) {
                validBalls++;
            }
        }
        
        currentOver = validBalls / 6;
        currentBall = (validBalls % 6) + 1;
        
        Map<String, Object> progression = new HashMap<>();
        progression.put("totalBalls", totalBalls);
        progression.put("validBalls", validBalls);
        progression.put("currentOver", currentOver);
        progression.put("currentBall", currentBall);
        progression.put("oversCompleted", currentOver);
        progression.put("ballsInCurrentOver", currentBall - 1);
        
        return progression;
    }
    
    /**
     * Validate cricket rules for ball scoring
     */
    public boolean validateBallScoring(Ball ball) {
        // Validate runs
        if (ball.getRuns() < 0 || ball.getRuns() > 6) {
            return false;
        }
        
        // Validate wicket
        if (ball.getIsWicket() && ball.getWicketType() == null) {
            return false;
        }
        
        // Validate extras
        if (ball.getIsWide() && ball.getRuns() == 0) {
            return false; // Wide should have at least 1 run
        }
        
        if (ball.getIsNoBall() && ball.getRuns() == 0) {
            return false; // No ball should have at least 1 run
        }
        
        // Validate extra runs
        if (ball.getExtraRuns() < 0) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Calculate Net Run Rate (NRR) for a team
     */
    public double calculateNetRunRate(Long teamId, Long leagueId) {
        List<Innings> battingInnings = inningsRepository.findByBattingTeamId(teamId);
        List<Innings> bowlingInnings = inningsRepository.findByBowlingTeamId(teamId);
        
        int totalRunsScored = 0;
        double totalOversScored = 0.0;
        int totalRunsConceded = 0;
        double totalOversBowled = 0.0;
        
        for (Innings innings : battingInnings) {
            if (innings.getMatch().getLeague().getId().equals(leagueId)) {
                totalRunsScored += innings.getTotalRuns();
                totalOversScored += innings.getOvers();
            }
        }
        
        for (Innings innings : bowlingInnings) {
            if (innings.getMatch().getLeague().getId().equals(leagueId)) {
                totalRunsConceded += innings.getTotalRuns();
                totalOversBowled += innings.getOvers();
            }
        }
        
        double runRateScored = totalOversScored > 0 ? totalRunsScored / totalOversScored : 0.0;
        double runRateConceded = totalOversBowled > 0 ? totalRunsConceded / totalOversBowled : 0.0;
        
        return runRateScored - runRateConceded;
    }
    
    /**
     * Calculate match statistics in real-time
     */
    public Map<String, Object> calculateMatchStatistics(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));
        
        List<Innings> innings = inningsRepository.findByMatchIdOrderByInningsNumber(matchId);
        
        Map<String, Object> statistics = new HashMap<>();
        
        for (Innings inning : innings) {
            String teamKey = inning.getBattingTeam().getId().equals(match.getTeam1().getId()) ? "team1" : "team2";
            
            Map<String, Object> teamStats = new HashMap<>();
            teamStats.put("runs", inning.getTotalRuns());
            teamStats.put("wickets", inning.getWickets());
            teamStats.put("overs", inning.getOvers());
            teamStats.put("runRate", calculateCurrentRunRate(inning.getId()));
            
            statistics.put(teamKey, teamStats);
        }
        
        // Calculate required run rate for chasing team
        if (innings.size() >= 2) {
            Innings firstInnings = innings.get(0);
            Innings secondInnings = innings.get(1);
            
            int target = firstInnings.getTotalRuns() + 1;
            double requiredRunRate = calculateRequiredRunRate(secondInnings.getId(), target);
            
            statistics.put("requiredRunRate", requiredRunRate);
            statistics.put("target", target);
        }
        
        return statistics;
    }
    
    /**
     * Update real-time statistics after each ball
     */
    @Transactional
    public void updateRealTimeStatistics(Long matchId) {
        // Update match scorecard
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));
        
        List<Innings> innings = inningsRepository.findByMatchIdOrderByInningsNumber(matchId);
        
        MatchScorecard scorecard = match.getScorecard();
        if (scorecard == null) {
            scorecard = new MatchScorecard(match);
            match.setScorecard(scorecard);
        }
        
        for (Innings inning : innings) {
            if (inning.getBattingTeam().getId().equals(match.getTeam1().getId())) {
                scorecard.setTeam1Runs(inning.getTotalRuns());
                scorecard.setTeam1Wickets(inning.getWickets());
                scorecard.setTeam1Overs(inning.getOvers());
            } else if (inning.getBattingTeam().getId().equals(match.getTeam2().getId())) {
                scorecard.setTeam2Runs(inning.getTotalRuns());
                scorecard.setTeam2Wickets(inning.getWickets());
                scorecard.setTeam2Overs(inning.getOvers());
            }
        }
        
        scorecardRepository.save(scorecard);
        
        // Calculate and update man of the match
        Long manOfTheMatchId = statisticsService.calculateManOfTheMatch(matchId);
        if (manOfTheMatchId != null) {
            scorecard.setManOfTheMatchId(manOfTheMatchId);
            scorecardRepository.save(scorecard);
        }
    }
}
