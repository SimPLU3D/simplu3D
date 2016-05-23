package fr.ign.cogit.simplu3d.rjmcmc.generic.visitor;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

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

import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;
import fr.ign.mpp.configuration.AbstractBirthDeathModification;
import fr.ign.mpp.configuration.AbstractGraphConfiguration;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.temperature.Temperature;
import fr.ign.simulatedannealing.visitor.Visitor;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class StatsVisitor<O extends ISimPLU3DPrimitive, C extends AbstractGraphConfiguration<O, C, M>, M extends AbstractBirthDeathModification<O, C, M>>
		implements Visitor<C, M> {

	private int dump;
	private int iter;
	public ApplicationFrame aF = null;
	final XYSeries series;

	final XYSeries seriesUnary;
	final XYSeries seriesBinary;

	final XYSeries seriesBest;

	private double bestEnergy = Double.POSITIVE_INFINITY;

	public static ChartPanel CHARTSINGLETON = null;

	public StatsVisitor(String title) {

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
		chartPanel.setPreferredSize(new java.awt.Dimension(800, (int) (0.8 * 540)));
		aF.setContentPane(content);
		aF.pack();
		aF.setVisible(true);

		CHARTSINGLETON = chartPanel;
	}

	/**
	 * Creates a sample chart.
	 * 
	 * @param dataset
	 *            the dataset.
	 * @return A sample chart.
	 */
	private JFreeChart createChart(final XYDataset dataset) {
		final JFreeChart result = ChartFactory.createXYLineChart("Évolution de l'énergie", "Itération", "Énergie",
				dataset, PlotOrientation.VERTICAL, true, true, true);

		result.setBorderPaint(Color.white);

		result.setBackgroundPaint(Color.white);

		final XYPlot plot = result.getXYPlot();

		Font font = new Font("Verdana", Font.PLAIN, 32);
		Font font2 = new Font("Verdana", Font.PLAIN, 28);

		// axe x
		ValueAxis axis = plot.getDomainAxis();

		axis.setLabelFont(font);
		axis.setTickLabelFont(font2);

		axis.setAutoRange(true);
		// axis.setFixedAutoRange(60000.0); // 60 seconds
		axis = plot.getRangeAxis();

		// axe y
		ValueAxis axis2 = plot.getRangeAxis();

		axis2.setLabelFont(font);
		axis2.setTickLabelFont(font2);

		axis2.setAutoRange(true);
		// axis.setFixedAutoRange(60000.0); // 60 seconds
		axis2 = plot.getRangeAxis();

		plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
		plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
		plot.setBackgroundPaint(Color.white);

		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();

		renderer.setSeriesPaint(0, new Color(255, 0, 0));
		renderer.setSeriesStroke(0, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f));

		renderer.setLegendTextFont(0, font2);

		renderer.setSeriesPaint(1, new Color(2, 157, 116));
		renderer.setSeriesStroke(1, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f));

		renderer.setLegendTextFont(1, font2);

		renderer.setSeriesPaint(2, new Color(112, 147, 219));
		renderer.setSeriesStroke(2, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f));

		renderer.setLegendTextFont(2, font2);

		renderer.setSeriesPaint(3, new Color(140, 23, 23));
		renderer.setSeriesStroke(3, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f,
				new float[] { 6.0f, 6.0f }, 0.0f));

		renderer.setLegendTextFont(3, font2);

		// axis.setRange(0.0, 200.0);
		return result;
	}

	@Override
	public void init(int dump, int save) {
		this.iter = 0;
		this.dump = dump;
	}

	@Override
	public void visit(C config, Sampler<C, M> sampler, Temperature t) {
		++iter;

		this.bestEnergy = Math.min(config.getEnergy(), bestEnergy);

		if (iter % dump == 0) {
			this.addInformationToMainWindow(config);
		}

	}

	private void addInformationToMainWindow(C config) {

		series.add(iter, config.getEnergy());
		seriesUnary.add(iter, config.getUnaryEnergy());
		seriesBinary.add(iter, config.getBinaryEnergy());

		seriesBest.add(iter, this.bestEnergy);

	}

	@Override
	public void begin(C config, Sampler<C, M> sampler, Temperature t) {
	}

	@Override
	public void end(C config, Sampler<C, M> sampler, Temperature t) {
	}

}
