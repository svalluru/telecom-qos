package com.sample.qos;

import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FinalBillVerifier extends RouteBuilder {

	@Autowired
	DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void configure() throws Exception {
		ProducerTemplate template = getContext().createProducerTemplate();

		from("timer:CheckForPaidFinalBills?delay=30s")
		.process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				String selectStr = "select * from account_closed where finalamountpaid = true and finalbillnotificationsent = false";
				List<HashMap<String, Object>> dbResult = (List<HashMap<String, Object>>) template.requestBody("direct:callJDBC", selectStr);
				
				for (HashMap<String,Object> hashMap : dbResult) {
					String phoneno = (String) hashMap.get("phoneno");
					//send kafka message and then update DB
					System.out.println(phoneno);
					
					boolean unlockreq = (boolean) hashMap.get("unlockphone");
					if(unlockreq) {
						template.requestBody("kafka:unlock-phone?brokers=localhost:9092", phoneno);
					}
					
					template.requestBody("kafka:account-closed?brokers=localhost:9092", phoneno);
					
					String updateProc = "update account_closed Set finalbillnotificationsent=true where phoneno = '"+phoneno+"'";
					template.requestBody("direct:callJDBC", updateProc);
				}
			}
		});
		
		
		
	}

}
