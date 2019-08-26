package bean.Class;

import bean.student.EditStudentMB;
import dao.ClassesDAO;
import model.Classes;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@SessionScoped
public class ClassesMB implements Serializable {
    private Classes classes;
    private List<Classes> listClasses;
    private int stt = 0;
    private ClassesDAO classesDAO = new ClassesDAO();

    @ManagedProperty(value = "#{infoClassMB}")
    private InfoClassMB infoClassMB;
    @ManagedProperty(value = "#{editStudentMB}")
    private EditStudentMB editStudentMB;

    public EditStudentMB getEditStudentMB() {
        return editStudentMB;
    }

    public void setEditStudentMB(EditStudentMB editStudentMB) {
        this.editStudentMB = editStudentMB;
    }

    public InfoClassMB getInfoClassMB() {
        return infoClassMB;
    }

    public void setInfoClassMB(InfoClassMB infoClassMB) {
        this.infoClassMB = infoClassMB;
    }

    public String info(Classes classes) {
        infoClassMB.setClasses(classes);
        return "info?faces-redirect=true&includeViewParams=true";
    }

    public void delete(Classes classes) {

    }


    public int getStt() {
        stt++;
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public Classes getClasses() {

        return classes;
    }

    public void setClasses(Classes classes) {
        this.classes = classes;
    }

    public List<Classes> getListClasses() {
        stt=0;
        return classesDAO.findAll();
    }

    public void setListClasses(List<Classes> listClasses) {
        this.listClasses = listClasses;
    }

    public void deleteClass(Classes classes) {
        ClassesDAO dao = new ClassesDAO();
        dao.delete(classes);
    }
}
