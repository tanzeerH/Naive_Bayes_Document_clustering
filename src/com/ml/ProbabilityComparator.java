package com.ml;

import java.util.Comparator;

public class ProbabilityComparator implements Comparator<Topic> {

	@Override
	public int compare(Topic o1, Topic o2) {
		if (o1.getProbability() > o2.getProbability())
			return 1;
		else if (o1.getProbability() < o2.getProbability())
			return -1;

		return 0;
	}

}
