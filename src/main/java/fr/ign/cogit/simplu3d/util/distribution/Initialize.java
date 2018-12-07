package fr.ign.cogit.simplu3d.util.distribution;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageOutputStreamSpi;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ImageWriterSpi;
import javax.media.jai.JAI;
import javax.media.jai.remote.SerializableRenderedImage;
import javax.media.jai.util.ImagingListener;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.image.io.ImageIOExt;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.util.logging.Logging;

import com.google.common.collect.Lists;

import it.geosolutions.jaiext.ConcurrentOperationRegistry;

public class Initialize {
	private static volatile boolean initialized;

	public static synchronized void init() {
		if (initialized) {
			return;
		}
		initialized = true;
		privateInit();
	}

	private static void privateInit() {
		// Register logging, and bridge to JAI logging
		GeoTools.init((Hints) null);

	
		
		
		// Custom GeoTools ImagingListener used to ignore common warnings
		JAI.getDefaultInstance().setImagingListener(new ImagingListener() {
			final Logger LOGGER = Logging.getLogger("javax.media.jai");

			@Override
			public boolean errorOccurred(String message, Throwable thrown, Object where, boolean isRetryable)
					throws RuntimeException {
				if (isSerializableRenderedImageFinalization(where, thrown)) {
					LOGGER.log(Level.FINEST, message, thrown);
				} else if (message.contains("Continuing in pure Java mode")) {
					LOGGER.log(Level.FINE, message, thrown);
				} else {
					LOGGER.log(Level.INFO, message, thrown);
				}
				return false; // we are not trying to recover
			}

			private boolean isSerializableRenderedImageFinalization(Object where, Throwable t) {
				if (!(where instanceof SerializableRenderedImage)) {
					return false;
				}

				// check if it's the finalizer
				StackTraceElement[] elements = t.getStackTrace();
				for (StackTraceElement element : elements) {
					if (element.getMethodName().equals("finalize")
							&& element.getClassName().endsWith("SerializableRenderedImage"))
						return true;
				}

				return false;
			}
		});
		
		

		// setup concurrent operation registry
		JAI jaiDef = JAI.getDefaultInstance();
		if (!(jaiDef.getOperationRegistry() instanceof ConcurrentOperationRegistry
				|| jaiDef.getOperationRegistry() instanceof it.geosolutions.jaiext.ConcurrentOperationRegistry)) {
			jaiDef.setOperationRegistry(ConcurrentOperationRegistry.initializeRegistry());
		}
		
		

		// if the server admin did not set it up otherwise, force X/Y axis
		// ordering
		// This one is a good place because we need to initialize this property
		// before any other opeation can trigger the initialization of the CRS
		// subsystem
		if (System.getProperty("org.geotools.referencing.forceXY") == null) {
			System.setProperty("org.geotools.referencing.forceXY", "true");
		}
		if (Boolean.TRUE.equals(Hints.getSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER))) {
			Hints.putSystemDefault(Hints.FORCE_AXIS_ORDER_HONORING, "http");
		}
		Hints.putSystemDefault(Hints.LENIENT_DATUM_SHIFT, true);

		final Hints defHints = GeoTools.getDefaultHints();

		
		// Initialize GridCoverageFactory so that we don't make a lookup every time a
		// factory is
		// needed
		Hints.putSystemDefault(Hints.GRID_COVERAGE_FACTORY, CoverageFactoryFinder.getGridCoverageFactory(defHints));

		// don't allow the connection to the EPSG database to time out. This is a server
		// app,
		// we can afford keeping the EPSG db always on
		System.setProperty("org.geotools.epsg.factory.timeout", "-1");

		// HACK: java.util.prefs are awful. See
		// http://www.allaboutbalance.com/disableprefs. When the site comes
		// back up we should implement their better way of fixing the problem.
		System.setProperty("java.util.prefs.syncInterval", "5000000");


		// Fix issue with tomcat and JreMemoryLeakPreventionListener causing issues with
		// IIORegistry leading to imageio plugins not being properly initialized
//		System.out.println("scanForPlugins");
		ImageIO.scanForPlugins();
		

	
		IIORegistry registry = IIORegistry.getDefaultInstance();
	
		//printRegistry(registry);



		// in any case, the native png reader is worse than the pure java ones, so
		// let's disable it (the native png writer is on the other side faster)...
		ImageIOExt.allowNativeCodec("png", ImageReaderSpi.class, false);
		ImageIOExt.allowNativeCodec("png", ImageWriterSpi.class, true);

//		System.out.println("allowNativeCodec");
		//printRegistry(registry);

