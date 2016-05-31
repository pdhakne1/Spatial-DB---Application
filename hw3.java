import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.List;
import java.awt.Point;
import java.awt.Polygon;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.RepaintManager;

import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;

import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.sql.Array;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Path2D;

import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import java.awt.Font;

public class hw3 {

	private JFrame frmPallaviVDhakne;
	private JLabel labelPosition;
	private Image img;
	private JLabel imgLabel;
	private JCheckBox chckbxBuildings;
	private JCheckBox chckbxBuildingsOnFire;
	private JCheckBox chckbxHydrants;
	private JRadioButton rdbtnWholeRegion;
	private JRadioButton rdbtnRangeQuery;
	private JRadioButton rdbtnFindNeighborBuildings;
	private JRadioButton rdbtnFinfClosestFire;
	private JTextArea textAreaQuery;
	private Path2D currentShape;
    private int currentyPoint;
    private int currentxPoint;
    private ArrayList<Integer> x= new ArrayList<Integer>();
    private ArrayList<Integer> y= new ArrayList<Integer>();
    private java.util.List<Polygon> buildings= new ArrayList<Polygon>();
    private java.util.List<Polygon> Rangebuildings= new ArrayList<Polygon>();
    private java.util.List<Polygon> RangeFirebuildings= new ArrayList<Polygon>();
    private java.util.List<Point> Rangehydrants= new ArrayList<Point>();
    private java.util.List<Polygon> firebuildings= new ArrayList<Polygon>();
    private java.util.List<Point> hydrants= new ArrayList<Point>();
    private java.util.List<Point> userPoint= new ArrayList<Point>();
    private java.util.List<Polygon> userRange= new ArrayList<Polygon>();
    private java.util.List<Polygon> neighborBuildings= new ArrayList<Polygon>();
    private java.util.List<Polygon> fireneighborBuildings= new ArrayList<Polygon>();
    private java.util.List<Point> userFirePoint= new ArrayList<Point>();
    private java.util.List<Polygon> userfireBuildings= new ArrayList<Polygon>();
    private java.util.List<Point> closestHydrants= new ArrayList<Point>();
    private boolean isvalue=false; 
    private String eventValue="";
    private int[] xval;
    private int[] yval;
    private int counter=0;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					hw3 window = new hw3();
					window.frmPallaviVDhakne.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	Connection dbConnect = null;
	private JPanel panel_1;
	private JLabel lblActive;
	private JLabel lblQuery;
	/**
	 * Create the application.
	 */
	public hw3() {
		initialize();
		dbConnect = dbConnection();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPallaviVDhakne = new JFrame();
		WindowListener wl = new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                try{
                    dbConnect.close();
                    System.out.println("Connection closed successfully!!");
                    frmPallaviVDhakne.dispose();
                    System.exit(0);
                }
                catch(SQLException ex){
                	ex.printStackTrace();
                }
                
            }
	 };
		frmPallaviVDhakne.setTitle("Pallavi V. Dhakne, Student ID: WXXXXXXX");
		frmPallaviVDhakne.setBounds(100, 100, 1200, 800);
		frmPallaviVDhakne.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmPallaviVDhakne.addWindowListener(wl);
		frmPallaviVDhakne.getContentPane().setLayout(null);
		
		chckbxBuildings = new JCheckBox("Buildings");
		chckbxBuildings.setBounds(846, 60, 97, 23);
		frmPallaviVDhakne.getContentPane().add(chckbxBuildings);
		
		chckbxBuildingsOnFire = new JCheckBox("Buildings on Fire");
		chckbxBuildingsOnFire.setBounds(846, 82, 161, 23);
		frmPallaviVDhakne.getContentPane().add(chckbxBuildingsOnFire);
		
		chckbxHydrants = new JCheckBox("Hydrants");
		chckbxHydrants.setBounds(1026, 82, 95, 23);
		frmPallaviVDhakne.getContentPane().add(chckbxHydrants);
		
		rdbtnWholeRegion = new JRadioButton("Whole Region");
		rdbtnWholeRegion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eventValue="NoValue";
				userFirePoint.clear();
				userfireBuildings.clear();
				userPoint.clear();
				imgLabel.repaint();
				rdbtnRangeQuery.setSelected(false);
				rdbtnFindNeighborBuildings.setSelected(false);
				rdbtnFinfClosestFire.setSelected(false);
				textAreaQuery.setText("");
			}
		});
		rdbtnWholeRegion.setBounds(846, 150, 152, 23);
		frmPallaviVDhakne.getContentPane().add(rdbtnWholeRegion);
		
		rdbtnRangeQuery = new JRadioButton("Range Query");
		rdbtnRangeQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eventValue="NoValue";
				userFirePoint.clear();
				userfireBuildings.clear();
				userPoint.clear();
				imgLabel.repaint();
				rdbtnWholeRegion.setSelected(false);
				rdbtnFindNeighborBuildings.setSelected(false);
				rdbtnFinfClosestFire.setSelected(false);
				textAreaQuery.setText("");
			}
		});
		rdbtnRangeQuery.setBounds(846, 170, 122, 23);
		frmPallaviVDhakne.getContentPane().add(rdbtnRangeQuery);
		
		rdbtnFindNeighborBuildings = new JRadioButton("Find Neighbor Buildings");
		rdbtnFindNeighborBuildings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eventValue="NoValue";
				userFirePoint.clear();
				userfireBuildings.clear();
				userPoint.clear();
				imgLabel.repaint();
				rdbtnRangeQuery.setSelected(false);
				rdbtnWholeRegion.setSelected(false);
				rdbtnFinfClosestFire.setSelected(false);
				chckbxBuildings.setSelected(false);
				chckbxBuildingsOnFire.setSelected(false);
				chckbxHydrants.setSelected(false);
				textAreaQuery.setText("");
				
			}
		});
		rdbtnFindNeighborBuildings.setBounds(846, 190, 210, 23);
		frmPallaviVDhakne.getContentPane().add(rdbtnFindNeighborBuildings);
		
		rdbtnFinfClosestFire = new JRadioButton("Find Closest Fire Hydrants");
		rdbtnFinfClosestFire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eventValue="NoValue";
				imgLabel.repaint();
				userFirePoint.clear();
				userfireBuildings.clear();
				userPoint.clear();
				rdbtnRangeQuery.setSelected(false);
				rdbtnFindNeighborBuildings.setSelected(false);
				rdbtnWholeRegion.setSelected(false);
				chckbxBuildings.setSelected(false);
				chckbxBuildingsOnFire.setSelected(false);
				chckbxHydrants.setSelected(false);
				textAreaQuery.setText("");
			}
		});
		rdbtnFinfClosestFire.setBounds(846, 210, 230, 23);
		frmPallaviVDhakne.getContentPane().add(rdbtnFinfClosestFire);
		
		JButton btnSubmitQuery = new JButton("Submit Query");
		btnSubmitQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				buildings.clear();
				firebuildings.clear();
				hydrants.clear();
				Rangebuildings.clear();
				RangeFirebuildings.clear();
				Rangehydrants.clear();
				//userPoint.clear();
				
				if(rdbtnWholeRegion.isSelected())
				{
					userPoint.clear();
					userFirePoint.clear();
					userfireBuildings.clear();
					readData(dbConnect,imgLabel);
				}
				if(rdbtnRangeQuery.isSelected())
				{
					//userPoint.clear();
					userFirePoint.clear();
					userfireBuildings.clear();
					getRangeresult(dbConnect);
				}
				if(rdbtnFindNeighborBuildings.isSelected())
				{
					userPoint.clear();
					userFirePoint.clear();
					userfireBuildings.clear();
					getNearestNeighbor(dbConnect);
				}
				if(rdbtnFinfClosestFire.isSelected())
				{
					userPoint.clear();
					closestHydrant(dbConnect);
				}
				
			}
		});
		btnSubmitQuery.setBounds(935, 302, 141, 23);
		frmPallaviVDhakne.getContentPane().add(btnSubmitQuery);
		
		JLabel lblCurrentMouseLocation = new JLabel("Current mouse location:");
		lblCurrentMouseLocation.setBounds(10, 602, 187, 14);
		frmPallaviVDhakne.getContentPane().add(lblCurrentMouseLocation);
		
		imgLabel = new ImageLabel("");
		imgLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				eventValue="NoEvent"; 
				textAreaQuery.setText("");
				if(arg0.getButton() == MouseEvent.BUTTON1)
				    {
						if(counter==0)
						{
						  userPoint.clear();
						  counter=1;
						}
							  eventValue="Point";
							  currentxPoint=arg0.getX();
							  currentyPoint=arg0.getY();
							  Point p= new Point(arg0.getX(),arg0.getY());
							  if(rdbtnRangeQuery.isSelected())
							  {
								userPoint.add(p); 
								imgLabel.repaint();
							  }
							  if(rdbtnFinfClosestFire.isSelected())
							  {
								  userFirePoint.add(p);
								  userFireBuilding(dbConnect);
								  imgLabel.repaint();
							  }
						
					  
	                }
				    	    
				    else if(arg0.getButton() == MouseEvent.BUTTON3)
				    {
				    	//JOptionPane.showMessageDialog(null,"Detected Mouse Right Click!");
				    	counter=0;
				    	eventValue="Range";
				    	imgLabel.repaint();
				    	
				    }
				
			}
		});
		imgLabel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent arg0) {
				int x,y;
				x=arg0.getX();
				y=arg0.getY();
				labelPosition.setText("("+x+","+y+")");
			}
		});
		img = new ImageIcon(this.getClass().getResource("/map.jpg")).getImage();
		imgLabel.setIcon(new ImageIcon(img));
		imgLabel.setBounds(10, 11, 820, 580);
		frmPallaviVDhakne.getContentPane().add(imgLabel);
		
		labelPosition = new JLabel("");
		labelPosition.setBounds(218, 602, 100, 14);
		frmPallaviVDhakne.getContentPane().add(labelPosition);
		
		textAreaQuery = new JTextArea();
		textAreaQuery.setEditable(false);
		textAreaQuery.setLineWrap(true);
		textAreaQuery.setBounds(10, 622, 1055, 108);
		frmPallaviVDhakne.getContentPane().add(textAreaQuery);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(840, 28, 306, 84);
		frmPallaviVDhakne.getContentPane().add(panel);
		
		lblActive = new JLabel("Active Feature Type");
		lblActive.setVerticalAlignment(SwingConstants.TOP);
		lblActive.setFont(new Font("Arial", Font.BOLD, 14));
		panel.add(lblActive);
		
		panel_1 = new JPanel();
		panel_1.setToolTipText("Query");
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBounds(840, 123, 306, 132);
		frmPallaviVDhakne.getContentPane().add(panel_1);
		
		lblQuery = new JLabel("Query");
		lblQuery.setFont(new Font("Arial", Font.BOLD, 14));
		panel_1.add(lblQuery);

		
		
	}
	
	public static Connection dbConnection()
	{
		try {
			 
			Class.forName("oracle.jdbc.driver.OracleDriver");
 
		} catch (ClassNotFoundException e) {
 
			System.out.println("Oracle JDBC Driver not found.");
			e.printStackTrace();
			return null;
 
		}
 
		System.out.println("Oracle JDBC Driver Registered!");
 
		Connection connection = null;
		
		try {
 
			connection = DriverManager.getConnection(
					"jdbc:oracle:thin:@dagobah.engr.scu.edu:1521:db11g", "pdhakne",
					"changeit");
			System.out.println("Connected successfully!!");
			return connection;
 
		} catch (SQLException e) {
 
			System.out.println("Connection Failed! Check console for errors.");
			e.printStackTrace();
			return null;
 
		}
 
	}
	
	public  void readData(Connection conn,JLabel imgLabel)
	{
		try {
			
			eventValue="WholeRange";
			Statement statement = conn.createStatement();
			 ResultSet rs;
			 String textQuery="Query1:";
			 if(chckbxBuildings.isSelected())
			 {
				 rs = statement.executeQuery("SELECT SHAPE FROM buildings b");
                 String newbname="";
                 textQuery=textQuery+"SELECT SHAPE FROM buildings b";
				 if(rs!=null)
				 {
					 
					 while(rs.next())
					 {	
						 
						 STRUCT st = (oracle.sql.STRUCT) rs.getObject(1);
						 JGeometry j_geom = JGeometry.load(st);
						 double[] ordinates= j_geom.getOrdinatesArray();
						 
						 xval= new int[ordinates.length/2];
						 yval= new int[ordinates.length/2];
						 for(int i=0,j=0;i<ordinates.length;i=i+2,j++)
						 {
							
							 xval[j]= (int)ordinates[i];
							 yval[j]= (int)ordinates[i+1];
						 }
						 Polygon p= new Polygon(xval, yval, xval.length);
						 buildings.add(p);
						 
					 }
					 //System.out.println(xval.length);
					 isvalue=true;
					 //imgLabel.repaint();
					 
				 }
			 }
			 if(chckbxBuildingsOnFire.isSelected())
			 {
				 
				 rs = statement.executeQuery("SELECT SHAPE FROM buildings where bName in (Select fname from fire_buildings)");
				 textQuery=textQuery+"\n"+"SELECT SHAPE FROM buildings where bName in (Select fname from fire_buildings)";
				 if(rs!=null)
				 {
					 
					 while(rs.next())
					 {	
						 
						 STRUCT st = (oracle.sql.STRUCT) rs.getObject(1);
						 JGeometry j_geom = JGeometry.load(st);
						 double[] ordinates= j_geom.getOrdinatesArray();
						 
						 int[] x= new int[ordinates.length/2];
						 int[] y= new int[ordinates.length/2];
						 for(int i=0,j=0;i<ordinates.length;i=i+2,j++)
						 {
							
							 x[j]= (int)ordinates[i];
							 y[j]= (int)ordinates[i+1];
						 }
						 Polygon p= new Polygon(x, y, x.length);
						 firebuildings.add(p);
						 
					 }
					 isvalue=true;
					 //imgLabel.repaint();
					
				 }
			 }
			
			 if(chckbxHydrants.isSelected())
			 {
				 rs = statement.executeQuery("SELECT SHAPE FROM hydrants");
				 textQuery=textQuery+"\n"+"SELECT SHAPE FROM hydrants";
				 if(rs!=null)
				 {
					 while(rs.next())
					 {	
						 
						 STRUCT st = (oracle.sql.STRUCT) rs.getObject(1);
						 JGeometry j_geom = JGeometry.load(st);
						 double[] ordinates= j_geom.getOrdinatesArray();
						 
						 int[] x= new int[ordinates.length/2];
						 int[] y= new int[ordinates.length/2];
						 for(int i=0,j=0;i<ordinates.length;i=i+2,j++)
						 {
							
							 x[j]= (int)ordinates[i];
							 y[j]= (int)ordinates[i+1];
							 Point p= new Point(x[j], y[j]);
							 hydrants.add(p);
						 }
						 
						 
					 }
					 isvalue=true;
					 //imgLabel.repaint();
				 }
			 }
			 textAreaQuery.setText(textQuery);
			 imgLabel.repaint();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	     
	}
	public void getRangeresult(Connection conn)
	{
		try{
			eventValue="RangeQuery";
			String textQuery="Query2:";
			if(userPoint.size()>2)
			{
			String rangecordinates=userPoint.get(0).x+","+userPoint.get(0).y+",";
			for(int i=1;i<userPoint.size();i++)
			 {
				 int x1 = userPoint.get(i).x;
				 int y1 = userPoint.get(i).y;
				 rangecordinates=rangecordinates+x1+","+y1+",";
				 
			 } 
			rangecordinates=rangecordinates+userPoint.get(0).x+","+userPoint.get(0).y;
			//System.out.println("rangecordinates"+rangecordinates);
			Statement statement = conn.createStatement();
			ResultSet rs;
			if(chckbxBuildings.isSelected())
			{
				String query="SELECT b.shape FROM buildings b WHERE SDO_ANYINTERACT(b.shape,sdo_geometry (2003, null, null, sdo_elem_info_array (1,1003,1),sdo_ordinate_array ("+rangecordinates+"))) = 'TRUE'";
				textQuery=textQuery+query;
				rs = statement.executeQuery(query);
				if(rs!=null)
				 {
					 
					 while(rs.next())
					 {	
						 
						 STRUCT st = (oracle.sql.STRUCT) rs.getObject(1);
						 JGeometry j_geom = JGeometry.load(st);
						 double[] ordinates= j_geom.getOrdinatesArray();
						 
						 int[] x= new int[ordinates.length/2];
						 int[] y= new int[ordinates.length/2];
						 for(int i=0,j=0;i<ordinates.length;i=i+2,j++)
						 {
							
							 x[j]= (int)ordinates[i];
							 y[j]= (int)ordinates[i+1];
						 }
						 Polygon p= new Polygon(x, y, x.length);
						 Rangebuildings.add(p);
						 //imgLabel.repaint();
					 }
				 }
			}
			if(chckbxBuildingsOnFire.isSelected())
			{
				String query="SELECT b.shape FROM buildings b WHERE b.bName in (select fname from fire_buildings) AND SDO_ANYINTERACT(b.shape,sdo_geometry (2003, null, null, sdo_elem_info_array (1,1003,1),sdo_ordinate_array ("+rangecordinates+"))) = 'TRUE'";
				textQuery=textQuery+"\n"+query;
				rs = statement.executeQuery(query);
				if(rs!=null)
				 {
					 
					 while(rs.next())
					 {	
						 
						 STRUCT st = (oracle.sql.STRUCT) rs.getObject(1);
						 JGeometry j_geom = JGeometry.load(st);
						 double[] ordinates= j_geom.getOrdinatesArray();
						 
						 int[] x= new int[ordinates.length/2];
						 int[] y= new int[ordinates.length/2];
						 for(int i=0,j=0;i<ordinates.length;i=i+2,j++)
						 {
							
							 x[j]= (int)ordinates[i];
							 y[j]= (int)ordinates[i+1];
						 }
						 Polygon p= new Polygon(x, y, x.length);
						 RangeFirebuildings.add(p);
						// imgLabel.repaint();
					 }
				 }
			}
			if(chckbxHydrants.isSelected())
			{
				String query="SELECT b.shape FROM hydrants b WHERE SDO_ANYINTERACT(b.shape,sdo_geometry (2003, null, null, sdo_elem_info_array (1,1003,1),sdo_ordinate_array ("+rangecordinates+"))) = 'TRUE'";
				textQuery=textQuery+"\n"+query;
				rs = statement.executeQuery(query);
				if(rs!=null)
				 {
					 
					 while(rs.next())
					 {	
						 
						 STRUCT st = (oracle.sql.STRUCT) rs.getObject(1);
						 JGeometry j_geom = JGeometry.load(st);
						 double[] ordinates= j_geom.getOrdinatesArray();
						 
						 int[] x= new int[ordinates.length/2];
						 int[] y= new int[ordinates.length/2];
						 for(int i=0,j=0;i<ordinates.length;i=i+2,j++)
						 {
							
							 x[j]= (int)ordinates[i];
							 y[j]= (int)ordinates[i+1];
							 Point p= new Point(x[j], y[j]);
							 Rangehydrants.add(p);
						 }
						 
						 //imgLabel.repaint();
					 }
				 }
			}
			imgLabel.repaint();
			textAreaQuery.setText(textQuery);
			}
			else{
				textAreaQuery.setText("No Polygon Selected.");
			}
			
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void getNearestNeighbor(Connection conn)
	{
		try{
			eventValue="NeighborQuery";
			Statement statement = conn.createStatement();
			ResultSet rs;
			
			String query="select shape from buildings where bName in(select b1.bName from buildings b1,(select b.shape as bshape, b.bName as bname from buildings b , fire_buildings f where b.bName =f.fname) b2 where b1.bname not in (select fname from fire_buildings) and SDO_WITHIN_DISTANCE(b1.SHAPE,b2.bshape,'distance=100')='TRUE')";
			rs = statement.executeQuery(query);
			if(rs!=null)
			 {
				 
				 while(rs.next())
				 {	
					 
					 STRUCT st = (oracle.sql.STRUCT) rs.getObject(1);
					 JGeometry j_geom = JGeometry.load(st);
					 double[] ordinates= j_geom.getOrdinatesArray();
					 
					 int[] x= new int[ordinates.length/2];
					 int[] y= new int[ordinates.length/2];
					 for(int i=0,j=0;i<ordinates.length;i=i+2,j++)
					 {
						
						 x[j]= (int)ordinates[i];
						 y[j]= (int)ordinates[i+1];
					 }
					 Polygon p= new Polygon(x, y, x.length);
					 neighborBuildings.add(p);
					
				 }
				 
			 }
			String query1="SELECT SHAPE FROM buildings where bName in (Select fname from fire_buildings)";
			rs = statement.executeQuery(query1);
			if(rs!=null)
			 {
				 
				 while(rs.next())
				 {	
					 
					 STRUCT st = (oracle.sql.STRUCT) rs.getObject(1);
					 JGeometry j_geom = JGeometry.load(st);
					 double[] ordinates= j_geom.getOrdinatesArray();
					 
					 int[] x= new int[ordinates.length/2];
					 int[] y= new int[ordinates.length/2];
					 for(int i=0,j=0;i<ordinates.length;i=i+2,j++)
					 {
						
						 x[j]= (int)ordinates[i];
						 y[j]= (int)ordinates[i+1];
					 }
					 Polygon p= new Polygon(x, y, x.length);
					 fireneighborBuildings.add(p);
					
				 }
				 
			 }
			String textQuery="Query3:"+query1+"\n"+query;
			textAreaQuery.setText(textQuery);
			imgLabel.repaint();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	public void userFireBuilding(Connection conn)
	{
		try{
			eventValue="UserFireBuilding";
			Statement statement = conn.createStatement();
			ResultSet rs;
			String query="SELECT b.shape as bshape FROM buildings b WHERE SDO_ANYINTERACT(b.shape,sdo_geometry(2001,null,mdsys.sdo_point_type("+currentxPoint+","+currentyPoint+",null),null,null)) = 'TRUE'";
			//System.out.println(query);
			textAreaQuery.setText(query);
			rs = statement.executeQuery(query);
			if(rs!=null)
			 {
				 
				 while(rs.next())
				 {	
					 STRUCT st = (oracle.sql.STRUCT) rs.getObject(1);
					 JGeometry j_geom = JGeometry.load(st);
					 double[] ordinates= j_geom.getOrdinatesArray();
					 
					 int[] x= new int[ordinates.length/2];
					 int[] y= new int[ordinates.length/2];
					 for(int i=0,j=0;i<ordinates.length;i=i+2,j++)
					 {
						
						 x[j]= (int)ordinates[i];
						 y[j]= (int)ordinates[i+1];
					 }
					 Polygon p= new Polygon(x, y, x.length);
					 userfireBuildings.add(p);
				 }
			 }
				 
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	public void closestHydrant(Connection conn)
	{
		try{
			eventValue="ClosestHydrantQuery";
			Statement statement = conn.createStatement();
			ResultSet rs;
			String newQuery="";
			String query="select shape from hydrants where hID in (select h.hID from hydrants h,(SELECT b.shape as bshape FROM buildings b WHERE SDO_ANYINTERACT(b.shape,sdo_geometry(2001,null,mdsys.sdo_point_type(";
			String endpart= ",null),null,null)) = 'TRUE') b2 where SDO_NN(h.SHAPE, b2.bshape,'SDO_NUM_RES=1')='TRUE')";
			textAreaQuery.setText("Query4: "+query+"X"+","+"Y"+endpart);
			for(Point p: userFirePoint)
			{
				newQuery= query+p.getX()+","+p.getY()+endpart;
				//System.out.println(newQuery);
				rs = statement.executeQuery(newQuery);
				if(rs!=null)
				 {
					 
					 while(rs.next())
					 {	
						 
						 STRUCT st = (oracle.sql.STRUCT) rs.getObject(1);
						 JGeometry j_geom = JGeometry.load(st);
						 double[] ordinates= j_geom.getOrdinatesArray();
						 
						 int[] x= new int[ordinates.length/2];
						 int[] y= new int[ordinates.length/2];
						 for(int i=0,j=0;i<ordinates.length;i=i+2,j++)
						 {
							
							 x[j]= (int)ordinates[i];
							 y[j]= (int)ordinates[i+1];
							 Point p1= new Point(x[j], y[j]);
							 closestHydrants.add(p1);
						 }
						
						
					 }
					 
				 }
			}

			imgLabel.repaint();
			userFirePoint.clear();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	public static int[] buildIntArray(ArrayList<Integer> integers) {
	    int[] ints = new int[integers.size()];
	    int i = 0;
	    for (Integer n : integers) {
	        ints[i++] = n;
	    }
	    return ints;
	}

	class ImageLabel extends JLabel{
		public ImageLabel(String string) {
			super(string);
		}

		@Override
		protected void paintComponent(Graphics g) {
			// TODO Auto-generated method stub
			super.paintComponent(g);
			//System.out.println("inside paint");
			if(eventValue.equalsIgnoreCase("WholeRange"))
			{
				g.setColor(Color.YELLOW);
				//g.drawLine(100, 100, 150, 150);
				for(Polygon p: buildings)
				{
					g.drawPolygon(p);
				}
				g.setColor(Color.RED);
				for(Polygon p: firebuildings)
				{
					g.drawPolygon(p);
				}
				g.setColor(Color.GREEN);
				for(Point p: hydrants)
				{
					g.fillRect(p.x,p. y, 15, 15);
				}
			}
			if(eventValue.equalsIgnoreCase("Range"))
			{
				g.setColor(Color.RED);
				//System.out.println(userPoint.size());
				 for(int i=0,j=0;i<userPoint.size();i=i+1,j++)
				 {
					 int x1,x2,y1,y2;
					 if(i==userPoint.size()-1)
					 {
						  x1 = userPoint.get(i).x;
						  y1 = userPoint.get(i).y;
						  x2 = userPoint.get(0).x;
						  y2 = userPoint.get(0).y;
					 }
					 else{
						  x1 = userPoint.get(i).x;
						  y1 = userPoint.get(i).y;
						  x2 = userPoint.get(i+1).x;
						  y2 = userPoint.get(i+1).y;
					 }
					
					 //System.out.println("x1:"+x1+"y1:"+y1+"x2:"+x2+"y2:"+y2);
					 g.drawLine(x1, y1, x2, y2);
				 }
				 
			}
			if(eventValue.equalsIgnoreCase("Point"))
			{
				g.setColor(Color.RED);
				 for(int i=0;i<userPoint.size();i++)
				 {
					 int x1 = userPoint.get(i).x;
					 int y1 = userPoint.get(i).y;
					 //System.out.println("x:"+x1+"y:"+y1);
					 g.fillRect(x1, y1, 5, 5);
					 if(i>0)
					 {
						 int x2 = userPoint.get(i-1).x;
						 int y2 = userPoint.get(i-1).y;
						 g.drawLine(x1, y1, x2, y2);
					 }
					 
				 }
			}
			if(eventValue.equalsIgnoreCase("RangeQuery"))
			{
				g.setColor(Color.RED);
				//System.out.println(userPoint.size());
				for(int i=0,j=0;i<userPoint.size();i=i+1,j++)
				 {
					 int x1,x2,y1,y2;
					 if(i==userPoint.size()-1)
					 {
						  x1 = userPoint.get(i).x;
						  y1 = userPoint.get(i).y;
						  x2 = userPoint.get(0).x;
						  y2 = userPoint.get(0).y;
					 }
					 else{
						  x1 = userPoint.get(i).x;
						  y1 = userPoint.get(i).y;
						  x2 = userPoint.get(i+1).x;
						  y2 = userPoint.get(i+1).y;
					 }
					
					 //System.out.println("x1:"+x1+"y1:"+y1+"x2:"+x2+"y2:"+y2);
					 g.drawLine(x1, y1, x2, y2);
				 }
				//g.drawLine(userPoint.get(userPoint.size()-1).x,userPoint.get(userPoint.size()-1).y,userPoint.get(0).x,userPoint.get(0).y);
				g.setColor(Color.YELLOW);
				//g.drawLine(100, 100, 150, 150);
				for(Polygon p: Rangebuildings)
				{
					g.drawPolygon(p);
				}
				g.setColor(Color.RED);
				//g.drawLine(100, 100, 150, 150);
				for(Polygon p: RangeFirebuildings)
				{
					g.drawPolygon(p);
				}
				g.setColor(Color.GREEN);
				for(Point p: Rangehydrants)
					{
						g.fillRect(p.x,p. y, 15,15);
					}
			}
			if(eventValue.equalsIgnoreCase("NeighborQuery"))
			{
				g.setColor(Color.RED);
				//g.drawLine(100, 100, 150, 150);
				for(Polygon p: fireneighborBuildings)
				{
					g.drawPolygon(p);
				}
				g.setColor(Color.YELLOW);
				//g.drawLine(100, 100, 150, 150);
				for(Polygon p: neighborBuildings)
				{
					g.drawPolygon(p);
				}
			}
			if(eventValue.equalsIgnoreCase("UserFireBuilding"))
			{
				g.setColor(Color.RED);
				for(Polygon p: userfireBuildings)
				{
					g.drawPolygon(p);
				}
			}
			if(eventValue.equalsIgnoreCase("ClosestHydrantQuery"))
			{
				g.setColor(Color.RED);
				for(Polygon p: userfireBuildings)
				{
					g.drawPolygon(p);
				}
				g.setColor(Color.GREEN);
				for(Point p: closestHydrants)
					{
						g.fillRect(p.x,p. y, 15, 15);
					}
				userfireBuildings.clear();
				closestHydrants.clear();
			}
			
		}	
	}
}
