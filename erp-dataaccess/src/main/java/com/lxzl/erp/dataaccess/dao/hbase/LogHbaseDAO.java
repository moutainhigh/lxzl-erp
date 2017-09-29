package com.lxzl.erp.dataaccess.dao.hbase;

import com.lxzl.erp.dataaccess.domain.LogDO;
import com.lxzl.se.dataaccess.hbase.BaseHbaseDAO;

public interface LogHbaseDAO extends BaseHbaseDAO {

	boolean addLog(final LogDO log);
	
	LogDO getLog(final Integer id);
	
	boolean deleteLog(final Integer id);
	
}
