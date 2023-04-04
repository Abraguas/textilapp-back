package com.tup.textilapp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private String description;
    private BigDecimal pricePerUnit;
    private String observations;
    private Integer stock;
    private String measurment;
    private String image;

    @ManyToOne(targetEntity = Brand.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private Brand brand;
    @ManyToOne(targetEntity = Unit.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    private Unit unit;
    @ManyToOne(targetEntity = Color.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "color_id", referencedColumnName = "id")
    private Color color;
    @ManyToOne(targetEntity = SubCategory.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "subcategory_id", referencedColumnName = "id")
    private SubCategory subCategory;
}
