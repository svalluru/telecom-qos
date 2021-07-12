package com.sample.qos;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class AccountClosedProcessor extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("kafka:account-closed?brokers=localhost:9092" +
		"&seekTo=beginning")
		.multicast()
		.log("******* Account Closed : ${body} *******")
		.setBody(simple("update account_closed Set accountclosed=true where phoneno = ${body} "))
		.to("direct:callJDBC, direct:sendemail");
		
		from("direct:sendemail")
		.log("From : noreply@telecom.com\n"
				+ "To : customer@yahoo.com\n"
				+ "\n"
				+ "Subject : Account closed for phone number ${body}\n"
				+ "\n"
				+ "Total Due Amount : 0");
	}

}
