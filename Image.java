package assign11;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * This class represents an image as a two-dimensional array of pixels and
 * provides a number of image filters (via instance methods) for changing the
 * appearance of the image. Application of multiple filters is cumulative; e.g.,
 * obj.redBlueSwapFilter() followed by obj.rotateClockwiseFilter() results in an
 * image altered both in color and orientation.
 *
 * Note: - The pixel in the northwest corner of the image is stored in the first
 * row, first column. - The pixel in the northeast corner of the image is stored
 * in the first row, last column. - The pixel in the southeast corner of the
 * image is stored in the last row, last column. - The pixel in the southwest
 * corner of the image is stored in the last row, first column.
 *
 * @author Prof. Martin and Elijah Potter
 * @version 11-30-2023
 */
public class Image {

	private Pixel[][] imageArray;

	/**
	 * Creates a new Image object by reading the image file with the given filename.
	 *
	 * DO NOT MODIFY THIS METHOD
	 *
	 * @param filename - name of the given image file to read
	 * @throws IOException if file does not exist or cannot be read
	 */
	public Image(String filename) {
		BufferedImage imageInput = null;
		try {
			imageInput = ImageIO.read(new File(filename));
		} catch (IOException e) {
			System.out.println("Image file " + filename + " does not exist or cannot be read.");
		}

		imageArray = new Pixel[imageInput.getHeight()][imageInput.getWidth()];
		for (int i = 0; i < imageArray.length; i++)
			for (int j = 0; j < imageArray[0].length; j++) {
				int rgb = imageInput.getRGB(j, i);
				imageArray[i][j] = new Pixel((rgb >> 16) & 255, (rgb >> 8) & 255, rgb & 255);
			}
	}

	/**
	 * Create an Image object directly from a pre-made Pixel array. This is
	 * primarily to be used in testing.
	 *
	 * DO NOT MODIFY THIS METHOD
	 */
	public Image(Pixel[][] imageArray) {
		this.imageArray = imageArray;
	}

	/**
	 * Create a new "default" Image object, whose purpose is to be used in testing.
	 *
	 * The orientation of this image: cyan red green magenta yellow blue
	 *
	 * DO NOT MODIFY THIS METHOD
	 */
	public Image() {
		imageArray = new Pixel[3][2];
		imageArray[0][0] = new Pixel(0, 255, 255); // cyan
		imageArray[0][1] = new Pixel(255, 0, 0); // red
		imageArray[1][0] = new Pixel(0, 255, 0); // green
		imageArray[1][1] = new Pixel(255, 0, 255); // magenta
		imageArray[2][0] = new Pixel(255, 255, 0); // yellow
		imageArray[2][1] = new Pixel(0, 0, 255); // blue
	}

	/**
	 * Gets the pixel at the specified row and column indexes.
	 *
	 * DO NOT MODIFY THIS METHOD
	 *
	 * @param rowIndex    - given row index
	 * @param columnIndex - given column index
	 * @return the pixel at the given row index and column index
	 * @throws IndexOutOfBoundsException if row or column index is out of bounds
	 */
	public Pixel getPixel(int rowIndex, int columnIndex) {
		if (rowIndex < 0 || rowIndex >= imageArray.length)
			throw new IndexOutOfBoundsException("rowIndex must be in range 0-" + (imageArray.length - 1));

		if (columnIndex < 0 || columnIndex >= imageArray[0].length)
			throw new IndexOutOfBoundsException("columnIndex must be in range 0-" + (imageArray[0].length - 1));

		return imageArray[rowIndex][columnIndex];
	}

	/**
	 * Writes the image represented by this object to file. Does nothing if the
	 * image length is 0.
	 *
	 * DO NOT MODIFY THIS METHOD
	 *
	 * @param filename - name of image file to write
	 * @throws IOException if file does cannot be written
	 */
	public void writeImage(String filename) {
		if (imageArray.length > 0) {
			BufferedImage imageOutput = new BufferedImage(imageArray[0].length, imageArray.length,
					BufferedImage.TYPE_INT_RGB);

			for (int i = 0; i < imageArray.length; i++)
				for (int j = 0; j < imageArray[0].length; j++)
					imageOutput.setRGB(j, i, imageArray[i][j].getPackedRGB());

			try {
				ImageIO.write(imageOutput, "png", new File(filename));
			} catch (IOException e) {
				System.out.println("The image cannot be written to file " + filename);
			}
		}
	}

	/**
	 * Applies a filter to the image represented by this object such that for each
	 * pixel the red amount and blue amount are swapped.
	 *
	 * HINT: Since the Pixel class does not include setter methods for its private
	 * instance variables, create new Pixel objects with the altered colors.
	 */
	public void redBlueSwapFilter() {
		for (int row = 0; row < imageArray.length; row++) {
			for (int column = 0; column < imageArray[row].length; column++) {
				int tempRed = imageArray[row][column].getRedAmount();
				int tempGreen = imageArray[row][column].getGreenAmount();
				int tempBlue = imageArray[row][column].getBlueAmount();
				imageArray[row][column] = new Pixel(tempBlue, tempGreen, tempRed);
			}
		}
	}

	/**
	 * Applies a filter to the image represented by this object such that the color
	 * of each pixel is converted to its corresponding grayscale shade, producing
	 * the effect of a black and white photo. The filter sets the amount of red,
	 * green, and blue all to the value of this average: (originalRed +
	 * originalGreen + originalBlue) / 3
	 *
	 * HINT: Since the Pixel class does not include setter methods for its private
	 * instance variables, create new Pixel objects with the altered colors.
	 */
	public void blackAndWhiteFilter() {
		for (int row = 0; row < imageArray.length; row++) {
			for (int column = 0; column < imageArray[row].length; column++) {
				int tempRed = imageArray[row][column].getRedAmount();
				int tempGreen = imageArray[row][column].getGreenAmount();
				int tempBlue = imageArray[row][column].getBlueAmount();
				int tempAvg = (tempRed + tempBlue + tempGreen) / 3;
				imageArray[row][column] = new Pixel(tempAvg, tempAvg, tempAvg);
			}
		}
	}

