package com.aestheticsw.jobkeywords.service.termextractor;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.aestheticsw.jobkeywords.domain.termfrequency.TermFrequency;
import com.aestheticsw.jobkeywords.domain.termfrequency.TermList;

@Component
public class FiveFiltersService {

    @Log
    private Logger log;

    @Autowired
    private RestTemplate restTemplate;

    Map<Pattern, String> regExMap = initRegExMap();

    // english blacklist
    private String blacklistRegEx = "experience|work|team|software engineer|services|systems|data|design|ability|candidate|knowledge|customers|applications|software|computer science|products|building|technologies|qualifications|projects|requirements|position|support|solutions|ceo|expertise|cloud|employment|world|platform|company|understanding|skills|software development experience|software development projects|software development|environment|infrastructure|opportunity|applicants|people|engineers|part|today|capital|practices|architecture|role|leadership|field|years|manner|service|thousands|generation|teams|code|research|candidates|responsibilities|leader|models|day|developers|billions|color|problems|approach|industry|machine|tools|features|religion|race|risk|implementation|quality|ssg|technology|analysis|software community|customer relationship management|bain capital ventures|subject matter experts|environment";

    // french blacklist - but FiveFilters doesn't do well with French. 
    // blacklistRegEx += "|sein|conception|missions|maitrise|poste|charge|production|afin|environnement|avez|equipe|realisation|mise|logiciels|groupe|informatique|gestion|conseil|rediger|nos clients|ajoutee|formation|votre mission|signalisation ferroviaire|des connaissances|accompagnons nos clients|cadre du developpement|'industrie|developpement logiciel|l offre yourcegid|des expertises metier|fonde son savoir-faire|performance des entreprises|integrer l 'equipe|venez relever ce|des conditions egales|votre mission consiste|groupe cegid compte|sommes partenaires des|assistance technique bureau|premier editeur francais|renforcons nos equipes|competences techniques plurielles|analyses fonctionnelles organiques|standards du client|un|une|le|la|les|l";

    private Pattern blacklistPattern;

    public TermList getTermList(String content, Locale locale) {

        content = replaceHtmlTagsWithTerminators(content, locale);

        content = removeBlacklistTerms(content);

        String[][] stringArray = executeFiveFiltersPost(content);
        if (stringArray == null) {
            if (content.length() < 10000) {
                testContentForInvalidCharacters(content);
            }
            throw new RuntimeException("Found invalid content, string-length=" + content.length());
        }
        List<TermFrequency> terms = new ArrayList<TermFrequency>();
        for (String[] innerArray : stringArray) {
            terms.add(new TermFrequency(innerArray));
        }
        TermList termList = new TermList(terms);
        TermList sortedTermList = termList.sort(new TermFrequency.FrequencyComparator());
        log.debug("\n  RegEx expression for NEW terms: " + sortedTermList.createRegExpForNewTerms(blacklistRegEx));
        return sortedTermList;
    }

    private String[][] executeFiveFiltersPost(String content) {
        String query = "http://termextract.fivefilters.org/extract.php";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("text", content);
        params.add("output", "json");
        params.add("max", "300");

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params,
                requestHeaders);
        ResponseEntity<String[][]> stringArrayEntty = restTemplate.exchange(query,
                HttpMethod.POST,
                requestEntity,
                String[][].class);
        String[][] stringArray = stringArrayEntty.getBody();

        return stringArray;
    }

    private String removeBlacklistTerms(String content) {
        if (blacklistPattern == null) {
            return content;
        }
        content = blacklistPattern.matcher(content).replaceAll(" ");
        return content;
    }

    private String replaceHtmlTagsWithTerminators(String content, Locale locale) {

        content = content.toLowerCase(locale);
        // remote accented characters
        content = Normalizer.normalize(content, Normalizer.Form.NFD);

        for (Pattern key : regExMap.keySet()) {
            content = key.matcher(content).replaceAll(regExMap.get(key));
        }
        return content;
    }

    // TODO Is the RegExp Pattern class multi threaded ?
    @PostConstruct
    private Map<Pattern, String> initRegExMap() {
        if (StringUtils.hasLength(blacklistRegEx)) {
            blacklistPattern = Pattern.compile("\\W(" + blacklistRegEx + ")\\W");
        }

        Map<Pattern, String> regExMap = new HashMap<>();

        regExMap.put(Pattern.compile("<font[^>]*>"), "");
        regExMap.put(Pattern.compile("</font>"), "");
        regExMap.put(Pattern.compile("<span[^>]*>"), "");
        regExMap.put(Pattern.compile("</span>"), "");
        regExMap.put(Pattern.compile("<ul[^>]*>"), " ");
        regExMap.put(Pattern.compile("</ul>"), "");
        regExMap.put(Pattern.compile("<li[^>]*>"), " ");
        regExMap.put(Pattern.compile("</li>"), ". ");
        regExMap.put(Pattern.compile("<div[^>]*>"), " ");
        regExMap.put(Pattern.compile("</div>"), ". ");
        regExMap.put(Pattern.compile("<p>"), ". ");
        regExMap.put(Pattern.compile("<p [^>]*>"), ". ");
        regExMap.put(Pattern.compile("</p>"), ". ");
        regExMap.put(Pattern.compile("<br>"), " ");
        regExMap.put(Pattern.compile("<br [^>]*>"), " ");
        regExMap.put(Pattern.compile("<br/>"), " ");
        regExMap.put(Pattern.compile("<b>"), "");
        regExMap.put(Pattern.compile("<b [^>]*>"), "");
        regExMap.put(Pattern.compile("</b>"), "");
        regExMap.put(Pattern.compile("<em>"), "");
        regExMap.put(Pattern.compile("<em [^>]*>"), "");
        regExMap.put(Pattern.compile("</em>"), "");

        regExMap.put(Pattern.compile("&[^;]*;"), " ");
        regExMap.put(Pattern.compile("- ·"), " ");
        regExMap.put(Pattern.compile("·"), " ");
        regExMap.put(Pattern.compile("®"), "");
        regExMap.put(Pattern.compile("«"), "");
        regExMap.put(Pattern.compile("»"), "");

        // remove accent characters that were stripped by Normalize.normalize()
        regExMap.put(Pattern.compile("\\p{M}"), "");

        // Non-printable characters don't appear to be a problem.
        // regExMap.put(Pattern.compile("[\\x00\\x08\\x0B\\x0C\\x0E-\\x1F]"), "");

        return regExMap;
    }

    /**
     * test the content for invalid characters
     */
    @SuppressWarnings("unused")
    private void testContentForInvalidCharacters(String content) {
        int subSize = 200;
        for (int len = 0; len < (content.length() - subSize); len += subSize) {
            String subContent = content.substring(len, len + subSize);

            // delay so this doesn't smell like a DoS attack.
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }

            String[][] stringArray = executeFiveFiltersPost(subContent);
            if (stringArray == null) {
                throw new RuntimeException("Found invalid content @ index=" + len + ", text=" + subContent);
            }
        }
    }

}