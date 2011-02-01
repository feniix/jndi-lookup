package com.so.test.jndi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;

@Controller
public class SqlController {

	private String testsql;
	private DataSource datasource;
	private static final Log LOG = LogFactory.getLog(SqlController.class);

	@RequestMapping("/testsql")
	public ModelAndView test() {
		ModelAndView mav = new ModelAndView();
	        mav.setViewName("testsql");
		long start = System.currentTimeMillis();
	        mav.addObject("results", this.testDB());
		mav.addObject("time",System.currentTimeMillis() - start);
	        return mav;
	}


	private List<String> testDB() {
		PreparedStatement prep = null;
		Connection conn = null;
		List<String> ret = new ArrayList<String>();
		try {
			conn = this.datasource.getConnection();
			prep = conn.prepareStatement(this.testsql);
			ResultSet rs = null;
			try {
				rs = prep.executeQuery();
				while (rs.next()) {
					ret.add(rs.getString(1));
				}
			}
			catch (Exception ex) {
				LOG.error(ex.getMessage());
			}
			finally {
				rs.close();
			}
		}
		catch (Exception ex) {
			LOG.error(ex.getMessage());
		}
		finally {
			if (prep!=null) {
				try {
					prep.close();
				}
				catch (Exception ex) {
					LOG.error(ex.getMessage());
				}
			}
			if (conn!=null) {
				try {
					conn.close();
				}
				catch (Exception ex) {
					LOG.error(ex.getMessage());
				}
			}
		}
		return ret;
	}

	@Autowired
	public void setDatasource(DataSource ds) {
		this.datasource = ds;
	}

	@Autowired
	public void setTestsql(String sql) {
		this.testsql = sql;
	}
}
