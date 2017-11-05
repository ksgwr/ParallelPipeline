package jp.ksgwr.parallelstream;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.concurrent.BlockingQueue;

/**
 * Line Producer (File reader)
 * @author ksgwr
 *
 */
public class LineProducer implements Runnable {

	/** line input queue */
	protected BlockingQueue<Line> inputQueue;

	/** input file reader */
	protected Reader[] in;

	/** parallel treads number */
	protected int n;

	/**
	 * constructor
	 * @param inputQueue line input queue
	 * @param in input file reader
	 * @param n parallel treads number
	 */
	public LineProducer(BlockingQueue<Line> inputQueue, Reader[] in, int n) {
		this.inputQueue = inputQueue;
		this.in = in;
		this.n = n;
	}

	@Override
	public void run() {
		try {
			for (Reader input : in) {
				BufferedReader br = new BufferedReader(input);
				long cnt = 1;
				String line;
				while ((line = br.readLine())!=null) {
					inputQueue.put(new Line(line, cnt++));
				}
				br.close();
				// send fin signal for consumers
				for (int i=0;i<n;i++) {
					inputQueue.put(new Line(null, cnt));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
