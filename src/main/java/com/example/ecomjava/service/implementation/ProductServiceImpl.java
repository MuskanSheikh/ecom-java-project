package com.example.ecomjava.service.implementation;

import com.example.ecomjava.common.Constant;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Long addToCart(AddToCartDTO addToCartDTO) {
        Optional<AddToCart> addToCartOptional = addToCartRepository.findByProductIdAndUserIdAndStatus(addToCartDTO.getProductId(),addToCartDTO.getUserId(),Constant.ADDED_TO_CART);
        AddToCart addToCart;
        if(addToCartOptional.isPresent()){
            addToCart = addToCartOptional.get();
            addToCart.setQuantity(addToCartDTO.getQuantity());
        }else{
            addToCart = AddToCart.builder()
                    .productId(addToCartDTO.getProductId())
                    .userId(addToCartDTO.getUserId())
                    .quantity(addToCartDTO.getQuantity())
                    .status(Constant.ADDED_TO_CART)
                    .isDeleted(0).build();
        }
            addToCartRepository.save(addToCart);

        Long count = addToCartRepository.countByUserIdAndStatus(addToCart.getUserId(),Constant.ADDED_TO_CART);
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
        Optional<AddToCart> addToCartOptional = addToCartRepository.findByProductIdAndUserIdAndStatus(productId,userId,Constant.ADDED_TO_CART);
        Long count = 0L;
        if(addToCartOptional.isPresent()){
            count = addToCartOptional.get().getQuantity();
        }
        return count;
    }

    @Override
    public Long getCountByUserId(Long userId) {
        return addToCartRepository.countByUserIdAndStatus(userId,Constant.ADDED_TO_CART);
    }

    @Override
    public List<ProductDTO> getCartDetail(Long userId) {
        List<AddToCart> cartList = addToCartRepository.findByUserIdAndStatus(userId,Constant.ADDED_TO_CART);
        List<ProductEntity> productList = productRepository.findAllById(cartList.stream().map(AddToCart::getProductId).collect(Collectors.toList()));
        List<ProductDTO> productDTOList = new ArrayList<>();
        productList.forEach(productEntity -> {
            ProductDTO productDTO = ProductDTO.getEntity(productEntity);
            cartList.stream()
                    .filter(cart -> cart.getProductId().equals(productEntity.getProductId()))
                    .findFirst()
                    .ifPresent(cart -> productDTO.setCartQty(cart.getQuantity()));
            productDTOList.add(productDTO);
        });
        return productDTOList.isEmpty() ? null: productDTOList;
    }

    @Override
    public int removeFromCart(Long userId, Long productId) {
        return addToCartRepository.deleteByProductIdAndUserId(productId,userId);
    }

    @Override
    public boolean checkoutCart(Long productId, Long qty,Long userId,String type) {
        if(type.equalsIgnoreCase("checkout")){
            Optional<AddToCart> cartOptional = addToCartRepository.findByProductIdAndUserIdAndStatus(productId, userId,Constant.ADDED_TO_CART);
            if(cartOptional.isPresent()){
                cartOptional.get().setQuantity(qty);
                addToCartRepository.save(cartOptional.get());
            }
            return true;
        }else{
            Optional<AddToCart> cartOptional = addToCartRepository.findByProductIdAndUserIdAndStatus(productId, userId,Constant.ADDED_TO_CART);
            if(cartOptional.isPresent()){
                cartOptional.get().setStatus(Constant.CONFIRMED);
                addToCartRepository.save(cartOptional.get());
            }
            Optional<ProductEntity> productEntityOptional = productRepository.findByProductId(productId);
            if(productEntityOptional.isPresent()){
                ProductEntity product = productEntityOptional.get();
                long currentQty = product.getQuantity();

                if (qty > currentQty) {
                    return false;
                }
                product.setQuantity(currentQty - qty);
                productRepository.save(product);

                return true;
            }
        }
        return false;
    }

    @Override
    public Page<ProductEntity> getPaginatedProductList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("productId").descending());
        return productRepository.findAllByIsDeleted(pageable,0);
    }
}
