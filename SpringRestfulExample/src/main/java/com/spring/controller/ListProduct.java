package com.spring.controller;
 
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.bl.dao.Product;
import com.spring.bl.dao.UserMgr;

@Controller
public class ListProduct {
 
	public static final String STUDENTS_VIEW_KEY = "studentsView";
 
	@Resource  
    private UserMgr productMgr; 
	
	//Devuelve la lita de estudiantes en XML
	@RequestMapping(method=RequestMethod.GET, value="/students")
	public String showProducts() {
		List<Product> products = productMgr.getProducts();
		 return "hol–a";
		
	}
	

	
}