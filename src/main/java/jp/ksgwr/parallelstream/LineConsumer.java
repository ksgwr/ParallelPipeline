package jp.ksgwr.parallelstream;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

/**
 * Line Consumer
 * @author ksgwr
 *
 */
public class LineConsumer implements Callable<Void> {

	/** line process function */
	protected Function f;

	/** line input queue */
	protected BlockingQueue<Line> inputQueue;

	/** line output queue */
	protected BlockingQueue<Line> outputQueue;

	/**
	 * constructor
	 * @param f line process function
	 * @param inputQueue line input queue
	 * @param outputQueue line output queue
	 */
	public LineConsumer(Function f, BlockingQueue<Line> inputQueue, BlockingQueue<Line> outputQueue) {
		this.f = f;
		this.inputQueue = inputQueue;
		this.outputQueue = outputQueue;
	}

	@Override
	public Void call() throws Exception {
		Line line;
		while ((line = inputQueue.take()).str != null) {
			outputQueue.put(new Line(f.printOutput(line.str), line.num));
		}
		outputQueue.put(new Line(null, Long.MAX_VALUE));
		return null;
	}
}
