/**
 *
 * @author t.harvey@sussex.ac.uk, c.kiefer@sussex.ac.uk
 */

package uk.ac.sussex.OS2015;

import uk.ac.sussex.OS2015.hardware.HDD;

public class FileSystem {

	protected HDD disk;
	
	/**
	 * Throw this if the disk is out of free space
	 *
	 */
	@SuppressWarnings("serial")
	public class OutOfSpaceException extends Exception {

		public OutOfSpaceException(String str) {
			super(str);
		}
	}
	
	
	/**
	 * Throw this if a file cannot be found
	 *
	 */
	@SuppressWarnings("serial")
	public class FileNotFoundException extends Exception {

		public FileNotFoundException(String str) {
			super(str);
		}
	}
	
	/**
	 * Throw this if your filesystem is not able to write to a file
	 *
	 */
	@SuppressWarnings("serial")
	public class CantWriteFileException extends Exception {

		public CantWriteFileException(String str) {
			super(str);
		}
	}

	
	/**
	 * Constructor
	 * @param storage
	 */
	public FileSystem(HDD storage) {
		this.disk = storage;
	}
	
	/**
	 * @return True if the FS has been formatted
	 */
	public boolean isFormatted() {
    	throw new UnsupportedOperationException("Not supported yet.");    			
	}
	
	
	/**
	 * Format the disk
	 */
	public void format() {
    	throw new UnsupportedOperationException("Not supported yet.");    			
	}
	
	/**
	 * @return the amount of free space
	 */
	public int freeSpace() {
    	throw new UnsupportedOperationException("Not supported yet.");    			
	}
	
	/**
	 * Respond to a shutdown of the machine. This is a very good time to write any cached or in-memory data to the disk 
	 */
	public void signalShutdown() {
    	throw new UnsupportedOperationException("Not supported yet.");    			
	}
	
    /**
     * Create file with contents from array
     * @param contents
     * @return Index of file created
     */
    public int newFile(byte[] contents) throws OutOfSpaceException {
    	throw new UnsupportedOperationException("Not supported yet.");    	
    }
    
    /**
     * Create file with name and contents from array
     * @param filename
     * @param contents
     * @return Index of file created
     */
    public int newFile(String filename, byte[] contents) throws OutOfSpaceException {
    	throw new UnsupportedOperationException("Not supported yet.");    	
    }

    
	/**
	 * @param handle
	 * @return An array of bytes with the data from the file
	 * @throws FileNotFoundException
	 */
	public byte[] readFile(int handle) throws FileNotFoundException {
    	throw new UnsupportedOperationException("Not supported yet.");    					
	}
	
	/**
	 * @param filename
	 * @return An array of bytes with the data from the file
	 * @throws FileNotFoundException
	 */
	public byte[] readFile(String filename) throws FileNotFoundException {
    	throw new UnsupportedOperationException("Not supported yet.");    					
	}
	
    
    /**
     * Delete file at index
     * @param index
     */
    public void deleteFile(int index) throws FileNotFoundException {
    	throw new UnsupportedOperationException("Not supported yet.");    	
    }
    
    /**
     * Delete file specified by filename
     * @param filename
     */
    public void deleteFile(String filename) throws FileNotFoundException {
    	throw new UnsupportedOperationException("Not supported yet.");    	
    }
    
    /**
     * Print all data stored on the filesystem in readable format
     * for testing
     */
    public void dumpContents() {
    	throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * @return The maximum size in characters of a filename (or -1 if the size is unlimited)
     */
    public int maxFilenameSize() {
    	throw new UnsupportedOperationException("Not supported yet.");
    	
    }
}
