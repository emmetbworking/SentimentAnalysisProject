
package ie.atu.sw;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executors;

/**
 * LexiconParser class is responsible for loading and parsing a lexicon file. A
 * map will store the words of the lexicon and it's sentiment values. The
 * lexicon file is expected to be a .txt file where each line contains a word
 * and its sentiment value. References: VirtualThreadFileParser example from
 * video 12.4
 */

public class LexiconParser {

	ConcurrentSkipListMap<String, Double> lexicon = new ConcurrentSkipListMap<>();

	/**
	 * Parses a lexicon file and returns a map of words and their sentiment values.
	 * 
	 * @return A map of words with sentiment values.
	 * @throws Exception if error occurs while parsing lexicon file.
	 */

	public ConcurrentSkipListMap<String, Double> parseLexicon(String file) throws Exception {
		try (var pool = Executors.newVirtualThreadPerTaskExecutor()) {
			Files.lines(Paths.get(file)).forEach(text -> pool.execute(() -> process(text)));
		}
		return lexicon;
	}

	/**
	 * Processes a line of text from the lexicon file. The line is expected to
	 * contain a word and its sentiment value, separated by a comma.
	 */

	public void process(String text) {
		String[] parts = text.split(",");
		lexicon.put(parts[0], Double.parseDouble(parts[1]));
	}
}
