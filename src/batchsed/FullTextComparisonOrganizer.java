/* This Source Code Form is subject to the terms of the hermA Licence.
 * If a copy of the licence was not distributed with this file, You have
 * received this Source Code Form in a manner that does not comply with
 * the terms of the licence.
 */
package batchsed;

import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Class to hide the details of the full text comparison
 * organization from calling code.
 * This class is thread safe in the sense that it does
 * not alter the state of any of the objects it references.
 * However, it does not synchronize accesses to those objects.
 */
public class FullTextComparisonOrganizer {
	
	private final CombinationTable pCombinationTable;
	private final ArrayList<Path> pPathTable;
	private final FullTextComparer pComparer;
	private final ComparisonOutput pComparisonOutput;
	
	/**
	 * Initializes a new instance of this class.
	 * 
	 * @param combinationTable
	 * the combination table to use for determining
	 * whether a combination has already been tested;
	 * not {@code null}
	 * 
	 * @param pathTable
	 * the lists of paths to which the indices in the second
	 * section of the comparison plan input file correspond;
	 * not {@code null}
	 * 
	 * @param comparer
	 * a {@link FullTextComparer} that will perform the
	 * comparisons; not {@code null}
	 * 
	 * @param comparisonOutput
	 * a {@link ComparisonOutput} to output the comparison
	 * results; not {@code null}
	 */
	public FullTextComparisonOrganizer(final CombinationTable combinationTable, final ArrayList<Path> pathTable, final FullTextComparer comparer, final ComparisonOutput comparisonOutput) {
		pCombinationTable = combinationTable;
		pPathTable = pathTable;
		pComparer = comparer;
		pComparisonOutput = comparisonOutput;
	}
	
	/**
	 * Takes a line from the second section of the comparison
	 * plan input file and performs the corresponding comparison.
	 * 
	 * @param line
	 * the line from the second section of the comparison
	 * plan input file; not {@code null}
	 */
	public void compare(final String line) {
		final int tabIndex = line.indexOf('\t');
		final String id1 = line.substring(0, tabIndex);
		final String id2 = line.substring(tabIndex + 1);
		if (pCombinationTable.containsCombination(id1, id2))
			return;
		pComparisonOutput.output(id1, id2, pComparer.compare(pPathTable.get(Integer.parseInt(id1)), pPathTable.get(Integer.parseInt(id2))));
	}
	
}
