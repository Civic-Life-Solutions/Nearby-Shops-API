package org.nearbyshops.BackupsDAO;

import com.zaxxer.hikari.HikariDataSource;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.JDBCContract;
import org.nearbyshops.Model.Cart;
import org.nearbyshops.Model.CartItem;

import java.sql.*;
import java.util.ArrayList;


public class CartItemService {

	private HikariDataSource dataSource = Globals.getDataSource();

	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();	
	}
	
	
	
	public int saveCartItem(CartItem cartItem)
	{	
		
		Connection connection = null;
		PreparedStatement statement = null;
		int rowCount = -1;

		String insertEndUser = "INSERT INTO "
				+ CartItem.TABLE_NAME
				+ "("  
				+ CartItem.CART_ID + ","
				+ CartItem.ITEM_ID + ","
				+ CartItem.ITEM_QUANTITY + ""
				+ ") VALUES(?,?,?)";

		/*+ "" + cartItem.getCartID() + ","
				+ "" + cartItem.getItemID() + ","
				+ "" + cartItem.getItemQuantity()*/

		try {
			
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(insertEndUser,Statement.RETURN_GENERATED_KEYS);

			statement.setObject(1,cartItem.getCartID());
			statement.setObject(2,cartItem.getItemID());
			statement.setObject(3,cartItem.getItemQuantity());


			rowCount = statement.executeUpdate();
			
			ResultSet rs = statement.getGeneratedKeys();

			//if(rs.next())
			//{
			//	rowCount = rs.getInt(1);
			//}
			

			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			
			try {
			
				if(statement!=null)
				{statement.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				
				if(connection!=null)
				{connection.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		return rowCount;
	}
	

	public int updateCartItem(CartItem cartItem)
	{	
		String updateStatement = "UPDATE "
				+ CartItem.TABLE_NAME
				+ " SET "
				+ CartItem.ITEM_ID + " = " + cartItem.getItemID() + ","
				+ CartItem.CART_ID + " = " + cartItem.getCartID() + ","
				+ CartItem.ITEM_QUANTITY + " = " + cartItem.getItemQuantity()
				+ " WHERE " + CartItem.CART_ID + " = "
				+ cartItem.getCartID() + " AND "
				+ CartItem.ITEM_ID + " = "
				+ cartItem.getItemID();


		Connection conn = null;
		Statement stmt = null;
		int updatedRows = -1;
		
		try {
			
			conn = DriverManager.getConnection(JDBCContract.CURRENT_CONNECTION_URL,
					JDBCContract.CURRENT_USERNAME,JDBCContract.CURRENT_PASSWORD);
			
			stmt = conn.createStatement();
			
			updatedRows = stmt.executeUpdate(updateStatement);
			
			
			System.out.println("Total rows updated CartItem : " + updatedRows);
			
			//conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		
		{
			
			try {
			
				if(stmt!=null)
				{stmt.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				
				if(conn!=null)
				{conn.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return updatedRows;
		
	}
	

	public int deleteCartItem(int itemID,int cartID)
	{

		String deleteStatement = "DELETE FROM " + CartItem.TABLE_NAME;


		boolean isFirst = true;

		if(itemID > 0)
		{
			deleteStatement = deleteStatement + " WHERE " + CartItem.ITEM_ID + " = " + itemID;
			isFirst = false;
		}

		if(cartID > 0)
		{
			if(isFirst)
			{
				deleteStatement = deleteStatement + " WHERE " + CartItem.CART_ID + " = " + cartID;
			}else
			{
				deleteStatement = deleteStatement + " AND " + CartItem.CART_ID + " = " + cartID;
			}

		}




		Connection conn= null;
		Statement stmt = null;
		int rowsCountDeleted = 0;
		try {
			
			conn = DriverManager.getConnection(JDBCContract.CURRENT_CONNECTION_URL,
					JDBCContract.CURRENT_USERNAME,JDBCContract.CURRENT_PASSWORD);
			
			stmt = conn.createStatement();
			
			rowsCountDeleted = stmt.executeUpdate(deleteStatement);
			
			System.out.println(" Deleted Count CartItem : " + rowsCountDeleted);
			
			conn.close();	
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally
		
		{
			
			try {
			
				if(stmt!=null)
				{stmt.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				
				if(conn!=null)
				{conn.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		
		return rowsCountDeleted;
	}
	
	
	
	
	
	public ArrayList<CartItem> getCartItem(Integer cartID, Integer itemID, Integer endUserID)
	{
		String query = "SELECT * FROM " + CartItem.TABLE_NAME + "," + Cart.TABLE_NAME
				+ " WHERE " + CartItem.TABLE_NAME + "."+ CartItem.CART_ID  + " = "
				+ Cart.TABLE_NAME + "." + Cart.CART_ID ;



		if(endUserID != null)
		{
			query = query + " AND " + Cart.END_USER_ID + " = " + endUserID;
		}



		ArrayList<CartItem> cartItemList = new ArrayList<CartItem>();

		//boolean isFirst = true;

		if(cartID != null)
		{
			query = query + " AND " + CartItem.TABLE_NAME + "." + CartItem.CART_ID + " = " + cartID;

		//	isFirst = false;
		}


		if(itemID != null)
		{

			query = query + " AND " + CartItem.ITEM_ID + " = " + itemID;

			/*
			if(isFirst)
			{
				query = query + " AND " + CartItem.ITEM_ID + " = " + itemID;

				isFirst = false;

			}else
			{

			}*/

		}

		
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		
		try {
			
			connection = DriverManager.getConnection(JDBCContract.CURRENT_CONNECTION_URL
					,JDBCContract.CURRENT_USERNAME
					, JDBCContract.CURRENT_PASSWORD);
			
			statement = connection.createStatement();
			
			rs = statement.executeQuery(query);
			
			while(rs.next())
			{
				CartItem cartItem = new CartItem();

				cartItem.setCartID(rs.getInt(CartItem.CART_ID));
				cartItem.setItemID(rs.getInt(CartItem.ITEM_ID));
				cartItem.setItemQuantity(rs.getInt(CartItem.ITEM_QUANTITY));

				cartItemList.add(cartItem);
			}
			

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		finally
		{
			
			try {
					if(rs!=null)
					{rs.close();}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			try {
			
				if(statement!=null)
				{statement.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				
				if(connection!=null)
				{connection.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return cartItemList;
	}



	public ArrayList<CartItem> getCartItem(Integer endUserID,Integer shopID)
	{


		String query = "SELECT cart.cart_id,cart_item.item_id," +
						" available_item_quantity, item_price, item_quantity, " +
						" (item_quantity * item_price) as Item_total" +
						" FROM " +
						" shop_item, cart_item, cart " +
						" Where " +
						"shop_item.shop_id = cart.shop_id " +
						" and " +
						" shop_item.item_id = cart_item.item_id " +
						" and " +
						" cart.cart_id = cart_item.cart_id " ;



		if(endUserID!=null)
		{
			query = query + " and end_user_id = " + endUserID;
		}


		if(shopID !=null)
		{
			query = query + " and cart.shop_id = " + shopID;
		}



		ArrayList<CartItem> cartItemList = new ArrayList<CartItem>();

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {

			conn = DriverManager.getConnection(JDBCContract.CURRENT_CONNECTION_URL
					,JDBCContract.CURRENT_USERNAME
					, JDBCContract.CURRENT_PASSWORD);

			stmt = conn.createStatement();

			rs = stmt.executeQuery(query);

			while(rs.next())
			{
				CartItem cartItem = new CartItem();

				cartItem.setCartID(rs.getInt(CartItem.CART_ID));
				cartItem.setItemID(rs.getInt(CartItem.ITEM_ID));
				cartItem.setItemQuantity(rs.getInt(CartItem.ITEM_QUANTITY));
				cartItem.setRt_availableItemQuantity(rs.getInt("available_item_quantity"));
				cartItem.setRt_itemPrice(rs.getDouble("item_price"));


				//cartItem.setItem(Globals.itemService.getItem(cartItem.getItemID()));

				cartItemList.add(cartItem);
			}



		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		finally
		{

			try {
				if(rs!=null)
				{rs.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {

				if(stmt!=null)
				{stmt.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {

				if(conn!=null)
				{conn.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		return cartItemList;
	}

}
