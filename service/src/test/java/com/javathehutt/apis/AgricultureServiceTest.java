package com.javathehutt.apis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.javathehutt.helpers.ApiData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AgricultureServiceTest {
    static AgricultureService agrService;
    static int startYearOne;
    static int endYearOne;
    static int startYearTwo;
    static int endYearTwo;

    @BeforeAll
    static void init() {
        agrService = new AgricultureService();

        startYearOne = 1995;
        endYearOne = 2005;
        startYearTwo = 2000;
        endYearTwo = 2010;
    }

    @Test
    void fetchDataTestShouldNotBeNull() {
        ApiData dataUSA = agrService
            .fetchData("USA", startYearOne, endYearOne);
        assertNotNull(dataUSA);
    }

    @Test
    void fetchDataTestStartAndEndYear() {
        // check start and end years are correct
        ApiData dataUSA = agrService
            .fetchData("USA", startYearOne, endYearOne);
        String usaEndYear = (String) dataUSA.getJsonArray().getJSONObject(0).get("date");
        String usaStartYear = (String) dataUSA.getJsonArray().getJSONObject(endYearOne-startYearOne).get("date");
        
        assertEquals(Integer.toString(endYearOne), usaEndYear);
        assertEquals(Integer.toString(startYearOne), usaStartYear);

        ApiData dataFIN = agrService
            .fetchData("FIN", startYearTwo, endYearTwo);

        String finEndYear = (String) dataFIN.getJsonArray().getJSONObject(0).get("date");
        String finStartYear = (String) dataFIN.getJsonArray().getJSONObject(endYearTwo-startYearTwo).get("date");
        
        assertEquals(Integer.toString(endYearTwo), finEndYear);
        assertEquals(Integer.toString(startYearTwo), finStartYear);
    }

    @Test
    void fetchDataTestValueShouldBeBigDecimal() {
        // assumes value is not null. might give false positive with different years selected.
        ApiData dataUSA = agrService
            .fetchData("USA", startYearOne, endYearOne);

        JSONArray dataArray = dataUSA.getJsonArray();
        
        for(int i = 0; i<dataArray.length();i++) {
            Object value = dataArray.getJSONObject(i).get("value");
            assertEquals(BigDecimal.class, value.getClass());
        }
    }

    @Test
    void fetchDataTestWrongIso3CodeShouldThrow() {
        String invalidIso3Code = "sillycode";
        assertThrows(JSONException.class, () -> agrService.fetchData(invalidIso3Code, startYearOne, endYearOne));
    }
}
