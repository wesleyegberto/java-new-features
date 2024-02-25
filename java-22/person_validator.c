/**
 * Compile: `gcc -shared -o libpersonvalidator.so -I $(pwd) person_validator.c`
 */
#include <string.h>

struct Person {
	char* name;
	short age;
};

char* validate_person(struct Person* person) {
	if (person == NULL) {
		return "Invalid person (null)";
	}
	if (person->name == NULL) {
		return "Invalid name (null)";
	}
	if (strlen(person->name) < 1) {
		return "Invalid name (empty)";
	}
	if (person->age < 1 || person->age > 150) {
		return "Invalid age";
	}
	if (person->age < 18) {
		return "Person is too young";
	}
	return "Person is valid";
}
