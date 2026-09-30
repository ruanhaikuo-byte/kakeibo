package com.example.kakeibo.transaction;

import com.example.kakeibo.category.Category;
import com.example.kakeibo.category.CategoryRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Optional;

// @Service 贴在类的正上方，是在告诉 Spring：
// "这个类是一个业务处理类，请你在项目启动的时候，
//  自动帮我创建一个它的实例，并且托管起来"
// 这样以后别的类（比如 Controller）需要用到 TransactionService 的时候，
// 不用自己 new 一个出来，Spring 会自动把现成的实例塞给它
@Service
public class TransactionService {
    // 声明一个字段，类型是 TransactionRepository
    // private 表示这个字段只能在 TransactionService 内部使用，外面访问不到
    // final 表示这个字段一旦被赋值，以后就不能再改指向别的对象
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }
    // 新增一笔记账的方法
    public void create(Long userId, Long categoryId , Long amount, LocalDate transactionDate, String memo){
    if (amount == null || amount <= 0){
        //判断传的参数是否合法
        throw new IllegalArgumentException("金額は正の数で入力してください");
    }
    // 用 categoryId 去数据库查一下这个分类存不存在
    // findById 返回的类型不是 Category 本身，而是 Optional<Category>
    // 意思是"这次查询，结果可能是有，也可能是没有"
    Category category = categoryRepository.findById(categoryId).
            orElseThrow(() -> new IllegalArgumentException("カテゴリーが見つかりません"));

    // 查到了分类之后，还要再确认一件事：这个分类是不是真的属于当前登录的这个用户
    // category.getUserId() 是分类实际的所有者
    // userId 是当前登录的这个人
    // 如果两者对不上，说明有人想借用别人的分类
    if(!category.getUserId().equals(userId)){
        throw new IllegalArgumentException("不正なカテゴリーです");
    }

    //将获取到的值都保存到transaction实体类
    Transaction transaction = new Transaction(userId, categoryId, amount, transactionDate, memo);
    transactionRepository.save(transaction);
    }

    // 修改一笔已有的记账
    public void update(Long userId, Long transactionId, Long categoryId, Long amount, LocalDate transactionDate, String meno){
        // 第一步：用 transactionId 去数据库查这条记账记录
        // 跟之前查分类是一样的套路，返回的也是一个"盒子"（Optional）
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);
    }

    //删除记录
    public void delete(Long userId, Long transactionId){
    //检查记录是否存在
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);
        Transaction transaction;

    if(transactionOptional.isPresent()){
        transaction = transactionOptional.get();
    }else {
        throw new IllegalArgumentException("記録が見つかりません");
    }
    //确认这条记录是不是当前登入用户自己的
    if (!transaction.getUserId().equals(userId) ) {
         throw new IllegalArgumentException("不正な操作です");
    }
    //删除语句
     transactionRepository.delete(transaction);
}










}
