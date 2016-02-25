package Services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Document;

public class CrawlWeb {
	
	static Map<String, ArrayList<String>> graph = new HashMap<String, ArrayList<String>>();
	
	public static void union(ArrayList<String> tocrawl,Collection<String> all_links)
	{
		for (String link : all_links) 
        {
			if(tocrawl.contains(link))
				continue;
				else
					tocrawl.add(link);
		}
    }
	public static  long crawl_web(String url,int max_page) throws Throwable
	{
		long startTime = System.currentTimeMillis();
		ArrayList<String> tocrawl=new ArrayList<String>();
		ArrayList<String> crawled=new ArrayList<String>();
		
		 int max_depth = 6;
		String page=null;
		
		tocrawl.add(url);
		
		int i=tocrawl.size();
	
		
		
		while(!(tocrawl.isEmpty()))
		{
			--i;
			page=tocrawl.get(i);
			tocrawl.remove(i);
			if(!(crawled.contains(page))&&(crawled.size()<max_page))
			{
				max_depth=max_depth-1;
				//System.out.println(max_depth);
				if(max_depth<0)
					break;
			}
			
		
		Document doc=LinkAnalyzer.getPage(page);
		Indexing.add_page_to_index(page, doc);
		ArrayList<String> links = LinkAnalyzer.getAllLinks(doc);
		union(tocrawl,links);
		graph.put(page, links);
		crawled.add(page);
		if(i==0)
		{
			break;
		}
	}
		long stopTime = System.currentTimeMillis();
		long total_time = stopTime - startTime;
		
		return total_time;
		
	}
	public static void main(String args[]){
		int i=5;
		try {
			CrawlWeb.crawl_web("https://www.computer.org/",i);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	}


