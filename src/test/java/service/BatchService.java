package service;

import cn.hn.java.summer.mybatis.batch.statement.BatchParameterInfo;
import model.Country;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xw2sy on 2017-07-11.
 */
@Service
public class BatchService {

    @Autowired
    private SqlSessionTemplate template;

    public int batchInsertCountries(int count){
        List<Country> countries=new ArrayList<Country>();
        for(int i=0;i<count;i++){
            countries.add(new Country("name"+i,"code"+i));
        }
        BatchParameterInfo batchParameterInfo=new BatchParameterInfo(countries);
        return template.insert("batch.insertCountries",batchParameterInfo) ;
    }

    public int batchUpdateCountries(int count){
        List<Country> countries=new ArrayList<Country>();
        for(int i=0;i<count;i++){
            countries.add(new Country("name-update"+i,"code"+i));
        }
        BatchParameterInfo batchParameterInfo=new BatchParameterInfo(countries);
        return template.insert("batch.updateCountries",batchParameterInfo) ;
    }

    public int batchDeleteCountries(int count){
        List<Country> countries=new ArrayList<Country>();
        for(int i=0;i<count;i++){
            countries.add(new Country("","code"+i));
        }
        BatchParameterInfo batchParameterInfo=new BatchParameterInfo(countries);
        return template.insert("batch.deleteCountries",batchParameterInfo) ;
    }
}
