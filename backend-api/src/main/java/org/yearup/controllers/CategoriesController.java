package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;

@RestController
@RequestMapping("categories")
@CrossOrigin //allows endpoints in this controller to receive requests from frontend running from another origin
public class CategoriesController
{
    private CategoryDao categoryDao;
    private ProductDao productDao;

    // create an Autowired controller to inject the categoryDao and ProductDao
    @Autowired
    public CategoriesController(ProductDao productDao, CategoryDao categoryDao){
        this.productDao = productDao;
        this.categoryDao =categoryDao;
    }

    @GetMapping("")//nothing appended after /categories
    @PreAuthorize("permitAll()") //Everyone is allowed to call this endpoint. No authentication required
    public ResponseEntity<List<Category>> getAll() {
        List<Category> categories = categoryDao.getAllCategories();
        if (categories == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops, something went wrong on our side.");
        }
        return ResponseEntity.ok(categories);
    }

    @GetMapping("{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Category> getById(@PathVariable int id) {
        Category category = categoryDao.getById(id);
        if (category == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
        }
        return ResponseEntity.ok(category);
    }

    @GetMapping("{categoryId}/products")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<Product>> getProductsById(@PathVariable int categoryId) {
        List<Product> products = productDao.listByCategoryId(categoryId);
        if(products == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No products found by the following category ID:" + categoryId);
        }
        return ResponseEntity.ok(products);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") //only ADMIN can call this function
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        Category savedCategory = categoryDao.create(category);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/categories/" + savedCategory.getCategoryId())
                .body(savedCategory);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateCategory(@PathVariable int id, @RequestBody Category category) {
        if (category.getName() == null || category.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
        }

        int rowUpdated = categoryDao.update(id, category);

        if (rowUpdated != 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
        }
        return ResponseEntity.ok().build(); //returns status code 200 with empty body
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        int rowDeleted = categoryDao.delete(id);
        if (rowDeleted != 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"); //throws 404, no category found to delete
        }
        return ResponseEntity.noContent().build(); //NO_CONTENT returns status code 204 with empty body, delete was successful
    }
}
