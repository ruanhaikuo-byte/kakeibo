package com.example.kakeibo.transaction;

import com.example.kakeibo.category.Category;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private Long amount;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(length = 100)
    private String memo;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount = amount; }
    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }
    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}