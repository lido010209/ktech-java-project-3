# Ktech-spring-boot-project-3

# 실행

## 준비
1. Set up for running web by [Spring initialzr](https://start.spring.io/) with developer tools: **Spring Web**, **Security**, **Lombok**, **Jpa**, **ThymeLeaf** and **Jwt**.
2. After unzip folder, set up for file [build.gradle](build.gradle) with **runtimeOnly** to activate Sqlite. As well, create file [application.yaml](application.yaml).

## 방식 
## 1. 기본 과제

### 1.1. 사용자 인증 및 권한 처리

[User](src\main\java\com\example\myProject1\user) and [Token](src\main\java\com\example\myProject1\token)
    
    
- Create [entity](src\main\java\com\example\myProject1\user\entity),  [dto](src\main\java\com\example\myProject1\user\dto), and [repo](src\main\java\com\example\myProject1\user\repo). => Create **2** table `user` and `authority` with relationship of `@ManyToMany` to provide authority to user.
#### 1. Authentication:
- [dto](src\main\java\com\example\myProject1\user\dto): Using interface `UserDetails` to authenticate this account in system and provide **authority** 
```java
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDto implements UserDetails{
    private String username;
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return stringAuthorities.stream().map(SimpleGrantedAuthority::new).toList();
    }
}
```
- [service](src\main\java\com\example\myProject1\user\service): using interface `UserDetailsService` in class `CustomUDService` to fetch User
```java
public class CustomUDService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException
    {
        UserEntity user = userRepo.findByUsername(username).orElseThrow(
                ()-> new UsernameNotFoundException("Username does not exist!!!")
        );
        return UserDto.userDto(user);
    }
}
```

- [`AuthenticationFacade`](src\main\java\com\example\myProject1\user\AuthenticationFacade.java): to load user who is in login status.
```java
@Component
public class AuthenticationFacade {
    public Authentication authentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
```
- **Authority**: Including 4 authorities
```java
@Service
public class AuthorityService {
    private final AuthorityRepo authorityRepo;
    private static final String[] authorities =
            {"ROLE_USER", "ROLE_ADMIN", "ROLE_BUSINESS", "ROLE_VIEWER"};
}
```
- Whenever user is created by username(아이디) and password(비밀번호), this user will be provided `ROLE_VIEWER` authority. 

```java
public UserDto infoLogin(UserDto dto){
        
        Authority authority1 = authorityRepo.findByAuthority("ROLE_VIEWER").orElseThrow();

        newUser.getAuthorities().add(authority1);
        return UserDto.userDto(userRepo.save(newUser));
    }
```

#### 2. Get token

- [`TokenUtils`](src\main\java\com\example\myProject1\token\TokenUtils.java): This class to set token for user login, including `subject`, `issueAt`, `expiration` of that token.
```java
public TokenUtils(
            @Value("${jwt.secret}")
            String code
    ) {
        this.keySecret = Keys.hmacShaKeyFor(code.getBytes());
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(this.keySecret).build();
    }

    public String generateToken(UserDto dto){
        Instant now = Instant.now();
        Claims jwtClaims = Jwts.claims()
                .setSubject(dto.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(60*60*24)));
        return Jwts.builder()
                .setClaims(jwtClaims)
                .signWith(keySecret)
                .compact();
    }
``` 
- Using `@PostMapping` to fetch jwt(token) for user login.
```java
@PostMapping("issue")
    public ResponseDto token(
            @RequestBody
            RequestDto dto
    ){
        return tokenService.response(dto);
    }
```

#### 3. Filter Handler: for authenticate login user.

```java
@Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        
        String username = tokenUtils.parsetClaims(token).getSubject();
        UserDto user = (UserDto) udService.loadUserByUsername(username);
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        AbstractAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user,
                        user.getPassword(),
                        user.getAuthorities()
                );
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        filterChain.doFilter(request, response);
    }
```
#### 4. Providing authority 

- Only when users register for their account (회원가입), update their account with **email, phone, name, nickname** and **age**, they will be provided `ROLE_USER`
```java
public UserDto create(UserDto dto){
        String userId = new AuthenticationFacade().authentication().getName();
        
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAge(dto.getAge());
        user.setNickname(dto.getNickname());

        Authority authority1 = authorityRepo.findByAuthority("ROLE_USER").orElseThrow();
        user.getAuthorities().add(authority1);
        return UserDto.userDto(userRepo.save(user));
    }
```
- Update avatar for user.
```java
public UserDto updateImg(MultipartFile image){
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username does not exist"));

        String directory = "profile/"+user.getId()+"/";
        try{
            Files.createDirectories(Path.of(directory));
        } catch (IOException exception){
            throw new RuntimeException("Fail to upload file");
        }
        String fileName= image.getOriginalFilename();
        String[] eleFile= fileName.split("\\.");
        String extension = eleFile[eleFile.length-1];

        String path = directory+ "profile."+extension;
        try{
            image.transferTo(Path.of(path));
        } catch (IOException exception){
            throw new RuntimeException("Fail to upload file");
        }

        String url = "/static/"+user.getId()+"/profile."+extension;
        user.setProfileImg(url);
        return UserDto.userDto(userRepo.save(user));
    }
```



### 1.2. 쇼핑몰 운영하기

#### a. 쇼핑몰 개설: [shop](src\main\java\com\example\myProject1\shop)

- Create [`Category`](src\main\java\com\example\myProject1\shop\service\CategoryService.java)

```java
@Service
public class CategoryService {
    private final CategoryRepo categoryRepo;
    private static final String[] categories=
            {"Fashion", "Electronics", "Sports", "Food & Drink", "Books"};

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
```
#### b. Create shop (user)

- Create [status](src\main\java\com\example\myProject1\status) for shop register form of user: including **5** status (`"Pending open", "Opened", "Reject", "Confirm reject", "Request close", "Closed"`)

```java
@Service
public class StatusService {
    private final StatusRepo statusRepo;
    private final WebService webService;
    private static final String[] allStatus =
            {"Pending open", "Opened", "Reject", "Confirm reject", "Request close", "Closed"};
}

```

- [`ShopService`](src\main\java\com\example\myProject1\shop\service\ShopService.java): Once users input their full information with their nickname, name, age, email, phone, shop automatically is added with `Pending open` status.

```java
public UserDto create(UserDto dto){
        
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAge(dto.getAge());
        user.setNickname(dto.getNickname());

        Authority authority1 = authorityRepo.findByAuthority("ROLE_USER").orElseThrow();
        user.getAuthorities().add(authority1);
        user = userRepo.save(user);

        if (user.getShop()==null) {
            Category categoryUser = categoryRepo.findByName("").orElseThrow();
            Status status = statusRepo.findByName("Pending open").orElseThrow();
            Shop shop = Shop.builder()
                    .owner(user)
                    .status(status)
                    .category(categoryUser)
                    .name("")
                    .introduction("")
                    .build();
            shopRepo.save(shop);
        }
        return UserDto.userDto(user);
    }

```

- Alse, editing shop form is available for user to add more shop's information (like **name, description, category**). Whenever user update this form, status will be `Pending open`

```java
    @Transactional
    public ShopDto update(ShopDto dto){
        UserEntity user = webService.user();
        Shop shop = user.getShop();
        Status status = statusRepo.findByName("Pending open").orElseThrow();
        shop.setName(dto.getName());
        shop.setIntroduction(dto.getIntroduction());
        shop.setCategory(category);
        shop.setStatus(status);
        return ShopDto.dto(shopRepo.save(shop));
    }
```

- **Confirm shop registration** (after admin decide to open shop or not) or **in case user would like to close shop.**

```java
public ShopDto updateStatus(ShopDto dto){
        UserEntity user = webService.user();
        Shop shop = user.getShop();

        if (dto.getStatus().equals("Confirm reject")){
            Status status = statusRepo.findByName(dto.getStatus()).orElseThrow();
            shop.setStatus(status);
        }
        else if (dto.getStatus().equals("Request close")){
            Status status = statusRepo.findByName(dto.getStatus()).orElseThrow();
            status.setReason(dto.getReason());
            statusRepo.save(status);
            shop.setStatus(status);
        }
        return ShopDto.dto(shopRepo.save(shop));
    }
```

#### c. Manage shop (admin)
- [`ManageShopService`](src\main\java\com\example\myProject1\shop\service\ManageShopService.java): admin will confirm shop registration form of user. (including request open or close)

```java
public ShopDto updateStatus(Long id, ShopDto dto){
//        UserEntity user = webService.userAdmin();
        Shop shop = shopRepo.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("Shop is not found!!!")
        );
        if (dto.getStatus().equals("Opened")){
            Status approved = statusRepo.findByName("Opened").orElseThrow();
            shop.setStatus(approved);
            UserEntity user = shop.getOwner();
            webService.updateAuthority("ROLE_BUSINESS", user);
        }
        else if (dto.getStatus().equals("Reject")){
            Status reject = statusRepo.findByName("Reject").orElseThrow();
            shop.setStatus(reject);
            reject.setReason(dto.getReason());
        }
        else if (dto.getStatus().equals("Closed")){
            Status closed = statusRepo.findByName("Closed").orElseThrow();
            shop.setStatus(closed);
            UserEntity user = shop.getOwner();
            webService.removeAuthority("ROLE_BUSINESS", user);
        }
        return ShopDto.dto(shopRepo.save(shop));
    }
```

#### d. Create items for shop [`ManageItemService.java`](src\main\java\com\example\myProject1\item\service\ManageItemService.java) 

- [`WebService`](src\main\java\com\example\myProject1\service\WebService.java): For getting user login with authority of `ROLE_BUSINESS`.

```java
@Transactional
    public UserEntity userBusiness(){
        String username = new AuthenticationFacade().authentication().getName();
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username does not exist"));
        Authority authority = authorityRepo.findByAuthority("ROLE_BUSINESS").orElseThrow();
        if (!user.getAuthorities().contains(authority))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You do not have this authority!!!");
        return user;
    }
```
- Items are created with building of **items' name, the quantity of item in stock, the price, default image, description**, and `Shop` is also got from `user` 
```java
public ItemDto create(ItemDto dto){
        UserEntity user = webService.userBusiness();
        Shop shop = user.getShop();
        Item item = Item.builder()
                .name(dto.getName())
                .stock(dto.getStock())
                .price(dto.getPrice())
                .image("/static/visual/shop/item.png")
                .description(dto.getDescription())
                .shop(shop)
                .build();
        return ItemDto.dto(itemRepo.save(item));

    }
```

- Update info or image of items, or delete items is allowed.

```java
@Transactional
    public ItemDto update(int itemIdx, ItemDto dto){
        UserEntity user = webService.userBusiness();
        Shop shop = user.getShop();
        if (itemIdx>= shop.getItems().size())
            throw new IllegalArgumentException("Item is not found!!!");
        Item item = shop.getItems().get(itemIdx);
        item.setName(dto.getName());
        item.setPrice(dto.getPrice());
        item.setStock(dto.getStock());
        item.setDescription(dto.getDescription());
        return ItemDto.dto(itemRepo.save(item));
    }

    @Transactional
    public ItemDto updateImg(int itemIdx, MultipartFile image){
        String path = directory+ "item."+extension;
        try{
            image.transferTo(Path.of(path));
        } catch (IOException exception){
            throw new RuntimeException("Fail to upload file");
        }

        String url = String.format("/static/%s/item.%s",
                item.getId(), extension);

        item.setImage(url);
        return ItemDto.dto(itemRepo.save(item));
    }

    @Transactional
    public void delete(int itemIdx){
        UserEntity user = webService.userBusiness();
        Shop shop = user.getShop();
        if (itemIdx>= shop.getItems().size())
            throw new IllegalArgumentException("Item is not found!!!");
        Item item = shop.getItems().get(itemIdx);
        itemRepo.delete(item);
        shop.getItems().remove(item);
        shopRepo.save(shop);
    }
```

### 1.3. 주문

- [`Order.java`](src\main\java\com\example\myProject1\order\entity\Order.java): fields in `Order`

```java
@Table(name = "customer_order")
public class Order extends BaseEntity {
    private Integer quantity;
    private Integer total;
    @ManyToOne
    private Status status;
    @ManyToOne
    private UserEntity buyer;
    @ManyToOne
    private Item item;
}
```
- [`StatusService.java`](src\main\java\com\example\myProject1\status\StatusService.java): Having 3 status for one order 
    - `Ordered`: Buyers complete their order (Assume that payment is completed as well)
    - `Paid`: After seller confirm the amount of money that buyer paid, they will update status order `Paid`.
    - `Cancel order`: Before seller confirm order, buyer is allowed to cancel their order.

```java
@Service
public class StatusService {
    private static final String[] allStatus =
            {"Ordered", "Paid", "Cancel order"};
}
```

#### a. Create order (Buyer) [`BuyerOrderService.java`](src\main\java\com\example\myProject1\order\service\BuyerOrderService.java)
- Whenever order is created, the status of order is set up as `Ordered`.

```java
@Transactional
    public OrderDto create(Long itemId, OrderDto dto){
        
        if (item.getStock()<dto.getQuantity())
            throw new IllegalArgumentException("Sorry, the quantity of item is not enough!!!");

        // Calculate the total bill
        Integer total = item.getPrice()* dto.getQuantity();

        // Set status
        Status status = statusRepo.findByName("Ordered")
                .orElseThrow(()-> new IllegalArgumentException("Invalid status"));

        // Calculate the total bill
        Integer total = item.getPrice()* dto.getQuantity();

        Order order = Order.builder()
                .quantity(dto.getQuantity())
                .total(total)
                .buyer(buyer)
                .item(item)
                .status(status)
                .build();

        order = orderRepo.save(order);
        buyer.getOrders().add(order);
        webService.calculatePayment();
        return OrderDto.dto(order);
    }
```
- In addition, I also calculate the total bill for one order.
- For one user, I also set `payment` for total amount of money in all orders
```java
@Table(name = "user")
public class UserEntity extends BaseEntity {
    private Integer payment;  
}
```

- [`webService.calculatePayment();`](src\main\java\com\example\myProject1\service\WebService.java)

```java
@Transactional
    public UserEntity calculatePayment(){
        String username = new AuthenticationFacade().authentication().getName();
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username does not exist"));
        Integer sum =0;
        if (!user.getOrders().isEmpty()) {
            for (Order order : user.getOrders()) {
                if (order.getStatus().getName().equals("Ordered")) {
                    sum += order.getTotal();
                }
            }
        }
        user.setPayment(sum);
        user = userRepo.save(user);
        return user;
    }
```

#### b. Cancel order (Buyer) [`BuyerOrderService.java`](src\main\java\com\example\myProject1\order\service\BuyerOrderService.java)

```java
    @Transactional
    public OrderDto cancelOrder (int orderIdx){
        UserEntity buyer = webService.user();
        if (orderIdx>= buyer.getOrders().size())
            throw new IllegalArgumentException("This order does not exist!!!");

        Order order = buyer.getOrders().get(orderIdx);
        // status cancel
        Status status = statusRepo.findByName("Cancel order").orElseThrow();
        order.setStatus(status);
        order = orderRepo.save(order);
        buyer.getOrders().remove(order);
        webService.calculatePayment();
        return OrderDto.dto(orderRepo.save(order));
    }
```
#### c. Confirm order (Seller) [`SellerOrderService.java`](src\main\java\com\example\myProject1\order\service\SellerOrderService.java)

```java
@Transactional
    public OrderDto confirmOrder(int itemIdx, int orderIdx){
        UserEntity seller = webService.userBusiness();
        Shop shop = seller.getShop();
        if (itemIdx>= shop.getItems().size())
            throw new IllegalArgumentException("Item is not found!!!");
        Item item = shop.getItems().get(itemIdx);
        if (orderIdx>= item.getOrders().size())
            throw new IllegalArgumentException("Order is not found!!!");
        Order order = item.getOrders().get(orderIdx);
        UserEntity buyer = order.getBuyer();
        if (order.getStatus().getName().equals("Cancel order")) {
            orderRepo.delete(order);
            buyer.getOrders().remove(order);
            webService.calculatePayment();
            return OrderDto.dto(order);
        }

        buyer.setPayment(buyer.getPayment() - order.getTotal());
        userRepo.save(buyer);

        item.setStock(item.getStock()-order.getQuantity());
        itemRepo.save(item);

        // Status
        Status status = statusRepo.findByName("Paid").orElseThrow();

        order.setStatus(status);
        order= orderRepo.save(order);
        buyer.getOrders().remove(order);
        webService.calculatePayment();
        return OrderDto.dto(order);
    }
```


### 1.4. 검색

#### a. Search shops

##### 1. 조건 없이 조회할 경우, 가장 최근에 거래가 있었던 쇼핑몰 순서로 조회된다.

- Using `@Query` in `OrderRepo` for get `List<Shop>` following the latest transaction of buyer. (Except for `Cancel order`)

```java
    @Query("SELECT o.item.shop FROM Order o WHERE o.buyer = :buyer AND o.status.name != 'Cancel order' ORDER BY o.createAt DESC")
    List<Shop> shopsOfBuyer(@Param("buyer") UserEntity buyer);
```

- [`SearchService`](src\main\java\com\example\myProject1\search\SearchService.java)
    - Get all shops that buyer did transaction with.
    - Then, get the other shops from `ShopRepo` in `Opened` status and that shop is not the one of searching user.

```java
public List<ShopDto> searchNoCondition(){
        List<ShopDto> shopDtos = new ArrayList<>();
        UserEntity user = webService.user();
        Set<Shop> shops = new LinkedHashSet<>();
        for (Shop shop: orderRepo.shopsOfBuyer(user)){
            shops.add(shop);
        }
        for (Shop shop: shopRepo.findAll()){
            if (shop.getStatus().getName().equals("Opened") && !user.getShop().equals(shop)){
                shops.add(shop);
            }
        }
        for (Shop shop: shops){
            shopDtos.add(ShopDto.dto(shop));
        }

        return shopDtos;
    }
```


##### 2. 이름, 쇼핑몰 분류를 조건으로 쇼핑몰을 검색할 수 있다.
```java
List<Shop> findByOwner(UserEntity owner);

    @Query("SELECT s FROM Shop s WHERE s.status.name = 'Opened' AND s.name LIKE %:keyword%")
    List<Shop> findByNameContaining(@Param("keyword") String keyword);

    @Query("SELECT s FROM Shop s WHERE s.status.name = 'Opened' AND s.category.name = :category")
    List<Shop> findByCategory(@Param("category") String category);

    @Query("SELECT s FROM Shop s WHERE s.status.name = 'Opened' AND s.name LIKE %:keyword% AND s.category.name = :category")
    List<Shop> findByNameAndCategory(@Param("keyword") String keyword, @Param("category") String category);
```

```java
public List<ShopDto> searchByName(String keyWord){
        List<ShopDto> shops = new ArrayList<>();
        UserEntity user = webService.user();

        for (Shop shop: shopRepo.findByNameContaining(keyWord)){
            if (!user.getShop().equals(shop))
                shops.add(ShopDto.dto(shop));
        }

        return shops;
    }

    public List<ShopDto> searchByCategory(String category){
        List<ShopDto> shops = new ArrayList<>();
        UserEntity user = webService.user();

        for (Shop shop: shopRepo.findByCategory(category)){
            if (!user.getShop().equals(shop))
                shops.add(ShopDto.dto(shop));
        }
        return shops;
    }

    public List<ShopDto> searchByNameAndCategory(String keyWord, String category){
        List<ShopDto> shops = new ArrayList<>();
        UserEntity user = webService.user();
        for (Shop shop: shopRepo.findByNameAndCategory(keyWord, category)){
            if (!user.getShop().equals(shop))
                shops.add(ShopDto.dto(shop));
        }
        return shops;
    }
```

#### b. Search items

##### 1. 이름, 가격 범위를 기준으로 상품을 검색할 수 있다. (Buyer)

1. In all shop
    - `@Query` in [`ItemRepo`](src\main\java\com\example\myProject1\item\repo\ItemRepo.java)

    ```java
        @Query("SELECT i FROM Item i WHERE i.name LIKE %:keyword%")
        List<Item> findByNameContaining(@Param("keyword") String keyword);

        @Query("SELECT i FROM Item i WHERE i.price BETWEEN :price1 AND :price2")
        List<Item> findByPriceLimit(@Param("price1") Integer price1, @Param("price2") Integer price2);

        @Query("SELECT i FROM Item i WHERE i.name LIKE %:keyword% AND i.price BETWEEN :price1 AND :price2")
        List<Item> findByNameAndPrice(
            @Param("keyword") String keyword,
            @Param("price1") Integer price1, 
            @Param("price2") Integer price2
            );  

    ```

    - Search items in [`SearchService`](src\main\java\com\example\myProject1\search\SearchService.java): except for all items in shop of searching user (in case that user has her.his own shop)

    ```java
    public List<ItemDto> searchItemsByName (String keyword){
        List<ItemDto> dtos = new ArrayList<>();
        UserEntity user =webService.user();

        for (Item item: itemRepo.findByNameContaining(keyword)){
            if (!user.getShop().getItems().contains(item)){
                dtos.add(ItemDto.dto(item));
            }
        }
        return dtos;
    }

    public List<ItemDto> searchItemsByPrice (Integer price1, Integer price2){
        List<ItemDto> dtos = new ArrayList<>();
        UserEntity user =webService.user();
        for (Item item: itemRepo.findByPriceLimit(price1, price2)){
            if (!user.getShop().getItems().contains(item)){
                dtos.add(ItemDto.dto(item));
            }
        }
        return dtos;
    }
    public List<ItemDto> searchItemsByNameAndPrice (Integer price1, Integer price2, String keyword){
        List<ItemDto> dtos = new ArrayList<>();
        UserEntity user =webService.user();

        for (Item item: itemRepo.findByNameAndPrice(keyword, price1, price2)){
            if (!user.getShop().getItems().contains(item)){
                dtos.add(ItemDto.dto(item));
            }
        }
        return dtos;
    }
    ```
    

2. In one shop (After buyer search shop they want)

```java
// find Item in another shop (Buyer search)
    public List<ItemDto> searchByNameInShop (String keyword, Long id){
        List<ItemDto> dtos = new ArrayList<>();
        Shop shop = shopRepo.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("Shop does not exist!!!")
        );

        for (Item item: itemRepo.findByNameContainingInShop(keyword, shop)){
            dtos.add(ItemDto.dto(item));
        }
        return dtos;
    }

    public List<ItemDto> searchByPriceInShop (Integer price1, Integer price2, Long id){
        List<ItemDto> dtos = new ArrayList<>();
        Shop shop = shopRepo.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("Shop does not exist!!!")
        );

        for (Item item: itemRepo.findByPriceInShop(price1, price2, shop)){
            dtos.add(ItemDto.dto(item));
        }
        return dtos;
    }

    public List<ItemDto> searchByNameAndPriceInShop (String keyword, Integer price1, Integer price2, Long id){
        List<ItemDto> dtos = new ArrayList<>();
        Shop shop = shopRepo.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("Shop does not exist!!!")
        );

        for (Item item: itemRepo.findByNameAndPriceInShop(keyword, price1, price2, shop)){
            dtos.add(ItemDto.dto(item));
        }
        return dtos;
    }

```

##### 2. 조회되는 상품이 등록된 쇼핑몰에 대한 정보가 함께 제공되어야 한다. (Seller)

```java
    public List<ItemDto> searchByNameInShop (String keyword){
        List<ItemDto> dtos = new ArrayList<>();
        UserEntity user = webService.userBusiness();
        Shop shop = user.getShop();

        for (Item item: itemRepo.findByNameContainingInShop(keyword, shop)){
            dtos.add(ItemDto.dto(item));
        }
        return dtos;
    }

    public List<ItemDto> searchByPriceInShop (Integer price1, Integer price2){
        List<ItemDto> dtos = new ArrayList<>();
        UserEntity user = webService.userBusiness();
        Shop shop = user.getShop();

        for (Item item: itemRepo.findByPriceInShop(price1, price2, shop)){
            dtos.add(ItemDto.dto(item));
        }
        return dtos;
    }

    public List<ItemDto> searchByNameAndPriceInShop (String keyword, Integer price1, Integer price2){
        List<ItemDto> dtos = new ArrayList<>();
        UserEntity user = webService.userBusiness();
        Shop shop = user.getShop();

        for (Item item: itemRepo.findByNameAndPriceInShop(keyword, price1, price2, shop)){
            dtos.add(ItemDto.dto(item));
        }
        return dtos;
    }
```

### 1.5. [`SecurityConfig`](src\main\java\com\example\myProject1\SecurityConfig.java) and [`PasswordConfig.java`](src\main\java\com\example\myProject1\PasswordConfig.java)

#### a. [`SecurityConfig`](src\main\java\com\example\myProject1\SecurityConfig.java) 
```java
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->{
                    // front-end
                    auth.requestMatchers("/error", "/lu/**", "/lu/login", "/static/**").permitAll();

                    // backend
                    auth.requestMatchers("/token/issue", "/token/forgot-password").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/").permitAll();
                    auth.requestMatchers("/").hasRole("VIEWER");
                    auth.requestMatchers("/avatar", "/malls").hasRole("VIEWER");

                    auth.requestMatchers("/malls/**", "/search/**").hasRole("USER");
                    auth.requestMatchers("/manage-items/**").hasRole("BUSINESS");
                    auth.requestMatchers("/shops/**").hasRole("USER");
                    auth.requestMatchers("/manage-shops/**").hasRole("ADMIN");
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(
                        new TokenFilterHandler(
                                tokenUtils,
                                udService
                        ),
                        AuthorizationFilter.class
                )
        ;
        return http.build();
    }
```

#### b. [`PasswordConfig.java`](src\main\java\com\example\myProject1\PasswordConfig.java)
```java
@Configuration
public class PasswordConfig {
    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
}
```

## 2.도전 과제

### 2.1. Design HTML, CSS

[**All template of html**](src\main\resources\templates)
1. [`home.html`](src\main\resources\templates\home.html): The home page of website
2. [user](src\main\resources\templates\user)
3. [shop](src\main\resources\templates\shop)
4. [item](src\main\resources\templates\item)
5. [order](src\main\resources\templates\order)
6. [search](src\main\resources\templates\search)

> In each folder, there might be some folder that defines which screen is the one of **admin**, **seller** or **buyer**, **non-active user** ...
[**All image and video for website**](src\main\resources\static\visual)


### 2.2. Javascript (Fetch API to connect backend and frontend)

1. In java

- I created 4 view cotroller for 4 types of authorities. 
    - [`AdminViewController.java`](src\main\java\com\example\myProject1\AdminViewController.java)
    ```java
    @Controller
    @RequestMapping("lu")
    public class AdminViewController {
        @GetMapping("manage-shops")
        public String allShop(){
            return "shop/admin/home";
        }
        @GetMapping("manage-shops/{id}")
        public String oneShop(){
            return "shop/admin/read";
        }
    }
    ```

    - [`BusinessViewController.java`](src\main\java\com\example\myProject1\BusinessViewController.java)
    ```java
    @Controller
    @RequestMapping("/lu")
    public class BusinessViewController {
        @GetMapping("manage-items")
        public String allItem(){
            return "item/home";
        }
        @GetMapping("manage-items/create")
        public String createItem(){
            return "item/create";
        }
        @GetMapping("manage-items/{idx}")
        public String oneItem(){
            return "item/update";
        }

        @GetMapping("manage-items/{idx}/manage-orders")
        public String allOrder(){
            return "order/user-seller/home";
        }
        @GetMapping("manage-items/{itemIdx}/manage-orders/{orderIdx}")
        public String oneOrder(){
            return "order/user-seller/read";
        }
    }
    ```
    - [`UserViewController.java`](src\main\java\com\example\myProject1\UserViewController.java)

    ```java
    @Controller
    @RequestMapping("lu")
    public class UserViewController {
        @GetMapping("profile")
        public String profile(){
            return "user/profile";
        }

        @GetMapping("shop")
        public String createShop(){
            return "shop/user-shop/manage-shop";
        }

        @GetMapping("malls")
        public String mall(){
            return "item/mall/home.html";
        }
        @GetMapping("malls/{itemId}")
        public String orderCreate(){
            return "order/user-buyer/create";
        }
        @GetMapping("order")
        public String allOrder(){
            return "order/user-buyer/allOrder";
        }
        @GetMapping("order/{orderIdx}")
        public String cancelOrder(){
            return "order/user-buyer/cancelOrder";
        }

        @GetMapping("search/shops")
        public String searchShops(){
            return "search/shop";
        }
        @GetMapping("search/shops/{id}/items")
        public String searchOneShop(){
            return "search/item";
        }
        @GetMapping("search/items")
        public String searchItems(){
            return "search/item";
        }
    }
    ```

    - [`NonUserViewController.java`](src\main\java\com\example\myProject1\NonUserViewController.java)
    ```java
    @Controller
    @RequestMapping("lu")
    public class NonUserViewController {
        @GetMapping("home")
        public String home(){
            return "home";
        }

        @GetMapping
        public String createAccount(){
            return "user/create";
        }

        @GetMapping("login")
        public String login(){
            return "user/login";
        }

        @GetMapping("edit")
        public String register(){
            return "user/register";
        }
    }
        
    ```

2. In javascripts

- [js](src\main\resources\static\js): Each page of HTML includes one file `.js` respectively and `base.js` file
    
    1. [`base.js`](src\main\resources\static\js\base.js)
    2. [user](src\main\resources\static\js\user)
    3. [shop](src\main\resources\static\js\shop)
    4. [item](src\main\resources\static\js\item)
    5. [order](src\main\resources\static\js\order)
    6. [search](src\main\resources\static\js\search)
  

## 기술
- Using [Spring boot](https://start.spring.io/) to supporting on running web and SQL
- Using [IntelliJ]() app to create project
- Using java 8.0
- Using [Vscode]() to create `.js`, `.html` confirm data of db.sqlite.
- Using [Bootstrap](https://getbootstrap.com/) to support file `.html` instead of `.css`
- Using [Font awesome](https://fontawesome.com/) to create icon like `search`
- Using [Canva](https://www.canva.com/) to create video to display in `.html`.

## 고민했던 내용

- `shop` 생성 때 사용자 한명 씩이 여러 샵을 만들 수 있는지 고민했습니다.
- 파일, 그래스 어떻게 깔끔하게 정리할 수 있는지 고민입니다.
- 화면 량이 많이 있습니다.


## 완수한 요구사항

### Self Checklist

#### 1. 필수 과제
- [x] 사용자 인증 및 권한 처리
- [x] 쇼핑몰 운영하기

#### 2. 도전 과제
- [x] HTML, CSS, JS를 활용하여 완성된 백엔드를 활용하는 클라이언트를 만들자.

#### 3. Postman collection

[postman_collection](postman_collection\Project.postman_collection.json)