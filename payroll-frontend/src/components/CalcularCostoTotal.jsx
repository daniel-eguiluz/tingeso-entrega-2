// src/components/CalcularCostoTotal.jsx

import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import costosService from "../services/ms-costos.service.js";

export default function CalcularCostoTotal() {
  const { id } = useParams(); // Obtener el ID del usuario desde la URL
  const navigate = useNavigate();
  const [costoTotal, setCostoTotal] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  const fetchCostoTotal = async () => {
    try {
      const response = await costosService.calcularCostos(id);
      setCostoTotal(response.data);
      setLoading(false);
    } catch (error) {
      console.error("Error al calcular el costo total del préstamo:", error);
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
    fetchCostoTotal();
  }, [id]);

  const handleVolver = () => {
    navigate("/");
  };

  return (
    <div className="container">
      <h1>Calcular Costo Total para Usuario ID: {id}</h1>

      {loading && (
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Cargando...</span>
        </div>
      )}

      {error && <div className="alert alert-danger">{error}</div>}

      {costoTotal && (
        <div className="mt-4">
          <h3>Detalles del Costo Total del Préstamo</h3>
          <table className="table table-bordered">
            <tbody>
              <tr>
                <th>Monto del Préstamo</th>
                <td>${costoTotal.montoPrestamo.toLocaleString()}</td>
              </tr>
              <tr>
                <th>Plazo (Años)</th>
                <td>{costoTotal.plazoAnios} años</td>
              </tr>
              <tr>
                <th>Tasa de Interés Anual</th>
                <td>{costoTotal.tasaInteresAnual}%</td>
              </tr>
              <tr>
                <th>Tasa de Interés Mensual</th>
                <td>{costoTotal.tasaInteresMensual}%</td>
              </tr>
              <tr>
                <th>Cuota Mensual</th>
                <td>${costoTotal.cuotaMensual.toLocaleString()}</td>
              </tr>
              <tr>
                <th>Seguro Desgravamen Mensual</th>
                <td>${costoTotal.seguroDesgravamenMensual.toLocaleString()}</td>
              </tr>
              <tr>
                <th>Seguro Incendio Mensual</th>
                <td>${costoTotal.seguroIncendioMensual.toLocaleString()}</td>
              </tr>
              <tr>
                <th>Comisión por Administración</th>
                <td>${costoTotal.comisionAdministracion.toLocaleString()}</td>
              </tr>
              <tr>
                <th>Costo Mensual Total</th>
                <td>${costoTotal.costoMensualTotal.toLocaleString()}</td>
              </tr>
              <tr>
                <th>Costo Total del Préstamo</th>
                <td>${costoTotal.costoTotal.toLocaleString()}</td>
              </tr>
              <tr>
                <th>Número de Pagos</th>
                <td>{costoTotal.numeroPagos} pagos</td>
              </tr>
            </tbody>
          </table>
          <button className="btn btn-secondary" onClick={handleVolver}>Volver</button>
        </div>
      )}
    </div>
  );
}
