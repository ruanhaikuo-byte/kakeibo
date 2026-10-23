package com.example.kakeibo.transaction;

import com.example.kakeibo.category.Category;
import com.example.kakeibo.category.CategoryRepository;
import com.example.kakeibo.user.User;
import com.example.kakeibo.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class TransactionController {
    private final TransactionService transactionService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public TransactionController(TransactionService transactionService,
                                 UserRepository userRepository,
                                 CategoryRepository categoryRepository) {
        this.transactionService = transactionService;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    // ===== 记账一览页 =====
    // 浏览器访问 GET /transactions 时，执行这个方法
    @GetMapping("/transactions")
    public String List(Authentication authentication, Model model) {
        // 第 1 步：取出当前登录用户的 id
        Long userId = getCurrentUserId(authentication);
        // 第 2 步：向 Service 要这个用户的全部记账
        List<Transaction> transactions = transactionService.findAllByUser(userId);
        // 第 3 步：放进 Model，页面里用 ${transactions} 取
        model.addAttribute("transactions", transactions);
        // 第 4 步：再放一份"分类 id → 分类名"的对照表，页面显示分类名用
        model.addAttribute("categoryNames", buildCategoryNameMap(userId));
        // 第 5 步：返回页面名，对应 templates/transactions/list.html
        return "transactions/list";

    }

    // 辅助方法：生成"分类 id → 分类名"的对照表
    private Map<Long, String> buildCategoryNameMap(Long userId) {
        Map<Long, String> map = new HashMap<>();
        for (Category category : categoryRepository.findByUserIdOrderBySortOrder(userId)) {
            map.put(category.getId(), category.getName());
        }
        return map;
    }

    // ===== 新增：显示表单 =====
// 浏览器访问 GET /transactions/new 时执行
    @GetMapping("/transactions/new")
    public String showCreateForm(Authentication authentication, Model model) {
        Long userId = getCurrentUserId(authentication);

        // 放一个空表单进去，页面的输入框会绑定到它上面
        // 名字 "transactionForm" 要和页面里 th:object 的名字一致
        model.addAttribute("transactionForm", new TransactionForm());

        // 放入分类下拉框、提交地址、页面标题
        prepareForm(model, userId, "/transactions", "記録を追加");

        // 对应 templates/transactions/form.html
        return "transactions/form";
    }
    @PostMapping("/transactions")
    public String create(@Valid TransactionForm transactionForm, BindingResult bindingResult,Model model, Authentication authentication){
        Long userId = getCurrentUserId(authentication);
        if (bindingResult.hasErrors()){
            prepareForm(model,userId,"/transactions", "記録を追加");
            return "transactions/form";
        }
        transactionService.create(
        userId,
        transactionForm.getCategoryId(),
        transactionForm.getAmount(),
        transactionForm.getTransactionDate(),
        transactionForm.getMemo()
        );
        return "redirect:/transactions";
    }

    // 辅助方法：表单页（新增、编辑共用）需要的数据
    private void prepareForm(Model model, Long userId, String formAction, String pageTitle) {
        // 当前用户的分类，按排序号排好，给下拉框用
        model.addAttribute("categories", categoryRepository.findByUserIdOrderBySortOrder(userId));
        // 表单提交到哪个地址
        model.addAttribute("formAction", formAction);
        // 页面标题
        model.addAttribute("pageTitle", pageTitle);
    }

    // 取出当前登录用户的 id
    // 参数 authentication：Spring Security 提供，里面记着"当前登录的是谁"
    private Long getCurrentUserId(Authentication authentication) {
        // 第 1 步：取出登录名。你的系统用邮箱登录，所以这里拿到的是邮箱
        String email = authentication.getName();
        //第 2 步：用邮箱去数据库查用户，结果装在 Optional（盒子）里
        Optional<User> optional = userRepository.findByEmail(email);
        // 第 3 步：盒子是空的，说明登录的用户在数据库里找不到
        // 正常情况不会发生，所以抛 IllegalStateException（表示"系统状态不对"）
        if (!optional.isPresent()) {
            throw new IllegalStateException("ログインユーザーが見つかりません");
        }
        // 第 4 步：取出用户，返回他的 id
        return optional.get().getId();
    }
}
