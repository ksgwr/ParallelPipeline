package jp.ksgwr.parallelstream;

import java.io.PrintStream;
import java.util.PriorityQueue;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Ordered Line Printer (manage consumer) extends LinePrinter
 * @author ksgwr
 *
 */
public class OrderedLinePrinter extends LinePrinter {

	/** ordering queue pool */
	protected PriorityQueue<Line> pool;

	/**
	 * constructor
	 * @param out output stream
	 * @param consumers consumer tasks
	 * @param outputQueue output queue
	 */
	public OrderedLinePrinter(PrintStream out, LineConsumer[] consumers, PriorityBlockingQueue<Line> outputQueue) {
		super(out, outputQueue);
		this.pool = new PriorityQueue<Line>(consumers.length);
	}

	@Override
	public Void call() throws Exception {
		long cnt = 1;
		Line line;
		boolean complete = false;
		try {
			while (!complete) {
				// wait output
				line = outputQueue.take();

				if (line.num == cnt) {
					cnt++;
					if (line.str != null) {
						out.println(line.str);
					}
				} else if (line.num != Long.MAX_VALUE){
					// not ordering, add pool
					pool.add(line);
				} else {
					// a consumer exit
					complete = true;
					for (Future<?> consumerTask : consumerTasks) {
						if (!consumerTask.isDone()) {
							complete = false;
						}
					}
				}

				//check pool
				line = pool.peek();
				while (line != null && line.num == cnt) {
					pool.poll();
					cnt++;
					if (line.str != null) {
						out.println(line.str);
					}
					line = pool.peek();
				}
			}
			if(autoClose) {
				out.close();
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return null;
	}
}
