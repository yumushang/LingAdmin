package xyz.linghuayicao.service.system;

import java.util.List;

import com.github.pagehelper.PageInfo;
import xyz.linghuayicao.entity.system.SysUser;
import xyz.linghuayicao.entity.system.TSysCode;
import xyz.linghuayicao.entity.system.TSysCodeCriteria;
import xyz.linghuayicao.entity.system.TSysCode;
import xyz.linghuayicao.entity.system.TSysCodeCriteria;

public interface TSysCodeService {
    int countByExample(TSysCodeCriteria example);

    TSysCode selectByPrimaryKey(Integer id);

    List<TSysCode> selectByExample(TSysCodeCriteria example);

    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TSysCode record);

    int updateByPrimaryKey(TSysCode record);

    int deleteByExample(TSysCodeCriteria example);

    int updateByExampleSelective(TSysCode record, TSysCodeCriteria example);

    int updateByExample(TSysCode record, TSysCodeCriteria example);

    int insert(TSysCode record);

    TSysCode insertSelective(TSysCode record);


    PageInfo selectByExample(TSysCodeCriteria example, Integer pageSize, Integer pageNum);
    
    
    
    
}