const tokenBase = localStorage.getItem("token");
if(tokenBase===null || tokenBase===undefined){
    location.href="/lu/login";
}


const baseDisplay = document.getElementById("base-display");
baseDisplay.innerHTML=
`
    <div class="container-fluid bg-light shadow-lg rounded">
                <div class="row d-flex flex-wrap align-items-center p-2 ps-4 justify-content-between">
                    <div class="d-none d-sm-flex col-sm-6 col-md-4 gap-3 d-flex flex-wrap align-items-center" >
                        <div class="col-2 ">
                            <a href="/lu/malls" class="link-image" target="_blank">
                                <i class="fas fa-home fa-2x"></i>
                            </a>
                            
                        </div>
                        <div class="col-8 d-flex align-items-center">
                            <div class="w-100" >
                                <form class="example d-flex" id="search">
                                    <select id="searchBy" class="d-none d-lg-block">
                                        <option value="" disabled selected>Search</option>
                                        <option value="Shops">Search Shops</option>
                                        <option value="Items">Search Items</option>
                                    </select>
                                    <button type="submit" id="search-button" style="font-size: x-small;"><i class="fas fa-search fa-sm"></i></button>
                                </form>
                            </div>
                        </div>
                    </div>

                    
                    <a href="/lu/profile" class="d-none d-md-block col-2 d-lg-none text-center">
                        <img src="/static/visual/logo.png" class="img-fluid rounded shadow-md" style="height: 60px;">
                    </a>
                    <a href="/lu/profile" class="d-none d-lg-block col-4 text-center">
                        <video width="" height="" autoplay loop muted class="rounded" style="height: 60px;">
                            <source src="/static/visual/main.mp4" type="video/mp4">
                            Your browser does not support the video tag.
                        </video>
                    </a>

                    <div class="d-none d-sm-flex col-sm-6 col-lg-4 flex-wrap align-items-center justify-content-end" >
                        <div class="col-2">
                            <a href="/lu/manage-shops" class="link-image" id="manage-shop" aria-haspopup="true">
                                <div style="position: relative; display: inline-block;" class="fs-3">
                                    <i class="fa-solid fa-cart-arrow-down fa-sm"></i>
                                    <span id="shop-count" class="d-none" style="position: absolute; top: -1px; right: -7px; background: red; color: white; border-radius: 50%; padding:1px 6px; font-size: 10px;">3</span>
                                </div>
                            </a>
                        </div>
                    
                        <div class="col-2">
                            <a href="/lu/manage-items" class="link-image" id="list-item" aria-haspopup="true">
                                <div style="position: relative; display: inline-block;" class="fs-3">
                                    <i class="fa-solid fa-list"></i>
                                    <span id="sell-count" class="d-none" style="position: absolute; top: -1px; right: -7px; background: red; color: white; border-radius: 50%; padding:1px 6px; font-size: 10px;">3</span>
                                
                                </div>
                            </a>
                        </div>

                        <div class="col-2">
                            <a href="/lu/shop" class="link-image" id="head-store" aria-haspopup="true">
                                <div style="position: relative; display: inline-block;" class="fs-3">
                                    <i class="fas fa-store fa-sm"></i>
                                </div>
                            </a>
                        </div>

                        <div class="col-2">
                            <a href="/lu/order" class="link-image" id="cart" aria-haspopup="true">
                                <div style="position: relative; display: inline-block;" class="fs-3">
                                    <i class="fas fa-cart-shopping fa-sm"></i>
                                    <span id="buy-count" class="d-none" style="position: absolute; top: -1px; right: -7px; background: red; color: white; border-radius: 50%; padding:1px 6px; font-size: 10px;">3</span>
                                </div>
                            </a>
                        </div>

                        <div class="col-2 text-center" id="profile">
                            <a href="/lu/profile" class="link-image" aria-haspopup="true">
                                <img src="/static/visual/user.png" id="base-image" class="img-fluid w-50" style="border-radius: 50%;" >
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        <div class="d-flex justify-content-end d-none" id="profile-items">
            <div class="col-2 col-lg-1 text-bg-light p-2 rounded">
                <a class="text-decoration-none" href="/lu/edit"><i class="fa-solid fa-user-pen"></i> Edit</a><br>
                <a class="text-decoration-none" href="/#" id="logout"><i class="fa-solid fa-right-from-bracket fa-sm"></i> Logout</a>
            </div>
        </div>    
`;


