package xyz.linghuayicao.controller.system;


import xyz.linghuayicao.entity.system.*;
import xyz.linghuayicao.service.system.TSysMenuService;
import xyz.linghuayicao.service.system.TSysRoleMenuPermissionService;
import xyz.linghuayicao.service.system.TSysRoleMenuService;
import xyz.linghuayicao.service.system.TSysRoleService;
import xyz.linghuayicao.utils.Consts;
import xyz.linghuayicao.utils.HttpResult;
import xyz.linghuayicao.utils.JsTreeBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.linghuayicao.entity.system.TSysMenuCriteria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value="/menu")
public class MenuController {

    @Autowired
    private TSysRoleService roleService;
    @Autowired
    private TSysMenuService menuService;
    @Autowired
    private TSysRoleMenuPermissionService roleMenuPermissionService;
    @Autowired
    private TSysRoleMenuService roleMenuService;



    //菜单管理
    @RequestMapping(value="/list")
    public String list(ModelMap mm){
        return "menu/list";
    }

    //菜单管理
    @RequestMapping(value="/fontAwesome")
    public String fontAwesome(ModelMap mm){
        return "base/fontAwesome";
    }


    @RequestMapping(value="/saveMenu")
    @ResponseBody
    public Object saveMenu(TSysMenu menu){
        HttpResult result = new HttpResult();
        try{
            //如果是on 则是选中 否则,,,
            if("on".equals(menu.getStatus())){
                menu.setStatus("1");
            }else {
                menu.setStatus("2");
            }
            //如果id为空或者为0 则需要新增 否则是更新
            if(menu.getId()==null||menu.getId()==0){
                menu = menuService.insertSelective(menu);
            }else{
                menuService.updateByPrimaryKeySelective(menu);
            }
            //返回id
            result.setData(menu.getId());
            result.setCode(Consts.SUCCESS);
            result.setMsg("操作成功!");
        }catch (Exception e){
            result.setCode(Consts.ERROR);
            result.setMsg("操作失败!"+e.getMessage());
        }
        return result;
    }

    @RequestMapping(value="/delMenu")
    @ResponseBody
    public Object delMenu(Integer menuId){
        HttpResult result = new HttpResult();
        try{
            menuService.deleteByPrimaryKey(menuId);
            result.setCode(Consts.SUCCESS);
            result.setMsg("操作成功!");
        }catch (Exception e){
            result.setCode(Consts.ERROR);
            result.setMsg("操作失败!"+e.getMessage());
        }
        return result;
    }

    @RequestMapping(value="/menuList")
    @ResponseBody
    public Object menuList(Integer upId){
        List<JsTreeBean> result = new ArrayList<>();
        TSysMenuCriteria criteria = new TSysMenuCriteria();
        criteria.createCriteria().andUpmenuidEqualTo(upId);
        criteria.setOrderByClause("weight DESC");
        List<TSysMenu> menuList =  menuService.selectByExample(criteria);
        if (menuList==null||menuList.size()==0) return null;
        for (TSysMenu menu:menuList) {
            JsTreeBean bean = new JsTreeBean();
            bean.setId(menu.getId().toString());
            bean.setText(menu.getMenuname());
            bean.setIcon(menu.getMenuicon());
            bean.setData(menu);
            //2018-8-28 支持三级菜单
            bean.setChildren(true);
            /*//如果是一级菜单 有子菜单
            if(upId==0){
                bean.setChildren(true);
            }else{
                //二级菜单没有子菜单  不支持三级菜单
                bean.setChildren(false);
            }*/
            result.add(bean);
        }
        return result;
    }


