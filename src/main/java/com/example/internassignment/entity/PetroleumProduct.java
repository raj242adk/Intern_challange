package com.example.internassignment.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PetroleumProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer year;
    private String petroleum_product;
    private Integer sale;
    private String country;


}
