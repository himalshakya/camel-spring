package net.shakya.learnCamelSpringBoot.route;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import net.shakya.learnCamelSpringBoot.domain.Item;
import net.shakya.learnCamelSpringBoot.domain.ItemJSON;
import net.shakya.learnCamelSpringBoot.exceptions.DataException;
import net.shakya.learnCamelSpringBoot.process.BuildJSONSQLProcessor;
import net.shakya.learnCamelSpringBoot.process.BuildSQLProcessor;
import net.shakya.learnCamelSpringBoot.process.MailProcessor;
import net.shakya.learnCamelSpringBoot.process.SuccessProcessor;
import net.shakya.learnCamelSpringBoot.process.ValidateDataProcessor;
import org.apache.camel.CamelContext;
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
public class ActiveMQCamelRoute extends RouteBuilder {

  private Environment environment;
  private DataSource dataSource;

  private BuildJSONSQLProcessor buildSQLProcessor;
  private SuccessProcessor successProcessor;
  private MailProcessor mailProcessor;

  private ValidateDataProcessor validateDataProcessor;

  public ActiveMQCamelRoute(CamelContext context,
      Environment environment, @Qualifier("dataSource") DataSource dataSource,
      BuildJSONSQLProcessor buildSQLProcessor, SuccessProcessor successProcessor,
      MailProcessor mailProcessor, ValidateDataProcessor validateDataProcessor) {
    super(context);
    this.environment = environment;
    this.dataSource = dataSource;
    this.buildSQLProcessor = buildSQLProcessor;
    this.successProcessor = successProcessor;
    this.mailProcessor = mailProcessor;
    this.validateDataProcessor = validateDataProcessor;
  }

  @Override
  public void configure() throws Exception  {

    log.debug("Starting the Camel Route");

    GsonDataFormat itemJSONFormat = new GsonDataFormat(ItemJSON.class);

//    errorHandler(deadLetterChannel("log:errorInRoute?level=ERROR&showProperties=true")
//        .maximumRedeliveries(3)
//        .redeliveryDelay(3000)
//        .backOffMultiplier(2)
//        .retryAttemptedLogLevel(LoggingLevel.ERROR));

    onException(PSQLException.class)
        .log(LoggingLevel.ERROR, "PSQLException in the route ${body}")
        .maximumRedeliveries(3)
        .redeliveryDelay(3000)
        .backOffMultiplier(2)
        .retryAttemptedLogLevel(LoggingLevel.ERROR);

    onException(DataException.class)
        .log(LoggingLevel.ERROR, "Exception in the route ${body}")
        .process(mailProcessor)
        .to("{{errorActiveMQRoute}}");

    from("{{fromActiveMQRoute}}")
        .routeId("activeMQRoute")
        .log("Read Message from activemq ${body}")
        .unmarshal(itemJSONFormat)
        .log("Unmarshalled message is ${body}")
        .process(validateDataProcessor)
        .process(buildSQLProcessor)
        .to("{{toActiveMQRoute}}")
        .to("{{selectNode}}")
        .end();


    /*from("{{startRoute}}")
        .routeId("mainRoute")
        .log("Timer Invoked and the body in " + environment.getProperty("message"))
        .choice()
          .when((header("env").isNotEqualTo("mock")))
            .pollEnrich("{{fromRoute}}")
          .otherwise()
            .log("mock env flow and the body is ${body}")
          .end()
        .to("{{toRoute}}")
        .unmarshal(bindy)
        .log("The unmarhalled object is ${body}")
        .split(body())
          .log("Record is ${body}")
          .process(buildSQLProcessor)
          .to("{{toRouteDB}}")
        .end()
        .process(successProcessor)
        .to("{{toRouteSuccess}}");
*/
    log.debug("Ending the Camel Route");
  }
}
