package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.birth;

import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

import fr.ign.cogit.simplu3d.rjmcmc.generic.transform.SimplePolygonTransform;
import fr.ign.random.Random;

public class SimplePolygonTransformTest {
	SimplePolygonTransform t;

	@Before
	public void setUp() throws Exception {
		String polSTR ="POLYGON ((665218.232277 6856149.105205, 665218.238327 6856149.137445, 665220.677545 6856149.434051, 665218.232277 6856149.105205))";
		WKTReader read = new WKTReader();

		t = new SimplePolygonTransform(read.read(polSTR));
	}

	@Test
	public void testApply() {
		
		GeometryFactory gf = new GeometryFactory();
		int nbTest = 100;
		Coordinate[] tabCoord = new Coordinate[nbTest];
		for (int index = 0; index < nbTest; index++) {
			double[] lvalIn = new double[4];
			RandomGenerator generator = Random.random();
			for (int i = 0; i < 2; i++) {
				lvalIn[i] = generator.nextDouble();
			}
			
			
			double[] lvalOut = new double[4];

	      t.apply(true, lvalIn, lvalOut);
	      
	      tabCoord[index] = new Coordinate(lvalOut[0] , lvalOut[1]);
	      
		}
		
		
		MultiPoint mp = gf.createMultiPoint(tabCoord);
		
		System.out.println(mp.getCoordinates().length);

		WKTWriter wW = new WKTWriter();
		String wkt = wW.write(mp);
		System.out.println(wkt);
	}

}
