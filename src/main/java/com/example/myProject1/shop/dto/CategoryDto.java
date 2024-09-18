package com.example.myProject1.shop.dto;

import com.example.myProject1.shop.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class CategoryDto {
    private Long id;
    private String name;
    public static CategoryDto dto(Category category){
        CategoryDto categoryDto = CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
        return categoryDto;
    }
}
