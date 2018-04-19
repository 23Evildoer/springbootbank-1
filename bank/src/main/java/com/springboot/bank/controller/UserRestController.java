package com.springboot.bank.controller;

import javax.servlet.http.HttpServletRequest;

import com.springboot.bank.domain.User;
import com.springboot.bank.security.JwtTokenUtil;
import com.springboot.bank.security.domain.JwtUser;
import com.springboot.bank.service.Userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 获取已授权用户信息
 *
 * @author SONG
 */
@RestController
@RequestMapping("/api")
public class UserRestController {

  @Value("${jwt.header}")
  private String tokenHeader;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  @Qualifier("jwtUserDetailsService")
  private UserDetailsService userDetailsService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private Userservice userservice;

  /**
   *  用户登录
   * @param request
   * @return
   */
  @RequestMapping(value = "/user", method = RequestMethod.GET)
  public JwtUser getAuthenticatedUser(
          HttpServletRequest request
  ) {
    String token = request.getHeader(tokenHeader).substring(7);
    String username = jwtTokenUtil.getUsernameFromToken(token);
    JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
    return user;
  }

  /**
   *  修改密码
   * @param password
   * @param request
   * @return
   */
  @RequestMapping(value = "changepassword", method = RequestMethod.POST)
  public ResponseEntity<?> changepassword(
          @RequestParam("password") String password,
          HttpServletRequest request
  ){
    System.out.println("加密前密码："+password);
    password = passwordEncoder.encode(password);
    System.out.println("加密后密码："+password);

    //  三句代码产生令牌
    String token = request.getHeader(tokenHeader).substring(7);
    String username = jwtTokenUtil.getUsernameFromToken(token);
    JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

    System.out.println("id:"+user.getId());
    //int count =1;
    int count = userservice.changePassword(user.getId(),password);
    return ResponseEntity.ok(count);
  }


  // ----------------------------------Restful-----------------------------------------

  /**
   *  查询
   * @return
   */
  @RequestMapping(value = "/users",method = RequestMethod.GET)
  public ResponseEntity<?> getUsers(){
    List<User> users = userservice.find();
    return  new ResponseEntity<>(users, HttpStatus.OK);
  }

  /**
   *  根据 ID
   * @param id
   * @return
   */
  @RequestMapping(value = "/users/{id}",method = RequestMethod.GET)
  public ResponseEntity<?> getUser(
          @PathVariable("id") Integer id
  ){
    User user = userservice.find(id);
    return new ResponseEntity<>(user,HttpStatus.OK);
  }

  /**
   *  新增
   * @param user
   * @return
   */
  @RequestMapping(value = "/users", method = RequestMethod.POST)
  public ResponseEntity<?> add(@RequestBody User user) {
    // 密码加密
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    // 设置默认日期
    user.setLastPasswordResetDate(new Date());
    user.setLoginDate(new Date());

    System.out.println("加密的密码："+user.getPassword());

    int count = userservice.add(user);
    return ResponseEntity.ok(count);
  }

  /**
   *  修改
   * @param user
   * @return
   */
  @RequestMapping(value = "/users",method = RequestMethod.PUT)
  public ResponseEntity<?> modify(
          @RequestBody User user
  ){
    int count = userservice.modify(user);
    return ResponseEntity.ok(user);
  }

  /**
   *  授权
   * @param userId
   * @param authorityId
   * @return
   */
  @PostMapping("/UserAuthority")
  public ResponseEntity<?> addUserAuthority(
          @RequestParam("userId")Integer userId,
          @RequestParam("authorityId")String authorityId
  ){
    System.out.println("接受的userId是："+userId+" "+"接受的authorityId是："+authorityId);
    int count =0;
    String[] strbox = authorityId.split(","); // 把传过来的字符串打散成一个数组
    System.out.println(Arrays.toString(strbox)+"\n"+strbox.length);
    // 遍历数组 转化数据类型时 判断有没有空指针
    for(String s:strbox){
      if(s!=null || !s.equals("")){
        count += userservice.addUserAuthority(userId,Integer.parseInt(s));
      }
    }
    return ResponseEntity.ok(count);
  }
}
