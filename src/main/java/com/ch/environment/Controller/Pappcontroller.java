package com.ch.environment.Controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ch.environment.Entity.Projectapplication;
import com.ch.environment.Entity.Urank;
import com.ch.environment.Entity.User;
import com.ch.environment.Mapper.PappMapper;
import com.ch.environment.Mapper.UserMapper;
import com.ch.environment.Service.PappService;
import com.ch.environment.Service.PappServiceimpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/app")
public class Pappcontroller {
    @Autowired
    PappService pappService;
    @Autowired
    PappMapper pappMapper;
    @Autowired
    UserMapper userMapper;
//    @GetMapping("/rank")
//    public String toRank(){
//        return //todo
//    }
    @GetMapping("/toAdd")
    public String toAdd(@ModelAttribute Projectapplication pro, HttpSession session, Model model){
        User user = (User) session.getAttribute("userlogin") ;
//        User user = userMapper.selectById(pro.getUserid());
        if (user == null){
            return "login";
        }
        model.addAttribute("username",user.getUsername());
        model.addAttribute("userid",user.getUserid());
//        model.addAttribute("projectapplication",new Projectapplication());
                return "add";
    }
    @GetMapping("/toRank")
    public String toRank(@RequestParam(defaultValue = "1")int page,
                         @RequestParam(defaultValue = "10")int size,
                         Model model,
                         HttpSession session){
        User user = (User) session.getAttribute("userlogin") ;
        Page<Urank> rankPage = pappService.getRankPage(page,size);

        Map<String,Object> pageInfo = new HashMap<>();
        pageInfo.put("current",rankPage.getCurrent());
        pageInfo.put("pages",rankPage.getPages());
        pageInfo.put("total",rankPage.getTotal());
        //计算范围
        int current = (int) rankPage.getCurrent();
        int pages = (int) rankPage.getPages();
        int start =  Math.max(1, current - 2);
        int end = Math.min(pages,current + 2);
        model.addAttribute("currentPage",current);
        model.addAttribute("totalPages",pages);
        model.addAttribute("rankPage",rankPage);
        model.addAttribute("pageRange", IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList()));
        model.addAttribute("userid",user.getUserid());
        return "rank";
    }
    @PostMapping("/add")
    public String add(@ModelAttribute Projectapplication pro, HttpServletRequest request, HttpSession session,String act,Model model){
        User user = (User) session.getAttribute("userlogin") ;
        pro.setUserid(user.getUserid());
        pro.setUsername(user.getUsername());
        return pappService.add(pro,request,act,session,model);
    }
    @GetMapping("/tohistory")
    public String tohistory(@RequestParam(required = false) String projectname, HttpSession session ,Model model) {
        User user = (User) session.getAttribute("userlogin");
        List<Projectapplication> projectList;
        if (projectname != null && !projectname.isEmpty()){
             projectList  = pappService.getProjectsByProjectname(projectname);
        }else {
            projectList = pappService.getProjectsByUser(user.getUserid());
        }
        model.addAttribute("projectList", projectList);
        model.addAttribute("userid",user.getUserid());
        return "history";
    }
    @DeleteMapping("/deleteRecord/{projectid}")
    @ResponseBody
    public boolean deleteRecord(@PathVariable Integer projectid) {
        return pappService.deleteProjectById(projectid);
    }
    @GetMapping("/toEdit")
    public String toEdit(@RequestParam("projectid") Integer projectid, Model model,HttpSession session) {
        User user = (User) session.getAttribute("userlogin");
        Projectapplication project = pappService.getProjectById(projectid);
        model.addAttribute("project", project);
        model.addAttribute("userid",user.getUserid());
        return "edit";
    }
    @PostMapping("/edit")
    public String edit(Projectapplication project) {
        pappService.updateProject(project);
        return "redirect:/app/tohistory";
    }

}
