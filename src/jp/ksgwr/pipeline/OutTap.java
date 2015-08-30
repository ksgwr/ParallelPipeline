package jp.ksgwr.pipeline;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Output Pipe
 * @author ksgwr
 *
 */
public class OutTap extends Pipe {

	/**
	 * constructor output System.out
	 */
	public OutTap() {
		super.out = System.out;
	}

	/**
	 * constructor
	 * @param out output stream
	 */
	public OutTap(OutputStream out) {
		super.out = out;
	}

	/**
	 * constructor
	 * @param file output file
	 * @throws FileNotFoundException exception
	 */
	public OutTap(File file) throws FileNotFoundException {
		super.out = new FileOutputStream(file);
	}

	/**
	 * constructor
	 * @param file output file
	 * @param charset charset
	 * @throws FileNotFoundException exception
	 */
	public OutTap(File file, String charset) throws FileNotFoundException {
		super.out = new FileOutputStream(file);
		super.outputCharset = charset;
	}

}
