// src/services/ms-simulacion.service.js

import httpClient from "../http-common.js";

const simularCredito = (idUsuario) => {
    return httpClient.get(`/simulacion/${idUsuario}`);
};

export default {
    simularCredito,
};
