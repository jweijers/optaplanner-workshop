package org.optaplanner.training.election.comparator;

import org.optaplanner.training.election.domain.FederalState;

import java.util.Comparator;

public class FederalStateComparator implements Comparator<FederalState> {

	@Override
	public int compare(final FederalState s1, final FederalState s2) {
		return Integer.compare(s1.getMinimumMajorityPopulation() / s1.getElectoralVotes(), s2.getMinimumMajorityPopulation() / s2.getElectoralVotes());
	}
}
