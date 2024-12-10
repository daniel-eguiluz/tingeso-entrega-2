// src/components/SolicitarCredito.jsx

import { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import solicitudService from "../services/ms-solicitud.service.js";

export default function SolicitarCredito() {
  const { id } = useParams(); // Obtener el ID del usuario desde la URL
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    tipo: "",
    plazo: "",
    tasaInteres: "",
    monto: "",
    valorPropiedad: "",
    antiguedadLaboral: "",
    ingresoMensual: "",
    saldo: "",
    deudas: "",
    cantidadDeudasPendientes: "",
    antiguedadCuenta: "",
    ingresosUltimos24Meses: Array(24).fill(""),
    saldosMensuales: Array(12).fill(""),
    depositosUltimos12Meses: Array(12).fill(""),
    retirosUltimos6Meses: Array(6).fill("")
  });

  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  // Manejar cambios en los campos individuales
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  // Manejar cambios en los campos que son arrays
  const handleArrayChange = (fieldName, index, value) => {
    const updatedArray = [...formData[fieldName]];
    updatedArray[index] = value;
    setFormData({ ...formData, [fieldName]: updatedArray });
  };

  // Manejar el envío del formulario
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setSuccess(null);

    try {
      // Preparar el objeto de solicitud con la estructura plana
      const solicitudCredito = {
        tipo: formData.tipo,
        plazo: Number(formData.plazo),
        tasaInteres: Number(formData.tasaInteres),
        monto: Number(formData.monto),
        valorPropiedad: Number(formData.valorPropiedad),
        antiguedadLaboral: Number(formData.antiguedadLaboral),
        ingresoMensual: Number(formData.ingresoMensual),
        saldo: Number(formData.saldo),
        deudas: Number(formData.deudas),
        cantidadDeudasPendientes: Number(formData.cantidadDeudasPendientes),
        antiguedadCuenta: Number(formData.antiguedadCuenta),
        ingresosUltimos24Meses: formData.ingresosUltimos24Meses.map(item => Number(item.trim())),
        saldosMensuales: formData.saldosMensuales.map(item => Number(item.trim())),
        depositosUltimos12Meses: formData.depositosUltimos12Meses.map(item => Number(item.trim())),
        retirosUltimos6Meses: formData.retirosUltimos6Meses.map(item => Number(item.trim())),
      };

      console.log("Solicitud de Crédito:", solicitudCredito); // Para depuración

      // Enviar la solicitud al backend
      const response = await solicitudService.solicitarCredito(id, solicitudCredito);
      setSuccess("Crédito solicitado exitosamente.");
      
      // Navegar de vuelta a la lista de usuarios después de un retraso
      setTimeout(() => navigate("/"), 2000);
    } catch (error) {
      console.error("Error al solicitar el crédito:", error);
      if (error.response) {
        // Log detallado del error del backend
        console.error("Datos del Error:", error.response.data);
        setError(`Error: ${error.response.status} - ${error.response.data.error || error.response.data}`);
      } else if (error.request) {
        setError("Error: No se recibió respuesta del servidor.");
      } else {
        setError(`Error: ${error.message}`);
      }
    }
  };

  return (
    <div className="container mt-4">
      <h1>Solicitar Crédito para Usuario ID: {id}</h1>

      {error && <div className="alert alert-danger">{error}</div>}
      {success && <div className="alert alert-success">{success}</div>}

      <form onSubmit={handleSubmit}>
        {/* Información del Préstamo */}
        <h3>Datos del Préstamo</h3>
        <div className="mb-3">
          <label htmlFor="tipo" className="form-label">
            Tipo de Crédito
          </label>
          <select
            className="form-select"
            id="tipo"
            name="tipo"
            value={formData.tipo}
            onChange={handleChange}
            required
          >
            <option value="">Seleccione una opción</option>
            <option value="Primera vivienda">Primera vivienda</option>
            <option value="Segunda vivienda">Segunda vivienda</option>
            <option value="Propiedades comerciales">Propiedades comerciales</option>
            <option value="Remodelacion">Remodelación</option>
          </select>
        </div>
        <div className="mb-3">
          <label htmlFor="plazo" className="form-label">
            Plazo (años)
          </label>
          <input
            type="number"
            className="form-control"
            id="plazo"
            name="plazo"
            value={formData.plazo}
            onChange={handleChange}
            placeholder="Ejemplo: 30"
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="tasaInteres" className="form-label">
            Tasa de Interés (%)
          </label>
          <input
            type="number"
            step="0.01"
            className="form-control"
            id="tasaInteres"
            name="tasaInteres"
            value={formData.tasaInteres}
            onChange={handleChange}
            placeholder="Ejemplo: 4.5"
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="monto" className="form-label">
            Monto del Préstamo
          </label>
          <input
            type="number"
            className="form-control"
            id="monto"
            name="monto"
            value={formData.monto}
            onChange={handleChange}
            placeholder="Ejemplo: 100000000"
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="valorPropiedad" className="form-label">
            Valor de la Propiedad
          </label>
          <input
            type="number"
            className="form-control"
            id="valorPropiedad"
            name="valorPropiedad"
            value={formData.valorPropiedad}
            onChange={handleChange}
            placeholder="Ejemplo: 120000000"
            required
          />
        </div>

        {/* Comprobante de Ingresos */}
        <h3>Comprobante de Ingresos</h3>
        <div className="mb-3">
          <label htmlFor="antiguedadLaboral" className="form-label">
            Antigüedad Laboral (años)
          </label>
          <input
            type="number"
            className="form-control"
            id="antiguedadLaboral"
            name="antiguedadLaboral"
            value={formData.antiguedadLaboral}
            onChange={handleChange}
            placeholder="Ejemplo: 5"
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="ingresoMensual" className="form-label">
            Ingreso Mensual
          </label>
          <input
            type="number"
            className="form-control"
            id="ingresoMensual"
            name="ingresoMensual"
            value={formData.ingresoMensual}
            onChange={handleChange}
            placeholder="Ejemplo: 800000"
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="saldo" className="form-label">
            Saldo
          </label>
          <input
            type="number"
            className="form-control"
            id="saldo"
            name="saldo"
            value={formData.saldo}
            onChange={handleChange}
            placeholder="Ejemplo: 1500000"
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="deudas" className="form-label">
            Deudas
          </label>
          <input
            type="number"
            className="form-control"
            id="deudas"
            name="deudas"
            value={formData.deudas}
            onChange={handleChange}
            placeholder="Ejemplo: 2000000"
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="cantidadDeudasPendientes" className="form-label">
            Cantidad de Deudas Pendientes
          </label>
          <input
            type="number"
            className="form-control"
            id="cantidadDeudasPendientes"
            name="cantidadDeudasPendientes"
            value={formData.cantidadDeudasPendientes}
            onChange={handleChange}
            placeholder="Ejemplo: 3"
            required
          />
        </div>

        {/* Información Financiera Adicional */}
        <h3>Información Financiera Adicional</h3>

        {/* Ingresos Últimos 24 Meses */}
        <div className="mb-4">
          <label className="form-label">
            Ingresos Últimos 24 Meses
          </label>
          <div className="row">
            {formData.ingresosUltimos24Meses.map((value, index) => (
              <div className="col-md-3 mb-2" key={index}>
                <input
                  type="number"
                  className="form-control"
                  placeholder={`Mes ${index + 1}`}
                  value={value}
                  onChange={(e) =>
                    handleArrayChange(
                      "ingresosUltimos24Meses",
                      index,
                      e.target.value
                    )
                  }
                  required
                />
              </div>
            ))}
          </div>
        </div>

        {/* Saldos Mensuales */}
        <div className="mb-4">
          <label className="form-label">
            Saldos Mensuales
          </label>
          <div className="row">
            {formData.saldosMensuales.map((value, index) => (
              <div className="col-md-3 mb-2" key={index}>
                <input
                  type="number"
                  className="form-control"
                  placeholder={`Mes ${index + 1}`}
                  value={value}
                  onChange={(e) =>
                    handleArrayChange("saldosMensuales", index, e.target.value)
                  }
                  required
                />
              </div>
            ))}
          </div>
        </div>

        {/* Depósitos Últimos 12 Meses */}
        <div className="mb-4">
          <label className="form-label">
            Depósitos Últimos 12 Meses
          </label>
          <div className="row">
            {formData.depositosUltimos12Meses.map((value, index) => (
              <div className="col-md-3 mb-2" key={index}>
                <input
                  type="number"
                  className="form-control"
                  placeholder={`Mes ${index + 1}`}
                  value={value}
                  onChange={(e) =>
                    handleArrayChange(
                      "depositosUltimos12Meses",
                      index,
                      e.target.value
                    )
                  }
                  required
                />
              </div>
            ))}
          </div>
        </div>

        {/* Antigüedad de la Cuenta */}
        <div className="mb-3">
          <label htmlFor="antiguedadCuenta" className="form-label">
            Antigüedad de la Cuenta (meses)
          </label>
          <input
            type="number"
            className="form-control"
            id="antiguedadCuenta"
            name="antiguedadCuenta"
            value={formData.antiguedadCuenta}
            onChange={handleChange}
            placeholder="Ejemplo: 10"
            required
          />
        </div>

        {/* Retiros Últimos 6 Meses */}
        <div className="mb-4">
          <label className="form-label">
            Retiros Últimos 6 Meses
          </label>
          <div className="row">
            {formData.retirosUltimos6Meses.map((value, index) => (
              <div className="col-md-4 mb-2" key={index}>
                <input
                  type="number"
                  className="form-control"
                  placeholder={`Mes ${index + 1}`}
                  value={value}
                  onChange={(e) =>
                    handleArrayChange(
                      "retirosUltimos6Meses",
                      index,
                      e.target.value
                    )
                  }
                  required
                />
              </div>
            ))}
          </div>
        </div>

        <button type="submit" className="btn btn-success">
          Enviar Solicitud
        </button>
      </form>
    </div>
  );
}
