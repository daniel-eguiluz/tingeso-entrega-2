// src/services/ms-solicitud.service.js

import httpClient from "../http-common.js";

// CRUD de Solicitudes
const getAllSolicitudes = () => {
    return httpClient.get('/solicitud/');
};

const getSolicitudById = (id) => {
    return httpClient.get(`/solicitud/${id}`);
};

const getSolicitudByUsuarioId = (idUsuario) => {
    return httpClient.get(`/solicitud/usuario/${idUsuario}`);
};

const saveSolicitud = (data) => {
    return httpClient.post('/solicitud/', data);
};

const updateSolicitud = (data) => {
    return httpClient.put('/solicitud/', data);
};

const deleteSolicitudById = (id) => {
    return httpClient.delete(`/solicitud/${id}`);
};

// Solicitar Crédito
const solicitarCredito = (idUsuario, data) => {
    return httpClient.post(`/solicitud/${idUsuario}`, data);
};

// Obtener Prestamo por ID
const getPrestamo = (idPrestamo) => {
    return httpClient.get(`/solicitud/prestamo/${idPrestamo}`);
};

// Obtener Comprobante por Usuario
const getComprobanteByUsuario = (idUsuario) => {
    return httpClient.get(`/solicitud/comprobante/${idUsuario}`);
};

export default {
    getAllSolicitudes,
    getSolicitudById,
    getSolicitudByUsuarioId,
    saveSolicitud,
    updateSolicitud,
    deleteSolicitudById,
    solicitarCredito,
    getPrestamo,
    getComprobanteByUsuario
};
