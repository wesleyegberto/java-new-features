package com.github.wesleyegberto.api.io;

import java.io.*;

public class PipeStreamTest {
	public static void main(String[] args) throws Exception {
		var writer = new StringWriter();
		var inputPipe = new PipedInputStream();
		var inputBuffed = new BufferedInputStream(inputPipe);

		var outputPipe = new PipedOutputStream(inputPipe);
		var outputBuffered = new BufferedOutputStream(outputPipe);

		var w = new PrintWriter(outputBuffered);
		w.write("Hello Bug");
		w.flush();
		w.close();

		System.out.println(new String(inputBuffed.readAllBytes()));
	}
}
