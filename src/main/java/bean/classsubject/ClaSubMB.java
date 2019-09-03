package bean.classsubject;

import dao.*;
import model.*;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Entity;
import java.util.*;

@ManagedBean
@SessionScoped
public class ClaSubMB {
    private ClassSubject classSubject = new ClassSubject();
    private Registersub registersub = new Registersub();
    private ClassCredit classCredit = new ClassCredit();
    private Subject subject = new Subject();
    private String score;


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


    public void listSub() {
        classe = null;
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


    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
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
    }

    public void updateListStudents() {
        Set<Student> students = null;

        classSubjects = claSubDAO.findBySubCla(subject, classCredit);
        classSubject = classSubjects.get(0);
        students = new LinkedHashSet<>();
        for (Registersub value : classSubject.getRegistersubs()) {
            students.add(value.getStudent());
        }
        listStudent = new LinkedList<>();
        listStudent.addAll(students);
    }

    public String getScore(Student student) {
        score = registersubDAO.findByClassStu(classSubject, student).get(0).getScore() != null ? registersubDAO.findByClassStu(classSubject, student).get(0).getScore().toString() : "Chưa có điểm";
        return score;
    }

    public void setClaSub() {
        classCredit = classCreditDAO.findById(classCredit.getId());
        subject = subjectDAO.findById(subject.getId());
        classSubject = claSubDAO.findBySubCla(subject, classCredit).get(0);
    }

    public String checkClaSub(String classe) {
        Set<Student> students = null;
        this.classe = classe;
        if (classe == null || classe.equals("")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Class is invalid");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }
        if (sub == null || sub.equals("")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Subject is invalid");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }
        classCredit = classCreditDAO.getIdByName(classe);
        subject = subjectDAO.getByName(sub);
        updateListStudents();
        return "info.xhtml?faces-redirect=true&includeViewParams=true";
    }

    public void updateScore(CellEditEvent event) {
        Student student = (Student) ((DataTable) event.getComponent()).getRowData();
        registersub = registersubDAO.findByClassStu(classSubject, student).get(0);
        double score = Double.parseDouble(event.getNewValue().toString());
        if (score == 0) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Failed", "Invalid");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            registersub.setScore(score);
            if (registersubDAO.update(registersub)) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Update score success");
                FacesContext.getCurrentInstance().addMessage(null, message);
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Failed", "Update score fail");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }

    }

    public void updateClassSubjects() {
        Subject subject = subjectDAO.getByName(sub);
        classSubjects = claSubDAO.findBySub(subject);
    }

    public String delete(ClassSubject classSubject) {
        for (Registersub registersub : registersubDAO.findByClass(classSubject)) {
            registersubDAO.delete(registersub);
        }
        claSubDAO.delete(classSubject);
        return "view.xhtml?faces-redirect=true";
    }

    public String register(ClassSubject classSubject, Student student) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage message = null;
        boolean result = false;
        for (Registersub registersub : registersubDAO.findByStudent(student)) {
            if (classSubject.getSubject().getName().equals(registersub.getClassSubject().getSubject().getName()) && registersub.getScore() == null) {
                result = true;
                break;
            }
        }
        if (result) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed", "Bạn đã đăng ký môn học này !");
        } else {
            Registersub registersub = new Registersub();
            registersub.setStudent(student);
            registersub.setClassSubject(classSubject);
            registersubDAO.create(registersub);
            classSubject.setRegistered(classSubject.getRegistered() != null ? classSubject.getRegistered() + 1 : 1);
            claSubDAO.update(classSubject);
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Success", "Đăng ký thành công " + classSubject.getSubject().getName());
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
        return null;
    }

    public void deleteStuClaSub(Student student) {
        List<Registersub> registersubList = registersubDAO.findByStudent(student);
        List<ClassSubject> classSubjects = claSubDAO.findBySubCla(subjectDAO.getByName(sub), classCreditDAO.getIdByName(classe));
        loop:
        for (ClassSubject classSubject : classSubjects) {
            for (Registersub registersub : registersubList) {
                if (registersub.getClassSubject().getId().equals(classSubject.getId())) {
                    registersubDAO.delete(registersub);
                    classSubject.setRegistered(classSubject.getRegistered() - 1);
                    claSubDAO.update(classSubject);
                    break loop;
                }
            }
        }
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
