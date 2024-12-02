package com.ust.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    private Long id;
    private String name;
    private int age;
    private String gender;
    private LocalDate dateOfBirth;
    private String email;

}