// src/components/EvaluarCredito.jsx

import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import evaluacionService from "../services/ms-evaluacion.service.js";

export default function EvaluarCredito() {
  const { id } = useParams(); // Obtener el ID de la solicitud desde la URL
  const navigate = useNavigate();
  const [evaluacion, setEvaluacion] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  const fetchEvaluacion = async () => {
    try {
      const response = await evaluacionService.evaluarCredito(id);
      setEvaluacion(response.data);
      setLoading(false);
    } catch (error) {
      console.error("Error al evaluar el crédito:", error);
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
    fetchEvaluacion();
  }, [id]);

  const handleVolver = () => {
    navigate("/");
  };

  return (
    <div className="container">
      <h1>Evaluación de Crédito para Solicitud ID: {id}</h1>

      {loading && (
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Cargando...</span>
        </div>
      )}

      {error && <div className="alert alert-danger">{error}</div>}

      {evaluacion && (
        <div className="mt-4">
          <h3>Resultados de la Evaluación</h3>
          <div className={`alert ${evaluacion.aprobado ? 'alert-success' : 'alert-danger'}`} role="alert">
            {evaluacion.aprobado ? "Crédito Aprobado" : "Crédito Rechazado"}
          </div>
          <h4>Detalles de las Reglas Evaluadas:</h4>
          <table className="table table-bordered">
            <thead>
              <tr>
                <th>Regla</th>
                <th>Cumplida</th>
              </tr>
            </thead>
            <tbody>
              {
                Object.entries(evaluacion.reglasCumplidas).map(([regla, cumplida], index) => (
                  <tr key={index}>
                    <td>{regla}</td>
                    <td>{cumplida ? "Sí" : "No"}</td>
                  </tr>
                ))
              }
            </tbody>
          </table>
          <button className="btn btn-secondary" onClick={handleVolver}>Volver</button>
        </div>
      )}
    </div>
  ); 
}
