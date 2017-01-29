import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ReadStatPriority {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException 
	{
		Scanner read = new Scanner(System.in);
		
		String[] healers = {"restoration", "mistweaver", "holy", "discipline"};
		String[] tanks = {"blood", "protection", "guardian", "brewmaster", "vengeance"};
		String[] dpsOnlyClass = {"mage", "hunter", "warlock", "rogue"};
		String[] dps = {"frost", "unholy", "havoc", "balance", "feral", "windwalker", "retribution", "shadow", "elemental", "enhancement", "arms", "fury"};
		
		System.out.println("Please enter a class to recieve stat priority:");
		String wowClass = read.nextLine();
		System.out.println("Enter the specialization:");
		String wowSpec = read.nextLine();
		String wowRole = "";
		
		if(Arrays.asList(dpsOnlyClass).contains(wowClass))
		{
			wowRole = "dps";
		} 
		else if (Arrays.asList(healers).contains(wowSpec))
		{
			wowRole = "healer";
		}
		else if (Arrays.asList(tanks).contains(wowSpec))
		{
			wowRole = "tank";
		}
		else if (Arrays.asList(dps).contains(wowSpec))
		{
			wowRole = "dps";
		}
		
		String inputUrl = wowSpec.toLowerCase() + "-" + wowClass.toLowerCase().replaceAll(" ", "-") + "-pve-" + wowRole.toLowerCase(); 
		String url = "http://www.icy-veins.com/wow/" + inputUrl + "-stat-priority";
		Document htmlDoc = Jsoup.connect(url).get();
		//Stat priority and weight are organized using the ol tag
		Elements priority = htmlDoc.select("ol");
		
		String scale = priority.text();
		String[] result = scale.split("; ");
		System.out.println();
		
		for(String s : result)
		{
			System.out.println(s);
		}
		
	}

}
