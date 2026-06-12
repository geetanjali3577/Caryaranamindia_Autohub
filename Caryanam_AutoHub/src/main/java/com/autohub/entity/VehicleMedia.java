package com.autohub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle-media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String fileType;

    private String filePath;

    private String mediaType;

    private LocalDateTime uploadedAt;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
}