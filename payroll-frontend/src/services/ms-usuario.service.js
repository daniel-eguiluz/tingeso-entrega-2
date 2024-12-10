// src/services/ms-usuario.service.js

import httpClient from "../http-common.js";

// Obtener todos los usuarios
const getAllUsuarios = () => {
    return httpClient.get('/usuario');
};

// Obtener un usuario por ID
const getUsuarioById = (id) => {
    return httpClient.get(`/usuario/${id}`);
};

// Guardar un nuevo usuario
const saveUsuario = (data) => {
    return httpClient.post('/usuario', data);
};

// Actualizar un usuario existente
const updateUsuario = (data) => {
    return httpClient.put('/usuario', data);
};

// Eliminar un usuario por ID
const deleteUsuarioById = (id) => {
    return httpClient.delete(`/usuario/${id}`);
};

export default {
    getAllUsuarios,
    getUsuarioById,
    saveUsuario,
    updateUsuario,
    deleteUsuarioById
};
