package com.example.ecomjava.web.controller;

import com.example.ecomjava.entity.CategoryEntity;
import com.example.ecomjava.entity.ProductEntity;
import com.example.ecomjava.service.ProductService;
import com.example.ecomjava.web.dto.AddToCartDTO;
import com.example.ecomjava.web.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN_USER')")
    public ResponseEntity<?> createProduct(@RequestPart("product") ProductDTO productDTO,@RequestPart("image") MultipartFile imageFile) {
        ProductEntity productEntity = productService.createProduct(productDTO,imageFile);
        if(productEntity.getProductId() == 0){
            return ResponseEntity.ok(Map.of("status",true));
        }else{
            return ResponseEntity.ok(Map.of("status",false));
        }
    }

    @GetMapping("/get-category-list")
    public ResponseEntity<?> getCategoryList() {
        List<CategoryEntity> categoryEntityList = productService.getCategoryList();
        return ResponseEntity.ok(categoryEntityList);
    }

    @GetMapping("/get-product-list")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getProductList() {
        List<ProductEntity> productEntityList = productService.getProductList();
        return ResponseEntity.ok(productEntityList);
    }

    @PostMapping("/add-to-cart")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addToCart(@RequestBody AddToCartDTO addToCartDTO) {
        Long productCount = productService.addToCart(addToCartDTO);
        return ResponseEntity.ok(Map.of("productCount",productCount));
    }
    @GetMapping("/get-by-id/{productId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN_USER')")
    public ResponseEntity<?> getByProductId(@PathVariable("productId") Long productId){
        ProductDTO productDTO = productService.getByProductId(productId);
        return ResponseEntity.ok(productDTO);
    }

    @GetMapping("/get-product-count")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN_USER')")
    public ResponseEntity<?> getProductQtyCount(@RequestParam("productId") Long productId, @RequestParam("userId") Long userId){
        Long count = productService.getCountByProductIdAndUserId(productId,userId);
        return ResponseEntity.ok(Map.of("count",count != null ? count : 0));
    }

    @GetMapping("/get-cart-count")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN_USER')")
    public ResponseEntity<?> getCartQtyCount(@RequestParam("userId") Long userId){
        Long count = productService.getCountByUserId(userId);
        return ResponseEntity.ok(Map.of("count",count != null ? count : 0));
    }
}
