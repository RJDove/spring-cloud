package com.oppo.autotest.otest.config.database;

import com.github.pagehelper.PageInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yanghuan
 */
@Configuration
@AutoConfigureAfter(DataSourceConfiguration.class)
@MapperScan("com.oppo.autotest.otest.mapper")
@Slf4j
public class SqlSessionConfiguration {
    @Value("${mysql.datasource.readSize}")
    private String readDataSourceSize;

    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;

    @Value("${mybatis.type-aliases-package}")
    private String typeAliasesPackage;

    @Value("${mybatis.configuration.cache-enabled}")
    private Boolean cacheEnabled;

    @Value("${mybatis.configuration.local-cache-scope}")
    private String localCacheScope;

    @Value("${pagehelper.helper-dialect}")
    private String helperDialect;

    @Value("${pagehelper.reasonable}")
    private String reasonable;

    @Value("${pagehelper.support-methods-arguments}")
    private String supportMethodsArguments;

    @Value("${pagehelper.params}")
    private String params;

    @Resource(name = "writeDataSource")
    private DataSource writeDataSource;

    @Resource(name = "readDataSource01")
    private DataSource readDataSource01;

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        try {
            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(routingDataSourceProxy());
            sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
            sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(
                    mapperLocations));
            sqlSessionFactoryBean.setConfiguration(mybatisConfig());
            Interceptor[] plugins = new Interceptor[]{pageInterceptor()};
            sqlSessionFactoryBean.setPlugins(plugins);
            return sqlSessionFactoryBean.getObject();
        } catch (IOException e) {
            log.error("mybatis resolver mapper*xml is error", e);
            return null;
        } catch (Exception e) {
            log.error("mybatis sqlSessionFactoryBean create error", e);
            return null;
        }
    }

    @Bean(name = "routingDataSourceProxy")
    public AbstractRoutingDataSource routingDataSourceProxy() {
        Map<Object, Object> targetDataSources = new HashMap<>(4);
        targetDataSources.put(DataSourceType.WRITE.getType(), writeDataSource);
        targetDataSources.put(DataSourceType.READ.getType() + "1", readDataSource01);
        /*targetDataSources.put(DataSourceType.READ.getType()+"2", readDataSource02);*/
        final int readSize = Integer.parseInt(readDataSourceSize);

        AbstractRoutingDataSource proxy = new AbstractRoutingDataSource() {
            private AtomicInteger count = new AtomicInteger(0);

            @Override
            protected Object determineCurrentLookupKey() {
                String typeKey = DataSourceContextHolder.getReadOrWrite();
                if (null == typeKey) {
                    return DataSourceType.WRITE.getType();
//                    throw new NullPointerException("数据库路由时，决定使用哪个数据库源类型不能为空...");
                }

                if (typeKey.equals(DataSourceType.WRITE.getType())) {
                    logger.warn("use write database");
                    return DataSourceType.WRITE.getType();
                }

                logger.warn("use read database");
                int number = count.getAndAdd(1);
                count.compareAndSet(1000000000, 0);
                int lookupKey = number % readSize;
                log.warn("use read-{}", lookupKey + 1);
                return DataSourceType.READ.getType() + (lookupKey + 1);
            }
        };
        proxy.setDefaultTargetDataSource(writeDataSource);
        proxy.setTargetDataSources(targetDataSources);
        return proxy;
    }

    @Bean(name = "mybatisConfig")
    public org.apache.ibatis.session.Configuration mybatisConfig() {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setCacheEnabled(cacheEnabled);
        if ("session".equals(localCacheScope)) {
            configuration.setLocalCacheScope(LocalCacheScope.SESSION);
        } else {
            configuration.setLocalCacheScope(LocalCacheScope.STATEMENT);
        }
        return configuration;
    }

    @Bean
    public PageInterceptor pageInterceptor() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        /*PageHelper pageHelper = new PageHelper();*/
        Properties p = new Properties();
        p.setProperty("helperDialect", helperDialect);
        p.setProperty("reasonable", reasonable);
        p.setProperty("supportMethodsArguments", supportMethodsArguments);
        p.setProperty("params", params);
        /*pageHelper.setProperties(p);*/
        pageInterceptor.setProperties(p);
        return pageInterceptor;
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(
            @Qualifier("routingDataSourceProxy") AbstractRoutingDataSource routingDataSourceProxy) {
        // 若要使用事务，切换数据源的方式要做改变，需要在Service层指定数据源
        return new DataSourceTransactionManager(routingDataSourceProxy);
    }
}