		// remove the ImageIO JPEG200 readers/writes, they are outdated and not quite
		// working
		// GeoTools has the GDAL and Kakadu ones which do work, removing these avoids
		// the
		// registry russian roulette (one never knows which one comes first, and
		// to re-order/unregister correctly the registry scan has to be completed
		// unregisterImageIO(ImageReaderSpi.class);
		// unregisterImageIO(ImageWriterSpi.class);

		// initialize GeoTools factories so that we don't make a SPI lookup every time a
		// factory is
		// needed
		Hints.putSystemDefault(Hints.FILTER_FACTORY, CommonFactoryFinder.getFilterFactory2(null));
		Hints.putSystemDefault(Hints.STYLE_FACTORY, CommonFactoryFinder.getStyleFactory(null));
		Hints.putSystemDefault(Hints.FEATURE_FACTORY, CommonFactoryFinder.getFeatureFactory(null));

		// // initialize the default executor service
		// final ThreadPoolExecutor executor =
		// new ThreadPoolExecutor(
		// CoverageAccessInfoImpl.DEFAULT_CorePoolSize,
		// CoverageAccessInfoImpl.DEFAULT_MaxPoolSize,
		// CoverageAccessInfoImpl.DEFAULT_KeepAliveTime,
		// TimeUnit.MILLISECONDS,
		// new LinkedBlockingQueue<Runnable>());
		// Hints.putSystemDefault(Hints.EXECUTOR_SERVICE, executor);

