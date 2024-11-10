package com.javathehutt;

import java.util.HashMap;
import java.util.Map;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class LineChartModule extends StackPane {

  private final LineChart baseChart;
  private final ObservableList<LineChart> backgroundCharts = FXCollections.observableArrayList();
  private final Map<LineChart, Color> chartColorMap = new HashMap<>();

  private final double yAxisWidth = 60;
  private final double yAxisSeparation = 20;

  public LineChartModule(LineChart baseChart, Color lineColor) {
    this.baseChart = baseChart;

    chartColorMap.put(baseChart, lineColor);

    styleBaseChart(baseChart);
    baseChart
        .getData()
        .forEach(series -> styleChartLine((XYChart.Series<Number, Number>) series, "#00eb52"));
    baseChart
        .getYAxis()
        .lookup(".axis-label")
        .setStyle("-fx-text-fill: #00eb52; -fx-font-weight: bold;");
    setFixedAxisWidth(baseChart);
    setAlignment(Pos.CENTER_LEFT);

    backgroundCharts.addListener((Observable observable) -> rebuildChart());
    rebuildChart();
  }

  private void styleBaseChart(LineChart baseChart) {
    baseChart.setLegendVisible(false);
    baseChart.getXAxis().setAutoRanging(false);
    baseChart.getXAxis().setAnimated(false);
    baseChart.getYAxis().setAnimated(false);
  }

  private void setFixedAxisWidth(LineChart chart) {
    chart.getYAxis().setPrefWidth(yAxisWidth);
    chart.getYAxis().setMaxWidth(yAxisWidth);
  }

  private void rebuildChart() {
    getChildren().clear();

    getChildren().add(resizeBaseChart(baseChart));
    for (LineChart lineChart : backgroundCharts) {
      getChildren().add(resizeBackgroundChart(lineChart));
    }
  }

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

  private Node resizeBackgroundChart(LineChart lineChart) {
    HBox hBox = new HBox(lineChart);
    hBox.setAlignment(Pos.CENTER_LEFT);
    hBox.prefHeightProperty().bind(heightProperty());
    hBox.prefWidthProperty().bind(widthProperty());
    hBox.setMouseTransparent(true);

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

  public void addSeries(XYChart.Series series, Color lineColor) {
    NumberAxis yAxis = new NumberAxis();
    NumberAxis xAxis = new NumberAxis();

    // style x-axis
    xAxis.setAutoRanging(false);
    xAxis.setVisible(false);
    xAxis.setOpacity(0.0); // somehow the upper setVisible does not work
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

    styleBackgroundChart(lineChart);
    styleChartLine((XYChart.Series<Number, Number>) lineChart.getData().get(0), "#ff7f50");

    lineChart
        .getYAxis()
        .lookup(".axis-label")
        .setStyle("-fx-text-fill: #ff7f50; -fx-font-weight: bold;");
    setFixedAxisWidth(lineChart);

    chartColorMap.put(lineChart, lineColor);
    backgroundCharts.add(lineChart);
  }

  private void styleBackgroundChart(LineChart lineChart) {
    Node contentBackground = lineChart.lookup(".chart-content").lookup(".chart-plot-background");
    contentBackground.setStyle("-fx-background-color: transparent;");

    lineChart.setVerticalZeroLineVisible(false);
    lineChart.setHorizontalZeroLineVisible(false);
    lineChart.setVerticalGridLinesVisible(false);
    lineChart.setHorizontalGridLinesVisible(false);
  }

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
}
