package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    @Autowired
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
                    item.setProduct(mapRow(rs));
                    item.setQuantity(rs.getInt("quantity"));
                    cart.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        }
        return cart;
    }

    @Override
    public ShoppingCartItem insertOrUpdate(int userId, int productId, int quantity){
        String updateItem = "UPDATE shopping_cart SET quantity = quantity + ? " +
                "WHERE product_id = ? AND user_id = ?";

        String addItem = "INSERT INTO shopping_cart (user_id, product_id, quantity) " +
                "VALUES (?, ?, ?) ";

        String getItem = "SELECT * FROM shopping_cart JOIN products USING (product_id) " +
                "WHERE user_id = ? AND product_id = ?";

        ShoppingCartItem item = new ShoppingCartItem();

        try(Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(updateItem)){
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.setInt(3, userId);

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated == 0) {
                try(PreparedStatement ps2 = conn.prepareStatement(addItem)){
                    ps2.setInt(1, userId);
                    ps2.setInt(2, productId);
                    ps2.setInt(3, quantity);

                    int rowsAdded = ps2.executeUpdate();
                    if (rowsAdded == 0){
                        throw new RuntimeException("Failed to add or update.");
                    }
                }
            }

            try(PreparedStatement ps3 = conn.prepareStatement(getItem)){
                ps3.setInt(1, userId);
                ps3.setInt(2, productId);

                try(ResultSet rs = ps3.executeQuery()){
                    if(rs.next()){
                        int quantities = rs.getInt("quantity");
                        item.setProduct(mapRow(rs));
                        item.setQuantity(quantities);
                    }
                }
            }

            return item;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteAllItem(int userId) {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";

        try(Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, userId);

            int rowsDeleted = ps.executeUpdate();

            return rowsDeleted;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected static Product mapRow(ResultSet row) throws SQLException
    {
        int productId = row.getInt("product_id");
        String name = row.getString("name");
        BigDecimal price = row.getBigDecimal("price");
        int categoryId = row.getInt("category_id");
        String description = row.getString("description");
        String subCategory = row.getString("subcategory");
        int stock = row.getInt("stock");
        boolean isFeatured = row.getBoolean("featured");
        String imageUrl = row.getString("image_url");

        return new Product(productId, name, price, categoryId, description, subCategory, stock, isFeatured, imageUrl);
    }
}