		// ImageIO.scanForPlugins();
		// System.out.println("registerApplicationClasspathSpis");
		// registry.registerApplicationClasspathSpis();
		// printRegistry(registry);
//		System.out.println("unregister Stuff");
		 unregisterImageIO(ImageReaderSpi.class);
		 unregisterImageIO(ImageWriterSpi.class);
		 unregisterImageIO(ImageInputStreamSpi.class);
		 unregisterImageIO(ImageOutputStreamSpi.class);
		// registry.deregisterServiceProvider();
		// com.sun.media.imageioimpl.plugins.gif.GIFImageWriterSpi@d7202f3
		// Service Provider = com.sun.imageio.plugins.gif.GIFImageWriterSpi@3b882e8c
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.pnm.PNMImageWriterSpi@6a306136
		// Service Provider =
		// it.geosolutions.imageioimpl.plugins.tiff.TIFFImageWriterSpi@6306338
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.raw.RawImageWriterSpi@254bee42
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageWriterSpi@7ecce3c7
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi@f6a2461
		// Service Provider = com.sun.imageio.plugins.wbmp.WBMPImageWriterSpi@10dac81
		// Service Provider =
		// it.geosolutions.imageioimpl.plugins.tiff.TIFFImageWriterSpi@5098bf96
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.bmp.BMPImageWriterSpi@2db09c25
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.gif.GIFImageWriterSpi@4e78386f
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.gif.GIFImageWriterSpi@17fea8e6
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageWriterSpi@43b6f9af
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.gif.GIFImageWriterSpi@64e34279
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.bmp.BMPImageWriterSpi@75b79ca9
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi@4512fcb2
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageWriterSpi@53260b07
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.pnm.PNMImageWriterSpi@d9570b4
		// Service Provider = com.sun.imageio.plugins.bmp.BMPImageWriterSpi@1b2c6f8f
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.raw.RawImageWriterSpi@5827f683
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.pnm.PNMImageWriterSpi@3fa8ae0f
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.wbmp.WBMPImageWriterSpi@1c848f23
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageWriterSpi@5c95343b
		// Service Provider =
		// it.geosolutions.imageioimpl.plugins.tiff.TIFFImageWriterSpi@67d4bafb
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.pnm.PNMImageWriterSpi@452cc79c
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.bmp.BMPImageWriterSpi@49e90259
		// Service Provider =
		// it.geosolutions.imageioimpl.plugins.tiff.TIFFImageWriterSpi@d6bed5a
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.raw.RawImageWriterSpi@52f506c4
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.wbmp.WBMPImageWriterSpi@36c085e
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi@33b9f40a
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageWriterSpi@63eac5de
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.wbmp.WBMPImageWriterSpi@5b9ef3ab
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageWriterSpi@1504d03d
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.bmp.BMPImageWriterSpi@6860ebc
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.raw.RawImageWriterSpi@336b3ad5
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.gif.GIFImageWriterSpi@10e18f5e
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.gif.GIFImageWriterSpi@1bb366f3
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.raw.RawImageWriterSpi@1ef645b6
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.wbmp.WBMPImageWriterSpi@21485389
		// Service Provider =
		// it.geosolutions.imageioimpl.plugins.tiff.TIFFImageWriterSpi@4f90da52
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.wbmp.WBMPImageWriterSpi@210d434e
		// Service Provider =
		// it.geosolutions.imageioimpl.plugins.tiff.TIFFImageWriterSpi@3fe78bd
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.pnm.PNMImageWriterSpi@50b2f471
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.pnm.PNMImageWriterSpi@51d6df93
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.gif.GIFImageWriterSpi@3f4eed0b
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageWriterSpi@1a939756
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.wbmp.WBMPImageWriterSpi@74b0a076
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.wbmp.WBMPImageWriterSpi@58d2a314
		// Service Provider =
		// it.geosolutions.imageioimpl.plugins.tiff.TIFFImageWriterSpi@753901b
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.wbmp.WBMPImageWriterSpi@695ef810
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.pnm.PNMImageWriterSpi@42a991e6
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.gif.GIFImageWriterSpi@4c838a4c
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.raw.RawImageWriterSpi@5077e7a7
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.bmp.BMPImageWriterSpi@1e26b5f7
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.pnm.PNMImageWriterSpi@39c97cf
		// Service Provider = com.sun.imageio.plugins.jpeg.JPEGImageWriterSpi@6eb72d62
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi@1ffabda0
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageWriterSpi@6011cdcc
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi@7cb3e26c
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.raw.RawImageWriterSpi@6cf56a96
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageWriterSpi@2b1ebaf8
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi@4bc0f828
		// Service Provider =
		// it.geosolutions.imageioimpl.plugins.tiff.TIFFImageWriterSpi@703cd1d5
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi@48fd1b17
		// Service Provider =
		// it.geosolutions.imageioimpl.plugins.tiff.TIFFImageWriterSpi@57062f6c
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.bmp.BMPImageWriterSpi@32fe644
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.bmp.BMPImageWriterSpi@70808af0
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.gif.GIFImageWriterSpi@33f7ef87
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.raw.RawImageWriterSpi@330531f5
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.bmp.BMPImageWriterSpi@23d73621
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.pnm.PNMImageWriterSpi@4103db7a
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi@155063a9
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi@327f0606
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.bmp.BMPImageWriterSpi@20e9ee13
		// Service Provider =
		// it.geosolutions.imageioimpl.plugins.tiff.TIFFImageWriterSpi@44f26f89
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.raw.RawImageWriterSpi@1395976e
		// Service Provider = com.sun.imageio.plugins.png.PNGImageWriterSpi@202ab4d6
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.wbmp.WBMPImageWriterSpi@3962c41d
		// Category = class javax.imageio.spi.ImageInputStreamSpi
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.FileImageInputStreamExtImplSpi@46d01508
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.StringImageInputStreamSpi@7883a123
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.URLImageInputStreamSpi@60873fb6
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.StringImageInputStreamSpi@4b768342
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.URLImageInputStreamSpi@6bdf32b
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.FileImageInputStreamExtImplSpi@36763390
		// Service Provider =
		// com.sun.media.imageioimpl.stream.ChannelImageInputStreamSpi@d6257ac
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.FileImageInputStreamExtImplSpi@779565b3
		// Service Provider =
		// com.sun.media.imageioimpl.stream.ChannelImageInputStreamSpi@7df50ece
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.URLImageInputStreamSpi@202fdc50
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.StringImageInputStreamSpi@18a0aea5
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.URLImageInputStreamSpi@61cd34c9
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.FileImageInputStreamExtImplSpi@61eedad0
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.StringImageInputStreamSpi@2bc0310e
		// Service Provider =
		// com.sun.media.imageioimpl.stream.ChannelImageInputStreamSpi@289e5063
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.URLImageInputStreamSpi@344bf8c7
		// Service Provider =
		// com.sun.media.imageioimpl.stream.ChannelImageInputStreamSpi@5643349a
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.StringImageInputStreamSpi@2e05cae3
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.FileImageInputStreamExtImplSpi@27cec188
		// Service Provider =
		// com.sun.media.imageioimpl.stream.ChannelImageInputStreamSpi@4afafcea
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.FileImageInputStreamExtImplSpi@40b308d1
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.URLImageInputStreamSpi@44034f0c
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.StringImageInputStreamSpi@297bac47
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.StringImageInputStreamSpi@62f48c19
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.URLImageInputStreamSpi@1edd636a
		// Service Provider =
		// com.sun.media.imageioimpl.stream.ChannelImageInputStreamSpi@1db20248
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.FileImageInputStreamExtImplSpi@7f1365b1
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.FileImageInputStreamExtImplSpi@4118fbf
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.StringImageInputStreamSpi@699f6de8
		// Service Provider =
		// com.sun.media.imageioimpl.stream.ChannelImageInputStreamSpi@1b3702a9
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.URLImageInputStreamSpi@307ec02c
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.URLImageInputStreamSpi@7acded66
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.FileImageInputStreamExtImplSpi@141cda1e
		// Service Provider =
		// com.sun.media.imageioimpl.stream.ChannelImageInputStreamSpi@62ee0114
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.StringImageInputStreamSpi@99342ef
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.URLImageInputStreamSpi@8584224
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.FileImageInputStreamExtImplSpi@5e42b812
		// Service Provider =
		// it.geosolutions.imageio.stream.input.spi.StringImageInputStreamSpi@7e129ed2
		// Service Provider =
		// com.sun.media.imageioimpl.stream.ChannelImageInputStreamSpi@3870a6ad
		// Service Provider =
		// com.sun.imageio.spi.InputStreamImageInputStreamSpi@461e8a67
		// Service Provider = com.sun.imageio.spi.RAFImageInputStreamSpi@33924663
		// Category = class javax.imageio.spi.ImageReaderSpi
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.raw.RawImageReaderSpi@51d888d9
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.tiff.TIFFImageReaderSpi@21ce16cd
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.raw.RawImageReaderSpi@2d1f6d6
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.wbmp.WBMPImageReaderSpi@8590b51
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageReaderSpi@52dd2ad1
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.wbmp.WBMPImageReaderSpi@f23aff
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.tiff.TIFFImageReaderSpi@5a1fdd91
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.raw.RawImageReaderSpi@2da92e27
		// Service Provider =
		// it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi@2a769b66
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.bmp.BMPImageReaderSpi@65ee3cfe
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.wbmp.WBMPImageReaderSpi@19fb4b6e
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.wbmp.WBMPImageReaderSpi@6fe39bf4
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.bmp.BMPImageReaderSpi@38ecc5f7
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.pnm.PNMImageReaderSpi@1acfdc53
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.pnm.PNMImageReaderSpi@13fc722
		// Service Provider = com.sun.imageio.plugins.jpeg.JPEGImageReaderSpi@2cd5cf75
		// Service Provider = com.sun.imageio.plugins.wbmp.WBMPImageReaderSpi@4e33dd12
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.wbmp.WBMPImageReaderSpi@5dddddfb
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.tiff.TIFFImageReaderSpi@446e3ac8
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.bmp.BMPImageReaderSpi@793e6c92
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.wbmp.WBMPImageReaderSpi@2f3ddb91
		// Service Provider =
		// it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi@4126538b
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.tiff.TIFFImageReaderSpi@33eb6246
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.tiff.TIFFImageReaderSpi@317f774e
		// Service Provider = com.sun.imageio.plugins.gif.GIFImageReaderSpi@1ba75915
		// Service Provider =
		// it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi@14e3cad1
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.wbmp.WBMPImageReaderSpi@1ef33441
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.tiff.TIFFImageReaderSpi@49b8fd0b
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.raw.RawImageReaderSpi@1274eb40
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageReaderSpi@4c2b0e91
		// Service Provider =
		// it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi@3548ad72
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.pnm.PNMImageReaderSpi@6d250b9e
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.bmp.BMPImageReaderSpi@56b1fc0a
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.pnm.PNMImageReaderSpi@1f8cccb1
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.raw.RawImageReaderSpi@3fa098e0
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.tiff.TIFFImageReaderSpi@3598b25c
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageReaderSpi@467f153b
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageReaderSpi@1db352f5
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.pnm.PNMImageReaderSpi@63a7d161
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.raw.RawImageReaderSpi@78c56e82
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.pnm.PNMImageReaderSpi@384c2cc
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.raw.RawImageReaderSpi@3422f26e
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.bmp.BMPImageReaderSpi@2e8d97df
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageReaderSpi@5862f52
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.pnm.PNMImageReaderSpi@e5f3407
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageReaderSpi@19e06c4
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageReaderSpi@601a07c0
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageReaderSpi@46816cdb
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.pnm.PNMImageReaderSpi@2a63fdbf
		// Service Provider =
		// it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi@665207b0
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.raw.RawImageReaderSpi@18a2864a
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.tiff.TIFFImageReaderSpi@6fa4514d
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.bmp.BMPImageReaderSpi@56797190
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageReaderSpi@1c3117dc
		// Service Provider =
		// it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi@582d00c0
		// Service Provider =
		// it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi@19cfff20
		// Service Provider =
		// it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi@5aedcb1c
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.pnm.PNMImageReaderSpi@6af34dc
		// Service Provider =
		// it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi@6a92b661
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.wbmp.WBMPImageReaderSpi@202024d4
		// Service Provider = com.sun.imageio.plugins.png.PNGImageReaderSpi@1a7d7b88
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.raw.RawImageReaderSpi@1768631d
		// Service Provider =
		// it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi@2e97a761
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.bmp.BMPImageReaderSpi@3fd7db2e
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.bmp.BMPImageReaderSpi@243ce145
		// Service Provider = com.sun.imageio.plugins.bmp.BMPImageReaderSpi@521881e4
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.bmp.BMPImageReaderSpi@db603cb
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.wbmp.WBMPImageReaderSpi@603370cc
		// Service Provider =
		// com.sun.media.imageioimpl.plugins.tiff.TIFFImageReaderSpi@8182189
		// Category = class javax.imageio.spi.ImageOutputStreamSpi
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.URLImageOutputStreamSpi@103257bb
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.StringImageOutputStreamSpi@46bb71db
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.FileImageOutputStreamExtImplSpi@29691a55
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.StringImageOutputStreamSpi@569d69b9
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.FileImageOutputStreamExtImplSpi@736b9ba7
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.URLImageOutputStreamSpi@4642c36c
		// Service Provider =
		// com.sun.media.imageioimpl.stream.ChannelImageOutputStreamSpi@45579076
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.StringImageOutputStreamSpi@347d3d88
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.URLImageOutputStreamSpi@5f690667
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.FileImageOutputStreamExtImplSpi@3689d5f7
		// Service Provider =
		// com.sun.media.imageioimpl.stream.ChannelImageOutputStreamSpi@57cfecc0
		// Service Provider =
		// com.sun.media.imageioimpl.stream.ChannelImageOutputStreamSpi@f97dcbb
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.StringImageOutputStreamSpi@7ca36f9d
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.FileImageOutputStreamExtImplSpi@5c4f8cbb
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.URLImageOutputStreamSpi@40ef65e4
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.URLImageOutputStreamSpi@4d99cb97
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.StringImageOutputStreamSpi@31bffdd4
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.FileImageOutputStreamExtImplSpi@446c6351
		// Service Provider =
		// com.sun.media.imageioimpl.stream.ChannelImageOutputStreamSpi@6a2ba914
		// Service Provider =
		// com.sun.media.imageioimpl.stream.ChannelImageOutputStreamSpi@319505ed
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.FileImageOutputStreamExtImplSpi@3457cca7
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.StringImageOutputStreamSpi@7dbd4c86
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.URLImageOutputStreamSpi@48869f1b
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.FileImageOutputStreamExtImplSpi@10b14d15
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.StringImageOutputStreamSpi@4936ae9a
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.URLImageOutputStreamSpi@5b228f8b
		// Service Provider =
		// com.sun.media.imageioimpl.stream.ChannelImageOutputStreamSpi@650ed029
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.FileImageOutputStreamExtImplSpi@299c8de9
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.StringImageOutputStreamSpi@2b650c1f
		// Service Provider =
		// com.sun.media.imageioimpl.stream.ChannelImageOutputStreamSpi@401f6825
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.URLImageOutputStreamSpi@400dce73
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.URLImageOutputStreamSpi@592c98c5
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.FileImageOutputStreamExtImplSpi@57e9cf6e
		// Service Provider =
		// com.sun.media.imageioimpl.stream.ChannelImageOutputStreamSpi@724f136e
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.StringImageOutputStreamSpi@35105acc
		// Service Provider =
		// com.sun.media.imageioimpl.stream.ChannelImageOutputStreamSpi@187e0f57
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.FileImageOutputStreamExtImplSpi@44a10e87
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.URLImageOutputStreamSpi@5c35ed4b
		// Service Provider =
		// it.geosolutions.imageio.stream.output.spi.StringImageOutputStreamSpi@7351fea6
		// Service Provider =
		// com.sun.imageio.spi.OutputStreamImageOutputStreamSpi@4e62e3d2
		// Service Provider = com.sun.imageio.spi.RAFImageOutputStreamSpi@6b9733c2

