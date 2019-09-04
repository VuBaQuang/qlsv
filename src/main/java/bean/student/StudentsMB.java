package bean.student;

import dao.*;
import model.*;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.*;

@ManagedBean
@ViewScoped
public class StudentsMB implements Serializable {
    private Registersub registersub = new Registersub();
    private Student student = new Student();
    private List<ClassSubject> classSubjectList = new LinkedList<>();
    private List<Student> listStudent = new ArrayList<>();
    private List<Student> listStudentNone = new ArrayList<>();
    private Map<String, Map<String, String>> provinceDistric = new HashMap<String, Map<String, String>>();
    private Map<String, Map<String, String>> districWard = new HashMap<String, Map<String, String>>();
    private String studentClass;
    private String province;
    private String district;
    private String ward;
    private Map<String, String> classes;
    private Map<String, String> provinces;
    private Map<String, String> districts;
    private Map<String, String> wards;
    private AddressDAO addressDAO = new AddressDAO();
    private ClassDAO classesDAO = new ClassDAO();
    private StudentDAO studentsDAO = new StudentDAO();
    private ClaSubDAO claSubDAO = new ClaSubDAO();
    private RegistersubDAO registersubDAO = new RegistersubDAO();
    private UserDAO userDAO = new UserDAO();
    String redirect = "student";

    public void rsStu() {
        student = new Student();
        province = null;
    }

    public List<ClassSubject> getClassSubjectList() {
        return classSubjectList;
    }

    public List<ClassSubject> getClassSubjectLists() {
        classSubjectList = new LinkedList<>();
        for (Registersub registersub : student.getRegistersubs()) {
            classSubjectList.add(registersub.getClassSubject());
        }
        return classSubjectList;
    }

    public void setClassSubjectList(List<ClassSubject> classSubjectList) {
        this.classSubjectList = classSubjectList;
    }

    public String score(Student student, ClassSubject classSubject) {
        String result = registersubDAO.findByClassStu(classSubject, student).get(0).getScore() != null ? registersubDAO.findByClassStu(classSubject, student).get(0).getScore().toString() : "Chưa có điểm";
        return result;
    }


    public double avgScore(Student student) {
        double sum = 0;
        int t = 0;
        double avg = 0;
        for (Registersub registersub : registersubDAO.findByStudent(student)) {
            if (registersub.getScore() != null) {
                sum += registersub.getScore() != null ? registersub.getScore() * registersub.getClassSubject().getSubject().getCoefficient() : 0;
                t += registersub.getClassSubject().getSubject().getCoefficient();
            }
        }
        if (t == 0) {
            return 0;
        } else {
            return Math.ceil((sum / t) * 100) / 100;
        }
    }

    public List<Student> getListStudentNone() {
        return studentsDAO.getStudentNone();
    }

