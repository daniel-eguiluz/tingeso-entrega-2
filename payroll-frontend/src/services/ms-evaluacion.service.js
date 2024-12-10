// src/services/ms-evaluacion.service.js

import httpClient from "../http-common.js";

const evaluarCredito = (idSolicitud) => {
    return httpClient.get(`/evaluacion/${idSolicitud}`);
};

export default {
    evaluarCredito,
};
