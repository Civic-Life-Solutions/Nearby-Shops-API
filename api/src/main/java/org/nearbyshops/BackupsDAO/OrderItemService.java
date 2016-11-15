package org.nearbyshops.BackupsDAO;

import com.zaxxer.hikari.HikariDataSource;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.JDBCContract;
import org.nearbyshops.Model.OrderItem;
import org.nearbyshops.ModelStats.OrderStats;

import java.sql.*;
import java.util.ArrayList;


public class OrderItemService {

	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();	
	}


	private HikariDataSource dataSource = Globals.getDataSource();
	
	
	
	public int saveOrderItem(OrderItem orderItem)
	{	
		
		Connection conn = null;
		Statement stmt = null;
		int rowCount = -1;



		String insertOrderItem = "INSERT INTO "
				+ OrderItem.TABLE_NAME
				+ "("
				+ OrderItem.ORDER_ID + ","
				+ OrderItem.ITEM_ID + ","
				+ OrderItem.ITEM_QUANTITY + ","
				+ OrderItem.ITEM_PRICE_AT_ORDER + ""
				+ ") VALUES("
				+ "" + orderItem.getOrderID() + ","
				+ "" + orderItem.getItemID() + ","
				+ "" + orderItem.getItemQuantity() + ","
				+ "" + orderItem.getItemPriceAtOrder() + ")";



		try {
			
			conn = DriverManager.getConnection(JDBCContract.CURRENT_CONNECTION_URL,
					JDBCContract.CURRENT_USERNAME,JDBCContract.CURRENT_PASSWORD);
			
			stmt = conn.createStatement();
			
			rowCount = stmt.executeUpdate(insertOrderItem,Statement.RETURN_GENERATED_KEYS);
			
			ResultSet rs = stmt.getGeneratedKeys();

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

		
		return rowCount;
	}
	

	public int updateOrderItem(OrderItem orderItem)
	{

		String updateStatement = "UPDATE "
				+ OrderItem.TABLE_NAME
				+ " SET "
				+ OrderItem.ITEM_ID + " = " + orderItem.getItemID() + ","
				+ OrderItem.ORDER_ID + " = " + orderItem.getOrderID() + ","
				+ OrderItem.ITEM_QUANTITY + " = " + orderItem.getItemQuantity() + ","
				+ OrderItem.ITEM_PRICE_AT_ORDER + " = " + orderItem.getItemPriceAtOrder()

				+ " WHERE "
				+ OrderItem.ORDER_ID + " = " + orderItem.getOrderID()
				+ " AND "
				+ OrderItem.ITEM_ID + " = " + orderItem.getItemID();



		Connection conn = null;
		Statement stmt = null;
		int updatedRows = -1;
		
		try {
			
			conn = DriverManager.getConnection(JDBCContract.CURRENT_CONNECTION_URL,
					JDBCContract.CURRENT_USERNAME,JDBCContract.CURRENT_PASSWORD);
			
			stmt = conn.createStatement();
			
			updatedRows = stmt.executeUpdate(updateStatement);
			
			
			System.out.println("Total rows updated: " + updatedRows);	
			
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
	

	public int deleteOrderItem(int itemID,int orderID)
	{

		String deleteStatement = "DELETE FROM " + OrderItem.TABLE_NAME;


		boolean isFirst = true;

		if(itemID > 0)
		{
			deleteStatement = deleteStatement + " WHERE " + OrderItem.ITEM_ID + " = " + itemID;
			isFirst = false;
		}

		if(orderID > 0)
		{
			if(isFirst)
			{
				deleteStatement = deleteStatement + " WHERE " + OrderItem.ORDER_ID + " = " + orderID;
			}else
			{
				deleteStatement = deleteStatement + " AND " + OrderItem.ORDER_ID + " = " + orderID;
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
			
			System.out.println(" Deleted Count: " + rowsCountDeleted);	
			
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
	
	
	
	
	
	public ArrayList<OrderItem> getOrderItem(Integer orderID, Integer itemID)
	{



		String query = "SELECT * FROM " + OrderItem.TABLE_NAME;


		boolean isFirst = true;

		if(orderID != null)
		{
			query = query + " WHERE " + OrderItem.ORDER_ID + " = " + orderID;

			isFirst = false;
		}

		if(itemID != null)
		{
			if(isFirst)
			{
				query = query + " WHERE " + OrderItem.ITEM_ID + " = " + itemID;
			}
			else
			{
				query = query + " AND " + OrderItem.ITEM_ID + " = " + itemID;
			}

		}




		ArrayList<OrderItem> orderItemList = new ArrayList<OrderItem>();


		
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

				OrderItem orderItem = new OrderItem();

				orderItem.setOrderID(rs.getInt(OrderItem.ORDER_ID));
				orderItem.setItemID(rs.getInt(OrderItem.ITEM_ID));
				orderItem.setItemPriceAtOrder(rs.getInt(OrderItem.ITEM_PRICE_AT_ORDER));
				orderItem.setItemQuantity(rs.getInt(OrderItem.ITEM_QUANTITY));

				orderItemList.add(orderItem);

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

		return orderItemList;
	}




	public OrderStats getOrderStats(int orderID)
	{
		String query = "select " +
				"count(item_id) as item_count, " +
				"sum(item_price_at_order * item_quantity) as item_total," +
				" order_id " +
				"from order_item " +
				"where " + "order_id= " + orderID + " group by order_id";





		OrderStats orderStats = null;

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

				orderStats = new OrderStats();

				orderStats.setOrderID(rs.getInt("order_id"));
				orderStats.setItemCount(rs.getInt("item_count"));
				orderStats.setItemTotal(rs.getInt("item_total"));

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



		return orderStats;
	}



}
