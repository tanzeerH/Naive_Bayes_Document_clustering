package com.ml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class NaiveBayes {

	private HashMap<String, HashMap<String, Double>> hashCondProb ;
	private HashMap<String, Double> priorHashMap ;
	private ArrayList<HashMap<String,Integer>> testList;
	private HashMap<String, Integer> hashTopicCount;
	
	ArrayList<String> classList;

	public NaiveBayes() {
		NaiveBayesTrain naiveBayesTrain=new NaiveBayesTrain();
		NaiveBayesTest naiveBayesTest=new NaiveBayesTest();
		
		hashCondProb=naiveBayesTrain.getHashCondProb();
		priorHashMap=naiveBayesTrain.getPriorHashMap();
		hashTopicCount=naiveBayesTrain.getHashTopicCount();
		testList=naiveBayesTest.getTestList();
		classList=naiveBayesTest.getClassList();
		
		int size=testList.size();
		int correct=0;
		for(int i=0;i<size;i++)
		{
			ArrayList<Topic> list=new ArrayList<Topic>();
			for(String key: hashTopicCount.keySet())
			{
				Topic topic=new Topic();
				topic.setTopic_name(key);
				//System.out.println("key "+ key+"  "+priorHashMap.get(key));
				double total_prob=Math.log(priorHashMap.get(key));
				//System.out.println(""+ total_prob);
				HashMap<String,Integer> hash=testList.get(i);
				//System.out.println("size: "+ hash.size());
				for(String word: hash.keySet())
				{
					double prob=0;
					if(hashCondProb.get(key).containsKey(word))
						prob=hashCondProb.get(key).get(word);
					
					if(prob!=0)
						total_prob+=Math.log(prob);
					
					
					
					
				}
				topic.setProbability((total_prob*-1));
				list.add(topic);
			}
			
			Collections.sort(list,new ProbabilityComparator());
			
			//for(int x=0;x<list.size();x++)
			if(list.get(0).getTopic_name().equals(classList.get(i)))
					correct++;
				//System.out.println(" topic: "+list.get(0).getTopic_name() + "  "+list.get(0).getProbability()+ "class: "+classList.get(i));
			//System.out.println();
			
		}
		System.out.println("correct: "+ correct+"  total: "+testList.size()+" precentage : "+((double)correct/(double)testList.size()));
		
		
	}
}
