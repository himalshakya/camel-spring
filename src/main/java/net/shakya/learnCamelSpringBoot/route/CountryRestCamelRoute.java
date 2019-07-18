package net.shakya.learnCamelSpringBoot.route;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import net.shakya.learnCamelSpringBoot.domain.Country;
import net.shakya.learnCamelSpringBoot.domain.Item;
import net.shakya.learnCamelSpringBoot.exceptions.DataException;
import net.shakya.learnCamelSpringBoot.process.BuildCountrySQLProcessor;
import net.shakya.learnCamelSpringBoot.process.BuildSQLProcessor;
import net.shakya.learnCamelSpringBoot.process.CountrySelectProcessor;
import net.shakya.learnCamelSpringBoot.process.MailProcessor;
import net.shakya.learnCamelSpringBoot.process.SuccessProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.gson.GsonDataFormat;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CountryRestCamelRoute extends RouteBuilder {

  private Environment environment;
  private DataSource dataSource;

  private BuildCountrySQLProcessor buildSQLProcessor;

//  private CountrySelectProcessor countrySelectProcessor;

  public CountryRestCamelRoute(CamelContext context,
      Environment environment, @Qualifier("dataSource") DataSource dataSource,
      BuildCountrySQLProcessor buildSQLProcessor){
//      SuccessProcessor successProcessor,
//      MailProcessor mailProcessor,
//      CountrySelectProcessor countrySelectProcessor) {
    super(context);
    this.environment = environment;
    this.buildSQLProcessor = buildSQLProcessor;
//    this.countrySelectProcessor = countrySelectProcessor;
  }

  @Override
  public void configure() throws Exception {

    GsonDataFormat countryFormat = new GsonDataFormat(Country.class);

    from("restlet:http://localhost:8081/country?restletMethods=POST")
        .routeId("countryPostRoute")
        .log("Received Body is ${body}")
        .convertBodyTo(String.class)
        .unmarshal(countryFormat)
        .log("Unmarshed record is ${body}")
        .process(buildSQLProcessor)
        .to("{{countryDBNode}}")
        .to("{{countrySelectNode}}")
        .convertBodyTo(String.class)
        .log("Inserted Country is ${body}")
        .end();

  }
}
