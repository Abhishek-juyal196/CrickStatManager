module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        background: {
          DEFAULT: '#0b1220',
          soft: '#0f172a'
        },
        surface: {
          DEFAULT: '#111827',
          raised: '#1f2937'
        },
        primary: {
          DEFAULT: '#22d3ee',
          soft: '#06b6d4'
        },
        accent: '#60a5fa'
      },
      boxShadow: {
        card: '0 8px 24px rgba(0,0,0,0.35)'
      }
    },
  },
  plugins: [],
}

