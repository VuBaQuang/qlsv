package bean.Class;

import dao.ClassDAO;
import model.*;
import model.ClassPayroll;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.*;

@ManagedBean
@SessionScoped
public class InfoClassMB {
    ClassPayroll class_ = new ClassPayroll();
    ClassDAO classesDAO = new ClassDAO();

    public InfoClassMB() {

    }

    public void findById() {
        class_= classesDAO.findById(class_.getId());
    }

    public ClassPayroll getClass_() {
        return class_;
    }

    public void setClass_(ClassPayroll class_) {
        this.class_ = class_;
    }

    public List<Student> getStudentList() {
        Iterator<Student> iterator = class_.getStudents().iterator();
        List<Student> studentList = new LinkedList<>();
        while (iterator.hasNext()) {
            studentList.add(iterator.next());
        }
        return studentList;
    }
}
