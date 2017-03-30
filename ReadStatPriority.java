import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * This is a project is related to an online game, and it is used to find recommended statistic priorities and weights from a well known website: icy-veins.com.
 * It was created so that finding the recommended statistics can be more convenient.
 * Given a class (ex. mage, priest, paladin) and a specialization specific to that class (ex. frost, fire, holy, protection), it will return the statistic priorities and
 * 	statistic weights if they are listed. Users are first prompt to enter the class, and then prompt to enter the specialization.
 * 
 *  Some sample inputs would be: 
 *  first prompt: "mage", second prompt: "frost" or "fire" or "arcane"
 *  first prompt: "warrior", second prompt: "fury" or "arms" or "protection"
 *  first prompt: "demon hunter", second prompt: "vengeance" or "havoc"
 *  
 * @author Matthew Waldron
 */

public class ReadStatPriority {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException 
	{
		Scanner read = new Scanner(System.in);
		boolean runAgain = false;
		
		// This loop is used so that if the user enters invalid information, they may re-enter it without restarting the program.
		do
		{
			
		// The user will be prompt to first enter the class they wish to search, and then the specialization of that class.
		System.out.println("Please enter a class to recieve the stat priority of:");
		String wowClass = read.nextLine().toLowerCase();
		System.out.println("Enter the specialization:");
		String wowSpec = read.nextLine().toLowerCase();
		
		// After obtaining the class and specialization, it will call a method to get the role (damage dealer(dps), healer, or tank).
		String wowRole = checkRole(wowSpec);
	
		// This if statement is checking to make sure that a role was able to be derived from the specialization.
		if(wowRole != null)
		{
			
			// Since some classes and specializations are two words ("death knight", "beast mastery"), the spaces need to be changed to hyphens.
			// After this, the url will be built using the specialization, class, and role. The website urls always follow this pattern for the statistics pages
			String inputUrl = wowSpec.replaceAll(" ", "-") + "-" + wowClass.replaceAll(" ", "-") + "-pve-" + wowRole; 
			String url = "http://www.icy-veins.com/wow/" + inputUrl + "-stat-priority";
			Document htmlDoc = null;
			
			// The program will try to connect to the url and get the html code of the website. If an error occurs it means that the input was not a valid combination.
			try{
				htmlDoc = Jsoup.connect(url).get();
			} catch (org.jsoup.HttpStatusException e) {
				System.out.println("Unable to find http address: " + e.getMessage());
				runAgain = true;
			} catch (java.io.EOFException f) {
				System.out.println("Unable to complete the request at this time: " + f.getMessage());
				runAgain = true;
			}
			
			if(htmlDoc != null)
			{
				/* Statistic priority and weight are the only elements organized on the pages using the <ol> tag.
				 * Some specializations have multiple priorities or weights.
				 * Headers such as "Stat Weights" are organized using the <h2> and <h3> tags.
				 * First, the elements using the <ol> tag will be selected.
				 */
				Elements priority = htmlDoc.select("ol");

				
				// Each <ol> has a header before it, but the first header may not have an <ol> after it.
				// Each element in priority will be all of the data in the specific <ol> tag. 
				for(Element l : priority)
				{
					// This will check the previous elements for each <ol> and find the respective header, since each <ol> will have a header.
					// A loop is used to find the corresponding header since some <ol> have paragraphs in between.
					Element prev = l.previousElementSibling();
					while(prev != null && (!prev.tag().toString().equals("h2") && !prev.tag().toString().equals("h3")))
					{
						prev = prev.previousElementSibling();
					}
					
					// This will output the header and format it to remove the "1. " or "1.2. " without removing space between words.
					if(prev != null)
						System.out.println(prev.text().replaceAll("([0-9])|(\\.)|(?<=[^\\w]) ", "") + ":");
					
					// Change spaces, periods, etc to ";" so that it can be split using a semicolon.
					String scale = l.text();
					scale = scale.replaceAll("(?<=[0-9]) |(\\.\\z)|(; )",";");
					String[] result = scale.split(";");
					
					// This will print out the priorities/weights alone with numbering them so they are easier to understand.
					for(int i = 0; i < result.length; i++)
					{
						System.out.println("  " + (i+1) + ". " +  result[i]);
					}
					System.out.println();
					runAgain = false;
				}
			}
			else
			{
				System.out.println("Cannot find class/spec combination, please try again.\n");
				runAgain = true;
			}
		}
		else
		{
			System.out.println("Unable to find class/spec, please try again.\n");
			runAgain = true;
		}
		} while(runAgain == true);
	}
	

	/**
	 * This method will use the specialization to determine the role of the specialization. Since there is a limited amount of specializations and roles, arrays are used
	 * 	to hold all of the specializations that correspond to the specific roles. It will check to see which array holds the parameter passed into the method, and return
	 * 	the corresponding role. If the parameter is not found in any array, then it will return null.  
	 * @param String wowSpec; The specialization used to find the role
	 * @return String; The role of the given specialization. If one is not found, then it will return null.
	 */
	private static String checkRole(String wowSpec)
	{
		String[] healers = {"restoration", "mistweaver", "holy", "discipline"};
		String[] tanks = {"blood", "protection", "guardian", "brewmaster", "vengeance"};
		String[] dps = {"frost", "unholy", "havoc", "balance", "feral", "windwalker", "retribution", "shadow", "elemental", "enhancement", "arms", "fury","frost", "fire",
							 "arcane", "survival", "marksmanship", "beast mastery", "destruction", "demonology", "affliction", "subtlety", "assassination", "outlaw"};
		
		if(Arrays.asList(dps).contains(wowSpec))
		{
			return "dps";
		} 
		else if (Arrays.asList(tanks).contains(wowSpec))
		{
			return "tank";
		}
		else if (Arrays.asList(healers).contains(wowSpec))
		{
			return "healing";
		}
		return null;
	}
}
