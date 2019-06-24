
package net.imagej.viewingimages;

import java.io.IOException;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.display.DatasetView;
import net.imagej.display.ImageDisplay;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

public class Ex7b_SetDisplayRangeIJ2 {

	public static <T extends RealType<T> & NativeType<T>> void main(
		final String[] args) throws IOException
	{
		// create an instance of imagej
		final ImageJ ij = new ImageJ();

		// show the UI in swing (IJ2) mode
		ij.ui().showUI("swing");

		// note if we show the UI in legacy mode (the default when imagej-legacy is
		// included in as a dependency the example doesn't work
		// ij.ui().showUI();

		Dataset data = (Dataset) ij.io().open("../../images/bridge.tif");

		// Wrap it in a display. This will show it in the active UI(s).
		ImageDisplay display = (ImageDisplay) ij.display().createDisplay("Bridge",
			data);

		// Extract the dataset view.
		DatasetView dv = ij.imageDisplay().getActiveDatasetView(display);

		// Update the channel range.
		dv.setChannelRange(0, 0, 100);

	}

}
