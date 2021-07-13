package com.sample.qos;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class UnlockDeviceProcessor extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		restConfiguration().component("undertow").bindingMode(RestBindingMode.json);
		
		onException(Exception.class).continued(true)
			.setHeader("Authorization", constant("Basic cGFtYWRtaW46cGFtYWRtaW4xIQ=="))
			.setHeader("Content-Type", constant("application/json"))
			.setBody(simple("{}"))
			.to("rest:post:kie-server/services/rest/server/containers/telecom_1.0.0-SNAPSHOT/processes/telecom.UnlockDeviceIssue/instances?host=localhost:8080");

		from("kafka:unlock-phone?brokers=localhost:9092" +
				"&seekTo=beginning")
		
		.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				ProducerTemplate template = exchange.getContext().createProducerTemplate();

				String phoneno = exchange.getIn().getBody(String.class);

				System.out.println("\n\n*** Device unlock STARTED for phone number : "+phoneno+" ***");

				if("6508621000".equals(phoneno)) {
				
					String sqlstr = "update account_closed Set phoneunlocked=true where phoneno = "+phoneno;
					template.requestBody("direct:callJDBC", sqlstr);
					System.out.println("\n\n*** Device unlock DONE for phone number : "+phoneno+" ***");
				}else if("6508621001".equals(phoneno)) {
					System.out.println("\n\nXXXXXX Issue while unlocking the phoneno : "+phoneno + " XXXXXX");
					throw new Exception();
				} 

			}
		});

	}

}
