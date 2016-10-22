package org.nearbyshops.Model;


import java.sql.Timestamp;

public class Shop {


	// Shop Table Name
	public static final String TABLE_NAME = "SHOP";

	// Shop columns

	public static final String SHOP_ID = "SHOP_ID";
	public static final String SHOP_NAME = "SHOP_NAME";
	public static final String DELIVERY_RANGE = "DELIVERY_RANGE";
	public static final String LAT_CENTER = "LAT_CENTER";
	public static final String LON_CENTER = "LON_CENTER";

	public static final String LAT_MAX = "LAT_MAX";
	public static final String LON_MAX = "LON_MAX";
	public static final String LAT_MIN = "LAT_MIN";
	public static final String LON_MIN = "LON_MIN";

	public static final String DELIVERY_CHARGES = "DELIVERY_CHARGES";
	public static final String DISTRIBUTOR_ID = "Distributor";
	public static final String IMAGE_PATH = "IMAGE_PATH";
	public static final String BACKDROP_IMAGE_PATH = "BACKDROP_IMAGE_PATH";
	public static final String LOGO_IMAGE_PATH = "LOGO_IMAGE_PATH";

	// recently Added
	public static final String SHOP_ADDRESS = "SHOP_ADDRESS";
	public static final String CITY = "CITY";
	public static final String PINCODE = "PINCODE";
	public static final String LANDMARK = "LANDMARK";
	public static final String BILL_AMOUNT_FOR_FREE_DELIVERY = "BILL_AMOUNT_FOR_FREE_DELIVERY";
	public static final String CUSTOMER_HELPLINE_NUMBER = "CUSTOMER_HELPLINE_NUMBER";
	public static final String DELIVERY_HELPLINE_NUMBER = "DELIVERY_HELPLINE_NUMBER";
	public static final String SHORT_DESCRIPTION = "SHORT_DESCRIPTION";
	public static final String LONG_DESCRIPTION = "LONG_DESCRIPTION";
	public static final String DATE_TIME_STARTED = "DATE_TIME_STARTED";
	public static final String IS_OPEN = "IS_SHOP_OPEN";

	// to be added
	public static final String PICK_FROM_SHOP_AVAILABLE = "PICK_FROM_SHOP_AVAILABLE";
	public static final String HOME_DELIVERY_AVAILABLE = "HOME_DELIVERY_AVAILABLE";




	// query postgres

	public static final String createTableShopPostgres =  "CREATE TABLE IF NOT EXISTS " + Shop.TABLE_NAME + "("
			+ " " + Shop.SHOP_ID + " SERIAL PRIMARY KEY,"
			+ " " + Shop.SHOP_NAME + " text,"
			+ " " + Shop.DELIVERY_RANGE + " FLOAT,"
			+ " " + Shop.LON_CENTER + " FLOAT,"
			+ " " + Shop.LAT_CENTER + " FLOAT,"
			+ " " + Shop.LON_MAX + " FLOAT,"
			+ " " + Shop.LAT_MAX + " FLOAT,"
			+ " " + Shop.LON_MIN + " FLOAT,"
			+ " " + Shop.LAT_MIN + " FLOAT,"
			+ " " + Shop.DELIVERY_CHARGES + " FLOAT,"
			+ " " + Shop.DISTRIBUTOR_ID + " INT,"
			+ " " + Shop.IMAGE_PATH + " text,"
			+ " " + Shop.SHOP_ADDRESS + " text,"
			+ " " + Shop.CITY + " text,"
			+ " " + Shop.PINCODE + " INT,"
			+ " " + Shop.LANDMARK + " text,"
			+ " " + Shop.BILL_AMOUNT_FOR_FREE_DELIVERY + " INT,"
			+ " " + Shop.CUSTOMER_HELPLINE_NUMBER + " text,"
			+ " " + Shop.DELIVERY_HELPLINE_NUMBER + " text,"
			+ " " + Shop.SHORT_DESCRIPTION + " text,"
			+ " " + Shop.LONG_DESCRIPTION + " text,"
			+ " " + Shop.DATE_TIME_STARTED + " timestamp with time zone NOT NULL DEFAULT now(),"
			+ " " + Shop.IS_OPEN + " boolean,"
			+ " FOREIGN KEY(" + Shop.DISTRIBUTOR_ID +") REFERENCES DISTRIBUTOR(ID))";






