package com.jandprocu.jandchase.api.productsms.service;

import com.jandprocu.jandchase.api.productsms.exception.ProductNotCreatedException;
import com.jandprocu.jandchase.api.productsms.exception.ProductNotFoundException;
import com.jandprocu.jandchase.api.productsms.exception.ProductNotUpdatedException;
import com.jandprocu.jandchase.api.productsms.repository.specification.ProductSpecification;
import com.jandprocu.jandchase.api.productsms.rest.ProductRequestByIds;
import com.jandprocu.jandchase.api.productsms.model.Product;
import com.jandprocu.jandchase.api.productsms.repository.ProductRepository;
import com.jandprocu.jandchase.api.productsms.rest.ProductResponse;
import com.jandprocu.jandchase.api.productsms.rest.ProductResponsePageable;
import com.jandprocu.jandchase.api.productsms.rest.ProductRest;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {

    private ProductRepository productRepository;
    private ModelMapper modelMapper;

    @Autowired
    private Environment env;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRest productRequest) {

        Product productEntity = modelMapper.map(productRequest, Product.class);
        productEntity.setProductId(UUID.randomUUID().toString());
        productEntity.setCreatedAt(new Date());
        try {
            productRepository.save(productEntity);
        } catch (DataAccessException exception) {
            throw new ProductNotCreatedException("Product " + productEntity.getName() + " not created");
        }

        return modelMapper.map(productEntity, ProductResponse.class);
    }

    @Override
    public ProductResponse getProductByProductId(String productId) {
        Product productEntity = getProductEntityByProductId(productId);
        return this.modelMapper.map(productEntity, ProductResponse.class);
    }

    @Override
    @Transactional
    public ProductResponse updateProductByProductId(String productId, ProductRest updateRequest) {
        Product productEntity = getProductEntityByProductId(productId);
        Product updatedProduct = this.updateProductEntity(updateRequest, productEntity);
        try {
            productRepository.save(updatedProduct);
        } catch (DataAccessException exception) {
            throw new ProductNotUpdatedException("Product " + productId + " not updated");
        }
        return this.modelMapper.map(updatedProduct, ProductResponse.class);
    }

    @Override
    @Transactional
    public ProductResponse partialUpdateProductByProductId(String productId, Map<String, Object> updateRequest) {
        Product productEntity = getProductEntityByProductId(productId);
        Product updatedProduct;
        try {
            updatedProduct = this.updatePartialProductEntity(updateRequest, productEntity);
            productRepository.save(updatedProduct);
        } catch (Exception exception) {
            throw new ProductNotUpdatedException("Product " + productId + " not updated");
        }
        return this.modelMapper.map(updatedProduct, ProductResponse.class);
    }

    private Product updatePartialProductEntity(Map<String, Object> updateRequest, Product productEntity) throws Exception {
        boolean anyError = updateRequest.entrySet().stream().anyMatch(entry -> {
            boolean updated = true;
            try {
                productEntity.setFieldValue(entry.getKey(), entry.getValue());
            } catch (Exception e) {
                updated = false;
            }
            return updated == false;
        });
        if (anyError) {
            throw new Exception();
        }

        return productEntity;
    }

    private Product updateProductEntity(ProductRest updateRequest, Product productEntity) {
        productEntity.setName(updateRequest.getName());
        productEntity.setDescription(updateRequest.getDescription());
        productEntity.setCategory(updateRequest.getCategory());
        productEntity.setAmount(updateRequest.getAmount());
        productEntity.setCurrency(updateRequest.getCurrency());
        return productEntity;
    }

    @Override
    @Transactional
    public ProductResponse deleteProductByProductId(String productId) {
        Product productEntity = getProductEntityByProductId(productId);
        this.productRepository.deleteById(productEntity.getId());
        return this.modelMapper.map(productEntity, ProductResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponsePageable getAllProductsByProductId(ProductRequestByIds requestByIds, int pageNo, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Product> pagedResult = this.productRepository.findByProductIdIn(requestByIds.getProductIds(), paging);

        return getListOfProductsResponse(pagedResult, pageNo);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponsePageable getAllProducts(String name, int pageNo, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Specification<Product> spec = Specification.where(new ProductSpecification(name));
        Page<Product> pagedResult = this.productRepository.findAll(spec, paging);

        return getListOfProductsResponse(pagedResult, pageNo);
    }

    @Override
    public String getFromCloudConfig() {
        return env.getProperty("product.name.property");
    }

    private ProductResponsePageable getListOfProductsResponse(Page<Product> pagedResult, int currentPage) {
        List<ProductResponse> productResponses = new ArrayList<>();
        ProductResponsePageable productResponsePageable = new ProductResponsePageable();
        if (pagedResult.hasContent()) {
            List<Product> products = pagedResult.getContent();
            productResponses.addAll(products.stream().map(product -> this.modelMapper.map(product, ProductResponse.class))
                    .collect(Collectors.toList()));

            productResponsePageable.setTotalPages(pagedResult.getTotalPages());
            productResponsePageable.setCurrentPageNumber(currentPage);
            productResponsePageable.setProducts(productResponses);
        }

        return productResponsePageable;
    }

    @Transactional(readOnly = true)
    public Product getProductEntityByProductId(String productId) {
        Product productEntity = this.productRepository.findByProductId(productId);
        if (productEntity == null)
            throw new ProductNotFoundException("Product with productId: " + productId + " not found");
        return productEntity;
    }


}
