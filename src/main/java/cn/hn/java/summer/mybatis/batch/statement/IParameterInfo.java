package cn.hn.java.summer.mybatis.batch.statement;

/**
 * parameter info interface
 * Created by xw2sy on 2017-06-14.
 */
public interface IParameterInfo<T> {

    StatementType getStatementType();

    T getParameter();
}
