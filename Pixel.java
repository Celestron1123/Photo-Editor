package assign11;

/**
 * This class allows the user to store and access picture data to be used in
 * projects requiring image processing.
 * 
 * @author Elijah Potter
 * @version 11-19-2023
 */
public class Pixel {
	private int redValue;
	private int greenValue;
	private int blueValue;

	/**
	 * This method creates a new pixel class with provided RGB values.
	 * 
	 * @param redAmount   - The amount of red in the pixel
	 * @param greenAmount - The amount of green in the pixel
	 * @param blueAmount  - The amount of blue in the pixel
	 * @throws IllegalArgumentException if the user provides illegal RGB values
	 */
	public Pixel(int redAmount, int greenAmount, int blueAmount) {
		if (redAmount < 0 || redAmount > 255 || greenAmount < 0 || greenAmount > 255 || blueAmount < 0
				|| blueAmount > 255) {
			throw new IllegalArgumentException(
					"Invalid value(s). Ensure that the provided color values are between 0 and 255");
		}
		redValue = redAmount;
		greenValue = greenAmount;
		blueValue = blueAmount;
	}

	/**
	 * Provides the amount of red in a given pixel.
	 * 
	 * @return the amount of red
	 */
	public int getRedAmount() {
		return redValue;
	}

	/**
	 * Provides the amount of green in a given pixel.
	 * 
	 * @return the amount of green
	 */
	public int getGreenAmount() {
		return greenValue;
	}

	/**
	 * Provides the amount of blue in a given pixel.
	 * 
	 * @return the amount of blue
	 */
	public int getBlueAmount() {
		return blueValue;
	}

	/**
	 * Provides the packed RGB value in a given pixel.
	 * 
	 * @return the amount of red, green, and blue as a 32-bit integer.
	 */
	public int getPackedRGB() {
		byte redByte = (byte) redValue;
		byte greenByte = (byte) greenValue;
		byte blueByte = (byte) blueValue;
		int fourByte = ((redByte & 0xFF) << 16) | ((greenByte & 0xFF) << 8) | (blueByte & 0xFF);
		return fourByte;
	}

}
