package net.shakya.learnCamelSpringBoot.route;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.DisableJmx;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("mock")
@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@DisableJmx(true)
public class SimpleCamelRouteMockTest  extends CamelTestSupport {

  @Autowired
  CamelContext context;

  @Autowired
  Environment environment;

  @Autowired
  protected CamelContext createCamleContext(){
    return context;
  }

  @Autowired
  ProducerTemplate producerTemplate;

  @Ignore
  @Test
  public void testMoveFileMock() throws InterruptedException {

    String message = "type,sku#,itemdescription,price\n"
        + "ADD,100,Samsung TV,500\n"
        + "ADD,100,LG TV,500";

    MockEndpoint mockEndpoint = getMockEndpoint(environment.getProperty("toRoute"));
    mockEndpoint.expectedMessageCount(1);
    mockEndpoint.expectedBodiesReceived(message);

    producerTemplate.sendBodyAndHeader(environment.getProperty("startRoute"),
        message, "env", environment.getProperty("spring.profiles.active"));

    assertMockEndpointsSatisfied();
  }
}
