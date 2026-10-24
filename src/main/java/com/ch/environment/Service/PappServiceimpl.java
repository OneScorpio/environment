package com.ch.environment.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.environment.Entity.Projectapplication;
import com.ch.environment.Entity.Urank;
import com.ch.environment.Entity.User;
import com.ch.environment.Entity.Userscorehistory;
import com.ch.environment.Mapper.PappMapper;
import com.ch.environment.Mapper.RankMapper;
import com.ch.environment.Mapper.UhistoryMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Slf4j
@Service
public class PappServiceimpl extends ServiceImpl<PappMapper, Projectapplication> implements PappService {
    @Autowired
    private PappMapper pappMapper;
    @Autowired
    private UhistoryMapper uhistoryMapper;
    @Autowired
    private RankMapper rankMapper;

    @Override
    public String add(Projectapplication pro, HttpServletRequest request, String act, HttpSession session, Model model){
        if("add".equals(act)){
            User user = (User) session.getAttribute("userlogin");
            pro.setUserid(user.getUserid());
            pro.setUsername(user.getUsername());
            boolean result = save(pro);
//            log.info("插入数据结果:{},插入数据内容{}",result,pro);
            Userscorehistory userc = new Userscorehistory();
            userc.setUserid(pro.getUserid());
            userc.setUsername(user.getUsername());
            userc.setProjectdesc(pro.getProjectdesc());
            userc.setPoints(pro.getPoints());
            uhistoryMapper.insert(userc);
            if (result){
                QueryWrapper<Projectapplication> queryWrapper =new QueryWrapper<>();
                queryWrapper.eq("userid",user.getUserid()).orderByDesc("createtime").last("limit 1");
                List<Projectapplication> last =list(queryWrapper);
                session.setAttribute("papp",last);
                session.setAttribute("user", user);

                return "redirect:/user/detail";
            }else {
                return "add";
            }
        }
        return "add";
    }

    @Override
    public Page<Urank> getRankPage(int current, int size){
        Page<Urank> page = new Page<>(current, size);
        return rankMapper.selectPage(page,new QueryWrapper<Urank>().orderByDesc("totalpoint").orderByDesc("ranksort"));
    }
    @Override
    public List<Projectapplication> getProjectsByUser(Integer userid) {
        QueryWrapper<Projectapplication> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid", userid);
        return pappMapper.selectList(queryWrapper);
    }

    @Override
    public boolean deleteProjectById(Integer projectid) {
        return removeById(projectid);
    }
    @Override
    public Projectapplication getProjectById(Integer projectid) {
        return getById(projectid);
    }

    @Override
    public void updateProject(Projectapplication project) {
        updateById(project);
    }
    @Override
    public List<Projectapplication> getProjectsByProjectname(String projectname) {
        QueryWrapper<Projectapplication> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("projectname", projectname);
        return pappMapper.selectList(queryWrapper);
    }


}
