package com.springboot.bank.mapper;

import com.springboot.bank.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.security.core.parameters.P;

import java.util.Date;
import java.util.List;

/**
 * @author SONG
 */
public interface UserMapper {

  User findByUsername(@Param("username") String username);

  /**
   *  修改密码
   * @param id
   * @param password
   * @return
   */
  @Update("update user set password=#{password},last_password_reset_date=now() where id=#{id}")
  int changePassword(@Param("id")Integer id,
                     @Param("password")String password);

  /**
   *  查询
   * @return
   */
  @Select("select id,username,email,enabled,last_password_reset_date lastPasswordResetDate,login_time loginDate from user")
  List<User> find();

  /**
   *  根据 ID 查询
   * @param id
   * @return
   */
  @Select("select id,username,email,enabled,last_password_reset_date lastPasswordResetDate,login_time,loginDate from user where id=#{id}")
  User findById(Integer id);

  /**
   *  新增
   * @param user
   * @return
   */
  @Insert("insert into user(username,password,email,enabled,last_password_reset_date,login_time) values" +
          "(#{username},#{password},#{email},#{enabled},#{lastPasswordResetDate},#{loginDate})")
  int add(User user);

  /**
   *  修改
   * @param user
   * @return
   */
  @Update("update user set email=#{email},enabled=#{enabled} where id=#{id}")
  int modify(User user);

  /**
   *  授权
   * @param userId
   * @param authorityId
   * @return
   */
  @Insert("insert into user_authority(user_id,authority_id) values (#{userId},#{authorityId})")
  int addUserAuthority(
          @Param("userId")Integer userId,
          @Param("authorityId")Integer authorityId
  );


}
