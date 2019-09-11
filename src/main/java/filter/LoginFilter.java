package filter;

import javax.faces.application.ResourceHandler;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//@WebFilter(urlPatterns = {"/faces/home/login.xhtml"})
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("LogFilter Home init!");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession(false);
        String loginURI = request.getContextPath() + "/";
        boolean loggedIn = session != null && session.getAttribute("user") != null;
        String rule = session != null ? (String) session.getAttribute("rule") : "";
        if (loggedIn) {
                if (rule.equals("1")) {
                    response.sendRedirect(loginURI + "faces/admin/admin.xhtml");
                }
                if (rule.equals("2")) {
                    response.sendRedirect(loginURI + "faces/user/view.xhtml");
                }
        } else {
            chain.doFilter(request, response);
        }

    }

    @Override
    public void destroy() {
        System.out.println("LogFilter destroy!");
    }
}
