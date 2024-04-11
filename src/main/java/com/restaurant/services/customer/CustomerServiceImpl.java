package com.restaurant.services.customer;

import com.restaurant.dto.CategoryDto;
import com.restaurant.dto.ProductDto;
import com.restaurant.dto.TableReservationDto;
import com.restaurant.entity.Category;
import com.restaurant.entity.Product;
import com.restaurant.entity.Reservation;
import com.restaurant.entity.User;
import com.restaurant.enums.ReservationStatus;
import com.restaurant.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final UserRepo userRepo;

    private final CategoryRepo categoryRepo;

    private final ProductRepo productRepo;

    private final TableReservationRepo tableReservationRepo;


    //// Categories Operations

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
    public List<ProductDto> getProductsByCategory(Long categoryId) {
        return productRepo.findAllByCategoryId(categoryId).stream().map(Product::getProductDto).collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> searchProductByCategoryAndTitle(Long categoryId, String title) {
        return productRepo.findAllByCategoryIdAndNameContaining(categoryId, title).stream().map(Product::getProductDto).collect(Collectors.toList());
    }


    ////// Reserve Table Operations

    @Override
    public TableReservationDto applyForReservation(TableReservationDto tableReservationDto) {
        Optional<User> optionalUser = userRepo.findById(tableReservationDto.getUserId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Reservation reservation = new Reservation();
            reservation.setTableType(tableReservationDto.getTableType());
            reservation.setDescription(tableReservationDto.getDescription());
            reservation.setReservationStatus(ReservationStatus.PENDING);
            reservation.setDateTime(tableReservationDto.getDateTime());
            reservation.setUser(user);
            Reservation requestedReservation = tableReservationRepo.save(reservation);
            TableReservationDto requestedTableReservationDto = new TableReservationDto();
            requestedTableReservationDto.setId(requestedReservation.getId());
            return requestedTableReservationDto;
        } else {
            return null;
        }
    }

    @Override
    public List<TableReservationDto> getAllReservationByUserId(Long userId) {
        return tableReservationRepo.findAllByUserId(userId).stream().map(Reservation::getTableReservationDto).collect(Collectors.toList());
    }

}

