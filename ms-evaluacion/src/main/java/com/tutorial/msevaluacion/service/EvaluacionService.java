package com.tutorial.msevaluacion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EvaluacionService {

    private static final Logger logger = LoggerFactory.getLogger(EvaluacionService.class);

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Método principal para evaluar el crédito basado en el ID de la solicitud.
     *
     * @param idSolicitud El ID de la solicitud a evaluar.
     * @return Un mapa con los resultados de la evaluación.
     * @throws Exception Si ocurre algún error durante la evaluación.
     */
    public Map<String, Object> evaluarCredito(int idSolicitud) throws Exception {
        logger.info("Iniciando evaluación de crédito para la solicitud ID: {}", idSolicitud);

        // Llamada al microservicio ms-solicitud para obtener la solicitud
        Map<String, Object> solicitud;
        try {
            solicitud = restTemplate.getForObject("http://ms-solicitud/solicitud/" + idSolicitud, Map.class);
            if (solicitud == null) {
                throw new Exception("Solicitud no encontrada");
            }
            logger.debug("Datos de solicitud: {}", solicitud);
        } catch (HttpClientErrorException.NotFound e) {
            logger.error("Solicitud con ID {} no encontrada en ms-solicitud", idSolicitud);
            throw new Exception("Solicitud no encontrada");
        } catch (Exception e) {
            logger.error("Error al obtener la solicitud: {}", e.getMessage());
            throw new Exception("Error al obtener la solicitud");
        }

        // Extraer IDs necesarios de la solicitud
        Integer idUsuarioObj = getIntegerFromMap(solicitud, "idUsuario");
        Integer idPrestamoObj = getIntegerFromMap(solicitud, "idPrestamo");

        if (idUsuarioObj == null || idPrestamoObj == null) {
            throw new Exception("Faltan campos 'idUsuario' o 'idPrestamo' en la solicitud");
        }

        int idUsuario = idUsuarioObj.intValue();
        int idPrestamo = idPrestamoObj.intValue();

        logger.info("ID de Usuario: {}, ID de Préstamo: {}", idUsuario, idPrestamo);

        // Llamada al microservicio ms-solicitud para obtener el comprobante de ingresos utilizando idUsuario
        Map<String, Object> comprobanteIngresos;
        try {
            comprobanteIngresos = restTemplate.getForObject("http://ms-solicitud/solicitud/comprobante/" + idUsuario, Map.class);
            if (comprobanteIngresos == null) {
                throw new Exception("Comprobante de ingresos no encontrado");
            }
            logger.debug("Datos de comprobante de ingresos: {}", comprobanteIngresos);
        } catch (HttpClientErrorException.NotFound e) {
            logger.error("Comprobante de ingresos para idUsuario {} no encontrado en ms-solicitud", idUsuario);
            throw new Exception("Comprobante de ingresos no encontrado");
        } catch (Exception e) {
            logger.error("Error al obtener el comprobante de ingresos: {}", e.getMessage());
            throw new Exception("Error al obtener el comprobante de ingresos");
        }

        // Llamada al microservicio ms-usuario para obtener el usuario
        Map<String, Object> usuario;
        try {
            usuario = restTemplate.getForObject("http://ms-usuario/usuario/" + idUsuario, Map.class);
            if (usuario == null) {
                throw new Exception("Usuario no encontrado");
            }
            logger.debug("Datos del usuario: {}", usuario);
        } catch (HttpClientErrorException.NotFound e) {
            logger.error("Usuario con ID {} no encontrado en ms-usuario", idUsuario);
            throw new Exception("Usuario no encontrado");
        } catch (Exception e) {
            logger.error("Error al obtener el usuario: {}", e.getMessage());
            throw new Exception("Error al obtener el usuario");
        }

        logger.info("Datos obtenidos correctamente. Iniciando evaluación de reglas...");

        // Evaluar las reglas R1-R6
        boolean r1 = evaluarRelacionCuotaIngreso(solicitud, comprobanteIngresos);
        boolean r2 = evaluarHistorialCrediticio(comprobanteIngresos);
        boolean r3 = evaluarAntiguedad(usuario, comprobanteIngresos);
        boolean r4 = evaluarRelacionDeudaIngreso(solicitud, comprobanteIngresos);
        boolean r5 = evaluarMontoMaximoFinanciamiento(solicitud);
        boolean r6 = evaluarEdad(usuario, solicitud);

        // Crear un mapa con las reglas R1-R6 y sus estados
        Map<String, Boolean> reglasCumplidas = new HashMap<>();
        reglasCumplidas.put("Relación Cuota/Ingreso Aprobada", r1);
        reglasCumplidas.put("Historial Crediticio del Cliente Aprobado", r2);
        reglasCumplidas.put("Antigüedad Laboral y Estabilidad Aprobada", r3);
        reglasCumplidas.put("Relación Deuda/Ingreso Aprobada", r4);
        reglasCumplidas.put("Monto Máximo de Financiamiento Aprobado", r5);
        reglasCumplidas.put("Edad del Solicitante Aprobada", r6);

        boolean aprobado = r1 && r2 && r3 && r4 && r5 && r6;

        logger.info("Resultado de reglas R1-R6: {}", aprobado ? "Aprobado" : "Rechazado");

        // Evaluación de R7: Capacidad de Ahorro
        String capacidadAhorro = "insuficiente";
        Map<String, Object> detallesAhorro = new HashMap<>();
        if (aprobado) {
            Map<String, Object> evaluacionAhorro = evaluarCapacidadAhorro(solicitud, comprobanteIngresos);
            capacidadAhorro = (String) evaluacionAhorro.get("capacidadAhorro");
            int reglasAhorroCumplidas = (int) evaluacionAhorro.get("reglasCumplidas");

            // Evaluar sub-reglas R71-R75 para detalles
            boolean rr71 = evaluarR71(solicitud, comprobanteIngresos);
            boolean rr72 = evaluarR72(comprobanteIngresos);
            boolean rr73 = evaluarR73(comprobanteIngresos);
            boolean rr74 = evaluarR74(solicitud, comprobanteIngresos);
            boolean rr75 = evaluarR75(solicitud, comprobanteIngresos); // Corregido

            detallesAhorro.put("R71", rr71);
            detallesAhorro.put("R72", rr72);
            detallesAhorro.put("R73", rr73);
            detallesAhorro.put("R74", rr74);
            detallesAhorro.put("R75", rr75);

            // Si capacidad de ahorro es "insuficiente", se rechaza
            if ("insuficiente".equals(capacidadAhorro)) {
                aprobado = false;
                logger.info("Capacidad de ahorro insuficiente. Rechazando solicitud.");
            } else {
                logger.info("Capacidad de ahorro aprobada: {}", capacidadAhorro);
            }

            // Agregamos "Capacidad de Ahorro Aprobada" como una regla global
            boolean capacidadAhorroAprobada = !"insuficiente".equals(capacidadAhorro);
            reglasCumplidas.put("Capacidad de Ahorro Aprobada", capacidadAhorroAprobada);
        } else {
            // Si ya estaba rechazado, igualmente evaluamos las subreglas de R7 para tener detalles
            boolean rr71 = evaluarR71(solicitud, comprobanteIngresos);
            boolean rr72 = evaluarR72(comprobanteIngresos);
            boolean rr73 = evaluarR73(comprobanteIngresos);
            boolean rr74 = evaluarR74(solicitud, comprobanteIngresos);
            boolean rr75 = evaluarR75(solicitud, comprobanteIngresos); // Corregido

            detallesAhorro.put("R71", rr71);
            detallesAhorro.put("R72", rr72);
            detallesAhorro.put("R73", rr73);
            detallesAhorro.put("R74", rr74);
            detallesAhorro.put("R75", rr75);

            // Determinar capacidadAhorro sin afectar la lógica original si ya se rechazó antes
            Map<String, Object> evaluacionAhorroTmp = evaluarCapacidadAhorro(solicitud, comprobanteIngresos);
            capacidadAhorro = (String) evaluacionAhorroTmp.get("capacidadAhorro");

            // Capacidad de Ahorro Aprobada depende de que no sea "insuficiente"
            boolean capacidadAhorroAprobada = !"insuficiente".equals(capacidadAhorro);
            reglasCumplidas.put("Capacidad de Ahorro Aprobada", capacidadAhorroAprobada);
        }

        // Construir el resultado final
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("capacidadAhorro", capacidadAhorro);
        resultado.put("detallesAhorro", detallesAhorro);
        resultado.put("aprobado", aprobado);
        resultado.put("reglasCumplidas", reglasCumplidas);

        logger.info("Evaluación completada para la solicitud ID: {}", idSolicitud);
        return resultado;
    }

    // Métodos auxiliares

    /**
     * Obtener un Integer de un Map con seguridad.
     */
    private Integer getIntegerFromMap(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        logger.warn("Clave '{}' no encontrada o no es un Number en el mapa: {}", key, map);
        return null;
    }

    /**
     * Obtener un Double de un Map con seguridad.
     */
    private Double getDoubleFromMap(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        logger.warn("Clave '{}' no encontrada o no es un Number en el mapa: {}", key, map);
        return null;
    }

    // Implementación de las reglas R1-R6...

    /**
     * R1: Relación Cuota/Ingreso <= 35%
     */
    private boolean evaluarRelacionCuotaIngreso(Map<String, Object> solicitud, Map<String, Object> comprobanteIngresos) throws Exception {
        Double tasaInteresAnual = getDoubleFromMap(solicitud, "tasaInteresAnual");
        Integer plazo = getIntegerFromMap(solicitud, "plazo");
        Integer monto = getIntegerFromMap(solicitud, "monto");
        Integer ingresoMensual = getIntegerFromMap(comprobanteIngresos, "ingresoMensual");

        if (tasaInteresAnual == null || plazo == null || monto == null || ingresoMensual == null) {
            throw new Exception("Faltan campos necesarios para evaluar R1");
        }

        double tasaMensual = (tasaInteresAnual / 100.0) / 12.0;
        int numeroPagos = plazo * 12;
        double cuota = (monto * tasaMensual * Math.pow(1 + tasaMensual, numeroPagos)) /
                (Math.pow(1 + tasaMensual, numeroPagos) - 1);

        double relacion = (cuota / ingresoMensual) * 100;
        return relacion <= 35;
    }

    /**
     * R2: Historial crediticio (no muchas deudas ni monto excesivo)
     */
    private boolean evaluarHistorialCrediticio(Map<String, Object> comprobanteIngresos) throws Exception {
        Integer deudas = getIntegerFromMap(comprobanteIngresos, "deudas");
        Integer ingresoMensual = getIntegerFromMap(comprobanteIngresos, "ingresoMensual");
        Integer cantidadDeudasPendientes = getIntegerFromMap(comprobanteIngresos, "cantidadDeudasPendientes");

        if (deudas == null || ingresoMensual == null || cantidadDeudasPendientes == null) {
            throw new Exception("Faltan campos necesarios para evaluar R2");
        }

        double porcentajeDeudas = (deudas / (double) ingresoMensual) * 100;
        if (cantidadDeudasPendientes > 3 || porcentajeDeudas > 30) {
            return false;
        }
        return true;
    }

    /**
     * R3: Antiguedad Laboral y Estabilidad
     */
    private boolean evaluarAntiguedad(Map<String, Object> usuario, Map<String, Object> comprobanteIngresos) throws Exception {
        String tipoEmpleado = (String) usuario.get("tipoEmpleado");
        Integer antiguedadLaboral = getIntegerFromMap(comprobanteIngresos, "antiguedadLaboral");
        String ingresos24Str = (String) comprobanteIngresos.get("ingresosUltimos24Meses");

        if (tipoEmpleado == null || antiguedadLaboral == null || ingresos24Str == null) {
            throw new Exception("Faltan campos necesarios para evaluar R3");
        }

        String[] ingresosArray = ingresos24Str.split(",");

        if ("Empleado".equalsIgnoreCase(tipoEmpleado)) {
            return antiguedadLaboral >= 1;
        } else if ("Independiente".equalsIgnoreCase(tipoEmpleado)) {
            return ingresosArray.length >= 24; // Tiene datos de 24 meses
        } else {
            return false;
        }
    }

    /**
     * R4: Relación Deuda/Ingreso <= 50%
     */
    private boolean evaluarRelacionDeudaIngreso(Map<String, Object> solicitud, Map<String, Object> comprobanteIngresos) throws Exception {
        Integer ingresoMensual = getIntegerFromMap(comprobanteIngresos, "ingresoMensual");
        Integer deudasActuales = getIntegerFromMap(comprobanteIngresos, "deudas");

        Double tasaInteresAnual = getDoubleFromMap(solicitud, "tasaInteresAnual");
        Integer plazo = getIntegerFromMap(solicitud, "plazo");
        Integer monto = getIntegerFromMap(solicitud, "monto");

        if (ingresoMensual == null || deudasActuales == null || tasaInteresAnual == null || plazo == null || monto == null) {
            throw new Exception("Faltan campos necesarios para evaluar R4");
        }

        double tasaMensual = (tasaInteresAnual / 100.0) / 12.0;
        int numeroPagos = plazo * 12;
        double cuotaNueva = (monto * tasaMensual * Math.pow(1 + tasaMensual, numeroPagos)) /
                (Math.pow(1 + tasaMensual, numeroPagos) - 1);

        double totalDeudas = deudasActuales + cuotaNueva;
        double relacion = (totalDeudas / ingresoMensual) * 100;
        return relacion <= 50;
    }

    /**
     * R5: Monto Máximo de Financiamiento según tipo
     */
    private boolean evaluarMontoMaximoFinanciamiento(Map<String, Object> solicitud) throws Exception {
        String tipo = (String) solicitud.get("tipoPrestamo");
        Integer valorPropiedad = getIntegerFromMap(solicitud, "valorPropiedad");
        Integer monto = getIntegerFromMap(solicitud, "monto");

        if (tipo == null || valorPropiedad == null || monto == null) {
            throw new Exception("Faltan campos necesarios para evaluar R5");
        }

        double porcentajeMaximo;
        switch (tipo.toLowerCase()) {
            case "primera vivienda":
                porcentajeMaximo = 0.80;
                break;
            case "segunda vivienda":
                porcentajeMaximo = 0.70;
                break;
            case "propiedades comerciales":
                porcentajeMaximo = 0.60;
                break;
            case "remodelacion":
                porcentajeMaximo = 0.50;
                break;
            default:
                porcentajeMaximo = 0.0;
                break;
        }

        double maximo = valorPropiedad * porcentajeMaximo;
        return monto <= maximo;
    }

    /**
     * R6: Edad del solicitante
     */
    private boolean evaluarEdad(Map<String, Object> usuario, Map<String, Object> solicitud) throws Exception {
        Integer edad = getIntegerFromMap(usuario, "edad");
        Integer plazo = getIntegerFromMap(solicitud, "plazo");

        if (edad == null || plazo == null) {
            throw new Exception("Faltan campos necesarios para evaluar R6");
        }

        int edadFinal = edad + plazo;
        return edadFinal < 70;
    }

    // R7: Capacidad de Ahorro (R71-R75)

    /**
     * Evaluación de la Capacidad de Ahorro basada en las reglas R71-R75.
     *
     * @param solicitud           Información de la solicitud.
     * @param comprobanteIngresos Información del comprobante de ingresos.
     * @return Un mapa con la capacidad de ahorro y el número de reglas cumplidas.
     */
    private Map<String, Object> evaluarCapacidadAhorro(Map<String, Object> solicitud, Map<String, Object> comprobanteIngresos) throws Exception {
        Map<String, Object> resultado = new HashMap<>();
        int reglasCumplidas = 0;

        if (evaluarR71(solicitud, comprobanteIngresos)) reglasCumplidas++;
        if (evaluarR72(comprobanteIngresos)) reglasCumplidas++;
        if (evaluarR73(comprobanteIngresos)) reglasCumplidas++;
        if (evaluarR74(solicitud, comprobanteIngresos)) reglasCumplidas++;
        if (evaluarR75(solicitud, comprobanteIngresos)) reglasCumplidas++; // Corregido

        String capacidad;
        if (reglasCumplidas == 5) capacidad = "sólida";
        else if (reglasCumplidas >= 3) capacidad = "moderada";
        else capacidad = "insuficiente";

        resultado.put("capacidadAhorro", capacidad);
        resultado.put("reglasCumplidas", reglasCumplidas);
        return resultado;
    }

    // Implementación de las sub-reglas R71-R75...

    /**
     * R71: Saldo Mínimo >= 10% del monto
     */
    private boolean evaluarR71(Map<String, Object> solicitud, Map<String, Object> comprobanteIngresos) throws Exception {
        Integer saldo = getIntegerFromMap(comprobanteIngresos, "saldo");
        Integer monto = getIntegerFromMap(solicitud, "monto");
        if (saldo == null || monto == null) {
            throw new Exception("Faltan campos necesarios para evaluar R71");
        }
        double requerido = monto * 0.10;
        return saldo >= requerido;
    }

    /**
     * R72: Historial Ahorro Consistente (saldo positivo 12 meses)
     */
    private boolean evaluarR72(Map<String, Object> comprobanteIngresos) throws Exception {
        String saldosStr = (String) comprobanteIngresos.get("saldosMensuales");
        if (saldosStr == null) {
            throw new Exception("Faltan campos necesarios para evaluar R72");
        }
        String[] saldosArray = saldosStr.split(",");
        if (saldosArray.length < 12) return false;
        for (String s : saldosArray) {
            double saldo = Double.parseDouble(s.trim());
            if (saldo <= 0) return false;
        }
        return true;
    }

    /**
     * R73: Depósitos Periódicos (5% ingreso mensual)
     */
    private boolean evaluarR73(Map<String, Object> comprobanteIngresos) throws Exception {
        Integer ingresoMensual = getIntegerFromMap(comprobanteIngresos, "ingresoMensual");
        if (ingresoMensual == null) {
            throw new Exception("Faltan campos necesarios para evaluar R73");
        }
        double minDeposito = ingresoMensual * 0.05;

        String depsStr = (String) comprobanteIngresos.get("depositosUltimos12Meses");
        if (depsStr == null) {
            throw new Exception("Faltan campos necesarios para evaluar R73");
        }
        String[] depsArray = depsStr.split(",");
        if (depsArray.length < 12) return false;

        // Chequear si al menos hace depósitos mensuales >= minDeposito
        boolean todosDepositosMensuales = true;
        for (String d : depsArray) {
            double dep = Double.parseDouble(d.trim());
            if (dep < minDeposito) {
                todosDepositosMensuales = false;
                break;
            }
        }
        if (todosDepositosMensuales) return true;

        // Chequear trimestral
        int countTrimestral = 0;
        for (int i = 0; i < depsArray.length; i += 3) {
            double suma = 0;
            for (int j = 0; j < 3 && (i + j) < depsArray.length; j++) {
                suma += Double.parseDouble(depsArray[i + j].trim());
            }
            if (suma >= minDeposito * 3) countTrimestral++;
        }

        return countTrimestral >= 4;
    }

    /**
     * R74: Relación Saldo/Antigüedad
     */
    private boolean evaluarR74(Map<String, Object> solicitud, Map<String, Object> comprobanteIngresos) throws Exception {
        Integer antiguedadCuenta = getIntegerFromMap(comprobanteIngresos, "antiguedadCuenta");
        Integer saldo = getIntegerFromMap(comprobanteIngresos, "saldo");
        Integer monto = getIntegerFromMap(solicitud, "monto");

        if (antiguedadCuenta == null || saldo == null || monto == null) {
            throw new Exception("Faltan campos necesarios para evaluar R74");
        }

        double requerido = (antiguedadCuenta < 2) ? monto * 0.20 : monto * 0.10;
        return saldo >= requerido;
    }

    /**
     * R75: Retiros Recientes (no >30% en últimos 6 meses)
     */
    private boolean evaluarR75(Map<String, Object> solicitud, Map<String, Object> comprobanteIngresos) throws Exception {
        String retirosStr = (String) comprobanteIngresos.get("retirosUltimos6Meses");
        String saldosStr = (String) comprobanteIngresos.get("saldosMensuales");
        if (retirosStr == null || saldosStr == null) {
            throw new Exception("Faltan campos necesarios para evaluar R75");
        }
        String[] retirosArray = retirosStr.split(",");
        String[] saldosArray = saldosStr.split(",");
        if (retirosArray.length < 6 || saldosArray.length < 6) return false;

        // Tomar últimos 6 saldos
        double[] ultSaldos = new double[6];
        for (int i = 0; i < 6; i++) {
            ultSaldos[i] = Double.parseDouble(saldosArray[saldosArray.length - 6 + i].trim());
        }

        double[] ultRetiros = new double[6];
        for (int i = 0; i < 6; i++) {
            ultRetiros[i] = Double.parseDouble(retirosArray[retirosArray.length - 6 + i].trim()); // Corregido
        }

        for (int i = 0; i < 6; i++) {
            double saldo = ultSaldos[i];
            double retiro = ultRetiros[i];
            if (saldo > 0) {
                double porc = (retiro / saldo) * 100;
                if (porc > 30) return false;
            }
        }
        return true;
    }
}
