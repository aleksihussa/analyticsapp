package com.javathehutt;

import com.javathehutt.dto.helpers.Country;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UiModule extends Application {

  private LineChart<String, Number> lineChart;
  private XYChart.Series<String, Number> tractorsSeries;
  private XYChart.Series<String, Number> gdpSeries;

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
    ComboBox<String> countryDropdown = new ComboBox<>();
    Service service = new Service();
    List<Country> countries = service.getCountries();
    if (countries != null) {
      for (Country country : countries) {
        countryDropdown.getItems().add(country.getValue());
      }
    } else {
      System.err.println("Failed to load countries.");
    }
    countryDropdown.setOnAction(event -> updateView(countryDropdown.getValue()));

    // Create the root layout and scene
    VBox vbox = new VBox(countryDropdown, lineChart);
    Scene scene = new Scene(vbox, 800, 600);

    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private void updateView(String country) {
    Random random = new Random();
    tractorsSeries.getData().clear();
    gdpSeries.getData().clear();

    // Generate random data
    for (int year = 2010; year <= 2020; year++) {
      int tractors = random.nextInt(3000) + 1000;
      int gdp = random.nextInt(50000) + 20000;

      tractorsSeries.getData().add(new XYChart.Data<>(String.valueOf(year), tractors));
      gdpSeries.getData().add(new XYChart.Data<>(String.valueOf(year), gdp));
    }
  }

  public static void run(String[] args) {
    launch(args);
  }
}
