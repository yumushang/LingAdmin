package xyz.linghuayicao.controller.base;

import xyz.linghuayicao.entity.system.SysUser;
import xyz.linghuayicao.utils.HttpKit;

public class BaseController {

    protected SysUser getUser(){
        SysUser user = (SysUser) HttpKit.getRequest().getSession().getAttribute("user");
        return user;
    }



}
