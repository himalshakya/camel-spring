package net.shakya.learnCamelSpringBoot.process;

import lombok.extern.slf4j.Slf4j;
import net.shakya.learnCamelSpringBoot.domain.Country;
import net.shakya.learnCamelSpringBoot.domain.Item;
import net.shakya.learnCamelSpringBoot.exceptions.DataException;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Slf4j
@Component
public class BuildCountrySQLProcessor implements org.apache.camel.Processor {

  @Override
  public void process(Exchange exchange) throws Exception {

    Country country = (Country) exchange.getIn().getBody();
    log.info("Country  is : " + country);

    String query = String.format("INSERT INTO COUNTRY (COUNTRY_NAME, COUNTRY_CODE, POPULATION) VALUES ('%s','%s', %s);",
        country.getName(), country.getAlpha3Code(), country.getPopulation());
    log.info("Final Query is : " + query);

    exchange.getIn().setBody(query);
    exchange.getIn().setHeader("countryId", country.getAlpha3Code());
  }
}
