package jp.ksgwr.pipeline;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Pipeline Main Class
 * @author ksgwr
 *
 */
public class Pipeline {

	/** pipes */
	Pipe[] pipes;

	/** executor */
	ExecutorService exec;

	/**
	 * constructor
	 * @param pipes pipes
	 */
	public Pipeline(Pipe[] pipes) {
		this.exec = Executors.newCachedThreadPool();
		this.pipes = pipes;
	}

	/**
	 * synchronized execution
	 * @throws IOException exception
	 * @throws InterruptedException exception
	 */
	public void exec() throws IOException, InterruptedException {
		run();
		join();
	}

	/**
	 * kick execution
	 * @return execution service
	 * @throws IOException exception
	 */
	public ExecutorService run() throws IOException {
		PipedOutputStream out = null;
		for (int i = 0; i < pipes.length; i++) {
			// first pipe, not use output stream
			if (i != 0) {
				pipes[i].in = new PipedInputStream(out);
			}
			// last pipe, not use input stream
			if (i != pipes.length - 1) {
				out = new PipedOutputStream();
				pipes[i].out = out;
			}
			pipes[i].init();
		}
		for(Pipe pipe:pipes) {
			exec.submit(pipe);
		}
		return exec;
	}

	/**
	 * wait task exit
	 * @throws InterruptedException exception
	 */
	public void join() throws InterruptedException {
		exec.shutdown();
		exec.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
	}

}
