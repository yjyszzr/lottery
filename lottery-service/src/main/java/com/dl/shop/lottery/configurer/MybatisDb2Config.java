package com.dl.shop.lottery.configurer;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@MapperScan(basePackages= {"com.dl.shop.lottery.dao2"}, sqlSessionFactoryRef="sqlSessionFactory2")
public class MybatisDb2Config {

	@Resource(name="dataSource2")
	private DataSource dataSource;
	@Resource
	private DataSourceConfig dataSourceConfig;
	
	@Bean(name="sqlSessionFactory2")
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        /*org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        factoryBean.setConfiguration(configuration);*/
        factoryBean.setDataSource(dataSource);
        factoryBean.setTypeAliasesPackage(dataSourceConfig.getMybatisTypeAliasesPackage());
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(dataSourceConfig.getMybatisMapperLocations()));
        return factoryBean.getObject();
	}
	
	@Bean(name="sqlSessionTemplate2")
	public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory1")SqlSessionFactory sqlSessionFactory) throws Exception {
		SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
		return sqlSessionTemplate;
	}
	
    @Bean(name="transactionManager2")
    public PlatformTransactionManager dBbTransactionManager() {
    	return new DataSourceTransactionManager(dataSource);
    }
}
