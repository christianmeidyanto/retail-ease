package com.retailease.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Builder(toBuilder = true)
@Getter
@Setter
@Entity
@Table(name = "t_sales_transaction_detail")
@AllArgsConstructor
@NoArgsConstructor
public class SalesTransactionDetail {
    @Id
    @GenericGenerator(strategy = "uuid2", name = "system-uuid")
    @GeneratedValue(generator = "system-uuid")
    private String id;

    @ManyToOne
    @JoinColumn(name = "sales_transaction_id",referencedColumnName = "id")
    private SalesTransaction salesTransaction;

    @ManyToOne
    @JoinColumn(name = "product_price_id")
    private ProductPrice productPrice;

    private Integer quantity;
}
