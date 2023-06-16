package com.campusdual.springontimize.model.core.service;


import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import com.campusdual.springontimize.model.core.dao.UserProductDao;
import com.campusdual.springontimize.model.core.dao.UserRoleDao;
import com.ontimize.jee.common.gui.SearchValue;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.campusdual.springontimize.api.core.service.IUserService;
import com.campusdual.springontimize.model.core.dao.UserDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;


@Lazy
@Service("UserService")
public class UserService implements IUserService {

	@Autowired
	private UserProductDao userProductDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserRoleDao userRoleDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	public void loginQuery(Map<?, ?> key, List<?> attr) {
	}

	//Sample to permission
	//@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult userQuery(Map<?, ?> keyMap, List<?> attrList) {
		return this.daoHelper.query(userDao, keyMap, attrList);
	}



	public EntityResult partnerQuery(Map<?, ?> keyMap, List<?> attrList) {

		EntityResult partnerResult = this.daoHelper.query(userDao, keyMap, attrList, "partners");

		return partnerResult;
	}

	@Override
	public EntityResult partnerAvailableQuery(Map<String, Object> keyMap, List<String> attrList) {
		List<String> users = null;
		if(keyMap.get(UserProductDao.ATTR_PRODUCT_ID)!=null){
			Map <String, Object> keys = new HashMap<>();
			List<String> attr = new ArrayList<>();
			attr.add(UserProductDao.ATTR_USER_ID);
			keys.put(UserProductDao.ATTR_PRODUCT_ID,keyMap.get(UserProductDao.ATTR_PRODUCT_ID));
			EntityResult partnersRelations = this.daoHelper.query(userProductDao,keys,attr);
			if(partnersRelations.isWrong()){
				return partnersRelations;
			}
			if(!partnersRelations.isEmpty()){
				users = new ArrayList<>();
				for(int i =0; i<partnersRelations.calculateRecordNumber();i++){
					users.add((String) partnersRelations.getRecordValues(i).get(UserProductDao.ATTR_USER_ID));
				}
			}
		}
		if(users!=null){
			keyMap.put(UserDao.id,new SearchValue(SearchValue.NOT_IN,users));
		}
		EntityResult partnerAvailableResult = this.daoHelper.query(userDao, keyMap, attrList, "partners");
		return partnerAvailableResult;
	}



	public EntityResult userInsert(Map<?, ?> attrMap) {

		EntityResult insertUserResult = this.daoHelper.insert(userDao, attrMap);

		if(!insertUserResult.isWrong()){
			Map<String, Object> attrToInsert = new HashMap<>();
			attrToInsert.put(UserRoleDao.id_rolename,attrMap.get("rol"));
			attrToInsert.put(UserRoleDao.user_,attrMap.get("user_"));

		    return this.daoHelper.insert(userRoleDao,attrToInsert);

		}else{
			return insertUserResult;
		}
	}

	public EntityResult userUpdate(Map<?, ?> attrMap, Map<?, ?> keyMap) {
		return this.daoHelper.update(userDao, attrMap, keyMap);
	}

	public EntityResult userDelete(Map<?, ?> keyMap) {
		return this.daoHelper.delete(this.userDao, keyMap);
	}

}
