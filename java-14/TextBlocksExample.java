public class TextBlocksExample {
	public static void main(String[] args) {
		// Java 13

		// Error (same line)
		// String name = """Pat Q. Smith""";

		// 3 double-quote must be followed by line terminator (new line)
		String niceJson = """
		{
			"name": "Odair Jose",
			"language": "Java"
		}"""; // same line to not append \n at the end
		System.out.println(niceJson);

		// the identation is kept based on the most left char of block (incidental white spaces)
		String html = """
		<html>
		    <body>
		        <p>Hello World.</p>
		    </body>
		</html>
		"""; // will append a \n at the end
		System.out.println(html);

		String badJson = """
				{
			"name": \""Odair Jose",
			"language": "Java"
				}""";
		System.out.println(badJson);


		// Java 14 - New escape sequences

		String sameLine = """
						  Line 1 \
						  Still line 1 \
						  Still... \
						  """;
		System.out.println(sameLine);

		String helpingKeepingTrailing = """
				A lot of space here               \s
				Here not much                     \s
				Here none but one will be inserted\s
				Ends                              \s
				""";
		System.out.println(helpingKeepingTrailing);
	}
}