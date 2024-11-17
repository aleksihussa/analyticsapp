package com.javathehutt.apis;

import com.javathehutt.helpers.ApiData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class GDPServiceTest {
    static GDPService gdpService;
    static int startYear;
    static int endYear;

    @BeforeAll
    static void init() {
        gdpService = new GDPService();
        // startyear and endyear do not affect the years fetched
        startYear = 2000;
        endYear = 2004;
    }

    @Test
    void fetchDataTestNotNull() {
        ApiData dataUSA = gdpService.fetchData("USA", startYear, endYear);
        assertNotNull(dataUSA);
    }

    @Test
    void fetchDataTestKeysShouldBeStrings() {

        ApiData dataFI = gdpService.fetchData("FIN", startYear, endYear);

        assertEquals(BigDecimal.class,
            dataFI.getJsonObject().get(Integer.toString(startYear)).getClass());

        Iterator<String> keys = dataFI.getJsonObject().keys();
        String key;

        while(keys.hasNext()) {
            key = keys.next();
            assertEquals(String.class, key.getClass());
        }
    }

    @Test
    void fetchDataTestValuesShouldBeBigDecimals() {
        ApiData dataSWE = gdpService.fetchData("SWE", startYear, endYear);

        JSONObject dataObjSWE = dataSWE.getJsonObject();
        Iterator<String> keys = dataObjSWE.keys();
        String key;

        while(keys.hasNext()) {
            key = keys.next();
            Object value = dataObjSWE.get(key);
            assertEquals(BigDecimal.class, value.getClass());
        }
    }

    @Test
    void fetchDataTestWrongIso3CodeShouldThrow() {
        String invalidIso3Code = "iamnotarealcountry";
        assertThrows(JSONException.class,
            () -> gdpService.fetchData(invalidIso3Code, startYear, endYear));
    }
}
