	package org.nearbyshops;


import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ServerProperties;


import org.glassfish.jersey.server.ResourceConfig;
import org.nearbyshops.DAOPreparedSettings.SettingsDAOPrepared;
import org.nearbyshops.Globals.GlobalConstants;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.*;
import org.nearbyshops.ModelDelivery.DeliveryAddress;
import org.nearbyshops.ModelRoles.DeliveryGuySelf;
import org.nearbyshops.ModelReviewItem.FavouriteItem;
import org.nearbyshops.ModelReviewItem.ItemReview;
import org.nearbyshops.ModelReviewItem.ItemReviewThanks;
import org.nearbyshops.ModelReviewShop.FavouriteShop;
import org.nearbyshops.ModelReviewShop.ShopReview;
import org.nearbyshops.ModelReviewShop.ShopReviewThanks;
import org.nearbyshops.ModelRoles.*;
import org.nearbyshops.ModelRoles.Deprecated.DistributorStaff;
import org.nearbyshops.ModelSecurity.ForbiddenOperations;
import org.nearbyshops.ModelSettings.ServiceConfiguration;
import org.nearbyshops.ModelSettings.Settings;

import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;

    /**
 * Main class.
 *
 */

public class Main implements ActionListener {
    // Base URI the Grizzly HTTP server will listen on
	
	//"http://localhost:8080/myapp/"
    public static final String BASE_URI = "http://0.0.0.0:5000/api";
    
    private HttpServer server_;
    
    private boolean isServerStart = false;
    
    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in org.sumeet.restsamples.Sample package
        final ResourceConfig rc = new ResourceConfig();
		rc.packages(true,"org.nearbyshops");

