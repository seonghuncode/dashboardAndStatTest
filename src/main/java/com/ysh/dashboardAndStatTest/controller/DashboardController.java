package com.ysh.dashboardAndStatTest.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.ysh.dashboardAndStatTest.service.DashboardCaptureService;
import com.ysh.dashboardAndStatTest.service.PdfService;

@Controller
public class DashboardController {
	
	@Autowired
    private PdfService pdfService;
	
	
	 @Autowired
	 private DashboardCaptureService captureService;


	@RequestMapping(value = "/Dashboard", method = RequestMethod.GET)
	public String dashboard() {
		return "dashboard/dashboard";
	}
	
	/** 해당 API로 요청이 오는 경우 해당 정보를 엑셀파일로 저장
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/export/excel")
	public void downloadExcel(HttpServletResponse  response) throws IOException {
		Workbook workbook = new XSSFWorkbook();
	    Sheet sheet = workbook.createSheet("취약점 통계");

	    Row header = sheet.createRow(0);
	    header.createCell(0).setCellValue("날짜");
	    header.createCell(1).setCellValue("방문자 수");
	    header.createCell(2).setCellValue("페이지뷰");

	    Row row1 = sheet.createRow(1);
	    row1.createCell(0).setCellValue("2025-06-26");
	    row1.createCell(1).setCellValue(1200);
	    row1.createCell(2).setCellValue(3000);

	    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	    response.setHeader("Content-Disposition", "attachment; filename=stat.xlsx");

	    workbook.write(response.getOutputStream());
	    workbook.close();
	}
	
	/** 해당 API로 요청이 오는 경우 데이터들에 대해 pdf로 저장
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/export/pdf")
	public void downloadPdf(HttpServletResponse response) throws IOException {
	    response.setContentType("application/pdf");
	    response.setHeader("Content-Disposition", "attachment; filename=stat.pdf");

	    Document document = new Document();
	    PdfWriter.getInstance(document, response.getOutputStream());

	    document.open();
	    document.add(new Paragraph("대시보드 통계 요약"));
	    document.add(new Paragraph("날짜: 2025-06-26"));
	    document.add(new Paragraph("방문자 수: 1200"));
	    document.add(new Paragraph("페이지뷰: 3000"));
	    document.close();
	}
	
	
	//JSP출력용
	 @RequestMapping("/report-view")
	 public String showReport(Model model) {
//	     List<ReportItem> items = Arrays.asList(
//	             new ReportItem("포트 점검", "양호"),
//	             new ReportItem("보안 패치", "미흡")
//	     );
//	     model.addAttribute("date", LocalDate.now().toString());
//	     model.addAttribute("items", items);
	     return "dashboard/dashboard"; // /WEB-INF/views/report.jsp
	 }
	 
	 @GetMapping("/download/report")
	 public ResponseEntity<byte[]> downloadPdf() {
	     byte[] pdf = pdfService.generatePdfFromJsp();
		 //byte[] pdf = null;


	     HttpHeaders headers = new HttpHeaders();
	     headers.setContentType(MediaType.APPLICATION_PDF);
	     headers.setContentDispositionFormData("attachment", "report.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }

	 
//	 @GetMapping("/download/report")
//	 public String downloadPdf() {
//	     String test = pdfService.test();
//		 //byte[] pdf = null;
//
//	     return test;
//    }
	 
	 

	    @GetMapping("/save-pdf")
	    public ResponseEntity<String> saveDashboardPdf() {
	        String jspUrl = "http://localhost:8081/Dashboard"; // 실제 JSP 경로
	        //String savePath = "/your/server/path/dashboard.pdf"; // 저장 경로
	        String savePath = "C:\\Users\\USER\\OneDrive\\바탕 화면\\개발\\dashboard.pdf"; // 저장 경로

	        captureService.captureJspAsPdf(jspUrl, savePath);
	        return ResponseEntity.ok("PDF 저장 완료: " + savePath);
	    }
	 
	
}
