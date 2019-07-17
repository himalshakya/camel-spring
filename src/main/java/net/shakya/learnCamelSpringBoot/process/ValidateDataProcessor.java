package net.shakya.learnCamelSpringBoot.process;

import lombok.extern.slf4j.Slf4j;
import net.shakya.learnCamelSpringBoot.domain.ItemJSON;
import net.shakya.learnCamelSpringBoot.exceptions.DataException;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@Slf4j
public class ValidateDataProcessor implements org.apache.camel.Processor {

  @Override
  public void process(Exchange exchange) throws Exception {
    ItemJSON item = (ItemJSON) exchange.getIn().getBody();
    log.info("Item in ValidateDataProcessor : " + item);

    if (ObjectUtils.isEmpty(item.getSku())){
      throw new DataException("Sku is null for " + item.getItemDescription());
    }
  }
}
