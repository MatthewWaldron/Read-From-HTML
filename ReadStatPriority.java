import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ReadStatPriority {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException 
	{
		Scanner read = new Scanner(System.in);
		boolean runAgain = true;
		while(runAgain == true)
		{
		runAgain = false;
		System.out.println("Please enter a class to recieve stat priority:");
		String wowClass = read.nextLine();
		System.out.println("Enter the specialization:");
		String wowSpec = read.nextLine();
		String wowRole = checkRole(wowClass, wowSpec);
	
		
		if(wowRole != null)
		{
			String inputUrl = wowSpec.toLowerCase() + "-" + wowClass.toLowerCase().replaceAll(" ", "-") + "-pve-" + wowRole.toLowerCase(); 
			String url = "http://www.icy-veins.com/wow/" + inputUrl + "-stat-priority";
			Document htmlDoc = null;
			
			try{
				htmlDoc = Jsoup.connect(url).get();
			} catch (org.jsoup.HttpStatusException e) {
				System.out.println("Unable to find http address: " + e.getMessage());
				runAgain = true;
			}
			
			if(htmlDoc != null)
			{
				//Stat priority and weight are organized using the <ol> tag
				//Headers such as "Stat Weights" are organized using the <h2> and <h3> tags
				Elements priority = htmlDoc.select("ol");

				
				//Each <ol> has a header before it, but the first header may not have an <ol> after it
				for(Element l : priority)
				{
					//This will check the previous elements for each ol and find the respective header
					//Have to use a loop to find the corresponding header since some ol have paragraphs in between
					Element prev = l.previousElementSibling();
					while(prev != null && (!prev.tag().toString().equals("h2") && !prev.tag().toString().equals("h3")))
					{
						prev = prev.previousElementSibling();
					}
					
					//Will output the header and format it to remove the "1. " or "1.2. " without removing space between words
					if(prev != null)
						System.out.println(prev.text().replaceAll("([0-9])|(\\.)|(?<=[^\\w]) ", "") + ":");
					
					//Change spaces, periods, etc to ";" so that it can be split using a semicolon
					String scale = l.text();
					scale = scale.replaceAll("\\. ", ";");
					scale = scale.replaceAll("; ", ";");
					scale = scale.replaceAll("(?<=[0-9]) ",";");
					String[] result = scale.split(";");
					
					for(String s : result)
					{
						System.out.println("  " + s.replaceAll("\\.", ""));
					}
					System.out.println();
				}
			}
			else
			{
				System.out.println("Cannot find class/spec combination, please try again.");
			}
		}
		else
		{
			System.out.println("Unable to find class/spec, please try again.");
			runAgain = true;
		}
		}
		
	}
	
	private static String checkRole(String wowClass, String wowSpec)
	{
		String[] healers = {"restoration", "mistweaver", "holy", "discipline"};
		String[] tanks = {"blood", "protection", "guardian", "brewmaster", "vengeance"};
		String[] dpsOnlyClass = {"mage", "hunter", "warlock", "rogue"};
		String[] dps = {"frost", "unholy", "havoc", "balance", "feral", "windwalker", "retribution", "shadow", "elemental", "enhancement", "arms", "fury"};
		
		if(Arrays.asList(dpsOnlyClass).contains(wowClass))
		{
			return "dps";
		} 
		else if (Arrays.asList(healers).contains(wowSpec))
		{
			return "healing";
		}
		else if (Arrays.asList(tanks).contains(wowSpec))
		{
			return "tank";
		}
		else if (Arrays.asList(dps).contains(wowSpec))
		{
			return "dps";
		}
		return null;
	}

}
