package cn.hn.java.summer.mybatis.batch;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * SessionFactoryBuilder for batch
 * Created by xw2sy on 2017-06-09.
 */
public class BatchSessionFactoryBuilder extends SqlSessionFactoryBuilder {

    public SqlSessionFactory build(Configuration config) {
        return new BatchSqlSessionFactory(config);
    }
}
