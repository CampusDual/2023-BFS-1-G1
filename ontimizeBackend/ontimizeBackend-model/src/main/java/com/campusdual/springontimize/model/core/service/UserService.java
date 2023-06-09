package com.campusdual.springontimize.model.core.service;


import java.util.*;

import com.campusdual.springontimize.model.core.dao.UserProductDao;
import com.campusdual.springontimize.model.core.dao.UserRoleDao;
import com.ontimize.jee.common.gui.SearchValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

	private String getUser(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getName();
	}

	public void loginQuery(Map<?, ?> key, List<?> attr) {
		// TODO document why this method is empty
	}

	//Sample to permission
	//@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult userQuery(Map<String, Object> keyMap, List<String> attrList) {
		return this.daoHelper.query(userDao, keyMap, attrList);
	}

	@Override
	public EntityResult myUserQuery(Map<Object, String> keyMap, List<String> attrList) {

		keyMap.put("user_",getUser());
		return this.daoHelper.query(userDao, keyMap, attrList);
	}



	public EntityResult partnerQuery(Map<String, Object> keyMap, List<String> attrList) {

		return this.daoHelper.query(userDao, keyMap, attrList, "partners");
	}

	public EntityResult partnerProductQuery(Map<String, Object> keyMap, List<String> attrList) {

		return this.daoHelper.query(userDao, keyMap, attrList, "partnerProduct");
	}


	public EntityResult adminQuery(Map<String, Object> keyMap, List<String> attrList) {

		return this.daoHelper.query(userDao, keyMap, attrList, "admins");
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
		return this.daoHelper.query(userDao, keyMap, attrList, "partners");
	}



	public EntityResult userInsert(Map<String, Object> attrMap) {

		EntityResult insertUserResult = this.daoHelper.insert(userDao, attrMap);

		if(!insertUserResult.isWrong()){
			Map<String, Object> attrToInsert = new HashMap<>();
			attrToInsert.put(UserRoleDao.id_rolename,attrMap.get("rol"));
			attrToInsert.put(UserRoleDao.user_,attrMap.get("user_"));


		   EntityResult insertRol  = this.daoHelper.insert(userRoleDao,attrToInsert);

			if(!insertRol.isWrong()){

				String products = (String) attrMap.get("productList");

				if (products!=null && !products.trim().isEmpty()){

					String [] productList = products.split(",");

					for (String idProduct: productList){

						Map <String, Object> keys = new HashMap<>();
						keys.put("product_id",idProduct);
						keys.put("user_id",attrMap.get("user_"));
						this.daoHelper.insert(userProductDao,keys);

					}
				}
				return insertUserResult;

		   }else{
			   return insertRol;
		   }

		}else{
			return insertUserResult;
		}
	}

	@Override
	public EntityResult newPartnerInsert(Map<String, Object> attrMap) {
		 attrMap.put("rol",2);
		 return this.userInsert(attrMap);
	}

	@Override
	public EntityResult newAdminInsert(Map<String, Object> attrMap) {
		attrMap.put("rol",1);
		return this.userInsert(attrMap);
	}

	@Override
	public EntityResult myUserUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) {
		keyMap.put("user_",getUser());
		return this.daoHelper.update(userDao, attrMap, keyMap);
	}

	public EntityResult userUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) {
		return this.daoHelper.update(userDao, attrMap, keyMap);
	}


	public EntityResult userDelete(Map<String, Object> keyMap) {
		return this.daoHelper.delete(this.userDao, keyMap);
	}

	public EntityResult adminDelete(Map<String, Object> keyMap) {
		return this.daoHelper.delete(this.userDao, keyMap);
	}


	public EntityResult partnerDelete(Map<String, Object> keyMap) {
		return this.daoHelper.delete(this.userDao, keyMap);
	}

}
