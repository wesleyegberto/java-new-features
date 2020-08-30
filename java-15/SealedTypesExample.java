/**
 * To run: `java --enable-preview --source 15 SealedTypesExample.java`
 */
public class SealedTypesExample {
	public static void main(String[] args) {
		// get something from somewhere
		var player = new Player();
		var entity = player.getCollidingEntity();

		if (entity instanceof Player p) {
			player.move();
			p.move();
		} else if (entity instanceof Enemy e) {
			player.kill(e);
		} else if (entity instanceof SceneryObject o) {
			o.move();
		}
		// we dont need to do this (nor ifs) - because we restricted the implementations types (we know them)
		// else {
		// 	throw IllegalStateException("Unexpected entity");
		// }
	}
}

/**
 * Sealed types will help us to restrict which types can extend/implement a type.
 * Will allow us to write less verbose polymorphic code.
 *
 * Constraints:
 * - The sealed class and its permitted subclasses must belong to the same module, and, if declared in an unnamed module, the same package.
 * - Every permitted subclass must directly extend the sealed class.
 * - Every permitted subclass must choose a modifier to describe how it continues the sealing initiated by its superclass:
 *     - `final`
 *     - `sealed`
 *     - `non-sealed` (back to a normal class open to extensibility)
 */
sealed abstract class GameEntity permits Player, Enemy, SceneryObject {
	public void move() {
		System.out.println("Moving");
	}

	public void destroy() {
		System.out.println("Destroying");
	}
}

final class Player extends GameEntity {
	/**
	 * Returns the entity which is colling with the player.
	 */
	public GameEntity getCollidingEntity() {
		// obscure logic
		return new Enemy();
	}

	public void kill(GameEntity killedBy) {
		// obscure death
		System.out.println("Killing");
	}
}

final class Enemy extends GameEntity {}

non-sealed class SceneryObject extends GameEntity {}
