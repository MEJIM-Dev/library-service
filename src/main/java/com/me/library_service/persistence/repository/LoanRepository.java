package com.me.library_service.persistence.repository;

import com.me.library_service.persistence.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByExpectedReturnDateBeforeAndActualReturnDateIsNullAndNotified(LocalDate now, boolean notified);

}
