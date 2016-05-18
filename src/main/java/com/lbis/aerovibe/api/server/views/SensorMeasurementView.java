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
import com.lbis.aerovibe.spring.common.controllers.SensorMeasurementController;
import com.lbis.aerovibe.spring.common.services.LatestDataService;
import java.util.LinkedList;
import java.util.List;
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
@RequestMapping("/sensorMeasurement")
public class SensorMeasurementView {

    @Autowired
    LatestDataService latestSensorsAndSensorMeasurements;

    @Autowired
    SensorMeasurementController sensorMeasurementContrroller;

    Logger logger = Logger.getLogger(SensorMeasurementView.class);

    @RequestMapping(value = "/latestMeasurements",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    public LinkedList<SensorMeasurement> getLatestMeasurements() {
        try {
            logger.info("Returning latest results.");
            LinkedList<SensorMeasurement> latestSensorMeasurement = latestSensorsAndSensorMeasurements.getLatestMeasurements();
            return latestSensorMeasurement;
        } catch (Throwable th) {
            logger.error("Fail to return latest results.", th);
        }
        return null;
    }

    @RequestMapping(value = "/latestMeasurementsByProvider",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    public LinkedList<SensorMeasurement> getLatestMeasurementsByProvider(@RequestParam(value = SensorMeasurement.PROVIDER) DataProvidorsEnums provider) {
        try {
            logger.info("Returning latest results from provider " + provider.name());
            LinkedList<SensorMeasurement> latestSensorMeasurement = latestSensorsAndSensorMeasurements.getLatestMeasurementsByProvider(provider);
            return latestSensorMeasurement;
        } catch (Throwable th) {
            logger.error("Fail to return latest results.", th);
        }
        return null;
    }

    @RequestMapping(value = "/latestMeasurementsPaged",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    public PartialList<SensorMeasurement> getLatestMeasurementsPaged(@RequestParam(value = SensorMeasurement.PAGE) Integer numberOfPage, HttpServletResponse response) {
        try {
            logger.info("Returning latest results from page " + numberOfPage);
            if (numberOfPage < 0 || numberOfPage > latestSensorsAndSensorMeasurements.getLatestMeasurementsChunked().size()) {
                response.sendError(HttpStatus.SC_NOT_FOUND, "Bad value for page number.");
                return null;
            }
            PartialList<SensorMeasurement> latestSensorMeasurementPaged = latestSensorsAndSensorMeasurements.getLatestMeasurementsChunked().get(numberOfPage);
            return latestSensorMeasurementPaged;
        } catch (Throwable th) {
            logger.error("Fail to return latest results.", th);
        }
        return null;
    }

    @RequestMapping(value = "/getLastMeasurementsForSensor",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    public List<SensorMeasurement> getLastMeasurementsForSensor(@RequestParam(value = Sensor.SENSOR_ID) String sensorId, @RequestParam(value = SensorMeasurement.NO_OF_MEASUREMENTS) Integer noOfMeasurements, HttpServletResponse response) {
        try {
            logger.info("Returning latest results for sensor Id " + sensorId);
            if (noOfMeasurements == null || noOfMeasurements < 0 || sensorId == null || sensorId.isEmpty()) {
                response.sendError(HttpStatus.SC_NOT_FOUND, "Bad value for sensor Id or number of measurements.");
                return null;
            }
            return sensorMeasurementContrroller.getLatestSensorMeasurementForSensorId(sensorId, noOfMeasurements);
        } catch (Throwable th) {
            logger.error("Fail to return latest results for sensor Id " + sensorId , th);
        }
        return null;
    }
}
