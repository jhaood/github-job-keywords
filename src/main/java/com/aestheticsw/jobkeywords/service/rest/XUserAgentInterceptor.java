package com.aestheticsw.jobkeywords.service.rest;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * This interceptor can overwrite the headers for an outbound HttpClient request. <p/>
 * 
 * For example,
 * the user-agent may have to be spoofed to look like a browser instead of HttpClient. This might
 * be needed later if Indeed or FiveFilters decides they don't want to serve requests from apps.
 */
public class XUserAgentInterceptor implements ClientHttpRequestInterceptor {

    /**
     * This isn't needed now - but was needed when trying to figure out why term extractor wasn't
     * returning data.
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        // HttpHeaders headers = request.getHeaders();
        // headers.add("Accept",
        // "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        /*
         * headers.add("X-User-Agent",
         * "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 Query String Parametersview sourceview URL encoded"
         * );
         * headers.add("Accept-Encoding", "gzip, deflate, sdch");
         * headers.add("Accept-Language", "en-US,en;q=0.8");
         * headers.add("Cache-Control", "max-age=0");
         * headers.add("Connection", "keep-alive");
         * headers.add("Cookie",
         * "CTK=19asr9k230nph1f0; RF=\"TFTzyBUJoNr4wP5QpciSOn6ifEMTEVq4ARC0hGY5P-gkhvCY-D1UltWIqfxDhxhHqiN1UggLuPE=\"; IRF=\"1qRi-3v0F_uf-yOkOwHemehIPriDHeZ-AD_rnIAayJ8=\"; CSRF=RCTCj8Abxq9j3u0bL2cYybndr26rFwzL; LC=\"co=FR&hl=fr_FR\"; SHOE=\"uQsQNsQkFYUJ008ISZ8DRWVFtv6W_JMN4mdL-LLfXiJuOW0VoJAjjiVpkv4kQoV23Sg-_-1ytM1OqAz8ROZROJDcOAoDohJHdMU_EBqxbwMOyVIOjrryq7DH189GzMI=\"; PUB=1; BIGipServerjob_iad=!YlkJJm1KgDuhYWHnj+SL47ecq6aoxVInDdjtEnHOjpnVpzOxZBWTrFttjRp0eryuGzkmkx1TYdWHS2k=; INDEED_CSRF_TOKEN=ClPCTHOQ2zlw3egLg2bQl8WakmtEeWpf; _mkto_trk=id:699-SXJ-715&token:_mch-indeed.com-1420554563497-42566; TS01c598d3=0160a2beff09f16a3c10d0f0f0a6a553318c160463ca6c315ab349cad0aefb8dfaa95db17be5052c844f60ecad51ec7c72d952d440688e76cfdabdb3074ee99a7773918ac93d8b06d43de5f602f27582982fb39fbe34263631f197d1f19e0fb70752db8b562fd68ecd5716b3e3d26da06ac246cd6f060c5f347cb466bd53402f81a2506ff7ee638943466c4de4e47bb62d5ee23bd42ae6ba9a59c0ca38e446b40736cbc688; DCT=4; JSESSIONID=97208F039EED60B31A14EF12640B8D84.jasxB_iad-job18; TS016080f8=0160a2beff8af91efc651c51dff589379d71c19e188469c827f0265638b0194c2885f98c9fe455fc17b6ea59258cb631c44485df3e271eae4c2090e2cedce12f163b39094c07e6f01fc7144638866b7333691120a4c4648f7b3b8f00c08419b665958f61ce"
         * );
         * headers.add("DNT", "1");
         * headers.add("Host", "api.indeed.com");
         */
        return execution.execute(request, body);
    }
}