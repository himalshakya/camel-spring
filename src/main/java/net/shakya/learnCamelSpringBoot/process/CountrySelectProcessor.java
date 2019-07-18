package net.shakya.learnCamelSpringBoot.process;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CountrySelectProcessor implements Processor {

  List<String> countryList = Arrays.asList("us", "in", "gb", "cn", "jp");

  @Override
  public void process(Exchange exchange) throws Exception {
    Random random = new Random();
    String countryCode = countryList.get(random.nextInt(countryList.size() - 1));

    log.info("selected country code is " + countryCode);

    exchange.getIn().setHeader("countryId", countryCode);
  }
}
