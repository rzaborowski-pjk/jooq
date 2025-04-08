package pjk.rafzab.jooq.config;


import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

@Configuration
public class InitialJooqConfiguration {

    @Autowired
    private DataSource dataSource;

//    @Bean
//    public DataSourceConnectionProvider connectionProvider() {
//        return new DataSourceConnectionProvider
//                (new TransactionAwareDataSourceProxy(dataSource));
//    }

//    @Bean
//    public DSLContext ctx() {
//        return new DefaultDSLContext(configuration());
//
//    }

    public org.jooq.Configuration configuration() {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.set(dataSource);
        jooqConfiguration.set(SQLDialect.POSTGRES);
//        jooqConfiguration.set(connectionProvider());

        return jooqConfiguration;
    }
}
