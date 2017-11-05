package jp.ksgwr.parallelstream;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

public class ParallelStreamTest {

	/**
	 * 入力が順序通りになって加工されることを確認する
	 * @throws FileNotFoundException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Test
	public void simpleTest() throws FileNotFoundException, InterruptedException, ExecutionException {

		final String BR = System.lineSeparator();

		StringBuilder input = new StringBuilder()
			.append("aaa"+BR)
			.append("bbb"+BR)
			.append("ccc"+BR)
			.append("ddd"+BR)
			.append("eee"+BR);
		InputStream in = new ByteArrayInputStream(input.toString().getBytes());
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(output);

		ParallelStream ps = ParallelStreamFactory.createInstance(new Function() {
			@Override
			public String printOutput(String line) {
				try {
					Thread.sleep(new Random().nextInt(500));
				} catch (InterruptedException e) {}
				return line+line;
			}
		}, in, out, true);

		ps.exec();

		String actual = new String(output.toByteArray());
		String expected = new StringBuilder()
			.append("aaaaaa"+BR)
			.append("bbbbbb"+BR)
			.append("cccccc"+BR)
			.append("dddddd"+BR)
			.append("eeeeee"+BR).toString();

		assertEquals(expected, actual);
	}

}
