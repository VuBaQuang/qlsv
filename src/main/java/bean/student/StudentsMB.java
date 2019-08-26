package bean.student;

import bean.Class.InfoClassMB;
import dao.AddressDAO;
import dao.ClassesDAO;
import dao.StudentsDAO;
import model.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.*;

@ManagedBean
@SessionScoped
public class StudentsMB implements Serializable {
    private Students student = new Students();
    private List<Students> listStudent = new ArrayList<>();
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
    private ClassesDAO classesDAO = new ClassesDAO();
    String redirect = "student";

    public void rsStu() {
        student = new Students();
        province=null;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    @ManagedProperty(value = "#{editStudentMB}")
    private EditStudentMB editStudentMB;


    public List<Students> getListStudent() {
        StudentsDAO dao = new StudentsDAO();
        return dao.findAll();
    }

    public String addStudent(String url) {

        StudentsDAO dao = new StudentsDAO();
        StringBuilder address = new StringBuilder();
        address.append(ward);
        address.append(", ");
        address.append(district);
        address.append(", ");
        address.append(province);
        student.setAddress(address.toString());
        Classes classes = classesDAO.getClassByName(studentClass);
        student.setClasses(classes);
        student = dao.create(student);
        return url + "?faces-redirect=true";
    }

    public void delete(Students student) {
        StudentsDAO dao = new StudentsDAO();
        dao.delete(student);
    }

    public String edit(Students student) {
        String[] address = student.getAddress().split("[,][ ]");
        province = address[2];
        district = address[1];
        ward = address[0];
        editStudentMB.setStudentClass(student.getClasses() != null ? student.getClasses().getName() : "");
        editStudentMB.setStudent(student);
        editStudentMB.setProvince(province);
        editStudentMB.setDistrict(district);
        editStudentMB.setWard(ward);
        return "update-student";
    }

    @PostConstruct
    public void init() {
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
        for (Classes classes : classesDAO.findAll()) {
            this.classes.put(classes.getName(), classes.getName());
        }
    }

    @PostConstruct
    public void setDistricts() {
        districts = provinceDistric.get(province);
        wards = new LinkedHashMap<>();
    }

    public String getProvince() {
        return province;
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
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
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

    public Students getStudent() {
        return student;
    }

    public void setStudent(Students student) {

        this.student = student;
    }

    public void setListStudent(List<Students> listStudent) {
        this.listStudent = listStudent;
    }

    public Map<String, String> getClasses() {
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

    public ClassesDAO getClassesDAO() {
        return classesDAO;
    }

    public void setClassesDAO(ClassesDAO classesDAO) {
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
