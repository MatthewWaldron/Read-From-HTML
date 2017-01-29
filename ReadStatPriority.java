import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ReadStatPriority {

	public static void main(String[] args) throws IOException 
	{
		String url = "http://www.icy-veins.com/wow/fury-warrior-pve-dps-stat-priority";
		Document htmlDoc = Jsoup.connect(url).get();
		//Stat priority and weight are organized using the ol tag
		Elements priority = htmlDoc.select("ol");
		
		String scale = priority.text();
		String[] result = scale.split(";");
		for(String s : result)
		{
			System.out.println(s);
		}
		
	}

}
