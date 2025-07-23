import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from './components/Header';
import CalculatorPage from './pages/CalculatorPage';

function App() {
  return (
    <Router basename="/calc">
      <div className="App">
        <Header />
        <main className="main">
          <div className="container">
            <Routes>
              <Route path="/" element={<CalculatorPage />} />
            </Routes>
          </div>
        </main>
      </div>
    </Router>
  );
}

export default App;