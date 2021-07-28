package com.sample.qos;

import java.util.ArrayList;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestPropertyDefinition;
import org.springframework.stereotype.Component;

@Component
public class UnlockDevicePAMTaskProcessor extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		RestPropertyDefinition corsAllowedHeaders = new RestPropertyDefinition();
		corsAllowedHeaders.setKey("Access-Control-Allow-Origin");
		corsAllowedHeaders.setValue("*");

		corsAllowedHeaders.setKey("Access-Control-Allow-Methods");
		corsAllowedHeaders.setValue("GET, HEAD, POST,PUT, DELETE, TRACE, OPTIONS, CONNECT, PATCH");

		corsAllowedHeaders.setKey("Access-Control-Allow-Headers");
		corsAllowedHeaders.setValue("Origin, Accept, X-Requested-With,Content-Type, Access-Control-Request-Method,Access-Control-Request-Headers, Authorization");

		ArrayList<RestPropertyDefinition> corslist = new ArrayList<RestPropertyDefinition>();
		corslist.add(corsAllowedHeaders);
		
		restConfiguration().component("undertow")
		.bindingMode(RestBindingMode.json)
		.port(9377)
	    .contextPath("/camel")
		.enableCORS(true)
		.setCorsHeaders(corslist);

		
		
		rest()
		.post("/phoneunlocked")
		.produces("application/json")
		.to("direct:updatePhoneUnlockDB");
		
		from("direct:updatePhoneUnlockDB")
		.log("\n\n*** Device unlock issue FIXED for phone number : ${body} ***")
		.setBody(simple("update account_closed Set phoneunlocked=true,status='' where phoneno = ${body} "))
		.to("direct:callJDBC");
	}
	


}
