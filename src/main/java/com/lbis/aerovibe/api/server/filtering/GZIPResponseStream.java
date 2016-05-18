/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lbis.aerovibe.api.server.filtering;

import com.lbis.aerovibe.enums.HTTPHeaders;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpHeaders;
import org.apache.log4j.Logger;

/**
 *
 * @author Development User
 */
public class GZIPResponseStream extends ServletOutputStream {

    protected ByteArrayOutputStream byteArrayOutputStream = null;
    protected GZIPOutputStream gZIPOutputStream = null;
    protected boolean closed = false;
    protected HttpServletResponse httpServletResponse = null;
    protected ServletOutputStream servletOutputStream = null;

    Logger logger = Logger.getLogger(GZIPResponseStream.class);

    public GZIPResponseStream(HttpServletResponse response) throws IOException {
        super();
        closed = false;
        this.httpServletResponse = response;
        this.servletOutputStream = response.getOutputStream();
        byteArrayOutputStream = new ByteArrayOutputStream();
        gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
    }

    @Override
    public void close() throws IOException {
        if (closed) {
            return;
        }
        gZIPOutputStream.finish();

        byte[] bytes = byteArrayOutputStream.toByteArray();

        httpServletResponse.addHeader(HttpHeaders.CONTENT_LENGTH, Integer.toString(bytes.length));
        httpServletResponse.addHeader(HttpHeaders.CONTENT_ENCODING, HTTPHeaders.GZIPHeader.gethTTPHeaderValue());
        servletOutputStream.write(bytes);
        servletOutputStream.flush();
        servletOutputStream.close();
        closed = true;
    }

    @Override
    public void flush() throws IOException {
        if (closed) {
            return;
        }
        gZIPOutputStream.flush();
    }

    @Override
    public void write(int b) throws IOException {
        if (closed) {
            return;
        }
        gZIPOutputStream.write((byte) b);
    }

    @Override
    public void write(byte b[]) throws IOException {
        write(b, 0, b.length);
    }

    @Override
    public void write(byte b[], int off, int len) throws IOException {
        if (closed) {
            return;
        }
        gZIPOutputStream.write(b, off, len);
    }

    public boolean closed() {
        return (this.closed);
    }

    public void reset() {
        //noop
    }
}
