/* This Source Code Form is subject to the terms of the hermA Licence.
 * If a copy of the licence was not distributed with this file, You have
 * received this Source Code Form in a manner that does not comply with
 * the terms of the licence.
 */
package batchsed;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.stream.Stream;

public class FullTextComparisonMain {
	
	public static void main(final String[] args) {
		if (args.length != 3) {
			System.err.println("expecting three arguments:");
			System.err.println("comparison plan input file");
			System.err.println("base path for resolving filenames");
			System.err.println("output file name");
			System.exit(1);
			return;
		}
		
		final FileSystem fs = FileSystems.getDefault();
		final Path inputPath = makePath(fs, args[0]);
		final Path baseDir = makePath(fs, args[1]);
		final Path outputPath = makePath(fs, args[2]);
		
		final CombinationTable combinationTable = new CombinationTable();
		try (final Stream<String> lines = Files.lines(outputPath, StandardCharsets.UTF_8)) {
			lines.forEach(l -> loadCombination(l, combinationTable));
		} catch (final FileNotFoundException | NoSuchFileException e) {
			// comparison file does not yet exist
		} catch (final IOException e) {
			throw new UncheckedIOException(e);
		}
		
		final ArrayList<Path> pathTable = new ArrayList<>();
		
		final int nThreads = Runtime.getRuntime().availableProcessors();
		final Thread[] threads = new Thread[nThreads];
		
		try (final BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
			try (final BufferedReader reader = Files.newBufferedReader(inputPath, StandardCharsets.UTF_8)) {
				while (true) {
					final String line = reader.readLine();
					if ((line == null) || "".equals(line))
						break;
					pathTable.add(baseDir.resolve(fs.getPath(line)));
				}
				
				final FullTextComparisonOrganizer comparisonOrganizer = new FullTextComparisonOrganizer(combinationTable, pathTable, new SimpleFullTextComparer(), new ToAppendableComparisonOutput(writer));
				
				for (int i = 0; i < nThreads; i++) {
					final Thread thread = new ParallelizerThread(reader, comparisonOrganizer);
					threads[i] = thread;
					thread.start();
				}
				
				try {
					for (final Thread thread : threads)
						thread.join();
				} catch (final InterruptedException e) {
					// should not happen
				}
			}
			writer.flush();
		} catch (final IOException e) {
			throw new UncheckedIOException(e);
		}
		
		System.out.println("Terminated successfully.");
	}
	
	private static Path makePath(final FileSystem fs, final String pathString) {
		return fs.getPath(pathString).toAbsolutePath().normalize();
	}
	
	private static void loadCombination(final String line, final CombinationTable combinationTable) {
		final int tabIndex1 = line.indexOf('\t');
		if (tabIndex1 < 0)
			throw new IllegalArgumentException("line has wrong format: " + line);
		final int afterTabIndex1 = tabIndex1 + 1;
		final int tabIndex2 = line.indexOf('\t', afterTabIndex1);
		if (tabIndex2 < 0)
			throw new IllegalArgumentException("line has wrong format: " + line);
		combinationTable.addCombination(line.substring(0, tabIndex1), line.substring(afterTabIndex1, tabIndex2));
	}
	
	private static class ParallelizerThread extends Thread {
		
		private final BufferedReader pReader;
		private final FullTextComparisonOrganizer pComparisonOrganizer;
		
		public ParallelizerThread(final BufferedReader reader, final FullTextComparisonOrganizer comparisonOrganizer) {
			pReader = reader;
			pComparisonOrganizer = comparisonOrganizer;
		}
		
		@Override
		public void run() {
			while (true) {
				final String line;
				try {
					synchronized (pReader) {
						line = pReader.readLine();
					}
				} catch (final IOException e) {
					throw new UncheckedIOException(e);
				}
				if (line == null)
					break;
				pComparisonOrganizer.compare(line);
			}
		}
		
	}
	
}
