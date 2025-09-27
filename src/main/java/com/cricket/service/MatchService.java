package com.cricket.service;

import com.cricket.entity.*;
import com.cricket.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

@Service
@Transactional
public class MatchService {
    
    @Autowired
    private MatchRepository matchRepository;
    
    @Autowired
    private InningsRepository inningsRepository;
    
    @Autowired
    private BallRepository ballRepository;
    
    @Autowired
    private MatchScorecardRepository scorecardRepository;
    
    @Autowired
    private TeamRepository teamRepository;
    
    @Autowired
    private LeagueRepository leagueRepository;
    
    // Stack for undo functionality
    private final Stack<Ball> undoStack = new Stack<>();
    
    public Match createMatch(Match match) {
        return matchRepository.save(match);
    }
    
    @Transactional(readOnly = true)
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Match> getMatchesByLeagueId(Long leagueId) {
        return matchRepository.findByLeagueId(leagueId);
    }
    
    @Transactional(readOnly = true)
    public Optional<Match> getMatchById(Long id) {
        return matchRepository.findById(id);
    }
    
    public Match updateMatch(Long id, Match matchDetails) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + id));
        
        match.setTeam1(matchDetails.getTeam1());
        match.setTeam2(matchDetails.getTeam2());
        match.setVenue(matchDetails.getVenue());
        match.setMatchDate(matchDetails.getMatchDate());
        match.setStatus(matchDetails.getStatus());
        
        return matchRepository.save(match);
    }
    
    public void deleteMatch(Long id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + id));
        matchRepository.delete(match);
    }
    
    public Match startMatch(Long id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + id));
        
        match.setStatus(Match.MatchStatus.IN_PROGRESS);
        
        // Create scorecard if it doesn't exist
        if (match.getScorecard() == null) {
            MatchScorecard scorecard = new MatchScorecard(match);
            match.setScorecard(scorecard);
            scorecardRepository.save(scorecard);
        }
        
        return matchRepository.save(match);
    }
    
    public Match completeMatch(Long id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + id));
        
        match.setStatus(Match.MatchStatus.COMPLETED);
        
        // Update final scorecard
        updateMatchScorecard(match);
        
        return matchRepository.save(match);
    }
    
    public Innings startInnings(Long matchId, Long battingTeamId, Long bowlingTeamId, Integer inningsNumber) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + matchId));
        
        Team battingTeam = teamRepository.findById(battingTeamId)
                .orElseThrow(() -> new RuntimeException("Batting team not found with id: " + battingTeamId));
        
        Team bowlingTeam = teamRepository.findById(bowlingTeamId)
                .orElseThrow(() -> new RuntimeException("Bowling team not found with id: " + bowlingTeamId));
        
        Innings innings = new Innings(match, battingTeam, bowlingTeam, inningsNumber);
        innings.setStatus(Innings.InningsStatus.IN_PROGRESS);
        
        return inningsRepository.save(innings);
    }
    
    public Ball addBall(Long matchId, Integer inningsNumber, Ball ballData) {
        Innings innings = inningsRepository.findByMatchIdAndInningsNumber(matchId, inningsNumber)
                .orElseThrow(() -> new RuntimeException("Innings not found"));
        
        // Add to undo stack before saving
        undoStack.push(ballData);
        
        // Calculate over and ball numbers
        List<Ball> existingBalls = ballRepository.findByInningsIdOrderByOverAndBall(innings.getId());
        int totalBalls = existingBalls.size();
        int overNumber = totalBalls / 6;
        int ballNumber = (totalBalls % 6) + 1;
        
        ballData.setInnings(innings);
        ballData.setOverNumber(overNumber);
        ballData.setBallNumber(ballNumber);
        
        Ball savedBall = ballRepository.save(ballData);
        
        // Update innings statistics
        updateInningsStatistics(innings);
        
        // Update match scorecard
        updateMatchScorecard(innings.getMatch());
        
        return savedBall;
    }
    
    public void undoLastBall(Long matchId) {
        if (undoStack.isEmpty()) {
            throw new RuntimeException("No balls to undo");
        }
        
        Ball lastBall = undoStack.pop();
        ballRepository.delete(lastBall);
        
        // Recalculate innings statistics
        Innings innings = lastBall.getInnings();
        updateInningsStatistics(innings);
        
        // Update match scorecard
        updateMatchScorecard(innings.getMatch());
    }
    
    private void updateInningsStatistics(Innings innings) {
        List<Ball> balls = ballRepository.findByInningsIdOrderByOverAndBall(innings.getId());
        
        int totalRuns = 0;
        int wickets = 0;
        int validBalls = 0;
        
        for (Ball ball : balls) {
            totalRuns += ball.getRuns();
            if (ball.getIsWicket()) {
                wickets++;
            }
            if (!ball.getIsWide() && !ball.getIsNoBall()) {
                validBalls++;
            }
        }
        
        innings.setTotalRuns(totalRuns);
        innings.setWickets(wickets);
        innings.setOvers(validBalls / 6.0);
        
        inningsRepository.save(innings);
    }
    
    private void updateMatchScorecard(Match match) {
        MatchScorecard scorecard = match.getScorecard();
        if (scorecard == null) {
            scorecard = new MatchScorecard(match);
            match.setScorecard(scorecard);
        }
        
        List<Innings> innings = inningsRepository.findByMatchIdOrderByInningsNumber(match.getId());
        
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
    }
    
    @Transactional(readOnly = true)
    public MatchScorecard getMatchScorecard(Long matchId) {
        return scorecardRepository.findByMatchId(matchId)
                .orElseThrow(() -> new RuntimeException("Scorecard not found for match: " + matchId));
    }
}
