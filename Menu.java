
package ie.atu.sw;

import java.io.FileWriter;
import java.util.Scanner;
import java.util.concurrent.ConcurrentSkipListMap;
import java.io.PrintWriter;
import java.io.IOException;

public class Menu {

	/**
	 * The Menu Class for the sentiment analysis application. It allows the user to
	 * enter a .txt file to be analysed using the "bingliu" lexicon ( errors occurred using other lexicons). 
	 * 
	 *  References: Menu built on from from file encryption project.
	 *              Code Stub provided.
	 *              
	 */

	// Initialise variables.
	private Scanner scanner = new Scanner(System.in);
	private LexiconParser lexiconLoader;
	private String lexiconFile;
	private String textFile;
	private String outputFile;
	private FileProcessor fileProcessor;

	// Constructor.
	// Initialise.
	public Menu() {
		lexiconLoader = new LexiconParser();
		lexiconFile = "";
		textFile = "";
		outputFile = "";
	}

	/**
	 * Method handles the menu options.
	 * O(1), checks based on the selected option.
	 * 
	 * @param option
	 * @param scanner
	 */
	private void handleOption(int option, Scanner scanner) {
		if (option == 1) {
			specifyTextFile(scanner);
		} else if (option == 2) {
			specifyOutputFile(scanner);
		} else if (option == 3) {
			configureLexicons(scanner);
		} else if (option == 4) {
			executeAnalyseAndReport();
		} else if (option == 5) {
			quit();
		}
	}

	/** Specify the text file to be analysed.
	    O(1), as it involves reading a single user input.
	    @param scanner
    */
	private void specifyTextFile(Scanner scanner) {
		System.out.println("Specify a Text File");
		textFile = scanner.next();

	}

	/**
	 *  Specify the output file to write result.
	 *  O(1), as it involves reading a single user input.
	 */
	private void specifyOutputFile(Scanner scanner) {
		System.out.println("Specify an Output File (default: ./out.txt)");
		outputFile = scanner.next();
	}

	//Configures the lexicons to be used for sentiment analysis.
	private void configureLexicons(Scanner scanner) {
		System.out.println("Configure Lexicons");
		System.out.println("Enter 'bingliu.txt'");
		lexiconFile = scanner.next();
		// Initialises lexicon variable
		ConcurrentSkipListMap<String, Double> lexicon = null;
		try {
			lexicon = lexiconLoader.parseLexicon(lexiconFile);
		} catch (Exception e) {
			System.out.println("Error: Unable to load lexicon: " + e.getMessage());
			e.printStackTrace();
		}
		fileProcessor = new SentimentParser(lexicon);
	}

	// Exit note.
	private void quit() {
		System.out.println("Exiting Application. Goodbye.");
	}

	/**
	 *  Executes sentiment analysis and report.
	 *  O(n), 'n' is the number of words in the text file so it depends on the analysis used.
	*/
	private void executeAnalyseAndReport() {
		if (textFile == null || textFile.isEmpty()) {
			System.out.println("No text file provided for analysis.");
			return;
		}
		try {
			double sentiment = fileProcessor.parseFile(textFile);
			writeSentimentToFile(sentiment);
		} catch (Exception e) {
			System.out.println("Error: Unable to analyze sentiment: " + e.getMessage());
		}
	}

	/**
	 * Writes sentiment report to file.
	 * O(1)
	 * @param sentimentValue
	 * @throws IOException
	 */
	public void writeSentimentToFile(double sentimentValue) throws IOException {
		if (outputFile == null || outputFile.isEmpty()) {
			System.out.println("No output file specified. Using default: ./out.txt");
			outputFile = "./out.txt";
		}
		String output = "Sentiment Value: " + sentimentValue;
		try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile, true))) {
			writer.println(output);
		} catch (IOException e) {
			System.out.println("An error occurred while writing to the file: " + e.getMessage());
		}
	}

    /** Menu.
	 *  O(n), 'n' is number of menu options, as it involves a loop iterating through the menu options.
     */
	public void displayMenu() {
		int option = 0;
		while (option != 5) {
			try {
				System.out.println(ConsoleColour.BLUE);
				System.out.println("************************************************************");
				System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
				System.out.println("*                                                          *");
				System.out.println("*             Virtual Threaded Sentiment Analyser          *");
				System.out.println("*                                                          *");
				System.out.println("************************************************************");
				System.out.println("(1) Specify a Text File");
				System.out.println("(2) Specify an Output File (default: ./out.txt)");
				System.out.println("(3) Configure Lexicons");
				System.out.println("(4) Execute, Analyse and Report");
				System.out.println("(5) Quit");

				System.out.print(ConsoleColour.BLUE);
				System.out.print("Select Option [1-5]>");
				System.out.println(" ");

				option = scanner.nextInt();

				handleOption(option, scanner);
			} catch (Exception e) {
				System.out.println("Invalid. Please enter an integer [1-5].");
				scanner.next();
			}
		}
	}
}