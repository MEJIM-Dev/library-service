package com.me.library_service.scheduler;

import com.me.library_service.config.NotificationProducer;
import com.me.library_service.model.notification.NotificationMessage;
import com.me.library_service.persistence.repository.LoanRepository;
import com.me.library_service.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoanScheduler {

    private final LoanRepository loanRepository;
//    private final NotificationProducer notificationProducer;
    private final MailService mailService;

    @Scheduled(cron = "${app.loan.cron}")
    public void notifyDueLoans() {
        log.info("[+] LoanScheduler: checking for loans due in 2 days");

        LocalDate targetDate = LocalDate.now().plusDays(2);

        loanRepository.findByExpectedReturnDateBeforeAndActualReturnDateIsNullAndNotified(targetDate.plusDays(1), false)
                .forEach(loan -> {
                    NotificationMessage message = NotificationMessage.builder()
                            .to(loan.getUser().getEmail())
                            .subject("Library Due Date Reminder")
                            .body("Dear user, the book '" + loan.getBook().getTitle() + "' is overdue. Please return it.")
                            .build();
//                    notificationProducer.sendNotification(message);
                    mailService.sendEmail(message);
                });
    }
}