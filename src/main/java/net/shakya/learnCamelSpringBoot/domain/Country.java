package net.shakya.learnCamelSpringBoot.domain;

import lombok.Data;

@Data
public class Country {
  private String name;
  private String alpha3Code;
  private String population;
}
