package net.shakya.learnCamelSpringBoot.route;

import javax.sql.DataSource;
import net.shakya.learnCamelSpringBoot.domain.ItemJSON;
import net.shakya.learnCamelSpringBoot.process.BuildSQLProcessor;
import net.shakya.learnCamelSpringBoot.process.MailProcessor;
import net.shakya.learnCamelSpringBoot.process.SuccessProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.DisableJmx;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("mock")
@RunWith(CamelSpringBootRunner.class)
//@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@DisableJmx(true)
public class KafkaCamelRouteMockTest extends CamelTestSupport {

////  @Mock
//  CamelContext context = new DefaultCamelContext();
//
//  @Autowired
//  Environment environment;
//
//  @Autowired
//  protected CamelContext createCamleContext(){
//    return context;
//  }
//
//
//  ProducerTemplate producerTemplate = context.createProducerTemplate();
//
//
//  ConsumerTemplate consumerTemplate = context.createConsumerTemplate();
//
//
//  @Override
//  protected RouteBuilder createRouteBuilder(){
//    return new KafkaCamelRoute(createCamleContext(), environment);
//  }
//
//  @Ignore
//  @Test
//  public void testMoveFileMock() throws InterruptedException {
//
//    String input = "{\"transactionType\":\"ADD\", \"sku\":\"100\", \"itemDescription\":\"SamsungTV\", \"price\":\"500.00\"}";
//
//    ItemJSON item = (ItemJSON) producerTemplate.requestBodyAndHeader("direct:input", input, "env", "mock");
//
//    System.out.println(item);
//    assertEquals("100", item.getSku());
//  }
}
