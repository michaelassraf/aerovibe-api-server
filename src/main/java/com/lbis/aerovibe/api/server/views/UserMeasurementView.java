/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lbis.aerovibe.api.server.views;

import com.couchbase.client.protocol.views.ViewRow;
import com.lbis.aerovibe.enums.HTTPHeaders;
import com.lbis.aerovibe.model.UserLocation;
import com.lbis.aerovibe.model.UserMeasurement;
import com.lbis.aerovibe.spring.common.controllers.TokenController;
import com.lbis.aerovibe.spring.common.controllers.UserMeasurementController;
import com.lbis.aerovibe.api.server.views.model.ViewBaseClass;
import com.lbis.aerovibe.model.Sensor;
import java.util.List;
import java.util.concurrent.Callable;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/userMeasurement")
public class UserMeasurementView extends ViewBaseClass {

    @Autowired
    UserMeasurementController userMeasurementController;

    Logger logger = Logger.getLogger(UserMeasurementView.class);

    @RequestMapping(value = "/addNewUserMeasurement",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON)
    @ResponseBody
    public UserLocation addNewUserMeasurement(@RequestBody UserLocation userLocation, @ModelAttribute(IP_ADDRESS_ATTRIBUTE_NAME) String clientIpAddress, @RequestHeader(HTTPHeaders.TokenHeaderValue) String tokenValue, @RequestHeader(HTTPHeaders.UserHeaderValue) String tokenValueUserId, HttpServletResponse response) {
        if (!tokenController.validateToken(tokenValue, tokenValueUserId, response)) {
            return null;
        }
        userLocation = userMeasurementController.addNewUserMeasurement(userLocation, clientIpAddress, logger);
        tokenController.addTokenToReponseHeader(tokenValue, tokenValueUserId, response);
        return userLocation;
    }

    @RequestMapping(value = "/getLastWeekUserMeasurements",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    public List<UserMeasurement> getLastWeekUserMeasurements(@RequestBody UserMeasurement userMeasurement, @RequestHeader(HTTPHeaders.TokenHeaderValue) String tokenValue, @RequestHeader(HTTPHeaders.UserHeaderValue) String tokenValueUserId, HttpServletResponse response) {

        if (!tokenController.validateToken(tokenValue, tokenValueUserId, response)) {
            return null;
        }

        Long now = System.currentTimeMillis();
        Long lastWeek = now - (7 * 24 * 60 * 60 * 1000);
        List<UserMeasurement> list = userMeasurementController.getUserMeasurementsForUser(userMeasurement.getUserMeasurementUserLocation().getObjectKey(), lastWeek, now, logger);

        tokenController.addTokenToReponseHeader(tokenValue, tokenValueUserId, response);
        return list;
    }

}
