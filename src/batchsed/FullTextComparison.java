/* This Source Code Form is subject to the terms of the hermA Licence.
 * If a copy of the licence was not distributed with this file, You have
 * received this Source Code Form in a manner that does not comply with
 * the terms of the licence.
 */
package batchsed;

/**
 * The result of a comparison between two full texts.
 */
public class FullTextComparison {
	
	private final long pEditDistance1in2;
	private final long pEditDistance2in1;
	
	private final long pSize1;
	private final long pSize2;
	
	/**
	 * Initializes a new instance of this class.
	 * 
	 * @param editDistance1in2
	 * the substring edit distance for turning the first full text
	 * into a sub-sequence of the second full text
	 * 
	 * @param editDistance2in1
	 * the substring edit distance for turning the second full text
	 * into a sub-sequence of the first full text
	 * 
	 * @param size1
	 * the size of the first full text
	 * (number of items in the sequence)
	 * 
	 * @param size2
	 * the size of the second full text
	 * (number of items in the sequence)
	 */
	public FullTextComparison(final long editDistance1in2, final long editDistance2in1, final long size1, final long size2) {
		pEditDistance1in2 = editDistance1in2;
		pEditDistance2in1 = editDistance2in1;
		
		pSize1 = size1;
		pSize2 = size2;
	}
	
	/**
	 * Returns the substring edit distance
	 * for turning the first full text
	 * into a sub-sequence of the second full text.
	 * 
	 * @return
	 * the substring edit distance for turning the first full text
	 * into a sub-sequence of the second full text
	 */
	public long getEditDistance1in2() {
		return pEditDistance1in2;
	}
	
	/**
	 * Returns the substring edit distance
	 * for turning the second full text
	 * into a sub-sequence of the first full text.
	 * 
	 * @return
	 * the substring edit distance for turning the second full text
	 * into a sub-sequence of the first full text
	 */
	public long getEditDistance2in1() {
		return pEditDistance2in1;
	}
	
	/**
	 * Returns the size of the first full text
	 * (number of items in the sequence).
	 * 
	 * @return
	 * the size of the first full text
	 * (number of items in the sequence)
	 */
	public long getSize1() {
		return pSize1;
	}
	
	/**
	 * Returns the size of the second full text
	 * (number of items in the sequence).
	 * 
	 * @return
	 * the size of the first second text
	 * (number of items in the sequence)
	 */
	public long getSize2() {
		return pSize2;
	}
	
}
