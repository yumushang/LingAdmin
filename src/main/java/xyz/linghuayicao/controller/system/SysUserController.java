package xyz.linghuayicao.controller.system;


import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import xyz.linghuayicao.entity.system.SysUser;
import xyz.linghuayicao.entity.system.SysUserCriteria;
import xyz.linghuayicao.entity.system.TSysDepartCriteria;
import xyz.linghuayicao.entity.system.TSysUsergroupCriteria;
import xyz.linghuayicao.service.system.SysUserService;
import xyz.linghuayicao.service.system.TSysDepartService;
import xyz.linghuayicao.service.system.TSysMenuService;
import xyz.linghuayicao.service.system.TSysUsergroupService;
import xyz.linghuayicao.utils.Consts;
import xyz.linghuayicao.utils.HttpResult;
import xyz.linghuayicao.entity.system.TSysUsergroupCriteria;

@Controller
@RequestMapping(value="/sysuser")
public class SysUserController {



    @Autowired
    private SysUserService service;
    @Autowired
    private TSysMenuService menuService;
    @Autowired
    private TSysUsergroupService usergroupService;
    @Autowired
    private TSysDepartService departService;

    //用户列表页
    @RequestMapping(value="/list")
    public String usergrouplist(ModelMap mm){
        return "user/list";
    }

    //用户input页
    @RequestMapping(value="/input")
    public String usergroupInput(ModelMap mm,HttpServletRequest request){
        if(!StringUtils.isEmpty(request.getParameter("id"))){
            Integer id = Integer.parseInt(request.getParameter("id"));
            mm.addAttribute("object",service.selectByPrimaryKey(id));
        }
        mm.addAttribute("usergroupList",usergroupService.selectByExample(new TSysUsergroupCriteria()));
        mm.addAttribute("departList",departService.selectByExample(new TSysDepartCriteria()));
        return "user/input";
    }

    @RequestMapping(value="/userList")
    @ResponseBody
    public HttpResult userList(Integer pageSize, Integer pageNum, String search){
        HttpResult result = new HttpResult();
        try{
            SysUserCriteria criteria = new SysUserCriteria();
            if(search!=null&&!search.equals("")){
                criteria.createCriteria().andUsernameLike(search);
            }
            PageInfo info = service.selectByExample(criteria,pageSize,pageNum);
            result.setCount(info.getTotal());
            result.setData(info.getList());
            result.setCode(Consts.SUCCESS);
            result.setMsg("获取成功");
        }catch (Exception e){
            result.setCode(Consts.ERROR);
            result.setMsg("获取失败!"+e.getMessage());
        }
        return result;
    }


    @RequestMapping(value="/saveUser")
    @ResponseBody
    public HttpResult saveUser(SysUser user){
        HttpResult result = new HttpResult();
        try{
            if (user==null||StringUtils.isEmpty(user.getAccount())) throw new Exception("参数错误!");
            //id为空则新增
            if(user.getId()==null||user.getId()==0){
                SysUserCriteria criteria = new SysUserCriteria();
                criteria.createCriteria().andAccountEqualTo(user.getAccount());
                List<SysUser> list =service.selectByExample(criteria);
                if(list==null||list.size()!=0){
                    throw new Exception("已经存在的账号!");
                }
                user.setPassword(Consts.DEFAULT_PASSWORD);
                user = service.insertSelective(user);
            }else{
                //更新
                service.updateByPrimaryKeySelective(user);
            }
            //返回id
            result.setData(user.getId());
            result.setCode(Consts.SUCCESS);
            result.setMsg("操作成功");
        }catch (Exception e){
            result.setCode(Consts.ERROR);
            result.setMsg("操作失败!"+e.getMessage());
        }
        return result;
    }

    @RequestMapping(value="/delUser")
    @ResponseBody
    public HttpResult delUser(@RequestParam(value = "userIds[]")Integer[] userIds){
        HttpResult result = new HttpResult();
        try{
            List<Integer> ids = Arrays.asList(userIds);
            SysUserCriteria criteria = new SysUserCriteria();
            criteria.createCriteria().andIdIn(ids);
            service.deleteByExample(criteria);
            //返回id
            result.setCode(Consts.SUCCESS);
            result.setMsg("操作成功");
        }catch (Exception e){
            result.setCode(Consts.ERROR);
            result.setMsg("操作失败!"+e.getMessage());
        }
        return result;
    }

    @RequestMapping(value="/resetPwd")
    @ResponseBody
    public HttpResult resetPwd(@RequestParam(value = "userIds[]")Integer[] userIds){
        HttpResult result = new HttpResult();
        try{
            List<Integer> ids = Arrays.asList(userIds);
            SysUserCriteria criteria = new SysUserCriteria();
            criteria.createCriteria().andIdIn(ids);
            SysUser user = new SysUser();
            user.setPassword(Consts.DEFAULT_PASSWORD);
            service.updateByExampleSelective(user,criteria);
            //返回id
            result.setCode(Consts.SUCCESS);
            result.setMsg("操作成功");
        }catch (Exception e){
            result.setCode(Consts.ERROR);
            result.setMsg("操作失败!"+e.getMessage());
        }
        return result;
    }

}
