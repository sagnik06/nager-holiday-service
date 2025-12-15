package com.example.nager.infra;
import com.example.nager.NagerHolidaysApplication;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = NagerHolidaysApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WireMockIT {
    static WireMockServer wm;
    @Autowired MockMvc mockMvc;

    @BeforeAll
    static void startWireMock(){
        wm = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wm.start();
        WireMock.configureFor("localhost", wm.port());
    }

    @AfterAll
    static void stopWireMock(){
        wm.stop();
    }

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry){
        registry.add("nager.base-url", () -> "http://localhost:" + wm.port());
    }


    @Test
    void lastThree_usesWireMock() throws Exception {
        // Stubs for current & previous year
        wm.stubFor(get(urlEqualTo("/PublicHolidays/2025/GB"))
                .willReturn(okJson("[{\"date\":\"2025-01-01\",\"name\":\"New Year's Day\",\"localName\":\"New Year's Day\"}]")));

        wm.stubFor(get(urlEqualTo("/PublicHolidays/2024/GB"))
                .willReturn(okJson("[{\"date\":\"2024-12-25\",\"name\":\"Christmas Day\",\"localName\":\"Christmas Day\"}]")));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/holidays/last-3/GB"))
                .andExpect(status().isOk());
    }

}
