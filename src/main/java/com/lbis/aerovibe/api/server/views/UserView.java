/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lbis.aerovibe.api.server.views;

import com.lbis.aerovibe.enums.HTTPHeaders;
import com.lbis.aerovibe.enums.UserEnum;
import com.lbis.aerovibe.model.User;
import com.lbis.aerovibe.spring.common.controllers.LocationController;
import com.lbis.aerovibe.spring.common.controllers.UserController;
import com.lbis.aerovibe.api.server.views.model.ViewBaseClass;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.apache.http.HttpStatus;
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
@RequestMapping("/user")
public class UserView extends ViewBaseClass {

    @Autowired
    UserController userController;

    @Autowired
    LocationController locationController;

    Logger logger = Logger.getLogger(UserView.class);

    @RequestMapping(value = "/signup",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON)
    @ResponseBody
    public User signUp(@RequestBody @Valid User user, @RequestHeader(HttpHeaders.USER_AGENT) String userAgent, @ModelAttribute(IP_ADDRESS_ATTRIBUTE_NAME) String clientIpAddress, HttpServletResponse response) {
        try {
            logger.info("Going to sign up user " + user.getUserEmail());
            User signedUpUser = userController.signUp(user);

            if (signedUpUser.getUserId() == null) {
                logger.info("User " + user.getUserEmail() + " already exists, try log in.");
                return signedUpUser;
            }

            addTokenToReponseHeader(userAgent, signedUpUser, response);
            locationController.addLocationToResponseHeader(clientIpAddress, response);
            return signedUpUser;
        } catch (Throwable th) {
            logger.error("Can't add new user. user was " + user.toString(), th);
        }
        return null;
    }

    @RequestMapping(value = "/signupOrLoginFacebookUser",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON)
    @ResponseBody
    public User signUpOrLoginFacebookUser(@RequestBody @Valid User user, @RequestHeader(HttpHeaders.USER_AGENT) String userAgent, @ModelAttribute(IP_ADDRESS_ATTRIBUTE_NAME) String clientIpAddress, HttpServletResponse response) {
        try {
            if (user.getUserType() != UserEnum.UserType.Facebook) {
                logger.info("User type is not facebook");
                return null;
            }
            logger.info("Going to sign up or login FB user " + user.getUserEmail());
            User signedUpUser = userController.signUpOrLoginFBUser(user);
            addTokenToReponseHeader(userAgent, signedUpUser, response);
            locationController.addLocationToResponseHeader(clientIpAddress, response);
            return signedUpUser;
        } catch (Throwable th) {
            logger.error("Can't sign up or login FB user " + user.toString(), th);
        }
        return null;
    }

    @RequestMapping(value = "/login",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON)
    @ResponseBody
    public User login(@RequestBody User user, @RequestHeader(HttpHeaders.USER_AGENT) String userAgent, @ModelAttribute(IP_ADDRESS_ATTRIBUTE_NAME) String clientIpAddress, HttpServletResponse response) {
        try {
            User loggedInUser = userController.login(user);
            if (loggedInUser == null) {
                logger.info("No such user " + user.getUserEmail());
                return loggedInUser;
            }

            if (loggedInUser.getUserPassword() == null) {
                logger.info("Bad password provided for " + user.getUserEmail());
                return loggedInUser;
            }
            addTokenToReponseHeader(userAgent, loggedInUser, response);
            locationController.addLocationToResponseHeader(clientIpAddress, response);
            return loggedInUser;
        } catch (Throwable th) {
            logger.error("Can't login. user was " + user.getUserEmail(), th);
        }
        return null;
    }

    @RequestMapping(value = "/update",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON)
    @ResponseBody
    public User update(@RequestBody User user, @RequestHeader(HTTPHeaders.TokenHeaderValue) String tokenValue, @RequestHeader(HTTPHeaders.UserHeaderValue) String tokenValueUserId, @ModelAttribute(IP_ADDRESS_ATTRIBUTE_NAME) String clientIpAddress, HttpServletResponse response) {
        if (!tokenController.validateToken(tokenValue, tokenValueUserId, response)) {
            return null;
        }
        User mergedUser = userController.updateUser(user, logger);
        if (mergedUser == null) {
            try {
                response.sendError(HttpStatus.SC_BAD_REQUEST, "Can't merge users");
            } catch (Throwable th) {
                logger.error("Can't return bad error code", th);
            }
        }
        locationController.addLocationToResponseHeader(clientIpAddress, response);
        return mergedUser;
    }

}
