package com.example.kakeibo.transaction;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class TransactionForm {
    @NotNull(message = "カテゴリーを選択してください")
    private Long categoryId;

    // 金额（必须填，且必须大于 0）
    @NotNull(message = "金額を入力してください")
    @Positive(message = "金額は1以上で入力してください")
    private Long amount;

    // 日期（浏览器的日期控件提交的格式是 2026-10-06）
    @NotNull(message = "日付を入力してください")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate transactionDate;

    // 备注（可以不填，最长 100 字）
    @Size(max = 100, message = "メモは100文字以内で入力してください")
    private String memo;

    // ===== getter / setter =====
    // Spring 填充表单靠 setter，页面和 Controller 读取靠 getter

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount = amount; }

    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }

    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }
}
