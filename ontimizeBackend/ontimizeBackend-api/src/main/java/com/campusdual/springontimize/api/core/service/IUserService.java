package com.campusdual.springontimize.api.core.service;


import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;


public interface IUserService {

	public EntityResult userQuery(Map<?, ?> keyMap, List<?> attrList);

	public EntityResult userInsert(Map<?, ?> attrMap);

	public EntityResult userUpdate(Map<?, ?> attrMap, Map<?, ?> keyMap);

	public EntityResult userDelete(Map<?, ?> keyMap);

	public EntityResult partnerQuery(Map<?, ?> keyMap, List<?> attrList);
	public EntityResult partnerAvailableQuery(Map<String, Object> keyMap, List<String> attrList);




}
