package net.shakya.learnCamelSpringBoot.route;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import net.shakya.learnCamelSpringBoot.domain.Item;
import net.shakya.learnCamelSpringBoot.process.BuildSQLProcessor;
import net.shakya.learnCamelSpringBoot.process.SuccessProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SimpleCamelRoute extends RouteBuilder {

  private Environment environment;
  private DataSource dataSource;
  private BuildSQLProcessor buildSQLProcessor;
  private SuccessProcessor successProcessor;

  public SimpleCamelRoute(CamelContext context,
      Environment environment, @Qualifier("dataSource") DataSource dataSource,
      BuildSQLProcessor buildSQLProcessor, SuccessProcessor successProcessor) {
    super(context);
    this.environment = environment;
    this.dataSource = dataSource;
    this.buildSQLProcessor = buildSQLProcessor;
    this.successProcessor = successProcessor;
  }

  @Override
  public void configure() throws Exception {

    log.debug("Starting the Camel Route");

    DataFormat bindy = new BindyCsvDataFormat(Item.class);

    from("{{startRoute}}")
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

    log.debug("Ending the Camel Route");
  }
}
