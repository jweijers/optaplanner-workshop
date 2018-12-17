package org.optaplanner.training.election.comparator;

import org.optaplanner.training.election.domain.Election;

import java.util.Comparator;
import java.util.Objects;

public class CandidateComparator implements Comparator<String> {
	@Override
	public int compare(final String c1, final String c2) {
		if (Objects.equals(c1, c2)){
			return 0;
		} else if (c1.equals(Election.NORMAL_CANDIDATE)){
			return -1;
		} else {
			return 1;
		}
	}
}
