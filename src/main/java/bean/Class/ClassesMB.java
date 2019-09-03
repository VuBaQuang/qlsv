package bean.Class;

import bean.student.EditStudentMB;
import dao.ClassDAO;
import model.ClassPayroll;
import org.hibernate.exception.ConstraintViolationException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@SessionScoped
public class ClassesMB implements Serializable {
    private ClassPayroll class_ = new ClassPayroll();
    private List<ClassPayroll> listClasses;
    private int stt = 0;
    private ClassDAO classesDAO = new ClassDAO();

    @ManagedProperty(value = "#{infoClassMB}")
    private InfoClassMB infoClassMB;
    @ManagedProperty(value = "#{editStudentMB}")
    private EditStudentMB editStudentMB;


    public String create() {
        int i = classesDAO.create(class_);
        if (i == 2) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Create error", "Code is exist");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        } else if (i == 1) {
            return "class?faces-redirect=true";
        } else return null;

    }


    public String info(ClassPayroll classes) {
        infoClassMB.setClassPayroll(classes);
        return "info?faces-redirect=true&includeViewParams=true";
    }

    public void delete(ClassPayroll classes) {
        classesDAO.delete(classes);
    }


    public int getStt() {
        stt++;
        return stt;
    }

    public InfoClassMB getInfoClassMB() {
        return infoClassMB;
    }

    public void setInfoClassMB(InfoClassMB infoClassMB) {
        this.infoClassMB = infoClassMB;
    }

    public EditStudentMB getEditStudentMB() {
        return editStudentMB;
    }

    public void setEditStudentMB(EditStudentMB editStudentMB) {
        this.editStudentMB = editStudentMB;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public ClassPayroll getClasses() {

        return class_;
    }

    public void setClasses(ClassPayroll class_) {
        this.class_ = class_;
    }

    public List<ClassPayroll> getListClasses() {
        stt = 0;
        return classesDAO.findAll();
    }

    public void setListClasses(List<ClassPayroll> listClasses) {
        this.listClasses = listClasses;
    }

    public void deleteClass(ClassPayroll class_) {
        ClassDAO dao = new ClassDAO();
        dao.delete(class_);
    }
}
