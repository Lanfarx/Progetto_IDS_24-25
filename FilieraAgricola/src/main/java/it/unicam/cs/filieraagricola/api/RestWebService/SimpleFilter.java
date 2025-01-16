package it.unicam.cs.filieraagricola.api.RestWebService;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintStream;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class SimpleFilter implements Filter {
    public SimpleFilter() {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterchain) throws IOException, ServletException {
        PrintStream var10000 = System.out;
        String var10001 = request.getRemoteHost();
        var10000.println("Remote Host:" + var10001 + request.getLocalPort());
        System.out.println("Remote Address:" + request.getRemoteAddr());
        filterchain.doFilter(request, response);
    }

    public void init(FilterConfig filterconfig) throws ServletException {
    }
}
