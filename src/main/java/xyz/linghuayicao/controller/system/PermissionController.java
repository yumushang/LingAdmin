package xyz.linghuayicao.controller.system;

import com.github.pagehelper.PageInfo;
import xyz.linghuayicao.entity.system.*;
import xyz.linghuayicao.service.system.*;
import xyz.linghuayicao.utils.Consts;
import xyz.linghuayicao.utils.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.linghuayicao.entity.system.TSysRoleCriteria;
import xyz.linghuayicao.entity.system.TSysUsergroupCriteria;
import xyz.linghuayicao.entity.system.TSysUsergroupRole;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value="/permission")
public class PermissionController {

    @Autowired
    private TSysRoleService roleService;
    @Autowired
    private TSysRoleMenuService roleMenuService;
    @Autowired
    private TSysRoleMenuPermissionService roleMenuPermissionService;
    @Autowired
    private TSysUsergroupRoleService usergroupRoleService;
    @Autowired
    private TSysUsergroupService usergroupService;
    @Autowired
    private TSysCodeService codeService;



    //角色管理
    @RequestMapping(value="/rolelist")
    public String freeMarker(ModelMap mm){
        return "role/list";
    }





    //角色管理
    @RequestMapping(value="/roleInput")
    public String roleInput(ModelMap mm,HttpServletRequest request){

        if(!StringUtils.isEmpty(request.getParameter("id"))){
            Integer id = Integer.parseInt(request.getParameter("id"));
            mm.addAttribute("role",roleService.selectByPrimaryKey(id));
        }
        return "role/input";
    }

    //用户组列表页
    @RequestMapping(value="/usergrouplist")
    public String usergrouplist(ModelMap mm){
        return "usergroup/list";
    }

    //用户组input页
    @RequestMapping(value="/usergroupInput")
    public String usergroupInput(ModelMap mm,HttpServletRequest request){
        if(!StringUtils.isEmpty(request.getParameter("id"))){
            Integer id = Integer.parseInt(request.getParameter("id"));
            mm.addAttribute("object",usergroupService.selectByPrimaryKey(id));
        }
        mm.addAttribute("roleList",roleService.selectByExample(new TSysRoleCriteria()));
        return "usergroup/input";
    }

