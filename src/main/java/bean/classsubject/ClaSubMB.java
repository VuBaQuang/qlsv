package bean.classsubject;

import dao.ClaSubDAO;
import dao.ClassCreditDAO;
import dao.SubjectDAO;
import model.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.*;

@ManagedBean
@SessionScoped
public class ClaSubMB {
    private ClassSubject classSubject = new ClassSubject();
    private Registersub registersub = new Registersub();
    private ClassCredit classCredit = new ClassCredit();
    private Subject subject = new Subject();

    private ClaSubDAO claSubDAO = new ClaSubDAO();
    private ClassCreditDAO classCreditDAO = new ClassCreditDAO();
    private SubjectDAO subjectDAO = new SubjectDAO();

    private String sub;

    private Map<String, String> listSubject;
    private List<ClassSubject> classSubjects;

    @PostConstruct
    public void listSub() {
        listSubject = new LinkedHashMap<>();
        for (Subject subject : subjectDAO.findAll()) {
            listSubject.put(subject.getName(), subject.getName());
        }

    }

    public void updateClassSubjects() {
        Subject subject = subjectDAO.getByName(sub);
        classSubjects = claSubDAO.findBySub(subject);
    }

    public List<ClassSubject> getClassSubjects() {
        return classSubjects;
    }

    public void setClassSubjects(List<ClassSubject> classSubjects) {
        this.classSubjects = classSubjects;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Map<String, String> getListSubject() {

        return listSubject;
    }

    public ClassCredit getClassCredit() {
        return classCredit;
    }

    public void setClassCredit(ClassCredit classCredit) {
        this.classCredit = classCredit;
    }

    public ClassSubject getClassSubject() {
        return classSubject;
    }

    public void setClassSubject(ClassSubject classSubject) {
        this.classSubject = classSubject;
    }

    public Registersub getRegistersub() {
        return registersub;
    }

    public void setRegistersub(Registersub registersub) {
        this.registersub = registersub;
    }


    public void setListSubject(Map<String, String> listSubject) {
        this.listSubject = listSubject;
    }

    public Map<String, String> getListSub(String nameClass) {
        List<ClassSubject> classSubjects = claSubDAO.getSubjectByClass(classCreditDAO.getIdByName(nameClass));
        Set<String> set = new LinkedHashSet<>();
        for (ClassSubject classSubject : classSubjects) {
            set.add(classSubject.getSubject().getName());
        }
        Map<String, String> map = new LinkedHashMap<>();
        for (String s : set) {
            map.put(s, s);
        }
        return map;
    }


}
