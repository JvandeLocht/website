import React from 'react';

const Header = () => {
  return (
    <header className="header">
      <div className="container">
        <div className="header-content">
          <a href="/" className="logo">Jan van de Locht</a>
          <nav className="nav">
            <a href="/">Resume</a>
            <a href="/calc" className="active">Calculator</a>
          </nav>
        </div>
      </div>
    </header>
  );
};

export default Header;