package bean.Class;

import dao.AddressDAO;
import dao.ClassesDAO;
import dao.StudentsDAO;
import model.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.*;

@ManagedBean
@SessionScoped
public class InfoClassMB {
    Classes classes = new Classes();

    public InfoClassMB() {

    }

    public Classes getClasses() {
        return classes;
    }

    public void setClasses(Classes classes) {
        this.classes = classes;
    }

    public List<Students> getStudentList() {
        Iterator<Students>  iterator =classes.getStudentses().iterator();
        List<Students> studentList=new LinkedList<>();
        while (iterator.hasNext()){
            studentList.add(iterator.next());
        }
        return studentList;
    }
}
