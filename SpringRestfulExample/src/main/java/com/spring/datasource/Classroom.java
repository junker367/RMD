package com.spring.datasource;
 
import java.util.ArrayList;
 
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
 
/**
 * @author Eggsy - eggsy_at_eggsylife.co.uk
 *
 */
@XmlRootElement(name = "class")
public class Classroom {
 
	private String classId = null;
	private ArrayList<Student> students = null;
 
	public Classroom() {
 
	}
 
	public Classroom(String id) {
		this.classId = id;
		students = new ArrayList<Student>();
		Student studentOne = new Student(1, "Student One");
		Student studentTwo = new Student(2, "Student Two");
		students.add(studentOne);
		students.add(studentTwo);
	}
 
	/**
	 * @return the classId
	 */
	public String getClassId() {
		return classId;
	}
 
	/**
	 * @param classId the classId to set
	 */
	public void setClassId(String classId) {
		this.classId = classId;
	}
 
	/**
	 * @return the students
	 */
	@XmlElement(name="student")
	public ArrayList<Student> getStudents() {
		return students;
	}
 
	/**
	 * @param students the students to set
	 */
	public void setStudents(ArrayList<Student> students) {
		this.students = students;
	}
}