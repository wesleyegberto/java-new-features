import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class ProcessTest {
	public static void main(String[] args) {
		final RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
		final long pid = runtime.getPid();
		System.out.println("Process ID is: " + pid);
	}
}
