// src/components/SimularCredito.jsx

import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import simulacionService from "../services/ms-simulacion.service.js";

export default function SimularCredito() {
  const { id } = useParams(); // Obtener el ID del usuario desde la URL
  const navigate = useNavigate();
  const [simulacion, setSimulacion] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  const fetchSimulacion = async () => {
    try {
      const response = await simulacionService.simularCredito(id);
      setSimulacion(response.data);
      setLoading(false);
    } catch (error) {
      console.error("Error al simular el crédito:", error);
      if (error.response) {
        setError(`Error: ${error.response.status} - ${error.response.data.error}`);
      } else if (error.request) {
        setError("Error: No se recibió respuesta del servidor.");
      } else {
        setError(`Error: ${error.message}`);
      }
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchSimulacion();
  }, [id]);

  const handleVolver = () => {
    navigate("/");
  };

  return (
    <div className="container">
      <h1>Simulación de Crédito para Usuario ID: {id}</h1>

      {loading && (
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Cargando...</span>
        </div>
      )}

      {error && <div className="alert alert-danger">{error}</div>}

      {simulacion && (
        <div className="mt-4">
          <h3>Detalles de la Simulación</h3>
          <ul className="list-group">
            <li className="list-group-item"><strong>Monto:</strong> ${simulacion.monto.toLocaleString()}</li>
            <li className="list-group-item"><strong>Plazo:</strong> {simulacion.plazo} años</li>
            <li className="list-group-item"><strong>Tasa de Interés Anual:</strong> {simulacion.tasaInteres}%</li>
            <li className="list-group-item"><strong>Pago Mensual:</strong> ${simulacion.pagoMensual.toFixed(2).toLocaleString()}</li>
            <li className="list-group-item"><strong>Intereses Totales:</strong> ${simulacion.interesesTotales.toFixed(2).toLocaleString()}</li>
            <li className="list-group-item"><strong>Total Pagos:</strong> ${simulacion.totalPagos.toFixed(2).toLocaleString()}</li>
          </ul>
          <button className="btn btn-secondary mt-3" onClick={handleVolver}>Volver</button>
        </div>
      )}
    </div>
  );
}