const buyCount= document.getElementById("buy-count");
const sellCount= document.getElementById("sell-count");
const shopCount = document.getElementById("shop-count");

//Search


//profile
const profileItems = document.getElementById("profile-items");
const profile = document.getElementById("profile");
profile.addEventListener("click", e=>{
    e.preventDefault();
    if (profileItems.classList.contains("d-none")) {
        profileItems.classList.remove("d-none");
        profileItems.classList.add("d-flex", "justify-content-end");
    } else {
        profileItems.classList.remove("d-flex", "justify-content-end");
        profileItems.classList.add("d-none");
    }
})
//logout
const logoutButton = document.getElementById("logout");

logoutButton.addEventListener("click", e =>{
    e.preventDefault();
    localStorage.removeItem("token");
    localStorage.removeItem("authority")
    location.href = "/lu/home";
})



fetch("/", {
    method: "GET",
    headers: {
        "Authorization": `Bearer ${tokenBase}`
    }
})
.then (response => {
    if (response.ok){
        return response.json();
    } else if (response.status ===403){
        localStorage.removeItem("token");
        window.location.href = "/lu/login";
    }
    else throw Error("failed to fetch");
})
// 응답을 담긴 JWT 를 저장한다
.then(json => {
    localStorage.setItem("authority", json.stringAuthorities);
    const baseImg = document.getElementById("base-image");
    baseImg.src = json.profileImg;

    const searchButton = document.getElementById("search-button");
    const headStore = document.getElementById("head-store");
    const cart =document.getElementById("cart");
    const listItem = document.getElementById("list-item")
    const manageShop = document.getElementById("manage-shop");
    if (!json.stringAuthorities.includes("ROLE_USER")){
        searchButton.type="";
        searchButton.href="/lu/edit";
        headStore.className="d-none";
        cart.className="d-none";
    }
    if (!json.stringAuthorities.includes("ROLE_BUSINESS")){
        listItem.className="d-none";
    }
    if (!json.stringAuthorities.includes("ROLE_ADMIN")){
        manageShop.className="d-none";
    }

    if (json.stringAuthorities.includes("ROLE_ADMIN")){
        fetch("/manage-shops/base", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${tokenBase}`
            }
        })
        .then (response => {
            if (response.ok){
                return response.json();
            } else if (response.status ===403){
                localStorage.removeItem("token");
                window.location.href = "/lu/login";
            }
            else throw Error("failed to fetch");
        })
        // 응답을 담긴 JWT 를 저장한다
        .then(json => {
            let sum =0;
            json.forEach(shop=>{
                if (shop.status==="Pending open" ||
                     shop.status==="Request close"){
                    sum+=1;
                }
            })
            shopCount.innerText=sum;
            if (sum>0){
                shopCount.className="";
            }
        })
        .catch(e =>{
            alert(e.message)
        })

    }

    const search = document.getElementById("search");
    const searchBy = document.getElementById("searchBy");

    search.addEventListener("submit", e=> {
        e.preventDefault();
        if (!json.stringAuthorities.includes("ROLE_USER")){
            alert("Please register your information!!!")
            location.href="/lu/edit";
        } else {
            if (searchBy.value==="Shops"){
                location.href= "/lu/search/shops";
            }
            else if (searchBy.value==="Items"){
                location.href= "/lu/malls";
            }
        }
    })
    buyCount.innerText= json.numberOrder;
    if (json.numberOrder>0){
        buyCount.className="";
    }
    sellCount.innerText=json.numberSell;
    if (json.numberSell>0){
        sellCount.className="";
    }

    const orderTotal=document.getElementById("allOrder-payment");
    if (orderTotal) orderTotal.innerText=`${json.payment}₩`;
})
.catch(e =>{
    alert(e.message)
})





