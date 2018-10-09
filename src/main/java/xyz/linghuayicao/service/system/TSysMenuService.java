package xyz.linghuayicao.service.system;

import xyz.linghuayicao.entity.system.TSysMenu;
import xyz.linghuayicao.entity.system.TSysMenuCriteria;
import xyz.linghuayicao.entity.system.TSysMenu;
import xyz.linghuayicao.entity.system.TSysMenuCriteria;

import java.util.List;

public interface TSysMenuService {
    int countByExample(TSysMenuCriteria example);

    TSysMenu selectByPrimaryKey(Integer id);

    List<TSysMenu> selectByExample(TSysMenuCriteria example);

    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TSysMenu record);

    int updateByPrimaryKey(TSysMenu record);

    int deleteByExample(TSysMenuCriteria example);

    int updateByExampleSelective(TSysMenu record, TSysMenuCriteria example);

    int updateByExample(TSysMenu record, TSysMenuCriteria example);

    int insert(TSysMenu record);

    TSysMenu insertSelective(TSysMenu record);

    List<TSysMenu> selectAllWithPermissionByUserGroupId(Integer userGroupId);

    List<TSysMenu> selectAllWithPermissionByRoleId(Integer roleId);

    List<TSysMenu> selectRoleMenusInAllMenusByUpId(Integer roleId, Integer upId);
}