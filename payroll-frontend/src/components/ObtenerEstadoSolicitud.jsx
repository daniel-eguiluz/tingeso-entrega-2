// src/components/ObtenerEstadoSolicitud.js

import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import seguimientoService from "../services/ms-seguimiento.service.js";

export default function ObtenerEstadoSolicitud() {
  const { id } = useParams(); // Obtener el ID del usuario desde la URL
  const navigate = useNavigate();
  const [estadoSolicitud, setEstadoSolicitud] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  const fetchEstadoSolicitud = async () => {
    try {
      const response = await seguimientoService.obtenerEstadoPorUsuario(id);
      setEstadoSolicitud(response.data);
      setLoading(false);
    } catch (error) {
      console.error("Error al obtener el estado de la solicitud:", error);
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
    fetchEstadoSolicitud();
  }, [id]);

  const handleVolver = () => {
    navigate("/usuario/listado");
  };

  return (
    <div className="container">
      <h1>Estado de Solicitud para Usuario ID: {id}</h1>

      {loading && (
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Cargando...</span>
        </div>
      )}

      {error && <div className="alert alert-danger">{error}</div>}

      {estadoSolicitud && (
        <div className="mt-4">
          <h3>Detalles del Estado de la Solicitud</h3>
          <ul className="list-group">
            <li className="list-group-item"><strong>Estado:</strong> {estadoSolicitud.estado}</li>
            {/* Agrega más campos según la estructura de respuesta */}
          </ul>
          <button className="btn btn-secondary" onClick={handleVolver}>Volver</button>
        </div>
      )}
    </div>
  );
}
