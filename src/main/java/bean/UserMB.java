package bean;

import dao.StudentDAO;
import dao.UserDAO;
import model.Student;
import model.User;
import org.primefaces.PrimeFaces;

import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.faces.validator.ValidatorException;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@ManagedBean
@SessionScoped
public class UserMB implements Serializable {
    private String user;
    private String password;
    private String confirm_password;


    private List<User> userList;


    private List<Student> studentList;
    private User oUser = new User();


    private Student student = new Student();
    private StudentDAO studentDAO = new StudentDAO();
    private UserDAO userDAO = new UserDAO();
    @ManagedProperty("#{claSubMB}")
    private ClaSubMB claSubMB = new ClaSubMB();

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    public void validatePasswordError(FacesContext context, UIComponent component,
                                      Object value) {
        String confirmPassword = (String) value;

        // Retrieve the temporary value from the password field
        UIInput passwordInput = (UIInput) component.findComponent("password");
        String password = (String) passwordInput.getLocalValue();

        if (password == null || confirmPassword == null || !password.equals(confirmPassword)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Xác nhận mật khẩu sai !");
            throw new ValidatorException(msg);
        }
    }

    public void create() {
        System.out.println("1");
    }

    public void delete(User user) {
        userDAO.delete(user);
    }

    public User getoUser() {
        return oUser;
    }

    public void setoUser(User oUser) {
        this.oUser = oUser;
    }

    public List<User> getUserList() {
        userList = userDAO.findAll();
        return userList;
    }

    public List<Student> getStudentList() {
        studentList = studentDAO.noneAccount();
        return studentList;
    }

    public List<String> listStudent() {
        List<String> list = new LinkedList<>();
        studentList = studentDAO.noneAccount();
        for (Student student : studentList) {
            list.add(student.getId() + " - " + student.getCode() + " - " + student.getName());
        }
        return list;
    }


    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    public void setUserList(List<User> studentList) {
        this.userList = studentList;
    }

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
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Loggin Error", "Invalid password");
            }
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Loggin Error", "Invalid user");
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
