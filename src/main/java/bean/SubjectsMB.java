package bean;

import dao.SubjectDAO;
import model.Subject;

import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
public class SubjectsMB {
    Subject subject = new Subject();
    List<Subject> subjects = new ArrayList<>();
    SubjectDAO subjectDAO = new SubjectDAO();


    public String create(){
        subjectDAO.create(subject);
        return "subject.xhtml?faces-redirect=true";
    }
    public void delete(Subject subject){
        subjectDAO.delete(subject);

    }
    public List<Subject> getSubjects() {
        return subjectDAO.findAll();
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
