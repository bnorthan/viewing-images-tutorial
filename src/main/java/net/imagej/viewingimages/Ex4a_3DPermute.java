
package net.imagej.viewingimages;

import java.io.IOException;

import ij.IJ;
import net.imagej.Dataset;
import net.imagej.DatasetService;
import net.imagej.ImageJ;
import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imagej.axis.AxisType;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

public class Ex4a_3DPermute {

	@SuppressWarnings("unchecked")
	public static <T extends RealType<T> & NativeType<T>> void main(final String[] args) throws IOException {

		// create an instance of imagej
		final ImageJ ij = new ImageJ();

		ij.launch(args);

		// open data
		Dataset data = (Dataset) ij.io().open("../images/CHUM_CR_R12802_SDTIRF_coreg_2018_05_04_mai_40X_fovA.czi");

		IJ2CourseImageUtility.displayAxisInfo(data, ij.log());
		ij.ui().show("Data ", data);

		int yIndex=data.dimensionIndex(Axes.Y);
		int zIndex=data.dimensionIndex(Axes.Z);
		
		IntervalView<T> raiXZY = Views.permute((RandomAccessibleInterval<T>) data, yIndex, zIndex);
		DatasetService datasetService = ij.dataset();
		AxisType[] axisTypes = new AxisType[] { Axes.X, Axes.Y, Axes.Z, Axes.TIME };
		ImgPlus imgPlusZXY = new ImgPlus(datasetService.create(raiXZY), "image", axisTypes);
		
		ij.ui().show("Data XZY", imgPlusZXY);

		IJ.run("Tile", "");

	}
}
