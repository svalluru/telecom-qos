package com.sample.qos;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class InitAccountClose extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		restConfiguration().component("servlet")
		.bindingMode(RestBindingMode.json);

		
		rest()
		.get("/accountclose")
		.produces("application/json")
		.consumes("application/json")
		.to("kafka:close-account?brokers=localhost:9092");
		
	}
	


}
