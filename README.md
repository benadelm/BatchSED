# BatchSED

A tool, developed in the research project [hermA](https://www.herma.uni-hamburg.de/en.html), for batch computing *substring edit distances* between the full texts of novels in order to detect duplicates.

The *substring edit distance* between two sequences is the smallest number of element insertions, deletions and substitutions necessary to transform the first sequence into a *subsequence* of the second sequence. For example, *text* can be turned into the substring *lex* of *lexicon* by changing one letter (*t* to *l*) and deleting one letter (*t*), so the substring edit distance is 2.

Unlike traditional edit distances, substring edit distances are asymmetric: To turn *lexicon* into a substring of *text*, *con* has to be deleted and *l* and *i* have to be changed or deleted, so the substring edit distance is 5.

This program computes substring edit distances in both directions for specified pairs of token sequences, such as the words in a novel (or other text). The substring edit distance for making a first sequence a sub-sequence of a second sequence is at most the length of the first sequence; whether the substring edit distance is low or high compared to that maximum provides information about the inclusion relation between the two sequences. When comparing texts, this turned out to also be a reliable indicator of duplication when combining both directions:

* If the substring edit distance is low in both directions, the texts are likely to be duplicates of each other (possibly with minor editorial differences, OCR errors, etc.).
* If the substring edit distance is high in both directions, the texts are unlikely to be duplicates of each other.
* If one substring edit distance is low while the other is high (or higher), one of the texts is probably contained in the other.
* Cases where both substring edit distances are neither high nor low may be philologically interesting (heavily revised editions etc.).

# Installation

The software has been tested with Windows 10 and Linux. Being written in Java, it should run on any platform Java supports; you will need a Java runtime to run the software. It has been developed and tested with Java 8, but newer versions may also work.

# Input

The program expects three positional command-line arguments:

1. path to the comparison plan input file (absolute or relative to the working directory)
2. base path for resolving filenames (absolute or relative to the working directory)
3. path to the output file (absolute or relative to the working directory)

## Comparison Plan

The comparison plan input file is a UTF-8 plain text file with two sections, separated by an empty line.

In the first section, every line contains the path (absolute or relative to the *base path* specified as the second command-line argument) of an input text file.

In the second section, every line contains two numbers separated by a tabulator character (U+0009). These numbers are 0-based indices of files (specified in the first section) to be compared.

Example:

	brave_new_world.txt
	1984.txt
	alice_in_wonderland.txt
	
	0	1
	1	2

Based on a comparison plan input file with the contents above, the program will compute substring edit distances between `brave_new_world.txt` (0) and `1984.txt` (1) and between `1984.txt` (1) and `alice_in_wonderland.txt` (2), but not between `brave_new_world.txt` (0) and `alice_in_wonderland.txt` (2).

## Text Files

Input text files are UTF-8 plain text files with one token per line. Empty lines are ignored.

# Notes on Running

If the output file already exists, the program reads from it for which pairs the substring edit distances have already been computed. Processing of the comparison plan then skips those pairs.

The running time of the program is dominated by the duration of the substring edit distance computations, which can be time-consuming. Computing the substring edit distance between a sequence of length *m* and a sequence of length *n* takes time more or less proportional to the product *m* · *n*. For example, the time needed to compute the substring edit distance between a sequence of 1000 items and a sequence of 200 items (*m* · *n* = 200000) is approximately a hundred times longer than the time needed for a sequence of 100 items and a sequence of 20 items (*m* · *n* = 2000). This allows for a relatively precise projection of the time it will take to compute substring edit distances for all the text pairs in a comparison plan. For example, on a 2.2 GHz Intel Xeon CPU E5-2630 v4 a running time estimate for for *m* · *n* = 2000 was 0.031 ms and thus 3.1 ms for *m* · *n* = 200000; comparing two novels the size of George Orwell’s *1984* (≈ 125000 words) would take four minutes on that processor.

To speed up the running time, the program processes different pairs in parallel, trying to make use of all available processors (or processor cores). That is, the substring edit distances for a single pair of texts are still computed sequentially, but two pairs of texts can be processed concurrently at the same time. If the number of text pairs to compare is much larger than the number of available processors, this can speed up the computation by a factor approximately equal to the number of processors. For example, when comparing significantly more than four text pairs on a four-core processor, the running time is reduced by approximately factor four.

As substring edit distance computations require multiple iterations over one of the two sequences to compare, the token sequences are fully read into memory before computing substring edit distances. If you are running the program on a machine with many cores or have extraordinarily long texts, increasing Java’s heap size (with the `-Xmx` option) can be advisable:

	java -Xmx10g -jar ...

# Output

The output file is a UTF-8 plain text file with every line corresponding to one pair of input files from the comparison plan. The lines contain the following fields, separated by tabulator characters (U+0009):

1. index of the first input file
2. index of the second input file
3. number of tokens in the first input file
4. number of tokens in the second input file
5. substring edit distance for making the token sequence of the first input file a sub-sequence of the token sequence of the second input file
6. substring edit distance for making the token sequence of the second input file a sub-sequence of the token sequence of the first input file

For the input example above, the output file could look like this:

	0	1	64531	125000	58077	112500
	1	2	125000	26432	118750	25110

The first line corresponds to the comparison between `brave_new_world.txt` (0) and `1984.txt` (1) and the second line corresponds to the comparison between `1984.txt` (1) and `alice_in_wonderland.txt` (2). In this example,

* `brave_new_world.txt` contains 64531 tokens,
* `1984.txt` contains 125000 tokens and
* `alice_in_wonderland.txt` contains 26432 tokens;
* the substring edit distance for making the token sequence of `brave_new_world.txt` a sub-sequence of the token sequence of `1984.txt` is 58077,
* the substring edit distance for making the token sequence of `1984.txt` a sub-sequence of the token sequence of `brave_new_world.txt` is 112500,
* the substring edit distance for making the token sequence of `1984.txt` a sub-sequence of the token sequence of `alice_in_wonderland.txt` is 118750 and
* the substring edit distance for making the token sequence of `alice_in_wonderland.txt` a sub-sequence of the token sequence of `1984.txt` is 25110.

(Numbers are invented.)