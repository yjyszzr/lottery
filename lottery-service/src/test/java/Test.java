import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dl.base.result.ResultGenerator;
import com.dl.shop.lottery.core.ProjectConstant;

public class Test {

	public static void main(String[] args) {
		int betEndTime = 1524650400 - ProjectConstant.BET_PRESET_TIME;
		LocalDateTime localDate = LocalDateTime.ofEpochSecond(betEndTime, 0, ZoneOffset.UTC);
		LocalDateTime now = LocalDateTime.now();
		if(localDate.isBefore(now)) {
			System.out.println("ok");
		}
		            
	}
}
