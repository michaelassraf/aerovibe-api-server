/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lbis.aerovibe.api.server.filtering;

/**
 *
 * @author Development User
 */
import com.lbis.aerovibe.enums.HTTPHeaders;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpHeaders;

public class GZIPFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            String ae = httpServletRequest.getHeader(HttpHeaders.ACCEPT_ENCODING);
            if (ae != null && ae.contains(HTTPHeaders.GZIPHeader.gethTTPHeaderValue())) {
                GZIPResponseWrapper gZIPResponseWrapper = new GZIPResponseWrapper(httpServletResponse);
                filterChain.doFilter(servletRequest, gZIPResponseWrapper);
                gZIPResponseWrapper.finishResponse();
                return;
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    public void init(FilterConfig filterConfig) {
        // noop
    }

    public void destroy() {
        // noop
    }
}
