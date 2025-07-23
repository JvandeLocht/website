import React, { useState, useCallback, useMemo } from 'react';
import axios from 'axios';

const CalculatorPage = () => {
  const [operation, setOperation] = useState('add');
  const [num1, setNum1] = useState('');
  const [num2, setNum2] = useState('');
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const validateInputs = useCallback((operation, num1, num2) => {
    if (!num1 || isNaN(parseFloat(num1))) {
      return 'Bitte geben Sie eine gültige erste Zahl ein';
    }
    
    if (operation !== 'sqrt' && (!num2 || isNaN(parseFloat(num2)))) {
      return 'Bitte geben Sie eine gültige zweite Zahl ein';
    }
    
    if (operation === 'divide' && parseFloat(num2) === 0) {
      return 'Division durch Null ist nicht erlaubt';
    }
    
    if (operation === 'sqrt' && parseFloat(num1) < 0) {
      return 'Quadratwurzel aus negativen Zahlen ist nicht erlaubt';
    }
    
    return null;
  }, []);

  const handleCalculate = useCallback(async (e) => {
    e.preventDefault();
    
    const validationError = validateInputs(operation, num1, num2);
    if (validationError) {
      setError(validationError);
      setResult(null);
      return;
    }

    setLoading(true);
    setError(null);
    setResult(null);

    try {
      const payload = {
        operation,
        num1: parseFloat(num1),
        ...(operation !== 'sqrt' && { num2: parseFloat(num2) })
      };

      const response = await axios.post('/api/calculate', payload);
      setResult(response.data.result);
    } catch (err) {
      const errorMessage = err.response?.data?.message || 'Ein Fehler ist aufgetreten';
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  }, [operation, num1, num2, validateInputs]);

  const resetForm = useCallback(() => {
    setNum1('');
    setNum2('');
    setResult(null);
    setError(null);
  }, []);

  const requiresSecondNumber = useMemo(() => operation !== 'sqrt', [operation]);

  return (
    <div className="calculator-container">
      <h1 className="calculator-title">Technischer Rechner</h1>
      
      <form onSubmit={handleCalculate} className="calculator-form">
        <div className="form-group">
          <label htmlFor="operation">Operation:</label>
          <select
            id="operation"
            value={operation}
            onChange={(e) => setOperation(e.target.value)}
          >
            <option value="add">Addition (+)</option>
            <option value="subtract">Subtraktion (-)</option>
            <option value="multiply">Multiplikation (×)</option>
            <option value="divide">Division (÷)</option>
            <option value="power">Potenz (^)</option>
            <option value="sqrt">Quadratwurzel (√)</option>
          </select>
        </div>

        <div className="form-group">
          <label htmlFor="num1">Erste Zahl:</label>
          <input
            type="number"
            id="num1"
            value={num1}
            onChange={(e) => setNum1(e.target.value)}
            step="any"
            required
          />
        </div>

         {requiresSecondNumber && (
           <div className="form-group">
             <label htmlFor="num2">Zweite Zahl:</label>
             <input
               type="number"
               id="num2"
               value={num2}
               onChange={(e) => setNum2(e.target.value)}
               step="any"
               required={requiresSecondNumber}
             />
           </div>
         )}
        <div style={{ display: 'flex', gap: '10px' }}>
          <button 
            type="submit" 
            className="calculate-btn"
            disabled={loading}
          >
            {loading ? <span className="loading"></span> : 'Berechnen'}
          </button>
          
          <button 
            type="button" 
            onClick={resetForm}
            className="calculate-btn"
            style={{ background: 'var(--text-light)' }}
          >
            Zurücksetzen
          </button>
        </div>
      </form>

      {result !== null && (
        <div className="result success">
          Ergebnis: {result}
        </div>
      )}

      {error && (
        <div className="result error">
          Fehler: {error}
        </div>
      )}
    </div>
  );
};

export default CalculatorPage;