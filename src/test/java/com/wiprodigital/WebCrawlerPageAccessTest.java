package com.wiprodigital;

import com.wiprodigital.crawler.CrawlerApplication;
import com.wiprodigital.crawler.CrawlerConfiguration;
import com.wiprodigital.crawler.exception.CrawlerPageNotFoundException;
import com.wiprodigital.crawler.rest.WebCrawler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by abuayyub on 11/01/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest( classes = {CrawlerConfiguration.class,CrawlerApplication.class})
@WebAppConfiguration
public class WebCrawlerPageAccessTest {

    @Autowired
    WebCrawler webCrawler;

    @Test
    public void getAnyWebPage(){
        String url = "http://wiprodigital.com";

        String requestedPage =webCrawler.getPageForUrl(url);
        assertThat(requestedPage, is(notNullValue()));

    }


    @Test(expected=CrawlerPageNotFoundException.class)
    public void getPageForUrlThatDoesNotExist() {

        String url = "http://myMadeUpDomain.com";
        String requestedPage =webCrawler.getPageForUrl(url);

    }


}
