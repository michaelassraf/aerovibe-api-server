/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lbis.aerovibe.api.server.views;

import com.lbis.aerovibe.enums.DataProvidorsEnums;
import com.lbis.aerovibe.model.PartialList;
import com.lbis.aerovibe.model.Sensor;
import com.lbis.aerovibe.model.SensorMeasurement;
import com.lbis.aerovibe.spring.common.controllers.SensorController;
import com.lbis.aerovibe.spring.common.mapping.GsonEnumBean;
import com.lbis.aerovibe.spring.common.services.LatestDataService;
import java.util.LinkedList;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sensor")
public class SensorView {

    @Autowired
    LatestDataService latestSensorsAndSensorMeasurements;

    @Autowired
    SensorController sensorController;

    Logger logger = Logger.getLogger(SensorView.class);

    @RequestMapping(value = "/activeSensors",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    public LinkedList<Sensor> getActiveSensors() {
        try {
            logger.info("Returning active sensors.");
            LinkedList<Sensor> activeSensors = latestSensorsAndSensorMeasurements.getActiveSensors();
            return activeSensors;
        } catch (Throwable th) {
            logger.error("Fail to return active sensors.", th);
        }
        return null;
    }

    @RequestMapping(value = "/activeSensorsByProvider",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    public LinkedList<Sensor> getActiveSensorsByProvider(@RequestParam(value = SensorMeasurement.PROVIDER) DataProvidorsEnums provider) {
        try {
            logger.info("Returning active sensors from provider " + provider.name());
            LinkedList<Sensor> activeSensors = latestSensorsAndSensorMeasurements.getActiveSensorsByProvider(provider);
            return activeSensors;
        } catch (Throwable th) {
            logger.error("Fail to return active sensors.", th);
        }
        return null;
    }

    @RequestMapping(value = "/activeSensorsPaged",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    public PartialList<Sensor> getActiveSensorsPaged(@RequestParam(value = Sensor.PAGE) Integer numberOfPage, HttpServletResponse response) {
        try {
            logger.info("Returning sensors from page " + numberOfPage);
            if (numberOfPage < 0 || numberOfPage > latestSensorsAndSensorMeasurements.getActiveSensorsChunked().size()) {
                response.sendError(HttpStatus.SC_NOT_FOUND, "Bad value for page number.");
                return null;
            }
            PartialList<Sensor> activeSensorsPaged = latestSensorsAndSensorMeasurements.getActiveSensorsChunked().get(numberOfPage);
            return activeSensorsPaged;
        } catch (Throwable th) {
            logger.error("Fail to return latest results.", th);
        }
        return null;
    }

    @RequestMapping(value = "/getSensorById",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    public Sensor getSensorById(@RequestParam(value = Sensor.SENSOR_ID) String sensorId, HttpServletResponse response) {
        try {
            logger.info("Returning sensor with Id " + sensorId);
            if (sensorId == null || sensorId.isEmpty()) {
                response.sendError(HttpStatus.SC_NOT_FOUND, "Bad value for sensor Id.");
                return null;
            }
            return sensorController.getSensorById(sensorId);
        } catch (Throwable th) {
            logger.error("Fail to return latest results.", th);
        }
        return null;
    }
}
