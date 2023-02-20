import jdk.incubator.concurrent.*;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Run: `java --source 20 --enable-preview --add-modules jdk.incubator.concurrent ScopedValueExampleVsThreadLocal.java`
 */
public class ScopedValueExampleVsThreadLocal {
	// pretty much the same
	final static ThreadLocal<String> USER_LOCAL = new ThreadLocal<>();
	final static ScopedValue<String> USER_SCOPE = ScopedValue.newInstance();


	public static void main(String[] args) {
		var users = List.of(
			"Neo",
			"Trinity",
			"Morpheus",
			"Switch",
			"Dozer"
		);

		var calculator = new Calculator();
		var worker = new UserWorker(calculator);

		// now ExecutorService extends AutoCloseable
		try (var executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory())) {
			System.out.println("Starting processing");
			for (var i = 0; i < 5; i++) {
				var user = users.get(i);

				System.out.printf("Starting worker user %s%n", user);
				executor.submit(() -> {
					// ThreadLocal just set
					USER_LOCAL.set(user);

					// with ScopedValue, we set the value and define the scope in which it will be available
					ScopedValue.where(USER_SCOPE, user)
						.run(worker::run);

					var userLocal = USER_LOCAL.get();
					System.out.printf("User is still available in ThreadLocal: %s%n", userLocal);
				});
			}
		}

		System.out.println("Processing ended");
	}
}

class UserWorker implements Runnable {
	private Calculator calculator;

	public UserWorker(Calculator calculator) {
		this.calculator = calculator;
	}

	public void run() {
		System.out.printf("Getting user for calculation...%n");
		// both we retrieve the value in the same way
		var user = ScopedValueExampleVsThreadLocal.USER_SCOPE.get();
		var userLocal = ScopedValueExampleVsThreadLocal.USER_LOCAL.get();

		System.out.printf("Users - ScopedValue: %s - ThreadLocal: %s%n", user, userLocal);
		if (!user.equals(userLocal)) {
			System.out.printf("Users from ScopedValue and ThreadLocal aren't the same%n", user);
			return;
		}
		// the first difference, in ThreadLocal we can change the value at any time
		ScopedValueExampleVsThreadLocal.USER_LOCAL.set(userLocal + " -- was changed");

		System.out.printf("User %s - calculating...%n", user);
		var answer = this.calculator.calculate();
		System.out.printf("User %s - answer: %d%n", user, answer);
	}
}

class Calculator {
	public int calculate() {
		// now the ThreadLocal will see a different value
		// (could be change anywhere, it require us to look for the points that is changing it)
		var user = ScopedValueExampleVsThreadLocal.USER_SCOPE.get();
		var userLocal = ScopedValueExampleVsThreadLocal.USER_LOCAL.get();

		System.out.printf("Users - ScopedValue: %s - ThreadLocal: %s%n", user, userLocal);
		// pretend to go to DB to sum the rows
		try {
			Thread.sleep(500);
		} catch (InterruptedException ex) {}
		return user.length();
	}
}
