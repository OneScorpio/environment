package com.ch.environment.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.environment.Entity.Projectapplication;
import com.ch.environment.Entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface UserService extends IService<User> {
    String login(User user, Model model, HttpSession session);
    String getUserById(String username,Model model,HttpSession session);
    String toDetail(Integer userid, Model model,HttpSession session);
    String register(User user);
    void updateRankByPoints();
}
