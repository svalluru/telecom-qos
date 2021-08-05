package com.sample.qos;

import java.util.ArrayList;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestPropertyDefinition;
import org.springframework.stereotype.Component;

@Component
public class BillPay extends RouteBuilder {

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
		.post("/billpay")
		.produces("application/json")
		.consumes("application/json")
		.route()
		.to("direct:updateBillPaid")
		.setBody(simple("Bill Paid"))
		;
		
		from("direct:updateBillPaid")
		.log("\n\n*** Final Bill paid for phone number : "+ simple("${body}")+" ***")
		.setBody(simple("update account_closed Set finalamountpaid=true where phoneno = ${body} "))
		.to("direct:callJDBC");
	}
	


}
