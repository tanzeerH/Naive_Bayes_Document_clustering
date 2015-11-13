package com.ml;

public class Topic {

	public Topic(String topic_name, double probability) {
		super();
		this.topic_name = topic_name;
		this.probability = probability;
	}
	public Topic()
	{
		
	}
	private String topic_name;
	private double probability;
	public String getTopic_name() {
		return topic_name;
	}
	public void setTopic_name(String topic_name) {
		this.topic_name = topic_name;
	}
	public double getProbability() {
		return probability;
	}
	public void setProbability(double probability) {
		this.probability = probability;
	}
}
