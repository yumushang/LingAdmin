package xyz.linghuayicao.service.system;

import xyz.linghuayicao.entity.system.SysUserCriteria;
import com.github.pagehelper.PageInfo;
import xyz.linghuayicao.entity.system.SysUser;
import xyz.linghuayicao.entity.system.SysUser;
import xyz.linghuayicao.entity.system.SysUserCriteria;

import java.util.List;

public interface SysUserService {
    int countByExample(SysUserCriteria example);

    SysUser selectByPrimaryKey(Integer id);

    List<SysUser> selectByExample(SysUserCriteria example);

    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    int deleteByExample(SysUserCriteria example);

    int updateByExampleSelective(SysUser record, SysUserCriteria example);

    int updateByExample(SysUser record, SysUserCriteria example);

    int insert(SysUser record);

    SysUser insertSelective(SysUser record);
    
    PageInfo selectByExample(SysUserCriteria example, Integer pageSize, Integer pageNum);
    
    
}