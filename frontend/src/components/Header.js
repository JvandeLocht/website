import React from 'react';

const Header = React.memo(() => {
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
});

Header.displayName = 'Header';

export default Header;