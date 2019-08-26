package bean;

import model.Subjects;

import javax.faces.bean.ManagedBean;
import java.util.ArrayList;

@ManagedBean
public class SubjectsMB {
    ArrayList<Subjects> subjects = new ArrayList<>();
}
