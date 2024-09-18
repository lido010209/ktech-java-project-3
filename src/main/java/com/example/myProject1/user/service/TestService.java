package com.example.myProject1.user.service;

import com.example.myProject1.item.entity.Item;
import com.example.myProject1.item.repo.ItemRepo;
import com.example.myProject1.status.Status;
import com.example.myProject1.status.StatusRepo;
import com.example.myProject1.shop.entity.Category;
import com.example.myProject1.shop.entity.Shop;
import com.example.myProject1.shop.repo.CategoryRepo;
import com.example.myProject1.shop.repo.ShopRepo;
import com.example.myProject1.user.dto.UserDto;
import com.example.myProject1.user.entity.Authority;
import com.example.myProject1.user.entity.UserEntity;
import com.example.myProject1.user.repo.AuthorityRepo;
import com.example.myProject1.user.repo.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    private final UserRepo userRepo;
    private final AuthorityRepo authorityRepo;
    private final PasswordEncoder encoder;
    private final StatusRepo statusRepo;
    private final CategoryRepo categoryRepo;
    private final ShopRepo shopRepo;
    private final ItemRepo itemRepo;

    public TestService(UserRepo userRepo, AuthorityRepo authorityRepo, PasswordEncoder encoder, StatusRepo statusRepo, CategoryRepo categoryRepo, ShopRepo shopRepo, ItemRepo itemRepo) {
        this.userRepo = userRepo;
        this.authorityRepo = authorityRepo;
        this.encoder = encoder;
        this.statusRepo = statusRepo;
        this.categoryRepo = categoryRepo;
        this.shopRepo = shopRepo;
        this.itemRepo = itemRepo;
        this.fixUser();
    }

    private void fixUser(){
        Authority authority1 = authorityRepo.findByAuthority("ROLE_VIEWER").orElseThrow();
        Authority authority2 = authorityRepo.findByAuthority("ROLE_USER").orElseThrow();
        Authority authority3 = authorityRepo.findByAuthority("ROLE_ADMIN").orElseThrow();
        Authority authority4 = authorityRepo.findByAuthority("ROLE_BUSINESS").orElseThrow();
        UserEntity admin = UserEntity.builder()
                .username("luna010209").password(encoder.encode("Luna@01"))
                .name("Luna Do").email("luna@gmail.com").phone("010-1234-3456")
                .nickname("lili").age(22)
                .profileImg("/static/visual/user.png")
                .build();
        admin.getAuthorities().add(authority3);
        admin.getAuthorities().add(authority1);
        admin.getAuthorities().add(authority2);
        admin.getAuthorities().add(authority4);
        UserDto.userDto(userRepo.save(admin));

        UserEntity user2 = UserEntity.builder()
                .username("lido010209").password(encoder.encode("Luna@01"))
                .name("DoLi").email("lido_1209@naver.com").phone("010-9876-1234")
                .nickname("lina").age(20)
                .profileImg("/static/visual/user.png")
                .build();
        user2.getAuthorities().add(authority1);
        user2.getAuthorities().add(authority2);
        user2.getAuthorities().add(authority4);
        user2 = userRepo.save(user2);
        Status status2 = statusRepo.findByName("Opened").orElseThrow();
        Status status1 = statusRepo.findByName("Reject").orElseThrow();
        Category category1 = categoryRepo.findByName("Fashion")
                .orElseThrow(()-> new IllegalArgumentException("This category is not available!!!"));
        Category category2 = categoryRepo.findByName("Electronics")
                .orElseThrow(()-> new IllegalArgumentException("This category is not available!!!"));
        Category category3 = categoryRepo.findByName("Sports")
                .orElseThrow(()-> new IllegalArgumentException("This category is not available!!!"));
        Category category4 = categoryRepo.findByName("Food & Drink")
                .orElseThrow(()-> new IllegalArgumentException("This category is not available!!!"));

        Shop shopAdmin = Shop.builder()
                .owner(admin)
                .status(status2)
                .category(category1)
                .name("Admin shop")
                .introduction("This shop is for testing")
                .build();
        shopRepo.save(shopAdmin);
        Shop shop = Shop.builder()
                .owner(user2)
                .status(status2)
                .category(category1)
                .name("luna shop")
                .introduction("Our store provides a lot of new phones and laptops")
                .build();
        shop = shopRepo.save(shop);

        Item item1 = Item.builder()
                .name("jacket")
                .stock(50)
                .price(2500)
                .description("This one keeps warm in the winter. It is trendy and fashionable")
                .image("/static/visual/shop/item1.png")
                .shop(shop)
                .build();
        Item item2 = Item.builder()
                .name("hat")
                .stock(35)
                .price(1000)
                .description("This one keeps warm in the winter. It is trendy and fashionable")
                .image("/static/visual/shop/item2.png")
                .shop(shop)
                .build();
        itemRepo.save(item1);
        itemRepo.save(item2);

        UserEntity user3 = UserEntity.builder()
                .username("Lido_010209").password(encoder.encode("Luna@01"))
//                .name("Luna").email("lina@kakao.com").phone("010-5965-1209")
                .profileImg("/static/visual/user.png")
                .build();
        user3.getAuthorities().add(authority1);
        UserDto.userDto(userRepo.save(user3));

        UserEntity user4 = UserEntity.builder()
                .username("luna01").password(encoder.encode("Luna@01"))
                .name("Luna").email("lala@gmail.com").phone("010-3245-1324")
                .nickname("lina").age(22)
                .profileImg("/static/visual/user.png")
                .build();
        user4.getAuthorities().add(authority1);
        user4.getAuthorities().add(authority2);
        userRepo.save(user4);

        UserEntity user5 = UserEntity.builder()
                .username("lina00").password(encoder.encode("Luna@01"))
                .name("Luna").email("lia@gmail.com").phone("010-3276-9543")
                .nickname("lina").age(22)
                .profileImg("/static/visual/user.png")
                .build();
        user5.getAuthorities().add(authority1);
        user5.getAuthorities().add(authority2);
        userRepo.save(user5);

        UserEntity buyer1 = UserEntity.builder()
                .username("buyer1").password(encoder.encode("Luna@01"))
                .name("buyer1").email("buyer1@gmail.com").phone("010-3276-1234")
                .nickname("buyer").age(22)
                .profileImg("/static/visual/user.png")
                .build();
        buyer1.getAuthorities().add(authority1);
        buyer1.getAuthorities().add(authority2);
        userRepo.save(buyer1);

        Category categoryUser = categoryRepo.findByName("").orElseThrow();
        Status status = statusRepo.findByName("Pending open").orElseThrow();
        Status status3 = statusRepo.findByName("Opened").orElseThrow();
        Shop shop1 = Shop.builder()
                .owner(buyer1)
                .status(status)
                .name("")
                .introduction("")
                .category(categoryUser)
                .build();
        shopRepo.save(shop1);
        UserEntity buyer2 = UserEntity.builder()
                .username("buyer2").password(encoder.encode("Luna@01"))
                .name("buyer2").email("buyer2@gmail.com").phone("010-35276-1234")
                .nickname("buyer").age(22)
                .profileImg("/static/visual/user.png")
                .build();
        buyer2.getAuthorities().add(authority1);
        buyer2.getAuthorities().add(authority2);
        userRepo.save(buyer2);
        Shop shop2 = Shop.builder()
                .owner(buyer2)
                .status(status3)
                .name("shop2")
                .introduction("Make you more beautiful and fashionable!!!")
                .category(category3)
                .build();
        shopRepo.save(shop2);

        UserEntity seller1 = UserEntity.builder()
                .username("seller1").password(encoder.encode("Luna@01"))
                .name("seller1").email("seller1@gmail.com").phone("010-356-1234")
                .nickname("seller").age(22)
                .profileImg("/static/visual/user.png")
                .build();
        seller1.getAuthorities().add(authority1);
        seller1.getAuthorities().add(authority2);
        seller1.getAuthorities().add(authority4);
        userRepo.save(seller1);
        Shop shop3 = Shop.builder()
                .owner(seller1)
                .status(status2)
                .category(category2)
                .name("seller shop")
                .introduction("Our store provides a lot of new phones and laptops")
                .build();
        shopRepo.save(shop3);
        UserEntity seller2 = UserEntity.builder()
                .username("seller2").password(encoder.encode("Luna@01"))
                .name("seller2").email("seller2@gmail.com").phone("01-356-1234")
                .nickname("seller").age(22)
                .profileImg("/static/visual/user.png")
                .build();
        seller2.getAuthorities().add(authority1);
        seller2.getAuthorities().add(authority2);
        seller2.getAuthorities().add(authority4);
        userRepo.save(seller2);
        Shop shop4 = Shop.builder()
                .owner(seller2)
                .status(status2)
                .category(category4)
                .name("lala shop")
                .introduction("Our store provides a lot of new phones and laptops")
                .build();
        shopRepo.save(shop4);


        Item item3 = Item.builder()
                .name("light jacket")
                .stock(40)
                .price(1500)
                .description("A lightweight jacket perfect for spring")
                .image("/static/visual/shop/item3.png")
                .shop(shop)
                .build();

        Item item20 = Item.builder()
                .name("potato chips")
                .stock(50)
                .price(2000)
                .description("Crispy and salty potato chips for a perfect snack")
                .image("/static/visual/shop/item20.png")
                .shop(shop4)
                .build();
        itemRepo.save(item20);

        Item item21 = Item.builder()
                .name("chocolate bar")
                .stock(40)
                .price(1500)
                .description("Delicious milk chocolate bar with a smooth texture")
                .image("/static/visual/shop/item21.png")
                .shop(shop4)
                .build();
        itemRepo.save(item21);

        Item item4 = Item.builder()
                .name("beanie")
                .stock(30)
                .price(1200)
                .description("A stylish and comfortable hat")
                .image("/static/visual/shop/item4.png")
                .shop(shop)
                .build();


        Item item25 = Item.builder()
                .name("wireless mouse")
                .stock(25)
                .price(3000)
                .description("Ergonomic wireless mouse with smooth scrolling and 2.4GHz connection")
                .image("/static/visual/shop/item25.png")
                .shop(shop3)
                .build();
        itemRepo.save(item25);

        Item item26 = Item.builder()
                .name("Bluetooth speaker")
                .stock(20)
                .price(7000)
                .description("Portable Bluetooth speaker with powerful sound and long battery life")
                .image("/static/visual/shop/item26.png")
                .shop(shop3)
                .build();
        itemRepo.save(item26);

        Item item22 = Item.builder()
                .name("gummy bears")
                .stock(35)
                .price(1800)
                .description("Chewy and fruity gummy bears in assorted flavors")
                .image("/static/visual/shop/item22.png")
                .shop(shop4)
                .build();
        itemRepo.save(item22);

        Item item5 = Item.builder()
                .name("winter coat")
                .stock(20)
                .price(3000)
                .description("A winter jacket with extra insulation")
                .image("/static/visual/shop/item5.png")
                .shop(shopAdmin)
                .build();

        Item item6 = Item.builder()
                .name("knit hat")
                .stock(25)
                .price(900)
                .description("A cozy hat for the cold weather")
                .image("/static/visual/shop/item6.png")
                .shop(shopAdmin)
                .build();

        Item item7 = Item.builder()
                .name("autumn jacket")
                .stock(60)
                .price(1800)
                .description("A trendy autumn jacket")
                .image("/static/visual/shop/item7.png")
                .shop(shopAdmin)
                .build();

        Item item27 = Item.builder()
                .name("smartphone charger")
                .stock(50)
                .price(1500)
                .description("Fast-charging smartphone charger with universal compatibility")
                .image("/static/visual/shop/item27.png")
                .shop(shop3)
                .build();
        itemRepo.save(item27);

        Item item28 = Item.builder()
                .name("4K UHD monitor")
                .stock(15)
                .price(25000)
                .description("High-resolution 4K UHD monitor with vibrant colors and ultra-thin design")
                .image("/static/visual/shop/item28.png")
                .shop(shop3)
                .build();
        itemRepo.save(item28);

        Item item23 = Item.builder()
                .name("cola soda")
                .stock(60)
                .price(1200)
                .description("Classic cola soda with a refreshing taste")
                .image("/static/visual/shop/item23.png")
                .shop(shop4)
                .build();
        itemRepo.save(item23);

        Item item29 = Item.builder()
                .name("gaming keyboard")
                .stock(10)
                .price(6000)
                .description("Mechanical gaming keyboard with customizable RGB backlighting")
                .image("/static/visual/shop/item29.png")
                .shop(shop3)
                .build();
        itemRepo.save(item29);


        Item item24 = Item.builder()
                .name("lemon soda")
                .stock(45)
                .price(1300)
                .description("Sparkling lemon soda with a zesty flavor")
                .image("/static/visual/shop/item24.png")
                .shop(shop4)
                .build();
        itemRepo.save(item24);

        Item item8 = Item.builder()
                .name("spring hat")
                .stock(45)
                .price(2200)
                .description("A fashionable spring hat")
                .image("/static/visual/shop/item8.png")
                .shop(shopAdmin)
                .build();

        Item item9 = Item.builder()
                .name("insulated jacket")
                .stock(10)
                .price(3200)
                .description("An insulated winter jacket")
                .image("/static/visual/shop/item9.png")
                .shop(shop)
                .build();

        Item item10 = Item.builder()
                .name("winter hat")
                .stock(28)
                .price(950)
                .description("A warm and cozy winter hat")
                .image("/static/visual/shop/item.png")
                .shop(shop2)
                .build();

        Item item11 = Item.builder()
                .name("all-season jacket")
                .stock(42)
                .price(1700)
                .description("A versatile jacket for all seasons")
                .image("/static/visual/shop/item.png")
                .shop(shop2)
                .build();

        Item item12 = Item.builder()
                .name("wool hat")
                .stock(33)
                .price(1300)
                .description("A stylish and warm wool hat")
                .image("/static/visual/shop/item.png")
                .shop(shop2)
                .build();

        Item item13 = Item.builder()
                .name("premium coat")
                .stock(15)
                .price(3100)
                .description("A premium winter coat")
                .image("/static/visual/shop/item.png")
                .shop(shop2)
                .build();

        Item item14 = Item.builder()
                .name("comfort ties")
                .stock(26)
                .price(850)
                .description("A comfortable and warm knit ties")
                .image("/static/visual/shop/item14.png")
                .shop(shopAdmin)
                .build();
        itemRepo.save(item1);
        itemRepo.save(item2);
        itemRepo.save(item3);
        itemRepo.save(item4);
        itemRepo.save(item5);
        itemRepo.save(item6);
        itemRepo.save(item7);
        itemRepo.save(item8);
        itemRepo.save(item9);
        itemRepo.save(item10);
        itemRepo.save(item11);
        itemRepo.save(item12);
        itemRepo.save(item13);
        itemRepo.save(item14);
        Item item15 = Item.builder()
                .name("leather wallet")
                .stock(15)
                .price(1200)
                .description("A sleek leather wallet with multiple compartments")
                .image("/static/visual/shop/item.png")
                .shop(shop2)
                .build();
        itemRepo.save(item15);

        Item item16 = Item.builder()
                .name("running shoes")
                .stock(30)
                .price(7500)
                .description("Lightweight running shoes for optimal performance")
                .image("/static/visual/shop/item16.png")
                .shop(shop)
                .build();
        itemRepo.save(item16);

        Item item17 = Item.builder()
                .name("backpack")
                .stock(12)
                .price(3200)
                .description("Durable and spacious backpack for everyday use")
                .image("/static/visual/shop/item.png")
                .shop(shop)
                .build();
        itemRepo.save(item17);

        Item item18 = Item.builder()
                .name("wireless earbuds")
                .stock(20)
                .price(4500)
                .description("High-quality wireless earbuds with noise cancellation")
                .image("/static/visual/shop/item.png")
                .shop(shopAdmin)
                .build();
        itemRepo.save(item18);

        Item item19 = Item.builder()
                .name("stainless steel water bottle")
                .stock(40)
                .price(1500)
                .description("Eco-friendly stainless steel water bottle to keep drinks hot or cold")
                .image("/static/visual/shop/item19.png")
                .shop(shop4)
                .build();
        itemRepo.save(item19);

    }
}
