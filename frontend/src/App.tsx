import { Routes, Route, Navigate, Link, useLocation } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import TripUnionPage from "./pages/TripUnionPage";
import TripUnionCreatePage from "./pages/TripUnionCreatePage";
import TripUnionEditPage from "./pages/TripUnionEditPage";
import TripPage from "./pages/TripPage";
import TripCreatePage from "./pages/TripCreatePage";
import TripEditPage from "./pages/TripEditPage";
import { useAuth } from "./context/useAuth";
import "./App.css";

function App() {
  const { token } = useAuth();
  const location = useLocation();

  return (
    <div className="app-container">
      {token && (
        <div className="tabs">
          <Link 
            to="/trip-union" 
            className={`tab ${location.pathname.startsWith('/trip-union') ? 'active' : ''}`}
          >
            Путевые листы
          </Link>
          <Link 
            to="/trips" 
            className={`tab ${location.pathname.startsWith('/trips') ? 'active' : ''}`}
          >
            Рейсы
          </Link>
        </div>
      )}
      <Routes>
        <Route path="/" element={token ? <Navigate to="/trip-union" /> : <LoginPage />} />
        <Route path="/trip-union" element={token ? <TripUnionPage /> : <Navigate to="/login" />} />
        <Route path="/trip-union/create" element={token ? <TripUnionCreatePage /> : <Navigate to="/login" />} />
        <Route path="/trip-union/edit/:id" element={token ? <TripUnionEditPage /> : <Navigate to="/login" />} />
        <Route path="/trips/edit/:tripId" element={token ? <TripEditPage /> : <Navigate to="/login" />} />
        <Route path="/trips/create" element={token ? <TripCreatePage /> : <Navigate to="/login" />} />
        <Route path="/trips" element={token ? <TripPage /> : <Navigate to="/login" />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="*" element={<Navigate to={token ? "/trip-union" : "/login"} />} />
      </Routes>
    </div>
  );
}

export default App;
