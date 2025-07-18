package com.me.library_service.controller;

import com.me.library_service.model.request.LoanRequest;
import com.me.library_service.model.response.ApiResponse;
import com.me.library_service.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@Valid @RequestBody LoanRequest loanRequest) {
        log.debug("[+] LoanController:checkout with request dto: {}", loanRequest);
        ApiResponse response = loanService.checkout(loanRequest);
        log.debug("[+] LoanController:checkout response: {}", response);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/checkin/{id}")
    public ResponseEntity<?> checkin(@PathVariable Long id) {
        log.debug("[+] LoanController:checkin with loan id: {}", id);
        ApiResponse response = loanService.checkin(id);
        log.debug("[+] LoanController:checkin response: {}", response);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<?> findAll(Pageable pageable) {
        log.debug("[+] LoanController:findAll with pageable: {}", pageable);
        ApiResponse response = loanService.findAll(pageable);
        log.debug("[+] LoanController:findAll response: {}", response);
        return ResponseEntity.ok().body(response);
    }
}
