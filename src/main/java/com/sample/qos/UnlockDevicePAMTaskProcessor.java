package com.sample.qos;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class UnlockDevicePAMTaskProcessor extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		restConfiguration().component("servlet")
		.bindingMode(RestBindingMode.json);

		
		
		rest()
		.post("/phoneunlocked")
		.produces("application/json")
		.to("direct:updatePhoneUnlockDB");
		
		from("direct:updatePhoneUnlockDB")
		.log("Body content is : ${body}")
		.setBody(simple("update account_closed Set phoneunlocked=true where phoneno = ${body} "))
		.to("direct:callJDBC");
	}
	


}
