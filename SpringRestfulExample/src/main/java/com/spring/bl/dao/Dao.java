package com.spring.bl.dao;

import java.io.Serializable;
import java.util.List;

public interface Dao {

  public void persist(Object entity);

  public void persist(Object[] entities);

  public <T> List<T> find(Class<T> entityClass);

  public <T> T load(Class<T> entityClass, Serializable id);

  public <T> List<T> find(String hql);

}