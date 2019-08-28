package bean.classsubject;

import dao.*;
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
    private ClassDAO classDAO = new ClassDAO();
    private RegistersubDAO registersubDAO = new RegistersubDAO();

    private List<Date> range;
    private String sub;
    private String classe;
    private String day;


    private List<Student> listStudent;
    private Map<String, String> listSubject;
    private Map<String, String> listClass;
    private List<ClassSubject> classSubjects;

    @PostConstruct
    public void listSub() {
        listSubject = new LinkedHashMap<>();
        for (Subject subject : subjectDAO.findAll()) {
            listSubject.put(subject.getName(), subject.getName());
        }
        listClass = new LinkedHashMap<>();
        for (ClassCredit classCredit : classCreditDAO.findAll()) {
            listClass.put(classCredit.getName(), classCredit.getName());
        }
        updateListStudent();
    }

    public void updateListStudent() {
        Set<Student> students = null;
        if (classe != null) {
            if (classe.equals("")) {
                classe = null;
            }
        }
        if (sub != null) {
            if (sub.equals("")) {
                sub = null;
            }
        }
        classSubjects = claSubDAO.findBySubCla(subjectDAO.getByName(sub), classCreditDAO.getIdByName(classe));
        students = new LinkedHashSet<>();
        for (ClassSubject classSubject : classSubjects) {
            for (Registersub value : classSubject.getRegistersubs()) {
                students.add(value.getStudent());
            }
        }
        listStudent = new LinkedList<>();
        listStudent.addAll(students);
        System.out.println(listStudent);
    }

    public void updateClassSubjects() {
        Subject subject = subjectDAO.getByName(sub);
        classSubjects = claSubDAO.findBySub(subject);
    }

    public String delete(ClassSubject classSubject) {
        claSubDAO.delete(classSubject);
        return "view.xhtml?faces-redirect=true";
    }

    public String deleteStuClaSub(Student student) {
        List<Registersub> registersubList = registersubDAO.findByStudent(student);
        List<ClassSubject> classSubjects = claSubDAO.findBySubCla(subjectDAO.getByName(sub), classCreditDAO.getIdByName(classe));

        for ( ClassSubject classSubject : classSubjects) {
            for (Registersub registersub : registersubList) {
                if (registersub.getClassSubject().getId().equals(classSubject.getId())) {
                    registersubDAO.delete(registersub);
                    break;
                }
            }


        }

//        for (ClassSubject classSubject : classSubjects) {
//            if (classSubject.getSubject().getName().equals(sub) && classSubject.getClassCredit().getName().equals(classe)) {
//                registersubDAO.delete(registersubDAO.findByClassStu(classSubject, student));
//                break;
//            }
//        }
        return "view.xhtml?faces-redirect=true";
    }

    public String create() {
        try {
            classSubject.setStartTime(range.size() > 0 ? range.get(0) : null);
            classSubject.setEndTime(range.size() > 1 ? range.get(1) : null);
            classSubject.setClassCredit(classCreditDAO.getIdByName(classe));
            classSubject.setSubject(subjectDAO.getByName(sub));
            classSubject.setDay(day);
            claSubDAO.create(classSubject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "view.xhtml?faces-redirect=true";
    }

    public List<Student> getListStudent() {
        return listStudent;
    }

    public void setListStudent(List<Student> listStudent) {
        this.listStudent = listStudent;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Map<String, String> getListClass() {
        return listClass;
    }

    public void setListClass(Map<String, String> listClass) {
        this.listClass = listClass;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public List<Date> getRange() {
        return range;
    }

    public void setRange(List<Date> range) {
        this.range = range;
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
}
