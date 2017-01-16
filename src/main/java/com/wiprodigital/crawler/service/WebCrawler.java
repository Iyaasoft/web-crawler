package com.wiprodigital.crawler.service;

import com.wiprodigital.crawler.exception.CrawlerPageNotFoundException;
import com.wiprodigital.crawler.parser.CrawlerPageParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

/**
 * Created by abuayyub on 11/01/2017.
 */
@Component
public class WebCrawler {

    @Autowired
    CrawlerPageParser pageParser;

    public String getPageForUrl(String url) throws IOException, CrawlerPageNotFoundException {
        String page =null;

        try {
           page = getPage(url);
        }  catch (Exception e) {
           return "Some exception occurred";
        }
        if (page.contains("not found")) {
            return url +" "+"Page not found or invalid url";
            //throw new CrawlerPageNotFoundException();
        }
        return page;
    }


        private String getPage(String url) throws IOException {

            StringBuilder fetchedPage = new StringBuilder();
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet get = new HttpGet(url);
            System.out.print(url);

            Optional<HttpResponse> response=Optional.empty();
            try {
                response = Optional.of(client.execute(get));
            } catch(ClientProtocolException e) {
                System.out.println("Error : "+url + " "+e.getCause().getMessage());
            }
            Optional<HttpEntity> entity = Optional.empty();
            if(response.isPresent()) {
                entity = Optional.ofNullable(response.get().getEntity());
            }

            if (entity.isPresent()) {
                Scanner scanner = new Scanner(entity.get().getContent());
                while (scanner.hasNextLine()) {
                    fetchedPage.append(scanner.nextLine());
                }
                return fetchedPage.toString();
            }
            return "not found";

        }


    }