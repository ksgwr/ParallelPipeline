package jp.ksgwr.parallelstream;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Parallel Stream Main Class
 * @author ksgwr
 *
 */
public class ParallelStream {

	/** line producer(reader) */
	private LineProducer producer;

	/** line consumer(processor) */
	private LineConsumer[] consumers;

	/** line printer(writer) */
	private LinePrinter printer;

	/** producer task */
	private Future<?> producerTask;

	/** consumer tasks */
	private Future<?>[] consumerTasks;

	/** printer task */
	private Future<?> printerTask;

	/** auto output stream close option */
	private boolean autoClose;

	/** executor service */
	private ExecutorService exec;

	/**
	 * constructor
	 * @param producer line producer
	 * @param consumers line consumer
	 * @param printer line printer
	 */
	public ParallelStream(LineProducer producer, LineConsumer[] consumers, LinePrinter printer) {
		this.producer = producer;
		this.consumers = consumers;
		this.printer = printer;
	}

	/**
	 * auto output stream close option
	 * @param autoClose if true, close output stream when consumer exit
	 */
	public void setAutoClose(boolean autoClose) {
		this.autoClose = autoClose;
	}

	/**
	 * synchronized execution
	 * @throws ExecutionException exception
	 * @throws InterruptedException exception
	 */
	public void exec() throws ExecutionException, InterruptedException {
		run();
		join();
	}

	/**
	 * kick execution
	 * @return execution service
	 * @throws ExecutionException exception
	 * @throws InterruptedException exception
	 */
	public ExecutorService run() throws ExecutionException, InterruptedException {
		exec = Executors.newFixedThreadPool(2+consumers.length);

		producerTask = exec.submit(producer);
		consumerTasks = new Future<?>[consumers.length];
		for(int i=0;i<consumers.length;i++) {
			consumerTasks[i] = exec.submit(consumers[i]);
		}
		printerTask = null;
		if (printer!=null) {
			printer.setAutoClose(autoClose);
			printer.setConsumerTasks(consumerTasks);
			printerTask = exec.submit(printer);
		}
		return exec;
	}

	/**
	 * wait task exit
	 * @throws InterruptedException exception
	 * @throws ExecutionException exception
	 */
	public void join() throws InterruptedException, ExecutionException {
		producerTask.get();
		for (Future<?> consumerTask : consumerTasks) {
			consumerTask.get();
		}
		if (printerTask != null) {
			printerTask.get();
		}
		exec.shutdown();
	}
}
