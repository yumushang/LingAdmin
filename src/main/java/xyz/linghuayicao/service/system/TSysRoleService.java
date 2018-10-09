package xyz.linghuayicao.service.system;

import com.github.pagehelper.PageInfo;
import xyz.linghuayicao.entity.system.TSysRole;
import xyz.linghuayicao.entity.system.TSysRoleCriteria;
import xyz.linghuayicao.entity.system.TSysRole;
import xyz.linghuayicao.entity.system.TSysRoleCriteria;

import java.util.List;

public interface TSysRoleService {
    int countByExample(TSysRoleCriteria example);

    TSysRole selectByPrimaryKey(Integer id);

    List<TSysRole> selectByExample(TSysRoleCriteria example);

    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TSysRole record);

    int updateByPrimaryKey(TSysRole record);

    int deleteByExample(TSysRoleCriteria example);

    int updateByExampleSelective(TSysRole record, TSysRoleCriteria example);

    int updateByExample(TSysRole record, TSysRoleCriteria example);

    int insert(TSysRole record);

    TSysRole insertSelective(TSysRole record);

    PageInfo selectByExample(TSysRoleCriteria example, Integer pageSize, Integer pageNum);
}