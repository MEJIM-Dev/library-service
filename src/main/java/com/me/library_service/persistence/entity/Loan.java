package com.me.library_service.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity(name = "loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Loan extends BaseEntity{

    @ManyToOne(optional = false)
    private Book book;

    @ManyToOne(optional = false)
    private User user;

    private LocalDate checkoutDate;
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;
    private boolean notified;
}
