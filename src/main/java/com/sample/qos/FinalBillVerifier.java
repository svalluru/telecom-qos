package com.sample.qos;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FinalBillVerifier extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("file:/Users/svalluru/tmp/closed_accounts")
		.log("${body}");
		
	}

}
