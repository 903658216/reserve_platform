package com.jh.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jh.entity.ResultData;
import com.jh.entity.User;
import com.jh.service.IUserService;
import com.jh.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private IUserService iUserService;


    /**
     * 用户注册功能
     * @param user
     * @return
     */
    @RequestMapping("/register")
    public ResultData<Integer> register(User user){

        try {
            boolean save = iUserService.save(user);
            return new ResultData<Integer>().setData(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResultData<Integer>().setCode(ResultData.Code.ERROR).setMsg("该用户名已存在！");
    }

    /**
     * 登录功能
     * @param username
     * @param password
     * @param devId
     * @return
     */
    @RequestMapping("/login")
    public ResultData<String> login(String username,String password,String devId){

        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .eq("username",username)
                .eq("password",password);
        User user = iUserService.getOne(queryWrapper);
        if (user != null){
            //登录成功
            //TODO
            //登录成功
            String jwtToken = JWTUtil.createJwtToken(user, devId);
            return new ResultData<String>().setData(jwtToken);
        }
        //登录失败
        return new ResultData<String>().setCode(ResultData.Code.ERROR).setData(null).setMsg("用户名或密码错误");
    }

}