		// Now you can expect validation errors to be sent to the client.
    	rc.property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
				// @ValidateOnExecution annotations on subclasses won't cause errors.
		rc.property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true);

		rc.register(GSONJersey.class);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        
    	//server = startServer();
    	Main main = new Main();
    	//main.go();
    	//main.hibernateTest();
    	
    	main.start();
    	//main.isServerStart = true;

    }
    
    public void go()
    {

        JFrame frame = new JFrame();
        
        JButton button = new JButton("Start/Stop Server");
        JButton buttontwo = new JButton("Stop Server");
        
        button.addActionListener(this);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(BorderLayout.NORTH,button);
        frame.getContentPane().add(BorderLayout.SOUTH, buttontwo);
        frame.setSize(300, 300);
        frame.setVisible(true);
    
    }
    

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(isServerStart == true)
        {
        	stopServer();
        }else if(isServerStart == false)
        {
        	start();
        }
	}
	
	
	
	public void start()
	{
		
		server_ = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "\nHit enter to stop it...", BASE_URI));
        //System.in.read();
        isServerStart = true;

		createDB();
        createTables();

	}

	public void stopServer()
	{
		server_.stop();
		isServerStart = false;
	}

	public void createDB()
	{

		Connection conn = null;
		Statement stmt = null;

		try {

			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres"
					, JDBCContract.CURRENT_USERNAME
					, JDBCContract.CURRENT_PASSWORD);

			stmt = conn.createStatement();

			String createDB = "CREATE DATABASE \"NearbyShopsDB\" WITH ENCODING='UTF8' OWNER=postgres CONNECTION LIMIT=-1";

			stmt.executeUpdate(createDB);

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally{


			// close the connection and statement accountApproved

			if(stmt !=null)
			{

				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}


			if(conn!=null)
			{
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}


	}
	
	
	
	private void createTables()
	{
		
		Connection connection = null;
		Statement statement = null;
		
		try {
			
			connection = DriverManager.getConnection(JDBCContract.CURRENT_CONNECTION_URL
					,JDBCContract.CURRENT_USERNAME
					,JDBCContract.CURRENT_PASSWORD);

			statement = connection.createStatement();


			statement.executeUpdate(ItemCategory.createTableItemCategoryPostgres);
			statement.executeUpdate(Item.createTableItemPostgres);

//			statement.executeUpdate(Distributor.createTableDistributorPostgres);

			statement.executeUpdate(DistributorStaff.createTableDistributorStaffPostgres);
			statement.executeUpdate(Shop.createTableShopPostgres);
			statement.executeUpdate(ShopAdmin.createtableShopAdminPostgres);

			statement.executeUpdate(ShopItem.createTableShopItemPostgres);
			statement.executeUpdate(EndUser.createTableEndUserPostgres);

			// Create Table Admin
			statement.executeUpdate(Admin.createTableAdminPostgres);
			statement.executeUpdate(Staff.createTableStaffPostgres);

			statement.executeUpdate(DeliveryAddress.createTableDeliveryAddressPostgres);
			statement.executeUpdate(DeliveryGuySelf.createtableDeliveryGuySelfPostgres);

			statement.executeUpdate(Order.createTableOrderPostgres);
			statement.executeUpdate(OrderItem.createtableOrderItemPostgres);

			statement.executeUpdate(Cart.createTableCartPostgres);
			statement.executeUpdate(CartItem.createtableCartItemPostgres);

			statement.executeUpdate(ServiceConfiguration.createTableServiceConfigurationPostgres);
			statement.executeUpdate(Settings.createTableSettingsPostgres);

			// tables for shop reviews
			statement.executeUpdate(ShopReview.createTableShopReviewPostgres);
			statement.executeUpdate(FavouriteShop.createTableFavouriteBookPostgres);
			statement.executeUpdate(ShopReviewThanks.createTableShopReviewThanksPostgres);

			// tables for Item reviews
			statement.executeUpdate(ItemReview.createTableItemReviewPostgres);
			statement.executeUpdate(FavouriteItem.createTableFavouriteItemPostgres);
			statement.executeUpdate(ItemReviewThanks.createTableItemReviewThanksPostgres);

			// tabled for keeping security records
			statement.executeUpdate(ForbiddenOperations.createTableForbiddenOperationPostgres);

			System.out.println("Tables Created ... !");




			// developers Note: whenever adding a table please check that its dependencies are already created.



			// Insert the default administrator if it does not exit

			if(Globals.adminDAOPrepared.getAdmin().size()==0)
			{
				Admin defaultAdmin = new Admin();

				defaultAdmin.setPassword("password");
				defaultAdmin.setUsername("username");
				defaultAdmin.setAdministratorName("default name");

				Globals.adminDAOPrepared.saveAdmin(defaultAdmin);
			}




			// Insert the root category whose ID is 1

			String insertItemCategory = "";

			// The root ItemCategory has id 1. If the root category does not exist then insert it.
			if(Globals.itemCategoryDAO.getItemCategory(1) == null)
			{

				insertItemCategory = "INSERT INTO "
						+ ItemCategory.TABLE_NAME
						+ "("
						+ ItemCategory.ITEM_CATEGORY_ID + ","
						+ ItemCategory.ITEM_CATEGORY_NAME + ","
						+ ItemCategory.PARENT_CATEGORY_ID + ","
						+ ItemCategory.ITEM_CATEGORY_DESCRIPTION + ","
						+ ItemCategory.IMAGE_PATH + ","
						+ ItemCategory.IS_LEAF_NODE + ") VALUES("
						+ "" + "1"	+ ","
						+ "'" + "ROOT"	+ "',"
						+ "" + "NULL" + ","
						+ "'" + "This is the root Category. Do not modify it." + "',"
						+ "'" + " " + "',"
						+ "'" + "FALSE" + "'"
						+ ")";

				statement.executeUpdate(insertItemCategory);

			}



			// Insert Default Settings
			SettingsDAOPrepared settingsDAO = Globals.settingsDAOPrepared;
			if(settingsDAO.getServiceConfiguration()==null){
				settingsDAO.saveSettings(settingsDAO.getDefaultConfiguration());
			}




			// Insert Default Service Configuration

			String insertServiceConfig = "";

			if(Globals.serviceConfigurationDAO.getServiceConfiguration()==null)
			{

				ServiceConfiguration defaultConfiguration = new ServiceConfiguration();

				defaultConfiguration.setServiceLevel(GlobalConstants.SERVICE_LEVEL_CITY);
				defaultConfiguration.setServiceType(GlobalConstants.SERVICE_TYPE_NONPROFIT);
				defaultConfiguration.setServiceID(1);
				defaultConfiguration.setServiceName("DEFAULT_CONFIGURATION");

				Globals.serviceConfigurationDAO.saveService(defaultConfiguration);

/*
				insertServiceConfig = "INSERT INTO "
						+ ServiceConfiguration.TABLE_NAME
						+ "("
						+ ServiceConfiguration.SERVICE_CONFIGURATION_ID + ","
						+ ServiceConfiguration.SERVICE_NAME + ") VALUES ("
						+ "" + "1" + ","
						+ "'" + "ROOT_CONFIGURATION" + "')";


				stmt.executeUpdate(insertServiceConfig);*/
			}



			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally{
			
			
			// close the connection and statement accountApproved

			if(statement !=null)
			{
				
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			

			if(connection!=null)
			{
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
}