	//	System.out.println("register Stuff");
		registry.registerServiceProvider(new it.geosolutions.imageioimpl.plugins.tiff.TIFFImageWriterSpi());
		registry.registerServiceProvider(new it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi());
		registry.registerServiceProvider(new it.geosolutions.imageio.stream.output.spi.FileImageOutputStreamExtImplSpi());
		registry.registerServiceProvider(new it.geosolutions.imageio.stream.output.spi.StringImageOutputStreamSpi());
		registry.registerServiceProvider(new it.geosolutions.imageio.stream.output.spi.URLImageOutputStreamSpi());
		registry.registerServiceProvider(new it.geosolutions.imageio.stream.input.spi.FileImageInputStreamExtImplSpi());
		registry.registerServiceProvider(new it.geosolutions.imageio.stream.input.spi.URLImageInputStreamSpi());
		registry.registerServiceProvider(new it.geosolutions.imageio.stream.input.spi.StringImageInputStreamSpi());

		// registry.registerServiceProvider(new
		// com.sun.media.imageioimpl.stream.ChannelImageInputStreamSpi());
		// registry.registerServiceProvider(new
		// com.sun.media.imageioimpl.stream.ChannelImageOutputStreamSpi());
		// registry.registerServiceProvider(new
		// com.sun.media.imageioimpl.plugins.tiff.TIFFImageReaderSpi());
		// registry.registerServiceProvider(new
		// com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi());
		// registry.registerServiceProvider(new
		// com.sun.media.jai.imageioimpl.ImageReadWriteSpi());
		ReferencingFactoryFinder.addAuthorityFactory(new org.geotools.referencing.factory.epsg.ThreadedHsqlEpsgFactory());
		//printRegistry(registry);

