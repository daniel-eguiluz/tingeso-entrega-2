// src/components/AddUsuario.jsx

import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import usuarioService from '../services/ms-usuario.service.js'; // Importación por defecto

const AddUsuario = () => {
    const navigate = useNavigate();

    const [usuario, setUsuario] = useState({
        rut: '',
        nombre: '',
        apellido: '',
        edad: '',
        tipoEmpleado: 'Empleado' // Valor por defecto
    });

    const [errors, setErrors] = useState({});

    const onChange = (e) => {
        setUsuario({ ...usuario, [e.target.name]: e.target.value });
    };

    const validateForm = () => {
        const newErrors = {};

        // Validación del RUT (Formato básico: 12.345.678-9)
        if (!usuario.rut.trim()) {
            newErrors.rut = 'El RUT es obligatorio.';
        } else if (!/^(\d{1,3}\.?\d{3}\.?\d{3}-[\dkK])$/.test(usuario.rut)) {
            newErrors.rut = 'Formato de RUT inválido. Ejemplo: 12.345.678-9';
        }

        // Validación del Nombre
        if (!usuario.nombre.trim()) {
            newErrors.nombre = 'El nombre es obligatorio.';
        }

        // Validación del Apellido
        if (!usuario.apellido.trim()) {
            newErrors.apellido = 'El apellido es obligatorio.';
        }

        // Validación de la Edad
        if (!usuario.edad) {
            newErrors.edad = 'La edad es obligatoria.';
        } else if (isNaN(usuario.edad) || parseInt(usuario.edad, 10) <= 0) {
            newErrors.edad = 'La edad debe ser un número positivo.';
        }

        // Validación del Tipo de Empleado
        if (!usuario.tipoEmpleado) {
            newErrors.tipoEmpleado = 'El tipo de empleado es obligatorio.';
        }

        setErrors(newErrors);

        // Retornar verdadero si no hay errores
        return Object.keys(newErrors).length === 0;
    };

    const onSubmit = async (e) => {
        e.preventDefault();

        if (!validateForm()) {
            return;
        }

        const nuevoUsuario = {
            rut: usuario.rut,
            nombre: usuario.nombre,
            apellido: usuario.apellido,
            edad: parseInt(usuario.edad, 10),
            tipoEmpleado: usuario.tipoEmpleado
        };

        try {
            await usuarioService.saveUsuario(nuevoUsuario); // Usar el método del objeto
            alert('Usuario agregado exitosamente.');
            navigate('/'); // Redirigir al listado de usuarios
        } catch (error) {
            console.error('Error al agregar usuario:', error);
            alert('Error al agregar usuario. Por favor, inténtelo nuevamente.');
        }
    };

    return (
        <div className="container mt-5">
            <h2 className="mb-4">Agregar Nuevo Usuario</h2>
            <form onSubmit={onSubmit} noValidate>
                {/* Campo RUT */}
                <div className="mb-3">
                    <label htmlFor="rut" className="form-label">RUT:</label>
                    <input
                        type="text"
                        id="rut"
                        name="rut"
                        value={usuario.rut}
                        onChange={onChange}
                        placeholder="12.345.678-9"
                        required
                        className={`form-control ${errors.rut ? 'is-invalid' : ''}`}
                    />
                    {errors.rut && <div className="invalid-feedback">{errors.rut}</div>}
                </div>

                {/* Campo Nombre */}
                <div className="mb-3">
                    <label htmlFor="nombre" className="form-label">Nombre:</label>
                    <input
                        type="text"
                        id="nombre"
                        name="nombre"
                        value={usuario.nombre}
                        onChange={onChange}
                        placeholder="Juan"
                        required
                        className={`form-control ${errors.nombre ? 'is-invalid' : ''}`}
                    />
                    {errors.nombre && <div className="invalid-feedback">{errors.nombre}</div>}
                </div>

                {/* Campo Apellido */}
                <div className="mb-3">
                    <label htmlFor="apellido" className="form-label">Apellido:</label>
                    <input
                        type="text"
                        id="apellido"
                        name="apellido"
                        value={usuario.apellido}
                        onChange={onChange}
                        placeholder="Pérez"
                        required
                        className={`form-control ${errors.apellido ? 'is-invalid' : ''}`}
                    />
                    {errors.apellido && <div className="invalid-feedback">{errors.apellido}</div>}
                </div>

                {/* Campo Edad */}
                <div className="mb-3">
                    <label htmlFor="edad" className="form-label">Edad:</label>
                    <input
                        type="number"
                        id="edad"
                        name="edad"
                        value={usuario.edad}
                        onChange={onChange}
                        placeholder="35"
                        required
                        min="1"
                        className={`form-control ${errors.edad ? 'is-invalid' : ''}`}
                    />
                    {errors.edad && <div className="invalid-feedback">{errors.edad}</div>}
                </div>

                {/* Campo Tipo de Empleado */}
                <div className="mb-3">
                    <label htmlFor="tipoEmpleado" className="form-label">Tipo de Empleado:</label>
                    <select
                        id="tipoEmpleado"
                        name="tipoEmpleado"
                        value={usuario.tipoEmpleado}
                        onChange={onChange}
                        required
                        className={`form-select ${errors.tipoEmpleado ? 'is-invalid' : ''}`}
                    >
                        <option value="">Seleccione</option>
                        <option value="Empleado">Empleado</option>
                        <option value="Independiente">Independiente</option>
                    </select>
                    {errors.tipoEmpleado && <div className="invalid-feedback">{errors.tipoEmpleado}</div>}
                </div>

                {/* Botón de Guardar */}
                <button type="submit" className="btn btn-primary">Guardar</button>
            </form>
        </div>
    );
};

export default AddUsuario;
