/* This Source Code Form is subject to the terms of the hermA Licence.
 * If a copy of the licence was not distributed with this file, You have
 * received this Source Code Form in a manner that does not comply with
 * the terms of the licence.
 */
package batchsed;

import java.util.Arrays;
import java.util.function.ToLongBiFunction;
import java.util.function.ToLongFunction;

/**
 * Contains the code for substring edit distance computations.
 */
public class SubstringEditDistance {
	
	/**
	 * Computes the substring edit distance between two sequences,
	 * with arbitrary cost functions.
	 * 
	 * @param subSequence
	 * the sequence to become a sub-sequence of the other;
	 * not {@code null}
	 * 
	 * @param superSequence
	 * the other sequence; not {@code null}
	 * 
	 * @param insertionCost
	 * the cost for adding an item to the second sequence;
	 * not {@code null}
	 * 
	 * @param deletionCost
	 * the cost for deleting an item from the first sequence;
	 * not {@code null}
	 * 
	 * @param substitutionCost
	 * the cost for replacing an item from the first sequence
	 * to obtain an item from the second sequence;
	 * not {@code null}
	 * 
	 * @return
	 * the total cost of the cheapest combination of
	 * insertions into the second sequence, deletions from
	 * the first sequence and replacements of items
	 * between the sequences to make the first sequence
	 * a sub-sequence of the second sequence
	 */
	public static <U, V> long substringEditDistance(final U[] subSequence, final V[] superSequence, final ToLongFunction<? super V> insertionCost, final ToLongFunction<? super U> deletionCost, final ToLongBiFunction<? super U, ? super V> substitutionCost) {
		final int m = subSequence.length;
		final int n = superSequence.length;
		if (m < n)
			return substringEditDistanceVariant2(subSequence, m, superSequence, n, insertionCost, deletionCost, substitutionCost);
		return substringEditDistanceVariant1(subSequence, m, superSequence, n, insertionCost, deletionCost, substitutionCost);
	}
	
	private static <U, V> long substringEditDistanceVariant1(final U[] subSequence, final int m, final V[] superSequence, final int n, final ToLongFunction<? super V> insertionCost, final ToLongFunction<? super U> deletionCost, final ToLongBiFunction<? super U, ? super V> substitutionCost) {
		final long[] table = new long[n + 1];
		Arrays.fill(table, 0L);
		
		long min = 0L;
		
		// Iteration
		for (final U u : subSequence) {
			final long delCost = deletionCost.applyAsLong(u);
			long leftAbove = table[0];
			long left = leftAbove + delCost;
			table[0] = left;
			min = left;
			int j = 0;
			for (final V v : superSequence) {
				j++;
				final long above = table[j];
				left = left + insertionCost.applyAsLong(v);
				final long abovepc = above + delCost;
				if (abovepc < left)
					left = abovepc;
				leftAbove += substitutionCost.applyAsLong(u, v);
				if (leftAbove < left)
					left = leftAbove;
				table[j] = left;
				leftAbove = above;
				if (left < min)
					min = left;
			}
		}
		
		return min;
	}
	
	private static <U, V> long substringEditDistanceVariant2(final U[] subSequence, final int m, final V[] superSequence, final int n, final ToLongFunction<? super V> insertionCost, final ToLongFunction<? super U> deletionCost, final ToLongBiFunction<? super U, ? super V> substitutionCost) {
		final long[] table = new long[m + 1];
		
		// Initialisierung
		long min = 0;
		for (int i = 0; i < m; i++) {
			table[i] = min;
			min += deletionCost.applyAsLong(subSequence[i]);
		}
		table[m] = min;
		
		// Iteration
		for (final V v : superSequence) {
			final long insCost = insertionCost.applyAsLong(v);
			long leftAbove = table[0];
			long left = 0;
			table[0] = 0;
			int i = 0;
			for (final U u : subSequence) {
				i++;
				final long above = table[i];
				left = left + deletionCost.applyAsLong(u);
				final long abovepc = above + insCost;
				if (abovepc < left)
					left = abovepc;
				leftAbove += substitutionCost.applyAsLong(u, v);
				if (leftAbove < left)
					left = leftAbove;
				table[i] = left;
				leftAbove = above;
			}
			if (left < min)
				min = left;
		}
		
		return min;
	}
	
}
