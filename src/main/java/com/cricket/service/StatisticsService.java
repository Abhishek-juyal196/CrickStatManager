package com.cricket.service;

import com.cricket.entity.*;
import com.cricket.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class StatisticsService {
    
    @Autowired
    private BallRepository ballRepository;
    
    @Autowired
    private InningsRepository inningsRepository;
    
    @Autowired
    private MatchRepository matchRepository;
    
    @Autowired
    private PlayerRepository playerRepository;
    
    @Autowired
    private TeamRepository teamRepository;
    
    public Map<String, Object> getPlayerBattingStats(Long playerId) {
        List<Ball> balls = ballRepository.findByBatsmanId(playerId);
        
        int totalRuns = 0;
        int ballsFaced = 0;
        int fours = 0;
        int sixes = 0;
        int centuries = 0;
        int halfCenturies = 0;
        int highestScore = 0;
        int currentInningsRuns = 0;
        Long currentInningsId = null;
        
        for (Ball ball : balls) {
            if (!ball.getIsWide() && !ball.getIsNoBall()) {
                ballsFaced++;
            }
            
            totalRuns += ball.getRuns();
            currentInningsRuns += ball.getRuns();
            
            if (ball.getRuns() == 4) fours++;
            if (ball.getRuns() == 6) sixes++;
            
            if (currentInningsId == null || !currentInningsId.equals(ball.getInnings().getId())) {
                if (currentInningsRuns >= 100) centuries++;
                else if (currentInningsRuns >= 50) halfCenturies++;
                
                if (currentInningsRuns > highestScore) {
                    highestScore = currentInningsRuns;
                }
                
                currentInningsRuns = 0;
                currentInningsId = ball.getInnings().getId();
            }
        }
        
        // Check last innings
        if (currentInningsRuns >= 100) centuries++;
        else if (currentInningsRuns >= 50) halfCenturies++;
        
        if (currentInningsRuns > highestScore) {
            highestScore = currentInningsRuns;
        }
        
        double average = ballsFaced > 0 ? (double) totalRuns / ballsFaced : 0.0;
        double strikeRate = ballsFaced > 0 ? (double) totalRuns / ballsFaced * 100 : 0.0;
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRuns", totalRuns);
        stats.put("ballsFaced", ballsFaced);
        stats.put("average", Math.round(average * 100.0) / 100.0);
        stats.put("strikeRate", Math.round(strikeRate * 100.0) / 100.0);
        stats.put("fours", fours);
        stats.put("sixes", sixes);
        stats.put("centuries", centuries);
        stats.put("halfCenturies", halfCenturies);
        stats.put("highestScore", highestScore);
        
        return stats;
    }
    
    public Map<String, Object> getPlayerBowlingStats(Long playerId) {
        List<Ball> balls = ballRepository.findByBowlerId(playerId);
        
        int totalRuns = 0;
        int ballsBowled = 0;
        int wickets = 0;
        int fiveWicketHauls = 0;
        int currentInningsWickets = 0;
        Long currentInningsId = null;
        
        for (Ball ball : balls) {
            if (!ball.getIsWide() && !ball.getIsNoBall()) {
                ballsBowled++;
            }
            
            totalRuns += ball.getRuns();
            
            if (ball.getIsWicket()) {
                wickets++;
                currentInningsWickets++;
            }
            
            if (currentInningsId == null || !currentInningsId.equals(ball.getInnings().getId())) {
                if (currentInningsWickets >= 5) fiveWicketHauls++;
                currentInningsWickets = 0;
                currentInningsId = ball.getInnings().getId();
            }
        }
        
        // Check last innings
        if (currentInningsWickets >= 5) fiveWicketHauls++;
        
        double overs = ballsBowled / 6.0;
        double economy = overs > 0 ? totalRuns / overs : 0.0;
        double average = wickets > 0 ? (double) totalRuns / wickets : 0.0;
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRuns", totalRuns);
        stats.put("ballsBowled", ballsBowled);
        stats.put("overs", Math.round(overs * 100.0) / 100.0);
        stats.put("wickets", wickets);
        stats.put("economy", Math.round(economy * 100.0) / 100.0);
        stats.put("average", Math.round(average * 100.0) / 100.0);
        stats.put("fiveWicketHauls", fiveWicketHauls);
        
        return stats;
    }
    
    public List<Map<String, Object>> getBattingLeaderboard(Long leagueId) {
        List<Player> players = playerRepository.findByLeagueId(leagueId);
        
        return players.stream()
                .map(player -> {
                    Map<String, Object> playerStats = getPlayerBattingStats(player.getId());
                    playerStats.put("playerId", player.getId());
                    playerStats.put("playerName", player.getName());
                    playerStats.put("teamName", player.getTeam().getName());
                    return playerStats;
                })
                .sorted((a, b) -> Integer.compare((Integer) b.get("totalRuns"), (Integer) a.get("totalRuns")))
                .collect(Collectors.toList());
    }
    
    public List<Map<String, Object>> getBowlingLeaderboard(Long leagueId) {
        List<Player> players = playerRepository.findByLeagueId(leagueId);
        
        return players.stream()
                .map(player -> {
                    Map<String, Object> playerStats = getPlayerBowlingStats(player.getId());
                    playerStats.put("playerId", player.getId());
                    playerStats.put("playerName", player.getName());
                    playerStats.put("teamName", player.getTeam().getName());
                    return playerStats;
                })
                .sorted((a, b) -> Integer.compare((Integer) b.get("wickets"), (Integer) a.get("wickets")))
                .collect(Collectors.toList());
    }
    
    public Map<String, Object> getTeamStats(Long teamId) {
        List<Innings> innings = inningsRepository.findByBattingTeamId(teamId);
        
        int totalRuns = 0;
        int totalWickets = 0;
        double totalOvers = 0.0;
        
        for (Innings inning : innings) {
            totalRuns += inning.getTotalRuns();
            totalWickets += inning.getWickets();
            totalOvers += inning.getOvers();
        }
        
        double average = totalWickets > 0 ? (double) totalRuns / totalWickets : 0.0;
        double runRate = totalOvers > 0 ? totalRuns / totalOvers : 0.0;
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRuns", totalRuns);
        stats.put("totalWickets", totalWickets);
        stats.put("totalOvers", Math.round(totalOvers * 100.0) / 100.0);
        stats.put("average", Math.round(average * 100.0) / 100.0);
        stats.put("runRate", Math.round(runRate * 100.0) / 100.0);
        
        return stats;
    }
    
    public List<Map<String, Object>> getLeagueStandings(Long leagueId) {
        List<Team> teams = teamRepository.findByLeagueId(leagueId);
        
        return teams.stream()
                .map(team -> {
                    Map<String, Object> teamStats = getTeamStats(team.getId());
                    teamStats.put("teamId", team.getId());
                    teamStats.put("teamName", team.getName());
                    teamStats.put("points", 0); // This would need to be calculated based on match results
                    teamStats.put("matchesPlayed", 0); // This would need to be calculated
                    teamStats.put("matchesWon", 0); // This would need to be calculated
                    teamStats.put("matchesLost", 0); // This would need to be calculated
                    teamStats.put("netRunRate", 0.0); // This would need to be calculated
                    return teamStats;
                })
                .sorted((a, b) -> Integer.compare((Integer) b.get("points"), (Integer) a.get("points")))
                .collect(Collectors.toList());
    }
    
    public Long calculateManOfTheMatch(Long matchId) {
        List<Innings> innings = inningsRepository.findByMatchId(matchId);
        
        Map<Long, Integer> playerPoints = new HashMap<>();
        
        for (Innings inning : innings) {
            List<Ball> balls = ballRepository.findByInningsIdOrderByOverAndBall(inning.getId());
            
            for (Ball ball : balls) {
                Long batsmanId = ball.getBatsman().getId();
                Long bowlerId = ball.getBowler().getId();
                
                // Batting points: 1 point per run
                int battingPoints = ball.getRuns();
                playerPoints.merge(batsmanId, battingPoints, Integer::sum);
                
                // Bowling points: 20 points per wicket
                if (ball.getIsWicket()) {
                    int bowlingPoints = 20;
                    playerPoints.merge(bowlerId, bowlingPoints, Integer::sum);
                }
            }
        }
        
        return playerPoints.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
