package com.restaurant.controller;

import com.restaurant.dto.*;
import com.restaurant.services.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;


    ////// Category Operations

    @PostMapping("/category")
    public ResponseEntity<CategoryDto> createCategory(@ModelAttribute CategoryDto categoryDto) throws IOException {
        CategoryDto createdCategory = adminService.createCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categoryDtos = adminService.getAllCategories();
        return ResponseEntity.ok(categoryDtos);
    }

    @GetMapping("/categories/{title}")
    public ResponseEntity<List<CategoryDto>> searchCategoryByTitle(@PathVariable("title") String title) {
        List<CategoryDto> categoryDtos = adminService.searchCategoryByTitle(title);
        return ResponseEntity.ok(categoryDtos);
    }

    /// Product Operations

    @PostMapping("/{categoryId}/product")
    public ResponseEntity<?> addProduct(@PathVariable Long categoryId, @ModelAttribute ProductDto productDto) throws IOException {
        ProductDto createdProduct = adminService.addProduct(categoryId, productDto);
        if (createdProduct == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductDto> productDtos = adminService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(productDtos);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<SingleProductDto> getProductById(@PathVariable Long productId) {
        SingleProductDto productDto = adminService.getProductById(productId);
        if (productDto != null) {
            return ResponseEntity.ok(productDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/product/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long productId, @ModelAttribute ProductDto productDto) throws IOException {
        ProductDto updatedProduct = adminService.updateProduct(productId, productDto);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        adminService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{categoryId}/products/{title}")
    public ResponseEntity<List<ProductDto>> searchProductByCategoryAndTitle(@PathVariable("categoryId") Long categoryId, @PathVariable("title") String title) {
        List<ProductDto> productDtos = adminService.searchProductByCategoryAndTitle(categoryId, title);
        return ResponseEntity.ok(productDtos);
    }


    ///// Table Reservation Request Operation

    @GetMapping("/reservations")
    public ResponseEntity<List<TableReservationDto>> getAllReservationRequests() {
        List<TableReservationDto> tableReservationDtos = adminService.getAllReservationRequests();
        return ResponseEntity.ok(tableReservationDtos);
    }

    @GetMapping("/reservation/{reservationId}/{status}")
    public ResponseEntity<?> changeReservationStatus(@PathVariable Long reservationId, @PathVariable String status) {
        TableReservationDto approvedTableReservationDto = adminService.changeReservationStatus(reservationId, status);
        if (approvedTableReservationDto == null)
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.OK).body(approvedTableReservationDto);
    }

}
