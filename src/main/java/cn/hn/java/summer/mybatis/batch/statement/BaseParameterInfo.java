package cn.hn.java.summer.mybatis.batch.statement;

/**
 * Base Parameter Info
 * Created by xw2sy on 2017-06-12.
 */
public class BaseParameterInfo<T> implements IParameterInfo<T> {

    /* 操作语句类型 */
    private StatementType statementType=StatementType.NONE;

    /* 参数 */
    private T parameter;

    BaseParameterInfo(StatementType statementType, T parameter) {
        this.statementType = statementType;
        this.parameter = parameter;
    }

    public StatementType getStatementType() {
        return statementType;
    }

    public void setStatementType(StatementType statementType) {
        this.statementType = statementType;
    }

    public T getParameter() {
        return parameter;
    }

    public void setParameter(T parameter) {
        this.parameter = parameter;
    }
}
