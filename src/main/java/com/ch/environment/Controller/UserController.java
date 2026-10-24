package com.ch.environment.Controller;

import com.ch.environment.Entity.User;
import com.ch.environment.Mapper.UserMapper;
import com.ch.environment.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
//标识代码页面是控制器
@Controller
//所有用到下面的接口的
@RequestMapping("/user")
public class UserController {
    //自动生成实例化
    @Autowired
    private UserService userService;
    private UserMapper userMapper;
    @GetMapping("/")
        public String tologin(@ModelAttribute User user){
            return "login";
        }

    @GetMapping("/search")
    public String toSearch(@ModelAttribute User user){

        return "search";
    }
    @GetMapping("/detail")
    public String toDetail(Integer userid,Model model,HttpSession session){
        userService.updateRankByPoints();
        return userService.toDetail(userid,model,session);
    }
    @PostMapping("/selectUserMessage")
    public String selectUserMessage(String userid, Model model,HttpSession session){
        return userService.getUserById(userid,model,session);
   }
    @GetMapping("/login")
    public String login(@ModelAttribute User user,  Model model,HttpSession session){
        model.addAttribute("user",new User());
        return userService.login(user,model,session);
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 使当前会话失效
        return "login"; // 重定向到登录页
    }
    @GetMapping("/register")
    public String register(User user){
        return userService.register(user);
    }

}