    @RequestMapping(value="/roleList")
    @ResponseBody
    public HttpResult roleList(Integer pageSize,Integer pageNum,String search){
        HttpResult result = new HttpResult();
        try{
            TSysRoleCriteria criteria = new TSysRoleCriteria();
            if(search!=null&&!search.equals("")){
                criteria.createCriteria().andRolenameLike(search);
            }
            PageInfo info = roleService.selectByExample(criteria,pageSize,pageNum);
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

    @RequestMapping(value="/usergroupList")
    @ResponseBody
    public HttpResult usergroupList(Integer pageSize,Integer pageNum,String search){
        HttpResult result = new HttpResult();
        try{
            TSysUsergroupCriteria criteria = new TSysUsergroupCriteria();
            if(search!=null&&!search.equals("")){
                criteria.createCriteria().andUsergroupnameLike(search);
            }
            PageInfo info = usergroupService.selectByExample(criteria,pageSize,pageNum);
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

    @RequestMapping(value="/usergroupRoleList")
    @ResponseBody
    public HttpResult usergroupRoleList(Integer id){
        HttpResult result = new HttpResult();
        try{
            TSysUsergroupRoleCriteria criteria = new TSysUsergroupRoleCriteria();
            criteria.createCriteria().andUsergroupidEqualTo(id);
            result.setData(usergroupRoleService.selectByExample(criteria));
            result.setCode(Consts.SUCCESS);
            result.setMsg("获取成功");
        }catch (Exception e){
            result.setCode(Consts.ERROR);
            result.setMsg("获取失败!"+e.getMessage());
        }
        return result;
    }



    @RequestMapping(value="/updateRoleAndMenu")
    @ResponseBody
    public Object updateRoleAndMenu(TSysRole role,@RequestParam(value = "menuIds[]")Integer[] menuIds){
        HttpResult result = new HttpResult();
        try{
            if (role==null) throw new Exception("参数错误!");
            //如果roleid为0或者为空 则需要新增role
            if(role.getId()==null||role.getId()==0){
                role.setCreatetime(new Date());
                role = roleService.insertSelective(role);
            }else{
                //如果不是新建的角色,新增菜单关联之前先删除之前的关联
                TSysRoleMenuCriteria criteria = new TSysRoleMenuCriteria();
                criteria.createCriteria().andRoleidEqualTo(role.getId());
                roleMenuService.deleteByExample(criteria);

                TSysRoleMenuPermissionCriteria criteria1 = new TSysRoleMenuPermissionCriteria();
                criteria1.createCriteria().andRoleidEqualTo(role.getId());
                roleMenuPermissionService.deleteByExample(criteria1);
                //更新role其他字段
                role.setCreatetime(new Date());
                roleService.updateByPrimaryKey(role);
            }

            //插入role_menu关联表
            List<TSysRoleMenu> roleMenus = new ArrayList<>();
            //插入role_menu_permission关联表
            List<TSysRoleMenuPermission> permissionList = new ArrayList<>();
            for (Integer menuId:menuIds) {
                TSysRoleMenu roleMenu = new TSysRoleMenu();
                TSysRoleMenuPermission permission = new TSysRoleMenuPermission();
                roleMenu.setMenuid(menuId);
                roleMenu.setRoleid(role.getId());
                roleMenus.add(roleMenu);

                permission.setRoleid(role.getId());
                permission.setMenuid(menuId);
                permissionList.add(permission);
            }
            roleMenuService.insertBatchSelective(roleMenus);
            roleMenuPermissionService.insertBatchSelective(permissionList);

            //返回roleid
            result.setData(role.getId());
            result.setCode(Consts.SUCCESS);
            result.setMsg("操作成功");
        }catch (Exception e){
            result.setCode(Consts.ERROR);
            result.setMsg("操作失败!"+e.getMessage());
        }
        return result;
    }


    @RequestMapping(value="/updateUsergroupAndRole")
    @ResponseBody
    public Object updateRoleAndMenu(TSysUsergroup object,@RequestParam(value = "roleIds[]")Integer[] roleIds){
        HttpResult result = new HttpResult();
        try{
            //判断传进来的值是对的
            if (object==null) throw new Exception("参数错误!");
            //如果id为0或者为空 则需要新增
            if(object.getId()==null||object.getId()==0){
                object.setCreatetime(new Date());
                object = usergroupService.insertSelective(object);
            }else{
                //如果不是新建的用户组,新增角色关联之前先删除之前的关联
                TSysUsergroupRoleCriteria criteria = new TSysUsergroupRoleCriteria();
                criteria.createCriteria().andUsergroupidEqualTo(object.getId());
                usergroupRoleService.deleteByExample(criteria);
            }

            //插入usergroup_Role关联表
            List<TSysUsergroupRole> usergroupRoles = new ArrayList<>();
            for (Integer roleId:roleIds) {
                TSysUsergroupRole usergroupRole = new TSysUsergroupRole();
                usergroupRole.setUsergroupid(object.getId());
                usergroupRole.setRoleid(roleId);
                usergroupRoles.add(usergroupRole);
            }
            usergroupRoleService.insertBatchSelective(usergroupRoles);
            //返回id
            result.setData(object.getId());
            result.setCode(Consts.SUCCESS);
            result.setMsg("操作成功");
        }catch (Exception e){
            result.setCode(Consts.ERROR);
            result.setMsg("操作失败!"+e.getMessage());
        }
        return result;
    }


    @RequestMapping(value="/deleteRole")
    @ResponseBody
    public HttpResult deleteRole(Integer roleId){
        HttpResult result = new HttpResult();
        try{
            roleService.deleteByPrimaryKey(roleId);
            result.setCode(Consts.SUCCESS);
            result.setMsg("操作成功!");
        }catch (Exception e){
            result.setCode(Consts.ERROR);
            result.setMsg("操作失败!"+e.getMessage());
        }
        return result;
    }

    @RequestMapping(value="/deleteUsergroup")
    @ResponseBody
    public HttpResult deleteUsergroup(Integer id){
        HttpResult result = new HttpResult();
        try{
            usergroupService.deleteByPrimaryKey(id);
            result.setCode(Consts.SUCCESS);
            result.setMsg("操作成功!");
        }catch (Exception e){
            result.setCode(Consts.ERROR);
            result.setMsg("操作失败!"+e.getMessage());
        }
        return result;
    }
}
