/**
 * To run: `java --enable-preview --source 15 SealedTypesExample.java`
 */
public class SealedTypesExample {
	public static void main(String[] args) {
		// get something from somewhere
		var player = new Player();
		var entity = player.getCollidingEntity();

		switch (entity) {
			case Player:
				player.move();
				entity.move();
				break;
			case Enemy:
				player.die();
				break;
			case Object:
				object.move();
				break;
			// we dont need to do this (nor ifs) - because we restricted the implementations types (we know them)
			// default:
			// 	throw IllegalStateException("Unexpected entity");
		}
	}
}

/**
 * Sealed types will help us to restrict which types can extend/implement a type.
 * Will allow us to write less verbose polymorphic code.
 */
sealed abstract class GameEntity
	permits Player, Enemy, Object {}

class Player extends GameEntity {
	/**
	 * Returns the entity which is colling with the player.
	 */
	public GameEntity getCollidingEntity() {
		// obscure logic
		return new Enemy();
	}
}

class Enemy extends GameEntity {}

class Object extends GameEntity {}
