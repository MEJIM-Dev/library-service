package com.me.library_service.service;

import com.me.library_service.model.request.LoanRequest;
import com.me.library_service.model.response.ApiResponse;
import org.springframework.data.domain.Pageable;

public interface LoanService {
    ApiResponse checkout(LoanRequest loanRequest);

    ApiResponse checkin(Long id);

    ApiResponse findAll(Pageable pageable);
}
