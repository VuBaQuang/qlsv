package filter;

import javax.faces.application.ResourceHandler;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/faces/user/*"})
public class UserFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("LogFilter user init!");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession(false);
        String loginURI = request.getContextPath() + "/";
        boolean loggedIn = session != null && session.getAttribute("user") != null;
        boolean roles = session != null && session.getAttribute("rule").equals("2");
        boolean resourceRequest = request.getRequestURI().startsWith(request.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER);
        if (loggedIn||resourceRequest) {
            if (roles) {
                chain.doFilter(request, response);
            } else {
                response.sendError(404);
            }
        } else {
            response.sendRedirect(loginURI);
        }

    }

    @Override
    public void destroy() {
        System.out.println("LogFilter destroy!");
    }
}
