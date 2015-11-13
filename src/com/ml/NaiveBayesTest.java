package com.ml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class NaiveBayesTest {

	private ArrayList<HashMap<String,Integer>> testList=new ArrayList<HashMap<String,Integer>>();
	private ArrayList<String> classList=new ArrayList<String>();
	public NaiveBayesTest() {
		parseTestFile();
	}
	private void parseTestFile()
	{
		int prevNewLine = 0;
		int count = 0;
		// boolean isNewDoc=false;
		int docIndex = -1;

		BufferedReader br = null;
		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader("test.data"));

			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.length() == 0) {
					prevNewLine++;
				} else {
					if (prevNewLine >= 2) {
						docIndex++;
						HashMap<String,Integer> hashMap=new HashMap<String, Integer>();
						testList.add(hashMap);
						classList.add(sCurrentLine);
							
					}
					prevNewLine = 0;
				}
				if (count == 0) {
					docIndex = 0;
					HashMap<String,Integer> hashMap=new HashMap<String, Integer>();
					testList.add(hashMap);
					classList.add(sCurrentLine);
				}
				count++;

				// if (count > 200)
				// break;

				if (sCurrentLine.length() != 0) {
					sCurrentLine = sCurrentLine.replaceAll("[^a-zA-Z ]", "")
							.toLowerCase();
					// System.out.println(sCurrentLine);
					String[] values = sCurrentLine.split("\\s+");

					for (int i = 0; i < values.length; i++) {
						HashMap<String, Integer> hash = testList.get(docIndex);
						if(!hash.containsKey(values[i]))
							hash.put(values[i],1);
						

						
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
		System.out.println("Doc Index:" + docIndex);
		

	}
	public ArrayList<String> getClassList() {
		return classList;
	}
	public void setClassList(ArrayList<String> classList) {
		this.classList = classList;
	}
	public ArrayList<HashMap<String, Integer>> getTestList() {
		return testList;
	}
	public void setTestList(ArrayList<HashMap<String, Integer>> testList) {
		this.testList = testList;
	}
	
}


