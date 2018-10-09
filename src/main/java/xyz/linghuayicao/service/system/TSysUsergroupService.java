package xyz.linghuayicao.service.system;

import com.github.pagehelper.PageInfo;
import xyz.linghuayicao.entity.system.TSysUsergroup;
import xyz.linghuayicao.entity.system.TSysUsergroupCriteria;
import xyz.linghuayicao.entity.system.TSysUsergroup;
import xyz.linghuayicao.entity.system.TSysUsergroupCriteria;

import java.util.List;

public interface TSysUsergroupService {
    int countByExample(TSysUsergroupCriteria example);

    TSysUsergroup selectByPrimaryKey(Integer id);

    List<TSysUsergroup> selectByExample(TSysUsergroupCriteria example);

    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TSysUsergroup record);

    int updateByPrimaryKey(TSysUsergroup record);

    int deleteByExample(TSysUsergroupCriteria example);

    int updateByExampleSelective(TSysUsergroup record, TSysUsergroupCriteria example);

    int updateByExample(TSysUsergroup record, TSysUsergroupCriteria example);

    int insert(TSysUsergroup record);

    TSysUsergroup insertSelective(TSysUsergroup record);

    PageInfo selectByExample(TSysUsergroupCriteria example, Integer pageSize, Integer pageNum);
}