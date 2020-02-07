package ticketrank;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import domain.MovieDTO;
import persistence.MovieDAO;

public class DaumTicket {
	String base = "";
	int page = 1; // 수집한 페이지수
	String url = "";
	int count = 0; // 전체댓글수
	int total = 0; // 평점을 모두 더하는 변수
	MovieDAO mDao = new MovieDAO();
		
	public TicketDTO daumCrawler(String movie, String code) throws IOException{ //throws 예외 던지기 (예외처리 님이 알아서하셈
		base ="https://movie.daum.net/moviedb/grade?movieId="+code+"&type=netizen&page=";
		url = base + page;
		System.out.println("■■ DAUM START ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		
		//페이지를 돌면서 댓글을 수집
				while (true) {
					//1페이지의 평점 10건 수집
					Document doc = Jsoup.connect(url).get();
					Elements reply = doc.select("ul.list_netizen div.review_info");
					
					//마지막 페이지면 수집 종료
					if(reply.isEmpty()) {
						break;
					}

					int score, regdate = 0;
					String writer, content, basedate, subdate = "";
					
					//평점 1건 수집
					for (Element one : reply) {
						count++;

						writer = one.select("em.link_profile").text();
						score = Integer.parseInt(one.select("em.emph_grade").text());
						content = one.select("p.desc_review").text();
						basedate = one.select("span.info_append").text();
						subdate = basedate.substring(0,10);
						regdate  = Integer.parseInt(subdate.replace(".", ""));
						
						//누적 평점을 계산
						total += score; //total = total+score;
						
						//1건에 평점을 DTO에 담음
						MovieDTO mDto = new MovieDTO(movie, content, writer, score, "daum", regdate);
						
						//DTO에 담긴 1건의 평점을 DB에 저장!
						mDao.addMovie(mDto);
						System.out.println("▶▶▶▶▶▶▶▶▶▶▶▶▶" + count + "건◀◀◀◀◀◀◀◀◀◀◀◀◀◀");
						System.out.println("영화 : " + movie);
						System.out.println("평점 : " + score);
						System.out.println("작성자 : " + writer);
						System.out.println("내용 : " + content);
						System.out.println("작성일 : " + regdate);
					}
					// 다음 페이지로 이동하기 위해 page+1증가
					page = page +1;
					
					// 다음 페이지로 이동할때 URL작성
					url = base + page;
				}
				System.out.println("■■ DAUM END ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
				// TicketMain으로 댓글수, 평점의 합을 return
				TicketDTO tDto = new TicketDTO(count, total);
				return tDto;
	}

}
