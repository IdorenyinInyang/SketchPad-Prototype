import java.util.*; 
import java.awt.*; 
import java.awt.event.*; 

public class drawGUI extends Frame implements ActionListener, ItemListener, MouseListener, MouseMotionListener, WindowListener
{
	static final int WIDTH = 800; 
	static final int HEIGHT = 600; 
	
	int upperLeftX, upperLeftY; // coordinates for the upper-left hand corner of the rectangle 
	int width, height; // size of the surrounding rectangle 
	int x1, y1, x2, y2; // coordinates of two selected points 
	
	boolean fill = false, erasure = false; // set/reset flags to indicate where the components will be exposed or not 
	String drawColor = "BLACK"; 
	String drawShape = "line"; 
	// chosen color and shape - initialized to default value 
	
	TextField color = new TextField(); // Text field for displaying the color
	TextField shape = new TextField(); // Text field for displaying the shape
	TextField position = new TextField(); // Text field for displaying the coordinate position of the mouse 
	
	CheckboxGroup fillOutline = new CheckboxGroup(); 
	
	String[] colorNames = {"BLACK","BLUE","CYAN","GRAY","GREEN","MAGENTA","RED","WHITE","YELLOW"}; 
	String[] shapeNames = {"line","square","rectangle","circle","ellipse"}; 
	String[] toolNames  = {"erase","clearpad"}; 
	String[] helpNames  = {"information","about"}; 
	
	String helpText =
			"Sketchpad allows you to draw different plane shapes over a predefined area.\n"+
			"A shape may be either filled or in outline, and in one of eight different colors.\n\n"+
			"The position of the mouse on the screen is recorded in the bottom left-hand \n"+
			"corner of the sketchpad. The choice of color and shape are displayed also in \n"+
			"the left-hand corner of the sketchpad\n\n"+
			"The size of a shape is determined by the position of the mouse when the mouse \n"+
			"button is first pressed, followed by the mouse being dragged to the final position\n"+
			"and released. The first press of the mouse button will generate a reference dot \n"+
			"on the screen. This dot will disappear after the mouse button is released\n\n"+
			"Both the square and circle only use the distance measured along the horizontal \n"+
			"axis when determining the size of the shape.\n\n"+
			"Upon selecting erase, press the mouse button, and move the mouse over the area\n"+
			"to be erased. Releasing the mouse button will deactivate erasure\n\n"+
			"To erase this text area choose clearpad from the TOOLS menu\n\n"; 
	
	TextArea info = new TextArea (helpText, 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY); // scrollbar to display information about sketchpad 
	Frame about = new Frame("About Sketchpad"); 
	
	//----------------------------------------------------------------- 
	
	// These are gonna be helper methods for the program 
	
	private void initializeTextFields() 
	{ 
		// add text field to show color of the figure 
		color.setLocation(5, 25); 
		color.setSize(70, 20); 
		color.setBackground(Color.WHITE); 
		color.setText(drawColor); 
		add(color); 
		
		// add text field to show shape of figure
		shape.setLocation(5,550);
		shape.setSize(70,20);
		shape.setBackground(Color.WHITE);
		shape.setText(drawShape);
		add(shape);

		// add text field to show position of mouse
		position.setLocation(5,575);
		position.setSize(70,20);
		position.setBackground(Color.WHITE);
		add(position);

		// set up text field for information
		info.setLocation(150,250);
		info.setSize(500,100);
		info.setBackground(Color.WHITE);
		info.setEditable(false);
	} 
	
	private void initializeMenuComponents() 
	{ 
		// add pull-down menu to menu bar
		MenuBar bar = new MenuBar();

		// add colors menu		
		Menu colors = new Menu("COLORS");
		for (int index=0; index != colorNames.length; index++) 
		{
			colors.add(colorNames[index]); 
		} 
		
		bar.add(colors);
		colors.addActionListener(this);

		// add shapes menu
		Menu shapes = new Menu("SHAPES");
		for (int index=0; index != shapeNames.length; index++) 
		{
			shapes.add(shapeNames[index]); 
		} 
				
		bar.add(shapes);
		shapes.addActionListener(this);		

		// add tools menu
		Menu tools = new Menu("TOOLS");
		for (int index=0; index != toolNames.length; index++) 
		{ 
			tools.add(toolNames[index]); 
		}
		
		bar.add(tools);
		tools.addActionListener(this);
			
		// add help menu
		Menu help = new Menu("HELP");
		for (int index=0; index != helpNames.length; index++) 
		{
			help.add(helpNames[index]); 
		}
		
		bar.add(help);
		help.addActionListener(this);

		setMenuBar(bar);
	} 
	
