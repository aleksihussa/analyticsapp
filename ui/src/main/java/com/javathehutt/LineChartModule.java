package com.javathehutt;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LineChartModule extends StackPane {

  private final LineChart baseChart;
  private final ObservableList<LineChart> backgroundCharts = FXCollections.observableArrayList();

  private final String chartTitleText;
  private final String xAxisLabelText;

  private final double yAxisWidth = 60;
  private final double yAxisSeparation = 20;

  // Constructor for LineChartModule
  public LineChartModule(
      LineChart baseChart, String lineColor, String chartTitleText, String xAxisLabelText) {
    this.baseChart = baseChart;
    this.chartTitleText = chartTitleText;
    this.xAxisLabelText = xAxisLabelText;

    // Style base chart
    styleBaseChart(baseChart, lineColor);

    // Add listener to rebuild chart when data changes
    backgroundCharts.addListener((Observable observable) -> rebuildChart());
    rebuildChart();
  }

  // Base chart styling
  private void styleBaseChart(LineChart baseChart, String lineColor) {
    baseChart.setLegendVisible(false);
    baseChart.getXAxis().setAutoRanging(false);
    baseChart.setAnimated(false);
    baseChart.getXAxis().setLabel("");

    styleChartLine((XYChart.Series<Number, Number>) baseChart.getData().get(0), lineColor);

    baseChart
        .getYAxis()
        .lookup(".axis-label")
        .setStyle("-fx-text-fill: " + lineColor + "; -fx-font-weight: bold;");

    setFixedAxisWidth(baseChart);
    setAlignment(Pos.CENTER_LEFT);
  }

  // Set fixed width for y-axis
  private void setFixedAxisWidth(LineChart chart) {
    chart.getYAxis().setPrefWidth(yAxisWidth);
    chart.getYAxis().setMaxWidth(yAxisWidth);
  }

  // Rebuilding returned chart
  private void rebuildChart() {
    getChildren().clear();

    getChildren().add(resizeBaseChart(baseChart));
    for (LineChart lineChart : backgroundCharts) {
      getChildren().add(resizeBackgroundChart(lineChart));
    }
  }

  // Resize base chart
  private Node resizeBaseChart(LineChart lineChart) {
    HBox hBox = new HBox(lineChart);
    hBox.setAlignment(Pos.CENTER_LEFT);
    hBox.prefHeightProperty().bind(heightProperty());
    hBox.prefWidthProperty().bind(widthProperty());

    lineChart
        .minWidthProperty()
        .bind(widthProperty().subtract((yAxisWidth + yAxisSeparation) * backgroundCharts.size()));
    lineChart
        .prefWidthProperty()
        .bind(widthProperty().subtract((yAxisWidth + yAxisSeparation) * backgroundCharts.size()));
    lineChart
        .maxWidthProperty()
        .bind(widthProperty().subtract((yAxisWidth + yAxisSeparation) * backgroundCharts.size()));

    return lineChart;
  }

  // Resize background chart
  private Node resizeBackgroundChart(LineChart lineChart) {
    HBox hBox = new HBox(lineChart);
    hBox.setAlignment(Pos.CENTER_LEFT);
    hBox.prefHeightProperty().bind(heightProperty());
    hBox.prefWidthProperty().bind(widthProperty());

    lineChart
        .minWidthProperty()
        .bind(widthProperty().subtract((yAxisWidth + yAxisSeparation) * backgroundCharts.size()));
    lineChart
        .prefWidthProperty()
        .bind(widthProperty().subtract((yAxisWidth + yAxisSeparation) * backgroundCharts.size()));
    lineChart
        .maxWidthProperty()
        .bind(widthProperty().subtract((yAxisWidth + yAxisSeparation) * backgroundCharts.size()));

    lineChart.translateXProperty().bind(baseChart.getYAxis().widthProperty());
    lineChart
        .getYAxis()
        .setTranslateX((yAxisWidth + yAxisSeparation) * backgroundCharts.indexOf(lineChart));

    return hBox;
  }

  // Add new series to chart
  public void addSeries(XYChart.Series series, String lineColor) {
    NumberAxis yAxis = new NumberAxis();
    NumberAxis xAxis = new NumberAxis();

    // style x-axis
    xAxis.setAutoRanging(false);
    xAxis.setVisible(false);
    xAxis.setOpacity(0.0);
    xAxis.lowerBoundProperty().bind(((NumberAxis) baseChart.getXAxis()).lowerBoundProperty());
    xAxis.upperBoundProperty().bind(((NumberAxis) baseChart.getXAxis()).upperBoundProperty());
    xAxis.tickUnitProperty().bind(((NumberAxis) baseChart.getXAxis()).tickUnitProperty());

    // style y-axis
    yAxis.setSide(Side.RIGHT);
    yAxis.setLabel(series.getName());

    // create chart
    LineChart lineChart = new LineChart(xAxis, yAxis);
    lineChart.setAnimated(false);
    lineChart.setLegendVisible(false);
    lineChart.getData().add(series);

    // Style background chart
    styleBackgroundChart(lineChart, lineColor);
    backgroundCharts.add(lineChart);
  }

  public void removeSeries(XYChart.Series series) {
    LineChart chartToRemove = null;
    for (LineChart chart : backgroundCharts) {
      if (chart.getData().contains(series)) {
        chartToRemove = chart;
        break;
      }
    }

    if (chartToRemove != null) {
      chartToRemove.getData().remove(series);
      backgroundCharts.remove(chartToRemove);
      this.getChildren().remove(chartToRemove);
    }
  }

  // Style background chart
  private void styleBackgroundChart(LineChart lineChart, String lineColor) {
    Node contentBackground = lineChart.lookup(".chart-content").lookup(".chart-plot-background");
    contentBackground.setStyle("-fx-background-color: transparent;");

    lineChart
        .getYAxis()
        .lookup(".axis-label")
        .setStyle("-fx-text-fill: " + lineColor + "; -fx-font-weight: bold;");

    setFixedAxisWidth(lineChart);
    styleChartLine((XYChart.Series<Number, Number>) lineChart.getData().get(0), lineColor);

    lineChart.setVerticalZeroLineVisible(false);
    lineChart.setHorizontalZeroLineVisible(false);
    lineChart.setVerticalGridLinesVisible(false);
    lineChart.setHorizontalGridLinesVisible(false);
  }

  // Style chart line and symbols
  private void styleChartLine(XYChart.Series<Number, Number> series, String colorCode) {
    Node seriesLine = series.getNode().lookup(".chart-series-line");
    if (seriesLine != null) {
      seriesLine.setStyle("-fx-stroke: " + colorCode + "; -fx-stroke-width: 2;");
    }

    // Add a listener to apply styles whenever the data changes
    series
        .getData()
        .addListener(
            (javafx.collections.ListChangeListener.Change<? extends XYChart.Data<Number, Number>>
                    change) -> {
              while (change.next()) {
                if (change.wasAdded()) {
                  for (XYChart.Data<Number, Number> data : change.getAddedSubList()) {
                    Node symbol = data.getNode().lookup(".chart-line-symbol");
                    if (symbol != null) {
                      symbol.setStyle("-fx-background-color: " + colorCode + ", white;");
                    }
                  }
                }
              }
            });
  }

  public VBox getChartModule() {
    Label chartTitle = new Label(this.chartTitleText);
    chartTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    Label xAxisLabel = new Label(this.xAxisLabelText);
    xAxisLabel.setStyle("-fx-text-fill: " + baseChart.getYAxis().lookup(".axis-label").getStyle());

    VBox chartModule = new VBox(5, chartTitle, this, xAxisLabel);
    chartModule.setAlignment(Pos.CENTER);

    return chartModule;
  }
}
