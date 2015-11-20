package com.ml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class NaiveBayesTrain {

	int TOPIC_COUNT = -1;
	int TOTAL_TOPIC = -1;
	// private ArrayList<HashMap<String, Integer>> hashDocList = new
	// ArrayList<HashMap<String, Integer>>();
	private HashMap<String, HashMap<String, Integer>> hashMapTopic = new HashMap<String, HashMap<String, Integer>>();
	private HashMap<String, Integer> hashTopicCount = new HashMap<String, Integer>();
	private HashMap<String, Integer> hashTopicWordCount = new HashMap<String, Integer>();

	private HashMap<String, Double> priorHashMap = new HashMap<String, Double>();

	private HashMap<String, Integer> wordMap = new HashMap<String, Integer>();

	private HashMap<String, HashMap<String, Double>> hashCondProb = new HashMap<String, HashMap<String, Double>>();
	
	public static double SMOOTTHING_FACTOR=.2;

	

	public HashMap<String, Double> getPriorHashMap() {
		return priorHashMap;
	}

	public void setPriorHashMap(HashMap<String, Double> priorHashMap) {
		this.priorHashMap = priorHashMap;
	}

	public HashMap<String, HashMap<String, Double>> getHashCondProb() {
		return hashCondProb;
	}

	public void setHashCondProb(
			HashMap<String, HashMap<String, Double>> hashCondProb) {
		this.hashCondProb = hashCondProb;
	}

	public HashMap<String, Integer> getHashTopicCount() {
		return hashTopicCount;
	}

	public void setHashTopicCount(HashMap<String, Integer> hashTopicCount) {
		this.hashTopicCount = hashTopicCount;
	}

	public NaiveBayesTrain() {
		readFileForParsing();
		// printTopicWordCount();
		trainNaiveBayes();
	}

	private void readFileForParsing() {
		int prevNewLine = 0;
		int count = 0;
		// boolean isNewDoc=false;
		int docIndex = -1;

		String current_topic = "";
		BufferedReader br = null;
		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader("training.data"));

			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.length() == 0) {
					prevNewLine++;
				} else {
					if (prevNewLine >= 2) {
						docIndex++;
						// HashMap<String, Integer> docMap = new HashMap<String,
						// Integer>();
						// hashDocList.add(docMap);
						if (hashMapTopic.containsKey(sCurrentLine)) {
							int x = hashTopicCount.get(sCurrentLine);
							hashTopicCount.put(sCurrentLine, x + 1);
						} else {
							TOPIC_COUNT++;
							HashMap<String, Integer> topicMap = new HashMap<String, Integer>();
							hashMapTopic.put(sCurrentLine, topicMap);

							HashMap<String, Double> condProbMap = new HashMap<String, Double>();
							hashCondProb.put(sCurrentLine, condProbMap);

							hashTopicCount.put(sCurrentLine, 1);
							hashTopicWordCount.put(sCurrentLine, 0);

						}
						current_topic = sCurrentLine;
					}
					prevNewLine = 0;
				}
				if (count == 0) {
					docIndex = 0;
					// HashMap<String, Integer> docMap = new HashMap<String,
					// Integer>();
					// hashDocList.add(docMap);
					if (hashMapTopic.containsKey(sCurrentLine)) {
						int x = hashTopicCount.get(sCurrentLine);
						hashTopicCount.put(sCurrentLine, x + 1);

					} else {

						TOPIC_COUNT++;
						HashMap<String, Integer> topicMap = new HashMap<String, Integer>();
						hashMapTopic.put(sCurrentLine, topicMap);

						HashMap<String, Double> condProbMap = new HashMap<String, Double>();
						hashCondProb.put(sCurrentLine, condProbMap);

						hashTopicCount.put(sCurrentLine, 1);
						hashTopicWordCount.put(sCurrentLine, 0);

					}
					current_topic = sCurrentLine;

				}
				// System.out.println(sCurrentLine);
				count++;

				// if (count > 200)
				// break;

				if (sCurrentLine.length() != 0) {
					sCurrentLine = sCurrentLine.replaceAll("[^a-zA-Z ]", "")
							.toLowerCase();
					// System.out.println(sCurrentLine);
					String[] values = sCurrentLine.split("\\s+");

					// HashMap<String, Integer> hashMap = hashDocList
					// .get(docIndex);
					HashMap<String, Integer> topicMap = hashMapTopic
							.get(current_topic);
					// System.out.println("current topic: "+current_topic);
					for (int i = 0; i < values.length; i++) {

						/*
						 * if (hashMap.containsKey(values[i])) { int x =
						 * hashMap.get(values[i]); hashMap.put(values[i], x +
						 * 1);
						 * 
						 * } else { hashMap.put(values[i], 1); }
						 */

						int wordcount = hashTopicWordCount.get(current_topic);
						hashTopicWordCount.put(current_topic, wordcount + 1);

						if (topicMap.containsKey(values[i])) {
							int x = topicMap.get(values[i]);
							topicMap.put(values[i], x + 1);

						} else {
							topicMap.put(values[i], 1);
						}
						if (!wordMap.containsKey(values[i]))
							wordMap.put(values[i], 1);

					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		TOTAL_TOPIC = docIndex + 1;
		System.out.println("Doc Index:" + docIndex + " TOPIC_COUNT: "
				+ TOPIC_COUNT + " word size: " + wordMap.size());

		/*
		 * int i = 0; for (String key : hashTopicCount.keySet()) { i++;
		 * System.out.println("index: " + i + " topic: " + key + "   count " +
		 * hashTopicCount.get(key)); }
		 */

	}

	private void printTopicWordCount() {
		System.out.println("wordMap size: " + wordMap.size());
		int i = 0;
		for (String key : hashTopicCount.keySet()) {
			i++;
			System.out.println("index: " + i + " topic: " + key + "   count "
					+ hashTopicWordCount.get(key) + " topic count:"
					+ hashTopicCount.get(key));
		}
	}

	

	private void trainNaiveBayes() {
		for (String key : hashTopicCount.keySet()) {
			int docInTopic = hashTopicCount.get(key);
			int count = wordMap.size();
			priorHashMap.put(key,
					(double) ((double) docInTopic / (double) TOTAL_TOPIC));
			// System.out.println("putting key: "+ key);

			int total_word_count = hashTopicWordCount.get(key);
			for (String e : wordMap.keySet()) {
				int e_count = 0;
				HashMap<String, Integer> hash = hashMapTopic.get(key);
				if (hash.containsKey(e)) {
					e_count = hash.get(e);

				}

				double cond_prob = (double) (e_count + SMOOTTHING_FACTOR)
						/ (double) (total_word_count + count * SMOOTTHING_FACTOR);
				hashCondProb.get(key).put(e, cond_prob);

				/*
				 * if(e_count>0) System.out.println("key: "+e+" COUNT: "+
				 * e_count+" cond prob "+ cond_prob);
				 */

			}

		}
	}

}
