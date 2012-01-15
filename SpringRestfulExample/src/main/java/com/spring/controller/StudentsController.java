package com.spring.controller;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.spring.bl.dao.UserDaoImpl;
import com.spring.datasource.Classroom;

@Controller
public class StudentsController {
 
	public static final String STUDENTS_VIEW_KEY = "studentsView";
 
	 private UserDaoImpl userDao;

	  @Autowired
	  public void setPersonDao(UserDaoImpl userDao) {
	    this.userDao = userDao;
	  }
	  
	//Devuelve la lita de estudiantes en XML
	@RequestMapping(method=RequestMethod.GET, value="/students")
	public ModelAndView showStudentsPage() {
	  List lista=userDao.listUser("junker367@gmail.com");
		Classroom classOfStudents = new Classroom("Class One");
		ModelAndView mav = new ModelAndView(STUDENTS_VIEW_KEY);
		mav.addObject("classRoom", classOfStudents);
		return mav;
	}
	
  @RequestMapping(method=RequestMethod.GET, value="/refresh")
  public ModelAndView showStudentsPage1() {
    Classroom classOfStudents = new Classroom("Class One");
    ModelAndView mav = new ModelAndView(STUDENTS_VIEW_KEY);
    mav.addObject("classRoom", classOfStudents);
    return mav;
  }
  
  
	
}