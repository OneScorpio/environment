package com.ch.environment.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.environment.Entity.Admin;
import com.ch.environment.Entity.Projectapplication;
import com.ch.environment.Mapper.AdminMapper;
import com.ch.environment.Mapper.PappMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;

@Service
public class AdminServiceimpl extends ServiceImpl<AdminMapper , Admin> implements AdminService{
    @Autowired
    AdminMapper adminMapper;
    @Autowired
    private PappMapper pappMapper;

    @Override
    public String adLogin(Admin admin, Model model , HttpSession session){
        admin.setPassword(admin.getPassword());
        Admin admin1 = query().eq("adminid",admin.getAdminid())
                .eq("password",admin.getPassword()).one();
        if (admin1 != null){
            session.setAttribute("adminlogin",admin1);
            model.addAttribute("admin",admin1);
            return "forward:/admin/adDetail";
        }else {
            model.addAttribute("msg","用户名或密码错误");
            return "adLogin";
        }
        }
    @Override
    public String toAdDetail(Integer adminid, Model model, HttpSession session){
        Admin admin1 = (Admin) session.getAttribute("adminlogin");
        if (adminid == null){
            adminid = admin1.getAdminid();
        }

        Admin admin = adminMapper.selectById(adminid);
        List<Projectapplication> projects = pappMapper.selectList(new QueryWrapper<Projectapplication>().orderByDesc("createtime"));
        model.addAttribute("admin",admin);
        model.addAttribute("projects",projects);
        return "adDetail";
    }


    }

