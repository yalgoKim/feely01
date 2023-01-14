package com.project.feeely.config;

import com.project.feeely.dto.enums.Roles;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
@Configuration
@MapperScan(value = "com.project.feeely.mapper" , sqlSessionFactoryRef = "SqlSessionFactory") // mapper를 찾아서 sqlsf 로 올려준다
public class DataSourceConfig {
    @Value("${mybatis.mapper-locations}")
    String Path;

    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource") // yml의 spring : datasource를 읽는다 이 데이터 소스를 자바로 만들겠다 하는 것 ! create bulid 하면 데이터소스가 완성됨
    public DataSource DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "SqlSessionFactory") //위에 매퍼스캔의 주소값과 맞춰줘
    // SqlSessionFactory : java랑 db를 연결해주는 jdbc 템플릿 같은 것
    public SqlSessionFactory SqlSessionFactory(@Qualifier("dataSource") DataSource DataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(DataSource);
        // MyBatis Alias , xml Source Mapping
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources(Path)); // applicationContext : spring의 실행 환경, 여기서 getResources 하겠다 : PATH 경로에 있는 xml을 다 path로 읽겠다
        sqlSessionFactoryBean.setTypeAliasesPackage("com.project.feeely.dto"); // mapper의 parametertype 패키지를 잘라준거
        sqlSessionFactoryBean.setTypeHandlers(new Roles.TypeHandler()); // 여러개 넣고싶으면 배열로 넣으면 됩니다
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "SessionTemplate")
    // SqlSessionFactory 만든 걸 template에 넣어줌
    public SqlSessionTemplate SqlSessionTemplate(@Qualifier("SqlSessionFactory") SqlSessionFactory firstSqlSessionFactory) {
        return new SqlSessionTemplate(firstSqlSessionFactory);
    }
}
