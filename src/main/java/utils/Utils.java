package utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import com.github.wihoho.jama.Matrix;
import org.opencv.core.Mat;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 * Provide general purpose methods for handling OpenCV-JavaFX data conversion.
 * Moreover, expose some "low level" methods for matching few JavaFX behavior.
 *
 * @author <a href="mailto:luigi.derussis@polito.it">Luigi De Russis</a>
 * @author <a href="http://max-z.de">Maximilian Zuleger</a>
 * @version 1.0 (2016-09-17)
 * @since 1.0
 * 
 */
public final class Utils
{
	/**
	 * Convert a Mat object (OpenCV) in the corresponding Image for JavaFX
	 *
	 * @param frame
	 *            the {@link Mat} representing the current frame
	 * @return the {@link Image} to show
	 */
	public static Image mat2Image(Mat frame)
	{
		try
		{
			return SwingFXUtils.toFXImage(matToBufferedImage(frame), null);
		}
		catch (Exception e)
		{
			System.err.println("Cannot convert the Mat object: " + e);
			return null;
		}
	}
	
	/**
	 * Generic method for putting element running on a non-JavaFX thread on the
	 * JavaFX thread, to properly update the UI
	 * 
	 * @param property
	 *            a {@link ObjectProperty}
	 * @param value
	 *            the value to set for the given {@link ObjectProperty}
	 */
	public static <T> void onFXThread(final ObjectProperty<T> property, final T value)
	{
		Platform.runLater(() -> {
			property.set(value);
		});
	}
	
	/**
	 * Support for the {@link mat2image()} method
	 * 
	 * @param original
	 *            the {@link Mat} object in BGR or grayscale
	 * @return the corresponding {@link BufferedImage}
	 */
	private static BufferedImage matToBufferedImage(Mat original)
	{
		// init
		BufferedImage image = null;
		int width = original.width(), height = original.height(), channels = original.channels();
		byte[] sourcePixels = new byte[width * height * channels];
		original.get(0, 0, sourcePixels);
		
		if (original.channels() > 1)
		{
			image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		}
		else
		{
			image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		}
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);
		
		return image;
	}

	public static Matrix convertPGMtoMatrix(String address) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(address);
		Scanner scan = new Scanner(fileInputStream);

		// Discard the magic number
		scan.nextLine();
		scan.nextLine();
		// Read pic width, height and max value
		int picWidth = scan.nextInt();
		int picHeight = scan.nextInt();

		fileInputStream.close();

		// Now parse the file as binary data
		fileInputStream = new FileInputStream(address);
		DataInputStream dis = new DataInputStream(fileInputStream);

		// look for 4 lines (i.e.: the header) and discard them
		int numnewlines = 3;
		while (numnewlines > 0) {
			char c;
			do {
				c = (char) (dis.readUnsignedByte());
			} while (c != '\n');
			numnewlines--;
		}

		// read the image data
		double[][] data2D = new double[picHeight][picWidth];
		for (int row = 0; row < picHeight; row++) {
			for (int col = 0; col < picWidth; col++) {
				data2D[row][col] = dis.readUnsignedByte();
			}
		}

		return new Matrix(data2D);
	}
}
