
import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.sql.Array;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import oracle.sql.ARRAY;
import oracle.sql.STRUCT;

public class populate {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			
            Connection conn = databaseConnection();
            Statement st = conn.createStatement(); 
            //insertBuildingsData(conn);
			st.executeUpdate("DELETE FROM fire_buildings");
			st.executeUpdate("DELETE FROM buildings");
			st.executeUpdate("DELETE FROM hydrants");
			insertBuildingsData(conn, args[0]);
			insertFireBuildingsdata(conn,args[2]);
			insertHydrantsData(conn,args[1]);
			//readData(conn);
			//conn.commit();
            conn.close();
		}
		catch(SQLException e)
		{
			//System.out.println("File Not Found.");
			e.printStackTrace();
		}

	}

	public static void insertBuildingsData(Connection conn, String arg)
	{
		try{
			String path ="Z:/pallavi/HW3_Data/data/building.xy";
			//System.out.println(System.getProperty("user.dir"));System.getProperty("user.dir")+File.separator+arg
			File text = new File(arg);
            Scanner scnr = new Scanner(text);
            //conn = databaseConnection();
            Statement st = conn.createStatement(); 
            //System.out.println("here");
            while(scnr.hasNextLine())
            {
            	String line = scnr.nextLine();
            	if(!line.equalsIgnoreCase(""))
            	{
            		String opString=line.replaceAll("\\s+", "");
                    String[] dataValues = opString.split(",");
                    String insQuery="INSERT INTO buildings (bID, bName, numVertices,shape) VALUES('"+dataValues[0]+"','"+dataValues[1]+"',"+dataValues[2]+",";
                    insQuery= insQuery+"sdo_geometry (2003, null, null, sdo_elem_info_array (1,1003,1),sdo_ordinate_array (";
                    for(int i=3; i<dataValues.length;i++)
                    {
                    	insQuery=insQuery+"'"+dataValues[i]+"',";
                    	
                    }
                    insQuery = insQuery+"'"+dataValues[3]+"','"+dataValues[4]+"')))";
                    //System.out.println(insQuery);
                    st.executeUpdate(insQuery);
                    //conn.commit();
            	}
            	
            }
            
            scnr.close();
		}
		catch(Exception e)
		{
			System.out.println("File Not Found.");
			e.printStackTrace();
		}
	}
	public static void insertFireBuildingsdata(Connection conn,String arg)
	{
		try{
			String path = System.getProperty("user.dir")+File.separator+arg;
			File text = new File(arg);
            Scanner scnr = new Scanner(text);
            //conn = databaseConnection();
            Statement st = conn.createStatement(); 
            while(scnr.hasNextLine())
            {
            	String line = scnr.nextLine();
            	if(!line.equalsIgnoreCase(""))
            	{
            		String opString=line.replaceAll("\\s+", "");
                    //String[] dataValues = opString.split(",");
                    String insQuery="INSERT INTO fire_buildings (fname) VALUES('"+opString+"')";
                    //System.out.println(insQuery);
                    st.executeUpdate(insQuery);
            	}
            	
            }
            scnr.close();
		}
		catch(Exception e)
		{
			System.out.println("File Not Found.");
			e.printStackTrace();
		}
	}
	public static void insertHydrantsData(Connection conn,String arg)
	{
		try{
			String path = System.getProperty("user.dir")+File.separator+arg;
			File text = new File(arg);
            Scanner scnr = new Scanner(text);
            //conn = databaseConnection();
            Statement st = conn.createStatement(); 
            while(scnr.hasNextLine())
            {
            	String line = scnr.nextLine();
            	if(!line.equalsIgnoreCase(""))
            	{
            		String opString=line.replaceAll("\\s+", "");
                    String[] dataValues = opString.split(",");
                    String insQuery="INSERT INTO hydrants (hID,shape) VALUES('"+dataValues[0]+"',";
                    insQuery= insQuery+"sdo_geometry (2001, null, null, sdo_elem_info_array (1,1,1),sdo_ordinate_array(";
                    for(int i=1; i<dataValues.length;i++)
                    {
                    	if(i==(dataValues.length-1))
                    	{
                    		insQuery=insQuery+dataValues[i];
                    	}
                    	else
                    	{
                    		insQuery=insQuery+dataValues[i]+",";
                    	}
                    	
                    	
                    }
                    insQuery = insQuery+")))";
                    //System.out.println(insQuery);
                    st.executeUpdate(insQuery);
            	}
            	
            }
            scnr.close();
		}
		catch(Exception e)
		{
			System.out.println("File Not Found.");
			e.printStackTrace();
		}
	}
	public static Connection databaseConnection()
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
			System.out.println("Connection successful.");
			return connection;
 
		} catch (SQLException e) {
 
			System.out.println("Connection Failed! Check console for errors.");
			e.printStackTrace();
			return null;
		}
 
	}

}
