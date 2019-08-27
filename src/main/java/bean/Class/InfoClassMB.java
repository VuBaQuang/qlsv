package bean.Class;

import dao.ClassDAO;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.*;

@ManagedBean
@SessionScoped
public class InfoClassMB {
    ClassPayroll classPayroll = new ClassPayroll();
    ClassDAO classesDAO = new ClassDAO();

    public InfoClassMB() {

    }

    public void findById() {
        classPayroll = classesDAO.findById(classPayroll.getId());
    }

    public ClassPayroll getClassPayroll() {
        return classPayroll;
    }

    public void setClassPayroll(ClassPayroll classPayroll) {
        this.classPayroll = classPayroll;
    }

    public List<Student> getStudentList() {
        Iterator<Student> iterator = classPayroll.getStudents().iterator();
        List<Student> studentList = new LinkedList<>();
        while (iterator.hasNext()) {
            studentList.add(iterator.next());
        }
        return studentList;
    }
}
