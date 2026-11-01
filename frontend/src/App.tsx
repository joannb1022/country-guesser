import { useState } from 'react'
import './App.css'

function App() {
  const [countryName, setCountryName] = useState("")

  function handleSearch (){

  }
  return (
      <main>
        <h1>Country Guesser</h1>
        <input
            type="text"
            placeholder="Enter country name..."
            value={countryName}
            onChange={(e) => setCountryName(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === "Enter") handleSearch();
            }}
        />

        <button onClick={handleSearch}>
          Search
        </button>
      </main>
  );
}

export default App
