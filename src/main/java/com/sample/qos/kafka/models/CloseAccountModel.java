package com.sample.qos.kafka.models;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"phoneno",
	"unlock",
	"eDelivery"
})
@Generated("jsonschema2pojo")
public class CloseAccountModel {

	@JsonProperty("phoneno")
	private String phoneno;
	@JsonProperty("unlock")
	private String unlock;
	@JsonProperty("eDelivery")
	private String eDelivery;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	@JsonProperty("finalamount")
	private String finalamount;

	@JsonProperty("finalamount")
	public String getFinalamount() {
		return finalamount;
	}

	@JsonProperty("finalamount")
	public void setFinalamount(String finalamount) {
		this.finalamount = finalamount;
	}

	@JsonProperty("phoneno")
	public String getPhoneno() {
		return phoneno;
	}

	@JsonProperty("phoneno")
	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}

	@JsonProperty("unlock")
	public String getUnlock() {
		return unlock;
	}

	@JsonProperty("unlock")
	public void setUnlock(String unlock) {
		this.unlock = unlock;
	}

	@JsonProperty("eDelivery")
	public String geteDelivery() {
		return eDelivery;
	}

	@JsonProperty("eDelivery")
	public void seteDelivery(String eDelivery) {
		this.eDelivery = eDelivery;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}