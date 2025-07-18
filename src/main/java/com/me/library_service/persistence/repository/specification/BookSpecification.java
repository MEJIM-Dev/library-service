package com.me.library_service.persistence.repository.specification;

import com.me.library_service.persistence.entity.Book;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookSpecification {

    public static Specification<Book> getCriteria(String title, String isbn, String publisher, LocalDate dateAddedToLibrary) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }
            if (isbn != null && !isbn.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("isbn"), isbn));
            }
            if (publisher != null && !publisher.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("publisher")), "%" + publisher.toLowerCase() + "%"));
            }
            if (dateAddedToLibrary != null) {
                predicates.add(criteriaBuilder.equal(root.get("dateAddedToLibrary"), dateAddedToLibrary));
            }

            // If no search parameters are provided, you might want to return all books
            // Or, if you want to enforce at least one parameter, you could return
            // criteriaBuilder.disjunction() (a predicate that always evaluates to false)
            // or throw an exception.
            if (predicates.isEmpty()) {
                // If no criteria, return a predicate that matches all (effectively no filter)
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
