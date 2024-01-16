/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DB;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author User
 */
public class DB {
      	public static Connection getConnection(){
		Connection con=null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			   con= DriverManager.getConnection("jdbc:mysql://localhost:3306/srr?zeroDateTimeBehavior=convertToNull","root","");
		}catch(Exception e){System.out.println(e);}
		return con;
	}
}
