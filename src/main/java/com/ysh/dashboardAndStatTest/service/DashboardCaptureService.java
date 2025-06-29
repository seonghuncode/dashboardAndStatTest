package com.ysh.dashboardAndStatTest.service;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

@Service
public class DashboardCaptureService {

    public void captureJspAsPdf(String jspUrl, String outputPath) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\USER\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
       // options.setBinary("C:\\Users\\USER\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        options.addArguments("--headless=new");  // 최신 Chrome 기준 headless 모드
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--print-to-pdf=" + outputPath);
        //options.addArguments("--disable-dev-shm-usage"); // 중요!
        options.addArguments("--window-size=1920,1080");
        

        WebDriver driver = new ChromeDriver(options);
        try {
            driver.get(jspUrl);
            // 차트가 렌더링될 시간을 충분히 기다림
            //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            //wait.until(ExpectedConditions.presenceOfElementLocated(By.id("myChart")));
            Thread.sleep(5000); // 필요 시 더 조정

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}