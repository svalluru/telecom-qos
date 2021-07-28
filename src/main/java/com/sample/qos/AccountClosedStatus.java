package com.sample.qos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestPropertyDefinition;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.qos.kafka.models.CloseAccountModel;

@Component
public class AccountClosedStatus extends RouteBuilder {

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
		.get("/accountclosestatus")
		.produces("application/json")
		.consumes("application/json")
		.outTypeList(CloseAccountModel.class)
		.to("direct:getstatus")
		;

		from("direct:getstatus")
		.process(new Processor() {
			ProducerTemplate template = getContext().createProducerTemplate();
			@Override
			public void process(Exchange exchange) throws Exception {
				String selectStr = "select * from account_closed";
				List<HashMap<String, Object>> dbResult = (List<HashMap<String, Object>>) template.requestBody("direct:callJDBC", selectStr);

				List<CloseAccountModel> respList = new ArrayList<CloseAccountModel>();
				for (HashMap<String,Object> hashMap : dbResult) {
					String phoneno = (String) hashMap.get("phoneno");
					String status = (String) hashMap.get("status");
					boolean unlockreq = (boolean) hashMap.get("unlockphone");
					CloseAccountModel model = new CloseAccountModel();
					model.setPhoneno(phoneno);
					model.setStatus(status);
					model.setUnlock(String.valueOf(unlockreq));

					ObjectMapper om = new ObjectMapper();
					om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					String jsonValue = om.writeValueAsString(model);
					respList.add(model);
				}
				exchange.getIn().setBody(respList);	
			}
		})
		//.marshal().json(JsonLibrary.Jackson)
		;


	}


}
