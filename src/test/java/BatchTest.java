import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import service.BatchService;

/**
 * Created by xw2sy on 2017-07-11.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class BatchTest {

    @Autowired
    BatchService batchService;

    @Test
    public void testInsert(){
        int count=1000;
        int result=batchService.batchInsertCountries(count);
        Assert.assertEquals(count,result);
    }


    @Test
    public void testUpdate(){
        int count=1000;
        int result=batchService.batchUpdateCountries(count);
        Assert.assertEquals(count,result);
    }

    @Test
    public void testDelete(){
        int count=1000;
        int result=batchService.batchDeleteCountries(count);
        Assert.assertEquals(count,result);
    }
}
