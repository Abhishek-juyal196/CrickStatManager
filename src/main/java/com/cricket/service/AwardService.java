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
public class AwardService {
    
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
    
    @Autowired
    private StatisticsService statisticsService;
    
    /**
     * Calculate Man of the Match using points system
     * Batting: 1 point per run
     * Bowling: 20 points per wicket
     */
    public Long calculateManOfTheMatch(Long matchId) {
        List<Innings> innings = inningsRepository.findByMatchIdOrderByInningsNumber(matchId);
        
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
    
    /**
     * Calculate Orange Cap (Top run scorer) for a league
     */
    public List<Map<String, Object>> calculateOrangeCap(Long leagueId) {
        List<Player> players = playerRepository.findByLeagueId(leagueId);
        
        return players.stream()
                .map(player -> {
                    Map<String, Object> playerStats = statisticsService.getPlayerBattingStats(player.getId());
                    playerStats.put("playerId", player.getId());
                    playerStats.put("playerName", player.getName());
                    playerStats.put("teamName", player.getTeam().getName());
                    return playerStats;
                })
                .sorted((a, b) -> Integer.compare((Integer) b.get("totalRuns"), (Integer) a.get("totalRuns")))
                .limit(10)
                .collect(Collectors.toList());
    }
    
    /**
     * Calculate Purple Cap (Top wicket taker) for a league
     */
    public List<Map<String, Object>> calculatePurpleCap(Long leagueId) {
        List<Player> players = playerRepository.findByLeagueId(leagueId);
        
        return players.stream()
                .map(player -> {
                    Map<String, Object> playerStats = statisticsService.getPlayerBowlingStats(player.getId());
                    playerStats.put("playerId", player.getId());
                    playerStats.put("playerName", player.getName());
                    playerStats.put("teamName", player.getTeam().getName());
                    return playerStats;
                })
                .sorted((a, b) -> Integer.compare((Integer) b.get("wickets"), (Integer) a.get("wickets")))
                .limit(10)
                .collect(Collectors.toList());
    }
    
    /**
     * Calculate tournament awards
     */
    public Map<String, Object> calculateTournamentAwards(Long leagueId) {
        Map<String, Object> awards = new HashMap<>();
        
        // Orange Cap
        List<Map<String, Object>> orangeCap = calculateOrangeCap(leagueId);
        if (!orangeCap.isEmpty()) {
            awards.put("orangeCap", orangeCap.get(0));
        }
        
        // Purple Cap
        List<Map<String, Object>> purpleCap = calculatePurpleCap(leagueId);
        if (!purpleCap.isEmpty()) {
            awards.put("purpleCap", purpleCap.get(0));
        }
        
        // Most Valuable Player (highest points)
        Long mvpId = calculateMostValuablePlayer(leagueId);
        if (mvpId != null) {
            Player mvp = playerRepository.findById(mvpId).orElse(null);
            if (mvp != null) {
                Map<String, Object> mvpInfo = new HashMap<>();
                mvpInfo.put("playerId", mvp.getId());
                mvpInfo.put("playerName", mvp.getName());
                mvpInfo.put("teamName", mvp.getTeam().getName());
                awards.put("mostValuablePlayer", mvpInfo);
            }
        }
        
        return awards;
    }
    
    /**
     * Calculate Most Valuable Player for a league
     */
    public Long calculateMostValuablePlayer(Long leagueId) {
        List<Player> players = playerRepository.findByLeagueId(leagueId);
        
        Map<Long, Integer> playerPoints = new HashMap<>();
        
        for (Player player : players) {
            Map<String, Object> battingStats = statisticsService.getPlayerBattingStats(player.getId());
            Map<String, Object> bowlingStats = statisticsService.getPlayerBowlingStats(player.getId());
            
            int totalPoints = (Integer) battingStats.get("totalRuns") + 
                            (Integer) bowlingStats.get("wickets") * 20;
            
            playerPoints.put(player.getId(), totalPoints);
        }
        
        return playerPoints.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
    
    /**
     * Calculate team awards
     */
    public Map<String, Object> calculateTeamAwards(Long leagueId) {
        List<Team> teams = teamRepository.findByLeagueId(leagueId);
        
        Map<String, Object> teamAwards = new HashMap<>();
        
        // Best batting team
        Team bestBattingTeam = teams.stream()
                .max((t1, t2) -> {
                    Map<String, Object> stats1 = statisticsService.getTeamStats(t1.getId());
                    Map<String, Object> stats2 = statisticsService.getTeamStats(t2.getId());
                    return Integer.compare((Integer) stats1.get("totalRuns"), (Integer) stats2.get("totalRuns"));
                })
                .orElse(null);
        
        if (bestBattingTeam != null) {
            Map<String, Object> bestBatting = new HashMap<>();
            bestBatting.put("teamId", bestBattingTeam.getId());
            bestBatting.put("teamName", bestBattingTeam.getName());
            bestBatting.put("totalRuns", statisticsService.getTeamStats(bestBattingTeam.getId()).get("totalRuns"));
            teamAwards.put("bestBattingTeam", bestBatting);
        }
        
        // Best bowling team
        Team bestBowlingTeam = teams.stream()
                .max((t1, t2) -> {
                    Map<String, Object> stats1 = statisticsService.getTeamStats(t1.getId());
                    Map<String, Object> stats2 = statisticsService.getTeamStats(t2.getId());
                    return Integer.compare((Integer) stats1.get("totalWickets"), (Integer) stats2.get("totalWickets"));
                })
                .orElse(null);
        
        if (bestBowlingTeam != null) {
            Map<String, Object> bestBowling = new HashMap<>();
            bestBowling.put("teamId", bestBowlingTeam.getId());
            bestBowling.put("teamName", bestBowlingTeam.getName());
            bestBowling.put("totalWickets", statisticsService.getTeamStats(bestBowlingTeam.getId()).get("totalWickets"));
            teamAwards.put("bestBowlingTeam", bestBowling);
        }
        
        return teamAwards;
    }
    
    /**
     * Calculate century makers for a league
     */
    public List<Map<String, Object>> calculateCenturyMakers(Long leagueId) {
        List<Player> players = playerRepository.findByLeagueId(leagueId);
        
        return players.stream()
                .map(player -> {
                    Map<String, Object> playerStats = statisticsService.getPlayerBattingStats(player.getId());
                    playerStats.put("playerId", player.getId());
                    playerStats.put("playerName", player.getName());
                    playerStats.put("teamName", player.getTeam().getName());
                    return playerStats;
                })
                .filter(stats -> (Integer) stats.get("centuries") > 0)
                .sorted((a, b) -> Integer.compare((Integer) b.get("centuries"), (Integer) a.get("centuries")))
                .collect(Collectors.toList());
    }
    
    /**
     * Calculate five-wicket haul takers for a league
     */
    public List<Map<String, Object>> calculateFiveWicketHaulTakers(Long leagueId) {
        List<Player> players = playerRepository.findByLeagueId(leagueId);
        
        return players.stream()
                .map(player -> {
                    Map<String, Object> playerStats = statisticsService.getPlayerBowlingStats(player.getId());
                    playerStats.put("playerId", player.getId());
                    playerStats.put("playerName", player.getName());
                    playerStats.put("teamName", player.getTeam().getName());
                    return playerStats;
                })
                .filter(stats -> (Integer) stats.get("fiveWicketHauls") > 0)
                .sorted((a, b) -> Integer.compare((Integer) b.get("fiveWicketHauls"), (Integer) a.get("fiveWicketHauls")))
                .collect(Collectors.toList());
    }
}
