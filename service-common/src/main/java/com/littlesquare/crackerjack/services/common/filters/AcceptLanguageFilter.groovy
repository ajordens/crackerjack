package com.littlesquare.crackerjack.services.common.filters


import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper
import javax.ws.rs.core.HttpHeaders

/**
 * @author Adam Jordens (adam@jordens.org)
 */
public class AcceptLanguageFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            chain.doFilter(new AcceptLanguageRequest((HttpServletRequest) request), response);
        } else {
            chain.doFilter(request, response);
        }
    }
    @Override
    public void destroy() {
        // Do nothing
    }

    public class AcceptLanguageRequest extends HttpServletRequestWrapper {
        public AcceptLanguageRequest(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getHeader(String name) {
            HttpServletRequest request = (HttpServletRequest) getRequest();
            return filterAcceptLanguage(name, request.getHeader(name))
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            HttpServletRequest request = (HttpServletRequest) getRequest();
            return Collections.enumeration(request.getHeaders(name).collect {
                return filterAcceptLanguage(name, it)
            })
        }

        private String filterAcceptLanguage(String headerName, String headerValue) {
            if (headerName.equalsIgnoreCase(HttpHeaders.ACCEPT_LANGUAGE) && headerValue) {
                return headerValue.replace("es-419", "es")
            }
            return headerValue
        }
    }
}