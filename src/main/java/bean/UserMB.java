package bean;

import dao.UserDAO;
import org.primefaces.PrimeFaces;

import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class UserMB {
  String user;
  String password;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void login() {
        FacesContext context = FacesContext.getCurrentInstance();
        UserDAO userDAO = new UserDAO();
        User user1 = userDAO.findByName(user);
        System.out.println(user1);
        FacesMessage message ;
        boolean loggedIn = false;
        if (user1 != null) {
            if (user1.getPassword().equals(password)) {
                loggedIn = true;
                context.getExternalContext().getSessionMap().put("user", user);
                context.getExternalContext().getSessionMap().put("rule", user1.getRule());
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", user);
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Invalid password");
            }
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Invalid user");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
        PrimeFaces.current().ajax().addCallbackParam("loggedIn", loggedIn);
    }
}
