package assign11;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * This class represents a GUI component for displaying an image.
 *
 * @author Prof. Martin
 * @version Assignment 10
 */
public class ImagePanel extends JPanel {

	private BufferedImage bufferedImg;

	/**
	 * Creates a new ImagePanel to display the given image.
	 *
	 * @param img - the given image
	 */
	public ImagePanel(Image img) {
		int rowCount = img.getNumberOfRows();
		int colCount = img.getNumberOfColumns();

		this.bufferedImg = new BufferedImage(colCount, rowCount, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < rowCount; i++)
			for (int j = 0; j < colCount; j++)
				this.bufferedImg.setRGB(j, i, img.getPixel(i, j).getPackedRGB());

		this.setPreferredSize(new Dimension(colCount, rowCount));
	}

	/**
	 * This method is called by the system when a component needs to be painted.
	 * Which can be at one of three times: --when the component first appears --when
	 * the size of the component changes (including resizing by the user) --when
	 * repaint() is called
	 *
	 * Partially overrides the paintComponent method of JPanel.
	 *
	 * @param g -- graphics context onto which we can draw
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.bufferedImg, 0, 0, this);
	}

	// Required by a serializable class (ignore for now)
	private static final long serialVersionUID = 1L;

}
