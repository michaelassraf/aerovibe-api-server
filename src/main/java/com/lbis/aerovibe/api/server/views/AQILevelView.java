/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lbis.aerovibe.api.server.views;

import com.lbis.aerovibe.enums.AQILevels;
import com.lbis.aerovibe.enums.DataProvidorsEnums;
import com.lbis.aerovibe.spring.common.mapping.GsonEnumBean;
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

/**
 *
 * @author administrator
 */
@Controller
@RequestMapping("/aQILevel")
public class AQILevelView {

    @Autowired
    GsonEnumBean gsonEnumBean;

    Logger logger = Logger.getLogger(this.getClass());

    @RequestMapping(value = "/getDetails",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    public Object getDetails(@RequestParam(value = AQILevels.AQI_LEVEL_ENUM_ID) AQILevels aQILevelEnumId, HttpServletResponse response) {
        try {
            logger.info("Returning data providor enum with Id " + aQILevelEnumId);
            if (aQILevelEnumId == null) {
                response.sendError(HttpStatus.SC_NOT_FOUND, "Bad value for dataProvidorsEnum.");
                return null;
            }
            return gsonEnumBean.getGson().fromJson(gsonEnumBean.getGson().toJson(aQILevelEnumId), Object.class);
        } catch (Throwable th) {
            logger.error("Fail to return latest results.", th);
        }
        return null;
    }
}
