package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Product;
import org.yearup.data.ProductDao;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.service.CoinbaseBtcPriceService;

import java.lang.invoke.ConstantBootstraps;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("products")
@CrossOrigin
public class ProductsController
{
    private ProductDao productDao;
    private UserDao userDao;
    private ProfileDao profileDao;
    private CoinbaseBtcPriceService btcPriceService;

    @Autowired
    public ProductsController(ProductDao productDao, UserDao userDao, ProfileDao profileDao, CoinbaseBtcPriceService btcPriceService)
    {
        this.productDao = productDao;
        this.userDao = userDao;
        this.profileDao = profileDao;
        this.btcPriceService = btcPriceService;
    }

    @GetMapping("")
    @PreAuthorize("permitAll()")
    public List<Product> search(@RequestParam(name="cat", required = false) Integer categoryId,
                                @RequestParam(name="minPrice", required = false) BigDecimal minPrice,
                                @RequestParam(name="maxPrice", required = false) BigDecimal maxPrice,
                                @RequestParam(name="subCategory", required = false) String subCategory,
                                Principal principal
                                )
    {
        String currency = "USD";

        if(principal != null){
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            Profile profile = profileDao.getProfileById(userId);

            if(profile != null && profile.getDefaultCurrency().equalsIgnoreCase("BTC")){
                currency = "BTC";
            }
        }
        try {
            List<Product> products = productDao.search(categoryId, minPrice, maxPrice, subCategory);

            if (currency.equalsIgnoreCase("BTC")){

                BigDecimal usdPerBtc = btcPriceService.getSpotPrice().getAmount();
                for (Product p : products){
                    //BTC = USD / USD per BTC
                    //rounds to nearest 8 decimal places
                    BigDecimal btc = p.getPrice().divide(usdPerBtc, 8, RoundingMode.HALF_UP);
                    p.setPrice(btc);
                }
            }
            return products;
        }
        catch(Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @GetMapping("{id}")
    @PreAuthorize("permitAll()")
    public Product getById(@PathVariable int id, Principal principal)
    {
        String currency = "USD";
        if(principal != null){
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            Profile profile = profileDao.getProfileById(userId);

            if(profile != null && profile.getDefaultCurrency().equalsIgnoreCase("BTC")){
                currency = "BTC";
            }
        }
        try {
            var product = productDao.getById(id);

            if(product == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            if (currency.equalsIgnoreCase("BTC")) {

                BigDecimal usdPerBtc = btcPriceService.getSpotPrice().getAmount();
                BigDecimal btc = product.getPrice().divide(usdPerBtc, 8, RoundingMode.HALF_UP);
                product.setPrice(btc);
            }

            return product;
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Product addProduct(@RequestBody Product product)
    {
        try
        {
            return productDao.create(product);
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateProduct(@PathVariable int id, @RequestBody Product product)
    {
        try
        {
            productDao.update(id, product); //fixed bug where it originally had: productDao.create(product);
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteProduct(@PathVariable int id)
    {
        try
        {
            var product = productDao.getById(id);

            if(product == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            productDao.delete(id);
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}