	private void initializeRadioButtons() 
	{ 
		// add radio buttons 
		Checkbox fill = new Checkbox("fill", fillOutline, false); 
		Checkbox outline = new Checkbox("outline", fillOutline, false); 
		
		fill.setLocation(5, 500); 
		fill.setSize(70, 20); 
		add(fill); 
		fill.addItemListener(this);
		
		outline.setLocation(5, 475); 
		outline.setSize(70, 20); 
		add(outline); 
		outline.addItemListener(this); 	
	} 
	
	// The constructor  
	public drawGUI(String s) 
	{ 
		super(s); // Technically, this is unnecessary, but whatevs... 
		setBackground(Color.WHITE); 
		setLayout(null); 
		
		initializeTextFields();  
		initializeMenuComponents(); 
		initializeRadioButtons(); 
		
		// Add in any remaining listeners 
		addMouseMotionListener(this); 
		addMouseListener(this); 
		addWindowListener(this);
	} 
	
	public void actionPerformed(ActionEvent action) 
	{ 
		Graphics graphics = getGraphics(); 
		Object source = action.getActionCommand(); 
		
		// checking for any particular color chosen 
		for(int i = 0; i < colorNames.length; i++) 
		{ 
			if(source.equals(colorNames[i])) 
			{ 
				drawColor = colorNames[i]; 
				color.setText(drawColor); 
				return;
			}
		} 
		
		// checking for any particular shape chosen 
		for(int i = 0; i < shapeNames.length; i++) 
		{ 
			if(source.equals(shapeNames[i])) 
			{ 
				drawShape = shapeNames[i]; 
				color.setText(drawShape); 
				return;
			}
		} 
		
		// check for tools chosen 
		if(source.equals("erase")) 
		{ 
			erasure = true; 
			return;
		} 
		
		else if(source.equals("clearpad"))
		{ 
			remove(info); 
			graphics.clearRect(0, 0, 800, 600); 
		} 
		
		// check for help chosen 
		if(source.equals("information")) 
		{ 
			add(info); 
			return;
		} 
		
		else if(source.equals("about")) 
		{ 
			displayAboutWindow(about);
		}
				
	} 
	
	public void itemStateChanged(ItemEvent item) // method to detect which radio button was selected
	{ 
		if(item.getItem() == "fill") 
		{ 
			fill = true; 
		} 
		
		else if(item.getItem() == "outline") 
		{ 
			fill = false;
		}
	} 
	
	protected void displayAboutWindow(Frame about) // method to display information about the Sketchpad GUI
	{ 
		about.setLocation(300, 300); 
		about.setSize(200, 150); 
		about.setBackground(Color.CYAN); 
		about.setFont(new Font("Serif", Font.ITALIC, 14)); 
		about.setLayout(new FlowLayout(FlowLayout.LEFT));  
		
		about.add(new Label("Author: Idorenyin Inyang, example from Barry Holmes")); 
		about.add(new Label("Title: Sketchpad")); 
		about.add(new Label("Date: 2015")); 
		about.setVisible(true); 
		about.addWindowListener(this); 
	}
	
	protected void selectColor(Graphics graphics) // method to change the color of the graphics to correspond
	{ 
		for(int i = 0; i < colorNames.length; i++) 
		{ 
			if(drawColor.equals(colorNames[i])) 
			{ 
				switch(i) 
				{ 
					case 0: graphics.setColor(Color.BLACK); 
						break;  
					case 1: graphics.setColor(Color.BLUE); 
						break; 
					case 2: graphics.setColor(Color.CYAN); 
						break; 
					case 3: graphics.setColor(Color.GRAY); 
						break; 
					case 4: graphics.setColor(Color.GREEN); 
						break; 
					case 5: graphics.setColor(Color.MAGENTA); 
						break; 
					case 6: graphics.setColor(Color.RED); 
						break; 
					case 7: graphics.setColor(Color.WHITE); 
						break; 
					case 8: graphics.setColor(Color.YELLOW); 
						break; 
				}
			}
		}
	} 
	
