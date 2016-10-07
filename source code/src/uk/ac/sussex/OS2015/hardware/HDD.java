/**
 * Provided for reference, the HDD class mimics operations available from a
 * basic disk controller.
 * @author t.harvey@sussex.ac.uk
 */

package uk.ac.sussex.OS2015.hardware;

import java.io.File;

public class HDD {

	public class HDDException extends Exception {

		public HDDException(String str) {
			super(str);
		}
	}

	private static final File DISK_IMAGE = new File("default.bin");
	private byte[] data;
	private int size;
	private int index;

	/**
	 * Create a HDD image from byte array
	 * 
	 * @param data
	 */
	public HDD(byte[] data) {
		this.data = data.clone();
		this.size = data.length;
		this.index = 0;
	}

	/**
	 * Create a blank HDD image at given size
	 * 
	 * @param size
	 *            in bytes
	 */
	public HDD(int size) {
		this.data = new byte[size];
		for(int i=0; i < size; i++) data[i] = 0;
		this.size = size;
		this.index = 0;
	}

	/**
	 * Write byte b to current position of read head, increment read head
	 * afterward.
	 * 
	 * @param b
	 * @throws HDDException
	 */
	public void write(byte b) throws HDDException {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (index < this.size) {
			data[index++] = b;
		} else {
			throw new HDDException("Out of bounds");
		}
	}

	/**
	 * Read value at current read head position, increment afterward.
	 * 
	 * @return
	 * @throws HDDException
	 */
	public byte read() throws HDDException {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (index < this.size) {
			return data[index++];
		} else {
			throw new HDDException("Out of bounds");
		}
	}

	/**
	 * Distance between read head and end of disk - does NOT count unallocated
	 * space!
	 * 
	 * @return
	 */
	public int remaining() {
		return size - index;
	}

	/**
	 * Reset read head to beginning of disk.
	 */
	public void reset() {
		index = 0;
	}

	/**
	 * Move read head to specified position
	 * 
	 * @param index
	 */
	public void seek(int index) {
		try {
			Thread.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.index = index;
	}

	/**
	 * Total size of disk
	 * 
	 * @return size in bytes
	 */
	public int capacity() {
		return size;
	}

	/*
	 * The following requires Java 1.7 or above, and is not necessary, only
	 * convinient!
	 */

	// /**
	// * Exports disk image to file - use to test your filesystem stores
	// correctly
	// * @throws IOException
	// */
	// public void exprt() throws IOException {
	// Files.write(DISK_IMAGE.toPath(), data, StandardOpenOption.CREATE_NEW,
	// StandardOpenOption.TRUNCATE_EXISTING);
	// }
	//
	// /**
	// * REPLACE current image with one loaded from file - use to test your
	// * filesystem works correctly!
	// * @throws IOException
	// */
	// public void imprt() throws IOException {
	// data = Files.readAllBytes(DISK_IMAGE.toPath());
	// }

}
