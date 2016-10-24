package org.nearbyshops.DAOsPrepared;

import com.zaxxer.hikari.HikariDataSource;
import org.nearbyshops.ContractClasses.CartContract;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.Item;
import org.nearbyshops.Model.ItemCategory;
import org.nearbyshops.Model.Shop;
import org.nearbyshops.Model.ShopItem;
import org.nearbyshops.ModelEndPoints.ShopItemEndPoint;

import java.sql.*;
import java.util.ArrayList;

public class ShopItemByShopDAO {


	private HikariDataSource dataSource = Globals.getDataSource();


	public ArrayList<ShopItem> getShopItems(
												Integer itemCategoryID,
												Integer shopID,
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



			String queryNormal = "SELECT * FROM " + ShopItem.TABLE_NAME;




			String queryJoin = "SELECT DISTINCT "
					+ "6371 * acos(cos( radians("
					+ latCenter + ")) * cos( radians( lat_center) ) * cos(radians( lon_center ) - radians("
					+ lonCenter + "))"
					+ " + sin( radians(" + latCenter + ")) * sin(radians(lat_center))) as distance" + ","
					+ ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + ","
					+ ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + ","
					+ ShopItem.TABLE_NAME + "." + ShopItem.ITEM_PRICE + ","
					+ ShopItem.TABLE_NAME + "." + ShopItem.AVAILABLE_ITEM_QUANTITY + ","
					+ ShopItem.TABLE_NAME + "." + ShopItem.EXTRA_DELIVERY_CHARGE + ","
					+ ShopItem.TABLE_NAME + "." + ShopItem.DATE_TIME_ADDED + ","
					+ ShopItem.TABLE_NAME + "." + ShopItem.LAST_UPDATE_DATE_TIME + ","

					+ Item.TABLE_NAME + "." + Item.ITEM_CATEGORY_ID + ","
					+ Item.TABLE_NAME + "." + Item.ITEM_IMAGE_URL + ","

					+ Item.TABLE_NAME + "." + Item.ITEM_NAME + ","
					+ Item.TABLE_NAME + "." + Item.ITEM_DESC + ","

					+ Item.TABLE_NAME + "." + Item.QUANTITY_UNIT + ","
					+ Item.TABLE_NAME + "." + Item.DATE_TIME_CREATED + ","
					+ Item.TABLE_NAME + "." + Item.ITEM_DESCRIPTION_LONG + ""

					+ " FROM "
					+ Shop.TABLE_NAME  + "," + ShopItem.TABLE_NAME + ","
					+ Item.TABLE_NAME + "," + ItemCategory.TABLE_NAME
					+ " WHERE "
					+ Shop.TABLE_NAME + "." + Shop.SHOP_ID
					+ "="
					+ ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID
					+ " AND "
					+ ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID
					+ "="
					+ Item.TABLE_NAME + "." + Item.ITEM_ID
					+ " AND "
					+ Item.TABLE_NAME + "." + Item.ITEM_CATEGORY_ID
					+ "="
					+ ItemCategory.TABLE_NAME + "." + ItemCategory.ITEM_CATEGORY_ID;



		if(endUserID!=null)
		{

			if(isFilledCart!=null)
			{
				if(isFilledCart)
				{
					queryJoin = queryJoin + " AND "
							+ ShopItem.TABLE_NAME
							+ "."
							+ ShopItem.SHOP_ID + " IN "
							+ " (SELECT " + CartContract.SHOP_ID + " FROM " + CartContract.TABLE_NAME + " WHERE "
							+ CartContract.END_USER_ID + " = " + endUserID + ")";
				}else
				{
					queryJoin = queryJoin + " AND "
							+ ShopItem.TABLE_NAME
							+ "."
							+ ShopItem.SHOP_ID + " NOT IN "
							+ " (SELECT " + CartContract.SHOP_ID + " FROM " + CartContract.TABLE_NAME + " WHERE "
							+ CartContract.END_USER_ID + " = " + endUserID + ")";

				}

			}
		}



		if(shopID !=null)
		{
				queryJoin = queryJoin + " AND "
						+ ShopItem.TABLE_NAME
						+ "."
						+ ShopItem.SHOP_ID + " = " + shopID;


				queryNormal = queryNormal + " WHERE "
							+ ShopItem.SHOP_ID + " = " + shopID;

				isFirst = false;

		}


		if(itemCategoryID !=null)
		{

			queryJoin = queryJoin + " AND "
					+ ItemCategory.TABLE_NAME
					+ "."
					+ ItemCategory.ITEM_CATEGORY_ID + " = " + itemCategoryID;


			if(isFirst)
			{
				queryNormal = queryNormal + " WHERE "
						+ ItemCategory.ITEM_CATEGORY_ID + " = " + itemCategoryID;

				isFirst = false;

			}else
			{
				queryNormal = queryNormal + " AND "
						+ ItemCategory.ITEM_CATEGORY_ID + " = " + itemCategoryID;
			}

		}


		if(priceEqualsZero!=null)
		{
			if(priceEqualsZero)
			{
				queryJoin = queryJoin + " AND "
						+ ShopItem.TABLE_NAME  + "." + ShopItem.ITEM_PRICE + " = " + 0;

				if(isFirst)
				{
					queryNormal = queryNormal + " WHERE "
							+ ShopItem.ITEM_PRICE + " = " + 0;

					isFirst = false;

				}else
				{
					queryNormal = queryNormal + " AND "
							+ ShopItem.ITEM_PRICE + " = " + 0;

				}

			}

		}


		if(isOutOfStock!=null)
		{
			if(isOutOfStock)
			{
				queryJoin = queryJoin + " AND "
						+ ShopItem.TABLE_NAME  + "." + ShopItem.AVAILABLE_ITEM_QUANTITY + " = " + 0;

				if(isFirst)
				{
					queryNormal = queryNormal + " WHERE "
							+ ShopItem.AVAILABLE_ITEM_QUANTITY + " = " + 0;

					isFirst = false;

				}else
				{
					queryNormal = queryNormal + " AND "
							+ ShopItem.AVAILABLE_ITEM_QUANTITY + " = " + 0;

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
					+ Shop.TABLE_NAME
					+ "."
					+ Shop.DELIVERY_RANGE
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



			// Make sure that shop center lies between the bounding coordinates generated by proximity bounding box

			String queryPartProximity = "";
			String queryPartProximityTwo = "";




			// filter using Haversine formula using SQL math functions
			queryPartProximity = queryPartProximity
					+ " (6371.01 * acos(cos( radians("
					+ latCenter
					+ ")) * cos( radians("
					+ Shop.LAT_CENTER
					+ " )) * cos(radians( "
					+ Shop.LON_CENTER
					+ ") - radians("
					+ lonCenter
					+ "))"
					+ " + sin( radians("
					+ latCenter
					+ ")) * sin(radians("
					+ Shop.LAT_CENTER
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


		if(shopID!=null || itemCategoryID!=null || (latCenter!=null && lonCenter!=null))
		{

			query = queryJoin;

	//		System.out.println("Query Join : "  + queryJoin);

		} else
		{

			query = queryNormal;
		}





			ArrayList<ShopItem> shopItemList = new ArrayList<ShopItem>();


			Connection connection = null;
			Statement statement = null;
			ResultSet rs = null;

			try {


				connection = dataSource.getConnection();
				statement = connection.createStatement();

				rs = statement.executeQuery(query);

				while(rs.next())
				{

					ShopItem shopItem = new ShopItem();
					shopItem.setShopID(rs.getInt(ShopItem.SHOP_ID));
					shopItem.setItemID(rs.getInt(ShopItem.ITEM_ID));
					shopItem.setAvailableItemQuantity(rs.getInt(ShopItem.AVAILABLE_ITEM_QUANTITY));
					shopItem.setItemPrice(rs.getDouble(ShopItem.ITEM_PRICE));

					shopItem.setDateTimeAdded(rs.getTimestamp(ShopItem.DATE_TIME_ADDED));
					shopItem.setLastUpdateDateTime(rs.getTimestamp(ShopItem.LAST_UPDATE_DATE_TIME));
					shopItem.setExtraDeliveryCharge(rs.getInt(ShopItem.EXTRA_DELIVERY_CHARGE));

					Item item = new Item();

					item.setItemID(rs.getInt(Item.ITEM_ID));
					item.setItemName(rs.getString(Item.ITEM_NAME));
					item.setItemDescription(rs.getString(Item.ITEM_DESC));

					item.setItemImageURL(rs.getString(Item.ITEM_IMAGE_URL));
					item.setItemCategoryID(rs.getInt(Item.ITEM_CATEGORY_ID));

					item.setItemDescriptionLong(rs.getString(Item.ITEM_DESCRIPTION_LONG));
					item.setDateTimeCreated(rs.getTimestamp(Item.DATE_TIME_CREATED));
					item.setQuantityUnit(rs.getString(Item.QUANTITY_UNIT));


					shopItem.setItem(item);
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


			return shopItemList;

		}




	public ShopItemEndPoint getEndpointMetadata(
			Integer itemCategoryID,
			Integer shopID,
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
								" FROM " + ShopItem.TABLE_NAME;




		String queryJoin = "SELECT DISTINCT "

				+ "count(*) as item_count"

				+ " FROM "
				+ Shop.TABLE_NAME  + "," + ShopItem.TABLE_NAME + ","
				+ Item.TABLE_NAME + "," + ItemCategory.TABLE_NAME

				+ " WHERE "
				+ Shop.TABLE_NAME + "." + Shop.SHOP_ID
				+ "="
				+ ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID
				+ " AND "
				+ ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID
				+ "="
				+ Item.TABLE_NAME + "." + Item.ITEM_ID
				+ " AND "
				+ Item.TABLE_NAME + "." + Item.ITEM_CATEGORY_ID
				+ "="
				+ ItemCategory.TABLE_NAME + "." + ItemCategory.ITEM_CATEGORY_ID;



		if(endUserID!=null)
		{

			if(isFilledCart!=null)
			{
				if(isFilledCart)
				{
					queryJoin = queryJoin + " AND "
							+ ShopItem.TABLE_NAME
							+ "."
							+ ShopItem.SHOP_ID + " IN "
							+ " (SELECT " + CartContract.SHOP_ID + " FROM " + CartContract.TABLE_NAME + " WHERE "
							+ CartContract.END_USER_ID + " = " + endUserID + ")";
				}else
				{
					queryJoin = queryJoin + " AND "
							+ ShopItem.TABLE_NAME
							+ "."
							+ ShopItem.SHOP_ID + " NOT IN "
							+ " (SELECT " + CartContract.SHOP_ID + " FROM " + CartContract.TABLE_NAME + " WHERE "
							+ CartContract.END_USER_ID + " = " + endUserID + ")";

				}

			}
		}



		if(shopID !=null)
		{
			queryJoin = queryJoin + " AND "
					+ ShopItem.TABLE_NAME
					+ "."
					+ ShopItem.SHOP_ID + " = " + shopID;


			queryNormal = queryNormal + " WHERE "
					+ ShopItem.SHOP_ID + " = " + shopID;

			isFirst = false;

		}




		if(itemCategoryID !=null)
		{

			queryJoin = queryJoin + " AND "
					+ ItemCategory.TABLE_NAME
					+ "."
					+ ItemCategory.ITEM_CATEGORY_ID + " = " + itemCategoryID;


			if(isFirst)
			{
				queryNormal = queryNormal + " WHERE "
						+ ItemCategory.ITEM_CATEGORY_ID + " = " + itemCategoryID;

				isFirst = false;

			}else
			{
				queryNormal = queryNormal + " AND "
						+ ItemCategory.ITEM_CATEGORY_ID + " = " + itemCategoryID;
			}

		}


		if(priceEqualsZero!=null)
		{
			if(priceEqualsZero)
			{
				queryJoin = queryJoin + " AND "
						+ ShopItem.TABLE_NAME  + "." + ShopItem.ITEM_PRICE + " = " + 0;

				if(isFirst)
				{
					queryNormal = queryNormal + " WHERE "
							+ ShopItem.ITEM_PRICE + " = " + 0;

					isFirst = false;

				}else
				{
					queryNormal = queryNormal + " AND "
							+ ShopItem.ITEM_PRICE + " = " + 0;

				}

			}

		}


		if(isOutOfStock!=null)
		{
			if(isOutOfStock)
			{
				queryJoin = queryJoin + " AND "
						+ ShopItem.TABLE_NAME  + "." + ShopItem.AVAILABLE_ITEM_QUANTITY + " = " + 0;

				if(isFirst)
				{
					queryNormal = queryNormal + " WHERE "
							+ ShopItem.AVAILABLE_ITEM_QUANTITY + " = " + 0;

					isFirst = false;

				}else
				{
					queryNormal = queryNormal + " AND "
							+ ShopItem.AVAILABLE_ITEM_QUANTITY + " = " + 0;

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
					+ Shop.TABLE_NAME
					+ "."
					+ Shop.DELIVERY_RANGE
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
					+ Shop.LAT_CENTER
					+ " )) * cos(radians( "
					+ Shop.LON_CENTER
					+ ") - radians("
					+ lonCenter
					+ "))"
					+ " + sin( radians("
					+ latCenter
					+ ")) * sin(radians("
					+ Shop.LAT_CENTER
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



		if(itemCategoryID!=null || (latCenter!=null && lonCenter!=null))
		{

			query = queryJoin;

//			System.out.println("Query Join : "  + queryJoin);


		} else
		{

			query = queryNormal;
		}




		ShopItemEndPoint endPoint = new ShopItemEndPoint();


		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		try {


			connection = dataSource.getConnection();
			statement = connection.createStatement();

			rs = statement.executeQuery(query);

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


		return endPoint;
	}



}
