package com.sample.qos;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class UnlockDeviceProcessor extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("kafka:unlock-phone?brokers=localhost:9092" +
		"&seekTo=beginning")
		.log("******* Device Unlocked : ${body} *******")
		.setBody(simple("update account_closed Set phoneunlocked=true where phoneno = ${body} "))
		.to("direct:callJDBC");

		
		
	}

}
