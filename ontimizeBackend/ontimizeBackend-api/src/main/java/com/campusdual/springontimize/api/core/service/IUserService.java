package com.campusdual.springontimize.api.core.service;


import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;


public interface IUserService {

	public EntityResult userQuery(Map<String, Object> keyMap, List<String> attrList);

	public EntityResult userInsert(Map<String, Object> attrMap);

	public EntityResult newPartnerInsert(Map<String, Object> attrMap);

	public EntityResult newAdminInsert(Map<String, Object> attrMap);

    EntityResult myUserUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap);

    public EntityResult userUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap);

	public EntityResult userDelete(Map<String,Object> keyMap);

	public EntityResult partnerDelete(Map<String,Object> keyMap);

	public EntityResult adminDelete(Map<String, Object> keyMap);

    EntityResult myUserQuery(Map<Object, String> keyMap, List<String> attrList);

    public EntityResult partnerQuery(Map<String, Object> keyMap, List<String> attrList);

	public EntityResult partnerProductQuery(Map<String, Object> keyMap, List<String> attrList);

	public EntityResult adminQuery(Map<String, Object> keyMap, List<String> attrList);

	public EntityResult partnerAvailableQuery(Map<String, Object> keyMap, List<String> attrList);




}
