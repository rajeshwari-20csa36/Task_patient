package com.ust.service;

import com.ust.model.Patient;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService {
    private static final String FILE_PATH = "patients.csv";

    public List<Patient> getAllPatients() {
        try {
            return Files.readAllLines(Paths.get(FILE_PATH)).stream()
                    .skip(1)
                    .map(this::parsePatient)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public Optional<Patient> getPatientById(Long id) {
        return getAllPatients().stream()
                .filter(patient -> patient.getId().equals(id))
                .findFirst();
    }

    public Patient addPatient(Patient patient) {
        List<Patient> patients = getAllPatients();
        long nextId = patients.isEmpty() ? 1 :
                patients.stream()
                        .mapToLong(Patient::getId)
                        .max()
                        .orElse(0) + 1;

        patient.setId(nextId);
        patients.add(patient);
        savePatients(patients);
        return patient;
    }

    public boolean deletePatient(Long id) {
        List<Patient> patients = getAllPatients();

        boolean removed = patients.removeIf(patient -> patient.getId().equals(id));

        if (removed) {
            savePatients(patients);
        }
        return removed;
    }

    private void savePatients(List<Patient> patients) {
        try {
            List<String> lines = new ArrayList<>();
            lines.add("id,name,age,gender,dateOfBirth,email");

            lines.addAll(patients.stream()
                    .map(this::patientToCsvLine)
                    .toList());

            Files.write(Paths.get(FILE_PATH), lines);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private Patient parsePatient(String line) {
        String[] fields = line.split(",");
        Patient patient = new Patient();
        patient.setId(Long.parseLong(fields[0]));
        patient.setName(fields[1]);
        patient.setAge(Integer.parseInt(fields[2]));
        patient.setGender(fields[3]);
        patient.setDateOfBirth(LocalDate.parse(fields[4]));
        patient.setEmail(fields[5]);
        return patient;
    }

    private String patientToCsvLine(Patient patient) {
        return String.format("%d,%s,%d,%s,%s,%s",
                patient.getId(),
                patient.getName(),
                patient.getAge(),
                patient.getGender(),
                patient.getDateOfBirth(),
                patient.getEmail());
    }
}