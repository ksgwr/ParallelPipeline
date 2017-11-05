package jp.ksgwr.parallelstream.sample;

import java.util.Random;

import jp.ksgwr.parallelstream.Function;
import jp.ksgwr.parallelstream.ParallelStream;
import jp.ksgwr.parallelstream.ParallelStreamFactory;

public class ParallelStreamSample {

	/**
	 * 並列処理でファイルを処理する例
	 * @param args 必要なし
	 * @throws Exception 例外は発生しない
	 */
	public static void main(String[] args) throws Exception {
		long start,end;

		ParallelStream ps = ParallelStreamFactory.createInstance(new Function() {

			@Override
			public String printOutput(String line) {
				try {
					Thread.sleep(new Random().nextInt(1000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return line;
			}
		}, ParallelStream.class.getClassLoader().getResourceAsStream("data/test"), System.out, true);

		start = System.currentTimeMillis();
		ps.exec();
		end = System.currentTimeMillis() - start;
		System.out.println(end);
	}
}