    @RequestMapping(value="/roleMenuListInAllMenu")
    @ResponseBody
    public Object roleMenuList(Integer roleId,Integer upId){

        List<JsTreeBean> result = new ArrayList<>();
        /*JsTreeBean bean1 = new JsTreeBean();
        if(upId==-1){
            bean1.setId(0+"");
            bean1.setText("绩效");
            bean1.setStateOpened(true);
            bean1.setChildren(true);
            result.add(bean1);
            return result;
        }*/
        List<TSysMenu> menuList =  menuService.selectRoleMenusInAllMenusByUpId(roleId,upId);
        if (menuList==null||menuList.size()==0) return null;

        for (TSysMenu menu:menuList) {
            JsTreeBean bean = new JsTreeBean();
            bean.setId(menu.getId().toString());
            bean.setText(menu.getMenuname());
            bean.setIcon(menu.getMenuicon());
            bean.setStateOpened(true);
            bean.setStateChecked(!StringUtils.isEmpty(menu.getRolehas()));
            bean.setData(menu);
            bean.setChildren(true);
            result.add(bean);
        }
        return result;
    }


    @RequestMapping(value="/roleMenuPermissionList")
    @ResponseBody
    public Object roleMenuPermissionList(Integer roleId,Integer menuId){
        HttpResult result = new HttpResult();
        try{
            TSysRoleMenuPermissionCriteria criteria = new TSysRoleMenuPermissionCriteria();
            criteria.createCriteria().andRoleidEqualTo(roleId).andMenuidEqualTo(menuId);
            List<TSysRoleMenuPermission> list = roleMenuPermissionService.selectByExample(criteria);
            if (list!=null&&list.size()!=0){
                Integer[] permission =new Integer[list.size()];
                for (int i=0;i<list.size();i++){
                    permission[i]=list.get(i).getPermissionid();
                }
                result.setData(permission);
            }
            result.setCode(Consts.SUCCESS);
            result.setMsg("获取成功");
        }catch (Exception e){
            result.setCode(Consts.ERROR);
            result.setMsg("获取失败!"+e.getMessage());
        }
        return result;
    }


    @RequestMapping(value="/updateRoleMenuPermission")
    @ResponseBody
    public Object updateRoleMenuPermission(Integer roleId,Integer menuId,@RequestParam(value = "permissions[]")Integer[] permissions,String roleName){
        HttpResult result = new HttpResult();
        try{
            //roleid为空或者为0 代表是新增角色 需要新增role
            if(roleId==null||roleId==0){
                TSysRole role = new TSysRole();
                role.setRolename(roleName);
                role.setCreatetime(new Date());
                role = roleService.insertSelective(role);
                roleId = role.getId();
            }else{
                //roleid为空不需要删除关联表
                TSysRoleMenuPermissionCriteria criteria = new TSysRoleMenuPermissionCriteria();
                criteria.createCriteria().andRoleidEqualTo(roleId).andMenuidEqualTo(menuId);
                roleMenuPermissionService.deleteByExample(criteria);
            }
            //检查之前菜单是否关联
            TSysRoleMenuCriteria criteria1 = new TSysRoleMenuCriteria();
            criteria1.createCriteria().andRoleidEqualTo(roleId).andMenuidEqualTo(menuId);
            List<TSysRoleMenu> roleMenus = roleMenuService.selectByExample(criteria1);
            //如果没值 则需要新增role_menu的关联
            if(roleMenus==null||roleMenus.size()<=0){
                TSysRoleMenu sysRoleMenu = new TSysRoleMenu();
                sysRoleMenu.setRoleid(roleId);
                sysRoleMenu.setMenuid(menuId);
                roleMenuService.insertSelective(sysRoleMenu);
            }
            //插入role_menu_permission关联表
            List<TSysRoleMenuPermission> permissionList = new ArrayList<>();
            for (Integer pId:permissions) {
                TSysRoleMenuPermission permission = new TSysRoleMenuPermission();
                permission.setMenuid(menuId);
                permission.setRoleid(roleId);
                permission.setPermissionid(pId);
                permissionList.add(permission);
            }
            roleMenuPermissionService.insertBatchSelective(permissionList);
            //把roleid返回前端
            result.setData(roleId);
            result.setCode(Consts.SUCCESS);
            result.setMsg("更新成功");
        }catch (Exception e){
            result.setCode(Consts.ERROR);
            result.setMsg("更新失败!"+e.getMessage());
        }
        return result;
    }




}
