///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.lbis.aerovibe.tests;
//
//import com.lbis.aerovibe.model.UserLocation;
//import com.lbis.aerovibeserver.location.geoip.GetLocationFromGeoIP;
//import com.lbis.aerovibeserver.location.geoip2.GetLocationFromGeoIP2;
//import com.lbis.aerovibeserver.schedule.tasks.impl.GetSensorsFromAirQualityEgg;
//import com.lbis.aerovibeserver.schedule.tasks.impl.GetSensorsFromBeijingAirPollution;
//import com.lbis.aerovibeserver.schedule.tasks.impl.aqe.model.AQEResult;
//import com.lbis.aerovibeserver.services.AQICNScrapingService;
//import java.io.File;
//import java.util.ArrayList;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
///**
// *
// * @author Development User
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
//public class WSTests {
//
//    @Autowired
//    GetLocationFromGeoIP getLocationFromIP;
//
//    @Autowired
//    GetLocationFromGeoIP2 getLocationFromIP2;
//
//    @Autowired
//    GetSensorsFromBeijingAirPollution getSensorsFromBeijingAirPollution;
//
//    @Autowired
//    AQICNScrapingService aQICNScrapingService;
//
//    @Autowired
//    GetSensorsFromAirQualityEgg getSensorsFromAirQualityEgg;
//
//    @Test
//    public void getUserLocation() {
//        UserLocation ss = getLocationFromIP.getUserLocationForIP("user_michaelassraf@gmail.com", "79.176.161.99");
//        System.err.println(ss.toString());
//    }
//
//    @Test
//    public void getUserLocationFromGeoIP2() {
//        UserLocation ss = getLocationFromIP2.getUserLocationForIP("user_michaelassraf@gmail.com", "79.176.161.99");
//        System.err.println(ss.toString());
//    }
//
//    @Test
//    public void getUserMeasurments() {
//        UserLocation ss = getLocationFromIP.getUserLocationForIP("user_michaelassraf@gmail.com", "109.64.29.144");
//        System.err.println(ss.toString());
//    }
//
//    @Test
//    public void getSensorsFromBeijingAirPollution() {
//        getSensorsFromBeijingAirPollution.getSensorsMeasurements();
//    }
//
//    @Test
//    public void getAllAQICNFiles() {
//        File[] files = aQICNScrapingService.getAQICNArrayFilesFromWeb();
//
//        for (File file : files) {
//            System.err.println(file.getAbsolutePath());
//        }
//    }
//
//    @Test
//    public void getSensorsFromAQE() {
//        Long start = System.currentTimeMillis();
//        ArrayList<AQEResult> aQEResults = getSensorsFromAirQualityEgg.giveMeAllTheFuckingStations();
//        System.err.println("Run time is " + String.valueOf(System.currentTimeMillis() - start) + " mili seconds ");
//        for (AQEResult aQEResult : aQEResults) {
//            System.err.println(aQEResult.getDescription());
//        }
//    }
//}
