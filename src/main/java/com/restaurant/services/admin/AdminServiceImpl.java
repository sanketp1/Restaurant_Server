package com.restaurant.services.admin;

import com.restaurant.dto.SingleProductDto;
import com.restaurant.dto.*;
import com.restaurant.entity.*;
import com.restaurant.enums.ReservationStatus;
import com.restaurant.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final CategoryRepo categoryRepo;

    private final ProductRepo productRepo;

    private final TableReservationRepo tableReservationRepo;

    /// Category Operations

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) throws IOException {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDto, category);
        category.setImg(categoryDto.getImg().getBytes());
        Category createdCategory = categoryRepo.save(category);
        CategoryDto createdCategoryDto = new CategoryDto();
        createdCategoryDto.setId(createdCategory.getId());
        createdCategoryDto.setName(createdCategory.getName());
        return createdCategoryDto;
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepo.findAll().stream().map(Category::getCategoryDto).collect(Collectors.toList());
    }

    @Override
    public List<CategoryDto> searchCategoryByTitle(String title) {
        return categoryRepo.findAllByNameContaining(title).stream().map(Category::getCategoryDto).collect(Collectors.toList());
    }

    //// Product Operations

    @Override
    public ProductDto addProduct(Long categoryId, ProductDto productDto) throws IOException {
        Optional<Category> optionalCategory = categoryRepo.findById(categoryId);
        if (optionalCategory.isPresent()) {
            Product product = new Product();
            BeanUtils.copyProperties(productDto, product);
            product.setImg(productDto.getImg().getBytes());
            product.setCategory(optionalCategory.get());
            Product createdProduct = productRepo.save(product);
            ProductDto createdProductDto = new ProductDto();
            createdProductDto.setId(createdProduct.getId());
            createdProductDto.setName(createdProduct.getName());
            return createdProductDto;
        }
        return null;
    }

    @Override
    public List<ProductDto> getProductsByCategory(Long categoryId) {
        return productRepo.findAllByCategoryId(categoryId).stream().map(Product::getProductDto).collect(Collectors.toList());
    }

    @Override
    public SingleProductDto getProductById(Long productId) {
        Optional<Product> optionalProduct = productRepo.findById(productId);
        SingleProductDto singleProductDto = new SingleProductDto();
        optionalProduct.ifPresent(product -> singleProductDto.setProductDto(product.getProductDto()));
        return singleProductDto;
    }

    @Override
    public ProductDto updateProduct(Long productId, ProductDto productDto) throws IOException {
        Optional<Product> optionalProduct = productRepo.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(productDto.getName());
            product.setPrice(productDto.getPrice());
            product.setDescription(productDto.getDescription());
            if (productDto.getImg() != null) {
                product.setImg(productDto.getImg().getBytes());
            }
            Product updatedProduct = productRepo.save(product);
            ProductDto updatedProductDto = new ProductDto();
            updatedProductDto.setId(updatedProduct.getId());
            updatedProductDto.setName(updatedProduct.getName());
            return updatedProductDto;
        } else {
            return null;
        }
    }

    @Override
    public void deleteProduct(Long productId) {
        Optional<Product> optionalProduct = productRepo.findById(productId);
        if (optionalProduct.isEmpty()) {
            throw new IllegalArgumentException("Product with id " + productId + " not found");
        }
        productRepo.deleteById(productId);
    }

    @Override
    public List<ProductDto> searchProductByCategoryAndTitle(Long categoryId, String title) {
        return productRepo.findAllByCategoryIdAndNameContaining(categoryId, title).stream().map(Product::getProductDto).collect(Collectors.toList());
    }


    ///// Table Reservation Request Operation

    @Override
    public List<TableReservationDto> getAllReservationRequests() {
        return tableReservationRepo.findAll().stream().map(Reservation::getTableReservationDto).collect(Collectors.toList());
    }

    @Override
    public TableReservationDto changeReservationStatus(Long reservationId, String status) {
        Optional<Reservation> optionalTableReservation = tableReservationRepo.findById(reservationId);
        if (optionalTableReservation.isPresent()) {
            Reservation reservation = optionalTableReservation.get();
            if (Objects.equals(status, "Approve")) {
                reservation.setReservationStatus(ReservationStatus.APPROVED);
            } else {
                reservation.setReservationStatus(ReservationStatus.DISAPPROVED);
            }
            Reservation approvedReservation = tableReservationRepo.save(reservation);
            TableReservationDto approvedTableReservationDto = new TableReservationDto();
            approvedTableReservationDto.setId(approvedReservation.getId());
            return approvedTableReservationDto;
        }
        return null;
    }

}
