package com.github.wesleyegberto.collections.processapi;

import java.io.IOException;

public class ProcessTest {
	public static void main(String[] args) throws IOException, InterruptedException {
		ProcessHandle process = ProcessHandle.current();
		System.out.println("Main pid: " + process.pid());
		System.out.println("Main user: " + process.info().user().orElse("Unknown"));
		
		Process sleepy = Runtime.getRuntime().exec("sleep 60s");
		System.out.println("Sleepy pid: " + sleepy.pid());
		
		System.out.println("Destroying sleepy");
		ProcessHandle handle = ProcessHandle.of(sleepy.pid()).get();
		handle.onExit()
		// sleepy.onExit()
			.thenRun(() -> {
				System.out.println("Alive: " + sleepy.isAlive());
				System.out.println("Exit code: " + sleepy.exitValue());
			});
		sleepy.destroy();
		
		Thread.sleep(5000);
	}
}