	protected void closedShapes(Graphics graphics, String shapes, boolean fill) // method to draw shape with a closed orientation
	{ 
		// calculate correct parameters for the shape 
		upperLeftX = Math.min(x1, x2); 
		upperLeftY = Math.min(y1, y2);  
		width = Math.abs(x1 - x2); 
		height = Math.abs(y1 - y2); 
		
		// drawing the appropriate shape 
		if(shapes.equals("sqare") && fill)  
			graphics.fillRect(upperLeftX, upperLeftY, width, width); 
		else if(shapes.equals("square") && !fill) 
			graphics.drawRect(upperLeftX, upperLeftY, width, width); 
		else if(shapes.equals("rectangle") && fill) 
			graphics.fillRect(upperLeftX, upperLeftY, width, height); 
		else if(shapes.equals("rectangle") && !fill) 
			graphics.drawRect(upperLeftX, upperLeftY, width, height);  
		else if(shapes.equals("circle") && fill) 
			graphics.fillOval(upperLeftX, upperLeftY, width, width); 
		else if (shapes.equals("circle") && !fill) 
			graphics.drawOval(upperLeftX, upperLeftY, width, width); 
		
	} 
	
	protected void displayMouseCoordinates(int x, int y) // this method is used to display the coordinates of the mouse
	{ 
		position.setText("[" + String.valueOf(x) + "," + String.valueOf(y) + "]");
	}  
	
	public void mouseDragged(MouseEvent event) // this method captures coordinates of new mouse position as it is dragged across the screen
	{ 
		Graphics graphics = getGraphics(); 
		
		x2 = event.getX(); 
		y2 = event.getY(); 
		
		if(erasure) 
		{ 
			graphics.setColor(Color.YELLOW); 
			graphics.fillRect(x2, y2, 5, 5);
		} 
		
		displayMouseCoordinates(event.getX(), event.getY());
	} 
	
	public void mouseMoved(MouseEvent event) 
	{ 
		displayMouseCoordinates(event.getX(), event.getY()); 
		
	} 
	
	
	public void mouseClicked(MouseEvent event) {}
	public void mouseEntered(MouseEvent event) {} 
	public void mouseExited(MouseEvent event) {} 
	
	public void mousePressed(MouseEvent event) // capture initial coordinates of the mouse
	{ 
		if(erasure) return; 
		
		upperLeftX = 0; 
		upperLeftY = 0; 
		width = 0; 
		height = 0; 
		
		x1 = event.getX(); 
		y1 = event.getY(); 
		
		Graphics graphics = getGraphics(); 
		
		// display reference point of coordinates (x1, y1)
		graphics.drawString(".", x1, y1); 
		displayMouseCoordinates(event.getX(), event.getY());
	} 
	
	public void mouseReleased(MouseEvent event) // draw the appropriate shape when mouse button is released 
	{ // shape will be drawn between the coordinates (x1, y1) and (x2, y2)
		Graphics graphics = getGraphics(); 
		displayMouseCoordinates(event.getX(), event.getY()); 
		
		if(erasure) 
		{ 
			erasure = false; 
			return;
		} 
		
		selectColor(graphics); 
		x2 = event.getX(); 
		y2 = event.getY(); 
		
		if(drawShape.equals("line")) 
			graphics.drawLine(x1, y1, x2, y2);  
		else if(drawShape.equals("circle")) 
			closedShapes(graphics, "circle", fill);  
		else if(drawShape.equals("ellipse")) 
			closedShapes(graphics, "ellipse", fill);  
		else if(drawShape.equals("square")) 
			closedShapes(graphics, "square", fill); 
		else if(drawShape.equals("rectangle")) 
			closedShapes(graphics, "rectangle", fill); 
		
		// erasure reference points at coordinates (x1, y1) 
		graphics.setColor(Color.YELLOW); 
		graphics.drawString(".", x1, y1); 
		graphics.setColor(Color.BLACK);	
	}  
	
	// more blank methods because INTERFACES
	public void windowClosed(WindowEvent event){}
	public void windowDeiconified(WindowEvent event){}
	public void windowIconified(WindowEvent event){}
	public void windowActivated(WindowEvent event){}
	public void windowDeactivated(WindowEvent event){}
	public void windowOpened(WindowEvent event){} 
	
	public void windowClosing(WindowEvent event) // method to check which window is closing
	{ 
		if(event.getWindow() == about) 
		{ 
			about.dispose();
			return; 
		} 
		
		else 
		{ 
			System.exit(0);
		}
	}
}
