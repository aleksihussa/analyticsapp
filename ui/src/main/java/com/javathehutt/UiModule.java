package com.javathehutt;

import com.javathehutt.apis.ApiService;
import com.javathehutt.converters.AgrEmplByCountryConverter;
import com.javathehutt.converters.GDPConverter;
import com.javathehutt.dto.AgrEmplByCountryDto;
import com.javathehutt.dto.GDPDto;
import com.javathehutt.dto.helpers.Country;
import com.javathehutt.helpers.ApiData;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.json.JSONArray;
import org.json.JSONObject;

public class UiModule extends Application {

  private LineChart<String, Number> lineChart;
  private XYChart.Series<String, Number> tractorsSeries;
  private XYChart.Series<String, Number> gdpSeries;
  private ComboBox<Integer> startYearDropdown;
  private ComboBox<Integer> endYearDropdown;

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Analyticsapp");

    CategoryAxis xAxis = new CategoryAxis();
    xAxis.setLabel("Year");

    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel("Value");

    // LineChart for tractors and GDP
    lineChart = new LineChart<>(xAxis, yAxis);
    lineChart.setTitle("Number of Tractors and GDP");
    lineChart.setAnimated(false);

    tractorsSeries = new XYChart.Series<>();
    tractorsSeries.setName("Tractors");

    gdpSeries = new XYChart.Series<>();
    gdpSeries.setName("GDP");

    List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();
    seriesList.add(gdpSeries);
    seriesList.add(tractorsSeries);
    lineChart.getData().addAll(seriesList);

    // Dropdown menu for countries
    ComboBox<Country> countryDropdown = new ComboBox<>();
    Service service = new Service();
    List<Country> countries = service.getCountries();
    if (countries != null) {
      countryDropdown.getItems().addAll(countries);

      Country finland =
          countries.stream()
              .filter(country -> "Finland".equals(country.getValue()))
              .findFirst()
              .orElse(null);
      // Set Finland as the default value
      if (finland != null) {
        countryDropdown.setValue(finland);
        updateView(finland.getId(), 1990, 2024);
      }
    } else {
      System.err.println("Failed to load countries.");
    }

    // Set custom cell factory to display only the value of the Country
    countryDropdown.setCellFactory(
        new Callback<ListView<Country>, ListCell<Country>>() {
          @Override
          public ListCell<Country> call(ListView<Country> param) {
            return new ListCell<Country>() {
              @Override
              protected void updateItem(Country item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                  setText(item.getValue());
                } else {
                  setText(null);
                }
              }
            };
          }
        });

    // Set button cell to display the selected value
    countryDropdown.setButtonCell(
        new ListCell<Country>() {
          @Override
          protected void updateItem(Country item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) {
              setText(item.getValue());
            } else {
              setText(null);
            }
          }
        });

    // ComboBoxes for start year and end year
    startYearDropdown = new ComboBox<>();
    endYearDropdown = new ComboBox<>();
    IntStream.rangeClosed(1990, 2024)
        .forEach(
            year -> {
              startYearDropdown.getItems().add(year);
              endYearDropdown.getItems().add(year);
            });
    startYearDropdown.setValue(1990);
    endYearDropdown.setValue(2024);

    // Add listeners to update the view when the year dropdowns change
    startYearDropdown.setOnAction(event -> updateViewIfValid(countryDropdown));
    endYearDropdown.setOnAction(event -> updateViewIfValid(countryDropdown));
    countryDropdown.setOnAction(event -> updateViewIfValid(countryDropdown));

    // Create the root layout and scene
    Label dashLabel = new Label("-");
    HBox yearInputBox = new HBox(10, startYearDropdown, dashLabel, endYearDropdown);
    HBox inputBox = new HBox(10, countryDropdown, yearInputBox);
    VBox vbox = new VBox(inputBox, lineChart);
    Scene scene = new Scene(vbox, 800, 600);

    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private void updateViewIfValid(ComboBox<Country> countryDropdown) {
    Country selectedCountry = countryDropdown.getValue();
    if (selectedCountry != null) {
      int startYear = startYearDropdown.getValue();
      int endYear = endYearDropdown.getValue();
      if (startYear <= endYear) {
        updateView(selectedCountry.getId(), startYear, endYear);
      } else {
        System.err.println("Invalid year range.");
      }
    }
  }

  private void updateView(String countryIsoCode, int startYear, int endYear) {
    Random random = new Random();
    tractorsSeries.getData().clear();
    gdpSeries.getData().clear();

    // Fast implementation, move if needed in multiple places
    ApiServiceFactory apiFactory = new ApiServiceFactory();
    ApiService gdpService = apiFactory.createService("gdp");
    ApiService agricultureService = apiFactory.createService("agriculture");

    // Fetch agriculture data for the selected country
    ApiData agricultureApiData = agricultureService.fetchData(countryIsoCode, startYear, endYear);
    JSONArray agricultureJSON = agricultureApiData.getJsonArray();
    AgrEmplByCountry agricultureData = new AgrEmplByCountryConverter().doForward(agricultureJSON);
    agricultureData.getValues().sort(Comparator.comparing(AgrEmplByCountryDto::getYear));

    System.out.println(agricultureData);

    // Fetch GDP data for the selected country
    ApiData gdpApiData = gdpService.fetchData(countryIsoCode, startYear, endYear);
    JSONObject gdpJSON = gdpApiData.getJsonObject();
    GDP gdpData = new GDPConverter().doForward(gdpJSON);
    gdpData.getValues().sort(Comparator.comparing(GDPDto::getYear));

    System.out.println(gdpData);

    // Add agriculture data to tractorsSeries
    if (agricultureData != null && !agricultureData.getValues().isEmpty()) {
      for (AgrEmplByCountryDto agrDto : agricultureData.getValues()) {
        int year = agrDto.getYear();
        if (year >= startYear && year <= endYear) {
          Double value = agrDto.getValue();
          if (value != null) {
            tractorsSeries.getData().add(new XYChart.Data<>(String.valueOf(year), value));
          }
        }
      }
    } else {
      System.err.println("Failed to load agriculture data for country: " + countryIsoCode);
    }

    if (gdpData != null && !gdpData.getValues().isEmpty()) {
      for (GDPDto gdpDto : gdpData.getValues()) {
        int year = gdpDto.getYear();
        if (year >= startYear && year <= endYear) {
          Double value = gdpDto.getValue();
          if (value != null) {
            gdpSeries.getData().add(new XYChart.Data<>(String.valueOf(year), value));
          }
        }
      }
    } else {
      System.err.println("Failed to load GDP data for country: " + countryIsoCode);
    }
  }

  public static void run(String[] args) {
    launch(args);
  }
}
