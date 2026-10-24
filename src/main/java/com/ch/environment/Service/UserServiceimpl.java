package com.ch.environment.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.environment.Entity.Projectapplication;
import com.ch.environment.Entity.User;
import com.ch.environment.Entity.Urank;
import com.ch.environment.Entity.Userscorehistory;
import com.ch.environment.Mapper.PappMapper;
import com.ch.environment.Mapper.RankMapper;
import com.ch.environment.Mapper.UhistoryMapper;
import com.ch.environment.Mapper.UserMapper;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceimpl extends ServiceImpl<UserMapper, User> implements UserService{
    private final HttpSession httpSession;
    @Autowired
    private  UserMapper userMapper;
    @Autowired
    private RankMapper rankMapper;
    @Autowired
    private UhistoryMapper uhistoryMapper;
    @Autowired
    private PappMapper pappMapper;

    public UserServiceimpl(HttpSession httpSession, UserMapper userMapper) {
        this.httpSession = httpSession;
        this.userMapper = userMapper;
    }

    @Override
    public String login(User user, Model model, HttpSession session){
        //验证码验证
        String rand = (String)session.getAttribute("rand");
        if(!rand.equalsIgnoreCase(user.getCode())) {
            model.addAttribute("errorMessage", "验证码错误！");
            return "error";
        }
        user.setPassword(user.getPassword());
//        QueryWrapper<User> qu = new QueryWrapper<>();
        User user1 = query().select("*").eq("userid",user.getUserid()).eq("password",user.getPassword()).one();
        if(user1 != null){
            session.setAttribute("userlogin",user1);
            model.addAttribute("user",user1);
            return "forward:/user/detail";
        }else {
            return "error";
        }
    }
    @Override
    public String getUserById(String userid,Model model,HttpSession session){
        User loginuser =(User)session.getAttribute("userlogin");
        if (loginuser == null){
            return "error";
        }
        User user = query().eq("userid",userid).one();
        if (user == null){
            return "error";
        }
        model.addAttribute("user",user);
        return "detail";
    }
    @Override
    public String toDetail(Integer userid, Model model, HttpSession session){

        User user1 = (User)session.getAttribute("user");
        if(userid == null){
            userid = user1.getUserid() ;
        }

        User user =userMapper.selectById(userid);
        Urank urank = rankMapper.selectById(userid);
        Userscorehistory uhistory = uhistoryMapper.selectById(userid);
        List<Projectapplication> papp = pappMapper.selectList(new QueryWrapper<Projectapplication>().eq("userid",userid).orderByDesc("updatetime"));
        long rankcount = uhistoryMapper.selectCount(new QueryWrapper<Userscorehistory>().eq("userid",userid));
        //更新user总分数
        int result1 = userMapper.updateTotalPoint(userid);
        int result2 = rankMapper.updateTotalPoint(userid);
        model.addAttribute("papp",papp);
        model.addAttribute("uhistory",uhistory);
        model.addAttribute("user",user);
        model.addAttribute("urank",urank);
        model.addAttribute("rankcount",rankcount);
        return "detail";
    }

    @Override
    public String register(User user) {
        try {
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                return "register";
            }
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                return "register";
            }
            if (user.getStatus() == null || user.getStatus().trim().isEmpty()) {
                return "register";
            }

            if (user.getClassname() == null || user.getClassname().trim().isEmpty()) {
                return "register";
            }
            if (user.getDepartment() == null || user.getDepartment().trim().isEmpty()) {
                return "register";
            }
            if(save(user)){
                Urank urank = new Urank();
                urank.setUserid(user.getUserid());
                urank.setUsername(user.getUsername());
                urank.setTotalpoint(0);
                urank.setUpdatetime(new Date());
                rankMapper.insert(urank);
                return "login";}

        } catch (Exception e) {

            e.printStackTrace();
        }
        return "register";
    }

    @Override
    public  void updateRankByPoints(){
        //降序排列
        List<Urank> uranks = rankMapper.selectAllByPoint();
        for (int i = 0;i<uranks.size();i++){
            Urank rank = uranks.get(i);
            rank.setRanksort(i+1);
            rank.setUpdatetime(new Date());

        }
        rankMapper.updateRanks(uranks);
    }


}
