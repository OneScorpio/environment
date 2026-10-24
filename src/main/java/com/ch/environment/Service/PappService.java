package com.ch.environment.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.environment.Entity.Projectapplication;
import com.ch.environment.Entity.Urank;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.List;


public interface PappService extends IService<Projectapplication> {
    String add(Projectapplication pro, HttpServletRequest request, String act, HttpSession session, Model model);
    Page<Urank> getRankPage(int current, int size);
    List<Projectapplication> getProjectsByUser(Integer userid);
    boolean deleteProjectById(Integer projectid);
    Projectapplication getProjectById(Integer projectid);
    void updateProject(Projectapplication project);
    List<Projectapplication> getProjectsByProjectname(String projectname);
}
