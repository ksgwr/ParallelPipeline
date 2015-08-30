package jp.ksgwr.parallelstream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Parallel Stream Factory, Create LineProducer, LineConsumer, LinePrinter
 * @author ksgwr
 *
 */
public class ParallelStreamFactory {

	/**
	 * createInstance
	 * @param f process function
	 * @param in input file readers
	 * @param out output stream
	 * @param order if true, ordering output
	 * @param n consumer threads number (n+2:threds. because producer, printer have each 1 threads)
	 * @return parallel stream instance
	 */
	public static ParallelStream createInstance(Function f, Reader[] in, PrintStream out, boolean order, int n) {
		BlockingQueue<Line> inputQueue = new ArrayBlockingQueue<Line>(n);
		BlockingQueue<Line> outputQueue;
		if (order) {
			outputQueue =  new PriorityBlockingQueue<Line>(n+n);
		} else {
			outputQueue = new ArrayBlockingQueue<Line>(n);
		}

		LineProducer producer = new LineProducer(inputQueue, in, n);
		LineConsumer[] consumers = new LineConsumer[n];
		for(int i=0;i<n;i++) {
			consumers[i] = new LineConsumer(f, inputQueue, outputQueue);
		}
		LinePrinter printer;
		if (order) {
			printer = new OrderedLinePrinter(out, consumers, (PriorityBlockingQueue<Line>)outputQueue);
		} else {
			printer = new LinePrinter(out, outputQueue);
		}

		return new ParallelStream(producer, consumers, printer);
	}

	public static ParallelStream createInstance(Function f, Reader[] in, PrintStream out, boolean order) {
		return createInstance(f, in, out, order, Runtime.getRuntime().availableProcessors());
	}

	public static ParallelStream createInstance(Function f, Reader[] in, PrintStream out) {
		return createInstance(f, in, out, true);
	}

	public static ParallelStream createInstance(Function f, Reader in, PrintStream out, boolean order) {
		return createInstance(f, new Reader[]{in}, out, order);
	}

	public static ParallelStream createInstance(Function f, Reader in, PrintStream out) {
		return createInstance(f, new Reader[]{in}, out);
	}

	public static ParallelStream createInstance(Function f, InputStream in, PrintStream out, boolean order) {
		return createInstance(f, new InputStreamReader(in), out, order);
	}

	public static ParallelStream createInstance(Function f, InputStream in, PrintStream out) {
		return createInstance(f, new InputStreamReader(in), out);
	}

	public static ParallelStream createInstance(Function f, File in, PrintStream out, boolean order) throws FileNotFoundException {
		return createInstance(f, new InputStreamReader(new FileInputStream(in)), out, order);
	}

	public static ParallelStream createInstance(Function f, File in, PrintStream out) throws FileNotFoundException {
		return createInstance(f, new InputStreamReader(new FileInputStream(in)), out);
	}

	public static ParallelStream createInstance(Function f, File in, String charset, PrintStream out, boolean order) throws FileNotFoundException, UnsupportedEncodingException {
		return createInstance(f, new InputStreamReader(new FileInputStream(in), charset), out, order);
	}

	public static ParallelStream createInstance(Function f, File in, String charset, PrintStream out) throws FileNotFoundException, UnsupportedEncodingException {
		return createInstance(f, new InputStreamReader(new FileInputStream(in), charset), out);
	}
}
