package com.ch.environment.Controller;

import com.ch.environment.Entity.Admin;
import com.ch.environment.Entity.Projectapplication;
import com.ch.environment.Entity.User;
import com.ch.environment.Service.AdminService;
import com.ch.environment.Service.PappService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;
    @Autowired
    PappService pappService;

    @GetMapping("/adLogin")
    public String AdLogin(@ModelAttribute Admin admin){
        return "adLogin";
    }
    @GetMapping("/adLoginVerify")
    public String toAdLogin(@ModelAttribute Admin admin, Model model, HttpSession session){
        model.addAttribute("admin",new Admin());
        return adminService.adLogin(admin,model,session);
    }
    @GetMapping("/adDetail")
    public String toAdDetail(Integer adminid, Model model, HttpSession session){
        return adminService.toAdDetail(adminid,model,session);
    }
    @GetMapping("/toEdit")
    public String toEdit(@RequestParam("projectid") Integer projectid, Model model, HttpSession session) {
//        User user = (User) session.getAttribute("userlogin");
        Admin admin = (Admin) session.getAttribute("adminlogin");
        Projectapplication project = pappService.getProjectById(projectid);
        model.addAttribute("project", project);
        model.addAttribute("userid",project.getUserid());
        model.addAttribute("adminid",admin.getAdminid());
        return "adEdit";
    }
    @PostMapping("/edit")
    public String edit(Projectapplication project) {
        pappService.updateProject(project);
        return "redirect:/admin/adDetail";
    }
}
