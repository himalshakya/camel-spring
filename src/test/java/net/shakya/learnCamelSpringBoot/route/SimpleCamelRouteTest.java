package net.shakya.learnCamelSpringBoot.route;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
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
public class SimpleCamelRouteTest {

  @Autowired
  ProducerTemplate producerTemplate;

  @Autowired
  Environment environment;

  private static final String INPUT_DIR = "data/dev/input";
  private static final String OUTPUT_DIR = "data/dev/output";

  @BeforeClass
  public static  void startCleanup() throws IOException {
    FileUtils.cleanDirectory(new File(INPUT_DIR));
    FileUtils.cleanDirectory(new File(OUTPUT_DIR));
  }

  @Test
  public void testMoveFile() throws InterruptedException {
    String message = "type,sku#,itemdescription,price\n"
        + "ADD,100,Samsung TV,500\n"
        + "ADD,100,LG TV,500";

    String filename = "fileTest.txt";

    producerTemplate.sendBodyAndHeader(environment.getProperty("fromRoute"), message, Exchange.FILE_NAME, filename);
    Thread.sleep(3000);

    File outFile = new File("data/dev/output/" + filename);
    Assert.assertTrue(outFile.exists());
  }

  @Test
  public void testMoveFile_ADD() throws InterruptedException, IOException {
    String message = "type,sku#,itemdescription,price\n"
        + "ADD,100,Samsung TV,500\n"
        + "ADD,101,LG TV,500";

    String filename = "fileAddTest.txt";

    producerTemplate.sendBodyAndHeader(environment.getProperty("fromRoute"), message, Exchange.FILE_NAME, filename);
    Thread.sleep(3000);

    File outFile = new File("data/dev/output/" + filename);
    Assert.assertTrue(outFile.exists());

    String outputMessage = "Data Updated Successfully";
    String output = new String(Files.readAllBytes(Paths.get("data/output/Success.txt")));
    Assert.assertEquals(outputMessage, output);
  }

  @Test
  public void testMoveFile_UPDATE() throws InterruptedException, IOException {
    String message = "type,sku#,itemdescription,price\n"
        + "UPDATE,100,Samsung TV,600";

    String filename = "fileUpdateTest.txt";

    producerTemplate.sendBodyAndHeader(environment.getProperty("fromRoute"), message, Exchange.FILE_NAME, filename);
    Thread.sleep(3000);

    File outFile = new File("data/dev/output/" + filename);
    Assert.assertTrue(outFile.exists());

    String outputMessage = "Data Updated Successfully";
    String output = new String(Files.readAllBytes(Paths.get("data/output/Success.txt")));
    Assert.assertEquals(outputMessage, output);
  }

  @Test
  public void testMoveFile_DELETE() throws InterruptedException, IOException {
    String message = "type,sku#,itemdescription,price\n"
        + "DELETE,100,Samsung TV,600";

    String filename = "fileDeleteTest.txt";

    producerTemplate.sendBodyAndHeader(environment.getProperty("fromRoute"), message, Exchange.FILE_NAME, filename);
    Thread.sleep(3000);

    File outFile = new File("data/dev/output/" + filename);
    Assert.assertTrue(outFile.exists());

    String outputMessage = "Data Updated Successfully";
    String output = new String(Files.readAllBytes(Paths.get("data/output/Success.txt")));
    Assert.assertEquals(outputMessage, output);
  }
}
