package xyz.linghuayicao.service.system;

import xyz.linghuayicao.entity.system.TSysLog;
import xyz.linghuayicao.entity.system.TSysLogCriteria;
import xyz.linghuayicao.entity.system.TSysLog;
import xyz.linghuayicao.entity.system.TSysLogCriteria;

import java.util.List;

public interface TSysLogService {
    int countByExample(TSysLogCriteria example);

    TSysLog selectByPrimaryKey(Long id);

    List<TSysLog> selectByExample(TSysLogCriteria example);

    int deleteByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TSysLog record);

    int updateByPrimaryKey(TSysLog record);

    int deleteByExample(TSysLogCriteria example);

    int updateByExampleSelective(TSysLog record, TSysLogCriteria example);

    int updateByExample(TSysLog record, TSysLogCriteria example);

    int insert(TSysLog record);

    int insertSelective(TSysLog record);
}