package com.lxzl.erp.dataaccess.dao.hbase.impl;

import java.util.Date;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.stereotype.Repository;

import com.lxzl.erp.dataaccess.dao.hbase.LogHbaseDAO;
import com.lxzl.erp.dataaccess.domain.LogDO;
import com.lxzl.se.common.util.date.DateUtil;
import com.lxzl.se.dataaccess.hbase.impl.BaseHbaseDAOImpl;

@Repository("logHbaseDAO")
public class LogHbaseDAOImpl extends BaseHbaseDAOImpl implements LogHbaseDAO {

	private static final String TABLE = "t_log";
	
	@Override
	public boolean addLog(final LogDO log) {
		boolean result = hbaseTemplate.execute(TABLE, new TableCallback<Boolean>() {
			@Override
			public Boolean doInTable(HTableInterface table) throws Throwable {
				Put put1 = new Put(serialize(log.getId()));
				long t1 = System.currentTimeMillis();
				System.out.println("t1=" + t1);
				put1.add(serialize("f_log"), serialize("q_log"), t1, serialize(log));
				table.put(put1);
				return true;
			}
		});

		return result;
	}

	@Override
	public LogDO getLog(final Integer id) {
		LogDO result = hbaseTemplate.execute(TABLE, new TableCallback<LogDO>() {
			@Override
			public LogDO doInTable(HTableInterface table) throws Throwable {
				Get get = new Get(serialize(id));
				Result result = table.get(get);
				byte[] valueByte = result.getValue(serialize("f_log"), serialize("q_log"));
				return deserialize(LogDO.class, valueByte);
			}
		});
		
		return result;
	}

	@Override
	public boolean deleteLog(final Integer id) {
		boolean result = hbaseTemplate.execute(TABLE, new TableCallback<Boolean>() {
			@Override
			public Boolean doInTable(HTableInterface table) throws Throwable {
				Delete delete = new Delete(serialize(id));
				delete.deleteColumn(serialize("f_log"), serialize("q_log"));
				table.delete(delete);
				return true;
			}
		});

		return result;
	}

	public static void main(String[] args) {
		Long a = Long.MAX_VALUE;
		String d1 = "2016-08-09 13:22:30";
		String d2 = "2016-08-09 08:33:13";
		Date d11= DateUtil.parse(d1, DateUtil.LONG_DATE_FORMAT_STR);
		Date d22= DateUtil.parse(d2, DateUtil.LONG_DATE_FORMAT_STR);
		Long b = a - d11.getTime();
		Long c = a - d22.getTime();
		int bh = b.hashCode();
		int ch = c.hashCode();
		
		if (b > c) {
			System.out.println(b + " b l=" + String.valueOf(b).length());
			System.out.println(c + " c l=" + String.valueOf(c).length());
		} else {
			System.out.println(c + " c l=" + String.valueOf(c).length());
			System.out.println(b + " b l=" + String.valueOf(b).length());
		}
		
		if (bh > ch) {
			System.out.println(bh + " bh l=" + String.valueOf(bh).length());
			System.out.println(ch + " ch l=" + String.valueOf(ch).length());
		} else {
			System.out.println(ch + " ch l=" + String.valueOf(ch).length());
			System.out.println(bh + " bh l=" + String.valueOf(bh).length());
		}
		
	}
}
