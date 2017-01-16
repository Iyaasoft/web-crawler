package com.wiprodigital;

import com.wiprodigital.crawler.CrawlerApplication;
import com.wiprodigital.crawler.CrawlerConfiguration;
import com.wiprodigital.crawler.parser.CrawlerPageParser;
import com.wiprodigital.crawler.service.WebCrawler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertTrue;

/**
 * Created by abuayyub on 11/01/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest( classes = {CrawlerConfiguration.class,CrawlerApplication.class})
@WebAppConfiguration
public class WebCrawlerPageAccessTest {

    @Autowired
    WebCrawler webCrawler;

    @Autowired
    CrawlerPageParser crawlerPageParser;

    @Test
    public void getAnyWebPage() throws Exception {
        String url = "http://wiprodigital.com";

        String requestedPage = webCrawler.getPageForUrl(url);
        assertThat(requestedPage, is(notNullValue()));
        System.out.println(requestedPage);

    }


    @Test
    public void getPageForUrlThatDoesNotExist() throws Exception {

        String url = "http://myMadeUpDomain.com";
        try {
           webCrawler.getPageForUrl(url);
        }  catch (Exception e) {
            assertTrue(e.getCause().getMessage().contains("not found"));
        }

    }


    @Test
    public void parseWebPageString() {

        String[] anchors = getSample().split("<a");

        List<String> hrefs = crawlerPageParser.parseStringAndExractDomainHref(anchors[0],"wiprodigital.com");
        assertThat(hrefs.size() ,is(greaterThan(0)));

    }




    private String getSample() {
        return "   <li class=\"wd-navbar-nav-elem\">" +
                "<a class=\"wd-navbar-nav-elem-link wd-nav-elem-link\" " +
                "href=\"http://wiprodigital.com/what-we-do\">What we do</a> " +
                "<ul class=\"wd-navbar-nav-elem-ddmenu dropdown-menu\"> " +
                "                               <li>" +
                "                                    " +
                "<a class=\"wd-navbar-nav-elem-link wd-nav-elem-link\" " +
                "href=\"http://wiprodigital.com/what-we-do#work-three-circles-row\">" +
                "Services</a></li>                                <li>" +
                "                                    <a class=\"wd-navbar-nav-elem-link wd-nav-elem-link\" " +
                "href=\"http://wiprodigital.com/what-we-do#wdwork_cases\">Case studies</a>" +
                "</li>                                <li>" +
                "                                    <a class=\"wd-navbar-nav-elem-link wd-nav-elem-link\" " +
                "href=\"http://wiprodigital.com/what-we-do#wdwork_partners\">Partners</a> " +
                "                               </li>                            </ul>   " +
                "                     </li>                        <li class=\"wd-navbar-nav-elem\">     " +
                "                       <a class=\"wd-navbar-nav-elem-link wd-nav-elem-link\" href=\"http://wiprodigital.com/what-we-think\">What we think</a>   " +
                "                         <ul class=\"wd-navbar-nav-elem-ddmenu dropdown-menu\">      " +
                "                          <li>                                    <a class=\"wd-navbar-nav-elem-link wd-nav-elem-link\" " +
                "href=\"http://wiprodigital.com/?s=&post_type[]=post\">Insights</a></li>           " +
                "                        <li>                                    <a class=\"wd-navbar-nav-elem-link wd-nav-elem-link\" " +
                "href=\"http://wiprodigital.com/?s=&post_type[]=cases\">Cases</a></li>" +
                "                                <li>               " +
                "                     <a class=\"wd-navbar-nav-elem-link wd-nav-elem-link\" " +
                "href=\"http://wiprodigital.com/?s=&post_type[]=events\">Events</a>" +
                "</li>                                <li>" +
                "                                    <a class=\"wd-navbar-nav-elem-link wd-nav-elem-link\" " +
                "href=\"http://wiprodigital.com/?s=&post_type[]=news\">News</a></li>" +
                "                            </ul>                        </li>" +
                "                        <li class=\"wd-navbar-nav-elem\">     ";
    }

}
