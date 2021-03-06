package model;
// Generated Aug 27, 2019 2:14:58 PM by Hibernate Tools 5.1.10.Final

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Subject generated by hbm2java
 */
@Entity
@Table(name = "subject", catalog = "qlsv", uniqueConstraints = @UniqueConstraint(columnNames = "code"))
public class Subject implements java.io.Serializable {

	private Integer id;
	private String code;
	private Integer coefficient;
	private String note;
	private String name;
	private Set<ClassSubject> classSubjects = new HashSet<ClassSubject>(0);

	public Subject() {
	}

	public Subject(String code, Integer coefficient, String note, String name, Set<ClassSubject> classSubjects) {
		this.code = code;
		this.coefficient = coefficient;
		this.note = note;
		this.name = name;
		this.classSubjects = classSubjects;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "code", unique = true, length = 6)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "coefficient", precision = 255, scale = 0)
	public Integer getCoefficient() {
		return this.coefficient;
	}

	public void setCoefficient(Integer coefficient) {
		this.coefficient = coefficient;
	}

	@Column(name = "note", length = 16777215)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "name", length = 16777215)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "subject")
	public Set<ClassSubject> getClassSubjects() {
		return this.classSubjects;
	}

	public void setClassSubjects(Set<ClassSubject> classSubjects) {
		this.classSubjects = classSubjects;
	}

}
