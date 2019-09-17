package bean;

import com.sun.xml.bind.v2.TODO;
import dao.ClassDAO;
import dao.StudentDAO;
import model.ClassPayroll;
import model.Student;

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
    private StudentDAO studentDAO = new StudentDAO();


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

    public void create() {
        int i = classesDAO.create(classPayroll);
        if (i == 0) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Thêm lớp " + classPayroll.getName() + " thành công");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed", "System Error !");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public boolean checkNull() {
        return classPayroll.getCode() != null;
    }

//TODO
    public void add2Class(Student students, ClassPayroll classes) {
        students.setClassPayroll(classes);
        studentDAO.update(students);
        updateStudentList();

    }

    public void delete(ClassPayroll classes) {

        try {
            classesDAO.delete(classes);
        }catch (Exception e){
            System.out.println("1111111111111111");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed", "System Error !");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }


    public int getStt() {
        stt++;
        return stt;
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
        this.studentList = new LinkedList<>();
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
