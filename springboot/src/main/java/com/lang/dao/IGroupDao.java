package com.lang.dao;

import java.util.List;
import java.util.Map;

/**
 * group表示一个"群",比如锦江的会员、集团、pms所有的服务器即构成一个"群";再比如外挂上面有很多酒店集团，他们也构成一个"群" 
 * 
 * @author Administrator
 */
public interface IGroupDao {

    Map<String, Object> findByUUID(String uuid);

    List<Map<String, Object>> listAll();

    void insert(String uuid);
}
