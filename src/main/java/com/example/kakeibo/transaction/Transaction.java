package com.example.kakeibo.transaction;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(nullable = false)
    private Long amount;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(length = 100)
    private String memo;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // JPA 要求必须有的空构造方法
    protected Transaction() {}

    // 真正会用到的构造方法，一次性把必填的值传进来
    public Transaction(Long userId, Long categoryId, Long amount,
                       LocalDate transactionDate, String memo) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.memo = memo;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount = amount; }

    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }

    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}