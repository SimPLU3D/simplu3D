package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.visitor;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.rjmcmc.configuration.Configuration;
import fr.ign.rjmcmc.kernel.SimpleObject;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.temperature.Temperature;
import fr.ign.simulatedannealing.visitor.Visitor;

public class StatsV⁮isitor<O extends SimpleObject> implements Visitor<O> {

  private int dump;
  private int iter;
  ApplicationFrame aF = null;
  final XYSeries series;

  final XYSeries seriesUnary;
  final XYSeries seriesBinary;

  final XYSeries seriesBest;

  private double bestEnergy = Double.POSITIVE_INFINITY;

  public StatsV⁮isitor(String title) {

    aF = new ApplicationFrame(title);

    this.series = new XYSeries("U Total");
    this.seriesUnary = new XYSeries("U Unaire");
    this.seriesBinary = new XYSeries("U Binaire");
    this.seriesBest = new XYSeries("Meilleur candidat");
    final XYSeriesCollection dataset = new XYSeriesCollection(this.series);
    dataset.addSeries(seriesUnary);
    dataset.addSeries(seriesBinary);
    dataset.addSeries(seriesBest);

    final JFreeChart chart = createChart(dataset);

    final ChartPanel chartPanel = new ChartPanel(chart);

    final JPanel content = new JPanel(new BorderLayout());
    content.add(chartPanel);
    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
    aF.setContentPane(content);
    aF.pack();
    aF.setVisible(true);
  }

  /**
   * Creates a sample chart.
   * @param dataset
   *        the dataset.
   * @return A sample chart.
   */
  private JFreeChart createChart(final XYDataset dataset) {
    final JFreeChart result = ChartFactory.createXYLineChart("Évolution de l'énergie", "Itération",
        "Énergie", dataset, PlotOrientation.VERTICAL, true, true, true);

    result.setBorderPaint(Color.white);

    result.setBackgroundPaint(Color.white);

    final XYPlot plot = result.getXYPlot();
    ValueAxis axis = plot.getDomainAxis();
    axis.setAutoRange(true);
    // axis.setFixedAutoRange(60000.0); // 60 seconds
    axis = plot.getRangeAxis();

    plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
    plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
    plot.setBackgroundPaint(Color.white);

    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
    renderer.setSeriesPaint(0, new Color(255, 0, 0));
    renderer.setSeriesPaint(1, new Color(2, 157, 116));
    renderer.setSeriesPaint(2, new Color(112, 147, 219));
    renderer.setSeriesPaint(3, new Color(140, 23, 23));

    // axis.setRange(0.0, 200.0);
    return result;
  }

  @Override
  public void init(int dump, int save) {
    this.iter = 0;
    this.dump = dump;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void visit(Configuration<O> config, Sampler<O> sampler, Temperature t) {
    ++iter;

    this.bestEnergy = Math.min(config.getEnergy(), bestEnergy);

    if (iter % dump == 0) {
      this.addInformationToMainWindow((GraphConfiguration<Cuboid>) config);
    }

  }

  private void addInformationToMainWindow(GraphConfiguration<Cuboid> config) {
    // TODO Auto-generated method stub

    series.add(iter, config.getEnergy());
    seriesUnary.add(iter, config.getUnaryEnergy());
    seriesBinary.add(iter, config.getBinaryEnergy());

    seriesBest.add(iter, this.bestEnergy);

  }

  @Override
  public void begin(Configuration<O> config, Sampler<O> sampler, Temperature t) {
  }

  @Override
  public void end(Configuration<O> config, Sampler<O> sampler, Temperature t) {

  }

}
