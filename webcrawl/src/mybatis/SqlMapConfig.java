package mybatis;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlMapConfig {

	private static SqlSessionFactory sqlSessionFactory;//=null값되어버림, 그러면 안되니까 14줄 static

	static { //정적블록
		String resource = "mybatis/Configuration.xml";
		try {
			Reader reader = Resources.getResourceAsReader(resource);
			if(sqlSessionFactory == null) {
				sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static SqlSessionFactory getSqlSession() {
		// TODO Auto-generated method stub
		return sqlSessionFactory;
	}


}
