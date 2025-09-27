package com.cricket.repository;

import com.cricket.entity.Ball;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BallRepository extends JpaRepository<Ball, Long> {
    
    List<Ball> findByInningsId(Long inningsId);
    
    @Query("SELECT b FROM Ball b WHERE b.innings.id = :inningsId ORDER BY b.overNumber, b.ballNumber")
    List<Ball> findByInningsIdOrderByOverAndBall(@Param("inningsId") Long inningsId);
    
    @Query("SELECT b FROM Ball b WHERE b.batsman.id = :playerId")
    List<Ball> findByBatsmanId(@Param("playerId") Long playerId);
    
    @Query("SELECT b FROM Ball b WHERE b.bowler.id = :playerId")
    List<Ball> findByBowlerId(@Param("playerId") Long playerId);
    
    @Query("SELECT b FROM Ball b WHERE b.innings.id = :inningsId AND b.overNumber = :overNumber ORDER BY b.ballNumber")
    List<Ball> findByInningsIdAndOverNumber(@Param("inningsId") Long inningsId, @Param("overNumber") Integer overNumber);
    
    @Query("SELECT b FROM Ball b WHERE b.innings.id = :inningsId AND b.isWicket = true")
    List<Ball> findWicketsByInningsId(@Param("inningsId") Long inningsId);
    
    @Query("SELECT b FROM Ball b WHERE b.innings.id = :inningsId AND b.extraType IS NOT NULL")
    List<Ball> findExtrasByInningsId(@Param("inningsId") Long inningsId);
    
    @Query("SELECT b FROM Ball b WHERE b.innings.id = :inningsId ORDER BY b.createdAt DESC")
    List<Ball> findLatestBallsByInningsId(@Param("inningsId") Long inningsId);
    
    @Query("SELECT b FROM Ball b WHERE b.innings.id = :inningsId ORDER BY b.createdAt DESC LIMIT 1")
    Optional<Ball> findLastBallByInningsId(@Param("inningsId") Long inningsId);
}
