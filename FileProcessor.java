package ie.atu.sw;

public interface FileProcessor {

	/**
	 * This interface defines methods a file processor implements. The file
	 * processor is responsible for parsing a file and processing a line of text.
	 * 
	 * Reference: Interface of shopping trolley example.
	 */

	/**
	 * @param file
	 * @return double value
	 * @throws Exception
	 */

	double parseFile(String file) throws Exception;

	/**
	 * @param text
	 * @return double
	 */
	double process(String text);
}
