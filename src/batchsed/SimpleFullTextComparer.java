/* This Source Code Form is subject to the terms of the hermA Licence.
 * If a copy of the licence was not distributed with this file, You have
 * received this Source Code Form in a manner that does not comply with
 * the terms of the licence.
 */
package batchsed;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * {@link FullTextComparer} implementation that loads the lines
 * of the given files as sequence items (ignoring empty lines)
 * and computes substring edit distances with unit costs
 * in both directions (subject to {@link String#equals(Object)}).
 * This class is thread safe
 * as it does not have any internal state.
 */
public class SimpleFullTextComparer implements FullTextComparer {
	
	private static final Predicate<String> NOT_EMPTY = s -> !"".equals(s);
	
	@Override
	public FullTextComparison compare(final Path file1, final Path file2) {
		final String[] tokens1, tokens2;
		try {
			tokens1 = loadTokens(file1);
			tokens2 = loadTokens(file2);
		} catch (final IOException e) {
			throw new UncheckedIOException(e);
		}
		
		final long ed12 = SubstringEditDistance.substringEditDistance(tokens1, tokens2, s -> 1, s -> 1, (s1, s2) -> s1.equals(s2) ? 0 : 1);
		final long ed21 = SubstringEditDistance.substringEditDistance(tokens2, tokens1, s -> 1, s -> 1, (s1, s2) -> s1.equals(s2) ? 0 : 1);
		
		return new FullTextComparison(ed12, ed21, tokens1.length, tokens2.length);
	}
	
	private static String[] loadTokens(final Path file) throws IOException {
		try (final Stream<String> lines = Files.lines(file, StandardCharsets.UTF_8)) {
			return lines.filter(NOT_EMPTY).toArray(String[]::new);
		}
	}
	
}
