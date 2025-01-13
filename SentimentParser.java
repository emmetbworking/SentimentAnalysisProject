
package ie.atu.sw;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The SentimentParser class is responsible for processing .txt files into the
 * application and calculating sentiment values. It implements the FileProcessor
 * interface and utilises a lexicon to perform sentiment analysis. This class
 * employs a thread pool for parallel processing of file content to enhance
 * performance during sentiment analysis. 
 * The lexicon, a ConcurrentSkipListMap, is used to associate sentiment values with words.
 *
 * References: Thread pool example from video 12.3
 *             Processes a line of text and calculates the sentiment value via project workshop video.
 *             Lexicon for sentiment analysis from project workshop video.
 */
public class SentimentParser implements FileProcessor {

	private double sentimentValue;

	// Lexicon for sentiment analysis.
	private final ConcurrentSkipListMap<String, Double> lexicon;

	/**
	 * Constructor.
	 * 
	 * @param lexicon A ConcurrentSkipListMap containing words and their associated
	 *                sentiment values.
	 */
	public SentimentParser(ConcurrentSkipListMap<String, Double> lexicon) {
		this.lexicon = lexicon;
	}

	/**
	 * Parses a file and calculate the total sentiment value.
	 * 
	 * @param file The path to the text file to be processed.
	 * @return The total sentiment value calculated from the file.
	 * @throws Exception If an error occurs during file processing.
	 */
	@Override
	public double parseFile(String file) throws Exception {
		// Initialise.
		sentimentValue = 0;

		// Creates an ExecutorService to manage threads.
		// Stripped from example in video 12.3
		try (ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor()) {
			sentimentValue = Files.lines(Paths.get(file)).map(text -> pool.submit(() -> process(text)))
					.mapToDouble(future -> {
						try {
							return future.get();
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}).sum();
		}
		return sentimentValue;
	}

	/**
	 * Processes a line of text and calculates the sentiment value.
	 * 
	 * @param text The input text to be processed.
	 * @return The sentiment value calculated for the input text. 
	 *         Acid stripped reference from workshop video for project.
	 */
	@Override
	public double process(String text) {
		double lineSentimentValue = 0;
		String[] parts = text.split("\\s+");
		for (String string : parts) {
			string = string.replaceAll("[^a-zA-Z]", "").toLowerCase();
			if (lexicon.containsKey(string)) {
				lineSentimentValue += lexicon.get(string);
				System.out.println("Word: " + string + " Sentiment Value: " + lexicon.get(string));
			}
		}
		return lineSentimentValue;
	}
}