    public void setListStudentNone(List<Student> listStudentNone) {
        this.listStudentNone = listStudentNone;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    @ManagedProperty(value = "#{editStudentMB}")
    private EditStudentMB editStudentMB;


    public List<Student> getListStudent() {
        StudentDAO dao = new StudentDAO();
        return dao.findAll();
    }


    public void add2Class(Student students, ClassPayroll classes) {
        students.setClassPayroll(classes);
        studentsDAO.update(students);
    }

    public void delStuClass(Student students) {
        students.setClassPayroll(null);
        studentsDAO.update(students);
    }

    public String edit() {
        StringBuilder builder = new StringBuilder();
        builder.append(ward);
        builder.append(", ");
        builder.append(district);
        builder.append(", ");
        builder.append(province);
        student.setAddress(builder.toString());
        ClassPayroll classes = classesDAO.getClassByName(studentClass);
        student.setClassPayroll(classes);
        StudentDAO dao = new StudentDAO();
        dao.update(student);

        return "student";
    }

    public String merge() {
        if (student.getId() != null) {
            return edit();
        } else {
            return addStudent();
        }
    }

    public String addStudent() {
        StudentDAO dao = new StudentDAO();
        StringBuilder address = new StringBuilder();
        address.append(ward);
        address.append(", ");
        address.append(district);
        address.append(", ");
        address.append(province);
        if (ward.equals("") && district.equals("") && province.equals("")) {
            student.setAddress("");
        } else {
            student.setAddress(address.toString().trim());
        }
        ClassPayroll classes = classesDAO.getClassByName(studentClass);
        student.setClassPayroll(classes);
        student = dao.create(student);
        return "student.xhtml?faces-redirect=true";
    }

    public String delete(Student student) {
        StudentDAO dao = new StudentDAO();
        dao.delete(student);
        return "student.xhtml?faces-redirect=true";
    }


    public void edit(Student student) {
//        this.student = student;
//        System.out.println(this.student.getName());
        String[] address = student.getAddress().split("[,][ ]");
        province = address[2];
        district = address[1];
        ward = address[0];
        editStudentMB.setStudentClass(student.getClass() != null ? student.getClass().getName() : "");
        editStudentMB.setStudent(student);
        editStudentMB.setProvince(province);
        editStudentMB.setDistrict(district);
        editStudentMB.setWard(ward);
        editStudentMB.setStudentClass(student.getClassPayroll().getName());
    }

    @PostConstruct
    public void init() {
        System.out.println("Update address !");
        classes = new LinkedHashMap<>();
        provinces = new LinkedHashMap<>();
        for (Province province : addressDAO.getAllProvince()) {
            provinces.put(province.getName(), province.getName());
            Map<String, String> mapDistrict = new LinkedHashMap<>();
            for (District district : addressDAO.getDistricByProvince(province)) {
                mapDistrict.put(district.getName(), district.getName());
                Map<String, String> mapWard = new LinkedHashMap<>();
                for (Ward ward : addressDAO.getWardByDistrict(district)) {
                    mapWard.put(ward.getName(), ward.getName());
                }
                districWard.put(district.getName(), mapWard);
                mapDistrict.put(district.getName(), district.getName());
            }
            provinceDistric.put(province.getName(), mapDistrict);
        }
        for (ClassPayroll classes : classesDAO.findAll()) {
            this.classes.put(classes.getName(), classes.getName());
        }
        System.out.println("Update address success!");
    }

    @PostConstruct
    public void setDistricts() {
        districts = provinceDistric.get(province);
        wards = new LinkedHashMap<>();
    }

    public String getProvince() {
        try{
            if (student.getAddress() != null && province != null ) {
                setDistricts();
                province = student.getAddress().split("[,][ ]")[2];
            }
            return province;
        }catch (ArrayIndexOutOfBoundsException e){
            province= null;
            return province;
        }

    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {


        try{
            if (student.getAddress() != null && district != null) {
                district = student.getAddress().split("[,][ ]")[1];
                setWards();
            }
            return district;
        }catch (ArrayIndexOutOfBoundsException e){
            district= null;
            return district;
        }



    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {

        try{
            if (student.getAddress() != null && ward != null)
                ward = student.getAddress().split("[,][ ]")[0];
            return ward;
        }catch (ArrayIndexOutOfBoundsException e){
            ward= null;
            return ward;
        }





    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public EditStudentMB getEditStudentMB() {
        return editStudentMB;
    }

    public void setEditStudentMB(EditStudentMB editStudentMB) {
        this.editStudentMB = editStudentMB;
    }

    public Student getStuUser(String user) {
        setStudent(userDAO.findByName(user).getStudent());
        return student;
    }

    public Student getStudent() {
        if (student.getAddress() != null) {
            String[] address = student.getAddress().split("[,][ ]");
            if (address.length == 3) {
                province = address[2];
                district = address[1];
                ward = address[0];
            }
        }
        return student != null ? student : new Student();
    }

    public void setStudent(Student student) {

        this.student = student;
    }

    public void setListStudent(List<Student> listStudent) {
        this.listStudent = listStudent;
    }

    public Map<String, String> getClasses() {
        if (student.getClassPayroll() != null)
            studentClass = student.getClassPayroll().getName();
        return classes;
    }

    public void setClasses(Map<String, String> classes) {
        this.classes = classes;
    }

    public void setDistricts(Map<String, String> districts) {
        this.districts = districts;
    }

    public void setWards(Map<String, String> wards) {
        this.wards = wards;
    }

    public ClassDAO getClassesDAO() {
        return classesDAO;
    }

    public void setClassesDAO(ClassDAO classesDAO) {
        this.classesDAO = classesDAO;
    }

    public Map<String, Map<String, String>> getProvinceDistric() {
        return provinceDistric;
    }

    public void setProvinceDistric(Map<String, Map<String, String>> provinceDistric) {
        this.provinceDistric = provinceDistric;
    }

    public Map<String, Map<String, String>> getDistricWard() {
        return districWard;
    }

    public void setDistricWard(Map<String, Map<String, String>> districWard) {
        this.districWard = districWard;
    }

    public Map<String, String> getProvinces() {
        return provinces;
    }

    public void setProvinces(Map<String, String> provinces) {
        this.provinces = provinces;
    }

    public Map<String, String> getDistricts() {
        return districts;
    }

    public Map<String, String> getWards() {
        return wards;
    }

    @PostConstruct
    public void setWards() {
        wards = districWard.get(district);
    }

    public AddressDAO getAddressDAO() {
        return addressDAO;
    }

    public void setAddressDAO(AddressDAO addressDAO) {
        this.addressDAO = addressDAO;
    }

}
