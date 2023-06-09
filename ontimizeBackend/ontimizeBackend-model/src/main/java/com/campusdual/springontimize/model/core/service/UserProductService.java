package com.campusdual.springontimize.model.core.service;


import com.campusdual.springontimize.api.core.service.IUserProductService;
import com.campusdual.springontimize.api.core.service.IUserRoleService;
import com.campusdual.springontimize.api.core.service.IUserService;
import com.campusdual.springontimize.model.core.dao.UserDao;
import com.campusdual.springontimize.model.core.dao.UserProductDao;
import com.campusdual.springontimize.model.core.dao.UserRoleDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Lazy
@Service("UserProductService")
public class UserProductService implements IUserProductService {

    @Autowired
    private UserProductDao userProductDao;
    @Autowired
    private DefaultOntimizeDaoHelper daoHelper;


    private String getUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }


    @Override
    public EntityResult userProductQuery(Map<Object, String> keyMap, List<?> attrList) {

        keyMap.put("user_id",getUser());
         EntityResult result =  this.daoHelper.query(userProductDao, keyMap, attrList);

         return result;
    }


    @Override
    public EntityResult userAllProductsQuery(Map<Object, String> keyMap, List<?> attrList) {

        EntityResult result =  this.daoHelper.query(userProductDao, keyMap, attrList);

        return result;
    }




    @Override
    public EntityResult userProductInsert(Map<?, ?> attrMap) {
        return this.daoHelper.insert(userProductDao, attrMap);
    }

    @Override
    public EntityResult userProductUpdate(Map<?, ?> attrMap, Map<?, ?> keyMap) {
        return this.daoHelper.update(userProductDao, attrMap, keyMap);
    }

    @Override
    public EntityResult userProductDelete(Map<?, ?> keyMap) {
        return this.daoHelper.delete(this.userProductDao, keyMap);
    }
}
