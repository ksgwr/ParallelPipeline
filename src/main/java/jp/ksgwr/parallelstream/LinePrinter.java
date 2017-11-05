package jp.ksgwr.parallelstream;

import java.io.PrintStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Line Printer (manage consumer)
 * @author ksgwr
 *
 */
public class LinePrinter implements Callable<Void> {

	/** output stream */
	protected PrintStream out;

	/** consumer tasks */
	protected Future<?>[] consumerTasks;

	/** output queue */
	protected BlockingQueue<Line> outputQueue;

	/** auto output stream close option */
	protected boolean autoClose;

	/**
	 * constructor
	 * @param out output stream
	 * @param outputQueue output queue
	 */
	public LinePrinter(PrintStream out, BlockingQueue<Line> outputQueue) {
		this.out = out;
		this.outputQueue = outputQueue;
	}

	/**
	 * set auto output stream close option
	 * @param autoClose if true, close output stream when consumer exit
	 */
	public void setAutoClose(boolean autoClose) {
		this.autoClose = autoClose;
	}

	/**
	 * set consumer tasks
	 * @param consumerTasks consumer tasks
	 */
	void setConsumerTasks(Future<?>[] consumerTasks) {
		this.consumerTasks = consumerTasks;
	}

	@Override
	public Void call() throws Exception {
		Line line;
		while (true) {
			line = outputQueue.take();
			if (line.num == Long.MAX_VALUE) {
				for (Future<?> consumerTask : consumerTasks) {
					if (!consumerTask.isDone()) {
						continue;
					}
				}
				break;
			}
			if (line.str != null) {
				out.println(line.str);
			}
		}
		if(autoClose) {
			out.close();
		}
		return null;
	}
}
