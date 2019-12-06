package GUI; 
//-----------------------------------------------------------------------------------------------------------------------------------------
//															BlueBook DaLAT Graphical User Interface
//
//   Descent and Landing Analysis Toolkit  - Version 0.2 ALPHA
//
//   Author: M. Braun
// 	 Date: 01/03/2019
//
//
//           Version 0.2 
// 							- Updates: Controller organised in dedicated table. 
//
//-----------------------------------------------------------------------------------------------------------------------------------------
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;
import javax.swing.*;  

import Model.atm_dataset;
import Sequence.SequenceElement;
import Simulator_main.DataSets.RealTimeResultSet;
import GUI.FxElements.SpaceShipView3D;
import GUI.FxElements.SpaceShipView3DFrontPage;
import GUI.FxElements.TargetView3D;
import GUI.FxElements.TargetWindow;
import GUI.PostProcessing.CreateCustomChart;
import GUI.PropulsionDraw.PropulsionDrawEditor;
import GUI.Settings.Settings;
import GUI.SimulationSetup.BasicSetup.SidePanelLeft;
import VisualEngine.animation.AnimationSet;
import VisualEngine.engineLauncher.worldGenerator;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import utils.Mathbox;
import utils.ReadInput;
import utils.TextAreaOutputStream;

import com.apple.eawt.Application;

import GUI.DataStructures.InputFileSet;

public class BlueBookVisual implements  ActionListener {
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												Main Container Frame Elements
    //-----------------------------------------------------------------------------------------------------------------------------------------
	static String PROJECT_TITLE = "  BlueBook Descent and Landing Analysis Toolkit - V0.3 ALPHA";
    static int x_init = 1350;
    static int y_init = 860 ;
    public static JFrame MAIN_frame;
    
    public static String CASE_FileEnding   = ".case";
    public static String RESULT_FileEnding = ".res";
    public static int OS_is = 1; 
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												Relative File Paths
    //-----------------------------------------------------------------------------------------------------------------------------------------
	public static String Elevation_File_RES4 	= "/ELEVATION/LRO_4.csv";
	public static String Elevation_File_RES16	= "/ELEVATION/LRO_16.csv";
	public static String Elevation_File_RES64 	= "/ELEVATION/LRO_16.csv";
	public static String Elevation_File_RES128 	= "/ELEVATION/LRO_128.csv";
	public static String LOCALELEVATIONFILE		= "/LocalElevation.inp";   		//
    public static String Init_File   			= "/INP/init.inp" ;		    		// Input: Initial state
    public static String RES_File    			= "/results.txt"  ;       	 	// Input: result file 
    public static String CTR_001_File			= "/CTRL/cntrl_1.inp";		    // Controller 01 input file 
    public static String Prop_File 	 			= "/INP/PROP/prop.inp";			// Main propulsion ystem input file 
    public static String SEQU_File		 		= "/SEQU.res";					// Sequence output file 
    public static String SC_file 				= "/INP/SC/sc.inp";
    public static String ICON_File   	 		= "/images/BB_icon2.png";
    public static String ERROR_File 				= "/INP/ErrorFile.inp";
    public static String SEQUENCE_File   		= "/INP/sequence_1.inp"; 
    public static String CONTROLLER_File			= "CTRL/ctrl_main.inp";
	public static String MAP_EARTH				= "/MAPS/Earth_MAP.jpg";
	public static String MAP_MOON				= "/MAPS/Moon_MAP.jpg";
	public static String MAP_VENUS				= "/MAPS/Venus_MAP.jpg";
	public static String MAP_MARS				= "/MAPS/Mars_MAP.jpg";
	public static String MAP_SOUTHPOLE_MOON		= "/MAPS/Moon_South_Pole.jpg";
	public static String EventHandler_File		= "/INP/eventhandler.inp";
	public static String INTEG_File_01 			= "/INP/INTEG/00_DormandPrince853Integrator.inp";
	public static String INTEG_File_02 			= "/INP/INTEG/01_ClassicalRungeKuttaIntegrator.inp";
	public static String INTEG_File_03 			= "/INP/INTEG/02_GraggBulirschStoerIntegrator.inp";
	public static String INTEG_File_04 			= "/INP/INTEG/03_AdamsBashfordIntegrator.inp";
	public static String INERTIA_File 		    = "/INP/INERTIA.inp";
	public static String InitialAttitude_File   = "/INP/INITIALATTITUDE.inp";
    public static String Aero_file 		        = System.getProperty("user.dir") + "/INP/AERO/aeroBasic.inp";
    public static String sequenceFile 		    = System.getProperty("user.dir") + "/INP/sequenceFile.inp";
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												Constants
    //----------------------------------------------------------------------------------------------------------------------------------------- 
    public static double PI = 3.14159265358979323846264338327950288419716939937510582097494459230781640628620899862803482534211706798214808;
    public static double kB    = 1.380650424e-23;              // Boltzmann constant                         [SI]    
    public static double G     = 1.48808E-34;
    public static int TARGET;  
    public static  double RM = 0; 		// Target planet radius
    public static int indx_target = 0;  // Target planet indx 
	static double deg2rad = PI/180.0; 		//Convert degrees to radians
	static double rad2deg = 180/PI; 		//Convert radians to degrees
	
    public static double[][] DATA_MAIN = { // RM (average radius) [m] || Gravitational Parameter [m3/s2] || Rotational speed [rad/s] || Average Collision Diameter [m]
			{6371000,3.9860044189E14,7.2921150539E-5,1.6311e-9}, 	// Earth
			{1737400,4903E9,2.661861E-6,320},						// Moon (Earth)
			{3389500,4.2838372E13,7.0882711437E-5,1.6311e-9},		// Mars
			{0,0,0,0},												// Venus
	 };
	
	public static String BB_delimiter = " ";
	public static String CurrentWorkfile_Name = "";
	public static File CurrentWorkfile_Path = new File("");
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //											Styles, Fonts, Colors
    //-----------------------------------------------------------------------------------------------------------------------------------------
    public static int gg = 235;
    //public static Color labelColor = new Color(0,0,0);    					    // Label Color
    public static Color labelColor = new Color(220,220,220);    					// Label Color
   	public static Color backgroundColor = new Color(41,41,41);				    // Background Color
   	public static Color valueColor =  new Color(65,105,225);
   	public static Color valueColor2 =  new Color(255,140,0);
   	public static Color w_c = new Color(gg,gg,gg);					            // Box background color
   //	public static Color t_c = new Color(255,255,255);				        // Table background color
   	public static Color t_c = new Color(61,61,61);				                // Table background color
   	
   	
   	public static Color light_gray = new Color(230,230,230);
   	
    static DecimalFormat decf 		  = new DecimalFormat("#.#");
    static DecimalFormat decQuarternion =  new DecimalFormat("#.########");
    static DecimalFormat decAngularRate =  new DecimalFormat("##.####");
    static DecimalFormat df_X4 		  = new DecimalFormat("#####.###");
    static DecimalFormat df_VelVector = new DecimalFormat("#.00000000");
    static Font menufont              = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 12);
    static Font small_font			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    static Font labelfont_small       = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 9);
    static Font labelfont_verysmall   = new Font("Verdana", Font.BOLD, 7);
    static Font targetfont            = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 14);
    static Font HeadlineFont          = new Font("Georgia", Font.LAYOUT_LEFT_TO_RIGHT, 14);
    public static DecimalFormat df 	  = new DecimalFormat();
    
    static List<JRadioButton> DragModelSet = new ArrayList<JRadioButton>();
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												Variables and Container Arrays
    //-----------------------------------------------------------------------------------------------------------------------------------------
    public static int INTEGRATOR = 0; 
    public static String[] Target_Options = { "Earth", 
    									      "Moon" ,	
    									      "Mars", 	
    										  "Venus"};
    public static String[] TargetCurve_Options = { "",						// No target curve -> continous burn
    											   "Parabolic", 
    											   "SquareRoot" ,	
    											   "Linear" 	
			  };	
    public static String[] TargetCurve_Options_TVC = { 	"",						// No target curve -> continous burn
    														"SquareRoot" ,	
    														"Linear" 	
};
    public static String[] Axis_Option_NR = { "Time [s]",
    										  "Longitude [deg]", 
    										  "Latitude [deg]" ,
    										  "Altitude ref. Landing [m]", 
    										  "Altidue ref. mean [m]",
    										  "Radius [m]",
    										  "Velocity (ref. ECEF) [m/s]", 
    										  "Flight Path inclination angle [deg]", 
    										  "Flight Path azimuth angle [deg]", 
    										  "Density [kg/m3]", 
    										  "Static temperature [K]", 
    										  "Mach [-]",
    										  "Heat capacity ratio", 
    										  "Gas constant", 
    										  "Static pressure [Pa]",
    										  "Dynamic Pressure [Pa]",
    										  "Flowzone [-]",
    										  "Aerodynamic Drag Coefficient Cd [-]", 
    										  "Aerodynamic Lift Coefficient Cl [-]",
    										  "Aerodynamic Sideslip Coefficient Cy [-]",
    										  "Aerodynamic Drag Force [N]", 
    										  "Aerodynamic Lift Force [N]",
    										  "Aerodynamic Side Force [N]", 
    										  "Aerodynamic angle of attack [deg]", 
    										  "Aerodynamic bank angle [deg]", 
    										  "Gx NED [m/s2]",
    										  "Gy NED [m/s2]",
    										  "Gz NED [m/s2]",
    										  "G total [m/s2]", 
    										  "Fx NED [N]",
    										  "Fy NED [N]",
    										  "Fz NED [N]",
    										  "Force Aero x NED [N]",
    										  "Force Aero y NED [N]",
    										  "Force Aero z NED [N]",
    										  "Force Thrust x NED [N]",
    										  "Force Thrust y NED [N]",
    										  "Force Thrust z NED [N]",
    										  "Force Gravity x NED [N]",
    										  "Force Gravity y NED [N]",
    										  "Force Gravity z NED [N]",
    										  "Position x ECEF [m]",
    										  "Position y ECEF [m]",
    										  "Position z ECEF [m]",
    										  "Velocity u NED/ECEF [m/s]",
    										  "Velocity v NED/ECEF [m/s]",
    										  "Velocity w NED/ECEF [m/s]",
    										  "Quaternion q1",
    										  "Quaternion q2",
    										  "Quaternion q3",
    										  "Quaternion q4",
    										  "Angular Rate x B2NED [deg/s]",
    										  "Angular Rate y B2NED [deg/s]",
    										  "Angular Rate z B2NED [deg/s]",
    										  "Angular Momentum x B [Nm]",
    										  "Angular Momentum y B [Nm]",
    										  "Angular Momentum z B [Nm]",
    										  "X Roll Angle - Euler Phi [deg]",
    										  "Y Pitch Angle - Euler Theta [deg]",
    										  "Z Yaw Angle - Euler Psi [deg]",
    										  "SC Mass [kg]",
    										  "Normalized Deceleartion [-]",
    										  "Total Engergy [J]",
    										  "Velocity horizontal [m/s]",
    										  "Velocity vertical [m/s]",
    										  "Groundtrack [km]",
    										  "Active Sequence ID [-]",
    										  "CNTRL Time [s]",    										  
    										  "Parachute Cd [-]",
    										  "Drag Force Parachute [N]",
    										  "Primary Thrust CMD [%]",
    										  "Primary Thrust Force [N]", 
    										  "Primary Thrust to mass [N/kg]",
    										  "Primary Tank filling level [%]",
    										  "Primary ISP [s]",
    										  "RCS Momentum X CMD [-]",
    										  "RCS Momentum Y CMD [-]",
    										  "RCS Momentum Z CMD [-]",
    										  "RCS Momentum X [Nm]",
    										  "RCS Momentum Y [Nm]",
    										  "RCS Momentum Z [Nm]",
    										  "RCS tank filling level [%]",
    										  "TM CNTRL Error [m/s]",
    										  "TVC CNTRL Error [deg]",
    										  "Thrust Elevation [deg]",
    										  "Thrust Elevation Angel Rate [deg/s]",
    										  "Thrust Force x B [N]",
    										  "Thrust Force y B [N]",
    										  "Thrust Force z B [N]",
    										  "Vel NED/ECI [m/s]",
    										  "FPA NED/ECI [m/s",
    										  "AZI  NED/ECI [m/s]",
    										  "Primary Propulsion Propellant flow rate [kg/s]",
    										  "Used Propellant Primary [kg]",
    										  "Used Propellant Secondary [kg]",
    										  "DeltV RCS X [m/s]",
    										  "DeltV RCS Y [m/s]",
    										  "DeltV RCS Z [m/s]",
    										  "DeltV RCS   [m/s]",
    										  "DeltaV Primary [m/s]"
    										  };
    
    public static String[] Thrust_switch = { "Universal Module - 3 DoF / 6 DoF"
    };
    public static String[] LocalElevation_Resolution = { "4", 
			  											 "16" , 
			  											 "64", 
			  											 "128"};
    
    public static String[] EventHandler_Type = { "Time [s]", 
			  									 "Longitude [rad]" , 
			  									 "Latitude [rad]", 
			  									 "Altitude [m]",
			  									 "Velocity [m]",
			  									 "FPA [rad]",
			  									 "Azimuth [rad]",
    											     "SC Mass [kg]"};

	public static String[] COLUMS_CONTROLLER = {"ID",
												"Controller Type",
												"P gain",
												"I gain",
												"D gain",
												"MIN cmd",
												"MAX cmd"};
	public static String[] COLUMS_ERROR = 		{"ID",
												"Error Type",
												"Trigger Value []",
												"Error Value"};
	public static String[] ErrorType = { "Thrust",
										 "TVC Lock", 
										 "Event Delay"
			
	};
public static String[] COLUMS_EventHandler = {"Event Type",
							 "Event Value"
};

public static String[] SequenceENDType = {"Time [s]",
										  "Altitude [m]",
										  "Velocity [m/s]",
										  "FPA ref. Horizon [deg]"
};
public static String[] SequenceType = {"Coasting (No Thrust/ FC OFF)",
									   "Constant Thrust (FC OFF)",
									   "Controlled Thrust (FC ON)",
									   "Controlled Pitch (FC ON) RCS Y",
									   "Controlled Bank (FC ON) RCS X",
									   "Parachute Deployment",
									   "Parachute Eject"
};
public static String[] SequenceFC    = { ""};
public static String[] FCTargetCurve = { "Parabolic Velocity-Altitude",
										 "SquareRoot Velocity-Altitude",
										 "Hover Parabolic entry"
};
public static String[] SequenceTVCFC     = { ""};


    public static double h_init;
    public static double v_init;
    public static double v_touchdown;
	public static double Propellant_Mass=0;
	public static double M0;
	
	public static int VelocityCoordinateSystem;
	public static int DOF_System;
	public static double rotX=0;
	public static double rotY=0;
	public static double rotZ=0;
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												Main GUI Elements
    //----------------------------------------------------------------------------------------------------------------------------------------- 
	private SidePanelLeft basicSidePanelLeft ;
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												GUI Elements
    //----------------------------------------------------------------------------------------------------------------------------------------- 
    static int extx_main = 1350;
    static int exty_main = 800; 
    public static boolean chartA3_fd=true;  
	static boolean Chart_DashBoardFlexibleChart_fd = true;
	static boolean CHART_P1_DashBoardOverviewChart_fd = true;
    public static JTextArea textArea = new JTextArea();
    public static JFrame frame_CreateLocalElevationFile;
    public static TextAreaOutputStream  taOutputStream = new TextAreaOutputStream(textArea, "");     
    private static Crosshair xCrosshair_x;
    private static Crosshair yCrosshair_x; 
    public static Crosshair xCrosshair_A3_1,xCrosshair_A3_2,xCrosshair_A3_3,xCrosshair_A3_4,yCrosshair_A3_1,yCrosshair_A3_2,yCrosshair_A3_3,yCrosshair_A3_4;
    public static Crosshair xCrosshair_DashBoardOverviewChart_Altitude_Velocity, yCrosshair_DashBoardOverviewChart_Altitude_Velocity,xCrosshair_DashBoardOverviewChart_Time_FPA, yCrosshair_DashBoardOverviewChart_Time_FPA;
    private static Crosshair xCH_DashboardFlexibleChart, yCH_DashboardFlexibleChart;
    public static JPanel PageX04_Dashboard;
    public static JPanel PageX04_Map;
    public static JPanel PageX04_3;
    public static JPanel PageX04_SimSetup; 
    public static JPanel PageX04_RawDATA; 
    public static JPanel PageX04_PolarMap;
    public static JPanel PolarMapContainer; 
    public static JPanel PageX04_GroundClearance; 
    public static JPanel P1_Plotpanel;
    public static JPanel P1_SidePanel; 
    public static JPanel PageX04_AttitudeSetup;
    public static JSplitPane SplitPane_Page1_Charts_horizontal; 
    public static JSplitPane SplitPane_Page1_Charts_vertical; 
    public static JSplitPane SplitPane_Page1_Charts_vertical2; 
    public static JFreeChart Chart_MercatorMap;
    public static JFreeChart Chart_GroundClearance;
	public static JFreeChart CHART_P1_DashBoardOverviewChart_Altitude_Velocity;
	public static JFreeChart CHART_P1_DashBoardOverviewChart_Time_FPA;
	public static JFreeChart chart_PolarMap;	  
	public static JPanel targetWindow;
	public static JFreeChart Chart_DashBoardFlexibleChart;	
	public static ChartPanel ChartPanel_DashBoardOverviewChart_Altitude_Velocity; 
	public static ChartPanel ChartPanel_DashBoardOverviewChart_Time_FPA; 
	public static ChartPanel ChartPanel_DashBoardFlexibleChart;    
    static public JFreeChart chartA3_1,chartA3_2,chartA3_3,chartA3_4; 
    public static ChartPanel CP_A31,CP_A32,CP_A33,CP_A34;
	public static XYSeriesCollection CHART_P1_DashBoardOverviewChart_Dataset_Altitude_Velocity = new XYSeriesCollection();
	public static DefaultTableXYDataset CHART_P1_DashBoardOverviewChart_Dataset_Time_FPA = new DefaultTableXYDataset();
	public static DefaultTableXYDataset ResultSet_GroundClearance_FlightPath = new DefaultTableXYDataset();
	public static DefaultTableXYDataset ResultSet_GroundClearance_Elevation = new DefaultTableXYDataset();
	public static XYSeriesCollection ResultSet_FlexibleChart = new XYSeriesCollection();
    public static XYSeriesCollection result11_A3_1 = new XYSeriesCollection();
    public static XYSeriesCollection result11_A3_2 = new XYSeriesCollection();
    public static XYSeriesCollection result11_A3_3 = new XYSeriesCollection();
    public static XYSeriesCollection result11_A3_4 = new XYSeriesCollection();
    public static JCheckBox INPUT_ISPMODEL; 
    public static JTextField INPUT_RCSX, INPUT_RCSY, INPUT_RCSZ;
    public static JLabel INDICATOR_PageMap_LAT,INDICATOR_PageMap_LONG, INDICATOR_LAT,INDICATOR_LONG,INDICATOR_ALT,INDICATOR_VEL,INDICATOR_FPA,INDICATOR_AZI,INDICATOR_M0,INDICATOR_INTEGTIME, INDICATOR_TARGET;
    public static JTextField  INPUT_RB; 
    public static JTextField INPUT_M0, INPUT_WRITETIME,INPUT_ISP,INPUT_PROPMASS,INPUT_THRUSTMAX,INPUT_THRUSTMIN,p42_inp14,p42_inp15,p42_inp16,p42_inp17;
    public static JTextField INPUT_PGAIN,INPUT_IGAIN,INPUT_DGAIN,INPUT_CTRLMAX,INPUT_CTRLMIN,INPUT_ISPMIN, INPUT_SURFACEAREA, INPUT_BALLISTICCOEFFICIENT;
    public static JLabel INDICATOR_VTOUCHDOWN ,INDICATOR_DELTAV, INDICATOR_PROPPERC, INDICATOR_RESPROP, Error_Indicator,Module_Indicator;
    public static JRadioButton RB_SurfaceArea, RB_BallisticCoefficient;
    public static JRadioButton SELECT_VelocitySpherical, SELECT_VelocityCartesian;
    public static JRadioButton SELECT_3DOF,SELECT_6DOF;
    public static JTextField INPUT_IXX, INPUT_IXY, INPUT_IXZ, INPUT_IYX, INPUT_IYY, INPUT_IYZ, INPUT_IZX, INPUT_IZY, INPUT_IZZ;
    public static JTextField INPUT_Quarternion1, INPUT_Quarternion2, INPUT_Quarternion3, INPUT_Quarternion4, INPUT_Euler1, INPUT_Euler2,INPUT_Euler3;
    public static JSlider sliderEuler1;
    public static JSlider sliderEuler2;
    public static JSlider sliderEuler3;
    public static JPanel SpaceShip3DControlPanel ;
    public static List<Object> SpaceShip3DControlPanelContent = new ArrayList<Object>();
    public static List<Object> targetWindowContent = new ArrayList<Object>();
    public static TimerTask task_Update;
    public static JTextField ConstantCD_INPUT;
    public static JTextField ConstantParachuteCD_INPUT, INPUT_ParachuteDiameter;
    public static JPanel SequenceLeftPanel;
    public static JTextField INPUT_RCSXTHRUST, INPUT_RCSYTHRUST, INPUT_RCSZTHRUST, INPUT_RCSTANK, INPUT_RCSXISP, INPUT_RCSYISP,INPUT_RCSZISP;
    public static JPanel SequenceProgressBar, FlexibleChartContentPanel, FlexibleChartContentPanel2;
    public static List<JLabel> sequenceProgressBarContent = new ArrayList<JLabel>();
    static DefaultTableModel MODEL_RAWData;
    static JTable TABLE_RAWData; 
    private static JButton yAxisIndicator, xAxisIndicator, yAxisIndicator2, xAxisIndicator2;
    private static  VariableList variableListY, variableListX,variableListY2, variableListX2;
    static JTextField INPUT_GlobalFrequency, INPUT_ControllerFrequency,INPUT_GlobalTime;
    
    
	 static int c_SEQUENCE = 12;
	 static Object[] ROW_SEQUENCE = new Object[c_SEQUENCE];
	 static DefaultTableModel MODEL_SEQUENCE;
	 static JTable TABLE_SEQUENCE;
	 
	 static int c_CONTROLLER = 12;
	 static Object[] ROW_CONTROLLER = new Object[c_SEQUENCE];
	 static DefaultTableModel MODEL_CONTROLLER;
	 static JTable TABLE_CONTROLLER;
	 
	 static int c_ERROR = 5;
	 static Object[] ROW_ERROR = new Object[c_ERROR];
	 static DefaultTableModel MODEL_ERROR;
	 static JTable TABLE_ERROR; 
	 
	 static int c_EventHanlder = 2;
	 static Object[] ROW_EventHandler = new Object[c_EventHanlder];
	 static DefaultTableModel MODEL_EventHandler;
	 static JTable TABLE_EventHandler;
		@SuppressWarnings("rawtypes")
		public static JComboBox EventHandlerTypeCombobox = new JComboBox();
		@SuppressWarnings("rawtypes")
		public static JComboBox SequenceENDTypeCombobox = new JComboBox();
		@SuppressWarnings("rawtypes")
		public static JComboBox SequenceTypeCombobox = new JComboBox();
		@SuppressWarnings("rawtypes")
		public static JComboBox SequenceFCCombobox = new JComboBox();
		@SuppressWarnings("rawtypes")
		public static JComboBox FCTargetCurveCombobox = new JComboBox();
		@SuppressWarnings("rawtypes")
		public static JComboBox SequenceTVCFCCombobox = new JComboBox();
		@SuppressWarnings("rawtypes")
		public static JComboBox TVCFCTargetCurveCombobox = new JComboBox();
		@SuppressWarnings("rawtypes")
		public static JComboBox ErrorTypeCombobox= new JComboBox(); 
	    @SuppressWarnings("rawtypes")
		public static JComboBox Target_chooser,TargetCurve_chooser,AscentDescent_SwitchChooser;
    static Border Earth_border = BorderFactory.createLineBorder(Color.BLUE, 1);
    static Border Moon_border 	= BorderFactory.createLineBorder(Color.GRAY, 1);
    static Border Mars_border 	= BorderFactory.createLineBorder(Color.ORANGE, 1);
    static Border Venus_border = BorderFactory.createLineBorder(Color.GREEN, 1);
    public static JCheckBox p421_linp0;
    private static List<atm_dataset> Page03_storage = new ArrayList<atm_dataset>(); // |1| time |2| altitude |3| velocity
    static XYSeriesCollection ResultSet_MercatorMap = new XYSeriesCollection();
    static XYSeriesCollection ResultSet_PolarMap = new XYSeriesCollection();
    	static int page1_plot_y =380;
    	@SuppressWarnings("rawtypes")
    	public static JComboBox axis_chooser3,axis_chooser4; 
        final static JFXPanel targetWindowFxPanel = new JFXPanel();
    	public static int thirdWindowIndx = 1;
    	
    	
    	public static List<Component> AeroLeftBarAdditionalComponents = new ArrayList<Component>();
    	public static List<Component> AeroParachuteBarAdditionalComponents = new ArrayList<Component>();
    	public static List<JRadioButton> ParachuteBulletPoints = new ArrayList<JRadioButton>();
     	public static  List<RealTimeResultSet> resultSet = new ArrayList<RealTimeResultSet>();
     	public static  List<GUISequenceElement> sequenceContentList = new ArrayList<GUISequenceElement>();
     	public static 				int sequenceDimensionWidth=1500;
     	
     	
     	private static List<InputFileSet> analysisFile = new ArrayList<InputFileSet>();
	//-----------------------------------------------------------------------------
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JPanel createContentPane () throws IOException{
    	JPanel MainGUI = new JPanel();
    	MainGUI = new JPanel();
    	MainGUI.setBackground(backgroundColor);
    	MainGUI.setLayout(new BorderLayout());
    	//---------------------------------------------------------------------------------------
    	//                Initialize paths from relative to absolute file paths
    	//---------------------------------------------------------------------------------------
    	 String dir = System.getProperty("user.dir");
    	 
    	 Init_File = dir + Init_File ;
    	 RES_File  = dir + RES_File  ;
    	 CTR_001_File  = dir + CTR_001_File  ;
    	 Prop_File  = dir + Prop_File  ;
    	 SEQU_File = dir + SEQU_File; 
    	 ICON_File = dir + ICON_File; 
    	 SEQUENCE_File = dir +SEQUENCE_File; 
    	 ERROR_File = dir + ERROR_File; 
    	 SC_file = dir + SC_file;
    	 Elevation_File_RES4 = dir + Elevation_File_RES4 ;
    	 Elevation_File_RES16 = dir + Elevation_File_RES16 ;
    	 Elevation_File_RES64 = dir + Elevation_File_RES64 ;
    	 Elevation_File_RES128 = dir + Elevation_File_RES128 ;
    	 LOCALELEVATIONFILE = dir + LOCALELEVATIONFILE;
    	 MAP_MARS = dir + MAP_MARS;
    	 MAP_EARTH = dir + MAP_EARTH;
    	 MAP_MOON = dir + MAP_MOON;
    	 MAP_VENUS = dir + MAP_VENUS;
    	 MAP_SOUTHPOLE_MOON = dir + MAP_SOUTHPOLE_MOON;
    	 EventHandler_File = dir + EventHandler_File;
    	 INTEG_File_01 = dir + INTEG_File_01;
    	 INTEG_File_02 = dir + INTEG_File_02;
    	 INTEG_File_03 = dir + INTEG_File_03;
    	 INTEG_File_04 = dir + INTEG_File_04; 
    	 INERTIA_File  = dir + INERTIA_File;
    	 InitialAttitude_File = dir + InitialAttitude_File;
    	 

    	// System.out.println(System.getProperty("os.name"));
    	 if(System.getProperty("os.name").contains("Mac")) {
    		 OS_is = 1;
    	 } else if(System.getProperty("os.name").contains("Win")) {
    		 OS_is = 2;
    	 } else if(System.getProperty("os.name").contains("Lin")) {
    		 OS_is = 3;
    	 }
     // ---------------------------------------------------------------------------------
    	 //       Define Task (FileWatcher) Update Result Overview
    	 // ---------------------------------------------------------------------------------
    	  task_Update = new FileWatcher( new File(RES_File) ) {
    		    protected void onChange( File file ) {
    		      // here we code the action on a change
    		     // System.out.println( "File "+ file.getName() +" have change !" );
          		  UPDATE_Page01(true);
          		  if(thirdWindowIndx==1) {
          		refreshTargetView3D();
          		  } else if(thirdWindowIndx==2) {
          			  CreateCustomChart.UpdateChart();
          		  }
            		refreshSpaceCraftView();
    		    }
    		  };
    	   Timer timer = new Timer();
    	  // repeat the check every second
    	   timer.schedule( task_Update , new Date(), 1000 );
    	// ---------------------------------------------------------------------------------
    //       The following function contains all GUI elements of the main window
    // ---------------------------------------------------------------------------------
    	decf.setMaximumFractionDigits(1);
    	decf.setMinimumFractionDigits(1);
        //Create the menu bar.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
        } catch (InstantiationException ex) {
        } catch (IllegalAccessException ex) {
        } catch (UnsupportedLookAndFeelException ex) {
        }
        UIManager.put("MenuBar.background", Color.green);
       // UIManager.put("Menu.background", labelColor);
      //  UIManager.put("MenuItem.background", labelColor);
        UIManager.put("Menu.opaque", true);
        
      	BackgroundMenuBar menuBar = new BackgroundMenuBar();
        //menuBar.setLocation(0, 0);
        menuBar.setColor(new Color(250,250,250));
        menuBar.setOpaque(true);
        menuBar.setPreferredSize(new Dimension(1200, 25));
        MainGUI.add(menuBar, BorderLayout.NORTH);

        JTabbedPane Page04_subtabPane = (JTabbedPane) new JTabbedPane();
        Page04_subtabPane.setPreferredSize(new Dimension(extx_main, exty_main));
        Page04_subtabPane.setBackground(backgroundColor);
        Page04_subtabPane.setForeground(Color.BLACK);
        
     	ImageIcon icon_BlueBook = null;
     	ImageIcon icon_windowSelect = null;
     	ImageIcon icon_visualEngine = null;
     	ImageIcon icon_preProcessing =null;
     	ImageIcon icon_postProcessing =null;
     	ImageIcon icon_simulation =null;
     	int sizeUpperBar=20;
     	try {
		icon_BlueBook = new ImageIcon("images/BB_icon2.png","");
		icon_windowSelect = new ImageIcon("images/windowSelect.png","");
		icon_visualEngine = new ImageIcon("images/visualEngine.png","");
		icon_preProcessing = new ImageIcon("images/preprocessingIcon.png","");
		icon_postProcessing = new ImageIcon("images/postprocessingIcon.png","");
		icon_simulation = new ImageIcon("images/simulationIcon.jpg","");
     	if(OS_is==1) {
        	 icon_BlueBook = new ImageIcon(getScaledImage(icon_BlueBook.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_windowSelect = new ImageIcon(getScaledImage(icon_windowSelect.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_visualEngine = new ImageIcon(getScaledImage(icon_visualEngine.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_simulation = new ImageIcon(getScaledImage(icon_simulation.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_preProcessing = new ImageIcon(getScaledImage(icon_preProcessing.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_postProcessing = new ImageIcon(getScaledImage(icon_postProcessing.getImage(),sizeUpperBar,sizeUpperBar));
     	} else if(OS_is==2) {
     	//	For Windows image icons have to be resized
        	 icon_BlueBook = new ImageIcon(getScaledImage(icon_BlueBook.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_windowSelect = new ImageIcon(getScaledImage(icon_windowSelect.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_visualEngine = new ImageIcon(getScaledImage(icon_visualEngine.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_simulation = new ImageIcon(getScaledImage(icon_simulation.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_preProcessing = new ImageIcon(getScaledImage(icon_preProcessing.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_postProcessing = new ImageIcon(getScaledImage(icon_postProcessing.getImage(),sizeUpperBar,sizeUpperBar));
     	}
     	} catch (Exception e) {
     		System.err.println("Error: Loading image icons failed");
     	}
        //Build the first menu.
     	JMenu menu_BlueBook = new JMenu("BlueBook");
     	menu_BlueBook.setOpaque(true);
     	menu_BlueBook.setBackground(labelColor);
        menu_BlueBook.setFont(small_font);
        menu_BlueBook.setMnemonic(KeyEvent.VK_A);
        menu_BlueBook.setIcon(icon_BlueBook);
        menuBar.add(menu_BlueBook);
        JMenuItem menuItem_OpenResultfile = new JMenuItem("Open Resultfile                 "); 
        menuItem_OpenResultfile.setForeground(Color.gray);
        menuItem_OpenResultfile.setFont(small_font);
        menuItem_OpenResultfile.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_BlueBook.add(menuItem_OpenResultfile);
        menuItem_OpenResultfile.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   
                  		
                    } });
        menu_BlueBook.addSeparator();
        JMenuItem menuItem_Import = new JMenuItem("Settings                "); 
        menuItem_Import.setForeground(labelColor);
        menuItem_Import.setFont(small_font);
        menuItem_Import.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_BlueBook.add(menuItem_Import);
        menuItem_Import.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   Thread thread = new Thread(new Runnable() {
                 		    public void run() {
                 		    		// Create new window here 
                 		    try {
                 		    			Settings.main();
  							} catch (IOException e) {
  								System.err.println("Error: Loaden Real Time Simulation Setup Window Failed");
  								e.printStackTrace();
  							};
                 		    }
                 		});
                 		thread.start();
                    } });
        JMenuItem menuItem_Export = new JMenuItem("Results save as                "); 
        menuItem_Export.setForeground(labelColor);
        menuItem_Export.setFont(small_font);
        menuItem_Export.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_BlueBook.add(menuItem_Export);
        menuItem_Export.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                     	File myfile;
  	        			myfile = new File(dir+"/RESULTS");
  		            	JFileChooser fileChooser = new JFileChooser(myfile);
	  		           	if (fileChooser.showSaveDialog(menuItem_Export) == JFileChooser.APPROVE_OPTION) {}
	  	                File file = fileChooser.getSelectedFile() ;
	  	                String filePath = file.getAbsolutePath();
	  	                filePath = filePath.replaceAll(RESULT_FileEnding, "");
	  	                File source = new File(RES_File);
	  	                File dest = new File(filePath+RESULT_FileEnding);
                	   try {
                	       FileUtils.copyFile(source, dest);
                	   } catch (IOException eIO) {System.out.println(eIO);}
                	   System.out.println("Result file "+file.getName()+" saved.");
                    } });
        menu_BlueBook.addSeparator();
        JMenuItem menuItem_Exit = new JMenuItem("Exit                  "); 
        menuItem_Exit.setForeground(Color.BLACK);
        menuItem_Exit.setFont(small_font);
        menuItem_Exit.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_BlueBook.add(menuItem_Exit);
        menuItem_Exit.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   MAIN_frame.dispose();
                    } });
        //--------------------------------------------------------------------------------------------------------------------------------
        JMenu menu_SIM = new JMenu("Simulation");
        menu_SIM.setForeground(labelColor);
        menu_SIM.setBackground(backgroundColor);
        menu_SIM.setFont(small_font);
        menu_SIM.setIcon(icon_simulation);
        menu_SIM.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu_SIM);
        JMenuItem menuItem_SimSettings = new JMenuItem("Run Simulation                 "); 
        menuItem_SimSettings.setForeground(Color.BLACK);
        menuItem_SimSettings.setFont(small_font);
        menuItem_SimSettings.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_R, ActionEvent.ALT_MASK));
        menu_SIM.add(menuItem_SimSettings);
        menuItem_SimSettings.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
             		  System.out.println("Action: RUN SIMULATION");
      				try {
      					String line;
      					Process proc = Runtime.getRuntime().exec("java -jar SIM.jar");
      					InputStream in = proc.getInputStream();
      					InputStream err = proc.getErrorStream();
      					System.out.println(in);
      					System.out.println(err);
      					 BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
      					  while ((line = input.readLine()) != null) {
      					    System.out.println(line);
      					  }
      					  //UPDATE_Page01();
      				} catch ( IOException e1) {
      					// TODO Auto-generated catch block
      					e1.printStackTrace();
      					System.out.println("Error:  " + e1);
      				} 
                    } });
        JMenuItem menuItem_Update = new JMenuItem("Update Data                 "); 
        menuItem_Update.setForeground(Color.BLACK);
        menuItem_Update.setFont(small_font);
        menuItem_Update.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_U, ActionEvent.ALT_MASK));
        menu_SIM.add(menuItem_Update);
        menuItem_Update.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   UPDATE_Page01(true);
                    } });
        
       
        JMenuItem menuItem_Refresh = new JMenuItem("Refresh Attitude                 "); 
        menuItem_Refresh.setForeground(Color.BLACK);
        menuItem_Refresh.setFont(small_font);
        menu_SIM.add(menuItem_Refresh);
        menuItem_Refresh.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   refreshSpaceCraftView();
                    } });
        
        menuItem_SimSettings = new JMenuItem("Run RealTime Module              "); 
        menuItem_SimSettings.setForeground(Color.BLACK);
        menuItem_SimSettings.setFont(small_font);
        menuItem_SimSettings.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_R, ActionEvent.ALT_MASK));
        menu_SIM.add(menuItem_SimSettings);
        menuItem_SimSettings.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
             		  System.out.println("Action: RUN SIMULATION");
      				try {
      					String line;
      					Process proc = Runtime.getRuntime().exec("java -jar SIM2.jar");
      					InputStream in = proc.getInputStream();
      					InputStream err = proc.getErrorStream();
      					System.out.println(in);
      					System.out.println(err);
      					 BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
      					  while ((line = input.readLine()) != null) {
      					    System.out.println(line);
      					  }
      					  //UPDATE_Page01();
      				} catch ( IOException e1) {
      					// TODO Auto-generated catch block
      					e1.printStackTrace();
      					System.out.println("Error:  " + e1);
      				} 
                    } });
        //--------------------------------------------------------------------------------------------------------------------------------
        JMenu menu_PreProcessing = new JMenu("PreProcessing");
        menu_PreProcessing.setForeground(labelColor);
        menu_PreProcessing.setBackground(backgroundColor);
        menu_PreProcessing.setFont(small_font);
        menu_PreProcessing.setIcon(icon_preProcessing);
        menu_PreProcessing.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu_PreProcessing);
        
        JMenuItem menuItem_ImportScenario = new JMenuItem("Simulation Setup Open               "); 
        menuItem_ImportScenario.setForeground(labelColor);
        menuItem_ImportScenario.setFont(small_font);
        menuItem_ImportScenario.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_PreProcessing.add(menuItem_ImportScenario);
        menuItem_ImportScenario.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                      	File myfile;
   	        			myfile = new File(dir+"/CASES");
   		            	JFileChooser fileChooser = new JFileChooser(myfile);
   		           	if (fileChooser.showOpenDialog(menuItem_Export) == JFileChooser.APPROVE_OPTION) {}
   	                File file = fileChooser.getSelectedFile() ;
   	                String filePath = file.getAbsolutePath();
   	                filePath = filePath.replaceAll(CASE_FileEnding, "");
                       file = new File(filePath + CASE_FileEnding);
               		   CurrentWorkfile_Path = file;
                       CurrentWorkfile_Name = fileChooser.getSelectedFile().getName();
                       MAIN_frame.setTitle("" + PROJECT_TITLE + " | " + CurrentWorkfile_Name.split("[.]")[0]);
                       try {IMPORT_Case();} catch (IOException e1) {System.out.println(e1);}
   					System.out.println("File "+CurrentWorkfile_Name+" opened.");

                	   Page04_subtabPane.setSelectedIndex(1);
                    } });
        JMenuItem menuItem_ExportScenario = new JMenuItem("Simulation Setup Save as              "); 
        menuItem_ExportScenario.setForeground(labelColor);
        menuItem_ExportScenario.setFont(small_font);
        menuItem_ExportScenario.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_PreProcessing.add(menuItem_ExportScenario);
        menuItem_ExportScenario.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {                	   
                   	File myfile;
	        			myfile = new File(dir+"/CASES");
		            	JFileChooser fileChooser = new JFileChooser(myfile);
		           	if (fileChooser.showSaveDialog(menuItem_Export) == JFileChooser.APPROVE_OPTION) {}
	                File file = fileChooser.getSelectedFile() ;
	                String filePath = file.getAbsolutePath();
	                filePath = filePath.replaceAll(CASE_FileEnding, "");
                    file = new File(filePath + CASE_FileEnding);
            		    CurrentWorkfile_Path = file;
                    CurrentWorkfile_Name = fileChooser.getSelectedFile().getName();
                    MAIN_frame.setTitle("" + PROJECT_TITLE + " | " + CurrentWorkfile_Name.split("[.]")[0]);
						EXPORT_Case();
                    } });
        //--------------------------------------------------------------------------------------------------------------------------------
        JMenu menu_PostProcessing = new JMenu("PostProcessing");
        menu_PostProcessing.setForeground(labelColor);
        menu_PostProcessing.setBackground(backgroundColor);
        menu_PostProcessing.setFont(small_font);
        menu_PostProcessing.setIcon(icon_postProcessing);
        menu_PostProcessing.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu_PostProcessing);
        
        JMenuItem menuItem_CreateLocalElevation = new JMenuItem("Create Custom Data Plot               "); 
        menuItem_CreateLocalElevation.setForeground(Color.BLACK);
        menuItem_CreateLocalElevation.setFont(small_font);
        menuItem_CreateLocalElevation.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_PostProcessing.add(menuItem_CreateLocalElevation);
        menuItem_CreateLocalElevation.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   
                	   Thread thread = new Thread(new Runnable() {
               		    public void run() {
               		    		// Create new window here 
               		    try {
               		    	String[] args = {""};
               		    			CreateCustomChart.main(args);
							} catch (IOException e) {
								System.err.println("Error: Loaden Real Time Simulation Setup Window Failed");
								e.printStackTrace();
							};
               		    }
               		});
               		thread.start();
               		
                    } });
        JMenuItem menuItem_DataPlotter = new JMenuItem("Open BlueBook DataPlotter               "); 
        menuItem_DataPlotter.setForeground(Color.BLACK);
        menuItem_DataPlotter.setFont(small_font);
        //menuItem_DataPlotter.setAccelerator(KeyStroke.getKeyStroke(
       //         KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_PostProcessing.add(menuItem_DataPlotter);
        menuItem_DataPlotter.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   
                	   Thread thread = new Thread(new Runnable() {
               		    public void run() {
               		    	@SuppressWarnings("unused")
							Process proc = null;
               		    try {
               		    	proc = Runtime.getRuntime().exec("java -jar BlueBookPlot.jar");
							} catch (IOException e) {
								System.err.println("Error: Loaden Real Time Simulation Setup Window Failed");
								e.printStackTrace();
							};
               		    }
               		});
               		thread.start();
               		
                    } });
      //--------------------------------------------------------------------------------------------------------------------------------
        JMenu menu_VisualEngine = new JMenu("Visual Engine");
        menu_VisualEngine.setForeground(labelColor);
        menu_VisualEngine.setBackground(backgroundColor);
        menu_VisualEngine.setFont(small_font);
        menu_VisualEngine.setIcon(icon_visualEngine);
        menu_VisualEngine.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu_VisualEngine);
        
        JMenuItem menuItem_Open = new JMenuItem("Open VisualEngine                 "); 
        menuItem_Open.setForeground(Color.gray);
        menuItem_Open.setFont(small_font);
        menuItem_Open.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_VisualEngine.add(menuItem_Open);
        menuItem_Open.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	  /*
                	   Thread thread = new Thread(new Runnable() {
                		    public void run() {
                		    	 worldGenerator.launchVisualEngine();
                		    }
                		});
                		thread.start();
                		*/
                    } });
        
        JMenuItem menuItem_Animation = new JMenuItem("Create Animation from Results         "); 
        menuItem_Animation.setForeground(Color.gray);
        menuItem_Animation.setFont(small_font);
        menuItem_Animation.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_VisualEngine.add(menuItem_Animation);
        menuItem_Animation.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	  /*
                	   Thread thread = new Thread(new Runnable() {
                		    public void run() {
                		    	List<AnimationSet> animationSets = READ_AnimationData();
                		    	worldAnimation.launchVisualEngine(animationSets);
                		    }
                		});
                		thread.start();
                		*/
                    } });
        
        JMenuItem menuItem_RealTime = new JMenuItem("Open Real Time Simulation Demo     "); 
        menuItem_RealTime.setForeground(Color.BLACK);
        menuItem_RealTime.setFont(small_font);
        menuItem_RealTime.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_VisualEngine.add(menuItem_RealTime);
        menuItem_RealTime.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	  
                	   Thread thread = new Thread(new Runnable() {
                		    public void run() {
                		    		// Create new window here 
                  				try {
                  					String line;
                  					Process proc = null;
                  					if(OS_is==1) {
                  						 proc = Runtime.getRuntime().exec("java -jar FlyMeToTheMoon_OSX.jar");
                  					} else if (OS_is==2){
                  						 proc = Runtime.getRuntime().exec("java -jar FlyMeToTheMoon_WIN.jar");
                  					}
                  					InputStream in = proc.getInputStream();
                  					InputStream err = proc.getErrorStream();
                  					System.out.println(in);
                  					System.out.println(err);
                  					 BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                  					  while ((line = input.readLine()) != null) {
                  					    System.out.println(line);
                  					  }
                  					  //UPDATE_Page01();
                  				} catch ( IOException e1) {
                  					// TODO Auto-generated catch block
                  					e1.printStackTrace();
                  					System.out.println("Error:  " + e1);
                  				} 
                		    }
                		});
                		thread.start();
                    } });

        JMenu menuItem_AddSpacecraft = new JMenu("Add Spacecraft                ");
        menuItem_AddSpacecraft.setForeground(Color.gray);
        //menuItem_AddSpacecraft.setBackground(backgroundColor);
        menuItem_AddSpacecraft.setFont(small_font);
        menuItem_AddSpacecraft.setMnemonic(KeyEvent.VK_A);
        menu_VisualEngine.add(menuItem_AddSpacecraft);
        ButtonGroup group_sc = new ButtonGroup();

        JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem("Gemini");
        menuItem.setForeground(labelColor);
        menuItem.setFont(small_font);
        menuItem_AddSpacecraft.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   worldGenerator.addEntity("gemini", "gray");
                    } });
        group_sc.add(menuItem);
        menuItem_AddSpacecraft.add(menuItem);

        menuItem = new JRadioButtonMenuItem("Mars Global Surveyor");
        menuItem.setForeground(labelColor);
        menuItem.setFont(small_font);
        menuItem_AddSpacecraft.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   worldGenerator.addEntity("MGS", "gray");
                    } });
        group_sc.add(menuItem);
        menuItem_AddSpacecraft.add(menuItem);
        
        JMenu menuItem_setEnvironment = new JMenu("Set Environment               ");
        menuItem_setEnvironment.setForeground(Color.gray);
        //menuItem_setEnvironment.setBackground(backgroundColor);
        menuItem_setEnvironment.setFont(small_font);
        menuItem_setEnvironment.setMnemonic(KeyEvent.VK_A);
        menu_VisualEngine.add(menuItem_setEnvironment);
        ButtonGroup group_env = new ButtonGroup();
        
        menuItem = new JRadioButtonMenuItem("Space");
        menuItem.setForeground(labelColor);
        menuItem.setFont(small_font);
        menuItem_AddSpacecraft.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   
                    } });
        group_env.add(menuItem);
        menuItem_setEnvironment.add(menuItem);

         menuItem = new JRadioButtonMenuItem("Earth");
         menuItem.setForeground(labelColor);
         menuItem.setFont(small_font);
         menuItem_AddSpacecraft.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                 	   worldGenerator.addEntity("MGS", "gray");
                     } });
         group_env.add(menuItem);
        
        menuItem_setEnvironment.add(menuItem);
         menuItem = new JRadioButtonMenuItem("Moon");
         menuItem.setForeground(labelColor);
         menuItem.setFont(small_font);
         menuItem_AddSpacecraft.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                 	   worldGenerator.addEntity("MGS", "gray");
                     } });
         group_env.add(menuItem);
        menuItem_setEnvironment.add(menuItem);
   //-------------------------------------------------------------------------     
        JMenu menu_Window = new JMenu();
        menu_Window.setText("Window");
       // menu_Window.setForeground(labelColor);
        //menu_Window.setBackground(backgroundColor);
        menu_Window.setForeground(Color.black);
       //menu_Window.setColor(labelColor);
        menu_Window.setFont(small_font);
        menu_Window.setIcon(icon_windowSelect);
        menu_Window.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu_Window);
        
        JMenu menu_ThirdWindow = new JMenu("Set window 2 content");
        //menu_ThirdWindow.setForeground(labelColor);
        //menu_ThirdWindow.setBackground(backgroundColor);
        menu_ThirdWindow.setFont(small_font);
        menu_ThirdWindow.setMnemonic(KeyEvent.VK_A);
        menu_Window.add(menu_ThirdWindow);
        
        ButtonGroup thirdWindow = new ButtonGroup();
        
        menuItem = new JRadioButtonMenuItem("Third 2D Chart Window");
       //menuItem.setForeground(labelColor);
        menuItem.setFont(small_font);
        menuItem.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   try {
					   CreateChart_ThirdWindowChart();
					   SplitPane_Page1_Charts_vertical.setDividerLocation(500);
					   thirdWindowIndx=0;
					} catch (IOException e1) {
					     System.err.println("Error: Thrid window could not be created");
					}
                    } });
        thirdWindow.add(menuItem);
        menu_ThirdWindow.add(menuItem);

         menuItem = new JRadioButtonMenuItem("3D Trajectory View");
        // menuItem.setForeground(labelColor);
         menuItem.setFont(small_font);
         menuItem.setSelected(true);
         menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

							createTargetView3D();
						SplitPane_Page1_Charts_vertical.setDividerLocation(500);
						thirdWindowIndx=1;
                    	       
                     } });
         thirdWindow.add(menuItem);
         menu_ThirdWindow.add(menuItem);
         
         menuItem = new JRadioButtonMenuItem("PostProcessing Area");
        // menuItem.setForeground(labelColor);
         menuItem.setFont(small_font);
         menuItem.setSelected(true);
         menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

							try {
								JPanel Content = CreateCustomChart.createContentPane();
							  	   for(int i=0;i<SpaceShip3DControlPanelContent.size();i++) {
							   		  SpaceShip3DControlPanel.remove((Component) SpaceShip3DControlPanelContent.get(i));
							   	    }
							         SpaceShip3DControlPanel.add(Content,BorderLayout.CENTER);
							         SpaceShip3DControlPanelContent.add(Content);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						SplitPane_Page1_Charts_vertical.setDividerLocation(500);
						thirdWindowIndx=2;
                    	       
                     } });
         thirdWindow.add(menuItem);
         menu_ThirdWindow.add(menuItem);
         
        JMenuItem menuItemSelect3D = new JMenuItem("Select Attitude Indicator");
       // menuItemSelect3D.setForeground(labelColor);
         menuItemSelect3D.setFont(small_font);
         menuItemSelect3D.setForeground(Color.black);
         //menuItemSelect3D.setSelected(true);
         menuItemSelect3D.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    	// Refresh Object file path
                    	// refresh SpaceShipView3D
                    	// refresh SpaceShipView3dFrontPage
                    File myfile;
                		myfile = new File(System.getProperty("user.dir")+"/INP/SpacecraftModelLibrary/");
                    	JFileChooser fileChooser = new JFileChooser(myfile);
                    //	fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.obj", "obj"));
                    //	fileChooser.setFileHidingEnabled(true);;
                    //	fileChooser.setFileFilter(new FileNameExtensionFilter("*.obj", "obj"));
                    	fileChooser.setFileFilter(new FileFilter() {

                    		   public String getDescription() {
                    		       return "Wavefront (*.obj)";
                    		   }

                    		   public boolean accept(File f) {
                    		       if (f.isDirectory()) {
                    		           return false;
                    		       } else {
                    		           String filename = f.getName().toLowerCase();
                    		           return filename.endsWith(".obj")  ;
                    		       }
                    		   }
                    		});
                   	if (fileChooser.showOpenDialog(menuItemSelect3D) == JFileChooser.APPROVE_OPTION) {
                   		
                   		File file = fileChooser.getSelectedFile() ;
                        String filePath = file.getAbsolutePath();
                        SpaceShipView3DFrontPage.setModelObjectPath(filePath);
                        refreshSpaceCraftView() ;
                   	}
                    	       
                     } });
         menu_Window.add(menuItemSelect3D);
         
      //--------------------------------------------------------------------------------------------------------------------------------
        PageX04_Dashboard = new JPanel();
        PageX04_Dashboard.setLocation(0, 0);
        PageX04_Dashboard.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_Dashboard.setLayout(new BorderLayout());
        PageX04_Dashboard.setBackground(backgroundColor);
        PageX04_Dashboard.setForeground(labelColor);
        PageX04_Map = new JPanel();
        PageX04_Map.setLocation(0, 0);
        PageX04_Map.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_Map.setLayout(new BorderLayout());
        PageX04_Map.setBackground(backgroundColor);
        PageX04_Map.setForeground(labelColor);
        PageX04_3 = new JPanel();
        PageX04_3.setLocation(0, 0);
        PageX04_3.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_3.setLayout(new BorderLayout());
        PageX04_3.setBackground(backgroundColor);
        PageX04_3.setForeground(labelColor);
        PageX04_SimSetup = new JPanel(); 
        PageX04_SimSetup.setLocation(0, 0);
        PageX04_SimSetup.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_SimSetup.setLayout(new BorderLayout());
        PageX04_SimSetup.setBackground(backgroundColor);
        PageX04_SimSetup.setForeground(labelColor);
        PageX04_PolarMap = new JPanel();
        PageX04_PolarMap.setLocation(0, 0);
        PageX04_PolarMap.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_PolarMap.setLayout(new BorderLayout());
        PageX04_PolarMap.setBackground(backgroundColor);
        PageX04_PolarMap.setForeground(labelColor);
        PageX04_GroundClearance = new JPanel();
        PageX04_GroundClearance.setLocation(0, 0);
        PageX04_GroundClearance.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_GroundClearance.setLayout(new BorderLayout());
        PageX04_GroundClearance.setBackground(backgroundColor);
        PageX04_GroundClearance.setForeground(labelColor);
        PageX04_RawDATA = new JPanel();
        PageX04_RawDATA.setLocation(0, 0);
        PageX04_RawDATA.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_RawDATA.setLayout(new BorderLayout());
        PageX04_RawDATA.setBackground(backgroundColor);
        PageX04_RawDATA.setForeground(labelColor);
        PageX04_AttitudeSetup = new JPanel();
        PageX04_AttitudeSetup.setLocation(0, 0);
        PageX04_AttitudeSetup.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_AttitudeSetup.setLayout(new BorderLayout());
        PageX04_AttitudeSetup.setBackground(backgroundColor);
        PageX04_AttitudeSetup.setForeground(labelColor);

        // Page 4.1
        P1_SidePanel = new JPanel();
        P1_SidePanel.setLayout(null);
        P1_SidePanel.setPreferredSize(new Dimension(385, exty_main));
        P1_SidePanel.setBackground(backgroundColor);
        P1_SidePanel.setForeground(labelColor);
        PageX04_Dashboard.add(P1_SidePanel, BorderLayout.LINE_START);
        
        P1_Plotpanel = new JPanel();
        P1_Plotpanel.setLayout(new BorderLayout());
        P1_Plotpanel.setPreferredSize(new Dimension(900, exty_main-100));
        P1_Plotpanel.setBackground(backgroundColor);
        P1_Plotpanel.setForeground(Color.white);
        PageX04_Dashboard.add(P1_Plotpanel,BorderLayout.LINE_END);
        
        SplitPane_Page1_Charts_horizontal = new JSplitPane();
        SplitPane_Page1_Charts_horizontal.setOrientation(JSplitPane.VERTICAL_SPLIT );
        SplitPane_Page1_Charts_horizontal.setDividerLocation(0.35);
        SplitPane_Page1_Charts_horizontal.setDividerSize(3);
        SplitPane_Page1_Charts_horizontal.setUI(new BasicSplitPaneUI() {
               @SuppressWarnings("serial")
   			public BasicSplitPaneDivider createDefaultDivider() {
               return new BasicSplitPaneDivider(this) {
                   @SuppressWarnings("unused")
   				public void setBorder( Border b) {
                   }

                   @Override
                       public void paint(Graphics g) {
                       g.setColor(Color.gray);
                       g.fillRect(0, 0, getSize().width, getSize().height);
                           super.paint(g);
                       }
               };
               }
           });

        SplitPane_Page1_Charts_horizontal.addComponentListener(new ComponentListener(){

   			@Override
   			public void componentHidden(ComponentEvent arg0) {
   				// TODO Auto-generated method stub
   				
   			}

   			@Override
   			public void componentMoved(ComponentEvent arg0) {
   				// TODO Auto-generated method stub
   				//System.out.println("Line moved");	
   				
   			}

   			@Override
   			public void componentResized(ComponentEvent arg0) {
   				// TODO Auto-generated method stub

   			}

   			@Override
   			public void componentShown(ComponentEvent arg0) {
   				// TODO Auto-generated method stub
   				
   			}
       
       	});
        SplitPane_Page1_Charts_horizontal.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, 
    		    new PropertyChangeListener() {
    		        @Override
    		        public void propertyChange(PropertyChangeEvent pce) {

    		        }
    		});
        SplitPane_Page1_Charts_horizontal.setDividerLocation(400);
       	P1_Plotpanel.add(SplitPane_Page1_Charts_horizontal, BorderLayout.CENTER);
        
       	
       	
        SplitPane_Page1_Charts_vertical = new JSplitPane();
       	//SplitPane_Page1_Charts.setPreferredSize(new Dimension(1000, 1000));
        SplitPane_Page1_Charts_vertical.setOrientation(JSplitPane.HORIZONTAL_SPLIT );
       //	SplitPane_Page1_Charts.setForeground(labelColor);
       //	SplitPane_Page1_Charts.setBackground(Color.gray);
        SplitPane_Page1_Charts_vertical.setDividerSize(3);
        SplitPane_Page1_Charts_vertical.setUI(new BasicSplitPaneUI() {
               @SuppressWarnings("serial")
   			public BasicSplitPaneDivider createDefaultDivider() {
               return new BasicSplitPaneDivider(this) {
                   @SuppressWarnings("unused")
   				public void setBorder( Border b) {
                   }
                   @Override
                       public void paint(Graphics g) {
                       g.setColor(Color.gray);
                       g.fillRect(0, 0, getSize().width, getSize().height);
                           super.paint(g);
                       }
               };
               }
           });
        SplitPane_Page1_Charts_vertical.setDividerLocation(500);
        SplitPane_Page1_Charts_horizontal.add(SplitPane_Page1_Charts_vertical, JSplitPane.TOP);
        
        SplitPane_Page1_Charts_vertical2 = new JSplitPane();
       	//SplitPane_Page1_Charts.setPreferredSize(new Dimension(1000, 1000));
        SplitPane_Page1_Charts_vertical2.setOrientation(JSplitPane.HORIZONTAL_SPLIT );
       //	SplitPane_Page1_Charts.setForeground(labelColor);
       //	SplitPane_Page1_Charts.setBackground(Color.gray);
        SplitPane_Page1_Charts_vertical2.setDividerSize(3);
        SplitPane_Page1_Charts_vertical2.setUI(new BasicSplitPaneUI() {
               @SuppressWarnings("serial")
   			public BasicSplitPaneDivider createDefaultDivider() {
               return new BasicSplitPaneDivider(this) {
                   @SuppressWarnings("unused")
   				public void setBorder( Border b) {
                   }
                   @Override
                       public void paint(Graphics g) {
                       g.setColor(Color.gray);
                       g.fillRect(0, 0, getSize().width, getSize().height);
                           super.paint(g);
                       }
               };
               }
           });
        SplitPane_Page1_Charts_vertical2.setDividerLocation(500);
        SplitPane_Page1_Charts_vertical2.setOneTouchExpandable(true);
        SplitPane_Page1_Charts_vertical2.setContinuousLayout(true);
        SplitPane_Page1_Charts_vertical2.resetToPreferredSizes();
        SplitPane_Page1_Charts_horizontal.add(SplitPane_Page1_Charts_vertical2, JSplitPane.BOTTOM);
       	
	    SpaceShip3DControlPanel = new JPanel();
		SpaceShip3DControlPanel.setLayout(new BorderLayout());
		SpaceShip3DControlPanel.setBackground(backgroundColor);
		SpaceShip3DControlPanel.setForeground(labelColor);
		//SpaceShip3DControlPanel.setSize(450, 400);
		SplitPane_Page1_Charts_vertical.add(SpaceShip3DControlPanel, JSplitPane.RIGHT);
		
	    JPanel TargetViewControlPanel = new JPanel();
	    TargetViewControlPanel.setLayout(new BorderLayout());
	    TargetViewControlPanel.setPreferredSize(new Dimension(450, 25));
	    TargetViewControlPanel.setBackground(backgroundColor);
	    SpaceShip3DControlPanel.add(TargetViewControlPanel, BorderLayout.PAGE_END);
	    
     	ImageIcon iconPlayPause =null;
     	 sizeUpperBar=25;
     	try {
     		iconPlayPause = new ImageIcon("images/playPauseIcon.png","");
     		iconPlayPause = new ImageIcon(getScaledImage(iconPlayPause.getImage(),sizeUpperBar,sizeUpperBar));
     	} catch (Exception e) {
     		System.err.println("Error: Dashboard control panel icons could not be loaded."); 
     	}
	    
        JButton ButtonTargetViewControlPlay = new JButton("");
        ButtonTargetViewControlPlay.setLocation(100, 0);
        ButtonTargetViewControlPlay.setSize(45,25);
        ButtonTargetViewControlPlay.setBackground(backgroundColor);
        ButtonTargetViewControlPlay.setOpaque(true);
        ButtonTargetViewControlPlay.setBorderPainted(false);
        ButtonTargetViewControlPlay.setIcon(iconPlayPause);
        ButtonTargetViewControlPlay.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) {
	                Platform.runLater(new Runnable() {
	                    @Override
	                    public void run() {
	                    			TargetView3D.playPauseAnimation();
	                    }
	                    });
        	}} );
	    TargetViewControlPanel.add(ButtonTargetViewControlPlay, BorderLayout.CENTER);
	    
	    JSlider SpeedSliderTargetViewControl = GuiComponents.getGuiSliderSpeed(small_font, 100, 0, 10, 40, labelColor, backgroundColor);
	    SpeedSliderTargetViewControl.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				double rotspeed = ((double) (SpeedSliderTargetViewControl.getValue()))/100;
				TargetView3D.targetBodyRotSpeed = rotspeed;
			}
	    	
	    });
	    TargetViewControlPanel.add(SpeedSliderTargetViewControl, BorderLayout.EAST);
	    
	    
	    FlexibleChartContentPanel = new JPanel();
	    FlexibleChartContentPanel.setLayout(new BorderLayout());
	    FlexibleChartContentPanel.setBackground(backgroundColor);
	    FlexibleChartContentPanel.setForeground(labelColor);
		//SpaceShip3DControlPanel.setSize(450, 400);
	    SplitPane_Page1_Charts_vertical2.add(FlexibleChartContentPanel, JSplitPane.LEFT);
	    
	    JPanel FlexibleChartControlPanel = new JPanel();
	    FlexibleChartControlPanel.setLayout(new BorderLayout());
	    FlexibleChartControlPanel.setBackground(backgroundColor);
	    //FlexibleChartContentPanel.add(FlexibleChartControlPanel, BorderLayout.PAGE_START);
	    
	    JPanel xControlPanel = new JPanel();
	    xControlPanel.setLayout(new BorderLayout());
	    //xControlPanel.setPreferredSize(new Dimension(1000, 25));
	    xControlPanel.setBackground(backgroundColor);
	    FlexibleChartContentPanel.add(xControlPanel, BorderLayout.PAGE_END);
	    
	    JPanel yControlPanel = new JPanel();
	    yControlPanel.setLayout(new BorderLayout());
	    //yControlPanel.setPreferredSize(new Dimension(400, 25));
	    yControlPanel.setBackground(backgroundColor);
	    FlexibleChartContentPanel.add(yControlPanel, BorderLayout.LINE_START);

	      
	       yAxisIndicator = new JButton();
	       variableListY =  new VariableList(yAxisIndicator, "y",2);
	       yAxisIndicator.setBackground(backgroundColor);
	       yAxisIndicator.setForeground(labelColor);
	       yAxisIndicator.setOpaque(true);
	       yAxisIndicator.setBorderPainted(false);
	      TextIcon t1 = new TextIcon(yAxisIndicator, "Altitude [m]", TextIcon.Layout.HORIZONTAL);
	      RotatedIcon r1 = new RotatedIcon(t1, RotatedIcon.Rotate.UP);
	      t1.setFont(small_font);
	      yAxisIndicator.addActionListener(new ActionListener() {

	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				variableListY.getVariableList(Axis_Option_NR);
			}
	    	  
	      });
	      yAxisIndicator.setIcon( r1 );
	      variableListY.setSelectedIndx(4);
	      
	       
	       xAxisIndicator = new JButton();
	       variableListX =  new VariableList(xAxisIndicator, "x",2);
	       xAxisIndicator.setBackground(backgroundColor);
	       xAxisIndicator.setForeground(labelColor);
	       xAxisIndicator.setOpaque(true);
	       xAxisIndicator.setBorderPainted(false);
	       t1 = new TextIcon(xAxisIndicator, "Time [s]", TextIcon.Layout.HORIZONTAL);
	       r1 = new RotatedIcon(t1, RotatedIcon.Rotate.ABOUT_CENTER);
	      xAxisIndicator.addActionListener(new ActionListener() {

	
			@Override
			public void actionPerformed(ActionEvent arg1) {
				variableListX.getVariableList(Axis_Option_NR);
			}
	    	  
	      });
	      xAxisIndicator.setIcon( r1 );

	      xAxisIndicator.setPreferredSize(new Dimension(25,25));
	      yAxisIndicator.setPreferredSize(new Dimension(25,25));
	      xAxisIndicator.setMinimumSize(new Dimension(25,25));
	      yAxisIndicator.setMinimumSize(new Dimension(25,25));
	      xControlPanel.add(xAxisIndicator, BorderLayout.CENTER);
	      yControlPanel.add(yAxisIndicator, BorderLayout.CENTER);
	    
	    
	    //---------------------------------------------------------
	      
		    FlexibleChartContentPanel2 = new JPanel();
		    FlexibleChartContentPanel2.setLayout(new BorderLayout());
		    FlexibleChartContentPanel2.setBackground(backgroundColor);
		    FlexibleChartContentPanel2.setForeground(labelColor);
			//SpaceShip3DControlPanel.setSize(450, 400);
		    SplitPane_Page1_Charts_vertical.add(FlexibleChartContentPanel2, JSplitPane.LEFT);
		    
		    JPanel FlexibleChartControlPanel2 = new JPanel();
		    FlexibleChartControlPanel2.setLayout(new BorderLayout());
		    FlexibleChartControlPanel2.setBackground(backgroundColor);
		    FlexibleChartContentPanel2.add(FlexibleChartControlPanel2, BorderLayout.PAGE_START);
		    
		    JPanel xControlPanel2 = new JPanel();
		    xControlPanel2.setLayout(new BorderLayout());
		    //xControlPanel.setPreferredSize(new Dimension(1000, 25));
		    xControlPanel2.setBackground(backgroundColor);
		    FlexibleChartContentPanel2.add(xControlPanel2, BorderLayout.PAGE_END);
		    
		    JPanel yControlPanel2 = new JPanel();
		    yControlPanel2.setLayout(new BorderLayout());
		    //yControlPanel.setPreferredSize(new Dimension(400, 25));
		    yControlPanel2.setBackground(backgroundColor);
		    FlexibleChartContentPanel2.add(yControlPanel2, BorderLayout.LINE_START);

		     
		       yAxisIndicator2 = new JButton();
		       variableListY2 =  new VariableList(yAxisIndicator2, "y",1);
		       yAxisIndicator2.setBackground(backgroundColor);
		       yAxisIndicator2.setForeground(labelColor);
		       yAxisIndicator2.setOpaque(true);
		       yAxisIndicator2.setBorderPainted(false);
		      TextIcon t2 = new TextIcon(yAxisIndicator2, "Altitude [m]", TextIcon.Layout.HORIZONTAL);
		      RotatedIcon r2 = new RotatedIcon(t2, RotatedIcon.Rotate.UP);
		      t2.setFont(small_font);
		      yAxisIndicator2.addActionListener(new ActionListener() {

		
				@Override
				public void actionPerformed(ActionEvent arg2) {
					variableListY2.getVariableList(Axis_Option_NR);
				}
		    	  
		      });
		      yAxisIndicator2.setIcon( r2 );
		      variableListY2.setSelectedIndx(4);
		      
		       
		       xAxisIndicator2 = new JButton();
		       variableListX2 =  new VariableList(xAxisIndicator2, "x",1);
		       xAxisIndicator2.setBackground(backgroundColor);
		       xAxisIndicator2.setForeground(labelColor);
		       xAxisIndicator2.setOpaque(true);
		       xAxisIndicator2.setBorderPainted(false);
		       t2 = new TextIcon(xAxisIndicator2, "Velocity [m/s]", TextIcon.Layout.HORIZONTAL);
		       r2 = new RotatedIcon(t2, RotatedIcon.Rotate.ABOUT_CENTER);
		      xAxisIndicator2.addActionListener(new ActionListener() {

		
				@Override
				public void actionPerformed(ActionEvent arg3) {
					variableListX2.getVariableList(Axis_Option_NR);
				}
		    	  
		      });
		      xAxisIndicator2.setIcon( r2 );
		      variableListX2.setSelectedIndx(6);

		      xAxisIndicator2.setPreferredSize(new Dimension(25,25));
		      yAxisIndicator2.setPreferredSize(new Dimension(25,25));
		      xAxisIndicator2.setMinimumSize(new Dimension(25,25));
		      yAxisIndicator2.setMinimumSize(new Dimension(25,25));
		      xControlPanel2.add(xAxisIndicator2, BorderLayout.CENTER);
		      yControlPanel2.add(yAxisIndicator2, BorderLayout.CENTER);
		      
		      
		
        JScrollPane scrollPane_P1 = new JScrollPane(P1_SidePanel);
        scrollPane_P1.setPreferredSize(new Dimension(415, exty_main));
        scrollPane_P1.getVerticalScrollBar().setUnitIncrement(16);
        PageX04_Dashboard.add(scrollPane_P1, BorderLayout.LINE_START);
        JScrollPane scrollPane1_P1 = new JScrollPane(P1_Plotpanel);
        scrollPane1_P1.getVerticalScrollBar().setUnitIncrement(16);
        PageX04_Dashboard.add(scrollPane1_P1, BorderLayout.CENTER);
        
        int uy_p41 = 10 ; 
        int y_ext_vel = 10; 
      
        JLabel LABEL_LONG = new JLabel(" Longitude [deg]");
        LABEL_LONG.setLocation(65, uy_p41 + 0 );
        LABEL_LONG.setSize(150, 20);
        LABEL_LONG.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //LABEL_LONG.setBorder(Moon_border);
        LABEL_LONG.setBackground(backgroundColor);
        LABEL_LONG.setForeground(labelColor);
        LABEL_LONG.setFont(small_font);
        P1_SidePanel.add(LABEL_LONG);
        JLabel LABEL_LAT = new JLabel(" Latitude [deg]");
        LABEL_LAT.setLocation(65, uy_p41 + 25 );
        LABEL_LAT.setSize(150, 20);
        LABEL_LAT.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        LABEL_LAT.setFont(small_font);
        //LABEL_LAT.setBorder(Moon_border);
        LABEL_LAT.setBackground(backgroundColor);
        LABEL_LAT.setForeground(labelColor);
        P1_SidePanel.add(LABEL_LAT);
        JLabel LABEL_ALT = new JLabel(" Altitude [m]");
        LABEL_ALT.setLocation(65, uy_p41 + 50 );
        LABEL_ALT.setSize(150, 20);
        LABEL_ALT.setFont(small_font);
        LABEL_ALT.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //LABEL_ALT.setBorder(Moon_border);
        LABEL_ALT.setBackground(backgroundColor);
        LABEL_ALT.setForeground(labelColor);
        P1_SidePanel.add(LABEL_ALT);
        
        
        JLabel LABEL_VEL = new JLabel(" Velocity [m/s]");
        LABEL_VEL.setLocation(65, uy_p41 + 75 + y_ext_vel);
        LABEL_VEL.setSize(150, 20);
        LABEL_VEL.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        LABEL_VEL.setFont(small_font);
        //LABEL_VEL.setBorder(Moon_border);
        LABEL_VEL.setBackground(backgroundColor);
        LABEL_VEL.setForeground(labelColor);
        P1_SidePanel.add(LABEL_VEL);
        JLabel LABEL_FPA = new JLabel(" Flight Path Angle [deg]");
        LABEL_FPA.setLocation(65, uy_p41 + 100 + y_ext_vel);
        LABEL_FPA.setSize(150, 20);
        LABEL_FPA.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        LABEL_FPA.setFont(small_font);
        //LABEL_FPA.setBorder(Moon_border);
        LABEL_FPA.setBackground(backgroundColor);
        LABEL_FPA.setForeground(labelColor);
        P1_SidePanel.add(LABEL_FPA);
        JLabel LABEL_AZI = new JLabel(" Azimuth [deg]");
        LABEL_AZI.setLocation(65, uy_p41 + 125 + y_ext_vel);
        LABEL_AZI.setSize(150, 20);
        LABEL_AZI.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        LABEL_AZI.setFont(small_font);
        //LABEL_AZI.setBorder(Moon_border);
        LABEL_AZI.setBackground(backgroundColor);
        LABEL_AZI.setForeground(labelColor);
        P1_SidePanel.add(LABEL_AZI);
        
        
        JLabel LABEL_M0 = new JLabel(" Initial Mass [kg]");
        LABEL_M0.setLocation(65, uy_p41 + 150 + y_ext_vel*2);
        LABEL_M0.setSize(150, 20);
        LABEL_M0.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //LABEL_M0.setBorder(Moon_border);
        LABEL_M0.setFont(small_font);
        LABEL_M0.setBackground(backgroundColor);
        LABEL_M0.setForeground(labelColor);
        P1_SidePanel.add(LABEL_M0);
        JLabel LABEL_INTEGTIME = new JLabel(" Integration Time [s]");
        LABEL_INTEGTIME.setLocation(65, uy_p41 + 175 + y_ext_vel*2);
        LABEL_INTEGTIME.setSize(150, 20);
        LABEL_INTEGTIME.setFont(small_font);
        LABEL_INTEGTIME.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //LABEL_INTEGTIME.setBorder(Moon_border);
        LABEL_INTEGTIME.setBackground(backgroundColor);
        LABEL_INTEGTIME.setForeground(labelColor);
        P1_SidePanel.add(LABEL_INTEGTIME);
        
        Error_Indicator = new JLabel("");
        Error_Indicator.setLocation(225, uy_p41 + 25 * 9);
        Error_Indicator.setSize(150, 20);
        //Error_Indicator.setBackground(backgroundColor);
        Error_Indicator.setFont(labelfont_small);
        Error_Indicator.setForeground(labelColor);
        P1_SidePanel.add(Error_Indicator);
        
        Module_Indicator = new JLabel("");
        Module_Indicator.setLocation(225, uy_p41 + 25 * 10);
        Module_Indicator.setSize(150, 20);
        Module_Indicator.setFont(labelfont_small);
        Module_Indicator.setForeground(labelColor);
        P1_SidePanel.add(Module_Indicator);
        
         INDICATOR_LONG = new JLabel();
        INDICATOR_LONG.setLocation(2, uy_p41 + 25 * 0 );
        INDICATOR_LONG.setSize(60, 20);
        INDICATOR_LONG.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
       // INDICATOR_LONG.setBorder(Moon_border);
        INDICATOR_LONG.setBackground(backgroundColor);
        INDICATOR_LONG.setForeground(labelColor);
        INDICATOR_LONG.setHorizontalAlignment(JLabel.RIGHT);
        INDICATOR_LONG.setFont(small_font);
        P1_SidePanel.add(INDICATOR_LONG);
        INDICATOR_LAT = new JLabel();
        INDICATOR_LAT.setLocation(2, uy_p41 + 25 * 1 );
        INDICATOR_LAT.setSize(60, 20);
        INDICATOR_LAT.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //INDICATOR_LAT.setBorder(Moon_border);
        INDICATOR_LAT.setBackground(backgroundColor);
        INDICATOR_LAT.setFont(small_font);
        INDICATOR_LAT.setHorizontalAlignment(JLabel.RIGHT);
        INDICATOR_LAT.setForeground(labelColor);
        P1_SidePanel.add(INDICATOR_LAT);
         INDICATOR_ALT = new JLabel();
        INDICATOR_ALT.setLocation(2, uy_p41 + 25 * 2 );
        INDICATOR_ALT.setSize(60, 20);
        INDICATOR_ALT.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //INDICATOR_ALT.setBorder(Moon_border);
        INDICATOR_ALT.setBackground(backgroundColor);
        INDICATOR_ALT.setFont(small_font);
        INDICATOR_ALT.setHorizontalAlignment(JLabel.RIGHT);
        INDICATOR_ALT.setForeground(labelColor);
        P1_SidePanel.add(INDICATOR_ALT);
        INDICATOR_VEL = new JLabel();
        INDICATOR_VEL.setLocation(2, uy_p41 + 25 * 3 + y_ext_vel);
        INDICATOR_VEL.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //INDICATOR_VEL.setBorder(Moon_border);
        INDICATOR_VEL.setBackground(backgroundColor);
        INDICATOR_VEL.setFont(small_font);
        INDICATOR_VEL.setForeground(labelColor);
        INDICATOR_VEL.setHorizontalAlignment(JLabel.RIGHT);
        INDICATOR_VEL.setSize(60, 20);
        P1_SidePanel.add(INDICATOR_VEL);
        INDICATOR_FPA = new JLabel();
        INDICATOR_FPA.setLocation(2, uy_p41 + 25 * 4 + y_ext_vel);
        INDICATOR_FPA.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //INDICATOR_FPA.setBorder(Moon_border);
        INDICATOR_FPA.setBackground(backgroundColor);
        INDICATOR_FPA.setFont(small_font);
        INDICATOR_FPA.setForeground(labelColor);
        INDICATOR_FPA.setHorizontalAlignment(JLabel.RIGHT);
        INDICATOR_FPA.setSize(60, 20);
        P1_SidePanel.add(INDICATOR_FPA);
        INDICATOR_AZI = new JLabel();
        INDICATOR_AZI.setLocation(2, uy_p41 + 25 * 5 + y_ext_vel);
        INDICATOR_AZI.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //INDICATOR_AZI.setBorder(Moon_border);
        INDICATOR_AZI.setBackground(backgroundColor);
        INDICATOR_AZI.setForeground(labelColor);
        INDICATOR_AZI.setHorizontalAlignment(JLabel.RIGHT);
        INDICATOR_AZI.setFont(small_font);
        INDICATOR_AZI.setSize(60, 20);
        P1_SidePanel.add(INDICATOR_AZI);        
        INDICATOR_M0 = new JLabel();
        INDICATOR_M0.setLocation(2, uy_p41 + 25 * 6 + y_ext_vel*2);
        INDICATOR_M0.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //INDICATOR_M0.setBorder(Moon_border);
        INDICATOR_M0.setBackground(backgroundColor);
        INDICATOR_M0.setForeground(labelColor);
        INDICATOR_M0.setHorizontalAlignment(JLabel.RIGHT);
        INDICATOR_M0.setFont(small_font);
        INDICATOR_M0.setSize(60, 20);
        P1_SidePanel.add(INDICATOR_M0);
        INDICATOR_INTEGTIME = new JLabel();
        INDICATOR_INTEGTIME.setLocation(2, uy_p41 + 25 * 7 + y_ext_vel*2);
        INDICATOR_INTEGTIME.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //INDICATOR_INTEGTIME.setBorder(Moon_border);
        INDICATOR_INTEGTIME.setBackground(backgroundColor);
        INDICATOR_INTEGTIME.setHorizontalAlignment(JLabel.RIGHT);
        INDICATOR_INTEGTIME.setForeground(labelColor);
        INDICATOR_INTEGTIME.setFont(small_font);
        INDICATOR_INTEGTIME.setSize(60, 20);
       P1_SidePanel.add(INDICATOR_INTEGTIME);
       
       
       
       
       JLabel LABEL_TARGET = new JLabel("Target Body:");
       LABEL_TARGET.setLocation(5, uy_p41 + 25 * 9  );
       LABEL_TARGET.setSize(250, 20);
       LABEL_TARGET.setBackground(backgroundColor);
       LABEL_TARGET.setForeground(labelColor);
       P1_SidePanel.add(LABEL_TARGET);
       INDICATOR_TARGET = new JLabel();
       INDICATOR_TARGET.setLocation(2, uy_p41 + 25 * 10 );
       INDICATOR_TARGET.setText("");
       INDICATOR_TARGET.setSize(100, 25);
       INDICATOR_TARGET.setBackground(backgroundColor);
       INDICATOR_TARGET.setForeground(labelColor);
       INDICATOR_TARGET.setHorizontalAlignment(SwingConstants.CENTER);
       INDICATOR_TARGET.setVerticalTextPosition(JLabel.CENTER);
       INDICATOR_TARGET.setFont(targetfont);
       INDICATOR_TARGET.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
      P1_SidePanel.add(INDICATOR_TARGET);
        
      
      JLabel LABEL_VTOUCHDOWN = new JLabel("  Touchdown velocity [m/s]");
      LABEL_VTOUCHDOWN.setLocation(55, uy_p41 + 285  + 25 *0 );
      LABEL_VTOUCHDOWN.setSize(200, 20);
      LABEL_VTOUCHDOWN.setBackground(Color.black);
      LABEL_VTOUCHDOWN.setForeground(labelColor);
      LABEL_VTOUCHDOWN.setFont(small_font);
      P1_SidePanel.add(LABEL_VTOUCHDOWN);
      JLabel LABEL_DELTAV = new JLabel("  Total D-V [m/s]");
      LABEL_DELTAV.setLocation(55, uy_p41 + 285 + 25 *1 );
      LABEL_DELTAV.setSize(200, 20);
      LABEL_DELTAV.setBackground(Color.black);
      LABEL_DELTAV.setForeground(labelColor);
      LABEL_DELTAV.setFont(small_font);
      P1_SidePanel.add(LABEL_DELTAV);
      JLabel LABEL_PROPPERC = new JLabel("  Used Propellant [kg]");
      LABEL_PROPPERC.setLocation(270, uy_p41 + 285 + 25 *0 );
      LABEL_PROPPERC.setSize(200, 20);
      LABEL_PROPPERC.setBackground(Color.black);
      LABEL_PROPPERC.setForeground(labelColor);
      LABEL_PROPPERC.setFont(small_font);
      P1_SidePanel.add(LABEL_PROPPERC);
      JLabel LABEL_RESPROP = new JLabel("  Residual Propellant [%]");
      LABEL_RESPROP.setLocation(260, uy_p41 + 285 + 25 *1 );
      LABEL_RESPROP.setSize(200, 20);
      LABEL_RESPROP.setBackground(Color.black);
      LABEL_RESPROP.setForeground(labelColor);
      LABEL_RESPROP.setFont(small_font);
      P1_SidePanel.add(LABEL_RESPROP);
      
       INDICATOR_VTOUCHDOWN = new JLabel("");
      INDICATOR_VTOUCHDOWN.setLocation(5, uy_p41 + 285  + 25 *0 );
      INDICATOR_VTOUCHDOWN.setSize(50, 20);
      INDICATOR_VTOUCHDOWN.setBackground(Color.black);
      INDICATOR_VTOUCHDOWN.setForeground(labelColor);
      P1_SidePanel.add(INDICATOR_VTOUCHDOWN);
       INDICATOR_DELTAV = new JLabel("");
      INDICATOR_DELTAV.setLocation(5, uy_p41 + 285 + 25 *1 );
      INDICATOR_DELTAV.setSize(50, 20);
      INDICATOR_DELTAV.setBackground(Color.black);
      INDICATOR_DELTAV.setForeground(labelColor);
      P1_SidePanel.add(INDICATOR_DELTAV);
       INDICATOR_PROPPERC = new JLabel("");
      INDICATOR_PROPPERC.setLocation(225, uy_p41 + 285 + 25 *0 );
      INDICATOR_PROPPERC.setSize(50, 20);
      INDICATOR_PROPPERC.setBackground(Color.black);
      INDICATOR_PROPPERC.setForeground(labelColor);
      P1_SidePanel.add(INDICATOR_PROPPERC);
       INDICATOR_RESPROP = new JLabel("");
      INDICATOR_RESPROP.setLocation(225, uy_p41 + 285 + 25 *1 );
      INDICATOR_RESPROP.setSize(40, 20);
      INDICATOR_RESPROP.setBackground(Color.black);
      INDICATOR_RESPROP.setForeground(labelColor);
      P1_SidePanel.add(INDICATOR_RESPROP);
//-----------------------------------------------------------------------------------------------
//								Console Window        
//-----------------------------------------------------------------------------------------------
        int console_size_x = 390;
        int console_size_y = 270; 
        JLabel LABEL_CONSOLE = new JLabel("Console:");
        LABEL_CONSOLE.setLocation(5, uy_p41 + 25 *17 );
        LABEL_CONSOLE.setSize(200, 20);
        LABEL_CONSOLE.setBackground(backgroundColor);
        LABEL_CONSOLE.setForeground(labelColor);
        P1_SidePanel.add(LABEL_CONSOLE);
        
        JPanel JP_EnginModel = new JPanel();
        JP_EnginModel.setSize(console_size_x,console_size_y);
        JP_EnginModel.setLocation(5, uy_p41 + 25 * 18);
        JP_EnginModel.setBackground(backgroundColor);
        JP_EnginModel.setForeground(labelColor);
         JP_EnginModel.setBorder(BorderFactory.createEmptyBorder(1,1,1,1)); 
        //JP_EnginModel.setBackground(Color.red);
        taOutputStream = null; 
        taOutputStream = new TextAreaOutputStream(textArea, ""); 
        textArea.setForeground(labelColor);
        textArea.setBackground(backgroundColor);
        JScrollPane JSP_EnginModel = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JSP_EnginModel.setBackground(backgroundColor);
        JSP_EnginModel.setForeground(labelColor);
        JSP_EnginModel.getVerticalScrollBar().setForeground(labelColor);
        JSP_EnginModel.getHorizontalScrollBar().setForeground(labelColor);
        JSP_EnginModel.getHorizontalScrollBar().setBackground(backgroundColor);
        JSP_EnginModel.getVerticalScrollBar().setBackground(backgroundColor);
        JSP_EnginModel.setOpaque(true);
        JSP_EnginModel.setPreferredSize(new Dimension(console_size_x-5,console_size_y-10));
        JSP_EnginModel.setLocation(5, 5);
        JP_EnginModel.add(JSP_EnginModel);
        System.setOut(new PrintStream(taOutputStream));       
//-----------------------------------------------------------------------------------------------
        P1_SidePanel.add(JP_EnginModel);
//-----------------------------------------------------------------------------------------------
	  //-----------------------------------------------------------------------------------------
	  // 										Page 4.2
	  //-----------------------------------------------------------------------------------------
      JTabbedPane TabPane_SimulationSetup = (JTabbedPane) new JTabbedPane();
      TabPane_SimulationSetup.setPreferredSize(new Dimension(extx_main, exty_main));
      TabPane_SimulationSetup.setBackground(backgroundColor);
      TabPane_SimulationSetup.setForeground(labelColor);
//-------------------------------------------------------------------------------------------	    
      // Main panels for each page 
		JPanel AerodynamicSetupPanel = new JPanel();
		AerodynamicSetupPanel.setLocation(0, 0);
		AerodynamicSetupPanel.setBackground(backgroundColor);
		AerodynamicSetupPanel.setForeground(labelColor);
		AerodynamicSetupPanel.setSize(400, 600);
		AerodynamicSetupPanel.setLayout(new BorderLayout()); 
		
	      JPanel BasicAndControllerPanel = new JPanel();
	      BasicAndControllerPanel.setLocation(0, uy_p41 + 26 * 38 );
	      BasicAndControllerPanel.setPreferredSize(new Dimension(1350, 1350));
	      BasicAndControllerPanel.setBackground(backgroundColor);
	      BasicAndControllerPanel.setForeground(Color.white);
	      BasicAndControllerPanel.setLayout(new BorderLayout());
	      
			JPanel SequenceSetupPanel = new JPanel();
			SequenceSetupPanel.setLocation(0, 0);
			SequenceSetupPanel.setBackground(backgroundColor);
			SequenceSetupPanel.setForeground(labelColor);
			SequenceSetupPanel.setPreferredSize(new Dimension(1350, 1350));
			SequenceSetupPanel.setLayout(new BorderLayout());
			
			JPanel NoiseSetupPanel = new JPanel();
			NoiseSetupPanel.setLocation(0, 0);
			NoiseSetupPanel.setBackground(backgroundColor);
			NoiseSetupPanel.setForeground(labelColor);
			NoiseSetupPanel.setPreferredSize(new Dimension(1350, 1350));
			NoiseSetupPanel.setLayout(new BorderLayout());
			
			JPanel gravityModelPanel = new JPanel();
			gravityModelPanel.setLocation(0, 0);
			gravityModelPanel.setBackground(backgroundColor);
			gravityModelPanel.setForeground(labelColor);
			gravityModelPanel.setPreferredSize(new Dimension(1350, 1350));
			gravityModelPanel.setLayout(new BorderLayout());
			
		
	     	ImageIcon icon_setup2 = new ImageIcon("images/setup2.png","");
	     	ImageIcon icon_inertia = new ImageIcon("images/inertia.png","");
	     	ImageIcon icon_aerodynamic = new ImageIcon("images/aerodynamic.png","");
	     	if(OS_is==2) {
	     		// Resize image icons for Windows 
	         	 int size=10;
	         	icon_setup2 = new ImageIcon(getScaledImage(icon_setup2.getImage(),size,size));
	         	icon_inertia = new ImageIcon(getScaledImage(icon_inertia.getImage(),size,size));
	         	icon_aerodynamic = new ImageIcon(getScaledImage(icon_aerodynamic.getImage(),size,size));
	      }
	      
	     	TabPane_SimulationSetup.addTab("Basic Setup" , icon_setup2, BasicAndControllerPanel, null);
	     	TabPane_SimulationSetup.addTab("Sequence Setup" , icon_setup2, SequenceSetupPanel, null);
	     	TabPane_SimulationSetup.addTab("Aerodynamic Setup" , icon_aerodynamic, AerodynamicSetupPanel, null);
	     	TabPane_SimulationSetup.addTab("Gravity Setup" , icon_setup2, gravityModelPanel, null);
	     	TabPane_SimulationSetup.addTab("Noise and Error Model Setup" , null, NoiseSetupPanel, null);
	     	PageX04_SimSetup.add(TabPane_SimulationSetup);
		TabPane_SimulationSetup.setSelectedIndex(0);
		TabPane_SimulationSetup.setFont(small_font);
		TabPane_SimulationSetup.setForeground(Color.black);
		
		
      int INPUT_width = 110;
      int SidePanel_Width = 380; 
      
      basicSidePanelLeft = new SidePanelLeft();
      
      JPanel PANEL_RIGHT_InputSection = new JPanel();
      PANEL_RIGHT_InputSection.setLayout(new BorderLayout());
      PANEL_RIGHT_InputSection.setPreferredSize(new Dimension(405, exty_main));
      PANEL_RIGHT_InputSection.setBackground(backgroundColor);
      PANEL_RIGHT_InputSection.setForeground(labelColor);
      
      JScrollPane scrollPane_LEFT_InputSection = new JScrollPane(basicSidePanelLeft.getMainPanel(),JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
      scrollPane_LEFT_InputSection.setPreferredSize(new Dimension(SidePanel_Width+25, exty_main));
      scrollPane_LEFT_InputSection.getVerticalScrollBar().setUnitIncrement(16);
      BasicAndControllerPanel.add(scrollPane_LEFT_InputSection, BorderLayout.LINE_START);
      JScrollPane scrollPane_RIGHT_InputSection = new JScrollPane(PANEL_RIGHT_InputSection,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
      scrollPane_RIGHT_InputSection.getVerticalScrollBar().setUnitIncrement(16);
      BasicAndControllerPanel.add(scrollPane_RIGHT_InputSection, BorderLayout.CENTER);

      
    //------------------------------------------------------------------------------------------------------------------
	  //   Right side :
    JPanel P2_ControllerPane = new JPanel();
   // P2_ControllerPane.setLayout(null);
    //P2_ControllerPane.setPreferredSize(new Dimension((exty_main+400),290));
    P2_ControllerPane.setBackground(backgroundColor);
    P2_ControllerPane.setForeground(labelColor);
    
    //-----------------------------------------------------------------------------------------------------------------------------
    //                  Additional Setup 
    //-----------------------------------------------------------------------------------------------------------------------------
    JPanel P2_SequenceMAIN = new JPanel();
    P2_SequenceMAIN.setLayout(new BorderLayout());
    P2_SequenceMAIN.setPreferredSize(new Dimension(960, 900));
    P2_SequenceMAIN.setBackground(backgroundColor);
    P2_SequenceMAIN.setForeground(labelColor);
    P2_SequenceMAIN.setLayout(null);
    PANEL_RIGHT_InputSection.add(P2_SequenceMAIN, BorderLayout.PAGE_START);
    
    targetWindow = new JPanel();
    targetWindow.setSize(300, 300);
    targetWindow.setLocation(540, uy_p41 + 25 * 0   );
    targetWindow.setBackground(Color.RED);
    targetWindow.setForeground(labelColor);
    targetWindow.setBorder(Mars_border);
    targetWindow.setLayout(new BorderLayout());
    P2_SequenceMAIN.add(targetWindow);
    
    JLabel LABEL_TARGETBODY = new JLabel("Target Body");
    LABEL_TARGETBODY.setLocation(163, uy_p41 + 25 * 2   );
    LABEL_TARGETBODY.setSize(150, 20);
    LABEL_TARGETBODY.setBackground(backgroundColor);
    LABEL_TARGETBODY.setForeground(labelColor);
    P2_SequenceMAIN.add(LABEL_TARGETBODY);
    
	  Target_chooser = new JComboBox(Target_Options);
	 // Target_chooser.setBackground(backgroundColor);
	  Target_chooser.setLocation(2, uy_p41 + 25 * 2 );
	  Target_chooser.setSize(150,25);
	  Target_chooser.setRenderer(new CustomRenderer());
	  Target_chooser.setSelectedIndex(3);
	  Target_chooser.addActionListener(new ActionListener() { 
    	  public void actionPerformed(ActionEvent e) {
  			indx_target= Target_chooser.getSelectedIndex();
			 refreshTargetWindow();
    	  }
  	  } );
	  Target_chooser.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			// TODO Auto-generated method stub
			indx_target= Target_chooser.getSelectedIndex();
			 WRITE_INIT();
			 refreshTargetWindow();
		}
		  
	  });
	  P2_SequenceMAIN.add(Target_chooser);
    
    
    JLabel LABEL_VCoordinateSystem = new JLabel("Select Coordinate System to solve the Velocity Vector");
    LABEL_VCoordinateSystem.setLocation(5, uy_p41 + 25 * 3   );
    LABEL_VCoordinateSystem.setSize(350, 20);
    LABEL_VCoordinateSystem.setBackground(backgroundColor);
    LABEL_VCoordinateSystem.setForeground(labelColor);
    P2_SequenceMAIN.add(LABEL_VCoordinateSystem);
    
    SELECT_VelocityCartesian =new JRadioButton("Cartesian Velocity Coordinates");    
    SELECT_VelocitySpherical =new JRadioButton("Spherical Velocity Coordinates");      
    SELECT_VelocitySpherical.setLocation(5, uy_p41 + 25 * 4 );
    SELECT_VelocitySpherical.setSize(220,20);
    SELECT_VelocitySpherical.setBackground(backgroundColor);
    SELECT_VelocitySpherical.setForeground(labelColor);
    SELECT_VelocitySpherical.setFont(small_font);
    SELECT_VelocityCartesian.setLocation(5, uy_p41 + 25 * 5);
    SELECT_VelocityCartesian.setSize(220,20);
    SELECT_VelocityCartesian.setBackground(backgroundColor);
    SELECT_VelocityCartesian.setForeground(labelColor);
    SELECT_VelocityCartesian.setFont(small_font);
   ButtonGroup bg_velocity=new ButtonGroup();    
   bg_velocity.add(SELECT_VelocitySpherical);
   bg_velocity.add(SELECT_VelocityCartesian); 
   P2_SequenceMAIN.add(SELECT_VelocitySpherical);
   P2_SequenceMAIN.add(SELECT_VelocityCartesian);
   SELECT_VelocitySpherical.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if(SELECT_VelocitySpherical.isSelected()) {
				VelocityCoordinateSystem = 1;
			} else if (SELECT_VelocityCartesian.isSelected()) {
				VelocityCoordinateSystem = 2;
			}
			WRITE_INIT();
		}
   });
   SELECT_VelocityCartesian.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if(SELECT_VelocitySpherical.isSelected()) {
				VelocityCoordinateSystem = 1;
			} else if (SELECT_VelocityCartesian.isSelected()) {
				VelocityCoordinateSystem = 2;
			}
			WRITE_INIT();
		}
  	 
   });
   
   JLabel LABEL_SelectDoF = new JLabel("Select Degrees of Freedom");
   LABEL_SelectDoF.setLocation(5, uy_p41 + 25 * 6   );
   LABEL_SelectDoF.setSize(350, 20);
   LABEL_SelectDoF.setBackground(backgroundColor);
   LABEL_SelectDoF.setForeground(labelColor);
   P2_SequenceMAIN.add(LABEL_SelectDoF);
   
   SELECT_3DOF =new JRadioButton("3DOF Model");    
   SELECT_6DOF =new JRadioButton("6DOF Model");      
   SELECT_3DOF.setLocation(5, uy_p41 + 25 * 7 );
   SELECT_3DOF.setSize(220,20);
   SELECT_3DOF.setBackground(backgroundColor);
   SELECT_3DOF.setForeground(labelColor);
   SELECT_3DOF.setFont(small_font);
   SELECT_6DOF.setLocation(5, uy_p41 + 25 * 8);
   SELECT_6DOF.setSize(220,20);
   SELECT_6DOF.setBackground(backgroundColor);
   SELECT_6DOF.setForeground(labelColor);
   SELECT_6DOF.setFont(small_font);
  ButtonGroup bg_dof=new ButtonGroup();    
  bg_dof.add(SELECT_3DOF);
  bg_dof.add(SELECT_6DOF); 
  P2_SequenceMAIN.add(SELECT_3DOF);
  P2_SequenceMAIN.add(SELECT_6DOF);
  SELECT_3DOF.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(SELECT_3DOF.isSelected()) {
				DOF_System = 3;
			} else if (SELECT_6DOF.isSelected()) {
				DOF_System = 6;
			}
			WRITE_INIT();
		}
 	 
  });
  SELECT_6DOF.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(SELECT_3DOF.isSelected()) {
				DOF_System = 3;
			} else if (SELECT_6DOF.isSelected()) {
				DOF_System = 6;
			}
			WRITE_INIT();
		}
 	 
  }); 
  
  JLabel LABEL_ControllerFrequency = new JLabel("Set Control Loop Frequency [Hz]");
  LABEL_ControllerFrequency.setLocation(INPUT_width, uy_p41 + 25 * 10 );
  LABEL_ControllerFrequency.setSize(300, 20);
  LABEL_ControllerFrequency.setBackground(backgroundColor);
  LABEL_ControllerFrequency.setForeground(labelColor);
  P2_SequenceMAIN.add(LABEL_ControllerFrequency);
 
 
  
  INPUT_ControllerFrequency = new JTextField(10);
  INPUT_ControllerFrequency.setLocation(2, uy_p41 + 25 * 10);
  INPUT_ControllerFrequency.setSize(INPUT_width-20, 20);
  //INPUT_M0.setBackground(backgroundColor);
  //INPUT_M0.setForeground(labelColor);
  INPUT_ControllerFrequency.setHorizontalAlignment(JTextField.RIGHT);
  INPUT_ControllerFrequency.addFocusListener(new FocusListener() {

	@Override
	public void focusGained(FocusEvent arg0) { }

	@Override
	public void focusLost(FocusEvent e) {
		WRITE_INIT();
	}
	  
  });
  P2_SequenceMAIN.add(INPUT_ControllerFrequency);
  
  JLabel LABEL_GlobalFrequency = new JLabel("Set Global Result Frequency [Hz]");
  LABEL_GlobalFrequency.setLocation(INPUT_width, uy_p41 + 25 * 11 );
  LABEL_GlobalFrequency.setSize(300, 20);
  LABEL_GlobalFrequency.setBackground(backgroundColor);
  LABEL_GlobalFrequency.setForeground(labelColor);
  P2_SequenceMAIN.add(LABEL_GlobalFrequency);
 
 
  
  INPUT_GlobalFrequency = new JTextField(10);
  INPUT_GlobalFrequency.setLocation(2, uy_p41 + 25 * 11);
  INPUT_GlobalFrequency.setSize(INPUT_width-20, 20);
  //INPUT_M0.setBackground(backgroundColor);
  //INPUT_M0.setForeground(labelColor);
  INPUT_GlobalFrequency.setEditable(false);
  INPUT_GlobalFrequency.setHorizontalAlignment(JTextField.RIGHT);
  INPUT_GlobalFrequency.addFocusListener(new FocusListener() {

	@Override
	public void focusGained(FocusEvent arg0) { }

	@Override
	public void focusLost(FocusEvent e) {
		WRITE_INIT();
	}
	  
  });
  P2_SequenceMAIN.add(INPUT_GlobalFrequency);
  
  JLabel LABEL_IntegTime = new JLabel("Set Maximum Simulation Time [s]");
  LABEL_IntegTime.setLocation(INPUT_width, uy_p41 + 25 * 12 );
  LABEL_IntegTime.setSize(300, 20);
  LABEL_IntegTime.setBackground(backgroundColor);
  LABEL_IntegTime.setForeground(labelColor);
  P2_SequenceMAIN.add(LABEL_IntegTime);
 
 
  
  INPUT_GlobalTime = new JTextField(10);
  INPUT_GlobalTime.setLocation(2, uy_p41 + 25 * 12);
  INPUT_GlobalTime.setSize(INPUT_width-20, 20);
  //INPUT_M0.setBackground(backgroundColor);
  //INPUT_M0.setForeground(labelColor);
  INPUT_GlobalTime.setHorizontalAlignment(JTextField.RIGHT);
  INPUT_GlobalTime.addFocusListener(new FocusListener() {

	@Override
	public void focusGained(FocusEvent arg0) { }

	@Override
	public void focusLost(FocusEvent e) {
		WRITE_INIT();
	}
	  
  });
  P2_SequenceMAIN.add(INPUT_GlobalTime);
    
  JLabel LABEL_InitAttitude = new JLabel("Initial Attitude:");
  LABEL_InitAttitude.setLocation(2, uy_p41 + 25 * 13 );
  LABEL_InitAttitude.setSize(300, 20);
  LABEL_InitAttitude.setBackground(backgroundColor);
  LABEL_InitAttitude.setForeground(labelColor);
  P2_SequenceMAIN.add(LABEL_InitAttitude);
    
//------------------------------------------------------------------------------------
  // Initial 
  
	JPanel InitialAttitudePanelMain = new JPanel();
	InitialAttitudePanelMain.setLayout(null);
	InitialAttitudePanelMain.setLocation(2, uy_p41 + 25 * 14);
	InitialAttitudePanelMain.setBackground(backgroundColor);
	InitialAttitudePanelMain.setForeground(labelColor);
	//InitialAttitudePanelMain.setBorder(Moon_border);
	InitialAttitudePanelMain.setSize(900, 430);
	P2_SequenceMAIN.add(InitialAttitudePanelMain);
  
  int box_size_InitialAttitude_x = 130;
  int box_size_InitialAttitude_y = 25;
	int box_size_x = 60;
	int box_size_y = 25;
	int gap_size_x =  4;
	int gap_size_y =  15;
  
	JPanel InitialAttitudePanel = new JPanel();
	InitialAttitudePanel.setLayout(null);
	InitialAttitudePanel.setLocation(2, 5);
	InitialAttitudePanel.setBackground(backgroundColor);
	InitialAttitudePanel.setForeground(labelColor);
	InitialAttitudePanel.setBorder(Moon_border);
	InitialAttitudePanel.setSize(400, 400);
	InitialAttitudePanelMain.add(InitialAttitudePanel);
	
      JLabel LABEL_Quarternions = new JLabel("Quarternion Representation");
      LABEL_Quarternions.setLocation(2, 2);
      LABEL_Quarternions.setSize(150, 20);
      LABEL_Quarternions.setBackground(backgroundColor);
      LABEL_Quarternions.setForeground(labelColor);
      LABEL_Quarternions.setFont(small_font);
      LABEL_Quarternions.setHorizontalAlignment(0);
      InitialAttitudePanel.add(LABEL_Quarternions);
	
      JLabel LABEL_Quarternion1 = new JLabel("Quarternion e1");
      LABEL_Quarternion1.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*0 - 15+45);
      LABEL_Quarternion1.setSize(box_size_InitialAttitude_x, 20);
      LABEL_Quarternion1.setBackground(backgroundColor);
      LABEL_Quarternion1.setForeground(labelColor);
      LABEL_Quarternion1.setFont(small_font);
      LABEL_Quarternion1.setHorizontalAlignment(0);
      InitialAttitudePanel.add(LABEL_Quarternion1);

    
    INPUT_Quarternion1 = new JTextField();
    INPUT_Quarternion1.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*0+45);
    INPUT_Quarternion1.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
    INPUT_Quarternion1.setBorder(Moon_border);
    INPUT_Quarternion1.setSize(box_size_InitialAttitude_x, box_size_InitialAttitude_y);
    INPUT_Quarternion1.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			WriteInitialAttitude();
			//------------------------------------------------------------------------------
			double[][] qvector= {{Double.parseDouble(INPUT_Quarternion1.getText())},
								 {Double.parseDouble(INPUT_Quarternion2.getText())},
								 {Double.parseDouble(INPUT_Quarternion3.getText())},
								 {Double.parseDouble(INPUT_Quarternion4.getText())}};
			double[][] EulerAngles = Mathbox.Quaternions2Euler2(qvector);
			DecimalFormat numberFormat = new DecimalFormat("#.0000000");
			INPUT_Euler1.setText(numberFormat.format(EulerAngles[0][0]*rad2deg));
			INPUT_Euler2.setText(numberFormat.format(EulerAngles[1][0]*rad2deg));
			INPUT_Euler3.setText(numberFormat.format(EulerAngles[2][0]*rad2deg));
			//------------------------------------------------------------------------------
		}
    	  
      });
    InitialAttitudePanel.add(INPUT_Quarternion1);	
    
      JLabel LABEL_Quarternion2 = new JLabel("Quarternion e2");
      LABEL_Quarternion2.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*1 - 15+45);
      LABEL_Quarternion2.setSize(box_size_InitialAttitude_x, 20);
      LABEL_Quarternion2.setBackground(backgroundColor);
      LABEL_Quarternion2.setForeground(labelColor);
      LABEL_Quarternion2.setFont(small_font);
      LABEL_Quarternion2.setHorizontalAlignment(0);
      InitialAttitudePanel.add(LABEL_Quarternion2);

    
    INPUT_Quarternion2 = new JTextField();
    INPUT_Quarternion2.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*1+45);
    INPUT_Quarternion2.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
    INPUT_Quarternion2.setBorder(Moon_border);
    INPUT_Quarternion2.setSize(box_size_InitialAttitude_x, box_size_InitialAttitude_y);
    INPUT_Quarternion2.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			WriteInitialAttitude();
			//------------------------------------------------------------------------------
			double[][] qvector= {{Double.parseDouble(INPUT_Quarternion1.getText())},
								 {Double.parseDouble(INPUT_Quarternion2.getText())},
								 {Double.parseDouble(INPUT_Quarternion3.getText())},
								 {Double.parseDouble(INPUT_Quarternion4.getText())}};
			double[][] EulerAngles = Mathbox.Quaternions2Euler2(qvector);
			DecimalFormat numberFormat = new DecimalFormat("#.0000000");
			INPUT_Euler1.setText(numberFormat.format(EulerAngles[0][0]*rad2deg));
			INPUT_Euler2.setText(numberFormat.format(EulerAngles[1][0]*rad2deg));
			INPUT_Euler3.setText(numberFormat.format(EulerAngles[2][0]*rad2deg));
			//------------------------------------------------------------------------------
		}
    	  
      });
    InitialAttitudePanel.add(INPUT_Quarternion2);
    
      JLabel LABEL_Quarternion3 = new JLabel("Quarternion e3");
      LABEL_Quarternion3.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*2 - 15+45);
      LABEL_Quarternion3.setSize(box_size_InitialAttitude_x, 20);
      LABEL_Quarternion3.setBackground(backgroundColor);
      LABEL_Quarternion3.setForeground(labelColor);
      LABEL_Quarternion3.setFont(small_font);
      LABEL_Quarternion3.setHorizontalAlignment(0);
      InitialAttitudePanel.add(LABEL_Quarternion3);

    
    INPUT_Quarternion3 = new JTextField();
    INPUT_Quarternion3.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*2+45);
    INPUT_Quarternion3.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
    INPUT_Quarternion3.setBorder(Moon_border);
    INPUT_Quarternion3.setSize(box_size_InitialAttitude_x, box_size_InitialAttitude_y);
    INPUT_Quarternion3.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			WriteInitialAttitude();
			//------------------------------------------------------------------------------
			double[][] qvector= {{Double.parseDouble(INPUT_Quarternion1.getText())},
								 {Double.parseDouble(INPUT_Quarternion2.getText())},
								 {Double.parseDouble(INPUT_Quarternion3.getText())},
								 {Double.parseDouble(INPUT_Quarternion4.getText())}};
			double[][] EulerAngles = Mathbox.Quaternions2Euler2(qvector);
			DecimalFormat numberFormat = new DecimalFormat("#.0000000");
			INPUT_Euler1.setText(numberFormat.format(EulerAngles[0][0]*rad2deg));
			INPUT_Euler2.setText(numberFormat.format(EulerAngles[1][0]*rad2deg));
			INPUT_Euler3.setText(numberFormat.format(EulerAngles[2][0]*rad2deg));
			//------------------------------------------------------------------------------
		}
    	  
      });
    InitialAttitudePanel.add(INPUT_Quarternion3);
    
      JLabel LABEL_Quarternion4 = new JLabel("Quarternion e4");
      LABEL_Quarternion4.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*3 - 15+45);
      LABEL_Quarternion4.setSize(box_size_InitialAttitude_x, 20);
      LABEL_Quarternion4.setBackground(backgroundColor);
      LABEL_Quarternion4.setForeground(labelColor);
      LABEL_Quarternion4.setFont(small_font);
      LABEL_Quarternion4.setHorizontalAlignment(0);
      InitialAttitudePanel.add(LABEL_Quarternion4);

    
    INPUT_Quarternion4 = new JTextField();
    INPUT_Quarternion4.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*3+45);
    INPUT_Quarternion4.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
    INPUT_Quarternion4.setBorder(Moon_border);
    INPUT_Quarternion4.setSize(box_size_InitialAttitude_x, box_size_InitialAttitude_y);
    INPUT_Quarternion4.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			WriteInitialAttitude();
			//------------------------------------------------------------------------------
			double[][] qvector= {{Double.parseDouble(INPUT_Quarternion1.getText())},
								 {Double.parseDouble(INPUT_Quarternion2.getText())},
								 {Double.parseDouble(INPUT_Quarternion3.getText())},
								 {Double.parseDouble(INPUT_Quarternion4.getText())}};
			double[][] EulerAngles = Mathbox.Quaternions2Euler2(qvector);
			DecimalFormat numberFormat = new DecimalFormat("#.0000000");
			INPUT_Euler1.setText(numberFormat.format(EulerAngles[0][0]*rad2deg));
			INPUT_Euler2.setText(numberFormat.format(EulerAngles[1][0]*rad2deg));
			INPUT_Euler3.setText(numberFormat.format(EulerAngles[2][0]*rad2deg));
			//------------------------------------------------------------------------------
		}
    	  
      });
    InitialAttitudePanel.add(INPUT_Quarternion4);
    
    
      JLabel LABEL_Euler = new JLabel("Euler Angle Representation");
      LABEL_Euler.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*4 +45);
      LABEL_Euler.setSize(150, 20);
      LABEL_Euler.setBackground(backgroundColor);
      LABEL_Euler.setForeground(labelColor);
      LABEL_Euler.setFont(small_font);
      LABEL_Euler.setHorizontalAlignment(0);
      InitialAttitudePanel.add(LABEL_Euler);
    
      JLabel LABEL_Euler1 = new JLabel("Euler E1 - Roll [deg]");
      LABEL_Euler1.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*5 - 15+45);
      LABEL_Euler1.setSize(box_size_InitialAttitude_x, 20);
      LABEL_Euler1.setBackground(backgroundColor);
      LABEL_Euler1.setForeground(labelColor);
      LABEL_Euler1.setFont(small_font);
      LABEL_Euler1.setHorizontalAlignment(0);
      InitialAttitudePanel.add(LABEL_Euler1);

         sliderEuler1 = GuiComponents.getGuiSlider(small_font, (int) (box_size_InitialAttitude_x*1.9), -180, 0 ,180);
         sliderEuler2 = GuiComponents.getGuiSlider(small_font, (int) (box_size_InitialAttitude_x*1.9), -90, 0 ,90);
         sliderEuler3 = GuiComponents.getGuiSlider(small_font, (int) (box_size_InitialAttitude_x*1.9), -180, 0 ,180);
        sliderEuler1.setValue(0);
        sliderEuler2.setValue(0);
        sliderEuler3.setValue(0);
        sliderEuler1.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*1, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*5-10+45);
       
       sliderEuler1.addChangeListener(new ChangeListener() {

		@Override
		public void stateChanged(ChangeEvent arg0) {
			
			double[][] Euler = {{0},{0},{0}};
			Euler[0][0] = Double.parseDouble(INPUT_Euler1.getText());
			Euler[1][0] = Double.parseDouble(INPUT_Euler2.getText());
			Euler[2][0] = Double.parseDouble(INPUT_Euler3.getText());
			double[][] Quarternions = Mathbox.Euler2Quarternions(Euler);
			INPUT_Quarternion1.setText(""+decQuarternion.format(Quarternions[0][0]));
			INPUT_Quarternion2.setText(""+decQuarternion.format(Quarternions[1][0]));
			INPUT_Quarternion3.setText(""+decQuarternion.format(Quarternions[2][0]));
			INPUT_Quarternion4.setText(""+decQuarternion.format(Quarternions[3][0]));
			WriteInitialAttitude();
			INPUT_Euler1.setText(""+sliderEuler1.getValue());
		}
    	   
       });
        InitialAttitudePanel.add(sliderEuler1);
       
        sliderEuler2.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*1, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*6-10+45);
	       sliderEuler2.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent arg0) {
					
					double[][] Euler = {{0},{0},{0}};
					Euler[0][0] = Double.parseDouble(INPUT_Euler1.getText());
					Euler[1][0] = Double.parseDouble(INPUT_Euler2.getText());
					Euler[2][0] = Double.parseDouble(INPUT_Euler3.getText());
					double[][] Quarternions = Mathbox.Euler2Quarternions(Euler);
					INPUT_Quarternion1.setText(""+decQuarternion.format(Quarternions[0][0]));
					INPUT_Quarternion2.setText(""+decQuarternion.format(Quarternions[1][0]));
					INPUT_Quarternion3.setText(""+decQuarternion.format(Quarternions[2][0]));
					INPUT_Quarternion4.setText(""+decQuarternion.format(Quarternions[3][0]));
					WriteInitialAttitude();
					INPUT_Euler2.setText(""+sliderEuler2.getValue());
				}
		    	   
		       });
        InitialAttitudePanel.add(sliderEuler2);
        
        sliderEuler3.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*1, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*7-10+45);
	       sliderEuler3.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent arg0) {
					
					double[][] Euler = {{0},{0},{0}};
					Euler[0][0] = Double.parseDouble(INPUT_Euler1.getText());
					Euler[1][0] = Double.parseDouble(INPUT_Euler2.getText());
					Euler[2][0] = Double.parseDouble(INPUT_Euler3.getText());
					double[][] Quarternions = Mathbox.Euler2Quarternions(Euler);
					INPUT_Quarternion1.setText(""+decQuarternion.format(Quarternions[0][0]));
					INPUT_Quarternion2.setText(""+decQuarternion.format(Quarternions[1][0]));
					INPUT_Quarternion3.setText(""+decQuarternion.format(Quarternions[2][0]));
					INPUT_Quarternion4.setText(""+decQuarternion.format(Quarternions[3][0]));
					WriteInitialAttitude();
					INPUT_Euler3.setText(""+sliderEuler3.getValue());
				}
		    	   
		       });
        InitialAttitudePanel.add(sliderEuler3);
    
    INPUT_Euler1 = new JTextField();
    INPUT_Euler1.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*5+45);
    INPUT_Euler1.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
    INPUT_Euler1.setBorder(Moon_border);
    INPUT_Euler1.setText("0");
    INPUT_Euler1.setSize(box_size_InitialAttitude_x, box_size_InitialAttitude_y);
    INPUT_Euler1.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			WriteInitialAttitude();
			sliderEuler1.setValue(Integer.parseInt(INPUT_Euler1.getText()));
		}
    	  
      });
    INPUT_Euler1.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			WriteInitialAttitude();
			if( Double.parseDouble(INPUT_Euler1.getText())>=-90 && Double.parseDouble(INPUT_Euler1.getText())<=90) {
			sliderEuler1.setValue((int) Double.parseDouble(INPUT_Euler1.getText()));
			}
		}
    	
    });
    InitialAttitudePanel.add(INPUT_Euler1);
    
    
      JLabel LABEL_Euler2 = new JLabel("Euler E2 - Pitch [deg]");
      LABEL_Euler2.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*6 - 15+45);
      LABEL_Euler2.setSize(box_size_InitialAttitude_x, 20);
      LABEL_Euler2.setBackground(backgroundColor);
      LABEL_Euler2.setForeground(labelColor);
      LABEL_Euler2.setFont(small_font);
      LABEL_Euler2.setHorizontalAlignment(0);
      InitialAttitudePanel.add(LABEL_Euler2);

    
    INPUT_Euler2 = new JTextField();
    INPUT_Euler2.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*6+45);
    INPUT_Euler2.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
    INPUT_Euler2.setBorder(Moon_border);
    INPUT_Euler2.setText("0");
    INPUT_Euler2.setSize(box_size_InitialAttitude_x, box_size_InitialAttitude_y);
    INPUT_Euler2.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			WriteInitialAttitude();
			sliderEuler2.setValue(Integer.parseInt(INPUT_Euler2.getText()));
		}
    	  
      });
    INPUT_Euler2.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			WriteInitialAttitude();
			if( Double.parseDouble(INPUT_Euler2.getText())>=-90 && Double.parseDouble(INPUT_Euler2.getText())<=90) {
			sliderEuler2.setValue((int) Double.parseDouble(INPUT_Euler2.getText()));
			}
		}
    	
    });
    InitialAttitudePanel.add(INPUT_Euler2);
    
    
      JLabel LABEL_Euler3 = new JLabel("Euler E3 - Yaw [deg]");
      LABEL_Euler3.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*7 - 15+45);
      LABEL_Euler3.setSize(box_size_InitialAttitude_x, 20);
      LABEL_Euler3.setBackground(backgroundColor);
      LABEL_Euler3.setForeground(labelColor);
      LABEL_Euler3.setFont(small_font);
      LABEL_Euler3.setHorizontalAlignment(0);
      InitialAttitudePanel.add(LABEL_Euler3);

    
    INPUT_Euler3 = new JTextField();
    INPUT_Euler3.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*7+45);
    INPUT_Euler3.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
    INPUT_Euler3.setBorder(Moon_border);
    INPUT_Euler3.setText("0");
    INPUT_Euler3.setSize(box_size_InitialAttitude_x, box_size_InitialAttitude_y);
    INPUT_Euler3.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			WriteInitialAttitude();
			sliderEuler3.setValue(Integer.parseInt(INPUT_Euler3.getText()));
		}
    	  
      });
    INPUT_Euler3.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			WriteInitialAttitude();
			if( Double.parseDouble(INPUT_Euler3.getText())>=-90 && Double.parseDouble(INPUT_Euler3.getText())<=90) {
			sliderEuler3.setValue((int) Double.parseDouble(INPUT_Euler3.getText()));
			}
		}
    	
    });
    InitialAttitudePanel.add(INPUT_Euler3);
    
	JPanel SpaceShip3DPanel = new JPanel();
	SpaceShip3DPanel.setLayout(new BorderLayout());
	SpaceShip3DPanel.setLocation(415, 5);
	//SpaceShip3DPanel.setBackground(backgroundColor);
	//SpaceShip3DPanel.setForeground(labelColor);
	SpaceShip3DPanel.setSize(450, 400);
	SpaceShip3DPanel.setBorder(Moon_border);
	InitialAttitudePanelMain.add(SpaceShip3DPanel);
	
	
    final JFXPanel fxPanel = new JFXPanel();
    SpaceShip3DPanel.add(fxPanel, BorderLayout.CENTER);
    Platform.runLater(new Runnable() {
        @Override
        public void run() {
        	SpaceShipView3D.start(fxPanel);
        }
   });
    

				//-----------------------------------------------------------------------------------------
			    // ---->>>>>                       TAB: Aerodynamic Setup Sim sided
				//-----------------------------------------------------------------------------------------		    
			    
			    
				JPanel SequenceRightPanel = new JPanel();
				SequenceRightPanel.setLocation(0, 0);
				SequenceRightPanel.setBackground(backgroundColor);
				SequenceRightPanel.setForeground(labelColor);
				SequenceRightPanel.setSize(400, 600);
				SequenceRightPanel.setLayout(null); 

			   
			    SequenceLeftPanel = new JPanel();
				SequenceLeftPanel.setLocation(0, 0);
				SequenceLeftPanel.setBackground(backgroundColor);
				SequenceLeftPanel.setForeground(labelColor);
				SequenceLeftPanel.setPreferredSize(new Dimension(sequenceDimensionWidth, 660));
				SequenceLeftPanel.setLayout(null); 
			    	
			  
			      JScrollPane ScrollPaneSequenceInput = new JScrollPane(SequenceLeftPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			    		  																	  JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			      ScrollPaneSequenceInput.getVerticalScrollBar().setUnitIncrement(16);
			      ScrollPaneSequenceInput.getHorizontalScrollBar().setUnitIncrement(16);
			      SequenceSetupPanel.add(ScrollPaneSequenceInput, BorderLayout.CENTER);

			      
				    SequenceProgressBar = new JPanel();
				    SequenceProgressBar.setLocation(0, 370);
				    SequenceProgressBar.setBackground(backgroundColor);
				    SequenceProgressBar.setForeground(labelColor);
				    SequenceProgressBar.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1));
				    SequenceProgressBar.setSize(sequenceDimensionWidth, 20);
				    SequenceProgressBar.setLayout(null); 
				    SequenceLeftPanel.add(SequenceProgressBar);
				    
				    JPanel SequenceControlBar = new JPanel();
				    SequenceControlBar.setLocation(5, 5);
				    SequenceControlBar.setBackground(backgroundColor);
				    SequenceControlBar.setForeground(labelColor);
				    SequenceControlBar.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
				    SequenceControlBar.setSize(500, 50);
				    SequenceControlBar.setLayout(null); 
				    SequenceLeftPanel.add(SequenceControlBar);
				    
				    JLabel SequenceTitle = new JLabel("Sequence Setup");
				    SequenceTitle.setLocation(2, 2);
				    SequenceTitle.setBackground(backgroundColor);
				    SequenceTitle.setForeground(labelColor);
				    SequenceTitle.setSize(150, 20);
				    SequenceControlBar.add(SequenceTitle);
				    
			        JButton SequenceToTheLeftButton = new JButton("");
			        SequenceToTheLeftButton.setLocation(5, 25);
			        SequenceToTheLeftButton.setSize(80,20);
			        SequenceToTheLeftButton.setForeground(BlueBookVisual.getBackgroundColor());
			        SequenceToTheLeftButton.setBackground(BlueBookVisual.getLabelColor());
			        SequenceToTheLeftButton.setOpaque(true);
			        SequenceToTheLeftButton.setBorderPainted(false);
			        SequenceToTheLeftButton.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							// TODO Auto-generated method stub
	        				for(int i=0;i<sequenceContentList.size();i++) {
								if(sequenceContentList.get(i).isSelected()) {
        							System.out.println("Sequence "+i+" selected.");
        						}
								System.out.println(i+"|"+sequenceContentList.get(i).getSequenceID());
	        				}
						} 
			        	
			        });
			        SequenceControlBar.add(SequenceToTheLeftButton);
				    
			      int globalLeftGap = 30;
			      int globalTopGap = 100;
			      GUISequenceElement sequenceElement1 = new GUISequenceElement(sequenceContentList.size());
			      sequenceContentList.add(sequenceElement1);
			      sequenceContentList.get(0).getMasterPanel().setLocation(globalLeftGap, globalTopGap);
			      SequenceLeftPanel.add(sequenceContentList.get(0).getMasterPanel());
				//-----------------------------------------------------------------------------------------
			    // ---->>>>>                       TAB: Aerodynamic Setup Sim sided
				//-----------------------------------------------------------------------------------------		    
			    
				JPanel AerodynamicRightPanel = new JPanel();
				AerodynamicRightPanel.setLocation(0, 0);
				AerodynamicRightPanel.setBackground(backgroundColor);
				AerodynamicRightPanel.setForeground(labelColor);
				AerodynamicRightPanel.setSize(400, 600);
				AerodynamicRightPanel.setLayout(null); 

			   
				JPanel AerodynamicLeftPanel = new JPanel();
			    AerodynamicLeftPanel.setLocation(0, 0);
			    AerodynamicLeftPanel.setBackground(backgroundColor);
			    AerodynamicLeftPanel.setForeground(labelColor);
			    AerodynamicLeftPanel.setSize(400, 600);
			    AerodynamicLeftPanel.setLayout(null); 
			    
				JPanel AerodynamicDragPanel = new JPanel();
				AerodynamicDragPanel.setLocation(0, 0);
				AerodynamicDragPanel.setBackground(backgroundColor);
				AerodynamicDragPanel.setForeground(labelColor);
				AerodynamicDragPanel.setSize(400, 150);
				AerodynamicDragPanel.setLayout(null); 
				AerodynamicLeftPanel.add(AerodynamicDragPanel);
				
			    JPanel AerodynamicParachutePanel = new JPanel();
				AerodynamicParachutePanel.setLocation(0, (int) AerodynamicDragPanel.getSize().getHeight());
				AerodynamicParachutePanel.setBackground(backgroundColor);
				AerodynamicParachutePanel.setForeground(labelColor);
				AerodynamicParachutePanel.setSize(190, 300);
				AerodynamicParachutePanel.setLayout(null); 
				AerodynamicLeftPanel.add(AerodynamicParachutePanel);
				
			    JPanel AerodynamicParachuteOptionPanel = new JPanel();
			    AerodynamicParachuteOptionPanel.setLocation(190, (int) AerodynamicDragPanel.getSize().getHeight());
			    AerodynamicParachuteOptionPanel.setBackground(backgroundColor);
			    AerodynamicParachuteOptionPanel.setForeground(labelColor);
			    AerodynamicParachuteOptionPanel.setSize(210, 300);
			    AerodynamicParachuteOptionPanel.setLayout(null); 
				AerodynamicLeftPanel.add(AerodynamicParachuteOptionPanel);

			      
			      JPanel AerodynamicInputPanel = new JPanel();
			      AerodynamicInputPanel.setLocation(0, uy_p41 + 26 * 38 );
			      AerodynamicInputPanel.setSize(SidePanel_Width, 750);
			      AerodynamicInputPanel.setBackground(backgroundColor);
			      AerodynamicInputPanel.setForeground(Color.white);
			      AerodynamicInputPanel.setLayout(null);	
			  
			      JScrollPane ScrollPaneAerodynamicInput = new JScrollPane(AerodynamicLeftPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			      ScrollPaneAerodynamicInput.setPreferredSize(new Dimension(405, exty_main));
			      ScrollPaneAerodynamicInput.getVerticalScrollBar().setUnitIncrement(16);
			      AerodynamicSetupPanel.add(ScrollPaneAerodynamicInput, BorderLayout.LINE_START);
			      JScrollPane ScrollPaneAerodynamicInput2 = new JScrollPane(AerodynamicRightPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			      ScrollPaneAerodynamicInput2.setPreferredSize(new Dimension(405, exty_main));
			      ScrollPaneAerodynamicInput2.getVerticalScrollBar().setUnitIncrement(16);
			      AerodynamicSetupPanel.add(ScrollPaneAerodynamicInput2, BorderLayout.CENTER);
			    
			      JLabel LABELdragModel = new JLabel("Select Aerodynamic Drag Model:");
			      LABELdragModel.setLocation(3, 5 + 25 * 0  );
			      LABELdragModel.setSize(190, 20);
			      LABELdragModel.setBackground(backgroundColor);
			      LABELdragModel.setForeground(labelColor);
			      LABELdragModel.setFont(small_font);
			      LABELdragModel.setHorizontalAlignment(0);
			      AerodynamicDragPanel.add(LABELdragModel);
			      
			      ButtonGroup dragModelGroup = new ButtonGroup();  
				     
			      JRadioButton aeroButton = new JRadioButton("Constant drag coefficient");
			      aeroButton.setLocation(3, 5 + 25 * 1  );
			      aeroButton.setSize(190, 20);
			      aeroButton.setBackground(backgroundColor);
			      aeroButton.setForeground(labelColor);
			      aeroButton.setFont(small_font);
			      aeroButton.addChangeListener(new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent arg0) {
						WRITE_AERO();
						//------------------------------------------------------------------------
						int indx = getDragModelSetIndx();
						for(int i=0;i<AeroLeftBarAdditionalComponents.size();i++) {
							AerodynamicDragPanel.remove(AeroLeftBarAdditionalComponents.get(i));
						}
						AerodynamicDragPanel.revalidate();
						AerodynamicDragPanel.repaint();
						if(indx==0) {	
						      JLabel LABEL_CD = new JLabel("Set constant CD value [-]");
						      LABEL_CD.setLocation(193, 5 + 25 * 1);
						      LABEL_CD.setSize(300, 20);
						      LABEL_CD.setBackground(backgroundColor);
						      LABEL_CD.setForeground(labelColor);
						      LABEL_CD.setFont(small_font);
						      AeroLeftBarAdditionalComponents.add(LABEL_CD);
						      AerodynamicDragPanel.add(LABEL_CD);
							
					        ConstantCD_INPUT = new JTextField(""+readFromFile(Aero_file,1));
					        ConstantCD_INPUT.setLocation(193, 5 + 25 * 2 );
					        ConstantCD_INPUT.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
					        ConstantCD_INPUT.setBorder(Moon_border);
					        ConstantCD_INPUT.setSize(100, 20);
					        ConstantCD_INPUT.setEditable(true);
					        ConstantCD_INPUT.addFocusListener(new FocusListener() {

								@Override
								public void focusGained(FocusEvent arg0) { }

								@Override
								public void focusLost(FocusEvent e) {
									WRITE_AERO();
								}
						   	  
						     });
					        AeroLeftBarAdditionalComponents.add(ConstantCD_INPUT);
					       // RB_INPUT.setBackground(Color.lightGray);
					        AerodynamicDragPanel.add(ConstantCD_INPUT);       
						}
						//------------------------------------------------------------------------
					}
			    	  
			      });
			      //aeroButton.setSelected(true);
			      DragModelSet.add(aeroButton);
			      //aeroButton.setHorizontalAlignment(0);
			      AerodynamicDragPanel.add(aeroButton);
			      dragModelGroup.add(aeroButton);
			       aeroButton = new JRadioButton("Hypersonic Panel Model");
			      aeroButton.setLocation(3, 5 + 25 * 2  );
			      aeroButton.setSize(190, 20);
			      aeroButton.setBackground(backgroundColor);
			      aeroButton.setForeground(labelColor);
			      aeroButton.setFont(small_font);
			      DragModelSet.add(aeroButton);
			      aeroButton.addChangeListener(new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent arg0) {
							WRITE_AERO();
							//------------------------------------------------------------------------
							int indx = getDragModelSetIndx();
							for(int i=0;i<AeroLeftBarAdditionalComponents.size();i++) {
								AerodynamicDragPanel.remove(AeroLeftBarAdditionalComponents.get(i));
							}
							AerodynamicDragPanel.revalidate();
							AerodynamicDragPanel.repaint();
							if(indx==1) {							
							     INPUT_RB = new JTextField(10);
							     double value = readFromFile(Aero_file, 2);
							     INPUT_RB.setText(""+value);
							     INPUT_RB.setLocation(193, 5 + 25 * 3);;
							     INPUT_RB.setSize(INPUT_width, 20);
							     INPUT_RB.setHorizontalAlignment(JTextField.RIGHT);
							     AeroLeftBarAdditionalComponents.add(INPUT_RB);
							     INPUT_RB.addFocusListener(new FocusListener() {

									@Override
									public void focusGained(FocusEvent arg0) { }

									@Override
									public void focusLost(FocusEvent e) {
										WRITE_AERO();
									}
							   	  
							     });
							     AerodynamicDragPanel.add(INPUT_RB);
						        
							      JLabel LABEL_RB = new JLabel("Heat Shield Body Radius RB [m]");
							      LABEL_RB.setLocation(193, 5 + 25 * 2);
							      LABEL_RB.setSize(300, 20);
							      LABEL_RB.setFont(small_font);
							      LABEL_RB.setBackground(backgroundColor);
							      LABEL_RB.setForeground(labelColor);
							      AeroLeftBarAdditionalComponents.add(LABEL_RB);
						       // RB_INPUT.setBackground(Color.lightGray);
							      AerodynamicDragPanel.add(LABEL_RB);       
							}
							//------------------------------------------------------------------------
					}
			    	  
			      });
			     // aeroButton.setHorizontalAlignment(0);
			      AerodynamicDragPanel.add(aeroButton);
			      dragModelGroup.add(aeroButton);

			      // System.out.println(dragModelGroup.getSelection().);
			      String[] titles = {"Constant Drag Coefficient", "Mach model"};
			      AerodynamicParachutePanel = GuiComponents.getdynamicList(AerodynamicParachutePanel, 
			    		  "Set Parachute drag model" , titles, ParachuteBulletPoints);
			      
			      ParachuteBulletPoints.get(0).addChangeListener(new ChangeListener() {

						@Override
						public void stateChanged(ChangeEvent arg0) {
							WRITE_AERO();
							for(int i=0;i<AeroParachuteBarAdditionalComponents.size();i++) {
								AerodynamicParachuteOptionPanel.remove(AeroParachuteBarAdditionalComponents.get(i));
							}
							AerodynamicParachuteOptionPanel.revalidate();
							AerodynamicParachuteOptionPanel.repaint();

							      JLabel LABEL = new JLabel("Set constant CD value [-]");
							      LABEL.setLocation(3, 30 + 25 * 0);
							      LABEL.setSize(210, 20);
							      LABEL.setBackground(backgroundColor);
							      LABEL.setForeground(labelColor);
							      LABEL.setFont(small_font);
							      AeroParachuteBarAdditionalComponents.add(LABEL);
							      AerodynamicParachuteOptionPanel.add(LABEL);
							      
							        ConstantParachuteCD_INPUT = new JTextField(""+readFromFile(Aero_file,4));
							        ConstantParachuteCD_INPUT.setLocation(3, 5 + 25 * 2 );
							        ConstantParachuteCD_INPUT.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
							        ConstantParachuteCD_INPUT.setBorder(Moon_border);
							        ConstantParachuteCD_INPUT.setSize(100, 20);
							        ConstantParachuteCD_INPUT.setEditable(true);
							        ConstantParachuteCD_INPUT.addFocusListener(new FocusListener() {

										@Override
										public void focusGained(FocusEvent arg0) { }

										@Override
										public void focusLost(FocusEvent e) {
											WRITE_AERO();
										}
								   	  
								     });
							        AeroParachuteBarAdditionalComponents.add(ConstantParachuteCD_INPUT);
							        AerodynamicParachuteOptionPanel.add(ConstantParachuteCD_INPUT); 
							
						}
				    	  
				      });
			      ParachuteBulletPoints.get(1).addChangeListener(new ChangeListener() {

						@Override
						public void stateChanged(ChangeEvent arg0) {
							WRITE_AERO();
							for(int i=0;i<AeroParachuteBarAdditionalComponents.size();i++) {
								AerodynamicParachuteOptionPanel.remove(AeroParachuteBarAdditionalComponents.get(i));
							}
							AerodynamicParachuteOptionPanel.revalidate();
							AerodynamicParachuteOptionPanel.repaint();

							      JLabel LABEL = new JLabel("Select 1D model [-]");
							      LABEL.setLocation(3, 30 + 25 * 1);
							      LABEL.setSize(300, 20);
							      LABEL.setBackground(backgroundColor);
							      LABEL.setForeground(labelColor);
							      LABEL.setFont(small_font);
							      AeroParachuteBarAdditionalComponents.add(LABEL);
							      AerodynamicParachuteOptionPanel.add(LABEL);
							
						}
				    	  
				      });
			      
			      ParachuteBulletPoints.get(1).setSelected(true);
			      
			      
			      
			      
			      
		//-----------------------------------------------------------------------------------------
	    // ---->>>>>                       TAB: Spacecraft Definition
		//-----------------------------------------------------------------------------------------			    
			    
			      
			      
			      
			      
			      
			    // Main (SUB) tabbed Pane for this page
		        JTabbedPane TabPane_SCDefinition = (JTabbedPane) new JTabbedPane();
		        TabPane_SCDefinition.setPreferredSize(new Dimension(extx_main, exty_main));
		        TabPane_SCDefinition.setBackground(backgroundColor);
		        TabPane_SCDefinition.setForeground(Color.BLACK);
		//-------------------------------------------------------------------------------------------	    

	    		
			      JPanel massAndInertiaPanel = new JPanel();
			    //  massAndInertiaPanel.setLocation(0, uy_p41 + 26 * 38 );
			      massAndInertiaPanel.setSize(SidePanel_Width, 550);
			      massAndInertiaPanel.setBackground(backgroundColor);
			      massAndInertiaPanel.setForeground(labelColor);
			      massAndInertiaPanel.setLayout(null);
			      
					JPanel InertiaxPanel = new JPanel();
					InertiaxPanel.setLocation(0, 80);
					InertiaxPanel.setBackground(backgroundColor);
					InertiaxPanel.setForeground(labelColor);
					InertiaxPanel.setSize(400, 600);
					InertiaxPanel.setLayout(null); 
					massAndInertiaPanel.add(InertiaxPanel);
					
					
			      JPanel propulsionInputPanel = new JPanel();
			     // propulsionInputPanel.setLocation(0, uy_p41 + 26 * 38 );
			     // propulsionInputPanel.setSize(SidePanel_Width, 350);
			      propulsionInputPanel.setBackground(backgroundColor);
			      propulsionInputPanel.setForeground(labelColor);
			      propulsionInputPanel.setLayout(new BorderLayout());
			      
			      JPanel guidanceNavigationAndControlPanel = new JPanel();
			     // propulsionInputPanel.setLocation(0, uy_p41 + 26 * 38 );
			     // propulsionInputPanel.setSize(SidePanel_Width, 350);
			      guidanceNavigationAndControlPanel.setBackground(backgroundColor);
			      guidanceNavigationAndControlPanel.setForeground(labelColor);
			      guidanceNavigationAndControlPanel.setLayout(null);
			
				
			     	if(OS_is==2) {
			     		// Resize image icons for Windows 
			         	 int size=10;
			         	icon_setup2 = new ImageIcon(getScaledImage(icon_setup2.getImage(),size,size));
			         	icon_inertia = new ImageIcon(getScaledImage(icon_inertia.getImage(),size,size));
			         	icon_aerodynamic = new ImageIcon(getScaledImage(icon_aerodynamic.getImage(),size,size));
			      }
			      
				TabPane_SCDefinition.addTab("Mass and Inertia" , icon_setup2, massAndInertiaPanel, null);
				TabPane_SCDefinition.addTab("Propulsion" , icon_inertia, propulsionInputPanel, null);
				TabPane_SCDefinition.addTab("Aerodynamic" , icon_aerodynamic, AerodynamicInputPanel, null);
				TabPane_SCDefinition.addTab("GNC" , icon_aerodynamic, guidanceNavigationAndControlPanel, null);
				PageX04_AttitudeSetup.add(TabPane_SCDefinition);
		        TabPane_SCDefinition.setSelectedIndex(0);
		        TabPane_SCDefinition.setFont(small_font);
				
	    //-------------------------------------------------------------------------------------------	
		 // Mass and Inertia  
		        
	 		      
    JSeparator Separator_Page2_2 = new JSeparator();
    Separator_Page2_2.setLocation(0, 0 );
    Separator_Page2_2.setSize(SidePanel_Width, 1);
    Separator_Page2_2.setBackground(Color.black);
    Separator_Page2_2.setForeground(labelColor);
    massAndInertiaPanel.add(Separator_Page2_2);

      String path3 = "images/mercuryBlueprint.png";
      File file3 = new File(path3);
      try {
      BufferedImage image4 = ImageIO.read(file3);
      JLabel label3 = new JLabel(new ImageIcon(image4));
      label3.setSize(560,430);
      label3.setLocation(435, 5);
      label3.setBorder(Moon_border);
      massAndInertiaPanel.add(label3);
      } catch (Exception e) {
      	System.err.println("Error: SpaceShip Setup/Basic Setup - could not load image");
      }
	  // Space intended for advanced integrator settings 
      
      

		
    JLabel LABEL_SpaceCraftSettings = new JLabel("Spacecraft Settings");
    LABEL_SpaceCraftSettings.setLocation(0, uy_p41 + 10 * 0  );
    LABEL_SpaceCraftSettings.setSize(400, 20);
    LABEL_SpaceCraftSettings.setBackground(backgroundColor);
    LABEL_SpaceCraftSettings.setForeground(labelColor);
    LABEL_SpaceCraftSettings.setFont(HeadlineFont);
    LABEL_SpaceCraftSettings.setHorizontalAlignment(0);
    massAndInertiaPanel.add(LABEL_SpaceCraftSettings);
    JLabel LABEL_Minit = new JLabel("Initial mass [kg]");
    LABEL_Minit.setLocation(INPUT_width+5, uy_p41 + 25 * 1 );
    LABEL_Minit.setSize(250, 20);
    LABEL_Minit.setBackground(backgroundColor);
    LABEL_Minit.setForeground(labelColor);
    massAndInertiaPanel.add(LABEL_Minit);
    
    INPUT_M0 = new JTextField(10){
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

				@Override public void setBorder(Border border) {
			        // No!
			    }
			};
    INPUT_M0.setLocation(2, uy_p41 + 25 * 1 );
    INPUT_M0.setSize(INPUT_width-20, 20);
    INPUT_M0.setBackground(backgroundColor);
    INPUT_M0.setForeground(Color.RED);
    INPUT_M0.setHorizontalAlignment(JTextField.RIGHT);
    INPUT_M0.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			WRITE_INIT();
			WRITE_PROP();
		}
  	  
    });
    massAndInertiaPanel.add(INPUT_M0);
		        
			      JSeparator Separator_Inertia = new JSeparator();
			      Separator_Inertia.setLocation(0, 0 );
			      Separator_Inertia.setSize(SidePanel_Width, 1);
			      Separator_Inertia.setBackground(Color.black);
			      Separator_Inertia.setForeground(labelColor);
			      InertiaxPanel.add(Separator_Inertia);

				  // Space intended for advanced integrator settings 
			      JLabel LABEL_InertiaTensor = new JLabel("Inertia Tensor [kg m\u00b2 ] ");
			      LABEL_InertiaTensor.setLocation(0, uy_p41 + 10 * 0  );
			      LABEL_InertiaTensor.setSize(190, 20);
			      LABEL_InertiaTensor.setBackground(backgroundColor);
			      LABEL_InertiaTensor.setForeground(labelColor);
			      LABEL_InertiaTensor.setFont(HeadlineFont);
			      LABEL_InertiaTensor.setHorizontalAlignment(0);
			      InertiaxPanel.add(LABEL_InertiaTensor);
		        
		        
				JPanel InertiaMatrixPanel = new JPanel();
				InertiaMatrixPanel.setLayout(null);
				InertiaMatrixPanel.setLocation(10, 40);
				InertiaMatrixPanel.setBackground(backgroundColor);
				InertiaMatrixPanel.setForeground(labelColor);
				InertiaMatrixPanel.setSize(330, 370);
				InertiaMatrixPanel.setBorder(Moon_border);
				InertiaxPanel.add(InertiaMatrixPanel);
				
		        String path2 = "images/momentOfInertia.png";
		        File file2 = new File(path2);
		        try {
		        BufferedImage image3 = ImageIO.read(file2);
		        JLabel label2 = new JLabel(new ImageIcon(image3));
		        label2.setSize(320,240);
		        label2.setLocation(5, 125);
		        label2.setBorder(Moon_border);
		        InertiaMatrixPanel.add(label2);
		        } catch (Exception e) {
		        	System.err.println("Error: SpaceShip Setup/Aerodynamik - could not load image");
		        }

				
		         INPUT_IXX = new JTextField();
		        INPUT_IXX.setLocation(gap_size_x+(box_size_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_y)*0);
		        INPUT_IXX.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
		        INPUT_IXX.setBorder(Moon_border);
		        INPUT_IXX.setSize(box_size_x, box_size_y);
		        INPUT_IXX.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
						WriteINERTIA();
					}
			    	  
			      });
		        InertiaMatrixPanel.add(INPUT_IXX);	 
		        
			      JLabel LABEL_IXX = new JLabel("Ixx");
			      LABEL_IXX.setLocation(gap_size_x+(box_size_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_y)*0 - 15);
			      LABEL_IXX.setSize(box_size_x, 20);
			      LABEL_IXX.setBackground(backgroundColor);
			      LABEL_IXX.setForeground(labelColor);
			      LABEL_IXX.setFont(small_font);
			      LABEL_IXX.setHorizontalAlignment(0);
			      InertiaMatrixPanel.add(LABEL_IXX);
 
		         INPUT_IXY = new JTextField("0");
		        INPUT_IXY.setLocation(gap_size_x+(box_size_x + gap_size_x)*1, gap_size_y + (gap_size_y + box_size_y)*0);
		        INPUT_IXY.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
		        INPUT_IXY.setBorder(Moon_border);
		        INPUT_IXY.setSize(box_size_x, box_size_y);
		        INPUT_IXY.setEditable(false);
		        INPUT_IXY.setBackground(Color.lightGray);
		        INPUT_IXY.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
						WriteINERTIA();
					}
			    	  
			      });
		        InertiaMatrixPanel.add(INPUT_IXY);	 
		        
			      JLabel LABEL_IXY = new JLabel("Ixy");
			      LABEL_IXY.setLocation(gap_size_x+(box_size_x + gap_size_x)*1, gap_size_y + (gap_size_y + box_size_y)*0 - 15);
			      LABEL_IXY.setSize(box_size_x, 20);
			      LABEL_IXY.setBackground(backgroundColor);
			      LABEL_IXY.setForeground(labelColor);
			      LABEL_IXY.setFont(small_font);
			      LABEL_IXY.setHorizontalAlignment(0);
			      InertiaMatrixPanel.add(LABEL_IXY);
		        
		         INPUT_IXZ = new JTextField();
		        INPUT_IXZ.setLocation(gap_size_x+(box_size_x + gap_size_x)*2, gap_size_y + (gap_size_y + box_size_y)*0);
		        INPUT_IXZ.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
		        INPUT_IXZ.setBorder(Moon_border);
		        INPUT_IXZ.setSize(box_size_x, box_size_y);
		        INPUT_IXZ.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
						WriteINERTIA();
					}
			    	  
			      });
		        InertiaMatrixPanel.add(INPUT_IXZ);	
		        
			      JLabel LABEL_IXZ = new JLabel("Ixz");
			      LABEL_IXZ.setLocation(gap_size_x+(box_size_x + gap_size_x)*2, gap_size_y + (gap_size_y + box_size_y)*0 - 15);
			      LABEL_IXZ.setSize(box_size_x, 20);
			      LABEL_IXZ.setBackground(backgroundColor);
			      LABEL_IXZ.setForeground(labelColor);
			      LABEL_IXZ.setFont(small_font);
			      LABEL_IXZ.setHorizontalAlignment(0);
			      InertiaMatrixPanel.add(LABEL_IXZ);
		        
		         INPUT_IYX = new JTextField("0");
		        INPUT_IYX.setLocation(gap_size_x+(box_size_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_y)*1);
		        INPUT_IYX.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
		        INPUT_IYX.setBorder(Moon_border);
		        INPUT_IYX.setSize(box_size_x, box_size_y);
		        INPUT_IYX.setEditable(false);
		        INPUT_IYX.setBackground(Color.lightGray);
		        INPUT_IYX.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
						WriteINERTIA();
					}
			    	  
			      });
		        InertiaMatrixPanel.add(INPUT_IYX);	
		        
			      JLabel LABEL_IYX = new JLabel("Iyx");
			      LABEL_IYX.setLocation(gap_size_x+(box_size_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_y)*1 - 15);
			      LABEL_IYX.setSize(box_size_x, 20);
			      LABEL_IYX.setBackground(backgroundColor);
			      LABEL_IYX.setForeground(labelColor);
			      LABEL_IYX.setFont(small_font);
			      LABEL_IYX.setHorizontalAlignment(0);
			      InertiaMatrixPanel.add(LABEL_IYX);
 
		         INPUT_IYY = new JTextField();
		        INPUT_IYY.setLocation(gap_size_x+(box_size_x + gap_size_x)*1, gap_size_y + (gap_size_y + box_size_y)*1);
		        INPUT_IYY.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
		        INPUT_IYY.setBorder(Moon_border);
		        INPUT_IYY.setSize(box_size_x, box_size_y);
		        INPUT_IYY.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
						WriteINERTIA();
					}
			    	  
			      });
		        InertiaMatrixPanel.add(INPUT_IYY);
		        
			      JLabel LABEL_IYY = new JLabel("Iyy");
			      LABEL_IYY.setLocation(gap_size_x+(box_size_x + gap_size_x)*1, gap_size_y + (gap_size_y + box_size_y)*1 - 15);
			      LABEL_IYY.setSize(box_size_x, 20);
			      LABEL_IYY.setBackground(backgroundColor);
			      LABEL_IYY.setForeground(labelColor);
			      LABEL_IYY.setFont(small_font);
			      LABEL_IYY.setHorizontalAlignment(0);
			      InertiaMatrixPanel.add(LABEL_IYY);
		        
		         INPUT_IYZ = new JTextField("0");
		        INPUT_IYZ.setLocation(gap_size_x+(box_size_x + gap_size_x)*2, gap_size_y + (gap_size_y + box_size_y)*1);
		        INPUT_IYZ.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
		        INPUT_IYZ.setBorder(Moon_border);
		        INPUT_IYZ.setSize(box_size_x, box_size_y);
		        INPUT_IYZ.setEditable(false);
		        INPUT_IYZ.setBackground(Color.lightGray);
		        INPUT_IYZ.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
						WriteINERTIA();
					}
			    	  
			      });
		        InertiaMatrixPanel.add(INPUT_IYZ);	
		        
			      JLabel LABEL_IYZ = new JLabel("Iyz");
			      LABEL_IYZ.setLocation(gap_size_x+(box_size_x + gap_size_x)*2, gap_size_y + (gap_size_y + box_size_y)*1 - 15);
			      LABEL_IYZ.setSize(box_size_x, 20);
			      LABEL_IYZ.setBackground(Color.gray);
			      LABEL_IYZ.setForeground(labelColor);
			      LABEL_IYZ.setFont(small_font);
			      LABEL_IYZ.setHorizontalAlignment(0);
			      InertiaMatrixPanel.add(LABEL_IYZ);
		        
		        INPUT_IZX = new JTextField();
		        INPUT_IZX.setLocation(gap_size_x+(box_size_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_y)*2);
		        INPUT_IZX.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
		        INPUT_IZX.setBorder(Moon_border);
		        INPUT_IZX.setSize(box_size_x, box_size_y);
		        INPUT_IZX.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
						WriteINERTIA();
					}
			    	  
			      });
		        InertiaMatrixPanel.add(INPUT_IZX);	 
		        
			      JLabel LABEL_IZX = new JLabel("Izx");
			      LABEL_IZX.setLocation(gap_size_x+(box_size_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_y)*2 - 15);
			      LABEL_IZX.setSize(box_size_x, 20);
			      LABEL_IZX.setBackground(backgroundColor);
			      LABEL_IZX.setForeground(labelColor);
			      LABEL_IZX.setFont(small_font);
			      LABEL_IZX.setHorizontalAlignment(0);
			      InertiaMatrixPanel.add(LABEL_IZX);
 
		         INPUT_IZY = new JTextField("0");
		        INPUT_IZY.setLocation(gap_size_x+(box_size_x + gap_size_x)*1, gap_size_y + (gap_size_y + box_size_y)*2);
		        INPUT_IZY.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
		        INPUT_IZY.setBorder(Moon_border);
		        INPUT_IZY.setSize(box_size_x, box_size_y);
		        INPUT_IZY.setEditable(false);
		        INPUT_IZY.setBackground(Color.lightGray);
		        INPUT_IZY.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
						WriteINERTIA();
					}
			    	  
			      });
		        InertiaMatrixPanel.add(INPUT_IZY);	
		        
			      JLabel LABEL_IZY = new JLabel("Izy");
			      LABEL_IZY.setLocation(gap_size_x+(box_size_x + gap_size_x)*1, gap_size_y + (gap_size_y + box_size_y)*2 - 15);
			      LABEL_IZY.setSize(box_size_x, 20);
			      LABEL_IZY.setBackground(backgroundColor);
			      LABEL_IZY.setForeground(labelColor);
			      LABEL_IZY.setFont(small_font);
			      LABEL_IZY.setHorizontalAlignment(0);
			      InertiaMatrixPanel.add(LABEL_IZY);

		        
		        INPUT_IZZ = new JTextField();
		        INPUT_IZZ.setLocation(gap_size_x+(box_size_x + gap_size_x)*2, gap_size_y + (gap_size_y + box_size_y)*2);
		        INPUT_IZZ.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
		        INPUT_IZZ.setBorder(Moon_border);
		        INPUT_IZZ.setSize(box_size_x, box_size_y);
		        INPUT_IZZ.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
						WriteINERTIA();
					}
			    	  
			      });
		        InertiaMatrixPanel.add(INPUT_IZZ);	
		        
			      JLabel LABEL_IZZ = new JLabel("Izz");
			      LABEL_IZZ.setLocation(gap_size_x+(box_size_x + gap_size_x)*2, gap_size_y + (gap_size_y + box_size_y)*2 - 15);
			      LABEL_IZZ.setSize(box_size_x, 20);
			      LABEL_IZZ.setBackground(backgroundColor);
			      LABEL_IZZ.setForeground(labelColor);
			      LABEL_IZZ.setFont(small_font);
			      LABEL_IZZ.setHorizontalAlignment(0);
			      InertiaMatrixPanel.add(LABEL_IZZ);
			      
			      
		        //---------------------------------------------------------------------------------------------
		        //                         Propulsion Definition Block
		        //--------------------------------------------------------------------------------------------- 
		      
			        JPanel propulsionSidePanel = new JPanel();
			        propulsionSidePanel.setPreferredSize(new Dimension(400, 800));
			        propulsionSidePanel.setBackground(backgroundColor);
			        propulsionSidePanel.setLayout(null);
			        propulsionInputPanel.add(propulsionSidePanel, BorderLayout.EAST);
			        
			        
			    PropulsionDrawEditor propulsionDrawEditior = new PropulsionDrawEditor();
		        JPanel PropulsionEditor = propulsionDrawEditior.getPropulsionDrawArea();
		        //PropulsionEditor.setSize(600, 500);
		        //PropulsionEditor.setLocation(400, 30);
		        PropulsionEditor.addComponentListener(new ComponentAdapter() 
		        {  
		            public void componentResized(ComponentEvent evt) {
		            	propulsionDrawEditior.getCanvas().resizeBackgroundImage();
		            }
		    });
		        propulsionInputPanel.add(PropulsionEditor, BorderLayout.CENTER);
		        
		        
		        JButton propEditorButton = new JButton("Open Full Size Editor");
		        propEditorButton.setSize(200,30);
		        propEditorButton.setLocation(0, uy_p41 + 25 * 1);
		        propEditorButton.setBackground(backgroundColor);
		        propEditorButton.setForeground(Color.BLACK);
		        propEditorButton.setFont(small_font);
		        propEditorButton.addActionListener(new ActionListener() {
		        	 public void actionPerformed(ActionEvent e)
		        	  {
	                	   Thread thread = new Thread(new Runnable() {
	                  		    public void run() {
	                  		    		PropulsionDrawEditor.setExit(false);
	                  		       	PropulsionDrawEditor.main(null);

	                  		    }
	                  		});
	                  		thread.start();
		        		 
		        	  }
		        });
		        propulsionSidePanel.add(propEditorButton);
		      
		      JLabel LABEL_PrimarySettings = new JLabel("Primary Propulsion System Settings");
		      LABEL_PrimarySettings.setLocation(0, uy_p41 + 25 * 3 );
		      LABEL_PrimarySettings.setSize(400, 20);
		      LABEL_PrimarySettings.setBackground(backgroundColor);
		      LABEL_PrimarySettings.setForeground(labelColor);
		      LABEL_PrimarySettings.setFont(HeadlineFont);
		      LABEL_PrimarySettings.setHorizontalAlignment(JLabel.LEFT);
		      propulsionSidePanel.add(LABEL_PrimarySettings);
		      
		      
		      JLabel LABEL_ME_ISP = new JLabel("Main propulsion system ISP [s]");
		      LABEL_ME_ISP.setLocation(INPUT_width+5, uy_p41 + 25 * 4 );
		      LABEL_ME_ISP.setSize(300, 20);
		      LABEL_ME_ISP.setBackground(backgroundColor);
		      LABEL_ME_ISP.setForeground(labelColor);
		      propulsionSidePanel.add(LABEL_ME_ISP);
		      JLabel LABEL_ME_PropMass = new JLabel("Main propulsion system propellant mass [kg]");
		      LABEL_ME_PropMass.setLocation(INPUT_width+5, uy_p41 + 25 * 5);
		      LABEL_ME_PropMass.setSize(300, 20);
		      LABEL_ME_PropMass.setBackground(backgroundColor);
		      LABEL_ME_PropMass.setForeground(labelColor);
		      propulsionSidePanel.add(LABEL_ME_PropMass);
		      JLabel LABEL_ME_Thrust_max = new JLabel("Main propulsion system max. Thrust [N]");
		      LABEL_ME_Thrust_max.setLocation(INPUT_width+5, uy_p41 + 25 * 6 );
		      LABEL_ME_Thrust_max.setSize(300, 20);
		      LABEL_ME_Thrust_max.setBackground(backgroundColor);
		      LABEL_ME_Thrust_max.setForeground(labelColor);
		      propulsionSidePanel.add(LABEL_ME_Thrust_max);
		      JLabel LABEL_ME_Thrust_min = new JLabel("Main Propulsion system min. Thrust [N]");
		      LABEL_ME_Thrust_min.setLocation(INPUT_width+5, uy_p41 + 25 * 7 );
		      LABEL_ME_Thrust_min.setSize(300, 20);
		      LABEL_ME_Thrust_min.setBackground(backgroundColor);
		      LABEL_ME_Thrust_min.setForeground(labelColor);
		      propulsionSidePanel.add(LABEL_ME_Thrust_min);
		      
		      JLabel LABEL_ME_ISP_Model = new JLabel("Include dynamic ISP model in throttled state");
		      LABEL_ME_ISP_Model.setLocation(INPUT_width+5, uy_p41 + 25 * 8 );
		      LABEL_ME_ISP_Model.setSize(300, 20);
		      LABEL_ME_ISP_Model.setBackground(backgroundColor);
		      LABEL_ME_ISP_Model.setForeground(labelColor);
		      propulsionSidePanel.add(LABEL_ME_ISP_Model);
		      
		      JLabel LABEL_ME_ISP_min = new JLabel("ISP for maximum throttled state [s]");
		      LABEL_ME_ISP_min.setLocation(INPUT_width+5, uy_p41 + 25 * 9 );
		      LABEL_ME_ISP_min.setSize(300, 20);
		      LABEL_ME_ISP_min.setBackground(backgroundColor);
		      LABEL_ME_ISP_min.setForeground(labelColor);
		      propulsionSidePanel.add(LABEL_ME_ISP_min);
		     
			 
		      INPUT_ISP = new JTextField(10){
				    /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

						@Override public void setBorder(Border border) {
					        // No!
					    }
					};
		      INPUT_ISP.setLocation(2, uy_p41 + 25 * 4 );
		      INPUT_ISP.setSize(INPUT_width-20, 20);
		      INPUT_ISP.setBackground(backgroundColor);
		      INPUT_ISP.setForeground(valueColor);
		      INPUT_ISP.setHorizontalAlignment(JTextField.RIGHT);
		      INPUT_ISP.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) { }

				@Override
				public void focusLost(FocusEvent e) {
					WRITE_INIT();
					WRITE_PROP();
				}
		    	  
		      });
		      propulsionSidePanel.add(INPUT_ISP);
		     INPUT_PROPMASS = new JTextField(10){
				    /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

						@Override public void setBorder(Border border) {
					        // No!
					    }
					};
		     INPUT_PROPMASS.setLocation(2, uy_p41 + 25 * 5);
		     INPUT_PROPMASS.setSize(INPUT_width-20, 20);
		     INPUT_PROPMASS.setBackground(backgroundColor);
		     INPUT_PROPMASS.setForeground(valueColor);
		     INPUT_PROPMASS.setHorizontalAlignment(JTextField.RIGHT);
		     INPUT_PROPMASS.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) { }

				@Override
				public void focusLost(FocusEvent e) {
					WRITE_INIT();
					WRITE_PROP();
				}
		   	  
		     });
		     propulsionSidePanel.add(INPUT_PROPMASS);        
		     INPUT_THRUSTMAX = new JTextField(10){
				    /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

						@Override public void setBorder(Border border) {
					        // No!
					    }
					};
		     INPUT_THRUSTMAX.setLocation(2, uy_p41 + 25 * 6 );
		     INPUT_THRUSTMAX.setSize(INPUT_width-20, 20);
		     INPUT_THRUSTMAX.setBackground(backgroundColor);
		     INPUT_THRUSTMAX.setForeground(valueColor);
		     INPUT_THRUSTMAX.setHorizontalAlignment(JTextField.RIGHT);
		     INPUT_THRUSTMAX.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) { }

				@Override
				public void focusLost(FocusEvent e) {
					WRITE_INIT();
					WRITE_PROP();
				}
		   	  
		     });
		     propulsionSidePanel.add(INPUT_THRUSTMAX);
		     INPUT_THRUSTMIN = new JTextField(10){
				    /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

						@Override public void setBorder(Border border) {
					        // No!
					    }
					};
		     INPUT_THRUSTMIN.setLocation(2, uy_p41 + 25 * 7 );;
		     INPUT_THRUSTMIN.setSize(INPUT_width-20, 20);
		     INPUT_THRUSTMIN.setBackground(backgroundColor);
		     INPUT_THRUSTMIN.setForeground(valueColor);
		     INPUT_THRUSTMIN.setHorizontalAlignment(JTextField.RIGHT);
		     INPUT_THRUSTMIN.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) { }

				@Override
				public void focusLost(FocusEvent e) {
					WRITE_INIT();
					WRITE_PROP();
				}
		   	  
		     });
		     propulsionSidePanel.add(INPUT_THRUSTMIN);
		     
		     INPUT_ISPMODEL = new JCheckBox(){
				    /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

						@Override public void setBorder(Border border) {
					        // No!
					    }
					};
		     INPUT_ISPMODEL.setLocation(INPUT_width+5-20, uy_p41 + 25 * 8+2);
		     INPUT_ISPMODEL.setSize(15, 15);
		     INPUT_ISPMODEL.setSelected(true);
		     INPUT_ISPMODEL.setBackground(backgroundColor);
		     INPUT_ISPMODEL.setForeground(valueColor);
		     INPUT_ISPMODEL.addItemListener(new ItemListener() {
		       	 public void itemStateChanged(ItemEvent e) {
		       		WRITE_PROP();
		       	 }
		                  });
		     INPUT_ISPMODEL.setHorizontalAlignment(0);
		     propulsionSidePanel.add(INPUT_ISPMODEL);
		     
		     
		     INPUT_ISPMIN = new JTextField(10){
				    /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

						@Override public void setBorder(Border border) {
					        // No!
					    }
					};
		     INPUT_ISPMIN.setLocation(2, uy_p41 + 25 * 9 );;
		     INPUT_ISPMIN.setSize(INPUT_width-20, 20);
		     INPUT_ISPMIN.setBackground(backgroundColor);
		     INPUT_ISPMIN.setForeground(valueColor);
		     INPUT_ISPMIN.setHorizontalAlignment(JTextField.RIGHT);
		     INPUT_ISPMIN.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) { }

				@Override
				public void focusLost(FocusEvent e) {
					WRITE_INIT();
					WRITE_PROP();
				}
		   	  
		     });
		     propulsionSidePanel.add(INPUT_ISPMIN);
		     
		        
				JPanel RCSPanel = new JPanel();
				RCSPanel.setLocation(3, 300);
				RCSPanel.setBackground(backgroundColor);
				RCSPanel.setSize(400, 350);
				RCSPanel.setLayout(null);
				propulsionSidePanel.add(RCSPanel);
				
		      JLabel LABEL_RCSSettings = new JLabel("Reaction Control System Settings");
		      LABEL_RCSSettings.setLocation(0, uy_p41 + 10 * 0  );
		      LABEL_RCSSettings.setSize(400, 20);
		      LABEL_RCSSettings.setBackground(backgroundColor);
		      LABEL_RCSSettings.setForeground(labelColor);
		      LABEL_RCSSettings.setFont(HeadlineFont);
		      LABEL_RCSSettings.setHorizontalAlignment(JLabel.LEFT);
		      RCSPanel.add(LABEL_RCSSettings);
		      
		      JLabel LABEL_RcsX = new JLabel("Momentum RCS X axis [Nm]");
		      LABEL_RcsX.setLocation(INPUT_width+1, uy_p41 + 25 * 2 );
		      LABEL_RcsX.setSize(250, 20);
		      LABEL_RcsX.setBackground(backgroundColor);
		      LABEL_RcsX.setForeground(labelColor);
		      RCSPanel.add(LABEL_RcsX);
		      
		      JLabel LABEL_RcsY = new JLabel("Momentum RCS Y axis [Nm]");
		      LABEL_RcsY.setLocation(INPUT_width+1, uy_p41 + 25 * 3 );
		      LABEL_RcsY.setSize(250, 20);
		      LABEL_RcsY.setBackground(backgroundColor);
		      LABEL_RcsY.setForeground(labelColor);
		      RCSPanel.add(LABEL_RcsY);
		      
		      JLabel LABEL_RcsZ = new JLabel("Momentum RCS Z axis [Nm]");
		      LABEL_RcsZ.setLocation(INPUT_width+1, uy_p41 + 25 * 4 );
		      LABEL_RcsZ.setSize(250, 20);
		      LABEL_RcsZ.setBackground(backgroundColor);
		      LABEL_RcsZ.setForeground(labelColor);
		      RCSPanel.add(LABEL_RcsZ);
		      
			     INPUT_RCSX = new JTextField(10){
					    /**
						 * 
						 */
						private static final long serialVersionUID = 1L;

							@Override public void setBorder(Border border) {
						        // No!
						    }
						};
			     INPUT_RCSX.setLocation(2, uy_p41 + 25 * 2 );;
			     INPUT_RCSX.setSize(INPUT_width-20, 20);
			     INPUT_RCSX.setBackground(backgroundColor);
			     INPUT_RCSX.setForeground(valueColor);
			     INPUT_RCSX.setHorizontalAlignment(JTextField.RIGHT);
			     INPUT_RCSX.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
					//	WRITE_INIT();
						WRITE_PROP();
					}
			   	  
			     });
			     RCSPanel.add(INPUT_RCSX);
			     
			     INPUT_RCSY = new JTextField(10){
					    /**
						 * 
						 */
						private static final long serialVersionUID = 1L;

							@Override public void setBorder(Border border) {
						        // No!
						    }
						};
			     INPUT_RCSY.setLocation(2, uy_p41 + 25 * 3 );;
			     INPUT_RCSY.setSize(INPUT_width-20, 20);
			     INPUT_RCSY.setBackground(backgroundColor);
			     INPUT_RCSY.setForeground(valueColor);
			     INPUT_RCSY.setHorizontalAlignment(JTextField.RIGHT);
			     INPUT_RCSY.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
					//	WRITE_INIT();
						WRITE_PROP();
					}
			   	  
			     });
			     RCSPanel.add(INPUT_RCSY);
			     
			     INPUT_RCSZ = new JTextField(10){
					    /**
						 * 
						 */
						private static final long serialVersionUID = 1L;

							@Override public void setBorder(Border border) {
						        // No!
						    }
						};
			     INPUT_RCSZ.setLocation(2, uy_p41 + 25 * 4 );;
			     INPUT_RCSZ.setSize(INPUT_width-20, 20);
			     INPUT_RCSZ.setBackground(backgroundColor);
			     INPUT_RCSZ.setForeground(valueColor);
			     INPUT_RCSZ.setHorizontalAlignment(JTextField.RIGHT);
			     INPUT_RCSZ.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
					//	WRITE_INIT();
						WRITE_PROP();
					}
			   	  
			     });
			     RCSPanel.add(INPUT_RCSZ);
			     
			     
			      JLabel LABEL_RcsXThrust = new JLabel("RCS X Thrust [N]");
			      LABEL_RcsXThrust.setLocation(INPUT_width+1, uy_p41 + 25 * 6 );
			      LABEL_RcsXThrust.setSize(250, 20);
			      LABEL_RcsXThrust.setBackground(backgroundColor);
			      LABEL_RcsXThrust.setForeground(labelColor);
			      RCSPanel.add(LABEL_RcsXThrust);
			      
			      JLabel LABEL_RcsYThrust = new JLabel("RCS Y Thrust [N]");
			      LABEL_RcsYThrust.setLocation(INPUT_width+1, uy_p41 + 25 * 7 );
			      LABEL_RcsYThrust.setSize(250, 20);
			      LABEL_RcsYThrust.setBackground(backgroundColor);
			      LABEL_RcsYThrust.setForeground(labelColor);
			      RCSPanel.add(LABEL_RcsYThrust);
			      
			      JLabel LABEL_RcsZThrust = new JLabel("RCS Z Thrust [N]");
			      LABEL_RcsZThrust.setLocation(INPUT_width+1, uy_p41 + 25 * 8 );
			      LABEL_RcsZThrust.setSize(250, 20);
			      LABEL_RcsZThrust.setBackground(backgroundColor);
			      LABEL_RcsZThrust.setForeground(labelColor);
			      RCSPanel.add(LABEL_RcsZThrust);
			      
			      JLabel LABEL_RcsTank = new JLabel("Secondary Propulsion Tank [kg]");
			      LABEL_RcsTank.setLocation(INPUT_width+1, uy_p41 + 25 * 10 );
			      LABEL_RcsTank.setSize(250, 20);
			      LABEL_RcsTank.setBackground(backgroundColor);
			      LABEL_RcsTank.setForeground(labelColor);
			      RCSPanel.add(LABEL_RcsTank);
			      
			      JLabel LABEL_RcsXISP= new JLabel("RCS ISP X axis Thruster [s]");
			      LABEL_RcsXISP.setLocation(INPUT_width+1, uy_p41 + 25 * 11 );
			      LABEL_RcsXISP.setSize(250, 20);
			      LABEL_RcsXISP.setBackground(backgroundColor);
			      LABEL_RcsXISP.setForeground(labelColor);
			      RCSPanel.add(LABEL_RcsXISP);
			      
			      JLabel LABEL_RcsYISP= new JLabel("RCS ISP Y axis Thruster [s]");
			      LABEL_RcsYISP.setLocation(INPUT_width+1, uy_p41 + 25 * 12 );
			      LABEL_RcsYISP.setSize(250, 20);
			      LABEL_RcsYISP.setBackground(backgroundColor);
			      LABEL_RcsYISP.setForeground(labelColor);
			      RCSPanel.add(LABEL_RcsYISP);
			      
			      JLabel LABEL_RcsZISP= new JLabel("RCS ISP Z axis Thruster [s]");
			      LABEL_RcsZISP.setLocation(INPUT_width+1, uy_p41 + 25 * 13 );
			      LABEL_RcsZISP.setSize(250, 20);
			      LABEL_RcsZISP.setBackground(backgroundColor);
			      LABEL_RcsZISP.setForeground(labelColor);
			      RCSPanel.add(LABEL_RcsZISP);
			      
				     INPUT_RCSXTHRUST = new JTextField(10){
						    /**
							 * 
							 */
							private static final long serialVersionUID = 1L;

								@Override public void setBorder(Border border) {
							        // No!
							    }
							};
				     INPUT_RCSXTHRUST.setLocation(2, uy_p41 + 25 * 6 );;
				     INPUT_RCSXTHRUST.setSize(INPUT_width-20, 20);
				     INPUT_RCSXTHRUST.setBackground(backgroundColor);
				     INPUT_RCSXTHRUST.setForeground(valueColor);
				     INPUT_RCSXTHRUST.setHorizontalAlignment(JTextField.RIGHT);
				     INPUT_RCSXTHRUST.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
						//	WRITE_INIT();
							WRITE_PROP();
						}
				   	  
				     });
				     RCSPanel.add(INPUT_RCSXTHRUST);
				     
				     INPUT_RCSYTHRUST = new JTextField(10){
						    /**
							 * 
							 */
							private static final long serialVersionUID = 1L;

								@Override public void setBorder(Border border) {
							        // No!
							    }
							};
				     INPUT_RCSYTHRUST.setLocation(2, uy_p41 + 25 * 7 );;
				     INPUT_RCSYTHRUST.setSize(INPUT_width-20, 20);
				     INPUT_RCSYTHRUST.setBackground(backgroundColor);
				     INPUT_RCSYTHRUST.setForeground(valueColor);
				     INPUT_RCSYTHRUST.setHorizontalAlignment(JTextField.RIGHT);
				     INPUT_RCSYTHRUST.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
						//	WRITE_INIT();
							WRITE_PROP();
						}
				   	  
				     });
				     RCSPanel.add(INPUT_RCSYTHRUST);
				     
				     INPUT_RCSZTHRUST = new JTextField(10){
						    /**
							 * 
							 */
							private static final long serialVersionUID = 1L;

								@Override public void setBorder(Border border) {
							        // No!
							    }
							};
				     INPUT_RCSZTHRUST.setLocation(2, uy_p41 + 25 * 8 );;
				     INPUT_RCSZTHRUST.setSize(INPUT_width-20, 20);
				     INPUT_RCSZTHRUST.setBackground(backgroundColor);
				     INPUT_RCSZTHRUST.setForeground(valueColor);
				     INPUT_RCSZTHRUST.setHorizontalAlignment(JTextField.RIGHT);
				     INPUT_RCSZTHRUST.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
						//	WRITE_INIT();
							WRITE_PROP();
						}
				   	  
				     });
				     RCSPanel.add(INPUT_RCSZTHRUST);
				     
				     INPUT_RCSTANK = new JTextField(10){
						    /**
							 * 
							 */
							private static final long serialVersionUID = 1L;

								@Override public void setBorder(Border border) {
							        // No!
							    }
							};
				     INPUT_RCSTANK.setLocation(2, uy_p41 + 25 * 10 );;
				     INPUT_RCSTANK.setSize(INPUT_width-20, 20);
				     INPUT_RCSTANK.setBackground(backgroundColor);
				     INPUT_RCSTANK.setForeground(valueColor);
				     INPUT_RCSTANK.setHorizontalAlignment(JTextField.RIGHT);
				     INPUT_RCSTANK.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
						//	WRITE_INIT();
							WRITE_PROP();
						}
				   	  
				     });
				     RCSPanel.add(INPUT_RCSTANK);
				     
				     INPUT_RCSXISP = new JTextField(10){
						    /**
							 * 
							 */
							private static final long serialVersionUID = 1L;

								@Override public void setBorder(Border border) {
							        // No!
							    }
							};
				     INPUT_RCSXISP.setLocation(2, uy_p41 + 25 * 11 );
				     INPUT_RCSXISP.setSize(INPUT_width-20, 20);
				     INPUT_RCSXISP.setBackground(backgroundColor);
				     INPUT_RCSXISP.setForeground(valueColor);
				     INPUT_RCSXISP.setHorizontalAlignment(JTextField.RIGHT);
				     INPUT_RCSXISP.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
						//	WRITE_INIT();
							WRITE_PROP();
						}
				   	  
				     });
				     RCSPanel.add(INPUT_RCSXISP);
				     
				     INPUT_RCSYISP = new JTextField(10){
						    /**
							 * 
							 */
							private static final long serialVersionUID = 1L;

								@Override public void setBorder(Border border) {
							        // No!
							    }
							};
				     INPUT_RCSYISP.setLocation(2, uy_p41 + 25 * 12 );
				     INPUT_RCSYISP.setSize(INPUT_width-20, 20);
				     INPUT_RCSYISP.setBackground(backgroundColor);
				     INPUT_RCSYISP.setForeground(valueColor);
				     INPUT_RCSYISP.setHorizontalAlignment(JTextField.RIGHT);
				     INPUT_RCSYISP.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
						//	WRITE_INIT();
							WRITE_PROP();
						}
				   	  
				     });
				     RCSPanel.add(INPUT_RCSYISP);
				     
				     INPUT_RCSZISP = new JTextField(10){
						    /**
							 * 
							 */
							private static final long serialVersionUID = 1L;

								@Override public void setBorder(Border border) {
							        // No!
							    }
							};
				     INPUT_RCSZISP.setLocation(2, uy_p41 + 25 * 13 );;
				     INPUT_RCSZISP.setSize(INPUT_width-20, 20);
				     INPUT_RCSZISP.setBackground(backgroundColor);
				     INPUT_RCSZISP.setForeground(valueColor);
				     INPUT_RCSZISP.setHorizontalAlignment(JTextField.RIGHT);
				     INPUT_RCSZISP.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
						//	WRITE_INIT();
							WRITE_PROP();
						}
				   	  
				     });
				     RCSPanel.add(INPUT_RCSZISP);
		     //----------------------------------------------------------------------------------
		     //						Aerodynamic Input
		     //----------------------------------------------------------------------------------
		     
		      JLabel LABEL_SurfaceArea = new JLabel("S/C Surface Area [m\u00b2]");
		      LABEL_SurfaceArea.setLocation(INPUT_width+35, uy_p41 + 25 * 1 );
		      LABEL_SurfaceArea.setSize(300, 20);
		      LABEL_SurfaceArea.setBackground(backgroundColor);
		      LABEL_SurfaceArea.setForeground(labelColor);
		      AerodynamicInputPanel.add(LABEL_SurfaceArea);
		      
		      JLabel LABEL_BallisticCoefficient = new JLabel("Ballistic Coefficient [kg/m\u00b2]");
		      LABEL_BallisticCoefficient.setLocation(INPUT_width+35, uy_p41 + 25 * 2 );
		      LABEL_BallisticCoefficient.setSize(300, 20);
		      LABEL_BallisticCoefficient.setBackground(backgroundColor);
		      LABEL_BallisticCoefficient.setForeground(labelColor);
		      AerodynamicInputPanel.add(LABEL_BallisticCoefficient);
		      
			     double value = Math.sqrt(4/PI*readFromFile(SC_file, 2));
			     //System.out.println(readFromFile(Aero_file, 2)+" | "+value); 
			     INPUT_ParachuteDiameter = new JTextField(""+decAngularRate.format(value)){
					    /**
						 * 
						 */
						private static final long serialVersionUID = 1L;

							@Override public void setBorder(Border border) {
						        // No!
						    }
						};
			     INPUT_ParachuteDiameter.setLocation(2, uy_p41 + 25 * 4);
			     INPUT_ParachuteDiameter.setSize(INPUT_width, 20);
			     INPUT_ParachuteDiameter.setBackground(backgroundColor);
			     INPUT_ParachuteDiameter.setForeground(valueColor);
			     INPUT_ParachuteDiameter.setHorizontalAlignment(JTextField.RIGHT);
			     INPUT_ParachuteDiameter.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
						WRITE_SC();
					}
			   	  
			     });
			     AerodynamicInputPanel.add(INPUT_ParachuteDiameter);
			     
			     INPUT_SURFACEAREA = new JTextField(10){
					    /**
						 * 
						 */
						private static final long serialVersionUID = 1L;

							@Override public void setBorder(Border border) {
						        // No!
						    }
						};
			     INPUT_BALLISTICCOEFFICIENT = new JTextField(10){
					    /**
						 * 
						 */
						private static final long serialVersionUID = 1L;

							@Override public void setBorder(Border border) {
						        // No!
						    }
						};

			     INPUT_SURFACEAREA.setLocation(2, uy_p41 + 25 * 1 );;
			     INPUT_SURFACEAREA.setSize(INPUT_width, 20);
			     INPUT_SURFACEAREA.setBackground(backgroundColor);
			     INPUT_SURFACEAREA.setForeground(valueColor);
			     INPUT_SURFACEAREA.setHorizontalAlignment(JTextField.RIGHT);
			     INPUT_SURFACEAREA.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
						WRITE_SC();
						EvaluateSurfaceAreaSetup() ;
					}
			   	  
			     });
			     AerodynamicInputPanel.add(INPUT_SURFACEAREA);
			     
			     INPUT_BALLISTICCOEFFICIENT.setLocation(2, uy_p41 + 25 * 2 );
			     INPUT_BALLISTICCOEFFICIENT.setSize(INPUT_width, 20);
			     INPUT_BALLISTICCOEFFICIENT.setBackground(backgroundColor);
			     INPUT_BALLISTICCOEFFICIENT.setForeground(valueColor);
			     INPUT_BALLISTICCOEFFICIENT.setHorizontalAlignment(JTextField.RIGHT);
			     INPUT_BALLISTICCOEFFICIENT.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
						WRITE_SC();
						EvaluateSurfaceAreaSetup() ;
					}
			   	  
			     });
			     AerodynamicInputPanel.add(INPUT_BALLISTICCOEFFICIENT);
			     
			      RB_SurfaceArea =new JRadioButton("");    
			      RB_BallisticCoefficient =new JRadioButton("");    
			     //r1.setBounds(75,50,100,30);    
			      RB_SurfaceArea.setLocation(INPUT_width+5, uy_p41 + 25 * 1 );
			      RB_SurfaceArea.setSize(22,22);
			      RB_SurfaceArea.setForeground(labelColor);
			      RB_SurfaceArea.setBackground(backgroundColor);
			     //r2.setBounds(75,100,100,30); 
			      RB_BallisticCoefficient.setLocation(INPUT_width+5, uy_p41 + 25 * 2 );
			      RB_BallisticCoefficient.setSize(22,22);
			      RB_BallisticCoefficient.setBackground(backgroundColor);
			      

				     ButtonGroup bg=new ButtonGroup();    
				     bg.add(RB_SurfaceArea);bg.add(RB_BallisticCoefficient); 
				     AerodynamicInputPanel.add(RB_SurfaceArea);
				     AerodynamicInputPanel.add(RB_BallisticCoefficient);
				     RB_SurfaceArea.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							// TODO Auto-generated method stub
							if(RB_SurfaceArea.isSelected()) {
								double BC = Double.parseDouble(INPUT_BALLISTICCOEFFICIENT.getText());
								double mass = Double.parseDouble(INPUT_M0.getText());
					    		INPUT_SURFACEAREA.setText(""+String.format("%.2f",mass/BC));
					    		INPUT_BALLISTICCOEFFICIENT.setText("");
					    		
					    		INPUT_SURFACEAREA.setEditable(true);
					    		INPUT_BALLISTICCOEFFICIENT.setEditable(false);	
					    		
							} else if (RB_BallisticCoefficient.isSelected()) {
								double surfacearea = Double.parseDouble(INPUT_SURFACEAREA.getText());
								double mass = Double.parseDouble(INPUT_M0.getText());
					    		INPUT_SURFACEAREA.setText("");
							INPUT_BALLISTICCOEFFICIENT.setText(""+String.format("%.2f", mass/surfacearea));
							
					    		INPUT_SURFACEAREA.setEditable(false);
					    		INPUT_BALLISTICCOEFFICIENT.setEditable(true);	
							}
							
						}
				    	 
				     });
				     RB_BallisticCoefficient.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							// TODO Auto-generated method stub
							if(RB_SurfaceArea.isSelected()) {
								double BC = Double.parseDouble(INPUT_BALLISTICCOEFFICIENT.getText());
								double mass = Double.parseDouble(INPUT_M0.getText());
					    		INPUT_SURFACEAREA.setText(""+String.format("%.2f",mass/BC));
					    		INPUT_BALLISTICCOEFFICIENT.setText("");
					    		
					    		INPUT_SURFACEAREA.setEditable(true);
					    		INPUT_BALLISTICCOEFFICIENT.setEditable(false);	
					    		
							} else if (RB_BallisticCoefficient.isSelected()) {
								double surfacearea = Double.parseDouble(INPUT_SURFACEAREA.getText());
								double mass = Double.parseDouble(INPUT_M0.getText());
					    		INPUT_SURFACEAREA.setText("");
							INPUT_BALLISTICCOEFFICIENT.setText(""+String.format("%.2f", mass/surfacearea));
							
					    		INPUT_SURFACEAREA.setEditable(false);
					    		INPUT_BALLISTICCOEFFICIENT.setEditable(true);	
					    		
							}
						}
				    	 
				     });
				     				     
				      JLabel LABEL_Parachute= new JLabel("Parachute Diameter [m]");
				      LABEL_Parachute.setLocation(INPUT_width+35, uy_p41 + 25 * 4 );
				      LABEL_Parachute.setSize(250, 20);
				      LABEL_Parachute.setBackground(backgroundColor);
				      LABEL_Parachute.setForeground(labelColor);
				      AerodynamicInputPanel.add(LABEL_Parachute);
				      
 
		      
		        String path = "images/milleniumSchlieren2.png";
		        File file = new File(path);
		        try {
		        BufferedImage image2 = ImageIO.read(file);
		        JLabel label = new JLabel(new ImageIcon(image2));
		        label.setSize(300,290);
		        label.setLocation(5, uy_p41 + 25 * 6);
		        label.setBorder(Moon_border);
		        AerodynamicInputPanel.add(label);
		        } catch (Exception e) {
		        	System.err.println("Error: SpaceShip Setup/Aerodynamik - could not load image");
		        }

		        
		        //--------------------------------------------------------------------------------------------------------	        
		        JPanel SequenceControlPanel = new JPanel();
		        SequenceControlPanel.setLayout(null);
		        SequenceControlPanel.setPreferredSize(new Dimension(400, 60));
		        SequenceControlPanel.setBackground(backgroundColor);
		        SequenceControlPanel.setForeground(labelColor);
		        guidanceNavigationAndControlPanel.add(SequenceControlPanel, BorderLayout.PAGE_START);


		        JButton BUTTON_AddController = new JButton("Add Controller");
		        BUTTON_AddController.setLocation(655, 5);
		        BUTTON_AddController.setSize(145,25);
		        BUTTON_AddController.addActionListener(new ActionListener() { 
		        	  public void actionPerformed(ActionEvent e) { 
		        		  AddController();
		        	  } } );
		        SequenceControlPanel.add(BUTTON_AddController);
		        JButton BUTTON_DeleteController = new JButton("Delete Controller");
		        BUTTON_DeleteController.setLocation(655, 32);
		        BUTTON_DeleteController.setSize(145,25);
		        BUTTON_DeleteController.addActionListener(new ActionListener() { 
		        	  public void actionPerformed(ActionEvent e) { 
		        		  DeleteController();
		        	  } } );
		        SequenceControlPanel.add(BUTTON_DeleteController);
		        
		        JButton BUTTON_AddError = new JButton("Add Error");
		        BUTTON_AddError.setLocation(805, 5);
		        BUTTON_AddError.setSize(145,25);
		        BUTTON_AddError.addActionListener(new ActionListener() { 
		        	  public void actionPerformed(ActionEvent e) { 
		        		  AddError();
		        	  } } );
		        SequenceControlPanel.add(BUTTON_AddError);
		        JButton BUTTON_DeleteError = new JButton("Delete Error");
		        BUTTON_DeleteError.setLocation(805, 32);
		        BUTTON_DeleteError.setSize(145,25);
		        BUTTON_DeleteError.addActionListener(new ActionListener() { 
		        	  public void actionPerformed(ActionEvent e) { 
		        		  DeleteError();
		        	  } } );
		        SequenceControlPanel.add(BUTTON_DeleteError);
		        //-----------------------------------------------------------------------------------------------------------------------------
		        //                  Sequence table 
		        //-----------------------------------------------------------------------------------------------------------------------------
		        JPanel Pane_Sequence = new JPanel();
		        Pane_Sequence.setLocation(0, 0);
		        Pane_Sequence.setPreferredSize(new Dimension(900, 250));
		        Pane_Sequence.setLayout(new BorderLayout());
		        Pane_Sequence.setBackground(backgroundColor);
		        Pane_Sequence.setForeground(labelColor);
		        guidanceNavigationAndControlPanel.add(Pane_Sequence, BorderLayout.PAGE_END);

		        JSplitPane SplitPane_Page2_Charts_HorizontalSplit = new JSplitPane();
		        SplitPane_Page2_Charts_HorizontalSplit.setOrientation(JSplitPane.HORIZONTAL_SPLIT );
		        SplitPane_Page2_Charts_HorizontalSplit.setDividerLocation(0.4);
		        SplitPane_Page2_Charts_HorizontalSplit.setDividerSize(3);
		        SplitPane_Page2_Charts_HorizontalSplit.setUI(new BasicSplitPaneUI() {
		               @SuppressWarnings("serial")
		    			public BasicSplitPaneDivider createDefaultDivider() {
		               return new BasicSplitPaneDivider(this) {
		                   @SuppressWarnings("unused")
		    				public void setBorder( Border b) {
		                   }

		                   @Override
		                       public void paint(Graphics g) {
		                       g.setColor(Color.gray);
		                       g.fillRect(0, 0, getSize().width, getSize().height);
		                           super.paint(g);
		                       }
		               };
		               }
		           });

		        SplitPane_Page2_Charts_HorizontalSplit.addComponentListener(new ComponentListener(){

		    			@Override
		    			public void componentHidden(ComponentEvent arg0) {
		    				// TODO Auto-generated method stub
		    				
		    			}

		    			@Override
		    			public void componentMoved(ComponentEvent arg0) {
		    				// TODO Auto-generated method stub
		    				//System.out.println("Line moved");	
		    				
		    			}

		    			@Override
		    			public void componentResized(ComponentEvent arg0) {
		    				// TODO Auto-generated method stub

		    			}

		    			@Override
		    			public void componentShown(ComponentEvent arg0) {
		    				// TODO Auto-generated method stub
		    				
		    			}
		       
		       	});
		        SplitPane_Page2_Charts_HorizontalSplit.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, 
		    		    new PropertyChangeListener() {
		    		        @Override
		    		        public void propertyChange(PropertyChangeEvent pce) {

		    		        }
		    		});
		        SplitPane_Page2_Charts_HorizontalSplit.setDividerLocation(700);
		        Pane_Sequence.add(SplitPane_Page2_Charts_HorizontalSplit, BorderLayout.CENTER);
		        
		        
		        
		        
		        
		        
		    	    TABLE_CONTROLLER = new JTable(){
		    	   	 
		    	    	/**
		    			 * 
		    			 */
		    			private static final long serialVersionUID = 1L;

		    			@Override
		    	    	public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
		    	            Component comp = super.prepareRenderer(renderer, row, col);
		    	           // String val_TLFC = (String) getModel().getValueAt(row, 1);


		    	           // comp.setFont(table_font);
		    	            
		    	            return comp;
		    	        }
		    	    };
		    	   // TABLE_SEQUENCE.setFont(table_font);
		    	    
		    		Action action4 = new AbstractAction()
		    	    {
		    	        /**
		    			 * 
		    			 */
		    			private static final long serialVersionUID = 1L;

		    			public void actionPerformed(ActionEvent e)
		    	        {WriteControllerINP();}
		    	    };
		    	    @SuppressWarnings("unused")
		    		TableCellListener tcl4 = new TableCellListener(TABLE_CONTROLLER, action4);
		    	    MODEL_CONTROLLER = new DefaultTableModel(){

		    			private static final long serialVersionUID = 1L;

		    			@Override
		    	        public boolean isCellEditable(int row, int column) {
		    	           //all cells false
		    				String Ctrl_type = (String) MODEL_CONTROLLER.getValueAt(row, 1);
		    				if (column == 0 ){
		    					return false;
		    				} else if (column==5 && Ctrl_type.equals("1") ){
		    					return false; 
		    				}  else if (column==6 && Ctrl_type.equals("1") ){
		    					return false; 
		    				}  else {
		    					return true; 
		    				}
		    	        }
		    	    }; 
		    	    MODEL_CONTROLLER.setColumnIdentifiers(COLUMS_CONTROLLER);
		    	    TABLE_CONTROLLER.setModel(MODEL_CONTROLLER);
		    	    TABLE_CONTROLLER.setBackground(backgroundColor);
		    	    TABLE_CONTROLLER.setBackground(backgroundColor);
		    	    TABLE_CONTROLLER.setForeground(labelColor);
		    	    TABLE_CONTROLLER.getTableHeader().setReorderingAllowed(false);
		    	    TABLE_CONTROLLER.setRowHeight(45);


		    		    TableColumn CtrId_colum   			 = TABLE_CONTROLLER.getColumnModel().getColumn(0);
		    		    TableColumn CtrType_column 	    	 = TABLE_CONTROLLER.getColumnModel().getColumn(1);
		    		    TableColumn CtrPGain_column  		 = TABLE_CONTROLLER.getColumnModel().getColumn(2);
		    		    TableColumn CtrIGain_column 	     = TABLE_CONTROLLER.getColumnModel().getColumn(3);
		    		    TableColumn CtrDGain_column 	  	 = TABLE_CONTROLLER.getColumnModel().getColumn(4);
		    		    TableColumn CtrMin_column 	  		 = TABLE_CONTROLLER.getColumnModel().getColumn(5);
		    		    TableColumn CtrMax_column 	  		 = TABLE_CONTROLLER.getColumnModel().getColumn(6);

		    		    
		    		    TABLE_CONTROLLER.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		    		    CtrId_colum.setPreferredWidth(50);
		    		    CtrType_column.setPreferredWidth(100);
		    		    CtrPGain_column.setPreferredWidth(100);
		    		    CtrIGain_column.setPreferredWidth(100);
		    		    CtrDGain_column.setPreferredWidth(100);
		    		    CtrMin_column.setPreferredWidth(120);
		    		    CtrMax_column.setPreferredWidth(120);

		    		    
		    		    ((JTable) TABLE_CONTROLLER).setFillsViewportHeight(true);
		    	    
		    		    TABLE_CONTROLLER.getTableHeader().setBackground(backgroundColor);
		    		    TABLE_CONTROLLER.getTableHeader().setForeground(labelColor);
		    		    
		    		    JScrollPane TABLE_CONTROLLER_ScrollPane = new JScrollPane(TABLE_CONTROLLER,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		    		    //TABLE_SEQUENCE_ScrollPane.setLayout(null);
		    		    TABLE_CONTROLLER_ScrollPane.getVerticalScrollBar().setBackground(backgroundColor);
		    		    TABLE_CONTROLLER_ScrollPane.getHorizontalScrollBar().setBackground(backgroundColor);
		    		    TABLE_CONTROLLER_ScrollPane.setBackground(backgroundColor);
		    		    //TABLE_SEQUENCE_ScrollPane.setSize(tablewidth3,tableheight3);
		    		    //TABLE_SEQUENCE_ScrollPane.setOpaque(false);
		    		   // P2_ControllerPane.add(TABLE_CONTROLLER_ScrollPane, BorderLayout.CENTER);
		    		    //PANEL_RIGHT_InputSection.add(TABLE_CONTROLLER_ScrollPane, BorderLayout.CENTER);
		    		    SplitPane_Page2_Charts_HorizontalSplit.add(TABLE_CONTROLLER_ScrollPane, JSplitPane.LEFT);
		    		    
		    		    //---------------------------------------------------------------------------------------------
		    		    TABLE_ERROR = new JTable(){
		    			   	 
		    		    	/**
		    				 * 
		    				 */
		    				private static final long serialVersionUID = 1L;

		    				@Override
		    		    	public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
		    		            Component comp = super.prepareRenderer(renderer, row, col);
		    		           // String val_TLFC = (String) getModel().getValueAt(row, 1);


		    		           // comp.setFont(table_font);
		    		            
		    		            return comp;
		    		        }
		    		    };
		    		   // TABLE_SEQUENCE.setFont(table_font);
		    		    
		    			Action action5 = new AbstractAction()
		    		    {
		    		        /**
		    				 * 
		    				 */
		    				private static final long serialVersionUID = 1L;

		    				public void actionPerformed(ActionEvent e)
		    		       {
		    					WriteErrorINP();	
		    					Update_ErrorIndicator();
		    		       }
		    		    };
		    		    @SuppressWarnings("unused")
		    			TableCellListener tcl5 = new TableCellListener(TABLE_ERROR, action5);
		    		    MODEL_ERROR = new DefaultTableModel(){

		    				private static final long serialVersionUID = 1L;

		    				@Override
		    		        public boolean isCellEditable(int row, int column) {
		    		           //all cells false
		    					if (column == 0 ){
		    						return false;
		    					} else {
		    						return true; 
		    					}
		    		        }
		    		    }; 
		    		    MODEL_ERROR.setColumnIdentifiers(COLUMS_ERROR);
		    		    TABLE_ERROR.setModel(MODEL_ERROR);
		    		    TABLE_ERROR.setBackground(backgroundColor);
		    		    TABLE_ERROR.setBackground(backgroundColor);
		    		    TABLE_ERROR.setForeground(labelColor);
		    		    TABLE_ERROR.getTableHeader().setReorderingAllowed(false);
		    		    TABLE_ERROR.setRowHeight(45);


		    			    TableColumn ErrorID_colum   			 = TABLE_ERROR.getColumnModel().getColumn(0);
		    			    TableColumn ErrorType_column 	    	 = TABLE_ERROR.getColumnModel().getColumn(1);
		    			    TableColumn ErrorTrigger_column  		 = TABLE_ERROR.getColumnModel().getColumn(2);
		    			    TableColumn ErrorValue_column  			 = TABLE_ERROR.getColumnModel().getColumn(3);
		    			    
		    			    TABLE_ERROR.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		    			    ErrorID_colum.setPreferredWidth(50);
		    			    ErrorType_column.setPreferredWidth(100);
		    			    ErrorTrigger_column.setPreferredWidth(100);
		    			    ErrorValue_column.setPreferredWidth(100);


		    			    
		    			    ((JTable) TABLE_ERROR).setFillsViewportHeight(true);
		    		    
		    			    TABLE_ERROR.getTableHeader().setBackground(backgroundColor);
		    			    TABLE_ERROR.getTableHeader().setForeground(labelColor);
		    			    
		    			    ErrorTypeCombobox.setBackground(backgroundColor);
		    			    try {
		    			    for (int i=0;i<ErrorType.length;i++) {
		    			    	ErrorTypeCombobox.addItem(ErrorType[i]);
		    			    }
		    			    } catch(NullPointerException eNPE) {
		    			    	System.out.println(eNPE);
		    			    }
		    			    ErrorType_column.setCellEditor(new DefaultCellEditor(ErrorTypeCombobox));
		    			    
		    			    JScrollPane TABLE_ERROR_ScrollPane = new JScrollPane(TABLE_ERROR,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		    			    TABLE_ERROR_ScrollPane.getVerticalScrollBar().setBackground(backgroundColor);
		    			    TABLE_ERROR_ScrollPane.getHorizontalScrollBar().setBackground(backgroundColor);
		    			    TABLE_ERROR_ScrollPane.setBackground(backgroundColor);
		    			    SplitPane_Page2_Charts_HorizontalSplit.add(TABLE_ERROR_ScrollPane, JSplitPane.RIGHT);

        //-----------------------------------------------------------------------------------------
        // Page 4.3
        //-----------------------------------------------------------------------------------------
		JPanel SouthPanel = new JPanel();
		SouthPanel.setLayout(null);
		//mainPanelh1.setLocation(0, 0);
		SouthPanel.setBackground(backgroundColor);
		SouthPanel.setForeground(labelColor);
		SouthPanel.setPreferredSize(new Dimension(1200, 120));
		PageX04_Map.add(SouthPanel, BorderLayout.SOUTH);
	    
        int uy2 = 10; 

       
        JLabel LABEL_PageMapLONG = new JLabel("Longitude [deg]");
        LABEL_PageMapLONG.setLocation(425, uy2 + 0 );
        LABEL_PageMapLONG.setSize(250, 20);
        LABEL_PageMapLONG.setBackground(backgroundColor);
        LABEL_PageMapLONG.setForeground(labelColor);
        SouthPanel.add(LABEL_PageMapLONG);
        JLabel LABEL_PageMapLAT = new JLabel("Latitude [deg]");
        LABEL_PageMapLAT.setLocation(825, uy2 + 0 );
        LABEL_PageMapLAT.setSize(250, 20);
        LABEL_PageMapLAT.setBackground(backgroundColor);
        LABEL_PageMapLAT.setForeground(labelColor);
        SouthPanel.add(LABEL_PageMapLAT);	
        
         INDICATOR_PageMap_LONG = new JLabel();
        INDICATOR_PageMap_LONG.setLocation(425, uy2 + 30 );
        INDICATOR_PageMap_LONG.setText("");
        INDICATOR_PageMap_LONG.setSize(80, 20);
        SouthPanel.add(INDICATOR_PageMap_LONG);
         INDICATOR_PageMap_LAT = new JLabel();
        INDICATOR_PageMap_LAT.setLocation(825, uy2 + 30 );
        INDICATOR_PageMap_LAT.setText("");
        INDICATOR_PageMap_LAT.setSize(80, 20);
        SouthPanel.add(INDICATOR_PageMap_LAT);

       PolarMapContainer = new JPanel(new GridBagLayout());
       PolarMapContainer.setLocation(0, 0);
       PolarMapContainer.setPreferredSize(new Dimension(extx_main, exty_main));
       PolarMapContainer.setBackground(backgroundColor);
       PageX04_PolarMap.add(PolarMapContainer, BorderLayout.CENTER);
 	  //-----------------------------------------------------------------------------------------
 	  // 										Page: Raw Data
 	  //-----------------------------------------------------------------------------------------
       
	    TABLE_RAWData = new JTable();	    
	    MODEL_RAWData = new DefaultTableModel(){

			private static final long serialVersionUID = 1L;

			@Override
	        public boolean isCellEditable(int row, int column) {
	           //all cells false
					return false;
	        }
	    }; 
	    MODEL_RAWData.setColumnIdentifiers(Axis_Option_NR);
	    TABLE_RAWData.setModel(MODEL_RAWData);
	    TABLE_RAWData.setBackground(backgroundColor);
	    TABLE_RAWData.setBackground(backgroundColor);
	    TABLE_RAWData.setForeground(labelColor);
	    TABLE_RAWData.getTableHeader().setReorderingAllowed(false);
	    TABLE_RAWData.setRowHeight(18);
		TABLE_RAWData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		((JTable) TABLE_RAWData).setFillsViewportHeight(true);
		TABLE_RAWData.getTableHeader().setBackground(backgroundColor);
		TABLE_RAWData.getTableHeader().setForeground(labelColor);

		    JScrollPane TABLE_RAWData_ScrollPane = new JScrollPane(TABLE_RAWData,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		    TABLE_RAWData_ScrollPane.getVerticalScrollBar().setBackground(backgroundColor);
		    TABLE_RAWData_ScrollPane.getHorizontalScrollBar().setBackground(backgroundColor);
		    TABLE_RAWData_ScrollPane.setBackground(backgroundColor);
		    PageX04_RawDATA.add(TABLE_RAWData_ScrollPane);
       
       
       
	//-------------------------------------------------------------------------------------------------------------------------------
        // Create Charts:
       CreateChart_DashboardOverviewChart_Altitude_Velocity();
       //CreateChart_DashboardOverviewChart_Time_FPA();

     	CreateChart_DashBoardFlexibleChart();
     	createChart_3DRotation();
     	CreateChart_MercatorMap();
     	CreateChart_PolarMap();
     	
     	//CreateChart_GroundClearance();
     	//---------------------------------------------------------------------
     	// Prepare icons
     	ImageIcon icon_dashboard = null;
     	ImageIcon icon_scSetup = null;
     	ImageIcon icon_setup = null;
     	ImageIcon icon_data = null;
     	ImageIcon icon_map = null;
     	if(OS_is==1) {
     	 icon_dashboard = new ImageIcon("images/homeIcon.png","");
     	 icon_scSetup = new ImageIcon("images/startup.png","");
     	 icon_setup = new ImageIcon("images/setup.png","");
     	 icon_data = new ImageIcon("images/data.png","");
     	 icon_map = new ImageIcon("images/map.png","");
     	} else if(OS_is==2) {
     	//	For Windows image icons have to be resized
        	 icon_dashboard = new ImageIcon("images/homeIcon.png","");
         icon_scSetup = new ImageIcon("images/startup.png","");
         icon_setup = new ImageIcon("images/setup.png","");
         icon_data = new ImageIcon("images/data.png","");
         icon_map = new ImageIcon("images/map.png","");
         	 int size=10;
     		icon_dashboard = new ImageIcon(getScaledImage(icon_dashboard.getImage(),size,size));
     		icon_scSetup = new ImageIcon(getScaledImage(icon_scSetup.getImage(),size,size));
     		icon_setup = new ImageIcon(getScaledImage(icon_setup.getImage(),size,size));
     		icon_data = new ImageIcon(getScaledImage(icon_data.getImage(),size,size));
     		icon_map = new ImageIcon(getScaledImage(icon_map.getImage(),size,size));
     	}
     	// Create Tabs:
        Page04_subtabPane.addTab("Dashboard" , icon_dashboard, PageX04_Dashboard, null);
        Page04_subtabPane.addTab("Simulation Setup"+"\u2713", icon_setup, PageX04_SimSetup, null);
        Page04_subtabPane.addTab("SpaceShip Setup"+"\u2713", icon_scSetup, PageX04_AttitudeSetup, null);
        Page04_subtabPane.addTab("Raw Data", icon_data, PageX04_RawDATA, null);
        Page04_subtabPane.addTab("Map" , icon_map, PageX04_Map, null);
        Page04_subtabPane.addTab("Polar Map" , icon_map, PageX04_PolarMap, null);
       // Page04_subtabPane.addTab("GroundClearance" , null, PageX04_GroundClearance, null);
        //Page04_subtabPane.addTab("Results" , null, PageX04_3, null);
        Page04_subtabPane.setFont(small_font);
        Page04_subtabPane.setBackground(Color.black);
        Page04_subtabPane.setForeground(Color.black);
        MainGUI.add(Page04_subtabPane);
        Page04_subtabPane.setSelectedIndex(0);
    		//CreateChart_A01();
        // The following prevents long load up times when opening the GUI 
    		try {     long filesize = 	new File(RES_File).length()/1000000;
			    	    if(filesize<10) {
							SET_MAP(indx_target);
			    	    }
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println(e1);
				System.out.println("ERROR: Loading map failed.");
			}
        //------------------------------------------------------------------------
        	// Check filesize 
    	    long filesize = 	new File(RES_File).length()/1000000;
    	    if(filesize<10) {
    		UPDATE_Page01(true);
    	    } else {
    	    	UPDATE_Page01(false);
    	    	System.out.println("Full data import supressed. Filesize prohibits fast startup.");
    	    }
    	    READ_INPUT();
    	       createTargetView3D();
    	       createTargetWindow();
    		READ_CONTROLLER();
  	      		UpdateFC_LIST();
    		READ_SEQUENCE();
    		READ_ERROR();
    		READ_INERTIA() ;
    		READ_InitialAttitude();
    		Update_ErrorIndicator();
    		  basicSidePanelLeft.Update_IntegratorSettings();
    	      Update_DashboardFlexibleChart2();
    	      try {
    	      READ_sequenceFile();
    	      } catch(Exception e) {
    	    	  System.out.println("ERROR: Reading sequenceFile.inp failed.");
    	      }

        MainGUI.setOpaque(true);
        return MainGUI;
	}
    public void actionPerformed(ActionEvent e)  {
    	
    }
    
    static Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
    

    
    @SuppressWarnings("unchecked")
	public static void UpdateFC_LIST() {
    	try {
    	SequenceTVCFCCombobox.removeAllItems();
    	SequenceFCCombobox.removeAllItems();
    	} catch(NullPointerException | java.lang.ArrayIndexOutOfBoundsException eNPE) {
    		System.out.println("ERROR: Removing Combobox items failed");
    	}
    	SequenceTVCFCCombobox.addItem("");
    	SequenceFCCombobox.addItem("");
    	for(int i=0;i<MODEL_CONTROLLER.getRowCount();i++) {
    		SequenceTVCFCCombobox.addItem("PID "+(i+1));
    		SequenceFCCombobox.addItem("PID "+(i+1));
    		}
    }

    
    public static void AddSequence() {
    	int NumberOfSequences = MODEL_SEQUENCE.getRowCount();
    	ROW_SEQUENCE[0]  = ""+NumberOfSequences;
    	ROW_SEQUENCE[1]  = ""+SequenceENDType[0];
    	ROW_SEQUENCE[2]  = "0";
    	ROW_SEQUENCE[3]  = ""+SequenceType[0];
    	ROW_SEQUENCE[4]  = ""+SequenceFC[0];
    	ROW_SEQUENCE[5]  = "1";
    	ROW_SEQUENCE[6]  = "1";
    	ROW_SEQUENCE[7]  = ""+FCTargetCurve[0];	
    	ROW_SEQUENCE[8]  = ""+SequenceTVCFC[0];
    	ROW_SEQUENCE[9]  = "1";
    	ROW_SEQUENCE[10] = "1";
    	ROW_SEQUENCE[11] = ""+TargetCurve_Options_TVC[0];	
    	MODEL_SEQUENCE.addRow(ROW_SEQUENCE);
    	
    	for(int i=0;i<MODEL_SEQUENCE.getRowCount();i++) {MODEL_SEQUENCE.setValueAt(""+i,i, 0);}    	
    	WriteSequenceINP();
    }
    
    public static void DeleteSequence() {
    	int j = TABLE_SEQUENCE.getSelectedRow();
    	if (j >= 0){MODEL_SEQUENCE.removeRow(j);}
    	for(int i=0;i<MODEL_SEQUENCE.getRowCount();i++) {MODEL_SEQUENCE.setValueAt(""+i,i, 0);}
    	
    	WriteSequenceINP();
    }
    public static void DeleteAllSequence() {
    	for(int j=MODEL_SEQUENCE.getRowCount()-1;j>=0;j--) {MODEL_SEQUENCE.removeRow(j);}
    	WriteSequenceINP();
    }
    
    public static void UpSequence() {
        int[] rows2 = TABLE_SEQUENCE.getSelectedRows();
        MODEL_SEQUENCE.moveRow(rows2[0],rows2[rows2.length-1],rows2[0]-1);
        TABLE_SEQUENCE.setRowSelectionInterval(rows2[0]-1, rows2[rows2.length-1]-1);
        for(int i=0;i<MODEL_SEQUENCE.getRowCount();i++) {MODEL_SEQUENCE.setValueAt(""+i,i, 0);}
        
        WriteSequenceINP();
    }
    
    public static void DownSequence() {
        int[] rows2 = TABLE_SEQUENCE.getSelectedRows();
        MODEL_SEQUENCE.moveRow(rows2[0],rows2[rows2.length-1],rows2[0]+1);
        TABLE_SEQUENCE.setRowSelectionInterval(rows2[0]+1, rows2[rows2.length-1]+1);
        for(int i=0;i<MODEL_SEQUENCE.getRowCount();i++) {MODEL_SEQUENCE.setValueAt(""+i,i, 0);}
        
        WriteSequenceINP();
    }
    
    public static void AddController() {
    	int NumberOfSequences = MODEL_CONTROLLER.getRowCount();
    	ROW_CONTROLLER[0]  = ""+NumberOfSequences;
    	ROW_CONTROLLER[1]  = "0";
    	ROW_CONTROLLER[2]  = "1";
    	ROW_CONTROLLER[3]  = "1";
    	ROW_CONTROLLER[4]  = "1";
    	ROW_CONTROLLER[5]  = "0";
    	ROW_CONTROLLER[6]  = "1";

    	MODEL_CONTROLLER.addRow(ROW_CONTROLLER);
    	
    	for(int i=0;i<MODEL_CONTROLLER.getRowCount();i++) {MODEL_CONTROLLER.setValueAt(""+(i+1),i, 0);}    	
    	WriteControllerINP();
    	UpdateFC_LIST();
    }
    
    public static void DeleteController() {
    	int j = TABLE_CONTROLLER.getSelectedRow();
    	if (j >= 0){MODEL_CONTROLLER.removeRow(j);}
    	for(int i=0;i<MODEL_CONTROLLER.getRowCount();i++) {MODEL_CONTROLLER.setValueAt(""+(i+1),i, 0);}
    	
    	WriteControllerINP();
    	UpdateFC_LIST();
    }
    public static void DeleteAllController() {
    	for(int j=TABLE_CONTROLLER.getRowCount()-1;j>=0;j--) {MODEL_CONTROLLER.removeRow(j);}
    	WriteControllerINP();
    	UpdateFC_LIST();
    }
    
    public static void UpController() {
        int[] rows2 = TABLE_CONTROLLER.getSelectedRows();
        MODEL_CONTROLLER.moveRow(rows2[0],rows2[rows2.length-1],rows2[0]-1);
        TABLE_CONTROLLER.setRowSelectionInterval(rows2[0]-1, rows2[rows2.length-1]-1);
        for(int i=0;i<MODEL_CONTROLLER.getRowCount();i++) {MODEL_CONTROLLER.setValueAt(""+(i+1),i, 0);}
        
        WriteControllerINP();
        UpdateFC_LIST();
    }
    
    public static void DownController() {
        int[] rows2 = TABLE_SEQUENCE.getSelectedRows();
        MODEL_CONTROLLER.moveRow(rows2[0],rows2[rows2.length-1],rows2[0]+1);
        TABLE_CONTROLLER.setRowSelectionInterval(rows2[0]+1, rows2[rows2.length-1]+1);
        for(int i=0;i<MODEL_CONTROLLER.getRowCount();i++) {MODEL_CONTROLLER.setValueAt(""+(i+1),i, 0);}
        
        WriteControllerINP();
        UpdateFC_LIST();
    }
    public static void AddError() {
    	int NumberOfSequences = MODEL_ERROR.getRowCount();
    	ROW_ERROR[0]  = ""+NumberOfSequences;
    	ROW_ERROR[1]  = ""+ErrorType[0];
    	ROW_ERROR[2]  = "0";
    	ROW_ERROR[3]  = "0";

    	MODEL_ERROR.addRow(ROW_ERROR);
    	
    	for(int i=0;i<MODEL_ERROR.getRowCount();i++) {MODEL_ERROR.setValueAt(""+(i+1),i, 0);}    	
    	WriteErrorINP();
    	Update_ErrorIndicator();
    }
    
    public static void DeleteError() {
    	int j = TABLE_ERROR.getSelectedRow();
    	if (j >= 0){MODEL_ERROR.removeRow(j);}
    	for(int i=0;i<MODEL_ERROR.getRowCount();i++) {MODEL_ERROR.setValueAt(""+(i+1),i, 0);}
    	
    	WriteErrorINP();
    	Update_ErrorIndicator();
    }
    
    public static void Update_ErrorIndicator() {
    	if(MODEL_ERROR.getRowCount()>0) {
    		Error_Indicator.setText("Induced Error ON");
    		Error_Indicator.setBackground(Color.red);
    		Error_Indicator.setForeground(Color.red);
    	} else {
    		Error_Indicator.setText("Induced Error OFF");
    		Error_Indicator.setBackground(backgroundColor);
    		Error_Indicator.setForeground(labelColor);
    	}
   // 	Module_Indicator.setText(""+AscentDescent_SwitchChooser.getSelectedItem()); 
    }
    
    
    
    public static JButton getxAxisIndicator() {
		return xAxisIndicator;
	}
	public void UPDATE_Page01(boolean fullImport){
		  try {
			READ_INPUT();
			if(fullImport) {
			READ_RAWDATA();
			SET_MAP(indx_target);
			}
		} catch (IOException | URISyntaxException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	    	Update_DashboardFlexibleChart();
	    	Update_DashboardFlexibleChart2();
		  if(fullImport) {
	    	CHART_P1_DashBoardOverviewChart_Dataset_Time_FPA.removeAllSeries();

	    //	Update_DashboardFlexibleChart2();

	    	ResultSet_MercatorMap.removeAllSeries();
	    	try {
	    	ResultSet_MercatorMap = AddDataset_Mercator_MAP();
	    	} catch(ArrayIndexOutOfBoundsException | IOException eFNF2) {}
	    	Update_DashboardFlexibleChart();
	    	Update_DashboardFlexibleChart2();
      	    		try {
	        	    		result11_A3_1.removeAllSeries();
	        	    		result11_A3_2.removeAllSeries();
	        	    		result11_A3_3.removeAllSeries();
	        	    		result11_A3_4.removeAllSeries();
							UpdateChart_A01();
						} catch (ArrayIndexOutOfBoundsException | NullPointerException | IOException
								| URISyntaxException  e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
      	    		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
      	    		System.out.println("Updated "+timeStamp);
		  }
	  } 

    public static void READ_SEQUENCE() throws IOException{	
		BufferedReader br = new BufferedReader(new FileReader(SEQUENCE_File));
       String strLine;
       try {
       while ((strLine = br.readLine()) != null )   {
	       	String[] tokens = strLine.split(" ");
	       	int sequence_ID 			= Integer.parseInt(tokens[0]);
	       	int trigger_end_type 		= Integer.parseInt(tokens[1]);
	       	double trigger_end_value 	= Double.parseDouble(tokens[2]);
	       	int sequence_type		 	= Integer.parseInt(tokens[3]);
	       	int sequence_controller_ID 	= Integer.parseInt(tokens[4]);
	       	double ctrl_target_vel      = Double.parseDouble(tokens[5]);
	       	double ctrl_target_alt 		= Double.parseDouble(tokens[6]);
	       	int ctrl_target_curve       = Integer.parseInt(tokens[7]);
	    	ROW_SEQUENCE[0] = ""+sequence_ID;
	       	try {
	    	ROW_SEQUENCE[1] = ""+SequenceENDType[trigger_end_type];
	       	} catch ( java.lang.ArrayIndexOutOfBoundsException eAU) {System.out.println("ERROR: Reading sequence file - index out of bounds. " );}
	    	ROW_SEQUENCE[2] = ""+trigger_end_value;
	       	try {
	    	ROW_SEQUENCE[3] = ""+SequenceType[sequence_type-1];
	       	} catch ( java.lang.ArrayIndexOutOfBoundsException eAU) {System.out.println("ERROR: Reading sequence file - index out of bounds. " );}
	       	try {
	    	ROW_SEQUENCE[4] = ""+SequenceFCCombobox.getItemAt(sequence_controller_ID-1);
	       	} catch ( java.lang.ArrayIndexOutOfBoundsException eAU) {System.out.println("ERROR: Reading sequence file - index out of bounds. " );}
	    	ROW_SEQUENCE[5] = ""+ctrl_target_vel;
	    	ROW_SEQUENCE[6] = ""+ctrl_target_alt;
	       	try {
	    	ROW_SEQUENCE[7] = ""+FCTargetCurve[ctrl_target_curve-1];		
	       	} catch ( java.lang.ArrayIndexOutOfBoundsException eAU) {System.out.println("ERROR: Reading sequence file - index out of bounds. " );}
				       	try {
					       	int TVCsequence_controller_ID   = Integer.parseInt(tokens[8]);
					       	double ctrl_target_x_TVC        = Double.parseDouble(tokens[9]);
					       	double ctrl_target_y_TVC 		= Double.parseDouble(tokens[10]);
					       	int ctrl_TVC_target_curve       = Integer.parseInt(tokens[11]);
					    	ROW_SEQUENCE[8]  = "" + SequenceTVCFCCombobox.getItemAt(TVCsequence_controller_ID-1);
					    	ROW_SEQUENCE[9]  = "" + ctrl_target_x_TVC;
					    	ROW_SEQUENCE[10] = "" + ctrl_target_y_TVC*rad2deg;
					    	ROW_SEQUENCE[11] = "" + TargetCurve_Options_TVC[ctrl_TVC_target_curve-1];
				       	} catch(java.lang.ArrayIndexOutOfBoundsException eAIOOBE) {System.out.println("No TVC controller found in Sequence file.");}
	       	
				    	MODEL_SEQUENCE.addRow(ROW_SEQUENCE);
	    	for(int i=0;i<MODEL_SEQUENCE.getRowCount();i++) {MODEL_SEQUENCE.setValueAt(""+i,i, 0);} // Update numbering
       }
       br.close();
       } catch(NullPointerException eNPE) { System.out.println(eNPE);}

   }
    
    public static void READ_CONTROLLER() throws IOException{	
		BufferedReader br = new BufferedReader(new FileReader(CONTROLLER_File));
       String strLine;
       try {
       while ((strLine = br.readLine()) != null )   {
	       	String[] tokens = strLine.split(" ");
	    	ROW_CONTROLLER[0] = ""+Integer.parseInt(tokens[0]);
	    	ROW_CONTROLLER[1] = ""+Integer.parseInt(tokens[1]);
	    	ROW_CONTROLLER[2] = ""+Double.parseDouble(tokens[2]);
	    	ROW_CONTROLLER[3] = ""+Double.parseDouble(tokens[3]);
	    	ROW_CONTROLLER[4] = ""+Double.parseDouble(tokens[4]);
	    	ROW_CONTROLLER[5] = ""+Double.parseDouble(tokens[5]);
	    	ROW_CONTROLLER[6] = ""+Double.parseDouble(tokens[6]);
				    	MODEL_CONTROLLER.addRow(ROW_CONTROLLER);
	    	for(int i=0;i<MODEL_CONTROLLER.getRowCount();i++) {MODEL_CONTROLLER.setValueAt(""+(i+1),i, 0);} // Update numbering
       }
       br.close();
       } catch(NullPointerException eNPE) { System.out.println(eNPE);}

   }
    
    public static void READ_ERROR() throws IOException{	
		BufferedReader br = new BufferedReader(new FileReader(ERROR_File));
       String strLine;
       try {
       while ((strLine = br.readLine()) != null )   {
	       	String[] tokens = strLine.split(" ");
	    	ROW_ERROR[0] = "0";
	    	try {
	    	ROW_ERROR[1] = ""+ ErrorType[Integer.parseInt(tokens[0])];
	    	} catch(java.lang.ArrayIndexOutOfBoundsException eA) { System.out.println("Read Error file failed: Array Index out of Bounds.");}
	    	ROW_ERROR[2] = ""+Double.parseDouble(tokens[1]);
	    	ROW_ERROR[3] = ""+Double.parseDouble(tokens[2]);
				    	MODEL_ERROR.addRow(ROW_ERROR);
	    	for(int i=0;i<MODEL_ERROR.getRowCount();i++) {MODEL_ERROR.setValueAt(""+(i+1),i, 0);} // Update numbering
       }
       br.close();
       } catch(NullPointerException eNPE) { System.out.println(eNPE);}

   }
    
    public static void READ_sequenceFile() throws IOException{	
		BufferedReader br = new BufferedReader(new FileReader(sequenceFile));
       String strLine;
       String fcSeparator="\\|FlightControllerElements\\|";
       String eventSeparator="\\|EventManagementElements";
       String endSeparator="\\|EndElement\\|";
       int sequenceID=0;
       try {
       while ((strLine = br.readLine()) != null )   {
	       
	       	String[] initSplit = strLine.split(fcSeparator);

	       	String[] head = initSplit[0].split(" ");
	       //System.out.pri
	       //	int  ID = Integer.parseInt(head[0]);
	       	String sequenceName = head[1];
	       	int flightControllerIndex = Integer.parseInt(initSplit[1].split(" ")[1]);
	       	String[] arr     = strLine.split(eventSeparator);
	       	//System.out.println(arr[1]);
	       	int eventIndex  = Integer.parseInt(arr[1].split(" ")[1]);
	       	
	       	String[] arr2   = strLine.split(endSeparator);
	       	//System.out.println(arr2[1]);
	       	int endIndex    = Integer.parseInt(arr2[1].split(" ")[1]);
	       	double endValue = Double.parseDouble(arr2[1].split(" ")[2]);
	       	
	       //	System.out.println(ID+" "+sequenceName+" "+flightControllerIndex+" "+eventIndex+" "+endIndex+" "+endValue);
	       	
	       	if(sequenceID!=0) {
	       		GUISequenceElement.addGUISequenceElment();
	       	} 
	       	sequenceContentList.get(sequenceID).setSequenceName(sequenceName);
       		sequenceContentList.get(sequenceID).setFlightControllerSelectIndex(flightControllerIndex);
       		sequenceContentList.get(sequenceID).setEventSelectIndx(eventIndex);
       		sequenceContentList.get(sequenceID).setEndSelectIndex(endIndex);
       		sequenceContentList.get(sequenceID).setValueEnd(""+endValue);
	       	sequenceID++;
       }
       br.close();
       } catch(NullPointerException eNPE) { System.out.println(eNPE);}

   }
    
    public void READ_INERTIA() throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(INERTIA_File));
	       String strLine;
	       int j=0;
	       try {
	       while ((strLine = br.readLine()) != null )   {
		       	String[] tokens = strLine.split(" ");
		       	if(j==0) {
		       		INPUT_IXX.setText(tokens[0]);
		       		INPUT_IXY.setText(tokens[1]);
		       		INPUT_IXZ.setText(tokens[2]);
		       	} else if (j==1) {
		       		INPUT_IYX.setText(tokens[0]);
		       		INPUT_IYY.setText(tokens[1]);
		       		INPUT_IYZ.setText(tokens[2]);
		       	} else if (j==2) {
		       		INPUT_IZX.setText(tokens[0]);
		       		INPUT_IZY.setText(tokens[1]);
		       		INPUT_IZZ.setText(tokens[2]);
		       	}
		       	
		       	j++;
	       }
	       br.close();
	       } catch(NullPointerException eNPE) { System.out.println(eNPE);}
    }
    public void READ_INPUT() throws IOException{
    	double InitialState = 0;
       	FileInputStream fstream = null; 
try {
              fstream = new FileInputStream(Init_File);
} catch(IOException eIIO) { System.out.println(eIIO); System.out.println("ERROR: Reading init.inp failed.");} 
        DataInputStream in = new DataInputStream(fstream);
        @SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        int k = 0;
        try {
        while ((strLine = br.readLine()) != null )   {
        	String fullLine = strLine;
        //	System.out.println(fullLine);
        	String[] tokens = strLine.split(" ");
        if (k==0){
        	InitialState = Double.parseDouble(tokens[0]);
        		INDICATOR_LONG.setText(decf.format(InitialState));
        		SidePanelLeft.INPUT_LONG_Rs.setText(df_X4.format(InitialState));
        	} else if (k==1){
            	InitialState = Double.parseDouble(tokens[0]);
        		INDICATOR_LAT.setText(decf.format( InitialState));
        		SidePanelLeft.INPUT_LAT_Rs.setText(df_X4.format( InitialState));
        	} else if (k==2){
            	InitialState = Double.parseDouble(tokens[0]);
        		INDICATOR_ALT.setText(decf.format( InitialState));
        		SidePanelLeft.INPUT_ALT_Rs.setText(decf.format( InitialState));
        		h_init = InitialState;
        	} else if (k==3){
            	InitialState = Double.parseDouble(tokens[0]);
        		INDICATOR_VEL.setText(decf.format(InitialState));
        		SidePanelLeft.INPUT_VEL_Rs.setText(decf.format(InitialState));
        		v_init = InitialState;
        	} else if (k==4){
            	InitialState = Double.parseDouble(tokens[0]);
        		INDICATOR_FPA.setText(decf.format(InitialState));
        		SidePanelLeft.INPUT_FPA_Rs.setText(df_X4.format(InitialState));
        	} else if (k==5){
            	InitialState = Double.parseDouble(tokens[0]);
        		INDICATOR_AZI.setText(decf.format(InitialState));
        		SidePanelLeft.INPUT_AZI_Rs.setText(df_X4.format(InitialState));
        	} else if (k==6){
            	InitialState = Double.parseDouble(tokens[0]);
        		INDICATOR_M0.setText(decf.format(InitialState));
        		INPUT_M0.setText(decf.format(InitialState));
        		M0=InitialState;
        	} else if (k==7){
            	InitialState = Double.parseDouble(tokens[0]);
        		INDICATOR_INTEGTIME.setText(decf.format(InitialState));
        		INPUT_GlobalTime.setText(decf.format(InitialState));
        		 //MODEL_EventHandler.setValueAt(decf.format(InitialState), 0, 1);
        	} else if (k==8){
            	InitialState = Double.parseDouble(tokens[0]);
        		int Integ_indx = (int) InitialState;
        		SidePanelLeft.Integrator_chooser.setSelectedIndex(Integ_indx);
        } else if (k==9){
        	InitialState = Double.parseDouble(tokens[0]);
        		int Target_indx = (int) InitialState;
        		indx_target = (int) InitialState; 
        		RM = DATA_MAIN[indx_target][0];
        		INDICATOR_TARGET.setText(Target_Options[indx_target]);
        		Target_chooser.setSelectedIndex(Target_indx);
                if(indx_target==0) {
                	INDICATOR_TARGET.setBorder(Earth_border);
                } else if(indx_target==1){
                	INDICATOR_TARGET.setBorder(Moon_border);
                } else if(indx_target==2){
                	INDICATOR_TARGET.setBorder(Mars_border);
                } else if(indx_target==3){
                	INDICATOR_TARGET.setBorder(Venus_border);
                }
            } else if (k==10){
            	//InitialState = Double.parseDouble(tokens[0]);
	            //	INPUT_WRITETIME.setText(decf.format(InitialState)); // write dt
            } else if (k==11){
            	InitialState = Double.parseDouble(tokens[0]);
            	SidePanelLeft.INPUT_REFELEV.setText(decf.format(InitialState));       // Reference Elevation
		    } else if (k==12) {
		    	    // Time format :
		    		String UTCTime = fullLine;
		    		//System.out.println("handover string: "+UTCTime);
		    		SidePanelLeft.timePanel.updateTimeFromString(UTCTime);	    	
		    } else if (k==13) {
	        	InitialState = Double.parseDouble(tokens[0]);
			    	int Integ_indx = (int) InitialState;
			    	if(Integ_indx==1) {
			    	SELECT_VelocitySpherical.setSelected(true);
			    	}else {
			    SELECT_VelocityCartesian.setSelected(true);
			    	}
		    } else if (k==14) {
	        	InitialState = Double.parseDouble(tokens[0]);
			    	int Integ_indx = (int) InitialState;
			    	if(Integ_indx==3) {
			    		SELECT_3DOF.setSelected(true);
			    	}else if(Integ_indx==6){
			    		SELECT_6DOF.setSelected(true);
			    	}
		    } else if (k==15) {
	        	InitialState = Double.parseDouble(tokens[0]);
		    	SidePanelLeft.INPUT_AngularRate_X.setText(decAngularRate.format(InitialState));
		    } else if (k==16) {
	        	InitialState = Double.parseDouble(tokens[0]);
		    	SidePanelLeft.INPUT_AngularRate_Y.setText(decAngularRate.format(InitialState));
		    } else if (k==17) {
	        	InitialState = Double.parseDouble(tokens[0]);
		    	SidePanelLeft.INPUT_AngularRate_Z.setText(decAngularRate.format(InitialState));
		    } else if(k==18) {
	        	InitialState = Double.parseDouble(tokens[0]);
		    	INPUT_ControllerFrequency.setText(decf.format(InitialState));
		    }else if(k==19) {
	        	InitialState = Double.parseDouble(tokens[0]);
		    	INPUT_GlobalFrequency.setText(decf.format(InitialState));
		    }
        	k++;
        }
        in.close();
        br.close();
        fstream.close();
        } catch (NullPointerException eNPE) { System.out.println(eNPE);}

    //------------------------------------------------------------------
    // Read from PROP
    try {
        fstream = new FileInputStream(Prop_File);
} catch(IOException eIIO) { System.out.println(eIIO); System.out.println("ERROR: Reading prop.inp failed.");} 
  DataInputStream in3 = new DataInputStream(fstream);
  @SuppressWarnings("resource")
  BufferedReader br4 = new BufferedReader(new InputStreamReader(in3));
  k = 0;
  String strLine3;
  try {
  while ((strLine3 = br4.readLine()) != null )   {
  	String[] tokens = strLine3.split(" ");
  	if(tokens[0].isEmpty()==false) {
  	InitialState = Double.parseDouble(tokens[0]);
  	} else {
  		InitialState =0; 
  	}
    if (k==0){
    	INPUT_ISP.setText(df_X4.format(InitialState)); 
  	} else if (k==1){
  		INPUT_PROPMASS.setText(df_X4.format(InitialState)); 
  	//System.out.println(RM);
  	} else if (k==2){
  		INPUT_THRUSTMAX.setText(df_X4.format(InitialState));
  	} else if (k==3){
  		INPUT_THRUSTMIN.setText(df_X4.format(InitialState)); 
  		Propellant_Mass=InitialState;
  	} else if (k==4){
  		int value = (int) InitialState; 
  		if(value==1) {INPUT_ISPMODEL.setSelected(true);}else {INPUT_ISPMODEL.setSelected(false);}
  	} else if (k==5){
  		INPUT_ISPMIN.setText(df_X4.format(InitialState)); 
  	} else if (k==6){
  		INPUT_RCSX.setText(df_X4.format(InitialState)); 
  	} else if (k==7){
  		INPUT_RCSY.setText(df_X4.format(InitialState));
  	} else if (k==8) {
  		INPUT_RCSZ.setText(df_X4.format(InitialState));
  	} else if (k==9) {
  		INPUT_RCSXTHRUST.setText(df_X4.format(InitialState));
  	} else if (k==10) {
  		INPUT_RCSYTHRUST.setText(df_X4.format(InitialState));
  	} else if (k==11) {
  		INPUT_RCSZTHRUST.setText(df_X4.format(InitialState));
  	} else if(k==12) {
  		INPUT_RCSTANK.setText(df_X4.format(InitialState));
  	} else if(k==13) {
  		INPUT_RCSXISP.setText(df_X4.format(InitialState));
  	} else if(k==14) {
  		INPUT_RCSYISP.setText(df_X4.format(InitialState));
  	} else if(k==15) {
  		INPUT_RCSZISP.setText(df_X4.format(InitialState));
  	}
  	k++;
  }
  in3.close();
  br4.close();
  fstream.close();
  } catch (NullPointerException eNPE) { System.out.println(eNPE);}  
  //------------------------------------------------------------------
  // Read from AERO
  try {
      fstream = new FileInputStream(Aero_file);
} catch(IOException eIIO) { System.out.println(eIIO); System.out.println("ERROR: Reading aeroBasic.inp failed.");} 
DataInputStream in55 = new DataInputStream(fstream);
@SuppressWarnings("resource")
BufferedReader br55 = new BufferedReader(new InputStreamReader(in55));
k = 0;
String strLine55;
try {
while ((strLine55 = br55.readLine()) != null )   {
	String[] tokens = strLine55.split(" ");
	if(tokens[0].isEmpty()==false) {
	InitialState = Double.parseDouble(tokens[0]);
	} else {
		InitialState =0; 
	}
    if (k==0){
	  int index = (int) InitialState; 
		for(int j=0;j<DragModelSet.size();j++) {
			if(j==index) {
				DragModelSet.get(j).setSelected(true);
			}
		}
	} else if (k==1){
		ConstantCD_INPUT.setText(""+InitialState);
	//System.out.println(RM);
	} else if (k==2){
		INPUT_RB.setText(""+(InitialState)); 
	} else if (k==5) {
		
	}
	k++;
}
in55.close();
br55.close();
fstream.close();
} catch (NullPointerException eNPE) { System.out.println(eNPE);}  
//--------------------------------------------------------------------------------------------------------
  //--------------------------------------------------------------------------------------------------------
  // Integrator settings 
  //--------------------------------------------------------------------------------------------------------
	String integ_file = null;  
	if(	SidePanelLeft.Integrator_chooser.getSelectedIndex()==0) {
		integ_file = INTEG_File_01; 
	} else if (	SidePanelLeft.Integrator_chooser.getSelectedIndex()==1) {
		integ_file = INTEG_File_02; 
	} else if (	SidePanelLeft.Integrator_chooser.getSelectedIndex()==2) {
		integ_file = INTEG_File_03; ;
	} else if (	SidePanelLeft.Integrator_chooser.getSelectedIndex()==3) {
		integ_file = INTEG_File_04; 
	}
    try {
        fstream = new FileInputStream(integ_file);
} catch(IOException | NullPointerException eIIO) { System.out.println(eIIO); System.out.println("ERROR: Reading integrator input failed. " + integ_file);} 
  in3 = new DataInputStream(fstream);
  @SuppressWarnings("resource")
  BufferedReader br5 = new BufferedReader(new InputStreamReader(in3));
  k = 0;
  try {
  while ((strLine3 = br5.readLine()) != null )   {
  	String[] tokens = strLine3.split(" ");
  	InitialState = Double.parseDouble(tokens[0]);
    if (k==0){
    		SidePanelLeft.	INPUT_IntegratorSetting_01.setText(""+(InitialState)); 
  	} else if (k==1){
  		SidePanelLeft.INPUT_IntegratorSetting_02.setText(""+(InitialState)); 
  	} else if (k==2){
  		SidePanelLeft.INPUT_IntegratorSetting_03.setText(""+(InitialState)); 
  	} else if (k==3){
  		SidePanelLeft.INPUT_IntegratorSetting_04.setText(""+(InitialState)); 
  	} else if (k==4){
  		SidePanelLeft.INPUT_IntegratorSetting_05.setText(""+(InitialState)); 
  	} else if (k==5){

  	} else if (k==6){

  	} else if (k==7){

  	}
  	k++;
  }
  in3.close();
  br5.close();
  fstream.close();
  } catch (NullPointerException eNPE) { System.out.println(eNPE);}  
  //-------------------------------------------------------------------------------------------------------------------
  try {
      fstream = new FileInputStream(SC_file);
} catch(IOException eIIO) { System.out.println(eIIO); System.out.println("ERROR: Reading integrator input failed. " + integ_file);} 
in3 = new DataInputStream(fstream);
@SuppressWarnings("resource")
BufferedReader br6 = new BufferedReader(new InputStreamReader(in3));
k = 0;
try {
while ((strLine3 = br6.readLine()) != null )   {
	String[] tokens = strLine3.split(" ");
	InitialState = Double.parseDouble(tokens[0]);
  if (k==0){
  		INPUT_SURFACEAREA.setText(""+(InitialState)); 
  		if(InitialState!=0) {
  			RB_SurfaceArea.setSelected(true);
  			INPUT_SURFACEAREA.setEditable(true);
  			INPUT_BALLISTICCOEFFICIENT.setEditable(false);	
  		}
	} else if (k==1){
		INPUT_BALLISTICCOEFFICIENT.setText(""+(InitialState)); 
  		if(InitialState!=0) {
  			RB_BallisticCoefficient.setSelected(true);
  			INPUT_SURFACEAREA.setEditable(false);
  			INPUT_BALLISTICCOEFFICIENT.setEditable(true);	
  		}
	} else if (k==2){
		
	} else if (k==3){

	} else if (k==4){
	
	} else if (k==5){

	} else if (k==6){

	} else if (k==7){

	}
	k++;
}
EvaluateSurfaceAreaSetup() ;
in3.close();
br5.close();
fstream.close();
} catch (NullPointerException eNPE) { System.out.println(eNPE);} 
    }
    
    public static void READ_RAWDATA() {
    	resultSet.clear();
    try {
		analysisFile = 	readResultFileList(RES_File);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    	// Delete all exisiting rows:
    	for(int j=MODEL_RAWData.getRowCount()-1;j>=0;j--) {MODEL_RAWData.removeRow(j);}
    	// Read all data from file: 
	    FileInputStream fstream = null;
		try{ fstream = new FileInputStream(RES_File);} catch(IOException eIO) { System.out.println(eIO);}
	              DataInputStream in = new DataInputStream(fstream);
	              BufferedReader br = new BufferedReader(new InputStreamReader(in));
	              String strLine;
	              try {
							while ((strLine = br.readLine()) != null )   {
								Object[] tokens = strLine.split(" ");
							    MODEL_RAWData.addRow(tokens);
						     	RealTimeResultSet resultElement = new RealTimeResultSet();
							    double[] CartesianPosition = {Double.parseDouble((String) tokens[41]),
			 							   						Double.parseDouble((String) tokens[42]),
			 							   						Double.parseDouble((String) tokens[43])};
							    resultElement.setCartesianPosECEF(CartesianPosition);
							    resultElement.setEulerX(Double.parseDouble((String) tokens[57]));
							    resultElement.setEulerY(Double.parseDouble((String) tokens[58]));
							    resultElement.setEulerZ(Double.parseDouble((String) tokens[59]));
							    resultElement.setVelocity(Double.parseDouble((String) tokens[6]) );
							    resultElement.setTime(Double.parseDouble((String) tokens[0]));
							    resultElement.setFpa(Double.parseDouble((String) tokens[7]));
							    resultSet.add(resultElement);
							  
							  }
			       fstream.close();
			       in.close();
			       br.close();

	              } catch (NullPointerException | IOException eNPE) { 
	            	  System.out.println("Read raw data, Nullpointerexception");
					}catch(IllegalArgumentException eIAE) {
					  System.out.println("Read raw data, illegal argument error");
					}
    }
    
    public static void READ_INTEG() {
    	  //--------------------------------------------------------------------------------------------------------
    	  // Integrator settings 
    	  //--------------------------------------------------------------------------------------------------------
    		String integ_file = null;
    		 FileInputStream  fstream = null; 
    		if(	SidePanelLeft.Integrator_chooser.getSelectedIndex()==0) {
    			integ_file = INTEG_File_01; 
    		} else if (	SidePanelLeft.Integrator_chooser.getSelectedIndex()==1) {
    			integ_file = INTEG_File_02; 
    		} else if (	SidePanelLeft.Integrator_chooser.getSelectedIndex()==2) {
    			integ_file = INTEG_File_03; ;
    		} else if (	SidePanelLeft.Integrator_chooser.getSelectedIndex()==3) {
    			integ_file = INTEG_File_04; 
    		}
    	    try {
    	         fstream = new FileInputStream(integ_file);
    	} catch(IOException eIIO) { System.out.println(eIIO); System.out.println("ERROR: Reading integrator input failed. " + integ_file);} 
    	  DataInputStream in3 = new DataInputStream(fstream);
    	  @SuppressWarnings("resource")
    	  BufferedReader br5 = new BufferedReader(new InputStreamReader(in3));
    	  int k = 0;
    	  String strLine3="" ; 
    	  try {
    	  try {
			while ((strLine3 = br5.readLine()) != null )   {
			  	String[] tokens = strLine3.split(" ");
			  	double InitialState = Double.parseDouble(tokens[0]);
			    if (k==0){
			    		SidePanelLeft.INPUT_IntegratorSetting_01.setText(""+(InitialState)); 
			  	} else if (k==1){
			  		SidePanelLeft.INPUT_IntegratorSetting_02.setText(""+(InitialState)); 
			  	} else if (k==2){
			  		SidePanelLeft.INPUT_IntegratorSetting_03.setText(""+(InitialState)); 
			  	} else if (k==3){
			  		SidePanelLeft.INPUT_IntegratorSetting_04.setText(""+(InitialState)); 
			  	} else if (k==4){
			  		SidePanelLeft.INPUT_IntegratorSetting_05.setText(""+(InitialState)); 
			  	} else if (k==5){

			  	} else if (k==6){

			  	} else if (k==7){

			  	}
			  	k++;
			  }
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	  try {
    		  in3.close();
	    	  br5.close();
	    	  fstream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	  } catch (NullPointerException eNPE) { System.out.println(eNPE);}  	
    }
    
    public static void READ_InitialAttitude() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(InitialAttitude_File));
	       String strLine;
	       int j=0;
	       try {
	       while ((strLine = br.readLine()) != null )   {
		       	String[] tokens = strLine.split(" ");
		       	if(j==0) {
		       		INPUT_Quarternion1.setText(tokens[0]);
		       	} else if (j==1) {
		       		INPUT_Quarternion2.setText(tokens[0]);
		       	} else if (j==2) {
		       		INPUT_Quarternion3.setText(tokens[0]);
		       	} else if (j==3) {
		       		INPUT_Quarternion4.setText(tokens[0]);
		       	}	       	
		       	j++;
	       }
	       br.close();
	       } catch(NullPointerException eNPE) { System.out.println(eNPE);}
    }
    
    public static List<AnimationSet>  READ_AnimationData() {
     int indx_time=0;
     int indx_vel =6;
    	 int indx_fpa =7;
    	 int indx_azi =8;
    	 float init_alt=0;
   	 List<AnimationSet> animationSets= new ArrayList<AnimationSet>();
	FileInputStream fstream = null;
    DataInputStream in = new DataInputStream(fstream);
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String strLine;
   	// Scan for specifin information 
	     fstream = null;
		try{ fstream = new FileInputStream(RES_File);} catch(IOException eIO) { System.out.println(eIO);}
	               in = new DataInputStream(fstream);
	               br = new BufferedReader(new InputStreamReader(in));
	              try {
	              int indx=0;
							while ((strLine = br.readLine()) != null )   {
								Object[] tokens = strLine.split(" ");

							    if(indx==0){init_alt=Float.parseFloat((String) tokens[4]);}
							    indx++;
							    }
			       fstream.close();
			       in.close();
			       br.close();

	              } catch (NullPointerException | IOException eNPE) { 
	            	  System.out.println("Read raw data, Nullpointerexception");
					}catch(IllegalArgumentException eIAE) {
					  System.out.println("Read raw data, illegal argument error");
					}
	//----------------------------------------------------------------------------------------------
	             // System.out.println(init_x+" | "+init_y+" | "+init_z+" | "); 
	      	    fstream = null;
	    		try{ fstream = new FileInputStream(RES_File);} catch(IOException eIO) { System.out.println(eIO);}
	               in = new DataInputStream(fstream);
	               br = new BufferedReader(new InputStreamReader(in));
	               strLine=null;
	    	              try {
	    							while ((strLine = br.readLine()) != null )   {
	    								Object[] tokens = strLine.split(" ");
	    							    AnimationSet animationSet = new AnimationSet();
	    							    animationSet.setTime(Float.parseFloat((String) tokens[indx_time]));
	    							    float velocity = Float.parseFloat((String) tokens[indx_vel]);
	    							    float fpa = Float.parseFloat((String) tokens[indx_fpa]);
	    							    float azi = Float.parseFloat((String) tokens[indx_azi]);
	    							    float v_v = (float) (velocity * Math.sin(fpa));
	    							    float v_h = (float) (velocity * Math.cos(fpa));
	    							    //System.out.println(v_v+" | "+init_alt);
	    							    animationSet.setV_h(v_h);
	    							    animationSet.setV_v(v_v);
	    							    animationSet.setAzimuth(azi);
	    							    animationSet.setAlt_init(init_alt);
	    							    animationSets.add(animationSet);	    							  }
	    			       fstream.close();
	    			       in.close();
	    			       br.close();

	    	              } catch (NullPointerException | IOException eNPE) { 
	    	            	  System.out.println("Read raw data, Nullpointerexception");
	    					}catch(IllegalArgumentException eIAE) {
	    					  System.out.println("Read raw data, illegal argument error");
	    					}
						return animationSets;
   }
    
    public static double readFromFile(String file, int indx) {
    	FileInputStream fstream = null;
    	double result=0;
    	  try {
    	      fstream = new FileInputStream(file);
    	} catch(IOException eIIO) { System.out.println(eIIO); System.out.println("ERROR: Reading from file failed.");} 
    	DataInputStream in55 = new DataInputStream(fstream);
    	@SuppressWarnings("resource")
    	BufferedReader br55 = new BufferedReader(new InputStreamReader(in55));
    	int k = 0;
    	double InitialState = 0;
    	String strLine55;
    	try {
    	while ((strLine55 = br55.readLine()) != null )   {
    		String[] tokens = strLine55.split(" ");
    		if(!tokens[0].isEmpty()) {
    		 InitialState = Double.parseDouble(tokens[0]);
    		} else {
    			InitialState =0; 
    			//System.out.println("isempty  "+indx+"|"+k);
    		}
    	 	if (k==indx){
    		  result = InitialState;
    		} 
    	 	//System.out.println(k+"|"+indx);
    		k++;
    	}
    	in55.close();
    	br55.close();
    	fstream.close();
    	} catch (NullPointerException eNPE) { System.out.println(eNPE);} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    	return result;
    }

    public static ArrayList<String> Read_SEQU(){
	ArrayList<String> SEQUENCE_DATA = new ArrayList<String>();
	 try {
		BufferedReader br = new BufferedReader(new FileReader(SEQU_File));
	   String strLine;
	   while ((strLine = br.readLine()) != null )   {
	   	String[] tokens = strLine.split(" ");
	   	SEQUENCE_DATA.add(tokens[0]+" "+
	   				      tokens[1]+" "+
	   				      tokens[2]+" "+
	   				      tokens[3]+" "+
	   				      tokens[4]+" "+
	   				      tokens[5]+" "+
	   				      tokens[6]+" "+
	   				      tokens[7]+" "+
	   				      tokens[8]+" "+
	   				      tokens[9]+" "+
	   				      tokens[10]+" "+
	   				      tokens[11]+" "
	   	);
	   }
	   br.close();
	   } catch(NullPointerException | IOException eNPE) { System.out.println(eNPE);System.out.println("ERROR: Read SEQU.res failed. ");}
	   return SEQUENCE_DATA;
	}
    
    public static List<InputFileSet> readResultFileList(String filePath) throws IOException{
    	List<InputFileSet> newInputFileSetList = new ArrayList<InputFileSet>();

   		      	InputFileSet newInputFileSet = new InputFileSet();
   		      	newInputFileSet.setInputDataFilePath(filePath);
   		      	newInputFileSetList.add(newInputFileSet);

    return newInputFileSetList;
    }
	public static void WriteErrorINP() {
	    try {
	        File fac = new File(ERROR_File);
	        if (!fac.exists())
	        {
	            fac.createNewFile();
	        } else {
	        	fac.delete();
	        	fac.createNewFile();
	        }
	        FileWriter wr = new FileWriter(fac);
	        for (int i=0; i<MODEL_ERROR.getRowCount(); i++)
	        {
	        	String error_type 		= (String) MODEL_ERROR.getValueAt(i, 1);
	        	for(int k=0;k<ErrorType.length;k++) { if(error_type.equals(ErrorType[k])){error_type=""+k;} }
	        	String error_trigger 	= (String) MODEL_ERROR.getValueAt(i, 2);
	        	String error_value 		= (String) MODEL_ERROR.getValueAt(i, 3); 
	        	wr.write(error_type+" "+error_trigger+" "+error_value+System.getProperty( "line.separator" ));
	        }
	        wr.close(); 
	        Update_ErrorIndicator();
	     } catch (IOException eIO){
	     	System.out.println(eIO);
	     }
	}
	public static void WriteSequenceINP() {
	        try {
	            File fac = new File(SEQUENCE_File);
	            if (!fac.exists())
	            {
	                fac.createNewFile();
	            } else {
	            	fac.delete();
	            	fac.createNewFile();
	            }
	            //System.out.println("\n----------------------------------");
	            //System.out.println("The file has been created.");
	            //System.out.println("------------------------------------");
	            FileWriter wr = new FileWriter(fac);
	            for (int i=0; i<MODEL_SEQUENCE.getRowCount(); i++)
	            {
	        			String row ="";
	        			for(int j=0;j<MODEL_SEQUENCE.getColumnCount();j++) {
	        				if(j==0) {
	        					String val =  (String) MODEL_SEQUENCE.getValueAt(i, j);
	        					row = row + val + " ";
	        				}  else if(j==1) {
	        					String str_val =  (String) MODEL_SEQUENCE.getValueAt(i, j);
	        					int val = 0 ; 
	        					for(int k=0;k<SequenceENDType.length;k++) { if(str_val.equals(SequenceENDType[k])){val=k;} }
	        					row = row + val + " ";
	        				} else if(j==2) {
	        					String val =  (String) MODEL_SEQUENCE.getValueAt(i, j);
	        					row = row + val + " ";
	        				} else if(j==3) {
	        					String str_val =  (String) MODEL_SEQUENCE.getValueAt(i, j);
	        					int val = 0 ; 
	        					for(int k=0;k<SequenceType.length;k++) { if(str_val.equals(SequenceType[k])){val=k+1;} }
	        					row = row + val + " ";
	        				} else if(j==4) {
	        					String str_val =  (String) MODEL_SEQUENCE.getValueAt(i, j);
	        					int val = 0 ; 
	        					//System.out.println(""+SequenceFC.length);
	        					try {
	        					for(int k=0;k<SequenceFCCombobox.getItemCount();k++) { if(str_val.equals(SequenceFCCombobox.getItemAt(k) )){val=k+1;} }
	        					} catch (NullPointerException eNPE) {System.out.println(eNPE);}
	        					row = row + val + " ";
	        				} else if(j==5) {
	        					String val =  (String) MODEL_SEQUENCE.getValueAt(i, j);
	        					row = row + val + " ";
	        				} else if(j==6) {
	        					String val =  (String) MODEL_SEQUENCE.getValueAt(i, j);
	        					row = row + val + " ";
	        				} else if(j==7) {
	        					String str_val =  (String) MODEL_SEQUENCE.getValueAt(i, j);
	        					int val = 0 ; 
	        					for(int k=0;k<FCTargetCurve.length;k++) { if(str_val.equals(FCTargetCurve[k])){val=k+1;} }
	        					row = row + val + " ";
	        				} else if(j==8) {
	        					String str_val =  "";
	        					try {
	        					str_val = (String) MODEL_SEQUENCE.getValueAt(i, j);
	        					} catch (java.lang.NumberFormatException eNFE) {
	        						System.out.println(eNFE);
	        					}
	        					int val = 0 ; 
	        					try {
	        					for(int k=0;k<SequenceTVCFCCombobox.getItemCount();k++) { if(str_val.equals(SequenceTVCFCCombobox.getItemAt(k))){val=k+1;} }
	        					} catch (NullPointerException eNPE) {System.out.println(eNPE);}
	        					row = row + val + " ";
	        				} else if(j==9) {
	        					double val = 0 ; 
	        					try {
	        					 val =  Double.parseDouble((String) MODEL_SEQUENCE.getValueAt(i, j));
	        					} catch (java.lang.NumberFormatException | NullPointerException eNFE) {
	        						System.out.println(eNFE);
	        					}
	        					row = row + val + " ";
	        				} else if(j==10) {
	        					double val = 0 ; 
	        					try {
	        					 val =  Double.parseDouble((String) MODEL_SEQUENCE.getValueAt(i, j))*deg2rad;
	        					} catch (java.lang.NumberFormatException | NullPointerException eNFE) {
	        						System.out.println(eNFE);
	        					}
	        					row = row + val + " ";
	        				} else if(j==11) {
	        					String str_val =  "";
	        					try {
	        					str_val = (String) MODEL_SEQUENCE.getValueAt(i, j);
	        					} catch (java.lang.NumberFormatException eNFE) {System.out.println(eNFE);}
	        					int val=0;
	        					try {
	        					for(int k=0;k<TargetCurve_Options_TVC.length;k++) { if(str_val.equals(TargetCurve_Options_TVC[k])){val=k+1;} }
	        					} catch (NullPointerException eNPE) {System.out.println(eNPE);}
	        					row = row + val + " ";
	        				} 
	        		   }
	        			wr.write(row+System.getProperty( "line.separator" ));
	            }
	            wr.close(); 
	        } catch (IOException eIO){
	        	System.out.println(eIO);
	        }
	}
	
	public static void WriteInitialAttitude() {
        try {
            File fac = new File(InitialAttitude_File);
            if (!fac.exists())
            {
                fac.createNewFile();
            } else {
            	fac.delete();
            	fac.createNewFile();
            }
            FileWriter wr = new FileWriter(fac);
            for (int i=0; i<4; i++)
            {
        			double value =0;
				if(i==0) { 
					 if(INPUT_Quarternion1.getText().equals("")) {
						 value =0;
					 } else {
						 value = Double.parseDouble(INPUT_Quarternion1.getText()); 
					 }
				} else if(i==1) {
					 if(INPUT_Quarternion2.getText().equals("")) {
						 value =0;
					 } else {
						 value = Double.parseDouble(INPUT_Quarternion2.getText()); 
					 }
				}else if(i==2) {
					 if(INPUT_Quarternion3.getText().equals("")) {
						 value =0;
					 } else {
						 value = Double.parseDouble(INPUT_Quarternion3.getText()); 
					 }
				} else if(i==3) {
					 if(INPUT_Quarternion4.getText().equals("")) {
						 value =0;
					 } else {
						 value = Double.parseDouble(INPUT_Quarternion4.getText()); 
					 } 
				}
        			wr.write(value+System.getProperty( "line.separator" ));
            }
            wr.close(); 
        } catch (IOException eIO){
        	System.out.println(eIO);
        }
	}
	public static void WriteINERTIA() {
	    try {
	        File fac = new File(INERTIA_File);
	        if (!fac.exists())
	        {
	            fac.createNewFile();
	        } else {
	        	fac.delete();
	        	fac.createNewFile();
	        }
	        FileWriter wr = new FileWriter(fac);
	        for (int j=0;j<3;j++) {
					 if(j==0) {
						 double Ixx = 0;
						 double Ixy = 0; 
						 double Ixz = 0;
						 if(INPUT_IXX.getText().equals("")) {
							 Ixx =0;
						 } else {
							 Ixx = Double.parseDouble(INPUT_IXX.getText()); 
						 }
						 if(INPUT_IXY.getText().equals("")) {
							 Ixy =0;
						 } else {
							 Ixy = Double.parseDouble(INPUT_IXY.getText());
						 }
						 if(INPUT_IXZ.getText().equals("")) {
							 Ixz =0;
						 } else {
							 Ixz = Double.parseDouble(INPUT_IXZ.getText());
						 }
					     wr.write(Ixx+" "+Ixy+" "+Ixz+System.getProperty( "line.separator" ));
					 } else if (j==1) {
						 double Iyx = 0; 
						 double Iyy = 0; 
						 double Iyz = 0;
						 if(INPUT_IYX.getText().equals("")) {
							 Iyx =0;
						 } else {
							 Iyx = Double.parseDouble(INPUT_IYX.getText());
						 }
						 if(INPUT_IYY.getText().equals("")) {
							 Iyy =0;
						 } else {
							 Iyy = Double.parseDouble(INPUT_IYY.getText());
						 }
						 if(INPUT_IYZ.getText().equals("")) {
							 Iyz =0;
						 } else {
							 Iyz = Double.parseDouble(INPUT_IYZ.getText());
						 }
					     wr.write(Iyx+" "+Iyy+" "+Iyz+System.getProperty( "line.separator" )); 
					 } else if (j==2 ) {
						 double Izx = 0;
						 double Izy = 0;
						 double Izz = 0;
						 if(INPUT_IZX.getText().equals("")) {
							 Izx =0;
						 } else {
							 Izx = Double.parseDouble(INPUT_IZX.getText());
						 }
						 if(INPUT_IZY.getText().equals("")) {
							 Izy =0;
						 } else {
							 Izy = Double.parseDouble(INPUT_IZY.getText()); 
						 }
						 if(INPUT_IZZ.getText().equals("")) {
							 Izz =0;
						 } else {
							 Izz = Double.parseDouble(INPUT_IZZ.getText()); 
						 }
					     wr.write(Izx+" "+Izy+" "+Izz+System.getProperty( "line.separator" ));
					 }
	        }
	        wr.close(); 
	
	     } catch (IOException eIO){
	     	System.out.println(eIO);
	     }
	}
	public static void WriteControllerINP() {
	        try {
	            File fac = new File(CONTROLLER_File);
	            if (!fac.exists())
	            {
	                fac.createNewFile();
	            } else {
	            	fac.delete();
	            	fac.createNewFile();
	            }
	            //System.out.println("\n----------------------------------");
	            //System.out.println("The file has been created.");
	            //System.out.println("------------------------------------");
	            FileWriter wr = new FileWriter(fac);
	            for (int i=0; i<MODEL_CONTROLLER.getRowCount(); i++)
	            {
	        			String row ="";
	        			for(int j=0;j<MODEL_CONTROLLER.getColumnCount();j++) {
	    					String val =  (String) MODEL_CONTROLLER.getValueAt(i, j);
	    					row = row + val + " ";
	        		   }
	        			wr.write(row+System.getProperty( "line.separator" ));
	            }
	            wr.close(); 
	        } catch (IOException eIO){
	        	System.out.println(eIO);
	        }
	}
	public static void WRITE_INIT() {
        try {
            File fac = new File(Init_File);
            if (!fac.exists())
            {
                fac.createNewFile();
            } else {
            	fac.delete();
            	fac.createNewFile();
            }
            //System.out.println("\n----------------------------------");
            //System.out.println("The file has been created.");
            //System.out.println("------------------------------------");
            double r = 0;
            int rr=0;
            FileWriter wr = new FileWriter(fac);
            for (int i = 0; i<=30; i++)
            {
        		if (i == 0 ){
        			r = Double.parseDouble(	SidePanelLeft.INPUT_LONG_Rs.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==1 ){
        			r = Double.parseDouble(	SidePanelLeft.INPUT_LAT_Rs.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==2 ){
        			r = Double.parseDouble(	SidePanelLeft.INPUT_ALT_Rs.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==3 ){
        			r = Double.parseDouble(	SidePanelLeft.INPUT_VEL_Rs.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i == 4 ){
            		r = Double.parseDouble(	SidePanelLeft.INPUT_FPA_Rs.getText()) ;
            		wr.write(r+System.getProperty( "line.separator" ));	
        			} else if (i == 5 ){
                	r = Double.parseDouble(	SidePanelLeft.INPUT_AZI_Rs.getText()) ;
                	wr.write(r+System.getProperty( "line.separator" ));	
            		} else if (i == 6 ){
                	r = Double.parseDouble(INPUT_M0.getText()) ;
                	wr.write(r+System.getProperty( "line.separator" ));	
            		} else if (i == 7 ){
                    r = Double.parseDouble((String) INPUT_GlobalTime.getText()) ;
                    wr.write(r+System.getProperty( "line.separator" ));	
		    		} else if (i == 8 ){
		            rr =  	SidePanelLeft.Integrator_chooser.getSelectedIndex() ;
		            wr.write(rr+System.getProperty( "line.separator" ));	
		    		} else if (i == 9 ){
		            rr =  Target_chooser.getSelectedIndex() ;
		            wr.write(rr+System.getProperty( "line.separator" ));	
		    		} else if (i == 10 ){
		            // r = 0;//Double.parseDouble(INPUT_WRITETIME.getText())  ; // delta-t write out
		            wr.write(0+System.getProperty( "line.separator" ));	
		    		} else if (i == 11 ){
			        r = Double.parseDouble(SidePanelLeft.INPUT_REFELEV.getText())  ; // Reference elevation
			        wr.write(r+System.getProperty( "line.separator" ));	
		        } else if (i == 12) {
	    				String timeString = SidePanelLeft.timePanel.getaTime().getUtcString();
		            wr.write(timeString+System.getProperty( "line.separator" ));	
		        } else if(i == 13) {
	                wr.write(VelocityCoordinateSystem+System.getProperty( "line.separator" ));	
		        } else if(i == 14) {
	                wr.write(DOF_System+System.getProperty( "line.separator" ));	
		        } else if(i == 15) {
		        	double rate = Double.parseDouble(	SidePanelLeft.INPUT_AngularRate_X.getText());
		        	wr.write(rate+System.getProperty( "line.separator" ));	
		        } else if(i == 16) {
		        	double rate = Double.parseDouble(	SidePanelLeft.INPUT_AngularRate_Y.getText());
		        	wr.write(rate+System.getProperty( "line.separator" ));	
		        } else if(i == 17) {
		        	double rate = Double.parseDouble(	SidePanelLeft.INPUT_AngularRate_Z.getText());
		        	wr.write(rate+System.getProperty( "line.separator" ));	
		        } else if(i==18) {
		        	double value = Double.parseDouble(INPUT_ControllerFrequency.getText());
		        	wr.write(value+System.getProperty( "line.separator" ));
		        } else if(i==19) {
		        	double value = Double.parseDouble(INPUT_GlobalFrequency.getText());
		        	wr.write(value+System.getProperty( "line.separator" ));
		        }
		            }               
            wr.close();
            } catch (IOException eIO) {
            	System.out.println(eIO);
            }
    }
    public void WRITE_CTRL_01() {
        try {
            File fac = new File(CTR_001_File);
            if (!fac.exists())
            {
                fac.createNewFile();
            } else {
            	fac.delete();
            	fac.createNewFile();
            }
            //System.out.println("\n----------------------------------");
            //System.out.println("The file has been created.");
            //System.out.println("------------------------------------");
            double r = 0;
            int rr=0;
            FileWriter wr = new FileWriter(fac);
            for (int i = 0; i<=12; i++)
            {
        		if (i == 0 ){
        			if(p421_linp0.isSelected()) {
        				rr=1;
        			wr.write(rr+System.getProperty( "line.separator" )); } else {
        				rr=0;
        		    wr.write(rr+System.getProperty( "line.separator" ));	
        			}
        			} else if (i ==1 ){
        			r = Double.parseDouble(INPUT_PGAIN.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==2 ){
        			r = Double.parseDouble(INPUT_IGAIN.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==3 ){
        			r = Double.parseDouble(INPUT_DGAIN.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i == 4 ){
            		r = Double.parseDouble(INPUT_CTRLMAX.getText()) ;
            		wr.write(r+System.getProperty( "line.separator" ));	
        			} else if (i == 5 ){
                	r = Double.parseDouble(INPUT_CTRLMIN.getText()) ;
                	wr.write(r+System.getProperty( "line.separator" ));	
            		} 
		            }               
            wr.close();
            } catch (IOException eIO) {
            	System.out.println(eIO);
            }
    }
    
    public static void WRITE_SC() {
    try {
            File fac = new File(SC_file);
            if (!fac.exists())
            {
                fac.createNewFile();
            } else {
            	fac.delete();
            	fac.createNewFile();
            }
            //System.out.println("\n----------------------------------");
            //System.out.println("The file has been created.");
            //System.out.println("------------------------------------");
            double r = 0;
            FileWriter wr = new FileWriter(fac);
            for (int i = 0; i<=15; i++)
            {
        			if (i == 0 ){
        				if(INPUT_SURFACEAREA.getText().isEmpty()) {
		        			r = 0;
		        			wr.write(r+System.getProperty( "line.separator" ));
        				}else {
		        			r = Double.parseDouble(INPUT_SURFACEAREA.getText()) ;
		        			wr.write(r+System.getProperty( "line.separator" ));
        				}
        			} else if (i ==1 ){
        				if(INPUT_BALLISTICCOEFFICIENT.getText().isEmpty()) {
		        			r = 0;
		        			wr.write(r+System.getProperty( "line.separator" ));
        				}else {
		        			r = Double.parseDouble(INPUT_BALLISTICCOEFFICIENT.getText()) ;
		        			wr.write(r+System.getProperty( "line.separator" ));
        				}
        			} else if (i==2) {
        				try {
        				if(INPUT_ParachuteDiameter.getText().isEmpty()) {
		        			 r = 0;
		        			wr.write(r+System.getProperty( "line.separator" ));
        				}else {
		        			double d = Double.parseDouble(INPUT_ParachuteDiameter.getText()) ;
		        			double area = PI/4*d*d;
		        			wr.write(area+System.getProperty( "line.separator" ));
        				}
        				} catch (NullPointerException e) {
		        			 r = 0;
		        			wr.write(r+System.getProperty( "line.separator" ));
        				}
        			}
            }
            wr.close();
      } catch (IOException eIO) {
      System.out.println(eIO);
      }
    }
    
    public static void WRITE_AERO() {
    try {
            File fac = new File(Aero_file);
            if (!fac.exists())
            {
                fac.createNewFile();
            } else {
            	fac.delete();
            	fac.createNewFile();
            }
            FileWriter wr = new FileWriter(fac);
            for (int i = 0; i<=15; i++)
            {
        			if (i == 0 ){
        					int indx = getDragModelSetIndx();
	        			   wr.write(indx+System.getProperty( "line.separator" ));
        			} else if ( i == 1 ) {
        				double CD = 1.4;
        				try {
        			    CD = Double.parseDouble(ConstantCD_INPUT.getText());
        				wr.write(CD+System.getProperty( "line.separator" ));
        				} catch(NullPointerException e) {
            				wr.write(CD+System.getProperty( "line.separator" ));
            			}
        			} else if ( i == 2 ) {
        				try {
        				if(INPUT_RB.getText().isEmpty()) {
		        			double r = 0;
		        			wr.write(r+System.getProperty( "line.separator" ));
        				}else {
		        			double r = Double.parseDouble(INPUT_RB.getText()) ;
		        			wr.write(r+System.getProperty( "line.separator" ));
        				}
        				} catch (NullPointerException e) {
		        			double r = 0;
		        			wr.write(r+System.getProperty( "line.separator" ));
        				}
        			} else if(i == 3 ) {
    					int indx = getParachuteModelSetIndx();

	        			   wr.write(indx+System.getProperty( "line.separator" ));
        			} else if(i == 4 ) {
        				try {
        				if(ConstantParachuteCD_INPUT.getText().isEmpty()) {
		        			double r = 0;
		        			wr.write(r+System.getProperty( "line.separator" ));
        				}else {
		        			double r = Double.parseDouble(ConstantParachuteCD_INPUT.getText()) ;
		        			wr.write(r+System.getProperty( "line.separator" ));
        				}
        				} catch (NullPointerException e) {
		        			double r = 0;
		        			wr.write(r+System.getProperty( "line.separator" ));
        				}
        			} else if (i==5) {

        			}
            }
            wr.close();
      } catch (IOException eIO) {
      System.out.println(eIO);
      }
    }
    
    public static void WRITE_INTEG() {
    	String integ_file = null;  
    	int steps =0 ; 
    	if(	SidePanelLeft.Integrator_chooser.getSelectedIndex()==0) {
    		integ_file = INTEG_File_01; 
    		steps = 4; 
    	} else if (	SidePanelLeft.Integrator_chooser.getSelectedIndex()==1) {
    		integ_file = INTEG_File_02; 
    		steps =1;
    	} else if (SidePanelLeft.Integrator_chooser.getSelectedIndex()==2) {
    		integ_file = INTEG_File_03; 
    		steps =4;
    	} else if (SidePanelLeft.Integrator_chooser.getSelectedIndex()==3) {
    		integ_file = INTEG_File_04; 
    		steps =5;
    	}
        try {
            File fac = new File(integ_file);
            if (!fac.exists())
            {
                fac.createNewFile();
            } else {
            	fac.delete();
            	fac.createNewFile();
            }
            double r = 0;
            FileWriter wr = new FileWriter(fac);
            for (int i = 0; i<steps; i++)
            {
            		   if(i==0) {
        			r = Double.parseDouble(SidePanelLeft.INPUT_IntegratorSetting_01.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
            	} else if (i==1) {
        			r = Double.parseDouble(SidePanelLeft.INPUT_IntegratorSetting_02.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
            	} else if (i==2) {
        			r = Double.parseDouble(SidePanelLeft.INPUT_IntegratorSetting_03.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
            	} else if (i==3) {
        			r = Double.parseDouble(SidePanelLeft.INPUT_IntegratorSetting_04.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
            	} else if (i==4) {
        			r = Double.parseDouble(SidePanelLeft.INPUT_IntegratorSetting_05.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
            	} 
		            }               
            wr.close();
            } catch (IOException eIO) {
            	System.out.println(eIO);
            }
    }
    
    public static void   WRITE_EventHandler() {
        try {
            File fac = new File(EventHandler_File);
            if (!fac.exists())
            {
                fac.createNewFile();
            } else {
            	fac.delete();
            	fac.createNewFile();
            }
            //-----------------------------------------------------
            FileWriter wr = new FileWriter(fac);
            for (int i=0; i<MODEL_EventHandler.getRowCount(); i++){
	            	int EventType =0;
		            	for(int j=0;j<EventHandler_Type.length;j++) {
		            		if(MODEL_EventHandler.getValueAt(i, 0).equals(EventHandler_Type[j])) {EventType =j;}
		            	}
	            	double EventValue = Double.parseDouble((String) MODEL_EventHandler.getValueAt(i, 1));
	            	wr.write(EventType+BB_delimiter+EventValue+BB_delimiter+System.getProperty( "line.separator" ));	
			}               
	            wr.close();
            } catch (IOException eIO) {System.out.println(eIO);}
    }
    
    public static void WRITE_PROP() {
        try {
            File fac = new File(Prop_File);
            if (!fac.exists())
            {
                fac.createNewFile();
            } else {
            	fac.delete();
            	fac.createNewFile();
            }
            //System.out.println("\n----------------------------------");
            //System.out.println("The file has been created.");
            //System.out.println("------------------------------------");
            double r = 0;
            FileWriter wr = new FileWriter(fac);
            for (int i = 0; i<=30; i++)
            {
        			if (i == 0 ){
            			r = Double.parseDouble(INPUT_ISP.getText()) ;
            			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==1 ){
            			r = Double.parseDouble(INPUT_PROPMASS.getText()) ;
            			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==2 ){
            			r = Double.parseDouble(INPUT_THRUSTMAX.getText()) ;
            			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==3 ){
            			r = Double.parseDouble(INPUT_THRUSTMIN.getText()) ;
            			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i == 4 ){
        					if(INPUT_ISPMODEL.isSelected()) {
        						wr.write(1+System.getProperty( "line.separator" ));
        					} else {
        						wr.write(0+System.getProperty( "line.separator" ));
        					}
        			} else if (i == 5 ){
        				try {
        					if (INPUT_ISPMIN.equals("")) {
        						r = Double.parseDouble(INPUT_ISP.getText()) ;
        					} else {
        						r = Double.parseDouble(INPUT_ISPMIN.getText()) ;
        					}
            			wr.write(r+System.getProperty( "line.separator" ));
        				} catch (java.lang.NumberFormatException eNFE) {
        					wr.write(""+System.getProperty( "line.separator" ));	
        				}
            		} else if( i == 6 ) {
            			r = Double.parseDouble(INPUT_RCSX.getText()) ;
            			wr.write(r+System.getProperty( "line.separator" ));
            		} else if( i == 7 ) {
            			r = Double.parseDouble(INPUT_RCSY.getText()) ;
            			wr.write(r+System.getProperty( "line.separator" ));
            		} else if( i == 8 ) {
            			r = Double.parseDouble(INPUT_RCSZ.getText()) ;
            			wr.write(r+System.getProperty( "line.separator" ));
            		} else if( i == 9 ) {
            			r = Double.parseDouble(INPUT_RCSXTHRUST.getText());
            			wr.write(r+System.getProperty( "line.separator" ));
            		} else if( i == 10 ) {
            			r = Double.parseDouble(INPUT_RCSYTHRUST.getText());
            			wr.write(r+System.getProperty( "line.separator" ));
            		} else if( i == 11 ) {
            			r = Double.parseDouble(INPUT_RCSZTHRUST.getText());
            			wr.write(r+System.getProperty( "line.separator" ));
            		} else if( i == 12 ) {
            			r = Double.parseDouble(INPUT_RCSTANK.getText());
            			wr.write(r+System.getProperty( "line.separator" ));
            		} else if( i == 13 ) {
            			r = Double.parseDouble(INPUT_RCSXISP.getText());
            			wr.write(r+System.getProperty( "line.separator" ));
            		} else if( i == 14 ) {
            			r = Double.parseDouble(INPUT_RCSYISP.getText());
            			wr.write(r+System.getProperty( "line.separator" ));
            		} else if( i == 15 ) {
            			r = Double.parseDouble(INPUT_RCSZISP.getText());
            			wr.write(r+System.getProperty( "line.separator" ));
            		}
		            }               
            wr.close();
            } catch (IOException eIO) {
            	System.out.println(eIO);
            }
    }
    
    public static void WRITE_SequenceFile() {
        try {
            File fac = new File(sequenceFile);
            if (!fac.exists())
            {
                fac.createNewFile();
            } else {
            	fac.delete();
            	fac.createNewFile();
            }
            String fcSeparator="|FlightControllerElements|";
            String eventSeparator="|EventManagementElements";
            String endSeparator="|EndElement|";
            FileWriter wr = new FileWriter(fac);
            for (int i = 0; i<sequenceContentList.size(); i++)
            {
        		int ID = sequenceContentList.get(i).getSequenceID();
            	String sequenceContent = ID+" "+
						 			 sequenceContentList.get(i).getSequenceName()+" "+
            							 fcSeparator	+" "+ 
            							 sequenceContentList.get(i).getFlightControllerSelect().getSelectedIndex()+" "+
            							 fcSeparator	+" "+ 
            							 eventSeparator+" "+ 
            							 sequenceContentList.get(i).getEventSelect().getSelectedIndex()+" "+
            							 eventSeparator+" "+ 
            							 endSeparator+" "+ 
            							 sequenceContentList.get(i).getEndSelect().getSelectedIndex()+" "+
            							 sequenceContentList.get(i).getValueEnd().getText()+" "
            							 +endSeparator+" ";
            	
            	wr.write(sequenceContent+System.getProperty( "line.separator" ));
		    }               
            wr.close();
            } catch (IOException eIO) {
            	System.out.println(eIO);
            }
    }
    
    public static int getDragModelSetIndx() {
		int k=0;
		for(int j=0;j<DragModelSet.size();j++) {
			if(DragModelSet.get(j).isSelected()) {
				k=j;
			}
		}
		return k;
    }

    public static int getParachuteModelSetIndx() {
		int k=0;
		for(int j=0;j<ParachuteBulletPoints.size();j++) {
			if(ParachuteBulletPoints.get(j).isSelected()) {
				k=j;
			}
		}
		return k;
    }
    public static void EvaluateSurfaceAreaSetup() {
	    	if(INPUT_SURFACEAREA.getText().equals("0")) {	    		
	    		INPUT_SURFACEAREA.setText("");
	    		INPUT_SURFACEAREA.setEditable(false);
	    		INPUT_BALLISTICCOEFFICIENT.setEditable(true);
	    	} else if (INPUT_BALLISTICCOEFFICIENT.getText().equals("0")) {
	    		INPUT_BALLISTICCOEFFICIENT.setText("");
	    		INPUT_SURFACEAREA.setEditable(true);
	    		INPUT_BALLISTICCOEFFICIENT.setEditable(false);	    		
	    	}
    	}
    
    public static double[][] FIND_ctrl_init_cond() throws IOException{
	   	   List<SequenceElement> SEQUENCE_DATA = new ArrayList<SequenceElement>(); 
	   	    SEQUENCE_DATA = ReadInput.readSequence();
	   	    double[][] INIT_CONDITIONS = new double[4][SEQUENCE_DATA.size()];
	   	    for (int i=0;i<SEQUENCE_DATA.size();i++) {
	   	    	
	   	    }
	   	    
	   	    return INIT_CONDITIONS;
	}
public void IMPORT_Case() throws IOException {
	BufferedReader br = new BufferedReader(new FileReader(CurrentWorkfile_Path));
	DeleteAllSequence();
	DeleteAllController();
    String strLine;
    int indx_init=0;
    int indx_prop=0;
    int indx_sc=0;
    int data_column=2;              // Data column of one column data files [-]
    @SuppressWarnings("unused")
	int k = 0 ; 
    while ((strLine = br.readLine()) != null )   {
    	String[] tokens = strLine.split(" ");
    	if(tokens[0].equals("|INIT|")) {
				            if (indx_init==0){SidePanelLeft.INPUT_LONG_Rs.setText(df_X4.format(Double.parseDouble(tokens[data_column])));
				  	 } else if (indx_init==1){SidePanelLeft.INPUT_LAT_Rs.setText(df_X4.format(Double.parseDouble(tokens[data_column])));
				 	 } else if (indx_init==2){SidePanelLeft.INPUT_ALT_Rs.setText(decf.format(Double.parseDouble(tokens[data_column])));
				 	 } else if (indx_init==3){SidePanelLeft.INPUT_VEL_Rs.setText(decf.format(Double.parseDouble(tokens[data_column])));
				 	 } else if (indx_init==4){SidePanelLeft.INPUT_FPA_Rs.setText(df_X4.format(Double.parseDouble(tokens[data_column])));
				 	 } else if (indx_init==5){SidePanelLeft.INPUT_AZI_Rs.setText(df_X4.format(Double.parseDouble(tokens[data_column])));
				 	 } else if (indx_init==6){INPUT_M0.setText(decf.format(Double.parseDouble(tokens[data_column])));
				 	 } else if (indx_init==7){MODEL_EventHandler.setValueAt(tokens[data_column], 0, 1);
				 	 } else if (indx_init==8){SidePanelLeft.Integrator_chooser.setSelectedIndex(Integer.parseInt(tokens[data_column]));
				     } else if (indx_init==9){Target_chooser.setSelectedIndex(Integer.parseInt(tokens[data_column]));
				     } else if (indx_init==10){INPUT_WRITETIME.setText(decf.format(Double.parseDouble(tokens[data_column])));
				     } else if (indx_init==11){SidePanelLeft.INPUT_REFELEV.setText(decf.format(Double.parseDouble(tokens[data_column])));
					 } else if (indx_init==12){AscentDescent_SwitchChooser.setSelectedIndex(Integer.parseInt(tokens[data_column]));
					 } 					     
        indx_init++;
    	} else if(tokens[0].equals("|PROP|")) {
			            if (indx_prop==0){INPUT_ISP.setText(decf.format(Double.parseDouble(tokens[data_column])));
			  	 } else if (indx_prop==1){INPUT_PROPMASS.setText(decf.format(Double.parseDouble(tokens[data_column])));
			 	 } else if (indx_prop==2){INPUT_THRUSTMAX.setText(decf.format(Double.parseDouble(tokens[data_column])));
			 	 } else if (indx_prop==3){INPUT_THRUSTMIN.setText(decf.format(Double.parseDouble(tokens[data_column])));
			 	 } 			     
		indx_prop++;
	    } else if(tokens[0].equals("|SC|")) {
            if (indx_sc==0){INPUT_SURFACEAREA.setText(decf.format(Double.parseDouble(tokens[data_column])));
  	 } else if (indx_sc==1){INPUT_BALLISTICCOEFFICIENT.setText(decf.format(Double.parseDouble(tokens[data_column])));
 	 } else if (indx_sc==2){
 	 } else if (indx_sc==3){
 	 } 			     
            indx_sc++;
	    } 	else if(tokens[0].equals("|CTRL|")) {
	    	ROW_CONTROLLER[0] = ""+Integer.parseInt(tokens[1]);
	    	ROW_CONTROLLER[1] = ""+Integer.parseInt(tokens[2]);
	    	ROW_CONTROLLER[2] = ""+Double.parseDouble(tokens[3]);
	    	ROW_CONTROLLER[3] = ""+Double.parseDouble(tokens[4]);
	    	ROW_CONTROLLER[4] = ""+Double.parseDouble(tokens[5]);
	    	ROW_CONTROLLER[5] = ""+Double.parseDouble(tokens[6]);
	    	ROW_CONTROLLER[6] = ""+Double.parseDouble(tokens[7]);
	    	MODEL_CONTROLLER.addRow(ROW_CONTROLLER);
	    	WriteControllerINP();
	    	UpdateFC_LIST(); 
	    } else if(tokens[0].equals("|SEQU|")) {
       	int sequence_ID 				= Integer.parseInt(tokens[1]);
       	int trigger_end_type 			= Integer.parseInt(tokens[2]);
       	double trigger_end_value 		= Double.parseDouble(tokens[3]);
       	int sequence_type		 		= Integer.parseInt(tokens[4]);
       	int sequence_controller_ID 		= Integer.parseInt(tokens[5]);
       	double ctrl_target_vel      	= Double.parseDouble(tokens[6]);
       	double ctrl_target_alt 			= Double.parseDouble(tokens[7]);
       	int ctrl_target_curve       	= Integer.parseInt(tokens[8]);
       	try {
       	int sequence_TVCcontroller_ID 	= Integer.parseInt(tokens[9]);
       	double TVCctrl_target_t      	= Double.parseDouble(tokens[10]);
       	double TVCctrl_target_fpa 		= Double.parseDouble(tokens[11]);
       	int ctrl_TVC_target_curve    	= Integer.parseInt(tokens[12]);
	    	ROW_SEQUENCE[8]  = ""+SequenceTVCFCCombobox.getItemAt(sequence_TVCcontroller_ID-1);
	    	ROW_SEQUENCE[9]  = ""+TVCctrl_target_t;
	    	ROW_SEQUENCE[10] = ""+TVCctrl_target_fpa;
	    	ROW_SEQUENCE[11] = ""+TargetCurve_Options_TVC[ctrl_TVC_target_curve-1];	
       	} catch (java.lang.ArrayIndexOutOfBoundsException eAIOOBE) {System.out.println("No TVC controller found in Sequence file.");}
    	ROW_SEQUENCE[0] = ""+sequence_ID;
    	ROW_SEQUENCE[1] = ""+SequenceENDType[trigger_end_type];
    	ROW_SEQUENCE[2] = ""+trigger_end_value;
    	ROW_SEQUENCE[3] = ""+SequenceType[sequence_type-1];
    	ROW_SEQUENCE[4] = ""+SequenceFCCombobox.getItemAt(sequence_controller_ID-1);
    	ROW_SEQUENCE[5] = ""+ctrl_target_vel;
    	ROW_SEQUENCE[6] = ""+ctrl_target_alt;
    	ROW_SEQUENCE[7] = ""+FCTargetCurve[ctrl_target_curve-1];	
    	MODEL_SEQUENCE.addRow(ROW_SEQUENCE);
    	WriteSequenceINP();
	 }
    k++;
    }
    br.close();    
    WRITE_INIT();
    WRITE_PROP();
}
public static void EXPORT_Case() {
	if ( CurrentWorkfile_Name.isEmpty()==false) {
		File file = CurrentWorkfile_Path;
        PrintWriter os = null;
		try {
			os = new PrintWriter(file);
		} catch (FileNotFoundException e) {System.out.println(e);}
	
    	for (int i = 0; i < 12; i++) {  // 					init.inp
        os.print("|INIT|" + BB_delimiter);
                       if (i==0) {os.print("|LONGITUDE[DEG]|"+ BB_delimiter+SidePanelLeft.INPUT_LONG_Rs.getText());
            	} else if (i==1) {os.print("|LATITUDE[DEG]|"+ BB_delimiter+SidePanelLeft.INPUT_LAT_Rs.getText());
            	} else if (i==2) {os.print("|ALTITUDE[m]|"+ BB_delimiter+SidePanelLeft.INPUT_ALT_Rs.getText());
            	} else if (i==3) {os.print("|VELOCITY[m/s]|"+ BB_delimiter+SidePanelLeft.INPUT_VEL_Rs.getText());
            	} else if (i==4) {os.print("|FPA[DEG]|"+ BB_delimiter+SidePanelLeft.INPUT_FPA_Rs.getText());
            	} else if (i==5) {os.print("|AZIMUTH[DEG]|"+ BB_delimiter+SidePanelLeft.INPUT_AZI_Rs.getText());
            	} else if (i==6) {os.print("|INITMASS[kg]|"+ BB_delimiter+INPUT_M0.getText());
            	} else if (i==7) {os.print("|INTEGTIME[s]|"+ BB_delimiter+MODEL_EventHandler.getValueAt( 0, 1));
            	} else if (i==8) {os.print("|INTEG[-]|"+ BB_delimiter+SidePanelLeft.Integrator_chooser.getSelectedIndex());
                } else if (i==9) {os.print("|TARGET[-]|"+ BB_delimiter+Target_chooser.getSelectedIndex());
                } else if (i==10){os.print("|WRITET[s]|"+ BB_delimiter+INPUT_WRITETIME.getText());
                } else if (i==11){os.print("|REFELEVEVATION[m]|"+ BB_delimiter+SidePanelLeft.INPUT_REFELEV.getText());
    		    } else if (i==11){os.print("|ThrustSwitch[-]|"+ BB_delimiter+AscentDescent_SwitchChooser.getSelectedIndex());
    		    } 
                os.print(BB_delimiter);
    	os.println("");
    	}
    	
    	for (int i = 0; i < 10; i++) {  // 					prop.inp
    		os.print("|PROP|" + BB_delimiter);
		               if (i==0){os.print("|ISP[s]|"+ BB_delimiter+INPUT_ISP.getText());
		     	} else if (i==1){os.print("|PROPMASS[kg]|"+ BB_delimiter+INPUT_PROPMASS.getText());
		     	} else if (i==2){os.print("|THRUSTMAX[N]|"+ BB_delimiter+INPUT_THRUSTMAX.getText());
		     	} else if (i==3){os.print("|THRUSTMIN[N]|"+ BB_delimiter+INPUT_THRUSTMIN.getText());
		     	} else if (i==4){if(INPUT_ISPMODEL.isSelected()) {os.print("|ISPMODEL[-]|"+ BB_delimiter+1);} else {os.print("|ISPMODEL[-]|"+ BB_delimiter+0);}
		     	} else if (i==5){os.print("|ISPMIN[s]|"+ BB_delimiter+INPUT_ISPMIN.getText());
		    	}
	        os.println("");
    	}
        for (int  row2 = 0; row2 < MODEL_CONTROLLER.getRowCount(); row2++) {  // 					Sequence.inp
    		os.print("|CTRL|" + BB_delimiter);
    	    for (int col = 0; col < MODEL_CONTROLLER.getColumnCount(); col++) {
		        os.print(MODEL_CONTROLLER.getValueAt(row2, col)+ BB_delimiter);
    	    	}
    	    os.println("");  	    
    }	
    	for (int row = 0; row < MODEL_SEQUENCE.getRowCount(); row++) {  // 					Sequence.inp
    		os.print("|SEQU|" + BB_delimiter);
    	    for (int col = 0; col < MODEL_SEQUENCE.getColumnCount(); col++) {
    	    	if (col==1) {
    	    		String str_val = (String) MODEL_SEQUENCE.getValueAt(row, col);
    	    		int val=0;
    	    		for(int k=0;k<SequenceENDType.length;k++) {if(str_val.equals(SequenceENDType[k])){val=k;}}
    	    		os.print(val+ BB_delimiter);
    	    	} else if (col==3) {
    	    		String str_val = (String) MODEL_SEQUENCE.getValueAt(row, col);
    	    		int val=0;
    	    		for(int k=0;k<SequenceType.length;k++) {if(str_val.equals(SequenceType[k])){val=k+1;}}
    	    		os.print(val+ BB_delimiter);
    	    	} else if (col==4) {
    	    		String str_val = (String) MODEL_SEQUENCE.getValueAt(row, col);
    	    		int val=0;
    	    		for(int k=0;k<SequenceFCCombobox.getItemCount();k++) {if(str_val.equals(SequenceFCCombobox.getItemAt(k))){val=k+1;}}
    	    		os.print(val+ BB_delimiter);
    	    	} else if (col==7) {
    	    		String str_val = (String) MODEL_SEQUENCE.getValueAt(row, col);
    	    		int val=0;
    	    		for(int k=0;k<FCTargetCurve.length;k++) {if(str_val.equals(FCTargetCurve[k])){val=k+1;}}
    	    		os.print(val+ BB_delimiter);
    	    	} else if (col==8) {
        	    	String str_val = (String) MODEL_SEQUENCE.getValueAt(row, col);
        	    	int val=0;
        	    	for(int k=0;k<SequenceTVCFCCombobox.getItemCount();k++) {if(str_val.equals(SequenceTVCFCCombobox.getItemAt(k))){val=k+1;}}
        	    	os.print(val+ BB_delimiter);
    	    	} else if (col==11) {
            	    String str_val = (String) MODEL_SEQUENCE.getValueAt(row, col);
            	    int val=0;
            	    for(int k=0;k<TargetCurve_Options_TVC.length;k++) {if(str_val.equals(TargetCurve_Options_TVC[k])){val=k+1;}}
            	    os.print(val+ BB_delimiter);
    	    	} else {
		        os.print(MODEL_SEQUENCE.getValueAt(row, col)+ BB_delimiter);
    	    	}
    	    }
    	    os.println("");
    	}
    
    	for (int i = 0; i < 3; i++) {  // 					sc.inp
    		os.print("|SC|" + BB_delimiter);
		               if (i==0){
		            	   double r=0;
	        				if(INPUT_SURFACEAREA.getText().isEmpty()) {
			        			r = 0;
	        				}else {
			        			r = Double.parseDouble(INPUT_SURFACEAREA.getText()) ;
	        				}
		            	   os.print("|SURFACEAREA[m2]|"+ BB_delimiter+r);
		     	} else if (i==1){
		            	   double r=0;
	        				if(INPUT_BALLISTICCOEFFICIENT.getText().isEmpty()) {
			        			r = 0;
	        				}else {
			        			r = Double.parseDouble(INPUT_BALLISTICCOEFFICIENT.getText()) ;
	        				}
		            	   os.print("|BC[Kgm-2]|"+ BB_delimiter+r);
		     	} else if (i==2){
		     	} else if (i==3){
		     	} else if (i==4){
		     	} else if (i==5){
		    	}
	        os.println("");
    	}
       os.close();   
       System.out.println("File "+CurrentWorkfile_Name+" saved.");
	}
}

	public static double[] Spherical2Cartesian(double[] X) {
	double[] result = new double[3];
	result[0]  =  X[0] * Math.cos(X[1]) * Math.cos(X[2]);
	result[1]  =  X[0] * Math.cos(X[1]) * Math.sin(X[2]);
	result[2]  = -X[0] * Math.sin(X[1]);
	
	// Filter small errors from binary conversion: 
	for(int i=0;i<result.length;i++) {if(Math.abs(result[i])<1E-9) {result[i]=0; }}
	return result; 
	}
	
	public static double[] Cartesian2Spherical(double[] X) {
	double[] result = new double[3];
	result[1] = -Math.atan(X[2]/(Math.sqrt(X[0]*X[0] + X[1]*X[1])));
	result[0] = Math.sqrt(X[0]*X[0] + X[1]*X[1] + X[2]*X[2]);
	result[2] = Math.atan2(X[1],X[0]);

	// Filter small errors from binary conversion: 
	for(int i=0;i<result.length;i++) {if(Math.abs(result[i])<1E-9) {result[i]=0; }}
	return result; 
	}
	/*
	public static DefaultTableXYDataset AddDataset_DashboardOverviewChart(double RM) throws IOException , FileNotFoundException, ArrayIndexOutOfBoundsException{		
	   	XYSeries xyseries11 = new XYSeries("Trajectory", false, false); 
	   	
	   	XYSeries xyseries_FPA_is = new XYSeries("Flight Path Angle", false, false);
	   	
	    FileInputStream fstream = null;
		try{ fstream = new FileInputStream(RES_File);} catch(IOException eIO) { System.out.println(eIO);}
	              DataInputStream in = new DataInputStream(fstream);
	              BufferedReader br = new BufferedReader(new InputStreamReader(in));
	              String strLine;
try {
	              while ((strLine = br.readLine()) != null )   {
		           String[] tokens = strLine.split(" ");
		           double x = Double.parseDouble(tokens[6]);
		           double y = Double.parseDouble(tokens[3]);
		           
		           double t = Double.parseDouble(tokens[0]);
		           double fpa = Double.parseDouble(tokens[7])*rad2deg;

		           try {
		           INDICATOR_VTOUCHDOWN.setText(""+decf.format(Double.parseDouble(tokens[6])));
		           INDICATOR_DELTAV.setText(""+decf.format(Double.parseDouble(tokens[99])));
		           INDICATOR_PROPPERC.setText(""+decf.format(Double.parseDouble(tokens[93]))); 
		           INDICATOR_RESPROP.setText(""+decf.format(Double.parseDouble(tokens[73])));
		           } catch (NumberFormatException e) {
		        	   System.err.println("Error: Emtpy String detected - Indicator Dashboard");
		           }
		           try {xyseries11.add(x  , y);} catch(org.jfree.data.general.SeriesException eSE) {
		        	   }
		           try {xyseries_FPA_is.add(t,fpa);} catch(org.jfree.data.general.SeriesException eSE) {
		        	   }
		        	   
		           }
	   	       fstream.close();
		       in.close();
		       br.close();
		       
			    CHART_P1_DashBoardOverviewChart_Dataset_Altitude_Velocity.addSeries(xyseries11);    
			    CHART_P1_DashBoardOverviewChart_Dataset_Time_FPA.addSeries(xyseries_FPA_is);
} catch (NullPointerException e) {
	System.out.println("ERRROR: AddDataset_DashboardOverviewChart");
}


	    return CHART_P1_DashBoardOverviewChart_Dataset_Altitude_Velocity;
	   }
	*/
	
	public static void createTargetView3D() {
        final JFXPanel fxPanel = new JFXPanel();
        //fxPanel.setSize(400,350);
  	   for(int i=0;i<SpaceShip3DControlPanelContent.size();i++) {
  		  SpaceShip3DControlPanel.remove((Component) SpaceShip3DControlPanelContent.get(i));
  	    }
        SpaceShip3DControlPanel.add(fxPanel,BorderLayout.CENTER);
        SpaceShip3DControlPanelContent.add(fxPanel);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	TargetView3D.start(fxPanel,indx_target);
            }
       });
      
	}
	
	public static void createTargetWindow() {
	  	   for(int i=0;i<targetWindowContent.size();i++) {
	  		 targetWindowContent.remove((Component) targetWindowContent.get(i));
	   	    }
        //fxPanel.setSize(400,350);
        targetWindow.add(targetWindowFxPanel,BorderLayout.CENTER);
        targetWindowContent.add(targetWindowFxPanel);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	TargetWindow.start(targetWindowFxPanel,indx_target);
            }
       });
      
	}
	
	public static void refreshTargetView3D() {
        SplitPane_Page1_Charts_vertical.setDividerLocation(500);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	TargetView3D.TargetBodyGroup.getChildren().removeAll();
            	TargetView3D.refreshTargetGroup(indx_target);
            }
       });
      
	}
	
	public static void refreshTargetWindow() {
		if(indx_target==0) {
			targetWindow.setBorder(Earth_border);
		} else if( indx_target==1) {
			targetWindow.setBorder(Moon_border);
		} else if( indx_target==2) {
			targetWindow.setBorder(Mars_border);
		} else if( indx_target==3) {
			targetWindow.setBorder(Venus_border);
		} 
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	TargetWindow.TargetBodyGroup.getChildren().removeAll();
           	TargetWindow.refreshTargetGroup(indx_target);
        
            }
       });
        targetWindow.revalidate();
        targetWindow.repaint();
        targetWindowFxPanel.revalidate();
        targetWindowFxPanel.repaint();
	}
	
	public static void refreshSpaceCraftView() {
		//SplitPane_Page1_Charts_vertical.remo
        final JFXPanel fxPanel = new JFXPanel();
        SplitPane_Page1_Charts_vertical2.add(fxPanel,JSplitPane.RIGHT);
        SplitPane_Page1_Charts_vertical2.setDividerLocation(500);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	SpaceShipView3DFrontPage.model.getChildren().removeAll();
            	SpaceShipView3DFrontPage.coordinateSystem.getChildren().removeAll();
            	//SpaceShipView3DFrontPage.root.getChildren().removeAll();
            	SpaceShipView3DFrontPage.start(fxPanel);
            }
       });
      
	}
	
	public static void CreateChart_DashboardOverviewChart_Altitude_Velocity() throws IOException {
		//CHART_P1_DashBoardOverviewChart = ChartFactory.createScatterPlot("", "Velocity [m/s]", "Altitude [m] ", CHART_P1_DashBoardOverviewChart_Dataset_Altitude_Velocity, PlotOrientation.VERTICAL, true, false, false); 
		CHART_P1_DashBoardOverviewChart_Altitude_Velocity = ChartFactory.createScatterPlot("", "", "", CHART_P1_DashBoardOverviewChart_Dataset_Altitude_Velocity);//("", "Velocity [m/s]", "Altitude [m] ", CHART_P1_DashBoardOverviewChart_Dataset_Altitude_Velocity, PlotOrientation.VERTICAL, true, false, false); 
		XYPlot plot = (XYPlot)CHART_P1_DashBoardOverviewChart_Altitude_Velocity.getXYPlot(); 
	    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
	    plot.setRenderer(0, renderer); 
	    renderer.setSeriesPaint( 0 , labelColor );	
	    CHART_P1_DashBoardOverviewChart_Altitude_Velocity.setBackgroundPaint(backgroundColor);
		Font font3 = new Font("Dialog", Font.PLAIN, 12); 	
		plot.getDomainAxis().setLabelFont(font3);
		plot.getRangeAxis().setLabelFont(font3);
		plot.getRangeAxis().setLabelPaint(labelColor);
		plot.getDomainAxis().setLabelPaint(labelColor);
		plot.setForegroundAlpha(0.8f);
		plot.setBackgroundPaint(backgroundColor);
		plot.setDomainGridlinePaint(labelColor);
		plot.setRangeGridlinePaint(labelColor); 
		CHART_P1_DashBoardOverviewChart_Altitude_Velocity.removeLegend();
		//CHART_P1_DashBoardOverviewChart_Altitude_Velocity.getLegend().setBackgroundPaint(backgroundColor);
		//CHART_P1_DashBoardOverviewChart_Altitude_Velocity.getLegend().setItemPaint(labelColor);;
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();

		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		//final NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
		//domainAxis.setInverted(true);
		
		//Shape cross = ShapeUtilities.createDiagonalCross(1, 1) ;
	    double size = 2.0;
	    double size2 = 1.0;
	    double delta = size / 2.0;
	    double delta2 = size2 / 2.0;
		Shape dot1 = new Ellipse2D.Double(-delta, -delta, size, size);
		Shape dot2 = new Ellipse2D.Double(-delta2, -delta2, size2, size2);
		renderer.setSeriesShape(0, dot1);
		renderer.setSeriesShape(1, dot2);
		
		JPanel PlotPanel_X43 = new JPanel();
		PlotPanel_X43.setLayout(new BorderLayout());
		PlotPanel_X43.setPreferredSize(new Dimension(900, page1_plot_y));
		PlotPanel_X43.setBackground(backgroundColor);
	
		ChartPanel_DashBoardOverviewChart_Altitude_Velocity = new ChartPanel(CHART_P1_DashBoardOverviewChart_Altitude_Velocity);
		ChartPanel_DashBoardOverviewChart_Altitude_Velocity.setMaximumDrawHeight(50000);
		ChartPanel_DashBoardOverviewChart_Altitude_Velocity.setMaximumDrawWidth(50000);
		ChartPanel_DashBoardOverviewChart_Altitude_Velocity.setMinimumDrawHeight(0);
		ChartPanel_DashBoardOverviewChart_Altitude_Velocity.setMinimumDrawWidth(0);
		ChartPanel_DashBoardOverviewChart_Altitude_Velocity.setMouseWheelEnabled(true);
		ChartPanel_DashBoardOverviewChart_Altitude_Velocity.setPreferredSize(new Dimension(900, page1_plot_y));
	    CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
	     xCrosshair_DashBoardOverviewChart_Altitude_Velocity = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	     xCrosshair_DashBoardOverviewChart_Altitude_Velocity.setLabelVisible(true);
	     xCrosshair_DashBoardOverviewChart_Altitude_Velocity.setLabelBackgroundPaint(labelColor);
	     yCrosshair_DashBoardOverviewChart_Altitude_Velocity = new Crosshair(Double.NaN, Color.RED, new BasicStroke(0f));
	     yCrosshair_DashBoardOverviewChart_Altitude_Velocity.setLabelVisible(true);
	     
	     yCrosshair_DashBoardOverviewChart_Altitude_Velocity.setLabelBackgroundPaint(labelColor);
	    crosshairOverlay.addDomainCrosshair(xCrosshair_DashBoardOverviewChart_Altitude_Velocity);
	    crosshairOverlay.addRangeCrosshair(yCrosshair_DashBoardOverviewChart_Altitude_Velocity);
		ChartPanel_DashBoardOverviewChart_Altitude_Velocity.addChartMouseListener(new ChartMouseListener() {
	        @Override
	        public void chartMouseClicked(ChartMouseEvent event) {
	            // ignore
	        }
	
	        @Override
	        public void chartMouseMoved(ChartMouseEvent event) {
	            Rectangle2D dataArea = BlueBookVisual.ChartPanel_DashBoardOverviewChart_Altitude_Velocity.getScreenDataArea();
	            JFreeChart chart = event.getChart();
	            XYPlot plot = (XYPlot) chart.getPlot();
	            ValueAxis xAxis = plot.getDomainAxis();
	            double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
	                    RectangleEdge.BOTTOM);
	            
	            //double max = xAxis.getUpperBound();
	           // double min = xAxis.getLowerBound();
	            //int indx = (int) ( (1- x/(max-min))*resultSet.size());
	            double y = DatasetUtilities.findYValue(plot.getDataset(), 0, x);
	            BlueBookVisual.xCrosshair_DashBoardOverviewChart_Altitude_Velocity.setValue(x);
	            BlueBookVisual.yCrosshair_DashBoardOverviewChart_Altitude_Velocity.setValue(y);
	        }
	});
	    ChartPanel_DashBoardOverviewChart_Altitude_Velocity.addOverlay(crosshairOverlay);
	   PlotPanel_X43.add(ChartPanel_DashBoardOverviewChart_Altitude_Velocity,BorderLayout.PAGE_START);
	   // P1_Plotpanel.add(PlotPanel_X43,BorderLayout.PAGE_START);
	   //SplitPane_Page1_Charts_vertical.add(ChartPanel_DashBoardOverviewChart_Altitude_Velocity, JSplitPane.LEFT);
	   FlexibleChartContentPanel2.add(ChartPanel_DashBoardOverviewChart_Altitude_Velocity, BorderLayout.CENTER);
	   //P1_Plotpanel.add(ChartPanel_DashBoardOverviewChart,BorderLayout.LINE_START);
		//jPanel4.validate();	
		CHART_P1_DashBoardOverviewChart_fd = false;
	}
	
	public static void CreateChart_ThirdWindowChart() throws IOException {
		ChartSetting chartSetting = new ChartSetting();
		chartSetting.setX(0);
		chartSetting.setY(6);
		List<String> variableList = new ArrayList<String>();
		for(int i=0;i<Axis_Option_NR.length;i++) {
			variableList.add(Axis_Option_NR[i]);
		}
        PlotElement plotElement = new PlotElement(0, variableList, analysisFile, chartSetting);
        JPanel plotElementPanel = plotElement.createPlotElement(plotElement);
  	   for(int i=0;i<SpaceShip3DControlPanelContent.size();i++) {
  		  SpaceShip3DControlPanel.remove((Component) SpaceShip3DControlPanelContent.get(i));
  	    }
	   FlexibleChartContentPanel.add(plotElementPanel, BorderLayout.CENTER);
	   SpaceShip3DControlPanel.add(plotElementPanel, BorderLayout.CENTER);
	   SpaceShip3DControlPanelContent.add(plotElementPanel);
	}
	public static void CreateChart_DashBoardFlexibleChart() throws IOException {
		//result1.removeAllSeries();
				try {
				ResultSet_FlexibleChart = AddDataset_DashboardFlexibleChart(4,3, ResultSet_FlexibleChart);
				} catch(FileNotFoundException | ArrayIndexOutOfBoundsException eFNF2) {
					
				}
			    //-----------------------------------------------------------------------------------
			    Chart_DashBoardFlexibleChart = ChartFactory.createScatterPlot("", "", "", ResultSet_FlexibleChart, PlotOrientation.VERTICAL, false, false, false); 
				XYPlot plot = (XYPlot)Chart_DashBoardFlexibleChart.getXYPlot(); 
			    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
			    plot.setRenderer(0, renderer); 
			    renderer.setSeriesPaint( 0 , labelColor);	
				Chart_DashBoardFlexibleChart.setBackgroundPaint(backgroundColor);
				Font font3 = new Font("Dialog", Font.PLAIN, 12); 	
				plot.getDomainAxis().setLabelFont(font3);
				plot.getRangeAxis().setLabelFont(font3);
				plot.getRangeAxis().setLabelPaint(labelColor);
				plot.getDomainAxis().setLabelPaint(labelColor);
				plot.setForegroundAlpha(0.5f);
				plot.setBackgroundPaint(backgroundColor);
				plot.setDomainGridlinePaint(labelColor);
				plot.setRangeGridlinePaint(labelColor); 
				//Chart_DashBoardFlexibleChart.getLegend().setBackgroundPaint(backgroundColor);
				//Chart_DashBoardFlexibleChart.getLegend().setItemPaint(labelColor);
				final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
				rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
				//final NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
				//domainAxis.setInverted(true);

				
				//Shape cross = ShapeUtilities.createDiagonalCross(1, 1) ;
			    double size = 2.0;
			    double delta = size / 2.0;
				Shape dot = new Ellipse2D.Double(-delta, -delta, size, size);
				renderer.setSeriesShape(0, dot);

			
				ChartPanel_DashBoardFlexibleChart = new ChartPanel(Chart_DashBoardFlexibleChart);
				ChartPanel_DashBoardFlexibleChart.setMaximumDrawHeight(50000);
				ChartPanel_DashBoardFlexibleChart.setMaximumDrawWidth(50000);
				ChartPanel_DashBoardFlexibleChart.setMinimumDrawHeight(0);
				ChartPanel_DashBoardFlexibleChart.setMinimumDrawWidth(0);
				ChartPanel_DashBoardFlexibleChart.setMouseWheelEnabled(true);
				//ChartPanel_DashBoardFlexibleChart.setPreferredSize(new Dimension(900, page1_plot_y));
				ChartPanel_DashBoardFlexibleChart.addChartMouseListener(new ChartMouseListener() {
			        @Override
			        public void chartMouseClicked(ChartMouseEvent event) {
			            // ignore
			        }
			
			        @Override
			        public void chartMouseMoved(ChartMouseEvent event) {
			            Rectangle2D dataArea = BlueBookVisual.ChartPanel_DashBoardFlexibleChart.getScreenDataArea();
			            JFreeChart chart = event.getChart();
			            XYPlot plot = (XYPlot) chart.getPlot();
			            ValueAxis xAxis = plot.getDomainAxis();
			            double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
			                    RectangleEdge.BOTTOM);
			            double y = DatasetUtilities.findYValue(plot.getDataset(), 0, x);
			            BlueBookVisual.xCH_DashboardFlexibleChart.setValue(x);
			            BlueBookVisual.yCH_DashboardFlexibleChart.setValue(y);
			        }
			});
			    CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
			    xCH_DashboardFlexibleChart = new Crosshair(Double.NaN, Color.RED, new BasicStroke(0f));
			    xCH_DashboardFlexibleChart.setLabelVisible(true);
			    xCH_DashboardFlexibleChart.setLabelPaint(labelColor);
			    xCH_DashboardFlexibleChart.setLabelBackgroundPaint(labelColor);
			    yCH_DashboardFlexibleChart = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
			    yCH_DashboardFlexibleChart.setLabelVisible(true);
			    yCH_DashboardFlexibleChart.setLabelBackgroundPaint(labelColor);
			    crosshairOverlay.addDomainCrosshair(xCH_DashboardFlexibleChart);
			    crosshairOverlay.addRangeCrosshair(yCH_DashboardFlexibleChart);
			    ChartPanel_DashBoardFlexibleChart.addOverlay(crosshairOverlay);
			   //PlotPanel_X44.add(ChartPanel_DashBoardFlexibleChart,BorderLayout.PAGE_START);
			    //P1_Plotpanel.add(PlotPanel_X44,BorderLayout.LINE_END);
			    //P1_Plotpanel.add(ChartPanel_DashBoardFlexibleChart,BorderLayout.CENTER);
			   FlexibleChartContentPanel.add(ChartPanel_DashBoardFlexibleChart, BorderLayout.CENTER);
				//jPanel4.validate();	
				Chart_DashBoardFlexibleChart_fd = false;
	}

	public static void Update_DashboardFlexibleChart(){
	    	ResultSet_FlexibleChart.removeAllSeries();
	    	try {
	    	ResultSet_FlexibleChart = AddDataset_DashboardFlexibleChart(variableListX.getSelectedIndx(),variableListY.getSelectedIndx(), 
	    			ResultSet_FlexibleChart);
	   //	Chart_DashBoardFlexibleChart.getXYPlot().getDomainAxis().setAttributedLabel(String.valueOf(axis_chooser.getSelectedItem()));
	    //	Chart_DashBoardFlexibleChart.getXYPlot().getRangeAxis().setAttributedLabel(String.valueOf(axis_chooser2.getSelectedItem()));
	    	} catch(ArrayIndexOutOfBoundsException | IOException eFNF2) {
	    	}
	}
	
	public static void Update_DashboardFlexibleChart2(){
		CHART_P1_DashBoardOverviewChart_Dataset_Altitude_Velocity.removeAllSeries();
    	try {
    		CHART_P1_DashBoardOverviewChart_Dataset_Altitude_Velocity = AddDataset_DashboardFlexibleChart(variableListX2.getSelectedIndx(),
    				variableListY2.getSelectedIndx(), CHART_P1_DashBoardOverviewChart_Dataset_Altitude_Velocity);
    	} catch(ArrayIndexOutOfBoundsException | IOException eFNF2) {
    	}
    	
}
	
	public static VariableList getVariableListY() {
		return variableListY;
	}
	public static VariableList getVariableListX() {
		return variableListX;
	}
	public static void createChart_3DRotation() {
		JPanel SpaceShip3DPanel = new JPanel();
		SpaceShip3DPanel.setLayout(new BorderLayout());
		SpaceShip3DPanel.setLocation(765, 10);
		//SpaceShip3DPanel.setBackground(backgroundColor);
		//SpaceShip3DPanel.setForeground(labelColor);
		SpaceShip3DPanel.setSize(450, 400);
		//SpaceShip3DPanel.setBorder(Moon_border);
		
        final JFXPanel fxPanel = new JFXPanel();
        SpaceShip3DPanel.add(fxPanel, BorderLayout.CENTER);
        SplitPane_Page1_Charts_vertical2.add(SpaceShip3DPanel, JSplitPane.RIGHT);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	SpaceShipView3DFrontPage.start(fxPanel);
            }
       });
	}
	public static XYSeriesCollection AddDataset_DashboardFlexibleChart(int x, int y, XYSeriesCollection XYSeries) throws IOException , IIOException, FileNotFoundException, ArrayIndexOutOfBoundsException{
	   			  XYSeries xyseries10 = new XYSeries("", false, true); 
	              FileInputStream fstream = null;
	      		try{ fstream = new FileInputStream(RES_File);} catch(IOException eIO) { System.out.println(eIO);}
	              DataInputStream in = new DataInputStream(fstream);
	              BufferedReader br = new BufferedReader(new InputStreamReader(in));
	              String strLine;
	              try {
			                  while ((strLine = br.readLine()) != null )   {
						            String[] tokens = strLine.split(" ");
						            double xx=0; double yy=0; 
						            if(x==3) {
						             xx = Double.parseDouble(tokens[x]); } else {
						            	 @SuppressWarnings("static-access")
										String x_axis_label = Axis_Option_NR[variableListX.getSelectedIndx()];
						            	 boolean isangle = x_axis_label.indexOf("[deg]") !=-1? true: false;
						            	 boolean isangle2 = x_axis_label.indexOf("[deg/s]") !=-1? true: false;
						            	 if(isangle || isangle2) {xx = Double.parseDouble(tokens[x])*rad2deg;} else {
						            		 		  xx = Double.parseDouble(tokens[x]);} 
						            	 }
						            if(y==3) {
						             yy = Double.parseDouble(tokens[y]);} else {
						            	 @SuppressWarnings("static-access")
										String x_axis_label = Axis_Option_NR[variableListY.getSelectedIndx()];
						            	 boolean isangle = x_axis_label.indexOf("[deg]") !=-1? true: false;
						            	 boolean isangle2 = x_axis_label.indexOf("[deg/s]") !=-1? true: false;
						            	 if(isangle || isangle2) {yy = Double.parseDouble(tokens[y])*rad2deg;} else {
						             yy = Double.parseDouble(tokens[y]);	}
						             }
						            
							           try {
								           INDICATOR_VTOUCHDOWN.setText(""+decf.format(Double.parseDouble(tokens[6])));
								           INDICATOR_DELTAV.setText(""+decf.format(Double.parseDouble(tokens[99])));
								           INDICATOR_PROPPERC.setText(""+decf.format(Double.parseDouble(tokens[93]))); 
								           INDICATOR_RESPROP.setText(""+decf.format(Double.parseDouble(tokens[73])));
								           } catch (NumberFormatException e) {
								        	   System.err.println("Error: Emtpy String detected - Indicator Dashboard");
								           }
						         	xyseries10.add(xx , yy);
					           }
	       in.close();
	       XYSeries.addSeries(xyseries10); 
	              } catch (NullPointerException eNPE) { 
	            	 // System.out.println(eNPE);
	            	  }
	    return XYSeries;
	   }
	public void SET_MAP(int TARGET) throws URISyntaxException, IOException{
		final XYPlot plot2 = (XYPlot) Chart_MercatorMap.getPlot();
		final PolarPlot plot_polar = (PolarPlot) chart_PolarMap.getPlot();
		  if (TARGET==0){ 
			  try {
		         BufferedImage myImage = ImageIO.read(new File(MAP_EARTH));
		         plot2.setBackgroundImage(myImage);  
			  } catch(IIOException eIIO) {
				  System.out.println(eIIO);System.out.println("ERROR: Reading maps failed.");
			  }
		  } else if (TARGET==1){
			  try {
		         BufferedImage myImage = ImageIO.read(new File(MAP_MOON));
		         BufferedImage myImage_Polar = ImageIO.read(new File(MAP_SOUTHPOLE_MOON));
		         plot2.setBackgroundImage(myImage);  
		         plot_polar.setBackgroundImage(myImage_Polar);
			  } catch(IIOException eIIO) {
				  System.out.println(eIIO);System.out.println("ERROR: Reading maps failed.");
			  }
		  } else if(TARGET==2){
			  try {
		         BufferedImage myImage = ImageIO.read(new File(MAP_MARS));
		         plot2.setBackgroundImage(myImage); 
			  } catch(IIOException eIIO) {
				  //System.out.println(eIIO);
				  System.out.println("ERROR: Reading maps failed.");
			  }
		  } else if(TARGET==3){
			  try {
		         BufferedImage myImage = ImageIO.read(new File(MAP_VENUS));
		         plot2.setBackgroundImage(myImage); 
		  } catch(IIOException eIIO) {
			  //System.out.println(eIIO);
			  System.out.println("ERROR: Reading maps failed.");
		  }
		  }
	}
	
	public static DefaultTableXYDataset AddDataset_GroundClearance() throws IOException, FileNotFoundException, ArrayIndexOutOfBoundsException{
       	XYSeries xyseries_FlightPath = new XYSeries("Flight Path", false, false); 
       	XYSeries xyseries_Delta = new XYSeries("Ground clearance", false, false); 
       	XYSeries xyseries_Elevation = new XYSeries("Local Elevation", false, false); 
       	@SuppressWarnings("unused")
		Random rand = new Random();
            FileInputStream fstream = null;
            FileInputStream fstream_LocalElev=null; 
            		try{ fstream = new FileInputStream(RES_File);fstream_LocalElev = new FileInputStream(LOCALELEVATIONFILE);} catch(IOException eIO) { System.out.println(eIO);}
                  DataInputStream in = new DataInputStream(fstream);
                  DataInputStream in2 = new DataInputStream(fstream_LocalElev);
                  BufferedReader br = new BufferedReader(new InputStreamReader(in));
                  BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));
                  String strLine;
                  String strLine_2;
                  try {
                  while ((strLine = br.readLine()) != null )   {
                   strLine_2 =  br2.readLine();
		           String[] tokens = strLine.split(" ");
		           String[] tokens2 = strLine_2.split(" ");
		           double y = Double.parseDouble(tokens[4]);     			 // Altitude 	[m]
		           double x = Double.parseDouble(tokens[40]);	 			 // Groundtrack [m]
		           try{
		           xyseries_FlightPath.add(x,y);
		           } catch ( org.jfree.data.general.SeriesException eSE){
		        	 // System.out.println(eSE); 
		           }
		           try{
		           double local_elevation = Double.parseDouble(tokens2[0]);
		           xyseries_Elevation.add(x,local_elevation);
		           xyseries_Delta.add(x,y-local_elevation);
		           } catch ( org.jfree.data.general.SeriesException eSE){
		        	  //System.out.println(eSE); 
		           }
                  }
           in.close();
           br.close();
           fstream.close();
           ResultSet_GroundClearance_FlightPath.addSeries(xyseries_FlightPath); 
           ResultSet_GroundClearance_FlightPath.addSeries(xyseries_Delta); 
           ResultSet_GroundClearance_Elevation.addSeries(xyseries_Elevation); 
                  } catch(NullPointerException eNPI) { System.out.print(eNPI); }
        return ResultSet_GroundClearance_FlightPath;          
       }
	public static void CreateChart_GroundClearance() throws IOException{
		ResultSet_GroundClearance_FlightPath.removeAllSeries();
		ResultSet_GroundClearance_Elevation.removeAllSeries();
		 try {
			 ResultSet_GroundClearance_FlightPath = AddDataset_GroundClearance(); 
		        } catch(FileNotFoundException | ArrayIndexOutOfBoundsException eFNF) {System.out.println(" Error read for plot X40");}
		        Chart_GroundClearance = ChartFactory.createXYAreaChart("", "Ground Track [km]", "Altitude/Elevation [m] ", ResultSet_GroundClearance_FlightPath, PlotOrientation.VERTICAL, true, false, false); 
				XYPlot plot = (XYPlot)Chart_GroundClearance.getXYPlot(); 
				StackedXYAreaRenderer renderer_Area = new StackedXYAreaRenderer( );
		        XYItemRenderer renderer_Line = new StandardXYItemRenderer();
		        renderer_Line.setSeriesPaint(0,Color.black);
		        renderer_Line.setSeriesPaint(1,Color.orange);
		        renderer_Area.setSeriesPaint(0,Color.gray);

		        plot.setRenderer(0, renderer_Line);  
 
		        plot.setDataset(1, ResultSet_GroundClearance_Elevation);
		        plot.setRenderer(1, renderer_Area);
			
		        Chart_GroundClearance.setBackgroundPaint(Color.white);
				
		        plot.getDomainAxis().setLabelFont(labelfont_small);
		        plot.getRangeAxis().setLabelFont(labelfont_small);
				
		       final XYPlot plot2 = (XYPlot) Chart_GroundClearance.getPlot();
		       plot2.setForegroundAlpha(0.5f);
		       plot2.setBackgroundPaint(Color.white);
		       plot2.setDomainGridlinePaint(Color.black);
		       plot2.setRangeGridlinePaint(new Color(220,220,220));

		       ValueAxis domain2 = plot.getDomainAxis();
		       //domain2.setRange(-180, 180);
		       domain2.setInverted(true);
		       // change the auto tick unit selection to integer units only...
		       final NumberAxis rangeAxis2 = (NumberAxis) plot2.getRangeAxis();
		       rangeAxis2.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		       //rangeAxis2.setRange(-90, 90);
		       ChartPanel CPXX4 = new ChartPanel(Chart_GroundClearance);
		       CPXX4.setBackground(backgroundColor);
		       //CPXX4.setDomainZoomable(false);
		       //CPXX4.setRangeZoomable(false);
		       CPXX4.setMaximumDrawHeight(50000);
		       CPXX4.setMaximumDrawWidth(50000);
		       CPXX4.setMinimumDrawHeight(0);
		       CPXX4.setMinimumDrawWidth(0);
		       CPXX4.setPreferredSize(new Dimension(1300, 660));
		       PageX04_GroundClearance.add(CPXX4, BorderLayout.CENTER);	
	}
	public static XYSeriesCollection AddDataset_Mercator_MAP() throws IOException, FileNotFoundException, ArrayIndexOutOfBoundsException{
       	XYSeries xyseries10 = new XYSeries("", false, true); 

            FileInputStream fstream = null;
            		try{ fstream = new FileInputStream(RES_File);} catch(IOException eIO) { System.out.println(eIO);}
                  DataInputStream in = new DataInputStream(fstream);
                  BufferedReader br = new BufferedReader(new InputStreamReader(in));
                  String strLine;
                  try {
                  while ((strLine = br.readLine()) != null )   {
		           String[] tokens = strLine.split(" ");
		           double y = Double.parseDouble(tokens[2])*180/PI;  // Latitude [deg[
		           double x = Double.parseDouble(tokens[1])*180/PI;	 // Longitude [deg]
		           while (x>180 || x<-180 || y>90 || y<-90){
		           if (x>180){
		        	   x=x-360;
		           } else if (x<-180){
		        	   x=x+360;
		           }
		           if (y>90){
		        	   y=y-180;
		           } else if (y<-90){
		        	   y=y+180;
		           }
		           }
		           //System.out.println(x + " | " + y);
		         	xyseries10.add(x,y);
		           }
           in.close();
        ResultSet_MercatorMap.addSeries(xyseries10); 
                  } catch(NullPointerException eNPI) { System.out.print(eNPI); }
        return ResultSet_MercatorMap;          
       }
	public static XYSeriesCollection AddDataset_Polar_MAP() throws IOException, FileNotFoundException, ArrayIndexOutOfBoundsException{
       	XYSeries xyseries10 = new XYSeries("", false, true); 

            FileInputStream fstream = null;
            		try{ fstream = new FileInputStream(RES_File);} catch(IOException eIO) { System.out.println(eIO);}
                  DataInputStream in = new DataInputStream(fstream);
                  BufferedReader br = new BufferedReader(new InputStreamReader(in));
                  String strLine;
                  try {
                  while ((strLine = br.readLine()) != null )   {
		           String[] tokens = strLine.split(" ");
		           double y = Double.parseDouble(tokens[2])*180/PI;  // Latitude [deg[
		           double x = Double.parseDouble(tokens[1])*180/PI;	 // Longitude [deg]
		           
		           while (x>360 || x<0 || y>90 || y<-90){
				                  if (x > 360){x=x-360;
				           } else if (x <   0){x=x+360; }
				           
				                  if (y>90){ y=y-180;
				           } else if (y<-90){ y=y+180;}
		           }
		           //System.out.println(x + " | " + y);
		         	xyseries10.add(x,y);
		           }
           in.close();
           ResultSet_PolarMap.addSeries(xyseries10); 
                  } catch(NullPointerException eNPI) { System.out.print(eNPI); }
        return ResultSet_PolarMap;          
       }
	public static void CreateChart_MercatorMap() throws IOException{
		 try {
		        ResultSet_MercatorMap = AddDataset_Mercator_MAP(); 
		        } catch(FileNotFoundException | ArrayIndexOutOfBoundsException eFNF) {
		        	System.out.println(" Error read for plot X40");
		        }

		        Chart_MercatorMap = ChartFactory.createScatterPlot("", "Longitude [deg]", "Latitude [deg] ", ResultSet_MercatorMap, PlotOrientation.VERTICAL, false, false, false); 
				XYPlot plot = (XYPlot)Chart_MercatorMap.getXYPlot(); 
		        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
		        
			    double size = 2.0;
			    double delta = size / 2.0;
				Shape dot = new Ellipse2D.Double(-delta, -delta, size, size);
				renderer.setSeriesShape(0, dot);
				renderer.setSeriesLinesVisible(0, false);
		        plot.setRenderer(0, renderer);
		        
		        Chart_MercatorMap.setBackgroundPaint(Color.white);
				
		        plot.getDomainAxis().setLabelFont(labelfont_small);
		        plot.getRangeAxis().setLabelFont(labelfont_small);
				
		       final XYPlot plot2 = (XYPlot) Chart_MercatorMap.getPlot();
		       plot2.setForegroundAlpha(0.5f);
		       plot2.setBackgroundPaint(Color.white);
		       plot2.setDomainGridlinePaint(Color.black);
		       plot2.setRangeGridlinePaint(new Color(220,220,220));

		       ValueAxis domain2 = plot.getDomainAxis();
		       domain2.setRange(-180, 180);
		       domain2.setInverted(false);
		       // change the auto tick unit selection to integer units only...
		       final NumberAxis rangeAxis2 = (NumberAxis) plot2.getRangeAxis();
		       rangeAxis2.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		       rangeAxis2.setRange(-90, 90);
		       ChartPanel CPXX4 = new ChartPanel(Chart_MercatorMap);
		       CPXX4.setBackground(backgroundColor);
		       CPXX4.setDomainZoomable(false);
		       CPXX4.setRangeZoomable(false);
		       CPXX4.setMaximumDrawHeight(50000);
		       CPXX4.setMaximumDrawWidth(50000);
		       CPXX4.setMinimumDrawHeight(0);
		       CPXX4.setMinimumDrawWidth(0);
				//CP2.setMouseWheelEnabled(tru
		       CPXX4.addChartMouseListener(new ChartMouseListener() {
		           @Override
		           public void chartMouseClicked(ChartMouseEvent event) {
		        	   Rectangle2D dataArea2 = CPXX4.getScreenDataArea();
		               Point2D p = CPXX4.translateScreenToJava2D(event.getTrigger().getPoint());
		               double x = Chart_MercatorMap.getXYPlot().getDomainAxis().java2DToValue(event.getTrigger().getX(), dataArea2, RectangleEdge.BOTTOM);
		               double y = plot2.getRangeAxis().java2DToValue(p.getY(), dataArea2, plot2.getRangeAxisEdge());
		               INDICATOR_PageMap_LONG.setText("" + df_X4.format(x));
		               INDICATOR_PageMap_LAT.setText("" + df_X4.format(y));
		           }

		           @Override
		           public void chartMouseMoved(ChartMouseEvent event) {
		        	   Rectangle2D dataArea2 = CPXX4.getScreenDataArea();
		        	   Point2D p = CPXX4.translateScreenToJava2D(event.getTrigger().getPoint());
		               ValueAxis xAxis2 = Chart_MercatorMap.getXYPlot().getDomainAxis();
		               double x = xAxis2.java2DToValue(event.getTrigger().getX(), dataArea2, RectangleEdge.BOTTOM);
		              // double y = yAxis2.java2DToValue(event.getTrigger().getYOnScreen(), dataArea3, RectangleEdge.BOTTOM);
		               double y = plot2.getRangeAxis().java2DToValue(p.getY(), dataArea2, plot2.getRangeAxisEdge());
		               BlueBookVisual.xCrosshair_x.setValue(x);
		               BlueBookVisual.yCrosshair_x.setValue(y);
		           }
		   });
		       CrosshairOverlay crosshairOverlay2 = new CrosshairOverlay();
		       xCrosshair_x = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
		       xCrosshair_x.setLabelVisible(true);
		       yCrosshair_x = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
		       yCrosshair_x.setLabelVisible(true);
		       crosshairOverlay2.addDomainCrosshair(xCrosshair_x);
		       crosshairOverlay2.addRangeCrosshair(yCrosshair_x);
		       CPXX4.addOverlay(crosshairOverlay2);
		       CPXX4.setPreferredSize(new Dimension(1300, 660));
		       PageX04_Map.add(CPXX4, BorderLayout.CENTER);	
	}
	
	public static JButton getyAxisIndicator() {
		return yAxisIndicator;
	}
	public static void CreateChart_PolarMap() throws IOException {
		ResultSet_PolarMap.removeAllSeries();
        try {
        	ResultSet_PolarMap = AddDataset_Polar_MAP(); 
        } catch(FileNotFoundException | ArrayIndexOutOfBoundsException eFNF) {
        	System.out.println(" Error read for plot X40");
        }

        chart_PolarMap = ChartFactory.createPolarChart("", ResultSet_PolarMap, false, false, false);
  
		PolarPlot plot =  (PolarPlot) chart_PolarMap.getPlot();
		MyDefaultPolarItemRenderer renderer = new MyDefaultPolarItemRenderer();
		renderer.setSeriesFillPaint(0, Color.red);
        plot.setRenderer(0, renderer);
	
		chart_PolarMap.setBackgroundPaint(Color.white);

		
        plot.getAxis().setLabelFont(labelfont_small);
   
       ValueAxis domain2 = plot.getAxis();
       domain2.setRange(-90, -70);
       domain2.setInverted(false);
       domain2.setTickLabelPaint(Color.white);
       plot.setAngleGridlinePaint(Color.white);
       plot.setAngleLabelPaint(Color.white);
       // change the auto tick unit selection to integer units only...
       
       ChartPanel CPXX4 = new ChartPanel(chart_PolarMap);
       CPXX4.setBackground(backgroundColor);
       CPXX4.setLayout(new BorderLayout());
       CPXX4.setDomainZoomable(false);
       CPXX4.setRangeZoomable(false);
       CPXX4.setPreferredSize(new Dimension(800, 800));
       CPXX4.setMaximumDrawHeight(50000);
       CPXX4.setMaximumDrawWidth(50000);
       CPXX4.setMinimumDrawHeight(0);
       CPXX4.setMinimumDrawWidth(0);
       JPanel innerPanel = new JPanel();
       innerPanel.setLayout(new BorderLayout());
       innerPanel.setPreferredSize(new Dimension(800, 800));
       innerPanel.add(CPXX4, BorderLayout.CENTER);
       PolarMapContainer.add(innerPanel);
       PolarMapContainer.addComponentListener(new ComponentAdapter() {
           @Override
           public void componentResized(ComponentEvent e) {
        	   KeepAspectRatio_Map(innerPanel, PolarMapContainer);
           }
       });
	}
	
	private static void KeepAspectRatio_Map(JPanel innerPanel, JPanel container) {
        int w = container.getWidth();
        int h = container.getHeight();
        int size =  Math.min(w, h);
        innerPanel.setPreferredSize(new Dimension(size, size));
        container.revalidate();
    }
	
	public static void CreateLocalElevationFile(int Resolution) throws IOException {
        FileInputStream fstream = null;
        ArrayList<String> steps = new ArrayList<String>();
        String Elevation_File="";
        if(Resolution==4) {
        	Elevation_File = Elevation_File_RES4;
        } else if (Resolution==16) {
        	Elevation_File = Elevation_File_RES16;
        } else if (Resolution==64) {
        	Elevation_File = Elevation_File_RES64;
        } else if (Resolution==128) {
        	Elevation_File = Elevation_File_RES128;
        }
		try{ fstream = new FileInputStream(RES_File);} catch(IOException eIO) { System.out.println(eIO);}
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String strLine;
      try {
      while ((strLine = br.readLine()) != null )   {
       String[] tokens = strLine.split(" ");
       double longitude = Double.parseDouble(tokens[1])*rad2deg;     // Longitude 	[deg]
       double latitude  = Double.parseDouble(tokens[2])*rad2deg;     // Latitude 	[deg]
       double local_elevation = GetLocalElevation(Elevation_File, longitude, latitude);
       //System.out.println(local_elevation);
       steps.add(local_elevation+" ");
      }
      	String resultpath="";
      	String dir = System.getProperty("user.dir");
      	resultpath = dir + "/LocalElevation.inp";
      PrintWriter writer = new PrintWriter(new File(resultpath), "UTF-8");
      for(String step: steps) {
          writer.println(step);
      }
      System.out.println("Write: Local Elevation File. ");

		writer.close();
		in.close();
		br.close();
		fstream.close();
      } catch(NullPointerException eNPI) { System.out.print(eNPI); }
		
	}
	@SuppressWarnings("resource")
	public static double GetLocalElevation(String InputFile, double longitude, double latitude) throws IOException {
		double ELEVATION = 0;
		int resolution = 4;
		int latitude_indx = (int) ((90-latitude)*resolution+1);
		int longitude_indx = (int) (longitude*resolution+1);
		FileInputStream inputStream = null;
		Scanner sc  = null;
		String path = InputFile;
		long k =0; 
		double max_runtime = 2; 
		long startTime = System.nanoTime();
		boolean TargetNOTReached=true; 
		File Input = new File(InputFile);
		try {
		    inputStream = new FileInputStream(path);
		    LineIterator it = FileUtils.lineIterator(Input, "UTF-8");
		  //  sc = new Scanner(inputStream, "UTF-8");
		    while (it.hasNext() && TargetNOTReached) {
		        String line = it.nextLine();
		        if(latitude_indx==k) {
					String[] tokens = line.split(",");
		        	ELEVATION = Double.parseDouble(tokens[longitude_indx]);
		        	TargetNOTReached=false;
		        	it.close();
		        	return ELEVATION;
		        }
				long endTime   = System.nanoTime();
				long totalTime = endTime - startTime;
				double  totalTime_sec = (double) (totalTime * 1E-9);
				if(totalTime_sec>max_runtime) {break;}
				k++;
		    }
		    // note that Scanner suppresses exceptions
		    /*
		    if (sc.ioException() != null) {
		        throw sc.ioException();
		    } */
		    it.close();
		} finally {
		    if (inputStream != null) {
		        inputStream.close();
		    }
		    if (sc != null) {
		        sc.close();
		    }
		}
		return ELEVATION;
	}
	/*
    public JPanel WINDOW_CreateLocalElevationFile() throws IOException, SQLException{
    	//---------------------------------------------------
    	// 				Data Select 
    	//---------------------------------------------------
    	// data_select == 1 => Export Requirements
    	// data_select == 2 => Export Change Log
    	//---------------------------------------------------
	   	JPanel MainGUI = new JPanel();
	   	MainGUI = new JPanel();
	   	//MainGUI.setLayout(new BorderLayout());
	   	MainGUI.setLayout(null);
	   	MainGUI.setBackground(backgroundColor);	
   		int extx = 370;
   		int exty = 400;
	  //----------------------------------------------------------------
   		
        JLabel Title = new JLabel("Select Resolution: ");
        Title.setLocation(5, 2 );
        Title.setSize(250, 15);
        Title.setBackground(backgroundColor);
        Title.setForeground(labelColor);
        MainGUI.add(Title);
        
	  	  @SuppressWarnings({ "rawtypes", "unchecked" })
		JComboBox ResolutionChooser = new JComboBox(LocalElevation_Resolution);
	  	ResolutionChooser.setLocation(30,20);
	  	ResolutionChooser.setSize(230,25);
	  	ResolutionChooser.setSelectedIndex(0);
	  	ResolutionChooser.setBorder(BorderFactory.createLineBorder(Color.black));
	  	ResolutionChooser.addActionListener(new ActionListener() { 
	    	  public void actionPerformed(ActionEvent e) {
	    		  // Get selected File Resolution from ResolutionChooser:
	    		  int Resoltuion = Integer.parseInt((String) ResolutionChooser.getSelectedItem());
	    		  // Create Elevation File:
	    		  
              	try {
						CreateLocalElevationFile(Resoltuion);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
              	// Update Ground Clearance Chart with generated data:
              	ResultSet_GroundClearance_FlightPath.removeAllSeries();
        		ResultSet_GroundClearance_Elevation.removeAllSeries();
       		 try {
       			 ResultSet_GroundClearance_FlightPath = AddDataset_GroundClearance(); 
       		        } catch(FileNotFoundException | ArrayIndexOutOfBoundsException eFNF) {System.out.println(" Error read for plot X40");} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
       		 // Dispose frame
       		frame_CreateLocalElevationFile.dispose();
	    	  }});
			  	  MainGUI.add(ResolutionChooser); 
	   	//----------------------------------------------------------------
	    //MainGUI.setOpaque(true);
        MainGUI.setSize(extx,exty);
	    return MainGUI;
    }
    */
	public static List<atm_dataset> INITIALIZE_Page03_storage_DATA() throws URISyntaxException{
    	   try{ // Temperature
    	       	FileInputStream fstream = null; 
    	       	try {
    	       	              fstream = new FileInputStream(RES_File);
    	       	} catch(IOException eIIO) { System.out.println(eIIO); } 
    		          DataInputStream in = new DataInputStream(fstream);
    		          BufferedReader br = new BufferedReader(new InputStreamReader(in));
    		          String strLine;
    		          int k = 0;
    		          while ((strLine = br.readLine()) != null )   {
			    if (k==0){
			    // Head line -> skip 	
			    } else {
    		        	  double time = 0;
    		        	  double velocity = 0 ; 

    		   String[] tokens = strLine.split(" ");
    		   time = Double.parseDouble(tokens[0]);		// Altitude
    		   velocity = Double.parseDouble(tokens[4]);		// density
    		   atm_dataset insert = new atm_dataset( time,  velocity, 0, 0,  0); 
    		   Page03_storage.add(insert);
    }
    		   k++;
    		   }
    		   in.close();
    		   }catch (Exception e){
    		     System.err.println("Error: " + e.getMessage());
    		   }
    	   return Page03_storage;	
    }
    
    public static void UpdateChart_A01() throws IOException , IIOException, FileNotFoundException, ArrayIndexOutOfBoundsException, NullPointerException, URISyntaxException{
    	result11_A3_1.removeAllSeries();
    	result11_A3_2.removeAllSeries();
    	result11_A3_3.removeAllSeries();
    	result11_A3_4.removeAllSeries();
    	
    	Page03_storage.removeAll(Page03_storage);
    	INITIALIZE_Page03_storage_DATA();
    	
       	XYSeries xyseries10 = new XYSeries("", false, true); 
       	XYSeries xyseries20 = new XYSeries("", false, true); 
       	XYSeries xyseries30 = new XYSeries("", false, true); 
       	XYSeries xyseries40 = new XYSeries("", false, true); 
       	FileInputStream fstream = null; 
try { fstream = new FileInputStream(RES_File);  } catch(IOException eIIO) { System.out.println(eIIO); } 
              DataInputStream in = new DataInputStream(fstream);
              BufferedReader br = new BufferedReader(new InputStreamReader(in));
              String strLine;
              try {
              while ((strLine = br.readLine()) != null )   {
	           String[] tokens = strLine.split(" ");
	           double x1 = Double.parseDouble(tokens[4]);
	           double y1 = Double.parseDouble(tokens[3])-RM;
	           
	           double x2 = Double.parseDouble(tokens[0]);
	           double y2 = Double.parseDouble(tokens[3])-RM;
	          
	           double x3 = Double.parseDouble(tokens[0]);
	           double y3 = Double.parseDouble(tokens[5])*rad2deg;
	           
	           double x4 = 0 , y4=0;
	           if (chartA3_fd==true){
	           x4 = Double.parseDouble(tokens[0]);
	           y4 = Double.parseDouble(tokens[Axis_Option_NR.length-2]);
	           } else {
		       x4 = Double.parseDouble(tokens[axis_chooser3.getSelectedIndex()]);
		       y4 = Double.parseDouble(tokens[axis_chooser4.getSelectedIndex()]);   
		       chartA3_4.getXYPlot().getDomainAxis().setLabel(Axis_Option_NR[axis_chooser3.getSelectedIndex()]);
		       chartA3_4.getXYPlot().getRangeAxis().setLabel(Axis_Option_NR[axis_chooser4.getSelectedIndex()]);
	           }

	         	xyseries10.add(x1 , y1);
	         	xyseries20.add(x2 , y2);
	         	xyseries30.add(x3 , y3);
	         	xyseries40.add(x4 , y4);
	         	//System.out.println(x1);
	           }
       in.close();
       result11_A3_1.addSeries(xyseries10); 
       result11_A3_2.addSeries(xyseries20);
       result11_A3_3.addSeries(xyseries30);
       result11_A3_4.addSeries(xyseries40);
              } catch (NullPointerException eNPE) { System.out.println(eNPE);}
    }
    
    
	public static void CreateChart_A01() {
	
				try {
					try {
						UpdateChart_A01();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (ArrayIndexOutOfBoundsException | NullPointerException | URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
	    	
	    	try {
	    		Page03_storage.removeAll(Page03_storage);
				INITIALIZE_Page03_storage_DATA();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				System.out.println("storage list init error");
				e.printStackTrace();
			}
	        //-----------------------------------------------------------------------------------
	    	//AddDataset_101();
	        //-----------------------------------------------------------------------------------
	    	String x_str_01 = "Velocity [m/s]";
	    	String y_str_01 = "Altitude [m]";
	    	String x_str_02 = "Time [s]";
	    	String y_str_02 = "Altitude [m]";
	    	String x_str_03 = "Time [s]";
	    	String y_str_03 = "Flight Path Angle [deg]";
	    	String x_str_04 = "Time [s]";
	    	String y_str_04 = "Normaliced Acceleration [-]";
	    	//int xplot = 670;
	    	int yplot = 350; 
			JPanel TopPanel = new JPanel();
			TopPanel.setLayout(new BorderLayout());
			TopPanel.setPreferredSize(new Dimension(extx_main, yplot));
			TopPanel.setBackground(backgroundColor);
			JPanel BottomPanel = new JPanel();
			BottomPanel.setLayout(new BorderLayout());
			BottomPanel.setPreferredSize(new Dimension(extx_main, yplot));
			BottomPanel.setBackground(backgroundColor);
	    	
			JPanel PlotPanel_01 = new JPanel();
			PlotPanel_01.setLayout(new BorderLayout());
			//PlotPanel_01.setPreferredSize(new Dimension(xplot, yplot));
			PlotPanel_01.setBackground(backgroundColor);
			JPanel PlotPanel_02 = new JPanel();
			PlotPanel_02.setLayout(new BorderLayout());
			//PlotPanel_02.setPreferredSize(new Dimension(xplot, yplot));
			PlotPanel_02.setBackground(backgroundColor);
			JPanel PlotPanel_03 = new JPanel();
			PlotPanel_03.setLayout(new BorderLayout());
			//PlotPanel_03.setPreferredSize(new Dimension(xplot, yplot));
			PlotPanel_03.setBackground(backgroundColor);
			JPanel PlotPanel_04 = new JPanel();
			PlotPanel_04.setLayout(new BorderLayout());
			//PlotPanel_04.setPreferredSize(new Dimension(xplot, yplot));
			PlotPanel_04.setBackground(backgroundColor);
			
			JPanel Midpanel = new JPanel();
			Midpanel.setLayout(null);
			//Midpanel.setPreferredSize(new Dimension(155, 300));
			Midpanel.setSize(155,300);
			Midpanel.setBackground(backgroundColor);
			BottomPanel.add(Midpanel, BorderLayout.CENTER);
			
		      JLabel p41_linp8 = new JLabel("X-Axis");
		      p41_linp8.setLocation(5, 10 + 25 * 1 );
		      //p41_linp8.setPreferredSize(new Dimension(150, 20));
		      p41_linp8.setHorizontalAlignment(0);
		      p41_linp8.setSize(150,20);
		      p41_linp8.setBackground(backgroundColor);
		      p41_linp8.setForeground(labelColor);
		      Midpanel.add(p41_linp8);
		      JLabel p41_linp9 = new JLabel("Y-Axis");
		      p41_linp9.setLocation(5, 10 + 25 * 4 );
		      //p41_linp9.setPreferredSize(new Dimension(150, 20));
		      p41_linp9.setSize(150, 20);
		      p41_linp9.setHorizontalAlignment(0);
		      p41_linp9.setBackground(backgroundColor);
		      p41_linp9.setForeground(labelColor);
		      Midpanel.add(p41_linp9);
			  axis_chooser3 = new JComboBox<Object>(Axis_Option_NR);
			  axis_chooser4 = new JComboBox<Object>(Axis_Option_NR);
		      axis_chooser4.setLocation(5, 10 + 25 * 5);
		      //axis_chooser2.setPreferredSize(new Dimension(150,25));
		     // axis_chooser4.setPreferredSize(new Dimension(150,25));
		      axis_chooser4.setSize(150,25);
		      axis_chooser4.setSelectedIndex(28);
		      axis_chooser4.addActionListener(new ActionListener() { 
		    	  public void actionPerformed(ActionEvent e) {
		    		  try {
						UpdateChart_A01();
					} catch (ArrayIndexOutOfBoundsException | NullPointerException | IOException | URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    	  }
		  	  } );
		      axis_chooser3.setLocation(5, 10 + 25 * 2);
		      //axis_chooser.setPreferredSize(new Dimension(150,25));
		      //axis_chooser3.setPreferredSize(new Dimension(150,25));
		      axis_chooser3.setSize(150,25);
		      axis_chooser3.setSelectedIndex(0);
		      axis_chooser3.addActionListener(new ActionListener() { 
		    	  public void actionPerformed(ActionEvent e) {
		    		  try {
						UpdateChart_A01();
					} catch (ArrayIndexOutOfBoundsException | NullPointerException | IOException | URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    	  }
		  	  } );
		      Midpanel.add(axis_chooser3);
		      Midpanel.add(axis_chooser4);
	//--------------------------------------------------------------------------------------------------------------------------------------
	    	chartA3_1 = ChartFactory.createScatterPlot("", x_str_01, y_str_01, result11_A3_1, PlotOrientation.VERTICAL, false, false, false); 
	        XYLineAndShapeRenderer renderer131 = new XYLineAndShapeRenderer( );
	        renderer131.setSeriesPaint( 0 , Color.BLACK );
			Font font3 = new Font("Dialog", Font.PLAIN, 12); 
			renderer131.setSeriesPaint( 2 , Color.gray );
			chartA3_1.getXYPlot().getDomainAxis().setLabelFont(font3);
			chartA3_1.getXYPlot().getRangeAxis().setLabelFont(font3);
			chartA3_1.getXYPlot().setRenderer(0, renderer131); 
			chartA3_1.setBackgroundPaint(Color.white);
			chartA3_1.getXYPlot().setForegroundAlpha(0.5f);
			chartA3_1.getXYPlot().setBackgroundPaint(Color.white);
			chartA3_1.getXYPlot().setDomainGridlinePaint(new Color(220,220,220));
			chartA3_1.getXYPlot().setRangeGridlinePaint(new Color(220,220,220));
			chartA3_1.getXYPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
			CP_A31 = new ChartPanel(chartA3_1);
			//CP_A31.setSize(586,350);
			//CP_A31.setLocation(2, 5);
			TopPanel.add(CP_A31,BorderLayout.CENTER);
	
	        //-----------------------------------------------------------------------------------
	    	chartA3_2 = ChartFactory.createScatterPlot("", x_str_02, y_str_02, result11_A3_2, PlotOrientation.VERTICAL, false, false, false); 
	    	 XYLineAndShapeRenderer renderer132 = new XYLineAndShapeRenderer( );
	    	renderer132.setSeriesPaint( 0 , Color.BLACK );	
			chartA3_2.getXYPlot().getDomainAxis().setLabelFont(font3);
			chartA3_2.getXYPlot().getRangeAxis().setLabelFont(font3);
			chartA3_2.getXYPlot().setRenderer(0, renderer132); 
			chartA3_2.setBackgroundPaint(Color.white);
			chartA3_2.getXYPlot().setForegroundAlpha(0.5f);
			chartA3_2.getXYPlot().setBackgroundPaint(Color.white);
			chartA3_2.getXYPlot().setDomainGridlinePaint(new Color(220,220,220));
			chartA3_2.getXYPlot().setRangeGridlinePaint(new Color(220,220,220));
			chartA3_2.getXYPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
			CP_A32 = new ChartPanel(chartA3_2);
			//CP_A32.setSize(736,350);
			//CP_A32.setLocation(590, 5);
			TopPanel.add(CP_A32,BorderLayout.EAST);
			//PageX04_3.add(PlotPanel_02);
	        //-----------------------------------------------------------------------------------
	    	chartA3_3 = ChartFactory.createScatterPlot("", x_str_03, y_str_03, result11_A3_3, PlotOrientation.VERTICAL, false, false, false); 
	    	 XYLineAndShapeRenderer renderer133 = new XYLineAndShapeRenderer( );
	    	 renderer133.setSeriesPaint( 0 , Color.BLACK );
	    	 renderer133.setSeriesPaint( 2 , Color.gray );
	        chartA3_3.getXYPlot().getDomainAxis().setLabelFont(font3);
	        chartA3_3.getXYPlot().getRangeAxis().setLabelFont(font3);
	        chartA3_3.getXYPlot().setRenderer(0, renderer133); 
	        chartA3_3.setBackgroundPaint(Color.white);
	        chartA3_3.getXYPlot().setForegroundAlpha(0.5f);
	        chartA3_3.getXYPlot().setBackgroundPaint(Color.white);
	        chartA3_3.getXYPlot().setDomainGridlinePaint(new Color(220,220,220));
	        chartA3_3.getXYPlot().setRangeGridlinePaint(new Color(220,220,220));
	        chartA3_3.getXYPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	        CP_A33 = new ChartPanel(chartA3_3);
	        //CP_A33.setSize(586,350);
	        //CP_A33.setLocation(2, 370);
	        BottomPanel.add(CP_A33,BorderLayout.WEST);
			//PageX04_3.add(PlotPanel_03);
			//-----------------------------------------------------------------------------------
	        chartA3_4 = ChartFactory.createScatterPlot("", x_str_04, y_str_04, result11_A3_4, PlotOrientation.VERTICAL, false, false, false); 
			XYPlot xyplot = (XYPlot)chartA3_4.getPlot(); 
	        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
	        xyplot.setRenderer(0, renderer); 
	        renderer.setSeriesPaint( 0 , Color.BLACK );
	        chartA3_4.setBackgroundPaint(Color.white);		
			xyplot.getDomainAxis().setLabelFont(font3);
			xyplot.getRangeAxis().setLabelFont(font3);
			
			final XYPlot plot = (XYPlot) chartA3_4.getPlot();
			plot.setForegroundAlpha(0.5f);
			//plot.setBackgroundPaint(new Color(238,238,238));
			plot.setBackgroundPaint(Color.white);
			plot.setDomainGridlinePaint(new Color(220,220,220));
			plot.setRangeGridlinePaint(new Color(220,220,220));
			final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
			rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());	
		    //plot.setDataset(0, result11_A3_4);
	
		    
		    //XYLineAndShapeRenderer splinerenderer_1 = new XYLineAndShapeRenderer();
		    //splinerenderer_1.setSeriesPaint(0, Color.BLACK);
	
	
	
			    CP_A34 = new ChartPanel(chartA3_4);
			    //CP_A34.setSize(780,360);
			    //CP_A34.setLocation(588, 370);
			    BottomPanel.add(CP_A34,BorderLayout.EAST);
			   // PageX04_3.add(CP_A34);
	
	        CP_A31.addChartMouseListener(new ChartMouseListener() {
	            @Override
	            public void chartMouseClicked(ChartMouseEvent event) {
	                // Update inforboard
	               // Rectangle2D dataArea = BB_AddOn_3DOF.CP_A31.getScreenDataArea();
	                //JFreeChart chart = event.getChart();
	             //   XYPlot plot = chartA3_1.getXYPlot();
	              //  ValueAxis xAxis = plot.getDomainAxis();
	            //    double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
	              //          RectangleEdge.BOTTOM);
	              //  double y = DatasetUtilities.findYValue(chartA3_1.getXYPlot().getDataset(), 0, x);
	               // BlueBook_main.xCrosshair_A3_1.setValue(x);
	               // BlueBook_main.yCrosshair_A3_1.setValue(y);
	                //===================================================
	                //double xx = xCrosshair_A3_1.getValue();
	              //  double yy = DatasetUtilities.findYValue((chartA3_2.getXYPlot()).getDataset(), 0, x);
	                //BlueBook_main.xCrosshair_A3_2.setValue(xx);
	               // BlueBook_main.yCrosshair_A3_2.setValue(yy);
	                //===================================================
	                //double xxx = xCrosshair_A3_1.getValue();
	               // double yyy = DatasetUtilities.findYValue((chartA3_3.getXYPlot()).getDataset(), 0, x);
	               // BlueBook_main.xCrosshair_A3_3.setValue(xxx);
	               // BlueBook_main.yCrosshair_A3_3.setValue(yyy);
	                //===================================================
	                //double xxxx = xCrosshair_A3_1.getValue();
	               // double yyyy = DatasetUtilities.findYValue((chartA3_4.getXYPlot()).getDataset(), 0, x);
	               // BlueBook_main.xCrosshair_A3_4.setValue(xxxx);
	               // BlueBook_main.yCrosshair_A3_4.setValue(yyyy);
	                //===================================================
	            	
	            }
	
	            @Override
	            public void chartMouseMoved(ChartMouseEvent event) {
	                Rectangle2D dataArea = BlueBookVisual.CP_A31.getScreenDataArea();
	                //JFreeChart chart = event.getChart();
	                XYPlot plot = chartA3_1.getXYPlot();
	                ValueAxis xAxis = plot.getDomainAxis();
	                double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
	                        RectangleEdge.BOTTOM);
	                double y = DatasetUtilities.findYValue(chartA3_1.getXYPlot().getDataset(), 0, x);
	                BlueBookVisual.xCrosshair_A3_1.setValue(x);
	                BlueBookVisual.yCrosshair_A3_1.setValue(y);
	                //===================================================
	                double time = get_time(x);
	                double xx = time ;//xCrosshair_A3_1.getValue();
	                double yy = DatasetUtilities.findYValue((chartA3_2.getXYPlot()).getDataset(), 0, time);
	                BlueBookVisual.xCrosshair_A3_2.setValue(xx);
	                BlueBookVisual.yCrosshair_A3_2.setValue(yy);
	                //===================================================
	                double xxx = time ; xCrosshair_A3_1.getValue();
	                double yyy = DatasetUtilities.findYValue((chartA3_3.getXYPlot()).getDataset(), 0, time);
	                BlueBookVisual.xCrosshair_A3_3.setValue(xxx);
	                BlueBookVisual.yCrosshair_A3_3.setValue(yyy);
	                //===================================================
	                double xxxx = time ; // xCrosshair_A3_1.getValue();
	                double yyyy = DatasetUtilities.findYValue((chartA3_4.getXYPlot()).getDataset(), 0, time);
	                BlueBookVisual.xCrosshair_A3_4.setValue(xxxx);
	                BlueBookVisual.yCrosshair_A3_4.setValue(yyyy);
	                //===================================================
	            }
	    });
	        CrosshairOverlay crosshairOverlay3 = new CrosshairOverlay();
	        xCrosshair_A3_1 = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	        xCrosshair_A3_1.setLabelVisible(true);
	        yCrosshair_A3_1 = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	        yCrosshair_A3_1.setLabelVisible(true);
	        crosshairOverlay3.addDomainCrosshair(xCrosshair_A3_1);
	        crosshairOverlay3.addRangeCrosshair(yCrosshair_A3_1);
	        CP_A31.addOverlay(crosshairOverlay3);
	      //===================================================
	        CrosshairOverlay crosshairOverlay4 = new CrosshairOverlay();
	        xCrosshair_A3_2 = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	        xCrosshair_A3_2.setLabelVisible(true);
	        yCrosshair_A3_2 = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	        yCrosshair_A3_2.setLabelVisible(true);
	        crosshairOverlay4.addDomainCrosshair(xCrosshair_A3_2);
	        crosshairOverlay4.addRangeCrosshair(yCrosshair_A3_2);
	        CP_A32.addOverlay(crosshairOverlay4); 
	        //===================================================
	        CrosshairOverlay crosshairOverlay5 = new CrosshairOverlay();
	        xCrosshair_A3_3 = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	        xCrosshair_A3_3.setLabelVisible(true);
	        yCrosshair_A3_3 = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	        yCrosshair_A3_3.setLabelVisible(true);
	        crosshairOverlay5.addDomainCrosshair(xCrosshair_A3_3);
	        crosshairOverlay5.addRangeCrosshair(yCrosshair_A3_3);
	        CP_A33.addOverlay(crosshairOverlay5); 
	        //===================================================
	        CrosshairOverlay crosshairOverlay6 = new CrosshairOverlay();
	        xCrosshair_A3_4 = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	        xCrosshair_A3_4.setLabelVisible(true);
	        yCrosshair_A3_4 = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	        yCrosshair_A3_4.setLabelVisible(true);
	        crosshairOverlay6.addDomainCrosshair(xCrosshair_A3_4);
	        crosshairOverlay6.addRangeCrosshair(yCrosshair_A3_4);
	        CP_A34.addOverlay(crosshairOverlay6); 
	        //===================================================
	        chartA3_fd = false;
			PageX04_3.add(TopPanel, BorderLayout.CENTER);
			PageX04_3.add(BottomPanel, BorderLayout.PAGE_END);
	    }
	public static double get_time(double velocity) {
		double time = 0;
		int leng = Page03_storage.size();
		double data_x[] = new double[leng];
		double data_y[] = new double[leng];
			for (int i = 0;i<leng;i++){
				data_y[i] = Page03_storage.get(i).get_altitude();  // time 
				data_x[i] = Page03_storage.get(i).get_density();   // velocity
			}
		time = Mathbox.LinearInterpolate( data_x , data_y , velocity);
//System.out.println(velocity + " | " + time);
		return time;
	}
    
	private static void createAndShowGUI() throws IOException {
        JFrame.setDefaultLookAndFeelDecorated(false);
        MAIN_frame = new JFrame("" + PROJECT_TITLE);
        MAIN_frame.setFont(small_font);
        BlueBookVisual demo = new BlueBookVisual();
        JPanel tp = demo.createContentPane();
        tp.setPreferredSize(new java.awt.Dimension(x_init, y_init));
        MAIN_frame.add(tp, BorderLayout.CENTER);
        MAIN_frame.pack();
        MAIN_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MAIN_frame.setLocationRelativeTo(null);
        MAIN_frame.setExtendedState(MAIN_frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
        MAIN_frame.setVisible(true);
        // Create Icon image  -  top left for windows
         try {
        	BufferedImage myIcon = ImageIO.read(new File(ICON_File)); 
        	MAIN_frame.setIconImage(myIcon);
         }catch(IIOException eIIO) {System.out.println(eIIO);}    
         // Create taskbar icon - for mac 
         if(OS_is==1) {
        	 // Set Taskbar Icon for MacOS
         try {
         Application application = Application.getApplication();
         Image image = Toolkit.getDefaultToolkit().getImage(ICON_File);
         application.setDockIconImage(image);
         } catch(Exception e) {
        	 System.err.println("Taskbar icon could not be created");
         }
         }
    }
    
    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
					createAndShowGUI();
				} catch (IOException  e) {System.out.println(e);}
            }
        });
    }

		public static String getPROJECT_TITLE() {
		return PROJECT_TITLE;
	}
		public static int RETURN_TARGET(){
			return TARGET; 
		}
		public static String getICON_File() {
			return ICON_File;
		}
		public static double getRM() {
			return RM;
		}
		public static List<RealTimeResultSet> getResultSet() {
			return resultSet;
		}
		
		
		public static Color getLabelColor() {
			return labelColor;
		}
		public static Color getBackgroundColor() {
			return backgroundColor;
		}


		public class BackgroundMenuBar extends JMenuBar {
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			Color bgColor=Color.WHITE;

		    public void setColor(Color color) {
		        bgColor=color;
		    }

		    @Override
		    protected void paintComponent(Graphics g) {
		        super.paintComponent(g);
		        Graphics2D g2d = (Graphics2D) g;
		        g2d.setColor(bgColor);
		        g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);

		    }
		}
		
		public class BackgroundMenu extends JMenu {
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			Color bgColor=Color.WHITE;

		    public void setColor(Color color) {
		        bgColor=color;
		    }

		    @Override
		    protected void paintComponent(Graphics g) {
		        super.paintComponent(g);
		        Graphics2D g2d = (Graphics2D) g;
		        g2d.setColor(bgColor);
		        g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);

		    }
		}
		
		public static class CustomRenderer extends DefaultListCellRenderer {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(@SuppressWarnings("rawtypes") JList list, Object value,
			        int index, boolean isSelected, boolean cellHasFocus) {
			    super.getListCellRendererComponent(list, value, index, isSelected,
			            cellHasFocus);
			    setBackground(backgroundColor);
			    setForeground(labelColor);     
			    return this;
			}  
		}

		public static String[] getAxis_Option_NR() {
			return Axis_Option_NR;
		}
		public static Font getSmall_font() {
			return small_font;
		}
		public static  List<JLabel> getSequenceProgressBarContent() {
			return sequenceProgressBarContent;
		}
		public static  List<GUISequenceElement> getSequenceContentList() {
			return sequenceContentList;
		}
		
		
		
}
class ColorArrowUI extends BasicComboBoxUI {

    public static ComboBoxUI createUI(JComponent c) {
        return new ColorArrowUI();
    }

    @Override protected JButton createArrowButton() {
        return new BasicArrowButton(
            BasicArrowButton.EAST,
            Color.GRAY, Color.magenta,
            Color.yellow, Color.blue);
    }
}