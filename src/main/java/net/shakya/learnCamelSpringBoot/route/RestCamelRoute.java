package net.shakya.learnCamelSpringBoot.route;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import net.shakya.learnCamelSpringBoot.domain.Item;
import net.shakya.learnCamelSpringBoot.exceptions.DataException;
import net.shakya.learnCamelSpringBoot.process.BuildSQLProcessor;
import net.shakya.learnCamelSpringBoot.process.CountrySelectProcessor;
import net.shakya.learnCamelSpringBoot.process.MailProcessor;
import net.shakya.learnCamelSpringBoot.process.SuccessProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RestCamelRoute extends RouteBuilder {

  private Environment environment;
  private DataSource dataSource;

  private BuildSQLProcessor buildSQLProcessor;
  private SuccessProcessor successProcessor;
  private MailProcessor mailProcessor;

  private CountrySelectProcessor countrySelectProcessor;

  public RestCamelRoute(CamelContext context,
      Environment environment, @Qualifier("dataSource") DataSource dataSource,
      BuildSQLProcessor buildSQLProcessor, SuccessProcessor successProcessor,
      MailProcessor mailProcessor, CountrySelectProcessor countrySelectProcessor) {
    super(context);
    this.environment = environment;
    this.dataSource = dataSource;
    this.buildSQLProcessor = buildSQLProcessor;
    this.successProcessor = successProcessor;
    this.mailProcessor = mailProcessor;
    this.countrySelectProcessor = countrySelectProcessor;
  }

  @Override
  public void configure() throws Exception {

    log.debug("Starting the Camel Route");

    DataFormat bindy = new BindyCsvDataFormat(Item.class);

    errorHandler(deadLetterChannel("log:errorInRoute?level=ERROR&showProperties=true")
        .maximumRedeliveries(3)
        .redeliveryDelay(3000)
        .backOffMultiplier(2)
        .retryAttemptedLogLevel(LoggingLevel.ERROR));

    onException(PSQLException.class)
        .log(LoggingLevel.ERROR, "PSQLException in the route ${body}")
        .maximumRedeliveries(3)
        .redeliveryDelay(3000)
        .backOffMultiplier(2)
        .retryAttemptedLogLevel(LoggingLevel.ERROR);

    onException(DataException.class)
        .log(LoggingLevel.ERROR, "Exception in the route ${body}")
        .process(mailProcessor);

    from("{{fromRESTRoute}}")
        .routeId("restRoute")
        .process(countrySelectProcessor)
          .setHeader(Exchange.HTTP_METHOD, constant("GET"))
          .setHeader(Exchange.HTTP_URI, simple("https://restcountries.eu/rest/v2/alpha/${header.countryId}"))
        .to("https://restcountries.eu/rest/v2/alpha/us")
        .convertBodyTo(String.class)
        .log("The REST API response is ${body}")
        .removeHeader(Exchange.HTTP_URI)
        .setHeader(Exchange.HTTP_METHOD, constant("POST"))
        .to("{{toRESTRoute}}")
        .end();


    log.debug("Ending the Camel Route");
  }
}
