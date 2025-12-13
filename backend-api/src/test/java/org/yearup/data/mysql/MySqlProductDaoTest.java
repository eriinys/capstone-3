package org.yearup.data.mysql;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.yearup.controllers.ProductsController;
import org.yearup.data.ProductDao;
import org.yearup.models.Product;
import java.util.*;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

class MySqlProductDaoTest extends BaseDaoTestClass
{
    private MySqlProductDao dao;

    @BeforeEach
    public void setup()
    {
        dao = new MySqlProductDao(dataSource);
    }

    @Test
    public void getById_shouldReturn_theCorrectProduct()
    {
        // arrange
        int productId = 1;
        Product expected = new Product()
        {{
            setProductId(1);
            setName("Smartphone");
            setPrice(new BigDecimal("499.99"));
            setCategoryId(1);
            setDescription("A powerful and feature-rich smartphone for all your communication needs.");
            setSubCategory("Black");
            setStock(50);
            setFeatured(false);
            setImageUrl("smartphone.jpg");
        }};

        // act
        var actual = dao.getById(productId);

        // assert
        assertEquals(expected.getPrice(), actual.getPrice(), "Because I tried to get product 1 from the database.");
    }

    //tests for bug fix #1
    @Test
    public void searchShouldReturnCorrectProducts(){
        //arange
        Integer categoryId = 3;
        BigDecimal minPrice = BigDecimal.valueOf(80);
        BigDecimal maxPrice = BigDecimal.valueOf(90);
        String subCategory = "";

        //act
        List<Product> result = dao.search(categoryId, minPrice, maxPrice, subCategory);

        //assert
        assertNotNull(result);
        for(Product p : result){
            assertEquals(3, p.getCategoryId());
            assertTrue(p.getPrice().compareTo(minPrice) >= 0); //product price is greater or equal to minPrice
            assertTrue(p.getPrice().compareTo(maxPrice) <= 0); //product price is less than or equal to maxPrice
        }
    }
}

