package com.restapi.controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.stereotype.Component;

import com.mysql.jdbc.PreparedStatement;
import com.restapi.model.*;

@Component
public class PizzaDeliveryService {

	public static Connection connection = null;

	public PizzaDeliveryService() {
		// public static void main(String args[]){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
			return;
		}

		System.out.println("MySQL JDBC Driver Registered!");

		try {
			connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/PizzaBytes", "root", "root");

		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}

		if (connection != null) {
			System.out.println("You made it, take control your database now!");
			//List<Order> list = userOrders("mary@gmail.com");

		} else {
			System.out.println("Failed to make connection!");
		}
	}

	/*Get List of Default Pizzas */
	public List<Pizza> getDefaultPizza(){

		PreparedStatement preparedStmt = null;
		List<Pizza> pizzaList = new ArrayList<Pizza>();
		List<Topping> topList;
		HashMap<Integer,Pizza> mapList = new HashMap<Integer,Pizza>();

		ResultSet rs = null;
		try {

			String query= "select p.PizzaId, p.PizzaName, p.SauceId, p.SizeId, s.SauceName, sz.SizeName,sz.PriceFactor, t.ToppingId, t.ToppingName from Pizza p, Sauce s, Size sz, PizzaTopping pt, Topping t where p.SauceId=s.SauceId and p.SizeId=sz.SizeId and p.PizzaId=pt.PId and pt.TId=t.ToppingId and p.IsDefault='Y';";
			preparedStmt=(PreparedStatement) connection.prepareStatement(query);
			rs=preparedStmt.executeQuery();

			while(rs.next())
			{
				if(mapList.containsKey(rs.getInt("PizzaId"))){
					Pizza pz = mapList.get(rs.getInt("PizzaId"));
					Topping t = new Topping();
					t.setToppingId(rs.getInt("ToppingId"));
					t.setToppingName(rs.getString("ToppingName"));
					pz.getToppingList().add(t);

				}else{
					Pizza p=new Pizza();
					Sauce s = new Sauce();
					Size sz = new Size();
					p.setPizzaId(rs.getInt("PizzaId"));
					p.setPizzaName(rs.getString("PizzaName"));
					p.setIsDefault("Y");
					s.setSauceId(rs.getInt("SauceId"));
					s.setSauceName(rs.getString("SauceName"));
					p.setSauce(s);
					sz.setSizeId(rs.getInt("SizeId"));
					sz.setSizeName(rs.getString("SizeName"));
					sz.setPriceFactor(rs.getInt("PriceFactor"));
					p.setSize(sz);
					Topping t = new Topping();
					t.setToppingId(rs.getInt("ToppingId"));
					t.setToppingName(rs.getString("ToppingName"));
					topList=new ArrayList<Topping>();
					topList.add(t);
					p.setToppingList(topList);
					mapList.put(p.getPizzaId(), p);
				}
			}

			for(int k: mapList.keySet()){
				pizzaList.add(mapList.get(k));
			}

		} catch (SQLException e) {
			System.out.println("SQl exception  "+e.getMessage());
		} finally {
			if (preparedStmt != null) {
				try {
					preparedStmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return pizzaList;
	}

	/*Get List of Toppings */
	public List<Topping> getToppings(){

		PreparedStatement preparedStmt = null;
		List<Topping> toppingList=new ArrayList<Topping>();

		ResultSet rs = null;
		try {

			String query= "select * from Topping t;";
			preparedStmt=(PreparedStatement) connection.prepareStatement(query);
			rs=preparedStmt.executeQuery();

			while(rs.next())
			{
				Topping p=new Topping();
				p.setToppingId(rs.getInt("ToppingId"));
				p.setToppingName(rs.getString("ToppingName"));
				p.setPrice(rs.getInt("ToppingPrice"));
				toppingList.add(p);
			}

		} catch (SQLException e) {
			System.out.println("SQl exception  "+e.getMessage());
		} finally {
			if (preparedStmt != null) {
				try {
					preparedStmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return toppingList;
	}


	/*Get List of Sauce */
	public List<Sauce> getSauce(){

		PreparedStatement preparedStmt = null;
		List<Sauce> sauceList=new ArrayList<Sauce>();

		ResultSet rs = null;
		try {

			String query= "select s.SauceId,s.SauceName from Sauce s;";
			preparedStmt=(PreparedStatement) connection.prepareStatement(query);
			rs=preparedStmt.executeQuery();

			while(rs.next())
			{
				Sauce p=new Sauce();
				p.setSauceId(rs.getInt("SauceId"));
				p.setSauceName(rs.getString("SauceName"));
				sauceList.add(p);
			}

		} catch (SQLException e) {
			System.out.println("SQl exception  "+e.getMessage());
		} finally {
			if (preparedStmt != null) {
				try {
					preparedStmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return sauceList;
	}

	/*Get List of Size */
	public List<Size> getSize(){

		PreparedStatement preparedStmt = null;
		List<Size> sizeList=new ArrayList<Size>();

		ResultSet rs = null;
		try {

			String query= "select * from Size s;";
			preparedStmt=(PreparedStatement) connection.prepareStatement(query);
			rs=preparedStmt.executeQuery();

			while(rs.next())
			{
				Size p=new Size();
				p.setSizeId(rs.getInt("SizeId"));
				p.setSizeName(rs.getString("SizeName"));
				p.setPriceFactor(rs.getInt("PriceFactor"));
				sizeList.add(p);
			}

		} catch (SQLException e) {
			System.out.println("SQl exception  "+e.getMessage());
		} finally {
			if (preparedStmt != null) {
				try {
					preparedStmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return sizeList;
	}


	/*Get User Details */
	public User getUserDetails(String email){

		PreparedStatement preparedStmt = null;
		User user = new User();

		ResultSet rs = null;
		try {

			String query= "select * from User where EmailId=?;";
			preparedStmt=(PreparedStatement) connection.prepareStatement(query);
			preparedStmt.setString(1, email);
			rs=preparedStmt.executeQuery();

			while(rs.next())
			{
				user.setUserId(rs.getInt("UserId"));
				user.setUserName(rs.getString("UserName"));
				user.setEmailAddress(rs.getString("EmailId"));
				user.setPhoneNumber(rs.getString("PhoneNumber"));
				user.setZipCode(rs.getInt("ZipCode"));
				user.setStreet(rs.getString("Street"));
				user.setCity(rs.getString("City"));
				user.setState(rs.getString("State"));
			}
		} catch (SQLException e) {
			System.out.println("SQl exception  "+e.getMessage());
		} finally {
			if (preparedStmt != null) {
				try {
					preparedStmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return user;
	}


	// Create a new User
	public User createUser(User user) {
		PreparedStatement preparedStmt = null;
		int userId = 0;
		String message = "";
		String pwd = "";
		ResultSet rs = null;
		try {

			preparedStmt = (PreparedStatement) connection.prepareStatement("SELECT * FROM User where EmailId=?");
			preparedStmt.setString(1, user.getEmailAddress());
			rs = preparedStmt.executeQuery();
			/*
			 * rs.next(); userId= rs.getInt(1);
			 */
			if (rs.next()) {
				
				User user1 = new User();
				return user1;
			} else {

				preparedStmt = (PreparedStatement) connection.prepareStatement("SELECT max(userId) FROM User");
				rs = preparedStmt.executeQuery();
				rs.next();
				userId = rs.getInt(1) + 1;

				preparedStmt = (PreparedStatement) connection
						.prepareStatement("insert into User values(?,?,?,?,?,?,?,?,?)");

				preparedStmt.setInt(1, userId);
				preparedStmt.setString(2, user.getUserName());
				pwd = encryptPassword(user.getPassword());
				preparedStmt.setString(3, pwd);
				preparedStmt.setString(4, user.getPhoneNumber());
				preparedStmt.setString(5, user.getEmailAddress());
				preparedStmt.setInt(6, user.getZipCode());
				preparedStmt.setString(7, user.getStreet());
				preparedStmt.setString(8, user.getState());
				preparedStmt.setString(9, user.getCity());

				int i = preparedStmt.executeUpdate();
				if (i == 1) {
					message = "User " + userId + "--" + user.getUserName() + " created";
					user.setPassword("");
					return user;
				} else {
					return null;
				}

			}
		} catch (SQLException e) {
			System.out.println("SQl exception  " + e.getMessage());
		} finally {
			if (preparedStmt != null) {
				try {
					preparedStmt.close();
				} catch (SQLException e) {
				}
			}
		}

		System.out.println(message);
		return user;
	}

	private String encryptPassword(String password) {
		// TODO Auto-generated method stub
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword("jasypt");
		String encryptedText = encryptor.encrypt(password);
		return encryptedText;
		//System.out.println(encryptedText);
	}

	private String dencryptPassword(String password) {
		// TODO Auto-generated method stub
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword("jasypt");
		String encryptedText = encryptor.decrypt(password);
		return encryptedText;
		//System.out.println(encryptedText);
	}

	public User authenticateUser(String email,String password){

		PreparedStatement preparedStmt = null;
		User user1= new User();
		User user = new User();
		ResultSet rs = null;
		try {
			String query= "select * from User where EmailId=?;";
			preparedStmt=(PreparedStatement) connection.prepareStatement(query);
			preparedStmt.setString(1, email);
			rs=preparedStmt.executeQuery();
			if(rs.next()){
					String authenticate =dencryptPassword(rs.getString("Password"));
					if(authenticate.equals(password)){
						user.setUserId(rs.getInt("UserId"));
						user.setUserName(rs.getString("UserName"));
						user.setEmailAddress(rs.getString("EmailId"));
						user.setPhoneNumber(rs.getString("PhoneNumber"));
						user.setZipCode(rs.getInt("ZipCode"));
						user.setStreet(rs.getString("Street"));
						user.setCity(rs.getString("City"));
						user.setState(rs.getString("State"));
					}
					else{
						return user1;

					}


			}else{
				return user1;
			}

		}catch (SQLException e) {
			System.out.println("SQl exception  "+e.getMessage());
		} finally {
			if (preparedStmt != null) {
				try {
					preparedStmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return user;
	}


	public static List<Order> userOrders(String email){

		PreparedStatement preparedStmt = null;
		HashMap<Integer, Order> mapList = new HashMap<Integer, Order>();
		List<Order> orderList = new ArrayList<Order>();
		List<Topping> topList ;
		List<Pizza> pizzaList ;

		ResultSet rs = null;
		try {
			String query= "select o.OrderId,o.OrderTime,o.DeliveredTime,p.PizzaId, p.PizzaName, p.SauceId, p.SizeId, s.SauceName, sz.SizeName,sz.PriceFactor, t.ToppingId, t.ToppingName"
					+" from Pizza p, Sauce s, Size sz, PizzaTopping pt, Topping t, PizzaBytes.Order o, PizzaBytes.User u, PizzaOrder po"
					+" where p.SauceId=s.SauceId and p.SizeId=sz.SizeId and p.PizzaId=pt.PId and pt.TId=t.ToppingId and o.OrderId=po.OId and po.PId=p.PizzaId"
					+" and u.EmailId=? and u.UserId=o.UId;";
			preparedStmt=(PreparedStatement) connection.prepareStatement(query);
			preparedStmt.setString(1, email);
			rs=preparedStmt.executeQuery();
			Boolean flag=true;
			while(rs.next()){
				if(mapList.containsKey(rs.getInt("OrderId"))){
					Order o = new Order();
					o = mapList.get(rs.getInt("OrderId"));
					pizzaList= new ArrayList<Pizza>();
					List<Pizza> tempList= new ArrayList<Pizza>();
					tempList = o.getPizzaList();
					for(Pizza pz: tempList){
						if(pz.getPizzaId()== rs.getInt("PizzaId")){
							Topping t = new Topping();
							t.setToppingId(rs.getInt("ToppingId"));
							t.setToppingName(rs.getString("ToppingName"));
							pz.getToppingList().add(t);
							flag=true;
						}else{
							flag=false;
						}

					}
					if(!flag){
						Pizza p=new Pizza();
						Sauce s = new Sauce();
						Size sz = new Size();
						p.setPizzaId(rs.getInt("PizzaId"));
						p.setPizzaName(rs.getString("PizzaName"));
						s.setSauceId(rs.getInt("SauceId"));
						s.setSauceName(rs.getString("SauceName"));
						p.setSauce(s);
						sz.setSizeId(rs.getInt("SizeId"));
						sz.setSizeName(rs.getString("SizeName"));
						sz.setPriceFactor(rs.getInt("PriceFactor"));
						p.setSize(sz);
						Topping t = new Topping();
						t.setToppingId(rs.getInt("ToppingId"));
						t.setToppingName(rs.getString("ToppingName"));
						topList=new ArrayList<Topping>();
						topList.add(t);
						p.setToppingList(topList);
						o.getPizzaList().add(p);

					}

				}else{
					Order o = new Order();
					o.setOrderId(rs.getInt("OrderId"));
					o.setOrderTime(rs.getTimestamp("OrderTime"));
					o.setOrderTimeString(rs.getTimestamp("OrderTime"));
					o.setDeliveryTime(rs.getTimestamp("DeliveredTime"));
					o.setDeliveryTimeString(rs.getTimestamp("DeliveredTime"));
					Pizza p=new Pizza();
					Sauce s = new Sauce();
					Size sz = new Size();
					p.setPizzaId(rs.getInt("PizzaId"));
					p.setPizzaName(rs.getString("PizzaName"));
					s.setSauceId(rs.getInt("SauceId"));
					s.setSauceName(rs.getString("SauceName"));
					p.setSauce(s);
					sz.setSizeId(rs.getInt("SizeId"));
					sz.setSizeName(rs.getString("SizeName"));
					p.setSize(sz);
					Topping t = new Topping();
					t.setToppingId(rs.getInt("ToppingId"));
					t.setToppingName(rs.getString("ToppingName"));
					topList=new ArrayList<Topping>();
					topList.add(t);
					p.setToppingList(topList);
					pizzaList= new ArrayList<Pizza>();
					pizzaList.add(p);
					o.setPizzaList(pizzaList);
					mapList.put(o.getOrderId(), o);
				}
			}

			for(int k: mapList.keySet()){
				orderList.add(mapList.get(k));
			}


		}catch (SQLException e) {
			System.out.println("SQl exception  "+e.getMessage());
		} finally {
			if (preparedStmt != null) {
				try {
					preparedStmt.close();
				} catch (SQLException e) {
				}
			}

		}

		return orderList;
	}


	//Place Order for default Pizza
	public void placeOrderForDefault(int pizzaId,int quantity,int orderId,int cost){

		PreparedStatement preparedStmt = null;


		ResultSet rs = null;
		try {
			/*preparedStmt=(PreparedStatement) connection.prepareStatement("SELECT max(OrderId) FROM pizzabytes.order");
			rs=preparedStmt.executeQuery();
			rs.next();
			orderId= rs.getInt(1)+1;*/


			/*preparedStmt=(PreparedStatement) connection.prepareStatement("SELECT u.UserId FROM pizzabytes.user u where u.EmailId=?");
			preparedStmt.setString(1,email);
			rs=preparedStmt.executeQuery();
			rs.next();
			userId= rs.getInt(1);*/


			/*preparedStmt=(PreparedStatement) connection.prepareStatement("insert into pizzabytes.order values(?,?,?,?,?,?)");

			preparedStmt.setInt(1,orderId);
			preparedStmt.setTimestamp(2,ordertime);
			preparedStmt.setTimestamp(3,delivertime);
			preparedStmt.setString(4,address);
			preparedStmt.setInt(5,userId);
			preparedStmt.setInt(6,0);
			preparedStmt.executeUpdate();*/

			preparedStmt=(PreparedStatement) connection.prepareStatement("insert into PizzaBytes.PizzaOrder values(?,?,?,?)");
			preparedStmt.setInt(1,pizzaId);
			preparedStmt.setInt(2,orderId);
			preparedStmt.setInt(3,quantity);
			preparedStmt.setInt(4,cost);
			preparedStmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("SQl exception  "+e.getMessage());
		} finally {
			if (preparedStmt != null) {
				try {
					preparedStmt.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	//Place Order for Usermade Pizza
	public void placeOrder(int quantity,int orderId,int cost,int sauceId,int sizeId,List<Topping> toppings){

		/*Calendar calendar = Calendar.getInstance();
		Date now = (Date) calendar.getTime();
		Timestamp ordertime = new Timestamp(now.getTime());

		calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.MINUTE, 30);
		Date nowN = (Date) calendar.getTime();
		Timestamp delivertime = new Timestamp(nowN.getTime());
*/
		PreparedStatement preparedStmt1 = null;
		PreparedStatement preparedStmt = null;
		int pizzaId=0;

		ResultSet rs = null;
		try {
			/*preparedStmt=(PreparedStatement) connection.prepareStatement("SELECT max(OrderId) FROM pizzabytes.order");
			rs=preparedStmt.executeQuery();
			rs.next();
			orderId= rs.getInt(1)+1;*/

			/*preparedStmt=(PreparedStatement) connection.prepareStatement("SELECT u.UserId FROM pizzabytes.user u where u.EmailId=?");
			preparedStmt.setString(1,email);
			rs=preparedStmt.executeQuery();
			rs.next();
			userId= rs.getInt(1);*/


			/*preparedStmt=(PreparedStatement) connection.prepareStatement("insert into pizzabytes.order values(?,?,?,?,?,?)");

			preparedStmt.setInt(1,orderId);
			preparedStmt.setTimestamp(2,ordertime);
			preparedStmt.setTimestamp(3,delivertime);
			preparedStmt.setString(4,address);
			preparedStmt.setInt(5,userId);
			preparedStmt.setInt(6,0);
			preparedStmt.executeUpdate();*/

			preparedStmt1=(PreparedStatement) connection.prepareStatement("SELECT max(PizzaId) FROM Pizza");
			rs=preparedStmt1.executeQuery();
			rs.next();
			pizzaId= rs.getInt(1)+1;
			System.out.println(pizzaId);

			preparedStmt1=(PreparedStatement) connection.prepareStatement("insert into Pizza values(?,?,?,?,?)");
			preparedStmt1.setInt(1,pizzaId);
			preparedStmt1.setString(2,"UserMade");
			preparedStmt1.setInt(3,sauceId);
			preparedStmt1.setInt(4,sizeId);
			preparedStmt1.setString(5,"N");
			preparedStmt1.executeUpdate();
			preparedStmt1.close();


			preparedStmt=(PreparedStatement) connection.prepareStatement("insert into PizzaOrder values(?,?,?,?)");
			preparedStmt.setInt(1,pizzaId);
			preparedStmt.setInt(2,orderId);
			preparedStmt.setInt(3,quantity);
			preparedStmt.setInt(4,cost);
			preparedStmt.executeUpdate();

			

			for(Topping top:toppings){
				preparedStmt=(PreparedStatement) connection.prepareStatement("insert into PizzaTopping values(?,?)");
				preparedStmt.setInt(1,pizzaId);
				preparedStmt.setInt(2,top.getToppingId());
				preparedStmt.executeUpdate();
			}



		} catch (SQLException e) {
			System.out.println("SQl exception  "+e.getMessage());
		} finally {
			if (preparedStmt != null) {
				try {
					preparedStmt.close();
				} catch (SQLException e) {
				}
			}
		}
	}


	public int createOrder(String address,int userId){
		Calendar calendar = Calendar.getInstance();
		java.sql.Date now = new java.sql.Date(calendar.getTime().getTime());
		Timestamp ordertime = new Timestamp(now.getTime());

		calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.MINUTE, 30);
		java.sql.Date nowN = new java.sql.Date(calendar.getTime().getTime());
		Timestamp delivertime = new Timestamp(nowN.getTime());


		PreparedStatement preparedStmt = null;
		int orderId=0;

		ResultSet rs = null;
		try {
			preparedStmt=(PreparedStatement) connection.prepareStatement("SELECT max(OrderId) FROM PizzaBytes.Order");
			rs=preparedStmt.executeQuery();
			rs.next();
			orderId= rs.getInt(1)+1;


			preparedStmt=(PreparedStatement) connection.prepareStatement("insert into PizzaBytes.Order values(?,?,?,?,?,?)");

			preparedStmt.setInt(1,orderId);
			preparedStmt.setTimestamp(2,ordertime);
			preparedStmt.setTimestamp(3,delivertime);
			preparedStmt.setString(4,address);
			preparedStmt.setInt(5,userId);
			preparedStmt.setInt(6,0);
			preparedStmt.executeUpdate();


		} catch (SQLException e) {
			System.out.println("SQl exception  "+e.getMessage());
		} finally {
			if (preparedStmt != null) {
				try {
					preparedStmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return orderId;
	}

}

