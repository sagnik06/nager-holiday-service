package com.example.nager.config;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import java.time.Duration;
@Configuration
public class WebClientConfig {
    @Value("${nager.base-url:https://date.nager.at/api/v3}")
    private String baseUrl;
    @Value("${app.webclient.connect-timeout-ms:10000}")
    private int connectTimeoutMs;
    @Value("${app.webclient.response-timeout-ms:20000}")
    private int responseTimeoutMs;
    @Value("${app.webclient.read-timeout-ms:20000}")
    private int readTimeoutMs;
    @Value("${app.webclient.write-timeout-ms:20000}")
    private int writeTimeoutMs;

    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMs)
            .responseTimeout(Duration.ofMillis(responseTimeoutMs))
            .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(readTimeoutMs/1000))
                                       .addHandlerLast(new WriteTimeoutHandler(writeTimeoutMs/1000)));
        ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(cfg -> cfg.defaultCodecs().maxInMemorySize(512 * 1024))
            .build();
        return WebClient.builder()
            .baseUrl(baseUrl)
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .exchangeStrategies(strategies)
            .build();
    }
}
