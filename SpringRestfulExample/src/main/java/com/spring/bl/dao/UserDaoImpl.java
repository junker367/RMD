package com.spring.bl.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl extends SpringHibernateDao{

  public UserDaoImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
    this.sessionFactory=sessionFactory;
  }

  @Autowired
  private SessionFactory sessionFactory;

  public List listUser(String mailUser) {
    String query = "";
    query += "FROM User as u WHERE u.mail=" + mailUser;
    List listaUsuarios = sessionFactory.getCurrentSession().createQuery(query).list();
    return listaUsuarios;
  }

}
