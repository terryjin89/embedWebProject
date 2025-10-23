package org.embed.comfiguration;


import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 데이터베이스와 MyBatis 연동에 관련된 설정을 담당하는 클래스입니다.
 * @Configuration: 이 클래스가 Spring의 설정 파일임을 나타냅니다.
 * @PropertySource: 참조할 properties 파일의 위치를 지정합니다. 여기서는 application.properties를 사용합니다.
 */
@Configuration
@PropertySource("classpath:/application.properties")
public class DBConfiguration {
	
	// Spring의 ApplicationContext를 주입받습니다. 주로 리소스(파일)를 읽어올 때 사용됩니다.
	@Autowired
	private ApplicationContext applicationContext;
	
	/**
	 * HikariCP(Connection Pool)의 설정을 담당하는 Bean을 생성합니다.
	 * @ConfigurationProperties: application.properties 파일에서 "spring.datasource.hikari" 접두사를 가진 속성들을
	 *                           읽어와 HikariConfig 객체에 자동으로 바인딩(매핑)합니다.
	 * @return HikariCP 설정 객체
	 */
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public HikariConfig hikariConfig() {
		return new HikariConfig();
	}
	
	/**
	 * 데이터 소스(DataSource) Bean을 생성합니다.
	 * 데이터베이스와의 연결을 관리하는 Connection Pool을 생성하는 역할을 합니다.
	 * 개선된 코드에서는 hikariConfig() 메소드를 직접 호출하는 대신,
	 * Spring 컨테이너에 의해 관리되는 HikariConfig Bean을 파라미터로 주입받아 의존성을 명확히 합니다.
	 * 
	 * @param hikariConfig HikariCP 설정 Bean
	 * @return 설정이 적용된 HikariDataSource 객체
	 */
	@Bean
	public DataSource dataSource(HikariConfig hikariConfig) {
		return new HikariDataSource(hikariConfig);
		
		/*
		// 이전 코드
		@Bean
		public DataSource dataSource() {
			return new HikariDataSource(hikariConfig());
		}
		*/
	}
	
	/**
	 * MyBatis의 SqlSessionFactory Bean을 생성합니다.
	 * SqlSessionFactory는 SQL 실행의 핵심 객체인 SqlSession을 생성하는 팩토리 역할을 합니다.
	 * 
	 * @param dataSource 위에서 생성한 DataSource Bean을 주입받습니다.
	 * @return SqlSessionFactory 객체
	 * @throws Exception
	 */
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		
		// SqlSessionFactory를 생성하기 위한 FactoryBean을 사용합니다.
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		
		// 데이터 소스를 설정합니다.
		factoryBean.setDataSource(dataSource);
		
		// MyBatis 매퍼 XML 파일의 위치를 지정합니다.
		// "classpath:/mapper/**/sql-*.xml" 패턴은 mapper 폴더와 그 하위 폴더에 있는
		// "sql-"로 시작하고 ".xml"로 끝나는 모든 파일을 매퍼로 등록하겠다는 의미입니다.
		factoryBean.setMapperLocations(applicationContext.getResources("classpath:/mapper/**/sql-*.xml"));
		
		// DTO(VO) 클래스가 위치한 패키지를 지정합니다.
		// 매퍼 XML에서 resultType 등을 사용할 때 패키지 경로를 생략하고 클래스명만 사용할 수 있게 해줍니다.
		factoryBean.setTypeAliasesPackage("org.embed.domain");
		
		// MyBatis의 추가 설정을 적용합니다.
		factoryBean.setConfiguration(mybatisConfig());
		
		// 설정이 완료된 factoryBean을 통해 SqlSessionFactory 객체를 생성하여 반환합니다.
		return factoryBean.getObject();
	}
	
	/**
	 * MyBatis의 SqlSessionTemplate Bean을 생성합니다.
	 * SqlSessionTemplate은 SqlSession을 대체하는 Spring의 핵심 클래스로,
	 * Thread-safe하게 동작하며 데이터베이스의 commit, close 등을 자동으로 처리해줍니다.
	 * 
	 * @param sqlSessionFactory 위에서 생성한 SqlSessionFactory Bean을 주입받습니다.
	 * @return SqlSessionTemplate 객체
	 * @throws Exception
	 */
	@Bean
	public SqlSessionTemplate sessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	
	/**
	 * MyBatis의 전역 설정을 담당하는 Configuration Bean을 생성합니다.
	 * @ConfigurationProperties: application.properties에서 "mybatis.configuration" 접두사를 가진 속성들을
	 *                           읽어와 MyBatis Configuration 객체에 자동으로 바인딩합니다.
	 *                           (예: map-underscore-to-camel-case=true)
	 * @return MyBatis 설정 객체
	 */
	@Bean
	@ConfigurationProperties(prefix = "mybatis.configuration")
	public org.apache.ibatis.session.Configuration mybatisConfig() {
		return new org.apache.ibatis.session.Configuration();
	}
}