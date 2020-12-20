package cn.edu.ncu.bootwebsocketmybatis.controller;

import cn.edu.ncu.bootwebsocketmybatis.entity.User;
import cn.edu.ncu.bootwebsocketmybatis.entity.UserInfo;
import cn.edu.ncu.bootwebsocketmybatis.service.UserInfoServiceImpl;
import cn.edu.ncu.bootwebsocketmybatis.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;


/**
 * @auther: Liu Zedi.
 * @date: Create in 2018/12/18  23:06
 * @package: cn.edu.ncu.bootwebsocketmybatis.controller
 * @project: boot-websocket-mybatis
 */
@Controller
public class indexController {

    private final Logger logger =
            LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserInfoServiceImpl userInfoService;

    /**
     * @return
     * @description 登陆和注册界面
     */
    @GetMapping("")
    public String login() {
        return "register";
    }

    @GetMapping("/chart")
    public String chart() {
        return "chart";
    }

    /**
     * @return
     * @description 注册界面
     */
    @RequestMapping("/register")
    public ModelAndView register() {
        return new ModelAndView("/register");
    }


    /**
     * 处理登陆事务
     *
     * @param userId
     * @param password
     * @param request
     * @return
     * @throws UnknownHostException
     */
    @PostMapping("/lo")
    public String login(String userId, String password, HttpServletRequest request, Model model) throws UnknownHostException {
        if (userService.login(new User(userId, password))) {
            User user = userService.findById(userId);
            model.addAttribute("user", user);
            return "chatRoom";
        } else {
            model.addAttribute("id", "帐号或密码错误");
            return "register";
        }
    }

    /**
     * 处理注册事务
     *
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/re")
    @ResponseBody
    public String register(String username, String password) {

        User user = userService.register(new User("", username, password));
        if (user != null) {         //注册成功
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(user.getId());
            userInfoService.addUserInfo(userInfo);
            return user.getId();
        } else {
            return "失败";
        }
    }

}
