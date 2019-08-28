package bean;

import model.Subject;

import javax.faces.bean.ManagedBean;
import java.util.ArrayList;

@ManagedBean
public class SubjectsMB {
    ArrayList<Subject> subjects = new ArrayList<>();
}
