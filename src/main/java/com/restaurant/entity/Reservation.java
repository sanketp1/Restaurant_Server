package com.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restaurant.dto.TableReservationDto;
import com.restaurant.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Data
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tableType;

    private String description;

    private Date dateTime;

    private ReservationStatus reservationStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    public TableReservationDto getTableReservationDto() {
        TableReservationDto tableReservationDto = new TableReservationDto();
        tableReservationDto.setId(id);
        tableReservationDto.setTableType(tableType);
        tableReservationDto.setDescription(description);
        tableReservationDto.setDateTime(dateTime);
        tableReservationDto.setReservationStatus(reservationStatus);
        tableReservationDto.setUserId(user.getId());
        tableReservationDto.setUsername(user.getName());
        return tableReservationDto;
    }

}
