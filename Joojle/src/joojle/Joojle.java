package joojle;

/**
 * Link to the Github <a href="https://github.com/telos-matter/Joojle">repo</a>
 *
 * @author telos_matter
 */
public class Joojle {
	
	public static void main(String[] args) throws Exception {
		String jarFilePath;
		String query;
		int MAX = 50;
		
		if (args.length >= 2) {
			jarFilePath = args[0];
			query = args[1];
			if (args.length >= 3) {
				MAX = Integer.parseInt(args[2]);
			}
		} else {
			jarFilePath = ""; // Put the link to you jar file here if you are not using the args
			query = ""; // and the query here
		}
		
        Loader.loadFrom(jarFilePath);
        System.out.println(String.format("Loaded %d functions from `%s`", Loader.LOADED_FUNCTIONS.size(), jarFilePath));
        
        int count = 1;
        System.out.println(String.format("Showing top %d results:\n", MAX));
        for (Result result : Searcher.search(query, Loader.LOADED_FUNCTIONS)) {
        	System.out.println(String.format("%d: %s", count, result));
        	
        	count++;
        	if (MAX < count) {
        		break;
        	}
        }
	}
	
}
