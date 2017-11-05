package jp.ksgwr.pipeline.sample;

import java.io.File;

import jp.ksgwr.parallelstream.Function;
import jp.ksgwr.pipeline.FunctionPipe;
import jp.ksgwr.pipeline.InTap;
import jp.ksgwr.pipeline.OutTap;
import jp.ksgwr.pipeline.ParallelStreamPipe;
import jp.ksgwr.pipeline.Pipe;
import jp.ksgwr.pipeline.Pipeline;

public class PipelineSample {


	/**
	 * 複数のパイプをつなげる例、並列度が上がりすぎるのが問題
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Pipeline pipeline = new Pipeline(new Pipe[]{
				new InTap(new File("src/test/resources/data/test"), "UTF-8"),
				new FunctionPipe(new Function(){
					@Override
					public String printOutput(String line) {
						return "hoge"+line;
					}

				}),
				new ParallelStreamPipe(new Function(){ //ここだけ並列処理
					@Override
					public String printOutput(String line) {
						return "fuga"+line;
					}

				}),
				new FunctionPipe(new Function(){
					@Override
					public String printOutput(String line) {
						return "test"+line;
					}

				}),
				new OutTap()});
		pipeline.exec();
	}
}
