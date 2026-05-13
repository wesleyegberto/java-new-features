import java.security.*;

String PEM = """
		-----BEGIN PUBLIC KEY-----
		MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEi/kRGOL7wCPTN4KJ2ppeSt5UYB6u
		cPjjuKDtFTXbguOIFDdZ65O/8HTUqS/sVzRF+dg7H3/tkQ/36KdtuADbwQ==
		-----END PUBLIC KEY-----
		""";

void main() throws Exception {
	decodeExamples();

	var keyPair = createKeyPair();
	var encodedKeys = encodeKeyPair(keyPair);
	decode(encodedKeys);
}

void decodeExamples() {
	IO.println("=== Decoding Examples ===");
	PEMDecoder pemDec = PEMDecoder.of();

	var message = switch (pemDec.decode(PEM)) {
		case PublicKey publicKey -> "PublicKey created:\n" + publicKey;
		case PrivateKey privateKey -> "PrivateKey created:\n" + privateKey;
		default -> "Invalid PEM";
	};
	IO.println(message);

	// we also can direct decode the type
	PublicKey publicKey = pemDec.decode(PEM, ECPublicKey.class);
	IO.println("\nECPublicKey created:");
	IO.println(publicKey);

	// using password
	PublicKey eckey = pemDec.withDecryption("password".toCharArray())
			.decode(PEM, ECPublicKey.class);

}

KeyPair createKeyPair() throws NoSuchAlgorithmException {
	IO.println("\n=== Generating keys ===");
	KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
	generator.initialize(256);
	var keyPair = generator.generateKeyPair();

	IO.println("PublicKey generated:");
	IO.println(keyPair.getPublic());

	IO.println("\nPrivateKey generated:");
	IO.println(keyPair.getPrivate());

	return keyPair;
}

PEMKeys encodeKeyPair(KeyPair pair) {
	IO.println("\n=== Encoding keys ===");
	PrivateKey privateKey = pair.getPrivate();
	PublicKey publicKey = pair.getPublic();

	// Old way
	/*
	byte[] privBytes = privateKey.getEncoded(); // X.509
	byte[] pubBytes = publicKey.getEncoded(); // PKCS#8
	String privPem = "-----BEGIN PRIVATE KEY-----\n" +
		Base64.getMimeEncoder().encodeToString(privBytes) +
		"\n-----END PRIVATE KEY-----";
	String pubPem = "-----BEGIN PUBLIC KEY-----\n" +
		Base64.getMimeEncoder().encodeToString(pubBytes) +
		pemEnc.encodeToString(publicKey) +
	*/

	PEMEncoder pemEnc = PEMEncoder.of();
	String privPem = pemEnc.encodeToString(privateKey);
	String pubPem = pemEnc.encodeToString(publicKey);

	IO.println("Public Key encoded:");
	IO.println(pubPem);

	IO.println("Private Key encoded:");
	IO.println(privPem);

	return new PEMKeys(pubPem, privPem);
}

void decode(PEMKeys keys) {
	IO.println("\n=== Decoding keys ===");
	PEMDecoder pemDec = PEMDecoder.of();

	PublicKey publicKey = pemDec.decode(keys.publicKey(), PublicKey.class);
	IO.println("PublicKey decoded:");
	IO.println(publicKey);

	PrivateKey privateKey = pemDec.decode(keys.privateKey(), PrivateKey.class);
	IO.println("\nPrivateKey decoded:");
	IO.println(privateKey);
}

record PEMKeys(String publicKey, String privateKey) {
}
