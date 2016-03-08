package singleProcess;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Document;

public class CrawlWeb {
	
	static Map<String, ArrayList<String>> graph = new HashMap<String, ArrayList<String>>();
	static ArrayList<String> tocrawl=new ArrayList<String>();
	 private static final long startTime = System.currentTimeMillis();
	public static int union(ArrayList<String> tocrawl,Collection<String> all_links)
	{
		
		for (String link : all_links) 
        {
			if(tocrawl.contains(link))
				continue;
				else
					tocrawl.add(link);
		}
	
		return tocrawl.size();
    }
	public static  void crawl_web(String url,int max_page) throws Throwable
	{
		DataBase db=new DataBase();
		long startTime = System.currentTimeMillis();
		
		ArrayList<String> crawled=new ArrayList<String>();
		
		 int max_depth = 6;
		String page=null;
		
		tocrawl.add(url);
		
		int size=tocrawl.size();
		
		
		while(!(tocrawl.isEmpty()))
		{
			--size;
			page=tocrawl.get(size);
		
			tocrawl.remove(size);
			if(!(crawled.contains(page))&&(crawled.size()<max_page))
			{
				System.out.println(page);
				max_depth=max_depth-1;
				System.out.println(max_depth);
				if(max_depth<0)
					break;			
		
		Document doc=LinkAnalyzer.getPage(page);
		Indexing.add_page_to_index(page, doc);
		ArrayList<String> links = LinkAnalyzer.getAllLinks(doc);
		size=union(tocrawl,links);
		graph.put(page, links);
		if(!(graph.isEmpty()))
				{
			String sql4 = "INSERT IGNORE INTO crawler.graph(URL,RE_URL)" + "VALUES ('"+page+"','"+links+"');";
			PreparedStatement stmt = db.conn.prepareStatement(sql4, Statement.RETURN_GENERATED_KEYS);
			stmt.execute();
			
				}
		
		
		
		crawled.add(page);
		if(size==0)
		{
			break;
		}
			}
			
	}
		long stopTime = System.currentTimeMillis();
		long total_time = stopTime - startTime;
		System.out.println(total_time+"ms");

		
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


