
package net.imagej.viewingimages;

import java.io.IOException;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imagej.axis.AxisType;
import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imglib2.FinalDimensions;
import net.imglib2.IterableInterval;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import ij.IJ;

public class Ex4b_3DProject {

	public static <T extends RealType<T> & NativeType<T>, S extends Op>
		IterableInterval<T> project(ImgPlus<T> imgPlus, AxisType ax,
			Class<S> projectType, OpService ops)
	{

		long[] projectedDimensions = new long[imgPlus.numDimensions() - 1];

		int projectedDimensionIndex = imgPlus.dimensionIndex(ax);

		int i = 0;

		for (int d = 0; d < imgPlus.numDimensions(); d++) {
			if (d != projectedDimensionIndex) {
				projectedDimensions[i] = imgPlus.dimension(d);
				i++;
			}
		}

		Img<T> projection = ops.create().img(new FinalDimensions(
			projectedDimensions), imgPlus.firstElement());

		UnaryComputerOp<ImgPlus<T>, T> projector = Computers.unary(ops, projectType,
			projection.firstElement(), imgPlus);

		return ops.transform().project(projection, imgPlus,
			(UnaryComputerOp) projector, projectedDimensionIndex);

	}
	

	public static <T extends RealType<T> & NativeType<T>, S extends Op> void
		project(ImgPlus<T> orig, ImgPlus<T> psf, ImgPlus<T> decon, AxisType ax,
			Class<S> projectType, ImageJ ij)
	{

		IterableInterval<T> projectionBars = project((ImgPlus<T>) orig, ax,
			projectType, ij.op());

		ij.ui().show("Bars Y Projection", projectionBars);

		IterableInterval<T> projectionPSF = project((ImgPlus<T>) psf, ax,
			projectType, ij.op());

		IterableInterval<T> projectionDeconvolved = project((ImgPlus<T>) decon, ax,
			projectType, ij.op());

		ij.ui().show("PSF Y Projection", projectionPSF);
		ij.ui().show("Deconvolved Y Projection", projectionDeconvolved);

	}


	@SuppressWarnings("unchecked")
	public static <T extends RealType<T> & NativeType<T>, S extends Op> void main(
		final String[] args) throws IOException
	{
		// create an instance of imagej
		final ImageJ ij = new ImageJ();

		ij.launch(args);

		ij.log().error("show me!");

		// get cells as IJ2 Dataset
		Dataset bars = (Dataset) ij.io().open(
			"../images/Bars-G10-P15-stack-cropped.tif");
		Dataset psf = (Dataset) ij.io().open(
			"../images/PSF-Bars-stack-cropped.tif");
		Dataset deconvolved = (Dataset) ij.io().open(
			"../images/deconvolvedbars.tif");

		IJ2CourseImageUtility.displayAxisInfo(bars, ij.log());
		IJ2CourseImageUtility.displayAxisInfo(psf, ij.log());
		IJ2CourseImageUtility.displayAxisInfo(deconvolved, ij.log());

		// deconvolved has C instead of Z
		deconvolved.axis(2).setType(Axes.Z);

		ij.ui().show("bars", bars);

		ij.ui().show("psf", psf);

		ij.ui().show("deconvolved", deconvolved);

		AxisType axesToProject = Axes.Z;

		Class<S> projectType = (Class<S>) Ops.Stats.Max.class;

		project((ImgPlus<T>) bars.getImgPlus(), (ImgPlus<T>) psf.getImgPlus(),
			(ImgPlus<T>) deconvolved.getImgPlus(), Axes.Z, projectType, ij);
		project((ImgPlus<T>) bars.getImgPlus(), (ImgPlus<T>) psf.getImgPlus(),
			(ImgPlus<T>) deconvolved.getImgPlus(), Axes.Y, projectType, ij);
		IJ.run("Tile", "");
	}
}
