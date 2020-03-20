/* This Source Code Form is subject to the terms of the hermA Licence.
 * If a copy of the licence was not distributed with this file, You have
 * received this Source Code Form in a manner that does not comply with
 * the terms of the licence.
 */
package batchsed;

import java.nio.file.Path;

/**
 * Can compare full text files.
 */
public interface FullTextComparer {
	
	/**
	 * Compares two full text files.
	 * 
	 * @param file1
	 * (a {@link Path} locating) the first file; not {@code null}
	 * 
	 * @param file2
	 * (a {@link Path} locating) the second file; not {@code null}
	 * 
	 * @return
	 * a {@link FullTextComparison} with the comparison result;
	 * not {@code null}
	 */
	FullTextComparison compare(Path file1, Path file2);
	
}
