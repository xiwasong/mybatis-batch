package cn.hn.java.summer.mybatis.batch;

import cn.hn.java.summer.mybatis.batch.statement.BatchParameterInfo;
import cn.hn.java.summer.mybatis.batch.statement.IParameterInfo;
import cn.hn.java.summer.mybatis.batch.statement.StatementType;
import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;

import java.util.Collection;
import java.util.List;

/**
 * sql session for batch
 * Created by xw2sy on 2017-06-09.
 */
public class BatchSqlSession extends DefaultSqlSession {

    BatchSqlSession(Configuration configuration, Executor executor, boolean autoCommit) {
        super(configuration, executor, autoCommit);
    }

    @Override
    public int delete(String statement, Object parameter) {
        //获取参数中设置的返回类型和真实参数
        //通过封装区别于其它类型的参数
        StatementType statementType= getStatementType(parameter);
        if(statementType==StatementType.BATCH){
            //批量操作
            return batchExecute(statement,parameter);
        }else {
            //其它
            return super.delete(statement, parameter);
        }
    }

    @Override
    public int update(String statement, Object parameter) {
        //获取参数中设置的返回类型和真实参数
        //通过封装区别于其它类型的参数
        StatementType statementType= getStatementType(parameter);
        if(statementType==StatementType.BATCH){
            //批量操作
            return batchExecute(statement,parameter);
        }else {
            //其它
            return super.update(statement, parameter);
        }
    }

    @Override
    public int insert(String statement, Object parameter) {
        //获取参数中设置的返回类型和真实参数
        //通过封装区别于其它类型的参数
        StatementType statementType= getStatementType(parameter);
        if(statementType==StatementType.BATCH){
            //批量操作
            return batchExecute(statement,parameter);
        }else {
            //其它
            return super.insert(statement, parameter);
        }
    }

    /**
     * 通过参数获取语句类型
     * @param parameter 参数
     * @return 语句类型
     */
    private StatementType getStatementType(Object parameter){
        //获取参数中设置的返回类型和真实参数
        //通过封装区别于其它类型的参数
        IParameterInfo statementInfo=null;
        if(parameter instanceof IParameterInfo){
            statementInfo= (IParameterInfo)parameter;
        }
        if(statementInfo!=null) {
            return statementInfo.getStatementType();
        }
        return StatementType.NONE;
    }

    /**
     * 批量操作
     * @param statement 语句
     * @param parameter 参数
     * @return 影响行
     */
    private int batchExecute(String statement, Object parameter){
        //批量操作参数
        BatchParameterInfo batchParameterInfo=(BatchParameterInfo)parameter;
        List parList=batchParameterInfo.getParameter();

        int result = 0;
        //批量操作的，需要重新生成executor
        final Environment environment = getConfiguration().getEnvironment();
        final TransactionFactory transactionFactory = getTransactionFactoryFromEnvironment(environment);
        Transaction tx = transactionFactory.newTransaction(environment.getDataSource(), null, false);
        final Executor executor = getConfiguration().newExecutor(tx, ExecutorType.BATCH);
        try {
            MappedStatement ms = getConfiguration().getMappedStatement(statement);
            int batchCount = batchParameterInfo.getBatchSize();// 每批commit的个数
            int batchLastIndex = batchCount;// 每批最后一个的下标
            for (int index = 0; index < parList.size();) {
                if (batchLastIndex >= parList.size()) {
                    batchLastIndex = parList.size();
                    executor.update(ms, wrapCollection(parList.subList(index,batchLastIndex)));
                    result += batchLastIndex-index;
                    commit(executor);
                    break;// 数据插入完毕，退出循环
                } else {
                    executor.update(ms, wrapCollection(parList.subList(index,batchLastIndex)));
                    result += batchCount;
                    commit(executor);
                    index = batchLastIndex;// 设置下一批下标
                    batchLastIndex = index + batchCount;
                }
            }
        } catch (Exception e) {
            throw ExceptionFactory.wrapException("Error updating database.  Cause: " + e, e);
        } finally {
            ErrorContext.instance().reset();
            commit(executor);
        }
        return result;
    }

    private Object wrapCollection(final Object object) {
        if (object instanceof Collection) {
            StrictMap<Object> map = new StrictMap<Object>();
            map.put("collection", object);
            if (object instanceof List) {
                map.put("list", object);
            }
            return map;
        } else if (object != null && object.getClass().isArray()) {
            StrictMap<Object> map = new StrictMap<Object>();
            map.put("array", object);
            return map;
        }
        return object;
    }

    private TransactionFactory getTransactionFactoryFromEnvironment(Environment environment) {
        if (environment == null || environment.getTransactionFactory() == null) {
            return new ManagedTransactionFactory();
        }
        return environment.getTransactionFactory();
    }

    private void commit(Executor executor) {
        try {
            executor.commit(true);
        } catch (Exception e) {
            throw ExceptionFactory.wrapException("Error committing transaction.  Cause: " + e, e);
        } finally {
            ErrorContext.instance().reset();
        }
    }
}
