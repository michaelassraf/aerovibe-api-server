/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lbis.aerovibe.api.server.filtering;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 *
 * @author Development User
 */
public class GZIPResponseWrapper extends HttpServletResponseWrapper {

    protected HttpServletResponse httpServletResponse = null;
    protected ServletOutputStream servletOutputStream = null;
    protected PrintWriter writer = null;

    public GZIPResponseWrapper(HttpServletResponse response) {
        super(response);
        httpServletResponse = response;
    }

    public ServletOutputStream createOutputStream() throws IOException {
        return (new GZIPResponseStream(httpServletResponse));
    }

    public void finishResponse() {
        try {
            if (writer != null) {
                writer.close();
            } else {
                if (servletOutputStream != null) {
                    servletOutputStream.close();
                }
            }
        } catch (IOException e) {
        }
    }

    @Override
    public void flushBuffer() throws IOException {
        servletOutputStream.flush();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called!");
        }

        if (servletOutputStream == null) {
            servletOutputStream = createOutputStream();
        }
        return (servletOutputStream);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (writer != null) {
            return (writer);
        }

        if (servletOutputStream != null) {
            throw new IllegalStateException("getOutputStream() has already been called!");
        }

        servletOutputStream = createOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(servletOutputStream, "UTF-8"));
        return (writer);
    }

    @Override
    public void setContentLength(int length) {
    }
}
