import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {

	public static void main(String[] args) {
			Document doc = null;
			String hrefUrl = "http://info.sporttery.cn/football/info/fb_team_info.php?tid=";
			try {
				Connection connect = Jsoup.connect("http://info.sporttery.cn/football/history/history_data.php?mid=127");
				doc = connect.get();
				Elements elementsByClass = doc.getElementsByClass("integral");
				for(Element elem: elementsByClass){
					Element elementById = elem.getElementById("jfg");
					if(null != elementById) {
						Elements elementsByTag = elem.getElementsByTag("a");
						for(Element e: elementsByTag) {
							String attr = e.attr("href");
							if(attr.startsWith(hrefUrl)) {
								String substring = attr.substring(hrefUrl.length());
							}
							System.out.println("");
						}
					}
				}
				System.out.println("end");
			}catch(Exception e) {
				e.printStackTrace();
			}
		            
	}
}
