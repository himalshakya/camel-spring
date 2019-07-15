package net.shakya.learnCamelSpringBoot.process;

import lombok.extern.slf4j.Slf4j;
import net.shakya.learnCamelSpringBoot.domain.Item;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BuildSQLProcessor implements org.apache.camel.Processor {

  @Override
  public void process(Exchange exchange) throws Exception {

    Item item = (Item) exchange.getIn().getBody();
    log.info("Item in Processor is : " + item);
    String query = getQuery(item);
    log.info("Final Query is : " + query);
    exchange.getIn().setBody(query);

  }

  private String getQuery(Item item){
    if (item.getTransactionType().equalsIgnoreCase("ADD")){
      return String.format("INSERT INTO ITEMS (SKU, ITEM_DESCRIPTION, PRICE) VALUES ('%s','%s', %.2f)",
          item.getSku(), item.getItemDescription(), item.getPrice().floatValue());
    }
    if (item.getTransactionType().equalsIgnoreCase("UPDATE")){
      return String.format("UPDATE ITEMS SET PRICE = %.2f, ITEM_DESCRIPTION = '%s' WHERE SKU = '%s'",
          item.getPrice().floatValue(), item.getItemDescription(), item.getSku());
    }
    if (item.getTransactionType().equalsIgnoreCase("DELETE")){
      return String.format("DELETE FROM ITEMS WHERE SKU = '%s'",
          item.getSku());
    }
    return "";
  }
}
