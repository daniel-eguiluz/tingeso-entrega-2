// src/services/ms-costos.service.js

import httpClient from "../http-common.js";

const calcularCostos = (idUsuario) => {
    return httpClient.get(`/costos/${idUsuario}`);
};

export default {
    calcularCostos,
};
