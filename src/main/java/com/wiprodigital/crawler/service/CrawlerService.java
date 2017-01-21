package com.wiprodigital.crawler.service;

import com.wiprodigital.crawler.parser.CrawlerPageParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Supplier;

/**
 * Created by abuayyub on 11/01/2017.
 */
@Service
public class CrawlerService {

    @Autowired
    WebCrawler webCrawler;

    @Autowired
    CrawlerPageParser crawlerPageParser;


    public Map<String,List<String>> generateSitePages(String url) {

        Map<String,List<String>> siteLinkToPages = new HashMap();
        List<String> anchorsToParse = getPage(url);

        searchPageForUrls(url, siteLinkToPages, anchorsToParse);

        return siteLinkToPages;
    }



    private void searchPageForUrls(String url, Map<String, List<String>> siteLinkToPages, List<String> anchorsToParse) {


        anchorsToParse.parallelStream().forEach(line -> {
             List<String>hrefs = crawlerPageParser.parseStringAndExractDomainHref(line,getDomain(url));

             hrefs.stream().forEach( href -> {
             if(!url.equals(href)){
                siteLinkToPages.put(href, getChildLinks(href, url));
             }
        });

        });
    }

    private List<String> searchPageForChildUrls(String url, List<String> anchorsToParse) {

        List<String> childUrls = new ArrayList();
        anchorsToParse.stream().forEach(line -> {
            List<String> hrefs = crawlerPageParser.parseStringAndExractDomainHref(line,getDomain(url));
            if(!url.equals(hrefs)){
                childUrls.addAll(hrefs);
            }
        });
        return childUrls;
    }



    private List<String> getChildLinks(final String href, final String domainUrl) {
        List<String> childLinks =  getPage(href);
        List<String> result = new ArrayList();
        CompletableFuture future  = CompletableFuture.supplyAsync(() -> searchPageForChildUrls(domainUrl, childLinks),executor);
        try {
            result = (List<String>)future.get();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<String> getPage(String url) {

        List<String> anchorsToParse = new ArrayList();
        try {
             final String page = webCrawler.getPageForUrl(url);
             getDomain(url);
             Supplier<String[]> anchors = () -> (page.split("<a"));
             anchorsToParse = Arrays.asList(anchors.get());

        } catch(IOException e)  {
            e.printStackTrace();// TODO lOG
        }

        return anchorsToParse;
    }

    private String getDomain(String url) {

        int firstSlash = url.indexOf("//");
        int endSlash = url.indexOf("/",firstSlash);

        return firstSlash > 0 ? url.substring(firstSlash, endSlash) : url;

    }

    private final Executor executor = Executors.newFixedThreadPool(20,new ThreadFactory(){

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return  thread;
        }
    });
}
