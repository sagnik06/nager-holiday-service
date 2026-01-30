package com.example.nager.web;

import com.example.nager.NagerHolidaysApplication;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.junit.jupiter.api.BeforeAll;

import java.time.LocalDate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(classes = NagerHolidaysApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.main.allow-bean-definition-overriding=true",
                "spring.cache.cache-names=lastThree,weekdayCounts,commonDates,publicHolidays"}
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WireMockIT {
    // Start WireMock early so DynamicPropertySource can reference the port reliably
    static WireMockServer wm = new WireMockServer(WireMockConfiguration.options().dynamicPort());

    static {
        wm.start();
        WireMock.configureFor("localhost", wm.port());
    }

    @AfterAll
    static void stopWireMock(){
        if (wm != null && wm.isRunning()) wm.stop();
    }

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry){
        // set base URL to WireMock host:port (the client will append the API path)
        registry.add("nager.base-url", () -> "http://localhost:" + wm.port());
    }

    @LocalServerPort
    private int port;

    private WebTestClient webClient;

    @BeforeAll
    void setupWebClient() {
        this.webClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void lastThree_usesWireMock() {
        int currentYear = LocalDate.now().getYear();
        // Stubs for current & previous year - note paths are relative to base-url (/api/v3)
        wm.stubFor(get(urlEqualTo("/PublicHolidays/" + currentYear + "/GB"))
                .willReturn(okJson("[{\"date\":\"" + currentYear + "-01-01\",\"name\":\"New Year's Day\",\"localName\":\"New Year's Day\"}]")));

        wm.stubFor(get(urlEqualTo("/PublicHolidays/" + (currentYear - 1) + "/GB"))
                .willReturn(okJson("[{\"date\":\"" + (currentYear - 1) + "-12-25\",\"name\":\"Christmas Day\",\"localName\":\"Christmas Day\"}]")));

        webClient.get()
                .uri("/api/holidays/last-3/GB")
                .exchange()
                .expectStatus().isOk();
    }

}
