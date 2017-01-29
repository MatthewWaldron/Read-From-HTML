import java.io.IOException;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ReadStatPriority {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException 
	{
		Scanner read = new Scanner(System.in);
		
		System.out.println("Enter the class:");
		String wowClass = read.nextLine();
		System.out.println("Enter the specialization:");
		String wowSpec = read.nextLine();
		System.out.println("Enter the role (dps, tank, healer)");
		String wowRole = read.nextLine();
		
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
