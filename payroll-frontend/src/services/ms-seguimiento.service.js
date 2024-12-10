// src/services/ms-seguimiento.service.js

import httpClient from "../http-common.js";

const obtenerEstadoPorUsuario = (idUsuario) => {
    return httpClient.get(`/seguimiento/${idUsuario}`);
};

export default {
    obtenerEstadoPorUsuario,
};
