package net.shakya.learnCamelSpringBoot.route;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import net.shakya.learnCamelSpringBoot.process.CountrySelectProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
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
public class CountryRestCamelRouteTest {


  @Autowired
  ProducerTemplate producerTemplate;



  @Test
  public void testPersistCountry() throws InterruptedException {
    String input = "{\"name\":\"United Kingdom of Great Britain and Northern Ireland\",\"topLevelDomain\":[\".uk\"],\"alpha2Code\":\"GB\",\"alpha3Code\":\"GBR\",\"callingCodes\":[\"44\"],\"capital\":\"London\",\"altSpellings\":[\"GB\",\"UK\",\"Great Britain\"],\"region\":\"Europe\",\"subregion\":\"Northern Europe\",\"population\":65110000,\"latlng\":[54.0,-2.0],\"demonym\":\"British\",\"area\":242900.0,\"gini\":34.0,\"timezones\":[\"UTC-08:00\",\"UTC-05:00\",\"UTC-04:00\",\"UTC-03:00\",\"UTC-02:00\",\"UTC\",\"UTC+01:00\",\"UTC+02:00\",\"UTC+06:00\"],\"borders\":[\"IRL\"],\"nativeName\":\"United Kingdom\",\"numericCode\":\"826\",\"currencies\":[{\"code\":\"GBP\",\"name\":\"British pound\",\"symbol\":\"£\"}],\"languages\":[{\"iso639_1\":\"en\",\"iso639_2\":\"eng\",\"name\":\"English\",\"nativeName\":\"English\"}],\"translations\":{\"de\":\"Vereinigtes Königreich\",\"es\":\"Reino Unido\",\"fr\":\"Royaume-Uni\",\"ja\":\"イギリス\",\"it\":\"Regno Unito\",\"br\":\"Reino Unido\",\"pt\":\"Reino Unido\",\"nl\":\"Verenigd Koninkrijk\",\"hr\":\"Ujedinjeno Kraljevstvo\",\"fa\":\"بریتانیای کبیر و ایرلند شمالی\"},\"flag\":\"https://restcountries.eu/data/gbr.svg\",\"regionalBlocs\":[{\"acronym\":\"EU\",\"name\":\"European Union\",\"otherAcronyms\":[],\"otherNames\":[]}],\"cioc\":\"GBR\"}";

    String countryList = producerTemplate.requestBody("http://localhost:8081/country", input, String.class);

    System.out.println(countryList);

    Assert.assertNotNull(countryList);
  }
}
