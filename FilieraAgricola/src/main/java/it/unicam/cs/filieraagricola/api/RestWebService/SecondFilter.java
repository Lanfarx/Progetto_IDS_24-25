package it.unicam.cs.filieraagricola.api.RestWebService;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(2)
@Component
public class SecondFilter implements Filter {
    public SecondFilter() {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterchain) throws IOException, ServletException {
        System.out.println("Secondo Filtro");
        filterchain.doFilter(request, response);
    }

    public void init(FilterConfig filterconfig) throws ServletException {
    }
}
