package xyz.linghuayicao.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import xyz.linghuayicao.dao.system.SysUserDAO;
import xyz.linghuayicao.entity.system.SysUserCriteria;
import xyz.linghuayicao.entity.system.SysUser;
import xyz.linghuayicao.service.system.SysUserService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.linghuayicao.entity.system.SysUser;
import xyz.linghuayicao.entity.system.SysUserCriteria;
import xyz.linghuayicao.service.system.SysUserService;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserDAO sysUserDAO;

    private static final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

    public int countByExample(SysUserCriteria example) {
        int count = this.sysUserDAO.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public SysUser selectByPrimaryKey(Integer id) {
        return this.sysUserDAO.selectByPrimaryKey(id);
    }

    public List<SysUser> selectByExample(SysUserCriteria example) {
        return this.sysUserDAO.selectByExample(example);
    }

    public int deleteByPrimaryKey(Integer id) {
        return this.sysUserDAO.deleteByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(SysUser record) {
        return this.sysUserDAO.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(SysUser record) {
        return this.sysUserDAO.updateByPrimaryKey(record);
    }

    public int deleteByExample(SysUserCriteria example) {
        return this.sysUserDAO.deleteByExample(example);
    }

    public int updateByExampleSelective(SysUser record, SysUserCriteria example) {
        return this.sysUserDAO.updateByExampleSelective(record, example);
    }

    public int updateByExample(SysUser record, SysUserCriteria example) {
        return this.sysUserDAO.updateByExample(record, example);
    }

    public int insert(SysUser record) {
        return this.sysUserDAO.insert(record);
    }

    public SysUser insertSelective(SysUser record) {
        this.sysUserDAO.insertSelective(record);
        return record;
    }
    
    
    @Override
    public PageInfo selectByExample(SysUserCriteria example, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<SysUser> list = sysUserDAO.selectByExample(example);
        //用PageInfo对结果进行包装
        PageInfo page = new PageInfo(list);
        return page;
    }
    
    
    
}