package org.yearup.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.yearup.data.ProductDao;
import org.yearup.models.Product;
import org.yearup.security.JwtAccessDeniedHandler;
import org.yearup.security.JwtAuthenticationEntryPoint;
import org.yearup.security.UserModelDetailsService;
import org.yearup.security.jwt.TokenProvider;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//tests for bug fix #2
@WebMvcTest(ProductsController.class) //loads only the web layer (controller/MVC)
public class ProductsControllerTest {
    @Autowired
    private MockMvc mvc; //simulates HTTP requests without a real server
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductDao productDao;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @MockBean
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @MockBean
    private UserModelDetailsService userModelDetailsService;

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void updateProductShouldUpdateNotCreate() throws  Exception {

}