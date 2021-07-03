package at.ac.fhcampuswien.newsanalyzer.ui;


import at.ac.fhcampuswien.newsanalyzer.ctrl.Controller;
import at.ac.fhcampuswien.newsanalyzer.ctrl.NewsAPIException;
import at.ac.fhcampuswien.newsanalyzer.downloader.Downloader;
import at.ac.fhcampuswien.newsanalyzer.downloader.ParallelDownloader;
import at.ac.fhcampuswien.newsanalyzer.downloader.SequentialDownloader;
import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsapi.enums.Country;
import at.ac.fhcampuswien.newsapi.enums.Endpoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class UserInterface {

	private Controller ctrl = new Controller();

	public void getDataForCustomInput() {
		System.out.println("Enter search query:");
		String searchQuery = readLine();
		System.out.println("Enter amount of results (max 100):");
		Double resultsAnmount = readDouble(0,100);
		NewsApi newsApi = new NewsApiBuilder()
				.setApiKey(Controller.APIKEY)
				.setQ(searchQuery)
				.setEndPoint(Endpoint.EVERYTHING)
				.setPageSize(String.valueOf(resultsAnmount.intValue()))
				.createNewsApi();
		String result = null;
		try {
			result = ctrl.process(newsApi);
		} catch (NewsAPIException e) {
			System.err.println("Error occured: " + e.getMessage());
		}

		System.out.println(result);
	}


	public void start() {
		Menu<Runnable> menu = new Menu<>("User Interface");
		menu.insert("a", "Top headlines for Austria", this::getTopHeadlinesAustria);
		menu.insert("b", "All news for 'bitcoin' in June", this::getAllNewsBitcoin);
		menu.insert("d", "Search based on user input", this::getDataForCustomInput); // Exercise 3
		menu.insert("w", "Get provider with max articles", this::getProviderWithMostArticles);	// Exercise 3
		menu.insert("x", "Shortest author name", this::getShortestNameOfAuthors);	// Exercise 3
		menu.insert("y", "Get article count", this::getArticleCount);	// Exercise 3
		menu.insert("z", "Sort by longest title", this::getSortArticlesByLongestTitle); // Exercise 3
		menu.insert("p", "Download Last Search", () -> {
			System.out.println("Sequential Download: ");
			long start2seq = System.currentTimeMillis();
			ctrl.downloadNews((new SequentialDownloader()));
			long end2seq = System.currentTimeMillis();
			System.out.println("Elapsed Time in milli seconds: "+ (end2seq-start2seq));

			System.out.println("\nParallel Download: ");
			long start2par = System.currentTimeMillis();
			ctrl.downloadNews((new ParallelDownloader()));
			long end2par = System.currentTimeMillis();
			System.out.println("Elapsed Time in milli seconds: "+ (end2par-start2par));

		}); //Todo
		menu.insert("q", "Quit", null);
		Runnable choice;
		while ((choice = menu.exec()) != null) {
			choice.run();
		}
		System.out.println("Program finished");
	}


	protected String readLine() {
		String value = "\0";
		BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
		try {
			value = inReader.readLine();
		} catch (IOException ignored) {

		}
		return value.trim();
	}

	protected Double readDouble(int lowerlimit, int upperlimit) 	{
		Double number = null;
		while (number == null) {
			String str = this.readLine();
			try {
				number = Double.parseDouble(str);
			} catch (NumberFormatException e) {
				number = null;
				System.out.println("Please enter a valid number:");
				continue;
			}
			if (number < lowerlimit) {
				System.out.println("Please enter a higher number:");
				number = null;
			} else if (number > upperlimit) {
				System.out.println("Please enter a lower number:");
				number = null;
			}
		}
		return number;
	}

	private void getShortestNameOfAuthors() {
		try {
			String result = ctrl.getShortestNameOfAuthors();
			System.out.println("The shortest author name is " + result);
		} catch (NewsAPIException e) {
			System.out.println("Please load data first!");
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	private void getArticleCount() {
		try {
			long result = ctrl.getArticleCount();
			System.out.println(result + " articles loaded");
		} catch (NewsAPIException e) {
			System.out.println("Please load data first!");
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	protected void getSortArticlesByLongestTitle() {
		try {
			String result = ctrl.getSortArticlesByLongestTitle();
			System.out.println("Longest title: " + result);
		} catch (NewsAPIException e) {
			System.out.println("Please load data first!");
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	protected void getProviderWithMostArticles() {
		try {
			String result = ctrl.getProviderWithMostArticles();
			System.out.println("Most articles: " + result);
		} catch (NewsAPIException e) {
			System.out.println("Please load data first!");
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	public void getTopHeadlinesAustria() {
		NewsApi newsApi = new NewsApiBuilder()
				.setApiKey(Controller.APIKEY)
				.setQ("corona")
				.setEndPoint(Endpoint.TOP_HEADLINES)
				.setSourceCountry(Country.at)
				.createNewsApi();

		String result = null;
		try {
			result = ctrl.process(newsApi);
		} catch (NewsAPIException e) {
			System.err.println("Error occured: " + e.getMessage());
		} catch (Exception e){
			System.out.println(e.getMessage());
		}

		System.out.println(result);
	}

	public void getAllNewsBitcoin() {
		NewsApi newsApi = new NewsApiBuilder()
				.setApiKey(Controller.APIKEY)
				.setQ("bitcoin")
				.setEndPoint(Endpoint.EVERYTHING)
				.setFrom("2021-06-14")
				.setPageSize("100")
				.createNewsApi();

		String result = null;
		try {
			result = ctrl.process(newsApi);
		} catch (NewsAPIException e) {
			System.err.println("Error occured: " + e.getMessage());
		} catch (Exception e){
			System.out.println(e.getMessage());
		}

		System.out.println(result);
	}

}