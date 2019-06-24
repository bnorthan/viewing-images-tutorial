
package net.imagej.viewingimages;

import java.io.IOException;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import ij.ImagePlus;

public class Ex7a_DisplayRangeIJ1 {

	public static <T extends RealType<T> & NativeType<T>> void main(
		final String[] args) throws IOException
	{
		// create an instance of imagej
		final ImageJ ij = new ImageJ();

		ij.launch(args);

		// launch it
		ij.launch(args);

		Dataset data = (Dataset) ij.io().open("../../images/bridge.tif");

		// convert bridge to IJ1
		ImagePlus impBridge = ImageJFunctions.wrap(
			(RandomAccessibleInterval<T>) data.getImgPlus(), "bridge");

		impBridge.setDisplayRange(0, 100);
		impBridge.show();
	}
}
