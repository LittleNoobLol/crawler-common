package com.crawler.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.druid.pool.DruidPooledConnection;

public class DBHelp {

	private static Logger log = Logger.getLogger(DBHelp.class);

	/**
	 * 关闭连接对象
	 */
	public void close(ResultSet rs, PreparedStatement pstmt, DruidPooledConnection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				log.error("关闭rs出错", e);
			}
		}

		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				log.error("关闭pstmt出错", e);
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error("关闭conn出错", e);
			}
		}
	}

	/**
	 * 执行更新
	 *
	 * @param sql传入的预设的 sql语句
	 * @param params问号参数列表
	 * @return 影响行数
	 */
	public int execUpdate(String sql, Object[] params) {

		DBPoolConnection dbp = DBPoolConnection.getInstance();

		DruidPooledConnection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dbp.getConnection();
			pstmt = conn.prepareStatement(sql);
			if (params != null) {
				// 设置参数列表
				for (int i = 0; i < params.length; i++) {
					// 因为问号参数的索引是从1开始，所以是i+1，将所有值都转为字符串形式，好让setObject成功运行
					pstmt.setObject(i + 1, params[i] + "");
				}
			}
			return pstmt.executeUpdate(); // 执行更新，并返回影响行数
		} catch (SQLException e) {
			log.error("execUpdate异常", e);
		} finally {
			close(null, pstmt, conn);
		}
		return 0;
	}

	/**
	 * 执行查询
	 *
	 * @param sql传入的预设的sql语句
	 * @param params问号参数列表
	 * @return 查询后的结果
	 */
	public List<Map<String, Object>> execQuery(String sql, Object[] params) {

		DBPoolConnection dbp = DBPoolConnection.getInstance();

		DruidPooledConnection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dbp.getConnection();
			pstmt = conn.prepareStatement(sql);

			if (params != null) {
				// 设置参数列表
				for (int i = 0; i < params.length; i++) {
					// 因为问号参数的索引是从1开始，所以是i+1，将所有值都转为字符串形式，好让setObject成功运行
					pstmt.setObject(i + 1, params[i] + "");
				}
			}

			// 执行查询
			rs = pstmt.executeQuery();

			List<Map<String, Object>> al = new ArrayList<Map<String, Object>>();

			// 获得结果集元数据（元数据就是描述数据的数据，比如把表的列类型列名等作为数据）
			ResultSetMetaData rsmd = rs.getMetaData();

			// 获得列的总数
			int columnCount = rsmd.getColumnCount();

			// 遍历结果集
			while (rs.next()) {
				Map<String, Object> hm = new HashMap<String, Object>();
				for (int i = 0; i < columnCount; i++) {
					// 根据列索引取得每一列的列名,索引从1开始
					String columnName = rsmd.getColumnName(i + 1);
					// 根据列名获得列值
					Object columnValue = rs.getObject(columnName);
					// 将列名作为key，列值作为值，放入 hm中，每个 hm相当于一条记录
					hm.put(columnName, columnValue);
				}
				// 将每个 hm添加到al中, al相当于是整个表，每个 hm是里面的一条记录
				al.add(hm);
			}
			return al;
		} catch (SQLException e) {
			log.error("execQuery异常", e);
		} finally {
			close(rs, pstmt, conn);
		}
		return null;
	}

}
