package jp.ksgwr.pipeline;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

import jp.ksgwr.parallelstream.Function;

/**
 * Function implements Pipe
 * @author ksgwr
 *
 */
public class FunctionPipe extends Pipe {

	/** function processor */
	Function f;

	/**
	 * constructor
	 * @param f function processor
	 */
	public FunctionPipe(Function f) {
		this.f = f;
	}

	@Override
	public Void call() throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		PrintStream writer = new PrintStream(out);
		String line;
		while ((line = br.readLine()) != null) {
			writer.println(f.printOutput(line));
		}
		br.close();
		writer.close();
		return null;
	}
}
