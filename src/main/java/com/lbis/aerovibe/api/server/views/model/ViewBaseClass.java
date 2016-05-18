/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lbis.aerovibe.api.server.views.model;

import com.lbis.aerovibe.enums.HTTPHeaders;
import com.lbis.aerovibe.model.Token;
import com.lbis.aerovibe.model.User;
import com.lbis.aerovibe.spring.common.controllers.TokenController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 *
 * @author Development User
 */
public abstract class ViewBaseClass {

    protected final static String IP_ADDRESS_ATTRIBUTE_NAME = "IP_ADDRESS_ATTRIBUTE_NAME";

    protected final static String X_FORWARDED_FOR = "X-FORWARDED-FOR";

    @Autowired
    protected TokenController tokenController;

    protected void addTokenToReponseHeader(String userAgent, User signedUpUser, HttpServletResponse response) {
        Token token = tokenController.generateAndStoreNewToken(userAgent, signedUpUser);
        response.addHeader(HTTPHeaders.TokenHeader.gethTTPHeaderValue(), token.getTokenValue());
        response.addHeader(HTTPHeaders.UserHeader.gethTTPHeaderValue(), signedUpUser.getUserId());
    }

    @ModelAttribute(IP_ADDRESS_ATTRIBUTE_NAME)
    public String populateClientIpAddress(
            HttpServletRequest request) {
        String ipAddress = request.getHeader(X_FORWARDED_FOR);
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
