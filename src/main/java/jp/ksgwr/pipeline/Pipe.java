package jp.ksgwr.pipeline;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.Callable;

/**
 * Pipe base class
 * @author ksgwr
 *
 */
public class Pipe implements Callable<Void> {

	/** input stream */
	protected InputStream in;

	/** input stream charset */
	protected String inputCharset;

	/** output stream */
	protected OutputStream out;

	/** output stream charset */
	protected String outputCharset;

	/**
	 * default constructor
	 */
	public Pipe() {

	}

	/**
	 * default initialize
	 */
	public void init() {

	}

	@Override
	public Void call() throws Exception {
		BufferedReader br = null;
		PrintStream writer = null;
		try {
			if (inputCharset == null) {
				br = new BufferedReader(new InputStreamReader(in));
			} else {
				br = new BufferedReader(new InputStreamReader(in, inputCharset));
			}
			if (outputCharset == null) {
				writer = new PrintStream(out);
			} else {
				writer = new PrintStream(out, true, outputCharset);
			}
			String line;
			while ((line = br.readLine()) != null) {
				writer.println(line);
			}
		} finally {
			if (br != null) {
				br.close();
			}
			if (writer != null) {
				writer.close();
			}
		}
		return null;
	}

}
