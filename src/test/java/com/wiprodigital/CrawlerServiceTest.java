package com.wiprodigital;

import com.wiprodigital.crawler.CrawlerApplication;
import com.wiprodigital.crawler.CrawlerConfiguration;
import com.wiprodigital.crawler.service.CrawlerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;

/**
 * Created by abuayyub on 13/01/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest( classes = {CrawlerConfiguration.class,CrawlerApplication.class})
@WebAppConfiguration
public class CrawlerServiceTest {

    @Autowired
    CrawlerService crawlerService;

    @Test
    public void produceSiteMapForUrl() {
        String url = "http://wiprodigital.com";
        Map<String,List<String>> result = null;
        result = crawlerService.generateSitePages(url);

        assertThat(result.keySet().size()
                ,is(greaterThan(0)));
        System.out.println(result);
    }
}
