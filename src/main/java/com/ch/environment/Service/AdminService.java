package com.ch.environment.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.environment.Entity.Admin;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface AdminService extends IService<Admin> {
    String adLogin(Admin admin, Model model, HttpSession session);
    String toAdDetail(Integer adminid, Model model, HttpSession session);
}
