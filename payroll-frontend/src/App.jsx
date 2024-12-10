// src/App.jsx

import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Navbar from "./components/Navbar";
import NotFound from './components/NotFound';
import ListadoUsuarios from './components/ListadoUsuarios';
import SolicitarCredito from './components/SolicitarCredito';
import AddUsuario from './components/AddUsuario';
import SimularCredito from './components/SimularCredito';
import EvaluarCredito from './components/EvaluarCredito';
import ObtenerEstadoSolicitud from './components/ObtenerEstadoSolicitud';
import CalcularCostoTotal from './components/CalcularCostoTotal'; // Nuevo componente

function App() {
  return (
      <Router>
          <div className="container">
            <Navbar />
            <Routes>
              <Route path="/" element={<ListadoUsuarios />} />
              <Route path="/usuarios/:id/solicitar-credito" element={<SolicitarCredito />} />
              <Route path="/usuarios/add" element={<AddUsuario />} />
              <Route path="/usuarios/:id/simular-credito" element={<SimularCredito />} />
              <Route path="/usuarios/:id/evaluar-credito" element={<EvaluarCredito />} />
              <Route path="/usuarios/:id/estado-solicitud" element={<ObtenerEstadoSolicitud />} />
              <Route path="/usuarios/:id/calcular-costo-total" element={<CalcularCostoTotal />} />
              <Route path="*" element={<NotFound />} />
            </Routes>
          </div>
      </Router>
  );
}

export default App;
