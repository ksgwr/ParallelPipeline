package jp.ksgwr.pipeline;

import java.io.PrintStream;

import jp.ksgwr.parallelstream.Function;
import jp.ksgwr.parallelstream.ParallelStream;
import jp.ksgwr.parallelstream.ParallelStreamFactory;

/**
 * ParallelStream Function Pipe
 * @author ksgwr
 *
 */
public class ParallelStreamPipe extends Pipe {

	/** process function */
	protected Function f;

	/** parallel stream executor */
	protected ParallelStream ps;

	/** parallel ordering */
	protected boolean order;

	/**
	 * constructor
	 * @param f process function
	 */
	public ParallelStreamPipe(Function f) {
		this(f, true);
	}

	/**
	 * constructor
	 * @param f process function
	 * @param order ordering
	 */
	public ParallelStreamPipe(Function f, boolean order) {
		this.f = f;
		this.order = order;
	}

	@Override
	public void init() {
		ps = ParallelStreamFactory.createInstance(f, in, new PrintStream(out), order);
		ps.setAutoClose(true);
	}

	@Override
	public Void call() throws Exception {
		ps.exec();
		return null;
	}
}
