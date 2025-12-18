package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    ShoppingCartItem insertOrUpdate(int productId, int quantity, int userId);
    int deleteAllItem(int userId);
}
