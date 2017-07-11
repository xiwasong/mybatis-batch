package cn.hn.java.summer.mybatis.batch.statement;

import java.util.List;

/**
 * Batch Parameter Info
 * Created by xw2sy on 2017-07-11
 */
public class BatchParameterInfo extends BaseParameterInfo<List> {

    //批量大小
    private int batchSize=200;

    public BatchParameterInfo(List parameter) {
        super(StatementType.BATCH, parameter);
    }

    public BatchParameterInfo(int batchSize, List parameter) {
        super(StatementType.BATCH, parameter);
        this.batchSize=batchSize;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}
