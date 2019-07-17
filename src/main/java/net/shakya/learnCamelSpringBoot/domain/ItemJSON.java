package net.shakya.learnCamelSpringBoot.domain;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ItemJSON {

  private String transactionType;
  private String sku;
  private String itemDescription;
  private BigDecimal price;

}
