package com.me.library_service.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "authors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Author extends BaseEntity{

    @Column(name = "name", nullable = false)
    private String name;

}
