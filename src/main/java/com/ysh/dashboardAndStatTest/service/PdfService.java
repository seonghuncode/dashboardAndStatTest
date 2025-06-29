package com.ysh.dashboardAndStatTest.service;

import java.io.ByteArrayOutputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.xhtmlrenderer.pdf.ITextRenderer;


@Service
public class PdfService {
	
	@Autowired
    private WebApplicationContext webApplicationContext;


//    public byte[] generatePdfFromJsp() {
//        // JSP 렌더링된 HTML 가져오기
//        RestTemplate restTemplate = new RestTemplate();
//        //String jspHtml = restTemplate.getForObject("http://localhost:8081/report-view", String.class);
//        String jspHtml = restTemplate.getForObject("http://localhost:8081/report-view", String.class);
//        //String jspHtml = "";
//
//        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
//            ITextRenderer renderer = new ITextRenderer();
//            renderer.setDocumentFromString(jspHtml);
//            renderer.layout();
//            renderer.createPDF(os);
//            return os.toByteArray();
//        } catch (Exception e) {
//            throw new RuntimeException("PDF 생성 실패", e);
//        }
//    }
	
	
	
	//=========================================================================================
    
	public byte[] generatePdfFromJsp() {
	    try {
	        String url = "http://localhost:8081/report-view";
	        RestTemplate restTemplate = new RestTemplate();
	        String jspHtml = restTemplate.getForObject(url, String.class);
	        
	        if (jspHtml == null || jspHtml.trim().isEmpty()) {
	            throw new RuntimeException("JSP에서 빈 HTML이 반환되었습니다.");
	        }
	        
	        // HTML을 XHTML로 정리 (엔티티 이스케이프 포함)
	        String cleanHtml = cleanHtmlForPdf(jspHtml);
	        
	        return createPdfFromHtml(cleanHtml);
	        
	    } catch (Exception e) {
	        throw new RuntimeException("PDF 생성 실패: " + e.getMessage(), e);
	    }
	}

	private String cleanHtmlForPdf(String html) {
	    try {
	        // 1. 먼저 기본적인 엔티티 이스케이프 처리
	        html = escapeXmlEntities(html);
	        
	        // 2. JSoup으로 HTML 파싱 및 XHTML로 변환
	        Document doc = Jsoup.parse(html);
	        doc.outputSettings()
	           .syntax(Document.OutputSettings.Syntax.xml)
	           .escapeMode(Entities.EscapeMode.xhtml)
	           .charset("UTF-8");
	        
	        String cleanedHtml = doc.html();
	        
	        // 3. 추가 정리
	        cleanedHtml = additionalCleanup(cleanedHtml);
	        
	        // 디버깅용 로그
	        System.out.println("=== HTML 정리 완료 ===");
	        System.out.println("Original length: " + html.length());
	        System.out.println("Cleaned length: " + cleanedHtml.length());
	        
	        return cleanedHtml;
	        
	    } catch (Exception e) {
	        System.out.println("JSoup 처리 실패, 수동 정리 방법 사용: " + e.getMessage());
	        return manualHtmlClean(html);
	    }
	}

	private String escapeXmlEntities(String html) {
	    if (html == null) return "";
	    
	    // XML 엔티티 이스케이프 (순서 중요!)
	    html = html.replace("&", "&amp;");  // 먼저 & 처리
	    html = html.replace("<", "&lt;");   // < 처리 (단, HTML 태그는 제외해야 함)
	    html = html.replace(">", "&gt;");   // > 처리 (단, HTML 태그는 제외해야 함)
	    
	    // 위의 방법은 모든 < > 를 바꾸므로 HTML 태그를 망가뜨림
	    // 더 정확한 방법으로 다시 처리
	    return escapeNonHtmlEntities(html);
	}

	private String escapeNonHtmlEntities(String html) {
	    // 이미 이스케이프된 엔티티는 건드리지 않음
	    html = html.replaceAll("&(?!(amp;|lt;|gt;|quot;|apos;|#\\d+;|#x[0-9a-fA-F]+;))", "&amp;");
	    
	    return html;
	}

	private String additionalCleanup(String html) {
	    // 자체 닫히는 태그 정리 (JSoup이 놓친 것들)
	    html = html.replaceAll("<meta([^>]*?)(?<!/)>", "<meta$1/>");
	    html = html.replaceAll("<link([^>]*?)(?<!/)>", "<link$1/>");
	    html = html.replaceAll("<br([^>]*?)(?<!/)>", "<br$1/>");
	    html = html.replaceAll("<hr([^>]*?)(?<!/)>", "<hr$1/>");
	    html = html.replaceAll("<img([^>]*?)(?<!/)>", "<img$1/>");
	    html = html.replaceAll("<input([^>]*?)(?<!/)>", "<input$1/>");
	    
	    // 이중 슬래시 제거
	    html = html.replaceAll("//>/", "/>");
	    
	    return html;
	}

	private String manualHtmlClean(String html) {
	    // JSoup 없이 수동으로 정리
	    
	    // 1. 엔티티 이스케이프
	    html = html.replaceAll("&(?!(amp;|lt;|gt;|quot;|apos;|#\\d+;|#x[0-9a-fA-F]+;))", "&amp;");
	    
	    // 2. DOCTYPE 추가
	    if (!html.contains("<!DOCTYPE")) {
	        html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" " +
	               "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" + html;
	    }
	    
	    // 3. 자체 닫히는 태그 정리
	    html = html.replaceAll("<meta([^>]*?)(?<!/)>", "<meta$1/>");
	    html = html.replaceAll("<link([^>]*?)(?<!/)>", "<link$1/>");
	    html = html.replaceAll("<br([^>]*?)(?<!/)>", "<br$1/>");
	    html = html.replaceAll("<hr([^>]*?)(?<!/)>", "<hr$1/>");
	    html = html.replaceAll("<img([^>]*?)(?<!/)>", "<img$1/>");
	    html = html.replaceAll("<input([^>]*?)(?<!/)>", "<input$1/>");
	    html = html.replaceAll("<area([^>]*?)(?<!/)>", "<area$1/>");
	    html = html.replaceAll("<base([^>]*?)(?<!/)>", "<base$1/>");
	    html = html.replaceAll("<col([^>]*?)(?<!/)>", "<col$1/>");
	    
	    // 4. 이중 슬래시 제거
	    html = html.replaceAll("//>/", "/>");
	    
	    return html;
	}

	private byte[] createPdfFromHtml(String html) throws Exception {
	    try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
	        ITextRenderer renderer = new ITextRenderer();
	        renderer.setDocumentFromString(html);
	        renderer.layout();
	        renderer.createPDF(os);
	        return os.toByteArray();
	    }
	}
	
//	public String test() {
//		return "test";
//	}
	
}