Parallel Pipeline Library

概要

パイプライン型の処理を実装するのを手助けするライブラリ
特徴として、並列処理パイプを実装しており、出力は入力ファイルの順序通りに出力する機能を実装しています。
Java8のStreamでは並列処理時は出力の順序を規定していませんが、このライブラリでは出力の順序を保つことを重視しています。
通信を含むバッチ処理を効率的に処理することができることが本ライブラリの特徴です。


インストール

parallelpipeline-1.0.0.jarを依存に含めてください


サンプル

```
Pipeline pipeline = new Pipeline(new Pipe[]{
				new InTap(new File("data/test"), "UTF-8"),
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
```

さらに詳しくは以下を参照してください。

jp.ksgwr.parallelstream.sample.ParallelStreamSample
jp.ksgwr.pipeline.sample.PipelineSample


使い方

Function#printOutputを実装します。出力がないときはnullを返します。

ライセンス

Apache License, Version 2.0