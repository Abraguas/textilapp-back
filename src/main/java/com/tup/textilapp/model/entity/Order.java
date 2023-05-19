package com.tup.textilapp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity userEntity;

    private Date date;
    @ManyToOne(targetEntity = OrderState.class)
    @JoinColumn(name = "state_id", referencedColumnName = "id")
    private OrderState state;

    private String observations;
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> details;
}