		// new com.sun.media.imageio.stream.RawImageInputStream(arg0, arg1)
		// com.sun.media.imageioimpl.common.ImageUtil
		// ImageUtil e;
		// Lookup the known TIFF providers
		// ImageReaderSpi jaiProvider = lookupProviderByName(registry,
		// "com.sun.media.imageioimpl.plugins.tiff.TIFFImageReaderSpi");
		// ImageReaderSpi geoProvider = lookupProviderByName(registry,
		// "it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi");
		//
		//// new com.sun.media.imageioimpl.plugins.raw.RawImageReaderSpi();
		// if (jaiProvider != null && geoProvider != null) {
		// // If both are found, EITHER
		// // order the it.geosolutions provider BEFORE the com.sun (JAI) provider
		//// registry.setOrdering(ImageReaderSpi.class, geoProvider, jaiProvider);
		//
		// // OR
		// // un-register the JAI provider
		// registry.deregisterServiceProvider(jaiProvider);
		// }
	}

	private static <T> void unregisterImageIO(Class<T> category) {
		IIORegistry registry = IIORegistry.getDefaultInstance();
		Iterator<T> it = registry.getServiceProviders(category, false);
		ArrayList<T> providers = Lists.newArrayList(it);
		for (T spi : providers) {
			if (spi.getClass().getPackage().getName().contains("com.sun")) {
				registry.deregisterServiceProvider(spi);
			}
		}
	}

//	@SuppressWarnings("unchecked")
//	private static <T> T lookupProviderByName(final ServiceRegistry registry, final String providerClassName) {
//		try {
//			return (T) registry.getServiceProviderByClass(Class.forName(providerClassName));
//		} catch (ClassNotFoundException ignore) {
//			return null;
//		}
//	}

	private static void printRegistry(IIORegistry registry) {
		Iterator<Class<?>> it = registry.getCategories();
		while (it.hasNext()) {
			Class<?> category = it.next();
	//		System.out.println("Category = " + category);
			Iterator<?> spIt = registry.getServiceProviders(category, true);
			while (spIt.hasNext()) {
	//			System.out.println("\tService Provider = " + spIt.next());
			}
		}
	}
}
