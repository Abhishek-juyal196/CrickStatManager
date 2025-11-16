import React, { createContext, useContext, useState, useEffect } from 'react';
import Header from './components/Header';
import Home from './components/Home';
import Team from './components/Team';
import Admin from './components/Admin';
import LiveScoring from './components/LiveScoring';
import api from './api';

// Initial state sourced from backend; keep UI-only slices for live features
const initialLeagueData = {
  leagues: [],
  activeLeagueId: '',
  teams: [],
  matches: [],
  pointsTable: [],
  players: {},
  teamPerformance: {},
  live: {
    matchId: '',
    overRuns: { a: [], b: [] },
    votes: { a: 0, b: 0 },
    playerLive: []
  }
};

// Context
const LeagueContext = createContext();

export const useLeague = () => {
  const context = useContext(LeagueContext);
  if (!context) {
    throw new Error('useLeague must be used within a LeagueProvider');
  }
  return context;
};

// Provider Component
export const LeagueProvider = ({ children }) => {
  const [leagueData, setLeagueData] = useState(initialLeagueData);
  const [activeTab, setActiveTab] = useState('home');
  const [selectedTeamId, setSelectedTeamId] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const setActiveLeagueId = (leagueId) => {
    setLeagueData(prev => ({ ...prev, activeLeagueId: String(leagueId || '') }));
  };

  const loadAll = async () => {
    try {
      setLoading(true);
      setError('');
      const [leagues, teams, matches] = await Promise.all([
        api.getLeagues(),
        api.getTeams(),
        api.getMatches()
      ]);
      const activeLeagueId = leagues && leagues.length ? String(leagues[0].id) : '';
      
      // Load standings for active league if available
      let standings = [];
      if (activeLeagueId) {
        try {
          standings = await api.getLeagueStandings(Number(activeLeagueId)) || [];
        } catch (e) {
          console.warn('Failed to load standings:', e);
        }
      }
      
      // Load players for all teams
      const playersPromises = (teams || []).map(async (team) => {
        try {
          const teamPlayers = await api.getTeamPlayers(team.id) || [];
          return { teamId: String(team.id), players: teamPlayers };
        } catch (e) {
          console.warn(`Failed to load players for team ${team.id}:`, e);
          return { teamId: String(team.id), players: [] };
        }
      });
      const playersResults = await Promise.all(playersPromises);
      const playersMap = {};
      playersResults.forEach(({ teamId, players }) => {
        playersMap[teamId] = players.map(p => ({
          id: String(p.id),
          name: p.name,
          role: p.role || 'Player',
          stats: {
            matches: p.matches || 0,
            runs: p.runs || 0,
            wickets: p.wickets || 0,
            strikeRate: p.strikeRate || 0,
            economy: p.economy || 0
          }
        }));
      });
      
      // Transform standings to match frontend format
      const pointsTable = (standings || []).map(s => ({
        teamId: String(s.teamId || s.id),
        played: s.played || 0,
        won: s.won || 0,
        lost: s.lost || 0,
        points: s.points || 0
      }));
      
      setLeagueData(prev => ({
        ...prev,
        leagues,
        activeLeagueId,
        teams: (teams || []).map(t => ({ id: String(t.id), name: t.name, logo: t.logoUrl, leagueId: String(t.leagueId) })),
        matches: (matches || []).map(m => ({
          id: String(m.id),
          leagueId: String(m.leagueId),
          teamAId: String(m.team1Id),
          teamBId: String(m.team2Id),
          venue: m.venue,
          date: m.matchDate,
          time: ''
        })),
        pointsTable,
        players: playersMap
      }));
    } catch (e) {
      setError(e.message || 'Failed to load data');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadAll();
  }, []);

  const updateLeagueData = (updater) => {
    setLeagueData(prev => {
      if (typeof updater === 'function') {
        return updater(prev);
      }
      return { ...prev, ...updater };
    });
  };

  const value = {
    leagueData,
    updateLeagueData,
    activeTab,
    setActiveTab,
    selectedTeamId,
    setSelectedTeamId,
    loading,
    error,
    reload: loadAll,
    setActiveLeagueId,
  };

  return (
    <LeagueContext.Provider value={value}>
      {children}
    </LeagueContext.Provider>
  );
};

// Main App Component
function App() {
  return (
    <LeagueProvider>
      <AppContent />
    </LeagueProvider>
  );
}

// App Content Component (uses the context)
function AppContent() {
  const { activeTab } = useLeague();

  const renderActiveView = () => {
    switch (activeTab) {
      case 'home':
        return <Home />;
      case 'team':
        return <Team />;
      case 'admin':
        return <Admin />;
      case 'live':
        return <LiveScoring />;
      default:
        return <Home />;
    }
  };

  return (
    <div className="min-h-screen flex flex-col bg-gray-900 text-white">
      <Header />
      <main className="flex-1">
        <div className="mx-auto max-w-6xl px-4 py-6 sm:py-8">
          {renderActiveView()}
        </div>
      </main>
      <footer className="mt-auto border-t border-gray-700 bg-gray-800">
        <div className="mx-auto max-w-6xl px-4 py-4 text-xs text-gray-400">
          © {new Date().getFullYear()} Premier Cricket League. React Version. All data in-memory.
        </div>
      </footer>
    </div>
  );
}

export default App;