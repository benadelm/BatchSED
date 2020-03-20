/* This Source Code Form is subject to the terms of the hermA Licence.
 * If a copy of the licence was not distributed with this file, You have
 * received this Source Code Form in a manner that does not comply with
 * the terms of the licence.
 */
package batchsed;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * {@link ComparisonOutput} implementation that outputs
 * {@link FullTextComparison} objects by appending their
 * data to an {@link Appendable} as one line (terminated
 * by {@code "\n"}) with the following fields (separated
 * by {@code "\t"}):
 * <ol>
 * <li>ID of the first full text</li>
 * <li>ID of the second full text</li>
 * <li>length of the first full text
 *     (number of items in the sequence)</li>
 * <li>length of the second full text
 *     (number of items in the sequence)</li>
 * <li>substring edit distance
 *     for turning the first full text
 *     into a sub-sequence of the second full text</li>
 * <li>substring edit distance
 *     for turning the second full text
 *     into a sub-sequence of the first full text</li>
 * </ol>
 */
public class ToAppendableComparisonOutput implements ComparisonOutput {
	
	private final Appendable pAppendable;
	
	/**
	 * Initializes a new instance of this class.
	 * 
	 * @param appendable
	 * the {@link Appendable} to append data to; not {@code null}
	 */
	public ToAppendableComparisonOutput(final Appendable appendable) {
		pAppendable = appendable;
	}
	
	@Override
	public void output(final String id1, final String id2, final FullTextComparison comparisonResult) {
		final String length1str = Long.toString(comparisonResult.getSize1());
		final String length2str = Long.toString(comparisonResult.getSize2());
		final String sed1in2str = Long.toString(comparisonResult.getEditDistance1in2());
		final String sed2in1str = Long.toString(comparisonResult.getEditDistance2in1());
		try {
			synchronized (pAppendable) {
				pAppendable.append(id1);
				pAppendable.append('\t');
				pAppendable.append(id2);
				pAppendable.append('\t');
				pAppendable.append(length1str);
				pAppendable.append('\t');
				pAppendable.append(length2str);
				pAppendable.append('\t');
				pAppendable.append(sed1in2str);
				pAppendable.append('\t');
				pAppendable.append(sed2in1str);
				pAppendable.append('\n');
			}
		} catch (final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
}
