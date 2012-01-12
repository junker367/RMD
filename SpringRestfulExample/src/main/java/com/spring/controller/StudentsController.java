package com.spring.controller;
 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.spring.datasource.Classroom;

@Controller
public class StudentsController {
 
	public static final String STUDENTS_VIEW_KEY = "studentsView";
 
	@RequestMapping(method=RequestMethod.GET,value="/students")
	public ModelAndView showStudentsPage() {
		Classroom classOfStudents = new Classroom("Class One");
		ModelAndView mav = new ModelAndView(STUDENTS_VIEW_KEY);
		mav.addObject("classRoom", classOfStudents);
		return mav;
	}
	
	@RequestMapping(value="/refresh/{ip}", method=RequestMethod.GET)
	public ModelAndView getBooking(@PathVariable("ip") String ip, Model model) {
	    System.out.println("ip:"+ip);
	    Classroom classOfStudents = new Classroom("Class One");
		ModelAndView mav = new ModelAndView(STUDENTS_VIEW_KEY);
		mav.addObject("classRoom", classOfStudents);
		return mav;
	}

}