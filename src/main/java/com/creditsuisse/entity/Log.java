package com.creditsuisse.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Log {
	@JsonProperty("id")
	private String id;

	@JsonProperty("state")
	private State state;

	@JsonProperty("timestamp")
	private long timestamp;

	@JsonProperty("type")
	private String type;

	@JsonProperty("host")
	private String host;

	public Log() {
	}

	public Log(String id, State state, long timestamp, String type, String host) {
		super();
		this.id = id;
		this.state = state;
		this.timestamp = timestamp;
		this.type = type == null ? "" : type;
		this.host = host == null ? "" : host;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		if (type == null)
			this.type = "";
		this.type = type;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		if (host == null)
			this.host = "";
		this.host = host;
	}

}
