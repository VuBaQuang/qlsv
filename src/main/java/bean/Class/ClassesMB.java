package bean.Class;

import bean.student.EditStudentMB;
import dao.ClassDAO;
import dao.StudentDAO;
import model.ClassCredit;
import model.ClassPayroll;
import model.Student;
import org.hibernate.exception.ConstraintViolationException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@ManagedBean
@FacesConverter(value = "SelectItemClassConverter")
@SessionScoped
public class ClassesMB implements Serializable, Converter {
    private ClassPayroll classPayroll = new ClassPayroll();

    private List<Student> studentList;
    private List<ClassPayroll> listClasses;
    private int stt = 0;
    private ClassDAO classesDAO = new ClassDAO();
    StudentDAO studentDAO = new StudentDAO();
    @ManagedProperty(value = "#{infoClassMB}")
    private InfoClassMB infoClassMB;
    @ManagedProperty(value = "#{editStudentMB}")
    private EditStudentMB editStudentMB;

    public ClassPayroll getClassPayroll() {
        return classPayroll;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    public void setClassPayroll(ClassPayroll classPayroll) {
        this.classPayroll = classPayroll;
    }

    public String create() {
        int i = classesDAO.create(classPayroll);
        if (i == 2) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Create error", "Code is exist");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        } else if (i == 1) {
            return "class?faces-redirect=true";
        } else return null;

    }

    public boolean checkNull() {
        return classPayroll.getCode() != null;
    }

    public String info(ClassPayroll classes) {
        infoClassMB.setClassPayroll(classes);
        return "info?faces-redirect=true&includeViewParams=true";
    }

    public void add2Class(Student students, ClassPayroll classes) {
        students.setClassPayroll(classes);
        studentDAO.update(students);
        updateStudentList();
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

    public void updateClass() {
        classPayroll = classesDAO.findById(classPayroll.getId());
    }
    public void delStuClass(Student students) {
        students.setClassPayroll(null);
        studentDAO.update(students);
        updateStudentList();
    }

    public void updateStudentList() {
        updateClass();
        Iterator<Student> iterator = classPayroll.getStudents().iterator();
        List<Student> studentList = new LinkedList<>();
        while (iterator.hasNext()) {
            studentList.add(iterator.next());
        }
        this.studentList = studentList;
    }

    @Override
    public ClassPayroll getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        } else {
            return classesDAO.findById(Integer.parseInt(value));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof ClassPayroll) {
            return String.valueOf(((ClassPayroll) value).getId());
        } else {
            throw new ConverterException(new FacesMessage(value + " is not a valid ClassPayroll"));
        }
    }
}
