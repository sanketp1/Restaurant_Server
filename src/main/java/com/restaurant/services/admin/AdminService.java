package com.restaurant.services.admin;

import com.restaurant.dto.*;

import java.io.IOException;
import java.util.List;

public interface AdminService {


    //// Category Operations

    CategoryDto createCategory(CategoryDto categoryDto) throws IOException;

    List<CategoryDto> getAllCategories();

    List<CategoryDto> searchCategoryByTitle(String title);


    ///// Product Operations

    ProductDto addProduct(Long categoryId, ProductDto productDto) throws IOException;

    List<ProductDto> getProductsByCategory(Long categoryId);

    SingleProductDto getProductById(Long productId);

    ProductDto updateProduct(Long productId, ProductDto productDto) throws IOException;

    void deleteProduct(Long productId);


    List<ProductDto> searchProductByCategoryAndTitle(Long categoryId, String title);


    ///// Table Reservation Request Operation

    List<TableReservationDto> getAllReservationRequests();

    TableReservationDto changeReservationStatus(Long reservationId, String status);
}
