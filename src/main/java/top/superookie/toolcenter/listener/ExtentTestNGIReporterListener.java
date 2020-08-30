package top.superookie.toolcenter.listener;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.ResourceCDN;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExtentTestNGIReporterListener implements IReporter {

    private static final String OUTPUT_FOLDER = "test-output/";
    private static final String FILE_NAME = "Extent.html";

    private static final String jsString = "var cssLocations = './config/extent.css';\n" +
            "var jsLocation = './config/extent.js';\n" +
            "var body = document.querySelector('body');\n" +
            "var head = document.querySelector('head');\n" +
            "\n" +
            "var link = document.createElement('link')\n" +
            "link.href = cssLocations;\n" +
            "link.type = 'text/css';\n" +
            "link.rel = 'stylesheet';\n" +
            "head.appendChild(link);\n" +
            "\n" +
            "var script = document.createElement('script');\n" +
            "script.src = jsLocation;\n" +
            "script.type = 'text/javascript';\n" +
            "body.appendChild(script);";

    private ExtentReports extent;

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        init();

        for (ISuite suite : suites) {
            Map<String, ISuiteResult> result = suite.getResults();
            for (ISuiteResult r : result.values()) {
                ITestContext context = r.getTestContext();
                buildTestNodes(context.getFailedTests(), Status.FAIL);
                buildTestNodes(context.getSkippedTests(), Status.SKIP);
                buildTestNodes(context.getPassedTests(), Status.PASS);
            }
        }

        for (String s : Reporter.getOutput()) {
            extent.setTestRunnerOutput(s);
        }

        extent.flush();
    }

    private void init() {
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(OUTPUT_FOLDER + FILE_NAME);
        htmlReporter.config().setEncoding("GBK");
        htmlReporter.config().setDocumentTitle("自动化测试报告");
        htmlReporter.config().setReportName("API自动化测试报告");
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP); //图表位置
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setResourceCDN(ResourceCDN.EXTENTREPORTS);
        htmlReporter.config().setJS(jsString);
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setReportUsesManualConfiguration(true);
    }

    private void buildTestNodes(IResultMap tests, Status status) {
        ExtentTest test;
        if (tests.size() > 0) {
            for (ITestResult result : tests.getAllResults()) {
                String fullName = result.getMethod().getQualifiedName();
                test = extent.createTest(getLastThreeSubSeq(fullName)); //显示方法名称

                for (String group : result.getMethod().getGroups()) {
                    test.assignCategory(group); //根据group
                }

                if (result.getThrowable() != null) {
                    test.log(status, result.getThrowable()); //异常案例，显示log到报告
                } else {
                    test.log(status, "Test " + status.toString().toLowerCase() + "ed");
                }

                test.getModel().setStartTime(getTime(result.getStartMillis()));
                test.getModel().setEndTime(getTime(result.getEndMillis()));
                test.getModel().setDescription("测试案例执行！");
            }
        }
    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }

    private static String getLastThreeSubSeq(String name) {
        String[] names = name.split("\\.");
        int len = names.length;

        String[] result = new String[3];

        for (int i = 0; i < 3; i++) {
            result[i] = names[len - 3 + i];
        }

        return String.join(".", result);
    }


}
