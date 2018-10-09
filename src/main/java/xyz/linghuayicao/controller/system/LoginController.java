package xyz.linghuayicao.controller.system;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.linghuayicao.controller.base.BaseController;
import xyz.linghuayicao.entity.system.SysUser;
import xyz.linghuayicao.entity.system.SysUserCriteria;
import xyz.linghuayicao.entity.system.TSysDepart;
import xyz.linghuayicao.entity.system.TSysMenu;
import xyz.linghuayicao.log.LogObjectHolder;
import xyz.linghuayicao.log.annotation.LogInfer;
import xyz.linghuayicao.permission.PermissionHolder;
import xyz.linghuayicao.service.system.SysUserService;
import xyz.linghuayicao.service.system.TSysDepartService;
import xyz.linghuayicao.service.system.TSysMenuService;
import xyz.linghuayicao.utils.Consts;
import xyz.linghuayicao.utils.HttpResult;
import xyz.linghuayicao.utils.LyingMenu;

@Controller
public class LoginController extends BaseController {


    @Autowired
    private SysUserService service;
    @Autowired
    private TSysMenuService menuService;
    @Autowired
    private TSysDepartService departService;

    //登录
    @RequestMapping(value="/login")
    public String login(ModelMap mm,HttpSession session){
        session.setAttribute("user", null);
        return "login";
    }

    @LogInfer(logModel = "登录", logType = "登录",remake = "用户登录",ID = "account")
    @RequestMapping(value="/ajaxLogin")
    @ResponseBody
    public HttpResult ajaxLogin(ModelMap mm, SysUser object, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        HttpResult result = new HttpResult();
        System.out.println(object.getAccount());
        System.out.println(object.getPassword());
        try {
            SysUserCriteria example = new SysUserCriteria();
            if(object.getAccount()==null && "".equals(object.getAccount())){
                throw new Exception("用户名不能为空!");
            }
            if(object.getPassword()==null && "".equals(object.getPassword())){
                throw new Exception("密码不能为空!");
            }
            example.createCriteria().andAccountEqualTo(object.getAccount()).andPasswordEqualTo(object.getPassword());
            List<SysUser> users = service.selectByExample(example);
            if(users.size()!=1){
                throw new Exception("用户名或者密码错误!");
            }
            SysUser user = users.get(0);
            //user塞入session中
            session.setAttribute("user", user);
            //BASE_URL塞入session中,页面所有连接前加上BASE_URL 解决页面路径多层问题
            session.setAttribute("BASE_URL",request.getContextPath());

            TSysDepart depart = departService.selectByPrimaryKey(user.getDepartid());
            session.setAttribute("userDepart", depart);

            //初始化菜单与权限
            initMenuHolder();
            result.setCode(Consts.SUCCESS);
            result.setMsg("登录成功!");
            result.setData(user);
        }catch (Exception e){
            result.setCode(Consts.ERROR);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @LogInfer(logModel = "修改登录密码", logType = "修改",remake = "修改登录密码",ID="account",objectKey = "user")
    @RequestMapping(value="/editPassword")
    @ResponseBody
    public HttpResult editPassword(ModelMap mm, HttpSession session, String oldPassword, String newPassword){
        HttpResult result = new HttpResult();
        try {
            SysUserCriteria example = new SysUserCriteria();
            if(getUser().getAccount()!=null&&!"".equals(getUser().getAccount())){
                throw new Exception("用户名不能为空!");
            }
            if(oldPassword!=null&&!"".equals(oldPassword)){
                throw new Exception("密码不能为空!");
            }
            example.createCriteria().andAccountEqualTo(getUser().getAccount()).andPasswordEqualTo(oldPassword);
            List<SysUser> users = service.selectByExample(example);
            if(users.size()!=1){
                throw new Exception("旧密码不正确!");
            }
            SysUser user = users.get(0);
            LogObjectHolder.me().set("user",user);
            user.setPassword(newPassword);
            service.updateByPrimaryKeySelective(user);
            result.setCode(Consts.SUCCESS);
            result.setMsg("登录成功!");
        }catch (Exception e){
            result.setCode(Consts.ERROR);
            result.setMsg(e.getMessage());
        }
        return result;
    }
    //无权限访问
    @RequestMapping(value="/noAuth")
    public String freeMarker(ModelMap mm){
        return "auth/noauth";
    }


    public void initMenuHolder(){
        List<TSysMenu> menuList = menuService.selectAllWithPermissionByUserGroupId(getUser().getUsergroupid());
        //存储 方便使用
        PermissionHolder.me().setMenuList(menuList);
        for (TSysMenu menu:menuList) {
            //将查询出来的菜单列表转成HashMap存储 方便取出
            PermissionHolder.me().set(menu.getMenuurl(),menu);
        }
    }

    @RequestMapping(value="/menu.json")
    @ResponseBody
    public Object menu(ModelMap mm){
        initMenuHolder();
        List<TSysMenu> menuList = PermissionHolder.me().getMenuList();
        List<LyingMenu> lyingMenus = new ArrayList<>();
        //第一层
        for (int i = 0; i < menuList.size(); i++) {
            if(menuList.get(i).getUpmenuid()==0){
                LyingMenu menu = new LyingMenu();
                menu.setHref(menuList.get(i).getMenuurl());
                menu.setIcon(menuList.get(i).getMenuicon());
                menu.setTitle(menuList.get(i).getMenuname());
                List<LyingMenu> lyingMenus1 = new ArrayList<>();
                //第二层
                for (int j = 0; j < menuList.size(); j++) {
                    if(menuList.get(j).getUpmenuid()==0){
                        continue;
                    }
                    if (menuList.get(j).getUpmenuid()==menuList.get(i).getId()){
                        LyingMenu menu1 = new LyingMenu();
                        menu1.setHref(menuList.get(j).getMenuurl());
                        menu1.setIcon(menuList.get(j).getMenuicon());
                        menu1.setTitle(menuList.get(j).getMenuname());
                        lyingMenus1.add(menu1);

                        List<LyingMenu> lyingMenus2 = new ArrayList<>();
                        //第三层
                        for(int k = 0; k < menuList.size(); k++){
                            if(menuList.get(j).getUpmenuid()==0){
                                continue;
                            }
                            if (menuList.get(k).getUpmenuid()==menuList.get(j).getId()){
                                LyingMenu menu2 = new LyingMenu();
                                menu2.setHref(menuList.get(k).getMenuurl());
                                menu2.setIcon(menuList.get(k).getMenuicon());
                                menu2.setTitle(menuList.get(k).getMenuname());
                                lyingMenus2.add(menu2);
                            }
                        }
                        menu1.setList(lyingMenus2);
                    }
                }
                menu.setList(lyingMenus1);
                lyingMenus.add(menu);
            }
        }

        return new Object[]{lyingMenus};
    }


    //导航-首页
    @RequestMapping(value="/newhomepage")
    public String newhomepage(ModelMap mm){
        return "newhomepage";
    }
    
    //人员管理
    @RequestMapping(value="/addpersonnel")
    public String addpersonnel(ModelMap mm){
        return "addpersonnel";
    }
    
}
