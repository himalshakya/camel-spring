package net.shakya.learnCamelSpringBoot.route;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("dev")
@RunWith(CamelSpringBootRunner.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
public class KafkaCamelRouteTest {

  @Autowired
  ProducerTemplate producerTemplate;

  @Autowired
  ConsumerTemplate consumerTemplate;

  @Autowired
  Environment environment;


  @Test
  public void kafkaRouteTest() throws InterruptedException {
    String input = "{\"transactionType\":\"ADD\", \"sku\":\"200\", \"itemDescription\":\"SamsungTV\", \"price\":\"500.00\"}";
    String response = (String) producerTemplate.requestBody("kafka:inputItemTopic?brokers=localhost:9092", input);
    assertNotNull(response);
  }

  @Test
  public void kafkaRouteErrorTest() throws InterruptedException {
    String input = "{\"transactionType\":\"ADD\", \"sku\":\"\", \"itemDescription\":\"SamsungTV\", \"price\":\"500.00\"}";
    producerTemplate.sendBody("kafka:inputItemTopic?brokers=localhost:9092", input);
    String response = (String) consumerTemplate.receiveBody("kafka:errorTopic?brokers=localhost:9092");

    System.out.println("Response is  : " + response);
    assertNotNull(response);
  }
}
