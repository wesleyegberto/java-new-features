import static java.lang.IO.println;

import java.io.IOException;
import java.net.*;
import java.net.http.*;
import java.util.function.Consumer;
import jdk.incubator.json.Json;
import jdk.incubator.json.JsonValue;
import jdk.incubator.json.JsonNull;
import jdk.incubator.json.JsonParseException;
import jdk.incubator.json.JsonValueException;

/**
 * To run: `java --source 28 --enable-preview --add-modules jdk.incubator.json SimpleJsonExample.java`
 */
void main() {
	parsing();
	printing();
	navigating();
	converting();
	readJsonFileWithComments();
	completeExample();
}

void printJson(JsonValue value) {
	println(value.getClass().getName() + " => " + value);
}

void parsing() {
	println("\n=== Parsing values ===");
	printJson(Json.parse("\"JSON with simple text\""));
	printJson(Json.parse("42"));
	printJson(Json.parse("42.42"));
	printJson(Json.parse("true"));
	printJson(Json.parse("{\"attr1\":\"value_1\",\"attr2\":42}"));
	printJson(Json.parse("[1,1,2,3,5]"));
	printJson(Json.parse("null"));
}

void printing() {
	println("\n=== Printing values ===");
	var object = Json.parse("{\"number\":42,\"object\":{\"attr1\":\"V\"},\"array\":[1,42,67],\"null\":null}");

	println("Compact JSON:");
	println(object);

	println("\nReadable JSON with spaces:");
	println(Json.toDisplayString(object, "  "));

	println("\nReadable JSON with tab:");
	println(Json.toDisplayString(object, "\t"));
}

void navigating() {
	println("\n=== Navigating the tree ===");
	var json = Json.parse("{\"number\":42,\"object\":{\"attr1\":\"V\"},\"array\":[1,42,67],\"null\":null}");

	printJson(json.get("number"));
	printJson(json.get("object"));
	printJson(json.get("object").get("attr1"));
	printJson(json.get("array"));
	printJson(json.get("array").get(0));
	printJson(json.get("null"));

	// handling null values
	json.get("null").tryValue().ifPresentOrElse(this::printJson, () -> println("Attribute has null value"));

	// handling optional values
	json.tryGet("invalid_attr").ifPresentOrElse(this::printJson, () -> println("Attribute does not exists"));
}

void converting() {
	println("\n=== Converting values to Java types ===");
	println(Json.parse("\"JSON with simple text\"").asString());
	println(Json.parse("42").asInt());
	println(Json.parse("true").asBoolean());
	println(Json.parse("{\"attr1\":\"value_1\",\"attr2\":42}").asMap());
	println(Json.parse("[1,1,2,3,5]").asList());
	println("Is null: " + (Json.parse("null") instanceof JsonNull));
}

void readJsonFileWithComments() {
	println("\n=== Converting JSON file with comments ===");
	try {
		String jsonc = Files.readString(Path.of("file-with-comments.json"));
		String json = jsonc.replaceAll("(?m)^\\s*[#(//)].*$", "");
		JsonValue jv = Json.parse(json);
		println(Json.toDisplayString(jv, "  "));
	} catch (IOException ex) {}
}

void completeExample() {
	println("\n=== Weather JSON example ===");
	try (var client = HttpClient.newHttpClient()) {
		var request = HttpRequest
				.newBuilder(URI.create("https://api.weather.gov/gridpoints/MTR/97,83/forecast"))
				.build();
		var response = client.send(request, HttpResponse.BodyHandlers.ofString());

		JsonValue json = Json.parse(response.body());

		json.get("properties").get("periods").asList().stream()
				.mapToInt(j -> j.get("temperature").asInt())
				.average()
				.ifPresent(IO::println);
	} catch (JsonParseException ex) {
		println("Invalid JSON: " + ex.getMessage());
	} catch (JsonValueException ex) {
		println("Invalid value: " + ex.getMessage());
	} catch (IOException ex) {
		println("IO error: " + ex.getMessage());
	} catch (InterruptedException ex) {
		println("Request error: " + ex.getMessage());
	}
}
