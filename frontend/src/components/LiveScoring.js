import React from 'react';
import { useLeague } from '../App';

const LiveScoring = () => {
  const { leagueData, updateLeagueData } = useLeague();

  const getTeamById = (teamId) => {
    return leagueData.teams.find(t => t.id === teamId);
  };

  const formatMatchLabel = (aId, bId) => {
    const a = getTeamById(aId);
    const b = getTeamById(bId);
    return `${a ? a.name : 'Unknown'} vs ${b ? b.name : 'Unknown'}`;
  };

  const handleVote = (team) => {
    if (!leagueData.live.matchId) return;
    
    updateLeagueData(prev => ({
      ...prev,
      live: {
        ...prev.live,
        votes: {
          ...prev.live.votes,
          [team]: prev.live.votes[team] + 1
        }
      }
    }));
  };

  const liveMatch = leagueData.matches.find(m => m.id === leagueData.live.matchId);

  if (!liveMatch) {
    return (
      <div className="space-y-8">
        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <h2 className="text-lg sm:text-xl font-semibold">Live Scoring</h2>
          </div>
          <div className="rounded-lg border border-slate-800/60 bg-surface p-4 shadow-card text-sm text-slate-300">
            No live match. Please check back later.
          </div>
        </div>
      </div>
    );
  }

  const teamA = getTeamById(liveMatch.teamAId);
  const teamB = getTeamById(liveMatch.teamBId);
  const totalVotes = Math.max(1, leagueData.live.votes.a + leagueData.live.votes.b);
  const aPct = Math.round(100 * leagueData.live.votes.a / totalVotes);
  const bPct = 100 - aPct;

  // Calculate cumulative runs for ogive chart
  const maxOvers = Math.max(leagueData.live.overRuns.a.length, leagueData.live.overRuns.b.length);
  let cumA = 0, cumB = 0;
  const cumulativeA = leagueData.live.overRuns.a.map(r => cumA += r);
  const cumulativeB = leagueData.live.overRuns.b.map(r => cumB += r);
  const maxCumulative = Math.max(1, ...cumulativeA, ...cumulativeB);

  return (
    <div className="space-y-8">
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <h2 className="text-lg sm:text-xl font-semibold">Live Scoring</h2>
        </div>
        <div className="rounded-lg border border-slate-800/60 bg-surface p-4 shadow-card text-sm text-slate-300">
          {teamA?.name || 'Team A'} vs {teamB?.name || 'Team B'} • {liveMatch.venue} • {liveMatch.date} {liveMatch.time || ''}
        </div>
        
        <div className="rounded-lg border border-slate-800/60 bg-surface p-4 shadow-card">
          <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
            <div className="min-w-0">
              Who will win? {teamA?.name || 'Team A'} vs {teamB?.name || 'Team B'}
            </div>
            <div className="flex items-center gap-2">
              <button 
                onClick={() => handleVote('a')}
                className="rounded-md bg-primary/90 px-4 py-2 text-xs font-semibold text-slate-900 hover:bg-primary"
              >
                Vote Team A
              </button>
              <button 
                onClick={() => handleVote('b')}
                className="rounded-md bg-accent/30 border border-accent/50 px-4 py-2 text-xs font-semibold text-slate-200 hover:bg-accent/40"
              >
                Vote Team B
              </button>
            </div>
          </div>
          <div className="mt-4 grid grid-cols-2 gap-3 text-xs">
            <div>
              <div className="flex items-center justify-between">
                <span>{teamA?.name || 'Team A'}</span>
                <span>{leagueData.live.votes.a} ({aPct}%)</span>
              </div>
              <div className="mt-1 h-2 w-full rounded bg-background">
                <div 
                  className="h-2 rounded bg-primary" 
                  style={{ width: `${aPct}%` }}
                ></div>
              </div>
            </div>
            <div>
              <div className="flex items-center justify-between">
                <span>{teamB?.name || 'Team B'}</span>
                <span>{leagueData.live.votes.b} ({bPct}%)</span>
              </div>
              <div className="mt-1 h-2 w-full rounded bg-background">
                <div 
                  className="h-2 rounded bg-accent" 
                  style={{ width: `${bPct}%` }}
                ></div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="space-y-3">
        <h3 className="text-sm font-medium text-slate-300">Runs Per Over</h3>
        <div className="rounded-lg border border-slate-800/60 bg-surface p-4 shadow-card">
          <div className="overflow-x-auto">
            <div className="min-w-[560px]">
              <svg width={Math.max(maxOvers * 40, 480)} height="120" className="w-full h-32">
                <defs>
                  <linearGradient id="gradA" x1="0%" y1="0%" x2="0%" y2="100%">
                    <stop offset="0%" style={{ stopColor: '#22d3ee', stopOpacity: 0.8 }} />
                    <stop offset="100%" style={{ stopColor: '#22d3ee', stopOpacity: 0.3 }} />
                  </linearGradient>
                  <linearGradient id="gradB" x1="0%" y1="0%" x2="0%" y2="100%">
                    <stop offset="0%" style={{ stopColor: '#60a5fa', stopOpacity: 0.8 }} />
                    <stop offset="100%" style={{ stopColor: '#60a5fa', stopOpacity: 0.3 }} />
                  </linearGradient>
                </defs>
                <g transform="translate(20,10)">
                  {/* Team A line */}
                  {cumulativeA.map((cum, i) => {
                    const x = (i * Math.max(maxOvers * 40, 480) / Math.max(maxOvers, 12));
                    const y = 120 - 20 - (cum / maxCumulative * (120 - 40));
                    const nextX = ((i + 1) * Math.max(maxOvers * 40, 480) / Math.max(maxOvers, 12));
                    const nextY = i + 1 < cumulativeA.length ? 
                      120 - 20 - (cumulativeA[i + 1] / maxCumulative * (120 - 40)) : y;
                    return (
                      <line 
                        key={`a-line-${i}`}
                        x1={x} 
                        y1={y} 
                        x2={nextX} 
                        y2={nextY} 
                        stroke="#22d3ee" 
                        strokeWidth="3" 
                        fill="none" 
                      />
                    );
                  })}
                  {/* Team B line */}
                  {cumulativeB.map((cum, i) => {
                    const x = (i * Math.max(maxOvers * 40, 480) / Math.max(maxOvers, 12));
                    const y = 120 - 20 - (cum / maxCumulative * (120 - 40));
                    const nextX = ((i + 1) * Math.max(maxOvers * 40, 480) / Math.max(maxOvers, 12));
                    const nextY = i + 1 < cumulativeB.length ? 
                      120 - 20 - (cumulativeB[i + 1] / maxCumulative * (120 - 40)) : y;
                    return (
                      <line 
                        key={`b-line-${i}`}
                        x1={x} 
                        y1={y} 
                        x2={nextX} 
                        y2={nextY} 
                        stroke="#60a5fa" 
                        strokeWidth="3" 
                        fill="none" 
                      />
                    );
                  })}
                  {/* Team A points */}
                  {cumulativeA.map((cum, i) => {
                    const x = (i * Math.max(maxOvers * 40, 480) / Math.max(maxOvers, 12));
                    const y = 120 - 20 - (cum / maxCumulative * (120 - 40));
                    return (
                      <circle 
                        key={`a-point-${i}`}
                        cx={x} 
                        cy={y} 
                        r="4" 
                        fill="#22d3ee" 
                      />
                    );
                  })}
                  {/* Team B points */}
                  {cumulativeB.map((cum, i) => {
                    const x = (i * Math.max(maxOvers * 40, 480) / Math.max(maxOvers, 12));
                    const y = 120 - 20 - (cum / maxCumulative * (120 - 40));
                    return (
                      <circle 
                        key={`b-point-${i}`}
                        cx={x} 
                        cy={y} 
                        r="4" 
                        fill="#60a5fa" 
                      />
                    );
                  })}
                </g>
              </svg>
              <div className="mt-2 grid grid-cols-12 gap-2 text-center text-xs text-slate-400">
                {Array.from({ length: Math.max(maxOvers, 12) }, (_, i) => (
                  <div key={i}>Ov {i + 1}</div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="space-y-3">
        <h3 className="text-sm font-medium text-slate-300">Player Live Performance</h3>
        <div className="overflow-hidden rounded-lg border border-slate-800/60 bg-surface shadow-card">
          <div className="overflow-x-auto">
            <table className="min-w-full text-sm">
              <thead className="bg-background/60">
                <tr className="text-left text-slate-300">
                  <th className="px-4 py-3">Player</th>
                  <th className="px-4 py-3">Team</th>
                  <th className="px-4 py-3">Runs(B)</th>
                  <th className="px-4 py-3">Wkts</th>
                  <th className="px-4 py-3">Overs</th>
                  <th className="px-4 py-3">SR</th>
                  <th className="px-4 py-3">Eco</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-800/60">
                {leagueData.live.playerLive.length === 0 ? (
                  <tr>
                    <td colSpan="7" className="px-4 py-4 text-sm text-slate-400">No player updates yet.</td>
                  </tr>
                ) : (
                  leagueData.live.playerLive.map(pl => {
                    const playerObj = (leagueData.players[pl.teamId] || []).find(p => p.id === pl.playerId);
                    const teamName = getTeamById(pl.teamId)?.name || '';
                    const sr = pl.balls ? (pl.runs / pl.balls * 100).toFixed(1) : '0.0';
                    const eco = pl.overs ? (pl.runs / pl.overs).toFixed(1) : '0.0';
                    
                    return (
                      <tr key={pl.playerId}>
                        <td className="px-4 py-3">{playerObj?.name || 'Player'}</td>
                        <td className="px-4 py-3">{teamName}</td>
                        <td className="px-4 py-3">{pl.runs} ({pl.balls})</td>
                        <td className="px-4 py-3">{pl.wkts}</td>
                        <td className="px-4 py-3">{pl.overs}</td>
                        <td className="px-4 py-3">{sr}</td>
                        <td className="px-4 py-3">{eco}</td>
                      </tr>
                    );
                  })
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LiveScoring;

