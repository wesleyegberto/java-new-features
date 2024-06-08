/*
 * To run: `java --enable-preview --source 23 FlexibleConstructorBodiesExample.java`
 */
public class FlexibleConstructorBodiesExample {

	public static void main(String[] args) {
		System.out.println("Pet:\n" + new Dog("Shih Tzu"));
	}
}

abstract class Animal {
	protected final String domain;
	protected final String kingdom;
	protected final String phylum;
	protected final String subphylum;
	protected final String phylumClass;
	protected final String family;

	private final String representation;

	protected Animal(String phylum, String subphylum, String phylumClass, String family) {
		this.domain = "Eukaryota";
		this.kingdom = "Animalia";
		this.phylum = phylum;
		this.subphylum = subphylum;
		this.phylumClass = phylumClass;
		this.family = family;

		this.representation = getClassification();
	}

	public abstract String getClassification();

	public String toString() {
		return this.representation;
	}
}

class Dog extends Animal {
	private final String order;
	private final String species;
	private final String breed;

	Dog(String breed) {
		this.order = "Carnivora";
		this.species = "Dog";
		this.breed = breed;
		super("Chordata", "Vertebrata", "Mammals", "Canidae");
	}

	public String getClassification() {
		return "Domain: %s / Kingdom: %s / Phylum: %s / Subphylum: %s / Class: %s / Order: %s/ Species: %s / Breed: %s".formatted(
			domain, kingdom, phylum, subphylum, phylumClass, order, species, breed
		);
	}
}
