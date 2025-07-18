package com.me.library_service.service.impl;

import com.me.library_service.exception.CustomException;
import com.me.library_service.model.enums.ResponseCode;
import com.me.library_service.model.request.LoanRequest;
import com.me.library_service.model.response.ApiResponse;
import com.me.library_service.model.response.BookResponse;
import com.me.library_service.model.response.LoanResponse;
import com.me.library_service.model.response.PagedResponse;
import com.me.library_service.persistence.entity.Book;
import com.me.library_service.persistence.entity.Loan;
import com.me.library_service.persistence.entity.User;
import com.me.library_service.persistence.repository.BookRepository;
import com.me.library_service.persistence.repository.LoanRepository;
import com.me.library_service.persistence.repository.UserRepository;
import com.me.library_service.service.LoanService;
import com.me.library_service.util.ModelMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;


    @Override
    public ApiResponse checkout(LoanRequest loanRequest) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ResponseCode.FAIL.getMessage());

        try {
            Book book = bookRepository.findById(loanRequest.getBookId())
                    .orElseThrow(()-> new CustomException("Book not found", HttpStatus.BAD_REQUEST));

            User user = userRepository.findById(loanRequest.getUserId())
                    .orElseThrow(()-> new CustomException("User not found", HttpStatus.BAD_REQUEST));

            Loan loan = Loan.builder()
                    .book(book)
                    .user(user)
                    .checkoutDate(LocalDate.now())
                    .expectedReturnDate(LocalDate.now().plusDays(10))
                    .notified(false)
                    .build();

            Loan saved = loanRepository.save(loan);

            LoanResponse loanResponse = ModelMapperUtils.map(saved, LoanResponse.class);

            apiResponse.setStatus(ResponseCode.SUCCESSFUL.getStatus());
            apiResponse.setMessage(ResponseCode.SUCCESSFUL.getMessage());
            apiResponse.setData(loanResponse);
        } catch (Exception e){
            log.error("Error: {}", e.getStackTrace());
        }

        return apiResponse;
    }

    @Override
    public ApiResponse checkin(Long id) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ResponseCode.FAIL.getMessage());

        try {
            Loan loan = loanRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Loan not found", HttpStatus.BAD_REQUEST));

            loan.setActualReturnDate(LocalDate.now());
            Loan saved = loanRepository.save(loan);

            LoanResponse loanResponse = ModelMapperUtils.map(saved, LoanResponse.class);

            apiResponse.setStatus(ResponseCode.SUCCESSFUL.getStatus());
            apiResponse.setMessage(ResponseCode.SUCCESSFUL.getMessage());
            apiResponse.setData(loanResponse);
        } catch (Exception e){
            log.error("Error: {}", e.getStackTrace());
        }

        return apiResponse;
    }

    @Override
    public ApiResponse findAll(Pageable pageable) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ResponseCode.FAIL.getMessage());

        try {
            Page<Loan> loans = loanRepository.findAll(pageable);

            PagedResponse<LoanResponse> pagedResponse = PagedResponse.<LoanResponse>builder()
                    .content(loans.map(loan -> ModelMapperUtils.map(loan, LoanResponse.class)).getContent())
                    .totalElements(loans.getTotalElements())
                    .build();

            apiResponse.setStatus(ResponseCode.SUCCESSFUL.getStatus());
            apiResponse.setMessage(ResponseCode.SUCCESSFUL.getMessage());
            apiResponse.setData(pagedResponse);
        } catch (Exception e){
            log.error("Error: {}", e.getStackTrace());
        }

        return apiResponse;
    }
}
