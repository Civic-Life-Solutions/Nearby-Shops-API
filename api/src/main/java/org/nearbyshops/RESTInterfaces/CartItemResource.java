package org.nearbyshops.RESTInterfaces;

import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.Cart;
import org.nearbyshops.Model.CartItem;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;


@Path("/CartItem")
public class CartItemResource {


	public CartItemResource() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCartItem(CartItem cartItem,
								   @QueryParam("EndUserID") int endUserID,
								   @QueryParam("ShopID") int shopID)
	{


		System.out.println("End User ID : " + endUserID + " ShopID : " + shopID);

		if(endUserID>0 && shopID>0)
		{

			// Check if the Cart exists if not then create one
			List<Cart> list = Globals.cartService.readCarts(endUserID,shopID);

			if(list.size()==0)
			{
				// cart does not exist so create one

				Cart cart = new Cart();

				cart.setEndUserID(endUserID);
				cart.setShopID(shopID);
				int idOfInsertedCart = Globals.cartService.saveCart(cart);

				cartItem.setCartID(idOfInsertedCart);
			}
			else if(list.size()==1)
			{
				// cart exists

				cartItem.setCartID(list.get(0).getCartID());
			}

		}

		int rowCount = Globals.cartItemService.saveCartItem(cartItem);


		if(rowCount == 1)
		{
			
			
			Response response = Response.status(Status.CREATED)
					.entity(null)
					.build();
			
			return response;
			
		}else if(rowCount <= 0)
		{
			Response response = Response.status(Status.NOT_MODIFIED)
					.entity(null)
					.build();
			
			return response;
		}

		return null;
	}

	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCartItem(CartItem cartItem,
								   @QueryParam("EndUserID") int endUserID,
								   @QueryParam("ShopID") int shopID)
	{


		if(endUserID>0 && shopID>0)
		{

			// Check if the Cart exists if not then create one
			List<Cart> list = Globals.cartService.readCarts(endUserID,shopID);

			if(list.size()==0)
			{
				// cart does not exist so create one

				Cart cart = new Cart();

				cart.setEndUserID(endUserID);
				cart.setShopID(shopID);
				int idOfInsertedCart = Globals.cartService.saveCart(cart);

				cartItem.setCartID(idOfInsertedCart);
			}
			else if(list.size()==1)
			{
				// cart exists

				cartItem.setCartID(list.get(0).getCartID());
			}

		}


		int rowCount = Globals.cartItemService.updateCartItem(cartItem);


		if(rowCount >= 1)
		{
			Response response = Response.status(Status.OK)
					.entity(null)
					.build();
			
			return response;
		}
		if(rowCount == 0)
		{
			Response response = Response.status(Status.NOT_MODIFIED)
					.entity(null)
					.build();
			
			return response;
		}


		return null;
	}


	@DELETE
	public Response deleteCartItem(@QueryParam("CartID")int cartID, @QueryParam("ItemID") int itemID,
								   @QueryParam("EndUserID") int endUserID,
								   @QueryParam("ShopID") int shopID)
	{



		if(endUserID>0 && shopID>0)
		{

			// Check if the Cart exists if not then create one
			List<Cart> list = Globals.cartService.readCarts(endUserID,shopID);

			if(list.size()==1)
			{
				cartID = list.get(0).getCartID();
			}

		}


		int rowCount = Globals.cartItemService.deleteCartItem(itemID,cartID);
		


		if(rowCount>=1)
		{

			// if the cart item is the last item then delete the cart also.

			if (Globals.cartItemService.getCartItem(cartID,0,0).size()==0);
			{
				Globals.cartService.deleteCart(cartID);
			}


			Response response = Response.status(Status.OK)
					.entity(null)
					.build();
			
			return response;
		}
		
		if(rowCount == 0)
		{
			Response response = Response.status(Status.NOT_MODIFIED)
					.entity(null)
					.build();
			
			return response;
		}
		
		
		return null;
	}
	




	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCartItem(@QueryParam("CartID")int cartID,
							 	@QueryParam("ItemID")int itemID,
								@QueryParam("EndUserID") int endUserID,
								@QueryParam("ShopID") int shopID)
	{


		List<CartItem> cartList;


		if(shopID>0)
		{

			cartList = Globals.cartItemService.getCartItem(endUserID, shopID);


			for(CartItem cartItem: cartList)
			{

				cartItem.setItem(Globals.itemService.getItem(cartItem.getItemID()));

			}


		}else
		{

			cartList = Globals.cartItemService.getCartItem(cartID,itemID,endUserID);

			for(CartItem cartItem: cartList)
			{
				if(cartID == 0)
				{
					cartItem.setCart(Globals.cartService.readCart(cartItem.getCartID()));
				}

				if(itemID == 0)
				{
					cartItem.setItem(Globals.itemService.getItem(cartItem.getItemID()));
				}

			}

		}


		GenericEntity<List<CartItem>> listEntity = new GenericEntity<List<CartItem>>(cartList){
		};
	
		
		if(cartList.size()<=0)
		{
			Response response = Response.status(Status.NO_CONTENT)
					.entity(listEntity)
					.build();
			
			return response;
			
		}else
		{
			Response response = Response.status(Status.OK)
					.entity(listEntity)
					.build();
			
			return response;
		}
		
	}

}
