package com.restaurant.services.customer;

import com.restaurant.dto.CategoryDto;
import com.restaurant.dto.ProductDto;
import com.restaurant.dto.TableReservationDto;

import java.util.List;

public interface CustomerService {


    //// Categories Operations

    List<CategoryDto> getAllCategories();

    List<CategoryDto> searchCategoryByTitle(String title);


    //// Product Operations

    List<ProductDto> getProductsByCategory(Long categoryId);

    List<ProductDto> searchProductByCategoryAndTitle(Long categoryId, String title);


    ////// Reserve Table Operations

    TableReservationDto applyForReservation(TableReservationDto tableReservationDto);

    List<TableReservationDto> getAllReservationByUserId(Long userId);

}
