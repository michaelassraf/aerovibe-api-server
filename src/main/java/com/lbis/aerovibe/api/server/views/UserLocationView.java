/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lbis.aerovibe.api.server.views;

import com.lbis.aerovibe.model.Sensor;
import com.lbis.aerovibe.model.UserLocation;
import com.lbis.aerovibe.spring.common.services.LatestDataService;
import java.util.LinkedList;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/userLocation")
public class UserLocationView {

    @Autowired
    LatestDataService latestSensorsAndSensorMeasurements;

    Logger logger = Logger.getLogger(UserLocationView.class);

    @RequestMapping(value = "/addUserLocation",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    public LinkedList<Sensor> getActiveSensors(@RequestBody UserLocation userLocation) {
        try {
            logger.info("Going to add user location for user " + userLocation.getUserLocationUserId());
            LinkedList<Sensor> activeSensors = latestSensorsAndSensorMeasurements.getActiveSensors();
            return activeSensors;
        } catch (Throwable th) {
            logger.error("Failed to add user location for user " + userLocation.getUserLocationUserId(), th);
        }
        return null;
    }

}
