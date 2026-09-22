package com.example.kakeibo.user;

import com.example.kakeibo.category.Category;
import com.example.kakeibo.category.CategoryRepository;
import com.example.kakeibo.category.CategoryType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       CategoryRepository categoryRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegisterForm form) {
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            throw new IllegalArgumentException("パスワードが一致しません");
        }
        if (userRepository.existsByEmail(form.getEmail())) {
            throw new EmailAlreadyExistsException("このメールアドレスは既に登録されています");
        }

        User user = new User();
        user.setEmail(form.getEmail());
        user.setPasswordHash(passwordEncoder.encode(form.getPassword()));
        User savedUser = userRepository.save(user);

        createDefaultCategories(savedUser.getId());

        return savedUser;
    }

    private void createDefaultCategories(Long userId) {
        List<String> expenseNames = List.of(
                "食費", "交通費", "日用品", "家賃", "水道光熱費",
                "通信費", "娯楽費", "医療費", "その他"
        );
        List<String> incomeNames = List.of("給料", "賞与", "その他");

        int order = 0;
        for (String name : expenseNames) {
            categoryRepository.save(newCategory(userId, name, CategoryType.EXPENSE, order++));
        }
        order = 0;
        for (String name : incomeNames) {
            categoryRepository.save(newCategory(userId, name, CategoryType.INCOME, order++));
        }
    }

    private Category newCategory(Long userId, String name, CategoryType type, int order) {
        Category category = new Category();
        category.setUserId(userId);
        category.setName(name);
        category.setType(type);
        category.setSortOrder(order);
        return category;
    }
}