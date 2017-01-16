package com.wiprodigital.crawler.parser;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by abuayyub on 13/01/2017.
 */
@Component
public class CrawlerPageParser {

     private static int HREF = 6;
     private static int SRC = 5;

      public List<String> parseStringAndExractDomainHref(String text, String domain) {

          List<String> result = new ArrayList();
          if (getHrefUrls(text, domain, result));
          if(getStaticContentURLs(text,domain,result));
          return result;
      }

    private boolean getStaticContentURLs(String text, String domain, List<String> result) {
        //todo
        return false;
    }

    private boolean getHrefUrls(String text, String domain, List<String> result) {

            Optional<String>  url = Optional.of(parseHtmlTextLine(text,domain,"href"));
            if(url.isPresent()){
                ParseHrefAndSrc parseHrefAndSrc = new ParseHrefAndSrc(url.get()).invoke();
            if (parseHrefAndSrc.is()) result.add(parseHrefAndSrc.getConcatUrlElements());
            String[] splitUrl = parseHrefAndSrc.getSplitUrl();
            result.add(splitUrl[0]);

            return true;
        }
        return false;
    }

    public String parseHtmlTextLine(String text, String domain ,String lookFor) {

        String url = "";
        if (text.contains(domain)
                && text.contains(lookFor +
                "") && !text.contains(lookFor + "=\"\"")) {
            int hrefIndex = text.indexOf(lookFor);
            int endTagIndex = text.indexOf("\">", hrefIndex);
            switch (lookFor) {
                case "href":
                    if (text.length() > HREF && endTagIndex > 0)
                        url = text.substring(hrefIndex + HREF, endTagIndex).replace('"', ' ');
                    break;
                case "src":

            }
            return url;
        }
        return "";
    }

    private class ParseHrefAndSrc {
        private boolean myResult;
        private String url;
        private String[] splitUrl;
        private String concatUrlElements;

        public ParseHrefAndSrc(String url) {
            this.url = url;
        }

        boolean is() {
            return myResult;
        }

        public String[] getSplitUrl() {
            return splitUrl;
        }

        public String getConcatUrlElements() {
            return concatUrlElements;
        }

        public ParseHrefAndSrc invoke() {
            splitUrl = url.split(" ");
            concatUrlElements = "";
            if( splitUrl.length > 1) {
                for(String element : splitUrl) {
                    if(element != null && element.length() > 0)
                    concatUrlElements = concatUrlElements.concat(element);
                    break;

                }
                myResult = true;
                return this;
            }
            myResult = false;
            return this;
        }
    }
}
