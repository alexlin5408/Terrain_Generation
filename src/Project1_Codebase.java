import jdk.jfr.Configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.lang.Thread;
import java.io.ObjectInputFilter;
import java.util.Arrays;

public class Project1_Codebase {
    //references to some variables we may want to access in a global context
    static int WIDTH = 500; //width of the image
    static int HEIGHT = 500; //height of the image
    static BufferedImage Display; //the image we are displaying

    static BufferedImage PreDisplay;
    static JFrame window; //the frame containing our window
    static WalkerBehavior walk = new WalkerBehavior();
    static PathRendering path = new PathRendering();
    static WorldGeometries world = new WorldGeometries();

    static boolean boundedbutton = true;

    static boolean gradientclicked;

    public static void main(String[] args) {

        //run the GUI on the special event dispatch thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Create the window and set options
                //The window
                window = new JFrame("RandomWalker");
                window.setPreferredSize(new Dimension(WIDTH + 100, HEIGHT + 50));
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.setVisible(true);
                window.pack();


                //Display panel/image
                JPanel DisplayPanel = new JPanel();
                Display = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
                DisplayPanel.add(new JLabel(new ImageIcon(Display)));
                window.add(DisplayPanel, BorderLayout.CENTER);


                //Config panel
                JPanel Configuration = new JPanel();
                Configuration.setBackground(new Color(230, 230, 230));
                Configuration.setPreferredSize(new Dimension(100, 500));
                Configuration.setLayout(new FlowLayout());
                //Step count input
                JLabel StepCountLabel = new JLabel("Step Count:");
                Configuration.add(StepCountLabel);

                JTextField StepCount = new JTextField("500");
                StepCount.setPreferredSize(new Dimension(100, 25));
                Configuration.add(StepCount);

                //Walker type input
                JLabel WalkerType = new JLabel("Walker Type:");
                Configuration.add(WalkerType);

                ButtonGroup WalkerTypes = new ButtonGroup();//group of buttons
                JRadioButton Standard = new JRadioButton("Standard");//creates a radio button. in a ButtonGroup, only one can be selected at a time
                Standard.setActionCommand("standard");//can be grabbed to see which button is active
                Standard.setSelected(true);//set this one as selected by default
                JRadioButton Picky = new JRadioButton("Picky");
                Picky.setActionCommand("picky");
                WalkerTypes.add(Standard); //add to panel
                WalkerTypes.add(Picky);
                Configuration.add(Standard); //set as part of group
                Configuration.add(Picky);

                //Walker type input
                JLabel Geometry = new JLabel("World Geometry:");
                Configuration.add(Geometry);
                ButtonGroup Geometries = new ButtonGroup();
                JRadioButton Bounded = new JRadioButton("Bounded");
                Bounded.setActionCommand("bounded");
                Bounded.setSelected(true);
                JRadioButton Toroidal = new JRadioButton("Toroidal");
                Toroidal.setActionCommand("toroidal");
                Geometries.add(Bounded);
                Geometries.add(Toroidal);
                Configuration.add(Bounded);
                Configuration.add(Toroidal);

                //Path Rendering input
                JLabel Render = new JLabel("Path Render:");
                Configuration.add(Render);
                ButtonGroup Renderers = new ButtonGroup();
                JRadioButton Satellite = new JRadioButton("Satellite");
                Satellite.setActionCommand("satellite");

                Satellite.setSelected(true);
                JRadioButton HeightRend = new JRadioButton("Height");
                HeightRend.setActionCommand("Heightrend");

                JLabel c1 = new JLabel("Dimensions:");
                JTextField dimesions = new JTextField("50");
                dimesions.setPreferredSize(new Dimension(100, 25));
                Renderers.add(Satellite);
                Renderers.add(HeightRend);

                Configuration.add(Satellite);
                Configuration.add(HeightRend);
                Configuration.add(c1);
                Configuration.add(dimesions);


                //Run Button
                JButton Run = new JButton("Walk");

                Run.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int count = Integer.parseInt(StepCount.getText()); //get from a TextField, and read as an int
                        String walk_type = WalkerTypes.getSelection().getActionCommand();//gets the action command of which radio button is selected
                        String geom_type = Geometries.getSelection().getActionCommand();
                        int dimensions = Integer.parseInt(dimesions.getText());
                        String render_type = Renderers.getSelection().getActionCommand();

                        //===Walk, Update Display, repaint===
                        //1. Generate a Buffered image using the data from above (return one from a method)?
                        //2. UpdateDisplay([[insert the image you made]]);
                        walk.setX(dimensions/2);
                        walk.setY(dimensions/2);
                        world.setEdgey(dimensions);
                        world.setEdgex(dimensions);
                        walk.setCount(count);
                        walk.setDimensions(dimensions);
                        //UpdateDisplay(Display);
                        PreDisplay = new BufferedImage(walk.nsize, walk.nsize, BufferedImage.TYPE_INT_ARGB);
                        if (walk_type == "standard")
                        {
                            testStandardWalker(Display);
                        }
                        else if(walk_type == "picky")
                        {
                            testPickyWalker(Display);
                        }

                        if(geom_type == "bounded")
                        {
                            boundedbutton = true;
                        }

                        else if(geom_type == "toroidal")
                        {
                            boundedbutton = false;
                        }

                        Graphics2D g = (Graphics2D) PreDisplay.getGraphics();
                        if (render_type == "satellite" && walk.finalarray.length > 0) {
                            world.sortarray();
                            for (int i = 1; i <= walk.nsize; i++) {
                                for (int j = 1; j <= walk.nsize; j++) {
                                    world.satelliteRendering(i, j);
                                    g.setColor(Color.decode(path.GetColor()));
                                    //System.out.print(g.getTransform());
                                    g.fillRect(i-1, j-1, 1, 1);
                                }
                            }

                        }

                        else if (render_type == "Heightrend" && walk.finalarray.length > 0) {
                            world.sortarray();
                            for (int i = 1; i <= walk.nsize; i++) {
                                for (int j = 1; j <= walk.nsize; j++) {
                                    //world.satelliteRendering(i,j);
                                    path.SetGradient();
                                    Color gradient = new Color(path.GetGradient((int) walk.getFinalarray()[i][j]));
                                    g.setColor(gradient);
                                    //g.setColor(Color.decode(path.GetColor()));
                                    g.fillRect(i-1, j-1, 1, 1);
                                    //g.fillRect(i* world.scale(),j* world.scale(),1,1);
                                }
                            }


                        }
                        UpdateDisplay(Display);
                        window.repaint();


                    }

                });


                Satellite.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PreDisplay = new BufferedImage(walk.nsize, walk.nsize, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g = (Graphics2D) PreDisplay.getGraphics();
                        String render_type = Renderers.getSelection().getActionCommand();
                        if (render_type == "satellite") {
                            world.sortarray();
                            for (int i = 1; i <= walk.nsize; i++) {
                                for (int j = 1; j <= walk.nsize; j++) {

                                    world.satelliteRendering(i, j);
                                    g.setColor(Color.decode(path.GetColor()));
                                    g.fillRect(i-1, j-1, 1, 1);
                                }
                            }
                            UpdateDisplay(Display);
                            window.repaint();
                        }
                    }
                });
                HeightRend.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PreDisplay = new BufferedImage(walk.nsize, walk.nsize, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g = (Graphics2D) PreDisplay.getGraphics();
                        String render_type = Renderers.getSelection().getActionCommand();
                        if (render_type == "Heightrend") {
                            world.sortarray();
                            for (int i = 1; i <= walk.nsize; i++) {
                                for (int j = 1; j <= walk.nsize; j++) {
                                    //world.satelliteRendering(i,j);
                                    path.SetGradient();
                                    Color gradient = new Color(path.GetGradient((int) walk.getFinalarray()[i][j]));
                                    g.setColor(gradient);
                                    //g.setColor(Color.decode(path.GetColor()));
                                    g.fillRect(i-1, j-1, 1, 1);
                                    //g.fillRect(i* world.scale(),j* world.scale(),1,1);
                                }
                            }
                            UpdateDisplay(Display);
                            window.repaint();

                        }
                    }
                });

                JButton RunDivide = new JButton("Divide");

                RunDivide.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String render_type = Renderers.getSelection().getActionCommand();
                        int newDimension = Integer.parseInt(dimesions.getText());
                        newDimension = newDimension * 2;
                        String divide = Integer.toString(newDimension);
                        dimesions.setText(divide);


                        float[][] newarray = new float[(2 * (walk.nsize))+1][(2 * (walk.nsize))+1];
                        for (int i = 1; i <= walk.nsize; i++) {
                            for (int j = 1; j <= walk.nsize; j++) {

                                    newarray[2 * i][2 * j] = walk.getFinalarray()[i][j];
                                    newarray[2 * i][(2 * j) - 1] = walk.getFinalarray()[i][j];
                                    newarray[(2 * i) - 1][2 * j] = walk.getFinalarray()[i][j];
                                    newarray[(2 * i) - 1][(2 * j) - 1] = walk.getFinalarray()[i][j];



                            }
                        }
                        walk.nsize = newDimension;
                        walk.finalarray = newarray;
                        float[][] output = new float[newarray.length][newarray.length];
                        for (int i = 1; i < walk.nsize; i++) {
                            for (int j = 1; j < walk.nsize; j++) {
                                float sum = 0;
                                for (int k = 0; k < walk.kernal.length; k++) {
                                    for (int l = 0; l < walk.kernal.length; l++) {
                                        if (i + k < newarray.length && j + l < newarray.length)
                                        {
                                            sum += newarray[i + k][j + l] * walk.kernal[k][l];
                                        }
                                    }
                                }
                            output[i][j] = sum;
                            }
                        }
                        walk.finalarray = output;


                        PreDisplay = new BufferedImage(walk.nsize, walk.nsize, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g = (Graphics2D) PreDisplay.getGraphics();

                        if (render_type == "Heightrend" && (walk.getFinalarray() !=null)) {
                            //world.sortarray();
                            for (int i = 1; i <= walk.nsize; i++) {
                                for (int j = 1; j <= walk.nsize; j++) {

                                    //world.satelliteRendering(i,j);
                                    path.SetGradient();
                                    Color gradient = new Color(path.GetGradient((int) walk.getFinalarray()[i][j]));
                                    g.setColor(gradient);
                                    //g.setColor(Color.decode(path.GetColor()));
                                    g.fillRect(i-1, j-1,1, 1);
                                    //g.fillRect(i* world.scale(),j* world.scale(),1,1);
                                }
                            }
                        }

                        if (render_type == "satellite" && (walk.getFinalarray() !=null)) {
                            world.sortarray();
                            for (int i = 1; i <= walk.nsize; i++) {
                                for (int j = 1; j <= walk.nsize; j++) {
                                    world.satelliteRendering(i, j);
                                    g.setColor(Color.decode(path.GetColor()));
                                    g.fillRect(i-1, j-1,1, 1);

                                }
                            }
                        }

                        //UpdateDisplay(Display);
                        UpdateDisplay(Display);
                        window.repaint();

                    }

                });

                Configuration.add(Run);
                Configuration.add(RunDivide);
                window.add(Configuration, BorderLayout.EAST);


            }
        });

    }


    //A method to update the display image to match one generated by you
    static void UpdateDisplay(BufferedImage img)
    {
        Graphics2D graphics2D = img.createGraphics();

        graphics2D.drawImage(PreDisplay,0,0,WIDTH,HEIGHT,null);
        graphics2D.dispose();
    }
    static void UpdateDisplayDivision(BufferedImage img)
    {
        Graphics2D graphics2D = img.createGraphics();
        graphics2D.scale(world.getscaleheight(),world.getscaleheight());
        graphics2D.drawImage(PreDisplay,0,0,WIDTH,HEIGHT,null);
        graphics2D.dispose();
    }
    static void testStandardWalker(BufferedImage img) {
        boolean stop = false;
        for (int i = 0; i < walk.getCount() && !stop; i++) {

            walk.StandardWalker();

            if(boundedbutton == true)
            {
                if (world.checkcorners(walk.getX(), walk.getY())) {
                    stop = false;
                }
                if (world.edgecase(walk.getX(), walk.getY())) {
                    world.checkside(walk.getX(), walk.getY());

                    walk.setarray(walk.getX(), walk.getY());
                }
            }

            else if(boundedbutton == false)
            {
                if (world.edgecase(walk.getX(), walk.getY())) {
                    world.tpside(walk.getX(), walk.getY());
                    walk.setarray(walk.getX(), walk.getY());
                }
            }

            if (!world.edgecase(walk.getX(), walk.getY()))
            {
                walk.setarray(walk.getX(), walk.getY());
            }
        }
    }

    static void testPickyWalker(BufferedImage img) {
        boolean stop = false;

        for (int i = 0; i < walk.getCount() && !stop; i++) {

            walk.PickyWalker();

            for (int j = 0; j < walk.getPickyNumber() && !stop; j++)
            {
                if(boundedbutton == true) {

                    if (world.checkcorners(walk.getX(), walk.getY())) {
                        stop = true;
                    }
                    walk.BoxSelection();

                    if (world.edgecase(walk.getX(), walk.getY())) {
                        world.checkside(walk.getX(), walk.getY());
                        walk.setarray(walk.getX(), walk.getY());

                    }
                }
                if(boundedbutton ==false) {
                    walk.BoxSelection();

                    if (world.edgecase(walk.getX(), walk.getY())) {
                        world.tpside(walk.getX(), walk.getY());
                        walk.setarray(walk.getX(), walk.getY());

                    }
                }

                if (!world.edgecase(walk.getX(), walk.getY()))
                {

                    walk.setarray(walk.getX(), walk.getY());
                }

                walk.setCount(walk.getCount()-1);

            }
        };

    }

}
































