package org.yearup.data.mysql;

import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        ShoppingCart cart = new ShoppingCart();

        String sql = "SELECT * FROM shopping_cart " +
                "JOIN products USING(product_id) " +
                "WHERE user_id = ?";
        try(Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, userId);

            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    ShoppingCartItem item = new ShoppingCartItem();
                    item.setProduct(new Product(rs.getInt("product_id"), rs.getString("name"), rs.getBigDecimal("price"),
                            rs.getInt("category_id"), rs.getString("description"), rs.getString("subcategory"),
                            rs.getInt("stock"), rs.getBoolean("featured"), rs.getString("image_url")));
                    item.setQuantity(rs.getInt("quantity"));
                    cart.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cart;
    }
}
