/* This Source Code Form is subject to the terms of the hermA Licence.
 * If a copy of the licence was not distributed with this file, You have
 * received this Source Code Form in a manner that does not comply with
 * the terms of the licence.
 */
package batchsed;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A table with the ID combinations already compared.
 * Effectively, this class stores unordered pairs of IDs.
 */
public class CombinationTable {
	
	private final Set<Combination> pCombinations;
	
	/**
	 * Initializes a new empty table.
	 */
	public CombinationTable() {
		pCombinations = Collections.synchronizedSet(new HashSet<>());
	}
	
	/**
	 * Adds an ID combination to this table.
	 * 
	 * @param id1
	 * the first ID; not {@code null}
	 * 
	 * @param id2
	 * the second ID; not {@code null}
	 */
	public void addCombination(final String id1, final String id2) {
		pCombinations.add(new Combination(id1, id2));
	}
	
	/**
	 * Checks whether this table contains a given combination
	 * of IDs.
	 * 
	 * @param id1
	 * the first ID; not {@code null}
	 * 
	 * @param id2
	 * the second ID; not {@code null}
	 * 
	 * @return
	 * {@code true} if the table contains the combination;
	 * {@code false} otherwise
	 */
	public boolean containsCombination(final String id1, final String id2) {
		return pCombinations.contains(new Combination(id1, id2));
	}
	
	private static class Combination {
		
		private final String pId1;
		private final String pId2;
		
		public Combination(final String id1, final String id2) {
			pId1 = id1;
			pId2 = id2;
		}
		
		@Override
		public int hashCode() {
			return pId1.hashCode() ^ pId2.hashCode();
		}
		
		@Override
		public boolean equals(final Object other) {
			if (other instanceof Combination)
				return equals((Combination) other);
			return super.equals(other);
		}
		
		public boolean equals(final Combination other) {
			return (pId1.equals(other.pId1) && pId2.equals(other.pId2)) || (pId1.equals(other.pId2) && pId2.equals(other.pId1));
		}
		
	}
	
}
