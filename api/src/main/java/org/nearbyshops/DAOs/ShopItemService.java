package org.nearbyshops.DAOs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.nearbyshops.ContractClasses.*;
import org.nearbyshops.Model.ShopItem;
import org.nearbyshops.ModelEndPoints.ShopItemEndPoint;
import org.nearbyshops.Utility.GeoLocation;

public class ShopItemService {

	public int insertShopItem(ShopItem shopItem)
	{
		
		Connection conn = null;
		Statement stmt = null;
		int rowCount = 0;

		//+ "" + shopItem.getQuantityUnit() + ","
		//+ "" + shopItem.getQuantityMultiple() + ","

		String insertShop = "INSERT INTO "
				+ ShopItemContract.TABLE_NAME				
				+ "("  
				+ ShopItemContract.SHOP_ID + ","
				+ ShopItemContract.ITEM_PRICE + ","
				+ ShopItemContract.ITEM_ID + ","
				+ ShopItemContract.AVAILABLE_ITEM_QUANTITY + ","

				+ ShopItemContract.EXTRA_DELIVERY_CHARGE + ","
				+ ShopItemContract.LAST_UPDATE_DATE_TIME
				+ " ) VALUES ("
				+ "" + shopItem.getShopID() + ","
				+ "" + shopItem.getItemPrice() + ","
				+ "" + shopItem.getItemID() + ","
				+ "" + shopItem.getAvailableItemQuantity() + ","

				+ "" + shopItem.getExtraDeliveryCharge() + ","
				+ "" + "now()" + ""
				+ ")";
		
		try {
			
			conn = DriverManager.getConnection(JDBCContract.CURRENT_CONNECTION_URL,JDBCContract.CURRENT_USERNAME,JDBCContract.CURRENT_PASSWORD);
			
			stmt = conn.createStatement();
			
			rowCount = stmt.executeUpdate(insertShop,Statement.RETURN_GENERATED_KEYS);
			
			
			System.out.println("Key autogenerated SaveShop: Rows Inserted = " + rowCount);
			
			
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




	public int updateAvailableItemQuantity(int orderID)
	{



		String updateQuantity =

			" Update shop_item SET available_item_quantity = available_item_quantity - item_quantity from order_item,customer_order " +
			" where order_item.item_id = shop_item.item_id " +
			" and customer_order.order_id = order_item.order_id " +
			" and shop_item.shop_id = customer_order.shop_id " +
			" and customer_order.order_id = " + orderID;



		Connection conn = null;
		Statement stmt = null;
		int updatedRows = -1;

		try {

			conn = DriverManager.getConnection(JDBCContract.CURRENT_CONNECTION_URL,
					JDBCContract.CURRENT_USERNAME,
					JDBCContract.CURRENT_PASSWORD);

			stmt = conn.createStatement();

			updatedRows = stmt.executeUpdate(updateQuantity);


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
	
	
	public int updateShopItem(ShopItem shopItem)
	{
		/*

				+ ShopItemContract.QUANTITY_MULTIPLE + " ="
				+ "" + shopItem.getQuantityMultiple() + ","
				+ ShopItemContract.QUANTITY_UNIT + " ="
				+ "'" + shopItem.getQuantityUnit() + "',"
		 */

		String updateStatement = "UPDATE " + ShopItemContract.TABLE_NAME 
				+ " SET "
				+ ShopItemContract.AVAILABLE_ITEM_QUANTITY + " = " + "" + shopItem.getAvailableItemQuantity() + ","
				+ ShopItemContract.ITEM_ID + " =" + "" + shopItem.getItemID() + ","
				+ ShopItemContract.ITEM_PRICE + " =" + "" + shopItem.getItemPrice() + ","
				+ ShopItemContract.SHOP_ID + " =" + "" + shopItem.getShopID() + ","

				+ ShopItemContract.EXTRA_DELIVERY_CHARGE + " = " + "" + shopItem.getExtraDeliveryCharge() + ","
				+ ShopItemContract.LAST_UPDATE_DATE_TIME + " = " + "" + "now()" + ""
				+ " WHERE "
				+ ShopItemContract.SHOP_ID + " = " + shopItem.getShopID()
				+ " AND "
				+ ShopItemContract.ITEM_ID + " = " + shopItem.getItemID();
		
		System.out.println("Query:" + updateStatement);
		
		
		Connection conn = null;
		Statement stmt = null;
		int updatedRows = 0;
		
		try {
			
			conn = DriverManager.getConnection(JDBCContract.CURRENT_CONNECTION_URL,
					JDBCContract.CURRENT_USERNAME,
					JDBCContract.CURRENT_PASSWORD);
			
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
	
	
	
	
	public int deleteShopItem(int shopID,int itemID)
	{
		String deleteStatement = "DELETE FROM " + ShopItemContract.TABLE_NAME 
				+ " WHERE " + ShopItemContract.SHOP_ID + " = " + shopID
				+ " AND " + ShopItemContract.ITEM_ID + " = " + itemID;
		
		
		Connection conn= null;
		Statement stmt = null;
		int rowsCountDeleted = 0;
		try {
			
			conn = DriverManager.getConnection(JDBCContract.CURRENT_CONNECTION_URL,JDBCContract.CURRENT_USERNAME,JDBCContract.CURRENT_PASSWORD);
			
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



	GeoLocation center;

	GeoLocation[] minMaxArray;
	GeoLocation pointOne;
	GeoLocation pointTwo;



public ArrayList<ShopItem> getShopItems(
											Integer itemCategoryID,
											Integer shopID, Integer itemID,
											Double latCenter, Double lonCenter,
											Double deliveryRangeMin, Double deliveryRangeMax,
											Double proximity,
											Integer endUserID, Boolean isFilledCart,
											Boolean isOutOfStock, Boolean priceEqualsZero,
											String sortBy,
											Integer limit, Integer offset

)
{

		String query = "";

		// set this flag to false after setting the first query
		boolean isFirst = true;
		
		
		
		String queryNormal = "SELECT * FROM " + ShopItemContract.TABLE_NAME;


	
	
		String queryJoin = "SELECT DISTINCT "
				+ "6371 * acos(cos( radians("
				+ latCenter + ")) * cos( radians( lat_center) ) * cos(radians( lon_center ) - radians("
				+ lonCenter + "))"
				+ " + sin( radians(" + latCenter + ")) * sin(radians(lat_center))) as distance" + ","
				+ ShopItemContract.TABLE_NAME + "." + ShopItemContract.ITEM_ID + ","
				+ ShopItemContract.TABLE_NAME + "." + ShopItemContract.SHOP_ID + ","
				+ ShopItemContract.TABLE_NAME + "." + ShopItemContract.ITEM_PRICE + ","
				+ ShopItemContract.TABLE_NAME + "." + ShopItemContract.AVAILABLE_ITEM_QUANTITY + ","
				+ ShopItemContract.TABLE_NAME + "." + ShopItemContract.EXTRA_DELIVERY_CHARGE + ","
				+ ShopItemContract.TABLE_NAME + "." + ShopItemContract.DATE_TIME_ADDED + ","
				+ ShopItemContract.TABLE_NAME + "." + ShopItemContract.LAST_UPDATE_DATE_TIME + ""

				+ " FROM "
				+ ShopContract.TABLE_NAME  + "," + ShopItemContract.TABLE_NAME + ","
				+ ItemContract.TABLE_NAME + "," + ItemCategoryContract.TABLE_NAME
				+ " WHERE "
				+ ShopContract.TABLE_NAME + "." + ShopContract.SHOP_ID
				+ "="
				+ ShopItemContract.TABLE_NAME + "." + ShopItemContract.SHOP_ID
				+ " AND "
				+ ShopItemContract.TABLE_NAME + "." + ShopItemContract.ITEM_ID
				+ "="
				+ ItemContract.TABLE_NAME + "." + ItemContract.ITEM_ID
				+ " AND "
				+ ItemContract.TABLE_NAME + "." + ItemContract.ITEM_CATEGORY_ID
				+ "="
				+ ItemCategoryContract.TABLE_NAME + "." + ItemCategoryContract.ITEM_CATEGORY_ID;



	if(endUserID!=null)
	{

		if(isFilledCart!=null)
		{
			if(isFilledCart)
			{
				queryJoin = queryJoin + " AND "
						+ ShopItemContract.TABLE_NAME
						+ "."
						+ ShopItemContract.SHOP_ID + " IN "
						+ " (SELECT " + CartContract.SHOP_ID + " FROM " + CartContract.TABLE_NAME + " WHERE "
						+ CartContract.END_USER_ID + " = " + endUserID + ")";
			}else
			{
				queryJoin = queryJoin + " AND "
						+ ShopItemContract.TABLE_NAME
						+ "."
						+ ShopItemContract.SHOP_ID + " NOT IN "
						+ " (SELECT " + CartContract.SHOP_ID + " FROM " + CartContract.TABLE_NAME + " WHERE "
						+ CartContract.END_USER_ID + " = " + endUserID + ")";

			}

		}
	}



	if(shopID !=null)
	{
			queryJoin = queryJoin + " AND "
					+ ShopItemContract.TABLE_NAME 
					+ "."
					+ ShopItemContract.SHOP_ID + " = " + shopID;


			queryNormal = queryNormal + " WHERE "
						+ ShopItemContract.SHOP_ID + " = " + shopID;

			isFirst = false;
		
	}
	
	
	if(itemID !=null)
	{	
	
		queryJoin = queryJoin + " AND "
					+ ShopItemContract.TABLE_NAME
					+ "."
					+ ShopItemContract.ITEM_ID + " = " + itemID;


		if(isFirst)
		{
			queryNormal = queryNormal + " WHERE "
					+ ShopItemContract.ITEM_ID + " = " + itemID;

			isFirst = false;

		}else
		{
			queryNormal = queryNormal + " AND "
					+ ShopItemContract.ITEM_ID + " = " + itemID;
		}

	}


	if(itemCategoryID !=null)
	{

		queryJoin = queryJoin + " AND "
				+ ItemCategoryContract.TABLE_NAME
				+ "."
				+ ItemCategoryContract.ITEM_CATEGORY_ID + " = " + itemCategoryID;


		if(isFirst)
		{
			queryNormal = queryNormal + " WHERE "
					+ ItemCategoryContract.ITEM_CATEGORY_ID + " = " + itemCategoryID;

			isFirst = false;

		}else
		{
			queryNormal = queryNormal + " AND "
					+ ItemCategoryContract.ITEM_CATEGORY_ID + " = " + itemCategoryID;
		}

	}


	if(priceEqualsZero!=null)
	{
		if(priceEqualsZero)
		{
			queryJoin = queryJoin + " AND "
					+ ShopItemContract.TABLE_NAME  + "." + ShopItemContract.ITEM_PRICE + " = " + 0;

			if(isFirst)
			{
				queryNormal = queryNormal + " WHERE "
						+ ShopItemContract.ITEM_PRICE + " = " + 0;

				isFirst = false;

			}else
			{
				queryNormal = queryNormal + " AND "
						+ ShopItemContract.ITEM_PRICE + " = " + 0;

			}

		}

	}


	if(isOutOfStock!=null)
	{
		if(isOutOfStock)
		{
			queryJoin = queryJoin + " AND "
					+ ShopItemContract.TABLE_NAME  + "." + ShopItemContract.AVAILABLE_ITEM_QUANTITY + " = " + 0;

			if(isFirst)
			{
				queryNormal = queryNormal + " WHERE "
						+ ShopItemContract.AVAILABLE_ITEM_QUANTITY + " = " + 0;

				isFirst = false;

			}else
			{
				queryNormal = queryNormal + " AND "
						+ ShopItemContract.AVAILABLE_ITEM_QUANTITY + " = " + 0;

			}

		}

	}


	/*

	if(itemCategoryID > 0)
	{

			queryJoin = queryJoin + " AND "
					+ ItemCategoryContract.TABLE_NAME
					+ "."
					+ ItemCategoryContract.ITEM_CATEGORY_ID + " = " + itemCategoryID;

	}

	*/



	/*
			Applying Filters
	 */



	if(latCenter != null && lonCenter != null)
	{
		// Applying shop visibility filter. Gives all the shops which are visible at the given location defined by
		// latCenter and lonCenter. For more information see the API documentation.


		String queryPartLatLonCenterTwo = "";

		queryPartLatLonCenterTwo = queryPartLatLonCenterTwo
				+ " AND "
				+ ShopContract.TABLE_NAME
				+ "."
				+ ShopContract.LAT_MAX
				+ " >= " + latCenter
				+ " AND "
				+ ShopContract.TABLE_NAME
				+ "."
				+ ShopContract.LAT_MIN
				+ " <= " + latCenter
				+ " AND "
				+ ShopContract.TABLE_NAME
				+ "."
				+ ShopContract.LON_MAX
				+ " >= " + lonCenter
				+ " AND "
				+ ShopContract.TABLE_NAME
				+ "."
				+ ShopContract.LON_MIN
				+ " <= " + lonCenter;

		//+ " BETWEEN " + latmax + " AND " + latmin;

		String queryPartlatLonCenter = "";

		queryPartlatLonCenter = queryPartlatLonCenter + " 6371.01 * acos( cos( radians("
				+ latCenter + ")) * cos( radians( lat_center) ) * cos(radians( lon_center ) - radians("
				+ lonCenter + "))"
				+ " + sin( radians(" + latCenter + ")) * sin(radians(lat_center))) <= delivery_range ";



		if(isFirst)
		{
			queryNormal = queryNormal + " WHERE ";

			// reset the flag
			isFirst = false;

		}else
		{
			queryNormal = queryNormal + " AND ";
		}


//		queryNormal = queryNormal + queryPartlatLonCenter;

		queryJoin = queryJoin + " AND " + queryPartlatLonCenter;
	}



	if(deliveryRangeMin !=null && deliveryRangeMax!=null){

		// apply delivery range filter

		// apply delivery range filter
		String queryPartDeliveryRange = "";

		queryPartDeliveryRange = queryPartDeliveryRange
				+ ShopContract.TABLE_NAME
				+ "."
				+ ShopContract.DELIVERY_RANGE
				+ " BETWEEN " + deliveryRangeMin + " AND " + deliveryRangeMax;
				//+ " <= " + deliveryRange;



		if(isFirst)
		{
			queryNormal = queryNormal + " WHERE ";

			// reset the flag
			isFirst = false;

		}else
		{
			queryNormal = queryNormal + " AND ";
		}


//		queryNormal = queryNormal + queryPartDeliveryRange;

		System.out.println("Delivery Range Min : "  + deliveryRangeMin + " Max : " + deliveryRangeMax);

		queryJoin = queryJoin + " AND " + queryPartDeliveryRange;

	}


	// proximity cannot be greater than the delivery range if the delivery range is supplied. Otherwise this condition is
	// not required.
	if(proximity !=null)
	{
		// generate bounding coordinates for the shop based on the required location and its

		center = GeoLocation.fromDegrees(latCenter,lonCenter);
		minMaxArray = center.boundingCoordinates(proximity,6371.01);

		pointOne = minMaxArray[0];
		pointTwo = minMaxArray[1];

		double latMin = pointOne.getLatitudeInDegrees();
		double lonMin = pointOne.getLongitudeInDegrees();
		double latMax = pointTwo.getLatitudeInDegrees();
		double lonMax = pointTwo.getLongitudeInDegrees();


		// Make sure that shop center lies between the bounding coordinates generated by proximity bounding box

		String queryPartProximity = "";
		String queryPartProximityTwo = "";


		queryPartProximityTwo = queryPartProximityTwo

				+ " AND "
				+ ShopContract.TABLE_NAME
				+ "."
				+ ShopContract.LAT_CENTER
				+ " < " + latMax

				+ " AND "
				+ ShopContract.TABLE_NAME
				+ "."
				+ ShopContract.LAT_CENTER
				+ " > " + latMin

				+ " AND "
				+ ShopContract.TABLE_NAME
				+ "."
				+ ShopContract.LON_CENTER
				+ " < " + lonMax

				+ " AND "
				+ ShopContract.TABLE_NAME
				+ "."
				+ ShopContract.LON_CENTER
				+ " > " + lonMin;


		// filter using Haversine formula using SQL math functions
		queryPartProximity = queryPartProximity
				+ " (6371.01 * acos(cos( radians("
				+ latCenter
				+ ")) * cos( radians("
				+ ShopContract.LAT_CENTER
				+ " )) * cos(radians( "
				+ ShopContract.LON_CENTER
				+ ") - radians("
				+ lonCenter
				+ "))"
				+ " + sin( radians("
				+ latCenter
				+ ")) * sin(radians("
				+ ShopContract.LAT_CENTER
				+ ")))) <= "
				+ proximity ;


		if(isFirst)
		{
			queryNormal = queryNormal + " WHERE ";

			// reset the flag
			isFirst = false;

		}else
		{
			queryNormal = queryNormal + " AND ";
		}



//		queryNormal = queryNormal + queryPartProximity;

		queryJoin = queryJoin + " AND " + queryPartProximity;

	}




	if(sortBy!=null)
	{
		if(!sortBy.equals(""))
		{
			String queryPartSortBy = " ORDER BY " + sortBy;

			queryNormal = queryNormal + queryPartSortBy;
			queryJoin = queryJoin + queryPartSortBy;
		}
	}



	if(limit !=null)
	{

		String queryPartLimitOffset = "";

		if(offset !=null)
		{
			queryPartLimitOffset = " LIMIT " + limit + " " + " OFFSET " + offset;

		}else
		{
			queryPartLimitOffset = " LIMIT " + limit + " " + " OFFSET " + 0;
		}

		queryNormal = queryNormal + queryPartLimitOffset;
		queryJoin = queryJoin + queryPartLimitOffset;
	}



	/*
			Applying Filters Ends
	 */


	if(itemCategoryID!=null || (latCenter==null && lonCenter ==null))
	{

		query = queryJoin;
		System.out.println("Query Join : "  + queryJoin);

	} else
	{

		query = queryNormal;
	}


		
		
		
		ArrayList<ShopItem> shopItemList = new ArrayList<ShopItem>();
		
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {


			conn = DriverManager.getConnection(
					JDBCContract.CURRENT_CONNECTION_URL,
					JDBCContract.CURRENT_USERNAME,
					JDBCContract.CURRENT_PASSWORD
			);
			
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(query);
			
			while(rs.next())
			{
				
				ShopItem shopItem = new ShopItem();
				shopItem.setShopID(rs.getInt(ShopItemContract.SHOP_ID));
				shopItem.setItemID(rs.getInt(ShopItemContract.ITEM_ID));
				shopItem.setAvailableItemQuantity(rs.getInt(ShopItemContract.AVAILABLE_ITEM_QUANTITY));
				shopItem.setItemPrice(rs.getDouble(ShopItemContract.ITEM_PRICE));

				shopItem.setDateTimeAdded(rs.getTimestamp(ShopItemContract.DATE_TIME_ADDED));
				shopItem.setLastUpdateDateTime(rs.getTimestamp(ShopItemContract.LAST_UPDATE_DATE_TIME));
				shopItem.setExtraDeliveryCharge(rs.getInt(ShopItemContract.EXTRA_DELIVERY_CHARGE));
				
				shopItemList.add(shopItem);
				
			}
			
			System.out.println("Total ShopItems queried = " + shopItemList.size());	
			
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
		
								
		return shopItemList;

	}




	public ShopItemEndPoint getEndpointMetadata(
			Integer itemCategoryID,
			Integer shopID, Integer itemID,
			Double latCenter, Double lonCenter,
			Double deliveryRangeMin, Double deliveryRangeMax,
			Double proximity,
			Integer endUserID, Boolean isFilledCart,
			Boolean isOutOfStock, Boolean priceEqualsZero

	)
	{


		String query = "";

		// set this flag to false after setting the first query
		boolean isFirst = true;



		String queryNormal = "SELECT " +
								"count(*) as item_count" +
								" FROM " + ShopItemContract.TABLE_NAME;




		String queryJoin = "SELECT DISTINCT "

				+ "count(*) as item_count"

				+ " FROM "
				+ ShopContract.TABLE_NAME  + "," + ShopItemContract.TABLE_NAME + ","
				+ ItemContract.TABLE_NAME + "," + ItemCategoryContract.TABLE_NAME

				+ " WHERE "
				+ ShopContract.TABLE_NAME + "." + ShopContract.SHOP_ID
				+ "="
				+ ShopItemContract.TABLE_NAME + "." + ShopItemContract.SHOP_ID
				+ " AND "
				+ ShopItemContract.TABLE_NAME + "." + ShopItemContract.ITEM_ID
				+ "="
				+ ItemContract.TABLE_NAME + "." + ItemContract.ITEM_ID
				+ " AND "
				+ ItemContract.TABLE_NAME + "." + ItemContract.ITEM_CATEGORY_ID
				+ "="
				+ ItemCategoryContract.TABLE_NAME + "." + ItemCategoryContract.ITEM_CATEGORY_ID;



		if(endUserID!=null)
		{

			if(isFilledCart!=null)
			{
				if(isFilledCart)
				{
					queryJoin = queryJoin + " AND "
							+ ShopItemContract.TABLE_NAME
							+ "."
							+ ShopItemContract.SHOP_ID + " IN "
							+ " (SELECT " + CartContract.SHOP_ID + " FROM " + CartContract.TABLE_NAME + " WHERE "
							+ CartContract.END_USER_ID + " = " + endUserID + ")";
				}else
				{
					queryJoin = queryJoin + " AND "
							+ ShopItemContract.TABLE_NAME
							+ "."
							+ ShopItemContract.SHOP_ID + " NOT IN "
							+ " (SELECT " + CartContract.SHOP_ID + " FROM " + CartContract.TABLE_NAME + " WHERE "
							+ CartContract.END_USER_ID + " = " + endUserID + ")";

				}

			}
		}



		if(shopID !=null)
		{
			queryJoin = queryJoin + " AND "
					+ ShopItemContract.TABLE_NAME
					+ "."
					+ ShopItemContract.SHOP_ID + " = " + shopID;


			queryNormal = queryNormal + " WHERE "
					+ ShopItemContract.SHOP_ID + " = " + shopID;

			isFirst = false;

		}


		if(itemID !=null)
		{

			queryJoin = queryJoin + " AND "
					+ ShopItemContract.TABLE_NAME
					+ "."
					+ ShopItemContract.ITEM_ID + " = " + itemID;


			if(isFirst)
			{
				queryNormal = queryNormal + " WHERE "
						+ ShopItemContract.ITEM_ID + " = " + itemID;

				isFirst = false;

			}else
			{
				queryNormal = queryNormal + " AND "
						+ ShopItemContract.ITEM_ID + " = " + itemID;
			}

		}


		if(itemCategoryID !=null)
		{

			queryJoin = queryJoin + " AND "
					+ ItemCategoryContract.TABLE_NAME
					+ "."
					+ ItemCategoryContract.ITEM_CATEGORY_ID + " = " + itemCategoryID;


			if(isFirst)
			{
				queryNormal = queryNormal + " WHERE "
						+ ItemCategoryContract.ITEM_CATEGORY_ID + " = " + itemCategoryID;

				isFirst = false;

			}else
			{
				queryNormal = queryNormal + " AND "
						+ ItemCategoryContract.ITEM_CATEGORY_ID + " = " + itemCategoryID;
			}

		}


		if(priceEqualsZero!=null)
		{
			if(priceEqualsZero)
			{
				queryJoin = queryJoin + " AND "
						+ ShopItemContract.TABLE_NAME  + "." + ShopItemContract.ITEM_PRICE + " = " + 0;

				if(isFirst)
				{
					queryNormal = queryNormal + " WHERE "
							+ ShopItemContract.ITEM_PRICE + " = " + 0;

					isFirst = false;

				}else
				{
					queryNormal = queryNormal + " AND "
							+ ShopItemContract.ITEM_PRICE + " = " + 0;

				}

			}

		}


		if(isOutOfStock!=null)
		{
			if(isOutOfStock)
			{
				queryJoin = queryJoin + " AND "
						+ ShopItemContract.TABLE_NAME  + "." + ShopItemContract.AVAILABLE_ITEM_QUANTITY + " = " + 0;

				if(isFirst)
				{
					queryNormal = queryNormal + " WHERE "
							+ ShopItemContract.AVAILABLE_ITEM_QUANTITY + " = " + 0;

					isFirst = false;

				}else
				{
					queryNormal = queryNormal + " AND "
							+ ShopItemContract.AVAILABLE_ITEM_QUANTITY + " = " + 0;

				}

			}

		}


	/*

	if(itemCategoryID > 0)
	{

			queryJoin = queryJoin + " AND "
					+ ItemCategoryContract.TABLE_NAME
					+ "."
					+ ItemCategoryContract.ITEM_CATEGORY_ID + " = " + itemCategoryID;

	}

	*/



	/*
			Applying Filters
	 */



		// apply visibility filter
		if(latCenter != null && lonCenter != null)
		{
			// Applying shop visibility filter. Gives all the shops which are visible at the given location defined by
			// latCenter and lonCenter. For more information see the API documentation.

			String queryPartlatLonCenter = "";

			queryPartlatLonCenter = queryPartlatLonCenter + " 6371.01 * acos( cos( radians("
					+ latCenter + ")) * cos( radians( lat_center) ) * cos(radians( lon_center ) - radians("
					+ lonCenter + "))"
					+ " + sin( radians(" + latCenter + ")) * sin(radians(lat_center))) <= delivery_range ";



			if(isFirst)
			{
				queryNormal = queryNormal + " WHERE ";

				// reset the flag
				isFirst = false;

			}else
			{
				queryNormal = queryNormal + " AND ";
			}


//		queryNormal = queryNormal + queryPartlatLonCenter;

			queryJoin = queryJoin + " AND " + queryPartlatLonCenter;
		}



		// apply delivery range filter
		if(deliveryRangeMin !=null && deliveryRangeMax!=null){

			// apply delivery range filter

			// apply delivery range filter
			String queryPartDeliveryRange = "";

			queryPartDeliveryRange = queryPartDeliveryRange
					+ ShopContract.TABLE_NAME
					+ "."
					+ ShopContract.DELIVERY_RANGE
					+ " BETWEEN " + deliveryRangeMin + " AND " + deliveryRangeMax;
			//+ " <= " + deliveryRange;



			if(isFirst)
			{
				queryNormal = queryNormal + " WHERE ";

				// reset the flag
				isFirst = false;

			}else
			{
				queryNormal = queryNormal + " AND ";
			}


//		queryNormal = queryNormal + queryPartDeliveryRange;

			System.out.println("Delivery Range Min : "  + deliveryRangeMin + " Max : " + deliveryRangeMax);

			queryJoin = queryJoin + " AND " + queryPartDeliveryRange;

		}


		// apply proximity filter
		if(proximity !=null)
		{

			// proximity cannot be greater than the delivery range if the delivery range is supplied. Otherwise this condition is
			// not required.

			// generate bounding coordinates for the shop based on the required location and its

			String queryPartProximity = "";

			// filter using Haversine formula using SQL math functions
			queryPartProximity = queryPartProximity
					+ " (6371.01 * acos(cos( radians("
					+ latCenter
					+ ")) * cos( radians("
					+ ShopContract.LAT_CENTER
					+ " )) * cos(radians( "
					+ ShopContract.LON_CENTER
					+ ") - radians("
					+ lonCenter
					+ "))"
					+ " + sin( radians("
					+ latCenter
					+ ")) * sin(radians("
					+ ShopContract.LAT_CENTER
					+ ")))) <= "
					+ proximity ;


			if(isFirst)
			{
				queryNormal = queryNormal + " WHERE ";

				// reset the flag
				isFirst = false;

			}else
			{
				queryNormal = queryNormal + " AND ";
			}



//		queryNormal = queryNormal + queryPartProximity;

			queryJoin = queryJoin + " AND " + queryPartProximity;

		}



	/*
			Applying Filters Ends
	 */

/*

		if(latCenter==null || lonCenter ==null)
		{
			query = queryNormal;

		} else
		{

			query = queryJoin;
		}
*/



		if(itemCategoryID!=null || (latCenter==null && lonCenter ==null))
		{

			query = queryJoin;
			System.out.println("Query Join : "  + queryJoin);

		} else
		{

			query = queryNormal;
		}




		ShopItemEndPoint endPoint = new ShopItemEndPoint();


		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {


			conn = DriverManager.getConnection(
					JDBCContract.CURRENT_CONNECTION_URL,
					JDBCContract.CURRENT_USERNAME,
					JDBCContract.CURRENT_PASSWORD
			);

			stmt = conn.createStatement();

			rs = stmt.executeQuery(query);

			while(rs.next())
			{
				endPoint.setItemCount(rs.getInt("item_count"));
			}

			System.out.println("Total ShopItem Count = " + endPoint.getItemCount());

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


		return endPoint;
	}



}
