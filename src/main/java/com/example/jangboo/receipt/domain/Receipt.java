package com.example.jangboo.recipt.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name="recipt")
public class Recipt {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipt_id")
    private Long id;

    @Column(name = "dept_id")
    private Long deptId;

    @Column(name = "appcode")
    private String appcode;

    @Column(name = "store")
    private String store;

    @Column(name = "ammount")
    private int amount;

    @Column(name = "transaction_date_time")
    private LocalDateTime transactionDate;

    @Column(name = "img_url")
    private String reciptImgUrl;

    @Builder
    public Recipt(Long deptId, String appcode, String store, int amount, LocalDateTime transactionDate, String reciptImgUrl) {
        this.deptId = deptId;
        this.appcode = appcode;
        this.store = store;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.reciptImgUrl = null;
    }

}
