package jp.ksgwr.pipeline;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Input Pipe
 * @author ksgwr
 *
 */
public class InTap extends Pipe {

	/**
	 * constructor
	 * @param in input stream
	 */
	public InTap(InputStream in) {
		super.in = in;
	}

	/**
	 * constructor
	 * @param file file
	 * @throws FileNotFoundException exception
	 */
	public InTap(File file) throws FileNotFoundException {
		InputStream in = new FileInputStream(file);
		super.in = in;
	}

	/**
	 * constructor
	 * @param file file
	 * @param charset charset
	 * @throws FileNotFoundException exception
	 */
	public InTap(File file, String charset) throws FileNotFoundException {
		InputStream in = new FileInputStream(file);
		super.in = in;
		super.inputCharset = charset;
	}
}
