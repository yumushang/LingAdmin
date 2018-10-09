package xyz.linghuayicao.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import xyz.linghuayicao.dao.system.TSysRoleDAO;
import xyz.linghuayicao.entity.system.TSysRole;
import xyz.linghuayicao.entity.system.TSysRoleCriteria;
import xyz.linghuayicao.service.system.TSysRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.linghuayicao.entity.system.TSysRole;
import xyz.linghuayicao.entity.system.TSysRoleCriteria;
import xyz.linghuayicao.service.system.TSysRoleService;

import java.util.List;

@Service
public class TSysRoleServiceImpl implements TSysRoleService {
    @Autowired
    private TSysRoleDAO tSysRoleDAO;

    private static final Logger logger = LoggerFactory.getLogger(TSysRoleServiceImpl.class);

    public int countByExample(TSysRoleCriteria example) {
        int count = this.tSysRoleDAO.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TSysRole selectByPrimaryKey(Integer id) {
        return this.tSysRoleDAO.selectByPrimaryKey(id);
    }

    public List<TSysRole> selectByExample(TSysRoleCriteria example) {
        return this.tSysRoleDAO.selectByExample(example);
    }

    public int deleteByPrimaryKey(Integer id) {
        return this.tSysRoleDAO.deleteByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(TSysRole record) {
        return this.tSysRoleDAO.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TSysRole record) {
        return this.tSysRoleDAO.updateByPrimaryKey(record);
    }

    public int deleteByExample(TSysRoleCriteria example) {
        return this.tSysRoleDAO.deleteByExample(example);
    }

    public int updateByExampleSelective(TSysRole record, TSysRoleCriteria example) {
        return this.tSysRoleDAO.updateByExampleSelective(record, example);
    }

    public int updateByExample(TSysRole record, TSysRoleCriteria example) {
        return this.tSysRoleDAO.updateByExample(record, example);
    }

    public int insert(TSysRole record) {
        return this.tSysRoleDAO.insert(record);
    }

    public TSysRole insertSelective(TSysRole record) {
        this.tSysRoleDAO.insertSelective(record);
        return record;
    }

    @Override
    public PageInfo selectByExample(TSysRoleCriteria example, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<TSysRole> list = tSysRoleDAO.selectByExample(example);
        //用PageInfo对结果进行包装
        PageInfo page = new PageInfo(list);
        return page;
    }
}