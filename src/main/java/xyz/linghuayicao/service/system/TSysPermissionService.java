package xyz.linghuayicao.service.system;

import xyz.linghuayicao.entity.system.TSysPermission;
import xyz.linghuayicao.entity.system.TSysPermissionCriteria;
import xyz.linghuayicao.entity.system.TSysPermission;
import xyz.linghuayicao.entity.system.TSysPermissionCriteria;

import java.util.List;

public interface TSysPermissionService {
    int countByExample(TSysPermissionCriteria example);

    TSysPermission selectByPrimaryKey(Integer id);

    List<TSysPermission> selectByExample(TSysPermissionCriteria example);

    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TSysPermission record);

    int updateByPrimaryKey(TSysPermission record);

    int deleteByExample(TSysPermissionCriteria example);

    int updateByExampleSelective(TSysPermission record, TSysPermissionCriteria example);

    int updateByExample(TSysPermission record, TSysPermissionCriteria example);

    int insert(TSysPermission record);

    int insertSelective(TSysPermission record);
}