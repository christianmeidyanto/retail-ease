package com.retailease.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
@Getter
@Setter
@Entity
@Table(name = "t_sales_transaction")
@AllArgsConstructor
@NoArgsConstructor
public class SalesTransaction {
    @Id
    @GenericGenerator(strategy = "uuid2", name = "system-uuid")
    @GeneratedValue(generator = "system-uuid")
    private String id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Column(name = "trans_date")
    private LocalDateTime transDate;

    @OneToMany(mappedBy = "salesTransaction", cascade = CascadeType.PERSIST)
    private List<SalesTransactionDetail> salesTransactionDetails;
}
