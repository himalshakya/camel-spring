package net.shakya.learnCamelSpringBoot.process;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HealthCheckProcessor implements org.apache.camel.Processor {

  @Override
  public void process(Exchange exchange) throws Exception {
    String healthCheckResult = exchange.getIn().getBody(String.class);

    log.info("Health check String of the APP is : " + healthCheckResult);

    ObjectMapper objectMapper = new ObjectMapper();

    Map<String, Object> map = objectMapper.readValue(healthCheckResult, new TypeReference<Map<String, Object>>(){});

    log.info("map read is : " + map);

    StringBuilder builder = null;
    for(String key : map.keySet()){
      if (map.get(key).toString().contains("DOWN")){
        if (builder == null){
          builder =  new StringBuilder();
        }

        builder.append(key + " component in the route is down\n");
      }
    }

    if (builder != null){
      log.info("Exception message is : " + builder.toString());
      exchange.getIn().setHeader("error", true);
      exchange.getIn().setBody(builder.toString());
      exchange.setProperty(Exchange.EXCEPTION_CAUGHT, builder.toString());
    }

  }
}
