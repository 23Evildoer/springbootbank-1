package com.springboot.bank.service;

import com.springboot.bank.domain.User;
import com.springboot.bank.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true,propagation = Propagation.NOT_SUPPORTED)
public class Userservice {
    @Autowired
    private UserMapper userMapper;

    /**
     *  修改密码
     * @param id
     * @param password
     * @return
     */
    public int changePassword(Integer id,String password){
        return userMapper.changePassword(id,password);
    }

    /**
     *  查询
     * @return
     */
    public List<User>find(){
        return userMapper.find();
    }

    /**
     *  根据 ID
     * @param id
     * @return
     */
    public User find(Integer id){
        return  userMapper.findById(id);
    }

    /**
     *  新增
     * @param user
     * @return
     */
    public int add(User user){
        return userMapper.add(user);
    }

    /**
     *  修改
     * @param user
     * @return
     */
    public int modify(User user){
        return userMapper.modify(user);
    }

    /**
     *  授权
     * @param userId
     * @param authorityId
     * @return
     */
    public int addUserAuthority(
            Integer userId,
            Integer authorityId){
        return  userMapper.addUserAuthority(userId,authorityId);
    }
}
