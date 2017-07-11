# mybatis-batch
mybatis batch operate extension for srping

# use guide
##### 1.set BatchSessionFactoryBuilder
```
SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
bean.setSqlSessionFactoryBuilder(new BatchSessionFactoryBuilder());
```
##### 2.add mapping statement
```
<insert id="insertCountries" parameterType="java.util.List">
        insert into country (countryname, countrycode)
        values
        <foreach collection="list" item="item" index="index" separator=",">
            (#{item.name,jdbcType=VARCHAR},#{item.code,jdbcType=VARCHAR} )
        </foreach>
</insert>
```
##### 3.use SqlSessionTemplate 
```
@Autowired
private SqlSessionTemplate template;  

List<Country> countries=new ArrayList<Country>();
for(int i=0;i<count;i++){
  countries.add(new Country("name"+i,"code"+i));
}
BatchParameterInfo batchParameterInfo=new BatchParameterInfo(countries);
template.insert("batch.insertCountries",batchParameterInfo) ;
```
