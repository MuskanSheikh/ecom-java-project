package com.example.ecomjava.service.implementation;

import com.example.ecomjava.entity.AddToCart;
import com.example.ecomjava.entity.CategoryEntity;
import com.example.ecomjava.entity.ProductEntity;
import com.example.ecomjava.repository.AddToCartRepository;
import com.example.ecomjava.repository.CategoryRepository;
import com.example.ecomjava.repository.ProductRepository;
import com.example.ecomjava.web.dto.AddToCartDTO;
import com.example.ecomjava.web.dto.ProductDTO;
import com.example.ecomjava.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AddToCartRepository addToCartRepository;

    @Override
    public ProductEntity createProduct(ProductDTO productDTO, MultipartFile imageFile) {
        Optional<ProductEntity> productEntityOptional = productRepository.findByIgnoreCaseProductName(productDTO.getProductName());
        if (productEntityOptional.isPresent()) return productEntityOptional.get();

        String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
        String uploadDir = "C:/Users/pcs72/Projects/React-Project/public/product-images/";

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image file: " + e.getMessage());
        }

        ProductEntity product = ProductEntity.builder()
                .productName(productDTO.getProductName())
                .description(productDTO.getDescription())
                .quantity(productDTO.getQuantity())
                .oldPrice(productDTO.getOldPrice())
                .newPrice(productDTO.getNewPrice())
                .imageUrl(uploadDir + fileName)
                .fileName(fileName)
                .categoryId(productDTO.getCategoryId()).build();
        return productRepository.save(product);
    }

    @Override
    public List<CategoryEntity> getCategoryList() {
        return categoryRepository.findAll();
    }

    @Override
    public List<ProductEntity> getProductList() {
        return productRepository.findAll();
    }

    @Override
    public Long addToCart(AddToCartDTO addToCartDTO) {
        Optional<AddToCart> addToCartOptional = addToCartRepository.findByProductIdAndUserId(addToCartDTO.getProductId(),addToCartDTO.getUserId());
        AddToCart addToCart;
        if(addToCartOptional.isPresent()){
            addToCart = addToCartOptional.get();
            addToCart.setQuantity(addToCartDTO.getQuantity());
        }else{
            addToCart = AddToCart.builder()
                    .productId(addToCartDTO.getProductId())
                    .userId(addToCartDTO.getUserId())
                    .quantity(addToCartDTO.getQuantity())
                    .isDeleted(0).build();
        }
            addToCartRepository.save(addToCart);

        Long count = addToCartRepository.countByUserId(addToCart.getUserId());
        return count;
    }

    @Override
    public ProductDTO getByProductId(Long productId) {
        Optional<ProductEntity> byProductId = productRepository.findByProductId(productId);
        ProductDTO productDTO = new ProductDTO();
        if(byProductId.isPresent()){
            productDTO = ProductDTO.getEntity(byProductId.get());
        }
        return productDTO;
    }

    @Override
    public Long getCountByProductIdAndUserId(Long productId, Long userId) {
        Optional<AddToCart> addToCartOptional = addToCartRepository.findByProductIdAndUserId(productId,userId);
        Long count = 0L;
        if(addToCartOptional.isPresent()){
            count = addToCartOptional.get().getQuantity();
        }
        return count;
    }

    @Override
    public Long getCountByUserId(Long userId) {
        return addToCartRepository.countByUserId(userId);
    }


}
