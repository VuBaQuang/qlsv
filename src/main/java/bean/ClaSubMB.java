package bean;

import dao.*;
import model.*;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.io.Serializable;
import java.util.*;

@ManagedBean
@FacesConverter(value = "ClaSubConvert")
@SessionScoped
public class ClaSubMB implements Serializable, Converter {
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
    private StudentDAO studentDAO = new StudentDAO();

    private List<Date> range;
    private String sub;
    private String classe;
    private String day;


    private List<Student> students;
    private List<Student> listStudent;
    private List<String> listSubject;
    private List<String> listClass;
    private List<ClassSubject> classSubjects;

    public void resetSub() {
        subject = new Subject();
    }

    public void reset() {
        classe = null;
        sub = null;
        range = new LinkedList<>();
        classSubject = new ClassSubject();
    }


    public void listSub() {

        listSubject = new LinkedList<>();
        for (Subject subject : subjectDAO.findAll()) {
            listSubject.add(subject.getName());
        }
        listClass = new LinkedList<>();
        for (ClassCredit classCredit : classCreditDAO.findAll()) {
            listClass.add(classCredit.getName());
        }
        updateListStudent();

    }

    public void createClaCre() {
        ClassCredit classCredit = classCreditDAO.create(this.classCredit);
        if (classCredit !=null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Thêm lớp " + classCredit.getName() + " thành công");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed", "System Error !");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void createSubject() {
        int i = subjectDAO.create(subject);
        if (i == 0) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Thêm môn học " + subject.getName() + " thành công");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else if (i == 1) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Failed", "Đã tồn tại môn học có mã " + subject.getCode());
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed", "System Error !");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void deleteSubject(Subject subject) {
        subjectDAO.delete(subject);
    }

    public boolean check() {
        return classe != null && sub != null;
    }

    public void add2Class(Student student, String classe, String subject) {
        ClassSubject classSubject = claSubDAO.getBySubFtClass(subjectDAO.getByName(subject), classCreditDAO.getIdByName(classe));
        if (registersubDAO.create(new Registersub(classSubject, student, null)).getId() != null) {
            classSubject.setRegistered(classSubject.getRegistered() != null ? classSubject.getRegistered() + 1 : 1);
            claSubDAO.update(classSubject);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Thêm thành công sinh viên " + student.getName() + " - " + student.getCode() + " vào lớp " + sub + " - " + classe);
            FacesContext.getCurrentInstance().addMessage(null, message);
            updateClassSubject();
            updateListStu();
            updateListStudent();
        }
    }

    public boolean checkClSu() {
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
        if (classe == null || sub == null) {
            return false;
        }
        return claSubDAO.getBySubFtClass(subjectDAO.getByName(sub), classCreditDAO.getIdByName(classe)) != null;
    }

    public void updateListStu() {
        StudentDAO studentDAO = new StudentDAO();
        students = new LinkedList<>();
        for (Object o : studentDAO.listStudent(subjectDAO.getByName(sub))) {
            Student student = (Student) o;
            students.add(student);
        }
    }

