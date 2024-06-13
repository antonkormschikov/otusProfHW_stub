package services;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.Json;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.jetty.util.IO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

public class SpringService_Test {
String json="[{\n" +
        "\"name\":\"QA java\",\n" +
        "\"price\": 15000\n" +
        "},\n" +
        "{\n" +
        "\"name\":\"Java\",\n" +
        "\"price\": 12000\n" +
        "}]";
    private static final WireMockServer wireMockServer = new WireMockServer();

    @BeforeEach
    public void startWireMock(){
        wireMockServer.start();
        configureFor(wireMockServer.port());
    }
    @AfterEach
    public void stopWireMock(){
        wireMockServer.stop();
    }

    @Test
    public void test1() throws IOException {
       stubFor(get(urlEqualTo("/cource/get/all"))
               .willReturn(aResponse()
               .withBody(json)
               .withStatus(200)));
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet(String.format("%s/cource/get/all","http://localhost:8080"));
        HttpResponse httpResponse = httpClient.execute(request);
        String responseString =  convertResponseToString(httpResponse);

        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(200);
        assertThat(responseString).isEqualTo(json);
    }

    private String convertResponseToString (HttpResponse response) throws IOException{
        InputStream responseStream = response.getEntity().getContent();
        Scanner scanner = new Scanner(responseStream,"UTF-8");
        String responseString = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return responseString;

    }



}
