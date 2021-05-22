public class SealedTypesExample {
	public static void main(String[] args) {
		var gowButton = new Circle();
		gowButton.press();

		// sealed, permits and non-sealed is contextual keywords (we can use as var name)
		var sealed = gowButton != null;
		SealedTypesExample.sealed();
	}

	public static void sealed() {
		// local class cannot extend/implement a sealed class/interface
		// class NewCross extends Cross {}

		System.out.println("My name is Neo");
	}
}

sealed interface PlayStationButton permits Circle, Square, Triangle, Cross {
	void press();
}

final class Circle implements PlayStationButton {
	public void press() {
		System.out.println("Button was pressed");
	}
}

final class Square implements PlayStationButton {
	public void press() {
		System.out.println("Button was pressed");
	}
}

final class Triangle implements PlayStationButton {
	public void press() {
		System.out.println("Button was pressed");
	}
}

sealed class Cross implements PlayStationButton {
	public void press() {
		System.out.println("Button was pressed");
	}
}

final class DoubleCross extends Cross { }
