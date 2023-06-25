package com.tup.textilapp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "image_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String type;
    @Lob
    @Column(name = "image_data", length = 2097152)
    private byte[] imageData;
}
