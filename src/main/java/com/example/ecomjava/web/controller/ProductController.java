package com.example.ecomjava.web.controller;

import com.example.ecomjava.config.SecurityUtils;
import com.example.ecomjava.entity.CategoryEntity;
import com.example.ecomjava.entity.ProductEntity;
import com.example.ecomjava.service.ProductService;
import com.example.ecomjava.web.dto.AddToCartDTO;
import com.example.ecomjava.web.dto.PaginationResponseDTO;
import com.example.ecomjava.web.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
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
    public ResponseEntity<?> getProductList(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,@RequestParam(required = false) Long categoryId,
                                            @RequestParam(required = false) String search) {
            Page<ProductEntity> productPage = productService.getPaginatedProductList(page, size,categoryId,search);

            PaginationResponseDTO<ProductEntity> response = new PaginationResponseDTO<>(
                    productPage.getContent(),
                    productPage.getNumber(),
                    productPage.getTotalPages(),
                    productPage.getTotalElements()
            );

            return ResponseEntity.ok(response);
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
    public ResponseEntity<?> getProductQtyCount(@RequestParam("productId") Long productId){
        Long userId = SecurityUtils.getCurrentUserId();
        Long count = productService.getCountByProductIdAndUserId(productId,userId);
        return ResponseEntity.ok(Map.of("count",count != null ? count : 0));
    }

    @GetMapping("/get-cart-count")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN_USER')")
    public ResponseEntity<?> getCartQtyCount(){
        Long userId = SecurityUtils.getCurrentUserId();
        Long count = productService.getCountByUserId(userId);
        return ResponseEntity.ok(Map.of("count",count != null ? count : 0));
    }

    @GetMapping("/cart-detail")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN_USER')")
    public ResponseEntity<?> getCartDetail(){
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(productService.getCartDetail(userId));

    }

    @PostMapping("/remove-item")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN_USER')")
    public ResponseEntity<?> removeProductFromCart(@RequestParam("productId") Long productId){
        Long userId = SecurityUtils.getCurrentUserId();
        int result = productService.removeFromCart(userId,productId);
        if(result == 1){
            return ResponseEntity.ok(Map.of("status", result, "message", "product removed"));
        }else{
            return ResponseEntity.ok(Map.of("status", result, "message", "Fail to removed product"));
        }
    }

    @PostMapping("/checkout")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN_USER')")
    public ResponseEntity<?> checkout(@RequestParam("productId") Long productId,@RequestParam("qty") Long qty,@RequestParam("type") String type){
        Long userId = SecurityUtils.getCurrentUserId();
        boolean result = productService.checkoutCart(productId, qty, userId,type);
        if(result){
            return ResponseEntity.ok(Map.of("status", result, "message", "success"));
        }else{
            return ResponseEntity.ok(Map.of("status", result, "message", "fail to checkout cart"));
        }
    }

}