	// real time variables
	private double distance;

	// normal variables
	private int shopID;
	
	private String shopName;

	// the radius of the circle considering shop location as its center.
	//This is the distance upto which shop can deliver its items
	private double deliveryRange;

	// latitude and longitude for storing the location of the shop
	private double latCenter;
	private double lonCenter;

	// bounding coordinates for the shop generated using shop center coordinates and delivery range.
	private double latMax;
	private double lonMax;
	private double latMin;
	private double lonMin;


	// delivery charger per order
	private double deliveryCharges;

	private int distributorID;
	
	private String imagePath;


	// added recently
	private String shopAddress;
	private String city;
	private long pincode;
	private String landmark;
	private int billAmountForFreeDelivery;
	private String customerHelplineNumber;
	private String deliveryHelplineNumber;
	private String shortDescription;
	private String longDescription;
	private Timestamp dateTimeStarted;
	private boolean isOpen;



	private float rt_rating_avg;
	private float rt_rating_count;


	public float getRt_rating_avg() {
		return rt_rating_avg;
	}

	public void setRt_rating_avg(float rt_rating_avg) {
		this.rt_rating_avg = rt_rating_avg;
	}

	public float getRt_rating_count() {
		return rt_rating_count;
	}

	public void setRt_rating_count(float rt_rating_count) {
		this.rt_rating_count = rt_rating_count;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}



	public double getDeliveryRange() {
		return deliveryRange;
	}

	public void setDeliveryRange(double deliveryRange) {
		this.deliveryRange = deliveryRange;
	}

	public double getLatCenter() {
		return latCenter;
	}

	public void setLatCenter(double latCenter) {
		this.latCenter = latCenter;
	}

	public double getLonCenter() {
		return lonCenter;
	}

	public void setLonCenter(double lonCenter) {
		this.lonCenter = lonCenter;
	}

	public double getLatMax() {
		return latMax;
	}

	public void setLatMax(double latMax) {
		this.latMax = latMax;
	}

	public double getLonMax() {
		return lonMax;
	}

	public void setLonMax(double lonMax) {
		this.lonMax = lonMax;
	}

	public double getLatMin() {
		return latMin;
	}

	public void setLatMin(double latMin) {
		this.latMin = latMin;
	}

	public double getLonMin() {
		return lonMin;
	}

	public void setLonMin(double lonMin) {
		this.lonMin = lonMin;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getDistributorID() {
		return distributorID;
	}

	public double getDeliveryCharges() {
		return deliveryCharges;
	}

	public void setDeliveryCharges(double deliveryCharges) {
		this.deliveryCharges = deliveryCharges;
	}

	public void setDistributorID(int distributorID) {
		this.distributorID = distributorID;
	}
	


	public Shop() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}


	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public long getPincode() {
		return pincode;
	}

	public void setPincode(long pincode) {
		this.pincode = pincode;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public int getBillAmountForFreeDelivery() {
		return billAmountForFreeDelivery;
	}

	public void setBillAmountForFreeDelivery(int billAmountForFreeDelivery) {
		this.billAmountForFreeDelivery = billAmountForFreeDelivery;
	}

	public String getCustomerHelplineNumber() {
		return customerHelplineNumber;
	}

	public void setCustomerHelplineNumber(String customerHelplineNumber) {
		this.customerHelplineNumber = customerHelplineNumber;
	}

	public String getDeliveryHelplineNumber() {
		return deliveryHelplineNumber;
	}

	public void setDeliveryHelplineNumber(String deliveryHelplineNumber) {
		this.deliveryHelplineNumber = deliveryHelplineNumber;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public Timestamp getDateTimeStarted() {
		return dateTimeStarted;
	}

	public void setDateTimeStarted(Timestamp dateTimeStarted) {
		this.dateTimeStarted = dateTimeStarted;
	}

	public boolean getisOpen() {
		return isOpen;
	}

	public void setisOpen(boolean open) {
		isOpen = open;
	}
}
