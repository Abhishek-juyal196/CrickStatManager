import React from 'react';
import { useLeague } from '../App';

const Header = () => {
  const { activeTab, setActiveTab, leagueData, setActiveLeagueId, reload, loading } = useLeague();

  const tabs = [
    { id: 'home', label: 'Home' },
    { id: 'team', label: 'Team' },
    { id: 'live', label: 'Live Scoring' },
    { id: 'admin', label: 'Admin Panel' }
  ];

  return (
    <header className="border-b border-gray-700 bg-gray-800 shadow-lg">
      <div className="mx-auto max-w-6xl px-4 py-4 flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="h-9 w-9 rounded-full bg-blue-500 flex items-center justify-center">
            <span className="text-white font-bold text-lg">🏏</span>
          </div>
          <h1 className="text-xl sm:text-2xl font-semibold tracking-tight text-white">Premier Cricket League</h1>
        </div>
        <nav className="flex items-center gap-2">
          {tabs.map(tab => (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id)}
              className={`px-4 py-2 rounded-md text-sm font-medium transition-colors border ${
                activeTab === tab.id
                  ? 'bg-blue-500 text-white border-blue-500'
                  : 'text-gray-300 border-gray-600 hover:bg-gray-700 hover:text-white'
              }`}
            >
              {tab.label}
            </button>
          ))}
        </nav>
        <div className="flex items-center gap-2">
          <select
            value={leagueData.activeLeagueId}
            onChange={(e) => setActiveLeagueId(e.target.value)}
            className="px-3 py-2 rounded-md text-sm bg-gray-700 text-white border border-gray-600"
          >
            <option value="">All Leagues</option>
            {leagueData.leagues.map(l => (
              <option key={l.id} value={l.id}>{l.name}</option>
            ))}
          </select>
          <button
            onClick={reload}
            disabled={loading}
            className="px-3 py-2 rounded-md text-sm border border-gray-600 text-gray-300 hover:bg-gray-700 disabled:opacity-50"
          >
            Refresh
          </button>
        </div>
      </div>
    </header>
  );
};

export default Header;
