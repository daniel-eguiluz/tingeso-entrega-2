// src/components/ListadoUsuarios.jsx

import { useEffect, useState } from "react";
import usuarioService from "../services/ms-usuario.service.js";
import { useNavigate } from "react-router-dom";

export default function ListadoUsuarios() {
  const [usuarios, setUsuarios] = useState([]);
  const [error, setError] = useState(null);
  const navigate = useNavigate(); // Hook para navegación programática

  async function fetchUsuarios() {
    try {
      const response = await usuarioService.getAllUsuarios();
      setUsuarios(response.data);
    } catch (error) {
      console.error("Error al obtener los usuarios:", error);
      if (error.response) {
        setError(`Error: ${error.response.status} - ${error.response.data}`);
      } else if (error.request) {
        setError("Error: No se recibió respuesta del servidor.");
      } else {
        setError(`Error: ${error.message}`);
      }
    }
  }

  useEffect(() => {
    fetchUsuarios();
  }, []);

  const handleSolicitarCredito = (id) => {
    navigate(`/usuarios/${id}/solicitar-credito`);
  };

  const handleSimularCredito = (id) => {
    navigate(`/usuarios/${id}/simular-credito`);
  };

  const handleEvaluarCredito = (id) => {
    navigate(`/usuarios/${id}/evaluar-credito`);
  };

  const handleObtenerEstadoSolicitud = (id) => {
    navigate(`/usuarios/${id}/estado-solicitud`);
  };

  const handleCalcularCostoTotal = (id) => {
    navigate(`/usuarios/${id}/calcular-costo-total`);
  };

  const handleAgregarUsuario = () => {
    navigate(`/usuarios/add`);
  };

  return (
    <div className="container">
      <h1>Listado de Usuarios</h1>

      {/* Botón para Agregar Usuario */}
      <div className="mb-3 d-flex justify-content-start">
        <button
          className="btn btn-success me-2"
          onClick={handleAgregarUsuario}
        >
          Agregar Usuario
        </button>
      </div>

      {error && <div className="alert alert-danger">{error}</div>}

      <table className="table table-striped">
        <thead>
          <tr>
            <th scope="col">ID</th>
            <th scope="col">RUT</th>
            <th scope="col">Nombre</th>
            <th scope="col">Apellido</th>
            <th scope="col">Edad</th>
            <th scope="col">Tipo de Empleado</th>
            <th scope="col">Acciones</th>
          </tr>
        </thead>
        <tbody>
          {
            usuarios.map((usuario) => (
              <tr key={usuario.id}>
                <td>{usuario.id}</td>
                <td>{usuario.rut}</td>
                <td>{usuario.nombre}</td>
                <td>{usuario.apellido}</td>
                <td>{usuario.edad}</td>
                <td>{usuario.tipoEmpleado}</td>
                <td>
                  <button
                    className="btn btn-primary btn-sm me-2"
                    onClick={() => handleSolicitarCredito(usuario.id)}
                  >
                    Solicitar Crédito
                  </button>
                  <button
                    className="btn btn-warning btn-sm me-2"
                    onClick={() => handleSimularCredito(usuario.id)}
                  >
                    Simular Crédito
                  </button>
                  <button
                    className="btn btn-info btn-sm me-2"
                    onClick={() => handleEvaluarCredito(usuario.id)}
                  >
                    Evaluar Crédito
                  </button>
                  <button
                    className="btn btn-secondary btn-sm me-2"
                    onClick={() => handleObtenerEstadoSolicitud(usuario.id)}
                  >
                    Obtener Estado Solicitud
                  </button>
                  <button
                    className="btn btn-success btn-sm me-2"
                    onClick={() => handleCalcularCostoTotal(usuario.id)}
                  >
                    Calcular Costo Total
                  </button>
                </td>
              </tr>
            ))
          }
        </tbody>
      </table>
    </div>
  );
}
