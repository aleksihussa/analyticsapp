package com.javathehutt;

import com.javathehutt.apis.ApiService;
import com.javathehutt.converters.GDPConverter;
import com.javathehutt.dto.GDPDto;
import com.javathehutt.dto.helpers.Country;
import com.javathehutt.helpers.ApiData;
import java.util.ArrayList;
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
    seriesList.add(tractorsSeries);
    seriesList.add(gdpSeries);
    lineChart.getData().addAll(seriesList);

    // Dropdown menu for countries
    ComboBox<Country> countryDropdown = new ComboBox<>();
    Service service = new Service();
    List<Country> countries = service.getCountries();
    if (countries != null) {
      countryDropdown.getItems().addAll(countries);
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
    startYearDropdown.setValue(2010);
    endYearDropdown.setValue(2020);

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
    System.out.println("Updating view for country: " + countryIsoCode);
    Random random = new Random();
    tractorsSeries.getData().clear();
    gdpSeries.getData().clear();

    // Fast implementation, move if needed in multiple places
    ApiServiceFactory apiFactory = new ApiServiceFactory();
    ApiService gdpService = apiFactory.createService("gdp");

    // Generate random data for tractors
    for (int year = startYear; year <= endYear; year++) {
      int tractors = random.nextInt(3000) + 1000;
      tractorsSeries.getData().add(new XYChart.Data<>(String.valueOf(year), tractors));
    }

    // Fetch GDP data for the selected country
    ApiData gdpApiData = gdpService.fetchData(countryIsoCode, startYear, endYear);
    JSONObject gdpJSON = gdpApiData.getJsonObject();
    GDP gdpData = new GDPConverter().doForward(gdpJSON);
    System.out.println(gdpData);

    if (gdpData != null && !gdpData.getValues().isEmpty()) {
      for (GDPDto gdpDto : gdpData.getValues()) {
        int year = gdpDto.getYear();
        if (year >= startYear && year <= endYear) {
          double gdp = gdpDto.getValue();
          gdpSeries.getData().add(new XYChart.Data<>(String.valueOf(year), gdp));
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
