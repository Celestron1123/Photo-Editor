package assign11;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This class serves as the basis for the Image Processor Program. It can be
 * broken down into doing two basic functions: Formatting the frame and menu-bar
 * for the program, and handling whenever a specific button is pressed.
 * 
 * The purpose of this program is to allow a user to open an acceptable image
 * file (specifically a jpg, jpeg, png, bmp, or gif), apply any number of
 * filters to the image, and then save the resultant image to their system.
 * 
 * @author Elijah Potter
 * @version 11-30-2023
 */
public class ImageProcessorFrame extends JFrame
		implements ActionListener, ChangeListener, MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;

	// Save-menu items
	private JMenuItem openItem;
	private JMenuItem saveItem;
	private JMenuItem restore;

	// Edit-menu items
	private JMenuItem redBlue;
	private JMenuItem blackWhite;
	private JMenuItem rotate;
	private JMenuItem shift;
	private JMenuItem bright;
	private JMenuItem crop;

	// Main panel and image file(s)
	private ImagePanel imgPanel;
	private Image file;
	private String unfilteredFile;

	// Coordinates for cropping
	private int startX;
	private int startY;
	private int endX;
	private int endY;

	// Slider
	private JSlider brightSlide;

	/**
	 * This method creates the frame upon which all images and filtered images will
	 * be displayed. Along with that frame, it creates the menu-bar (being split
	 * into "File" and "Edit" functionalities) and all contained menu-options
	 * therein.
	 */
	public ImageProcessorFrame() {
		// Initialize the frame
		super("Image Processor");

		// Create the save-menu.
		// Modified code from lab 12
		JMenuBar menubar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		this.openItem = new JMenuItem("Open new image");
		this.openItem.addActionListener(this);
		fileMenu.add(this.openItem);
		this.saveItem = new JMenuItem("Save image");
		this.saveItem.addActionListener(this);
		this.saveItem.setEnabled(false);
		fileMenu.add(this.saveItem);
		this.restore = new JMenuItem("Restore image");
		this.restore.addActionListener(this);
		this.restore.setEnabled(false);
		fileMenu.add(this.restore);
		menubar.add(fileMenu);

		// Create the edit-menu
		JMenu editMenu = new JMenu("Edit");
		this.redBlue = new JMenuItem("Swap red and blue pixels");
		this.redBlue.addActionListener(this);
		this.redBlue.setToolTipText("Swaps all red and blue pixels with one another.");
		this.redBlue.setEnabled(false);
		editMenu.add(redBlue);
		this.blackWhite = new JMenuItem("Desaturate image");
		this.blackWhite.addActionListener(this);
		this.blackWhite.setToolTipText("Makes the image black and white.");
		this.blackWhite.setEnabled(false);
		editMenu.add(this.blackWhite);
		this.rotate = new JMenuItem("Rotate image");
		this.rotate.addActionListener(this);
		this.rotate.setToolTipText("Rotates the image clockwise.");
		this.rotate.setEnabled(false);
		editMenu.add(this.rotate);
		this.shift = new JMenuItem("Shift image");
		this.shift.addActionListener(this);
		this.shift.setToolTipText("Shifts the image to the left by half.");
		this.shift.setEnabled(false);
		editMenu.add(this.shift);
		this.bright = new JMenuItem("Brightness");
		this.bright.addActionListener(this);
		this.bright.setToolTipText("Move the slider up or down to change the brightness of the image.");
		this.bright.setEnabled(false);
		editMenu.add(this.bright);
		this.crop = new JMenuItem("Crop");
		this.crop.addActionListener(this);
		this.crop.setToolTipText("Crop the image by dragging the mouse across the image.");
		this.crop.setEnabled(false);
		editMenu.add(this.crop);
		menubar.add(editMenu);
		this.setJMenuBar(menubar);

		// Create the frame
		this.setPreferredSize(new Dimension(600, 600));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}

	/**
	 * This method allows for the functionality of saving an image file to a system.
	 * However, the saved file will always be a jpg file.
	 * 
	 * This method is a modified block of code from lab12
	 * 
	 * @throws IOException If a file is unable to be saved.
	 */
	private void saveFile() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("C:\\Users"));
		chooser.setSelectedFile(new File("filtered_image.jpg"));
		chooser.setFileFilter(new FileNameExtensionFilter("JPG Images", "jpg"));
		chooser.setDialogTitle("Select the location for the new file.");
		if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(null, "Save image cancelled.");
			return;
		}
		BufferedImage img = new BufferedImage(this.imgPanel.getWidth(), this.imgPanel.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		this.imgPanel.paint(img.getGraphics());
		try {
			ImageIO.write(img, "jpg", chooser.getSelectedFile());
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "The image cannot be written to file.");
		}
	}

	/**
	 * Being the counter-part to the saveFile() method, this method lets the user
	 * select an image file from their system. This method only allows the selection
	 * of jpg, jpeg, png, bmp, or gif files. As well, it enables the use of
	 * filtering and saving a given image.
	 */
	private void openFile() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("C:\\Users"));
		chooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "bmp", "gif"));
		chooser.setDialogTitle("Select a file");
		if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(null, "Open image cancelled.");
			return;
		}
		File imageFile = chooser.getSelectedFile();
		file = new Image(imageFile.getAbsolutePath());
		unfilteredFile = imageFile.getAbsolutePath();
		imgPanel = new ImagePanel(file);
		imgPanel.addMouseListener(this);
		imgPanel.addMouseMotionListener(this);
		setContentPane(imgPanel);
		revalidate();
		this.saveItem.setEnabled(true);
		this.redBlue.setEnabled(true);
		this.blackWhite.setEnabled(true);
		this.rotate.setEnabled(true);
		this.shift.setEnabled(true);
		this.bright.setEnabled(true);
	}

	/**
	 * This method handles what happens whenever any button or menu-option is
	 * selected.
	 * 
	 * @param e - The action taken (i.e. whichever button was pressed)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object click = e.getSource();
		if (click == saveItem) {
			saveFile();
		} else if (click == openItem) {
			openFile();
		} else if (click == redBlue) {
			file.redBlueSwapFilter();
			imgPanel = new ImagePanel(file);
			imgPanel.addMouseListener(this);
			imgPanel.addMouseMotionListener(this);
			setContentPane(imgPanel);
			revalidate();
			this.restore.setEnabled(true);
		} else if (click == blackWhite) {
			file.blackAndWhiteFilter();
			imgPanel = new ImagePanel(file);
			imgPanel.addMouseListener(this);
			imgPanel.addMouseMotionListener(this);
			setContentPane(imgPanel);
			revalidate();
			this.restore.setEnabled(true);
		} else if (click == rotate) {
			file.rotateClockwiseFilter();
			imgPanel = new ImagePanel(file);
			imgPanel.addMouseListener(this);
			imgPanel.addMouseMotionListener(this);
			setContentPane(imgPanel);
			revalidate();
			this.restore.setEnabled(true);
		} else if (click == shift) {
			file.customFilter();
			imgPanel = new ImagePanel(file);
			imgPanel.addMouseListener(this);
			imgPanel.addMouseMotionListener(this);
			setContentPane(imgPanel);
			revalidate();
			this.restore.setEnabled(true);
		} else if (click == bright) {
			// Create the brightness slider
			brightSlide = new JSlider(-200, 200, 0);
			brightSlide.setPaintTicks(true);
			brightSlide.setMajorTickSpacing(25);
			brightSlide.setMinorTickSpacing(5);
			brightSlide.setPaintLabels(true);
			brightSlide.addChangeListener(this);

			// Create the rest of the temporary panel
			file.brightnessFilter(0);
			imgPanel = new ImagePanel(file);
			imgPanel.addMouseListener(this);
			imgPanel.addMouseMotionListener(this);
			JPanel sliderPanel = new JPanel(new GridLayout(1, 1));
			sliderPanel.add(brightSlide);
			JPanel finalPanel = new JPanel(new BorderLayout());
			finalPanel.add(sliderPanel, BorderLayout.SOUTH);
			finalPanel.add(imgPanel, BorderLayout.CENTER);
			setContentPane(finalPanel);
			revalidate();
		} else if (click == crop) {
			file.cropFilter(startX, startY, endX, endY);
			imgPanel = new ImagePanel(file);
			imgPanel.addMouseListener(this);
			imgPanel.addMouseMotionListener(this);
			setContentPane(imgPanel);
			revalidate();
			this.redBlue.setEnabled(true);
			this.blackWhite.setEnabled(true);
			this.rotate.setEnabled(true);
			this.shift.setEnabled(true);
			this.bright.setEnabled(true);
			this.restore.setEnabled(true);
			this.crop.setEnabled(false);
		} else if (click == restore) {
			file = new Image(unfilteredFile);
			imgPanel = new ImagePanel(file);
			imgPanel.addMouseListener(this);
			imgPanel.addMouseMotionListener(this);
			setContentPane(imgPanel);
			revalidate();
			this.restore.setEnabled(false);
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider src = (JSlider) e.getSource();
		if (!src.getValueIsAdjusting()) {
			int sliderValue = (int) src.getValue();
			file.brightnessFilter(sliderValue);
			imgPanel = new ImagePanel(file);
			imgPanel.addMouseListener(this);
			imgPanel.addMouseMotionListener(this);
			setContentPane(imgPanel);
			revalidate();
			this.restore.setEnabled(true);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		this.redBlue.setEnabled(true);
		this.blackWhite.setEnabled(true);
		this.rotate.setEnabled(true);
		this.shift.setEnabled(true);
		this.bright.setEnabled(true);
		this.crop.setEnabled(false);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		setContentPane(imgPanel);
		startX = e.getX();
		startY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		endX = e.getX();
		endY = e.getY();
		int smallX = returnLess(startX, endX);
		int smallY = returnLess(startY, endY);
		Graphics g = getGraphics();
		g.setColor(new Color(105, 105, 105, 125));
		g.fillRect(smallX, smallY + 50, Math.abs(endX - startX), Math.abs(endY - startY));
		this.redBlue.setEnabled(false);
		this.blackWhite.setEnabled(false);
		this.rotate.setEnabled(false);
		this.shift.setEnabled(false);
		this.bright.setEnabled(false);
		this.crop.setEnabled(true);
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {

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
}
