package com.sample.qos;

import java.sql.Timestamp;

import javax.sql.DataSource;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.qos.kafka.models.CloseAccountModel;


/**
 * Sample JSON input
 * {
	"phoneno" : "6508621000",
	"unlock" : "true",
	"eDelivery" : "true"
}
 * @author svalluru
 *
 */
@Component
public class CloseAccountProcessor extends RouteBuilder {

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


		from("kafka:close-account?brokers=localhost:9092" +
				"&seekTo=beginning") 
		.to("direct:generateFinalBill")
		.to("direct:storeFinalBillDetails");


		from("direct:generateFinalBill")
		.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				String jsonBody = (String) exchange.getIn().getBody();
				ObjectMapper om = new ObjectMapper();
				om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				CloseAccountModel readValue = om.readValue(jsonBody, CloseAccountModel.class);

				readValue.setFinalamount("76.00");

				String emailContent = "From : noreply@telecom.com\n"
						+ "To : customer@yahoo.com\n"
						+ "\n"
						+ "Subject : Final Bill for account "+ readValue.getPhoneno()
						+ "\n"
						+ "Total Due Amount : $76\n"
						+ "";
				System.out.println("\n\nSent email with Final Bill Details \n\n************ \n" + emailContent +"\n************ \n\n\n");


				exchange.getIn().setBody(readValue);
			}
		})
		;




		from("direct:storeFinalBillDetails")
		.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				CloseAccountModel readValue = exchange.getIn().getBody(CloseAccountModel.class);
				ProducerTemplate template = exchange.getContext().createProducerTemplate();

				String sqlstr = "insert into account_closed values('"+readValue.getPhoneno()+"',"+readValue.getUnlock()
				+",false,"+readValue.geteDelivery()+",'"+readValue.getFinalamount()+"',false,false,false,'"+new Timestamp(System.currentTimeMillis()).toString()+"','NA');";
				template.requestBody("direct:callJDBC", sqlstr);
				//System.out.println(sqlstr);
				System.out.println("\n\n*** Stored Final bill details in Database ***");
			}
		});


		from("direct:callJDBC")
		.to("jdbc:dataSource");


	}

}
