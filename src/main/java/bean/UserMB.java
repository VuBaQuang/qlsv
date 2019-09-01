package bean;

import bean.classsubject.ClaSubMB;
import dao.ClaSubDAO;
import dao.RegistersubDAO;
import dao.UserDAO;
import model.ClassSubject;
import model.Registersub;
import model.Student;
import model.User;
import org.primefaces.PrimeFaces;

import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@ManagedBean
@SessionScoped
public class UserMB {
    private String user;
    private String password;
    private Student student = new Student();
    UserDAO userDAO = new UserDAO();
    ClaSubDAO claSubDAO = new ClaSubDAO();
    RegistersubDAO registersubDAO = new RegistersubDAO();
    @ManagedProperty("#{claSubMB}")
    ClaSubMB claSubMB = new ClaSubMB();

    public ClaSubMB getClaSubMB() {
        return claSubMB;
    }

    public void setClaSubMB(ClaSubMB claSubMB) {
        this.claSubMB = claSubMB;
    }

    public Student getStudent() {
        setStudent(userDAO.findByName(user).getStudent());
        return student;
    }


    public void setStudent(Student student) {
        this.student = student;
    }

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

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        UserDAO userDAO = new UserDAO();
        User user1 = userDAO.findByName(user);
        FacesMessage message = null;
        boolean loggedIn = false;
        if (user1 != null) {
            if (user1.getPassword().equals(password)) {
                loggedIn = true;
                context.getExternalContext().getSessionMap().put("user", user);
                context.getExternalContext().getSessionMap().put("rule", user1.getRule());
                if (user1.getRule().equals("1")) {
                    return "admin";
                }
                if (user1.getRule().equals("2")) {
                    return "user";
                }
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Invalid password");
            }
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Invalid user");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
        PrimeFaces.current().ajax().addCallbackParam("loggedIn", loggedIn);
        return null;
    }

    public void logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().invalidateSession();
        try {
            context.getExternalContext().redirect("/QLSV/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void home() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            context.getExternalContext().redirect("/QLSV/");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
