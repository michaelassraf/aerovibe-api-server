/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lbis.aerovibe.api.server.views;

import com.lbis.aerovibe.enums.RequestParams;
import com.lbis.aerovibe.spring.common.couchbase.CouchbaseConnectionPool;
import com.lbis.aerovibe.spring.common.properties.PropertiesFieldNames;
import com.lbis.aerovibe.api.server.views.model.ViewBaseClass;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/file")
public class FileView extends ViewBaseClass {

    Logger logger = Logger.getLogger(FileView.class);

    @Value(PropertiesFieldNames.fileViewPathPrefix)
    String fileViewPathPrefix;

    @Autowired
    CouchbaseConnectionPool couchbaseConnectionPool;

    final String userProfilePrefix = "user_profile_picture_";

    @RequestMapping(value = "/getPhoto", method = RequestMethod.GET, consumes = "*", produces = "image/jpeg")
    @ResponseBody
    public byte[] getPhoto(@RequestParam(value = "id") String id) {
        try {
            return (byte[]) couchbaseConnectionPool.getClient().get(userProfilePrefix + id);
        } catch (Throwable th) {
            logger.error("Can't reture image with id " + id, th);
            return null;
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(@RequestParam(value = RequestParams.FILE_NAME_PARAM) String fileName, @RequestParam(value = RequestParams.FILE_CONTENT_PARAM) MultipartFile file) {
        if (file.isEmpty()) {
            return "File is empty";
        }

        ByteArrayOutputStream baos = null;
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            image = getScaledImage(image, 200, 200);
            baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();
            byte[] bytes = baos.toByteArray();
            couchbaseConnectionPool.getClient().set(userProfilePrefix + fileName, bytes);
            logger.info("Successfully uploaded file " + fileName + " to couchbase " + "user_profile_picture_" + fileName);
            return "File was successfully uploaded";
        } catch (Throwable th) {
            logger.error("Failed to upload file. File was " + fileName, th);
            return "Upload of file failed.";
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (Throwable th) {
                    logger.error("Failed to close stream.", th);
                }
            }
        }
    }

    public BufferedImage getScaledImage(BufferedImage image, int width, int height) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        double scaleX = (double) width / imageWidth;
        double scaleY = (double) height / imageHeight;
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

        return bilinearScaleOp.filter(image,new BufferedImage(width, height, image.getType()));
    }
}
