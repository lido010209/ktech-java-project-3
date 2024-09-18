package com.example.myProject1.shop.service;

import com.example.myProject1.shop.dto.CategoryDto;
import com.example.myProject1.shop.entity.Category;
import com.example.myProject1.shop.repo.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepo categoryRepo;
    private static final String[] categories=
            {"Fashion", "Electronics", "Sports", "Food & Drink", "Books",""};

    public CategoryService(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
        for (String name: categories){
            if (!categoryRepo.existsByName(name)){
                Category category = Category.builder().name(name).build();
                CategoryDto.dto(categoryRepo.save(category));
            }
        }
    }
}
