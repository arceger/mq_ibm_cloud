package com.over.web5.dois;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RestClientWithBasicAuth {

    public static void main(String[] args) {
        String username = "usertest";
        String password = "";

        String message = "Mensagem de teste";

        try {
            // Codificar credenciais para autenticação básica
            String credentials = username + ":" + password;
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

            // Configurar requisição HTTP POST com autenticação básica
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://web-ger-1-486b.qm.us-south.mq.appdomain.cloud/ibmmq/rest/v1/messaging/qmgr/GER.1/queue/TOCLOUD.TEST/message"))
                    .header("Authorization", "Basic " + encodedCredentials)
                    .header("Content-Type", "text/plain")
                    .POST(HttpRequest.BodyPublishers.ofString(message))
                    .build();

            // Enviar a requisição
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Exibir resposta
            int statusCode = response.statusCode();
            String responseBody = response.body();
            System.out.println("Status code: " + statusCode);
            System.out.println("Response body: " + responseBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}