	/**
	 * Applies a filter to the image represented by this object such that it is
	 * rotated clockwise (by 90 degrees). This filter rotates directly clockwise, it
	 * should not do this by rotating counterclockwise 3 times.
	 *
	 * HINT: If the image is not square, this filter requires creating a new array
	 * with different lengths. Use the technique of creating and reassigning a new
	 * backing array from BetterDynamicArray (assign06) as a guide for how to make a
	 * second array and eventually reset the imageArray reference to this new array.
	 * Note that we learned how to rotate a square 2D array *left* in Class Meeting
	 * 11.
	 */
	public void rotateClockwiseFilter() {
		Pixel[][] rotoPhoto = new Pixel[imageArray[0].length][imageArray.length];
		for (int row = 0; row < imageArray[0].length; row++)
			for (int column = 0; column < imageArray.length; column++)
				rotoPhoto[row][column] = imageArray[imageArray.length - 1 - column][row];
		imageArray = rotoPhoto;
	}

	/**
	 * This filter shifts any given image to the left by half. The fraction of the
	 * image cut off during the shifting process is shifted back to where the
	 * modified image ends, creating a sort of scrolling effect.
	 */
	public void customFilter() {
		int shiftValue = imageArray[0].length / 2;
		Pixel[][] shiftPhoto = new Pixel[imageArray.length][imageArray[0].length];
		int shiftRow = 0;
		for (int row = 0; row < imageArray.length; row++) {
			int shiftColumn = 0;
			for (int column = 0; column < imageArray[0].length; column++) {
				if ((column + shiftValue >= imageArray[0].length)) {
					shiftPhoto[row][column] = imageArray[shiftRow][shiftColumn];
					shiftColumn++;
				} else
					shiftPhoto[row][column] = imageArray[row][column + shiftValue];
			}
			shiftRow++;
		}
		imageArray = shiftPhoto;
	}

	/**
	 * This filter allows the user to change the brightness of an image from a given
	 * value (positive to increase brightness, and negative to darken)
	 * 
	 * @param sliderValue - The amount of "brightness" desired
	 */
	public void brightnessFilter(int sliderValue) {
		int lightValue = sliderValue;
		for (int row = 0; row < imageArray.length; row++) {
			for (int column = 0; column < imageArray[row].length; column++) {
				int tempRed = imageArray[row][column].getRedAmount();
				tempRed = addBright(tempRed, lightValue);
				int tempGreen = imageArray[row][column].getGreenAmount();
				tempGreen = addBright(tempGreen, lightValue);
				int tempBlue = imageArray[row][column].getBlueAmount();
				tempBlue = addBright(tempBlue, lightValue);
				imageArray[row][column] = new Pixel(tempRed, tempGreen, tempBlue);
			}
		}
	}

	/**
	 * This filter crops a given image according to given values describing
	 * coordinate points of a rectangle.
	 * 
	 * @param topX - The X value of the top-left coordinate
	 * @param topY - The Y value of the top-left coordinate
	 * @param botX - The X value of the bottom-right coordinate
	 * @param botY - The Y value of the bottom-right coordinate
	 */
	public void cropFilter(int topX, int topY, int botX, int botY) {
		int smallX = returnLess(topX, botX);
		int bigX = returnMore(topX, botX);
		int smallY = returnLess(topY, botY);
		int bigY = returnMore(topY, botY);
		int cropRow = 0;
		int cropColumn = 0;
		Pixel[][] cropPhoto = new Pixel[bigY - smallY + 1][bigX - smallX + 1];
		for (int row = smallY; row <= bigY; row++, cropRow++) {
			cropColumn = 0;
			for (int column = smallX; column <= bigX; column++, cropColumn++) {
				cropPhoto[cropRow][cropColumn] = imageArray[row][column];
			}
		}
		imageArray = cropPhoto;
	}

	/**
	 * This private helper method changes the value of a pixel according to a given
	 * "brightness" parameter. If the result exceeds 255 or 0, it will default to
	 * either of those numbers depending on what is desired.
	 * 
	 * @param color  - The value of the pixel
	 * @param bright - How much the value of the pixel should change
	 * @return The result of color + bright, never to exceed 255 or 0
	 */
	private int addBright(int color, int bright) {
		int resultColor = color + bright;
		if (resultColor > 255)
			return 255;
		else if (resultColor < 0)
			return 0;
		else
			return resultColor;
	}

	public int getNumberOfRows() {
		return this.imageArray.length;
	}

	public int getNumberOfColumns() {
		if (this.imageArray.length == 0)
			return 0;
		return this.imageArray[0].length;
	}

	/**
	 * This private helper method returns whichever of the two passed parameters is
	 * lesser.
	 * 
	 * @param entryX - An integer to be compared against the other
	 * @param entryY - An integer to be compared against the other
	 * @return Whichever integer was smaller
	 */
	private int returnLess(int entryX, int entryY) {
		if (entryX <= entryY)
			return entryX;
		else
			return entryY;
	}

	/**
	 * This private helper method returns whichever of the two passed parameters is
	 * greater.
	 * 
	 * @param entryX - An integer to be compared against the other
	 * @param entryY - An integer to be compared against the other
	 * @return Whichever integer was larger
	 */
	private int returnMore(int entryX, int entryY) {
		if (entryX >= entryY)
			return entryX;
		else
			return entryY;
	}

}
