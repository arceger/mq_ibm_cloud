package com.over.web5;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;


public class RestTemplateFilter implements ClientHttpRequestInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateFilter.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {

        LOGGER.info("RestTemplate does a http/s to - {} with HTTP Method : {}", request.getURI(), request.getMethod());

        ClientHttpResponse response = execution.execute(request, body);

        if (response.getStatusCode().is4xxClientError() || response.getStatusCode()
                .is5xxServerError()) {
            LOGGER.error("RestTemplate received a bad response from : {} - with response status : {} and body : {} ",
                    request.getURI(), response.getStatusCode(), new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8));
        } else {
            LOGGER.info("RestTemplate received a good response from : {}- with response status {}",
                    request.getURI(),
                    response.getStatusCode());
        }

        return response;
    }
}