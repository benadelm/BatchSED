/* This Source Code Form is subject to the terms of the hermA Licence.
 * If a copy of the licence was not distributed with this file, You have
 * received this Source Code Form in a manner that does not comply with
 * the terms of the licence.
 */
package batchsed;

/**
 * Can output {@link FullTextComparison} objects.
 */
public interface ComparisonOutput {
	
	/**
	 * Outputs (the data in) a {@link FullTextComparison} object
	 * corresponding to the comparison between two full texts
	 * with given IDs.
	 * 
	 * @param id1
	 * the ID of the first full text; not {@code null}
	 * 
	 * @param id2
	 * the ID of the second full text; not {@code null}
	 * 
	 * @param comparisonResult
	 * the result of the comparison between the two full texts;
	 * not {@code null}
	 */
	void output(String id1, String id2, FullTextComparison comparisonResult);
	
}