    public List<Student> getStudents() {
        updateListStu();
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

//    public void updateListStudents() {
//        Set<Student> students = null;
//        if (classe != null) {
//            if (classe.equals("")) {
//                classe = null;
//            }
//        }
//        if (sub != null) {
//            if (sub.equals("")) {
//                sub = null;
//            }
//        }
//        classSubjects = claSubDAO.findBySubCla(subjectDAO.getByName(sub), classCreditDAO.getIdByName(classe));
//        students = new LinkedHashSet<>();
//        for (ClassSubject classSubject : classSubjects) {
//            for (Registersub value : classSubject.getRegistersubs()) {
//                students.add(value.getStudent());
//            }
//        }
//        listStudent = new LinkedList<>();
//        listStudent.addAll(students);
//    }

    public void add2Class(Student students, ClassPayroll classes) {
        students.setClassPayroll(classes);
        studentDAO.update(students);
    }

    public void updateListStudent() {
        Set<Student> students ;
        students = new LinkedHashSet<>();
        try {
            for (Registersub value : classSubject.getRegistersubs()) {
                students.add(value.getStudent());
            }
            listStudent = new LinkedList<>();
            listStudent.addAll(students);
        } catch (NullPointerException e) {
            listStudent = new LinkedList<>();
        }

    }

    public String getScores(Student student) {
        score="Chưa có điểm";

        List<Registersub> list = registersubDAO.findByClassStu(classSubject, student);
        if(list.size()>0){
            score = registersubDAO.findByClassStu(classSubject, student).get(0).getScore() != null ? registersubDAO.findByClassStu(classSubject, student).get(0).getScore().toString() : "Chưa có điểm";
        }
        return score;
    }

    public void setClaSub() {
        classCredit = classCreditDAO.findById(classCredit.getId());
        subject = subjectDAO.findById(subject.getId());
        classSubject = claSubDAO.findBySubCla(subject, classCredit).get(0);
    }

    public void updateScore(CellEditEvent event) {
        Student student = (Student) ((DataTable) event.getComponent()).getRowData();
        registersub = registersubDAO.findByClassStu(classSubject, student).get(0);
        double score;

        try {
            score = Double.parseDouble(event.getNewValue().toString());
            registersub.setScore(score);
            if (registersubDAO.update(registersub)) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Update score success");
                FacesContext.getCurrentInstance().addMessage(null, message);
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Failed", "Update score fail");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Failed", "Invalid");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void updateClassSubject() {
        if ((sub != null && !sub.equals("")) && (classe != null && !classe.equals(""))) {
            Subject subject = subjectDAO.getByName(sub);
            ClassCredit classCredit = classCreditDAO.getIdByName(classe);
            classSubject = new ClassSubject();
            classSubject = claSubDAO.getBySubFtClass(subject, classCredit);
        } else {
            classSubject = new ClassSubject();
        }
    }


    public void updateClassSubjects() {
        Subject subject = subjectDAO.getByName(sub);
        classSubjects = claSubDAO.findBySub(subject);
    }


    public void delete() {
            classSubject  = claSubDAO.getBySubFtClass(subjectDAO.getByName(sub),classCreditDAO.getIdByName(classe));
            for (Registersub registersub : registersubDAO.findByClass(classSubject)) {
                registersubDAO.delete(registersub);
            }
            claSubDAO.delete(classSubject);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Xóa lớp "+sub+" - "+classe+" thành công");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String register(ClassSubject classSubject, Student student) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage message = null;
        boolean result = false;
        for (Registersub registersub : registersubDAO.findByStudent(student)) {
            if (classSubject.getSubject().getName().equals(registersub.getClassSubject().getSubject().getName())) {
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
        updateClassSubject();

    }

    public void create() {
        if (claSubDAO.getBySubFtClass(subjectDAO.getByName(sub), classCreditDAO.getIdByName(classe)) != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail", "Đã tồn tại " + sub + " - " + classe);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            try {
                classSubject.setStartTime(range != null && range.size() > 0 ? range.get(0) : null);
                classSubject.setEndTime(range != null && range.size() > 1 ? range.get(1) : null);
                classSubject.setClassCredit(classCreditDAO.getIdByName(classe));
                classSubject.setSubject(subjectDAO.getByName(sub));
                classSubject.setDay(day);
                claSubDAO.create(classSubject);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Tạo lớp " + sub + " - " + classe + " thành công !");
                FacesContext.getCurrentInstance().addMessage(null, message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    public List<String> getListClass() {
        return listClass;
    }

    public void setListClass(List<String> listClass) {
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

    public List<String> getListSubject() {
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

    public void setListSubject(List<String> listSubject) {
        this.listSubject = listSubject;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        } else {
            return ClaSubDAO.findById(Integer.parseInt(value));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof ClassSubject) {
            return String.valueOf(((ClassSubject) value).getId());
        } else {
            throw new ConverterException(new FacesMessage(value + " is not a valid ClaSub"));
        }
    }
}

