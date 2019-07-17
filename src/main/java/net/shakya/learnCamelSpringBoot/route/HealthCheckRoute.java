package net.shakya.learnCamelSpringBoot.route;

import lombok.extern.slf4j.Slf4j;
import net.shakya.learnCamelSpringBoot.process.HealthCheckProcessor;
import net.shakya.learnCamelSpringBoot.process.MailProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class HealthCheckRoute extends RouteBuilder {

  HealthCheckProcessor healthCheckProcessor;
  MailProcessor mailProcessor;

  public HealthCheckRoute(
      HealthCheckProcessor healthCheckProcessor, MailProcessor mailProcessor) {
    this.healthCheckProcessor = healthCheckProcessor;
    this.mailProcessor = mailProcessor;
  }

  @Override
  public void configure() throws Exception {
      from("{{healthRoute}}")
          .routeId("healthRoute")
          .pollEnrich("http://localhost:8080/actuator/health")
          .process(healthCheckProcessor)
          .choice()
            .when(header("error").isEqualTo(true))
            .process(mailProcessor);

     /*
    from("{{healthRoute}}")
        .routeId("healthRoute")
        .choice()
        .when(isNotMock)
        .pollEnrich("http://localhost:8080/actuator")
        .end()
        .process(healthCheckProcessor)
        .choice()
        .when(header("error").isEqualTo(true))
        .choice()
        .when(isNotMock)
        .process(mailProcessor)
        .end()
        .end();

    */
  }
}
