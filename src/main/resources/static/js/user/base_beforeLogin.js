if (localStorage.getItem("token")) {
    location.href= "/lu/profile";
}

const baseDisplay = document.getElementById("base-display");
baseDisplay.innerHTML=
`
<div class="container-fluid bg-light shadow-lg rounded">
                <div class="row d-flex flex-wrap align-items-center p-2 ps-4 justify-content-between">
                    <div class="col-6 col-md-4 col-lg-3 d-flex gap-3 d-flex flex-wrap align-items-center" >
    
                        <div class="col-2">
                            <a href="/lu/home" class="link-image">
                                <i class="fas fa-home fa-2x"></i>
                            </a>
                        </div>
                        <div class="col-8 ">
                            <div class="d-flex align-items-center">
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
    
                    </div>
    
                    <a href="/lu/home" class="d-none d-md-block col-2 d-lg-none text-center">
                        <img src="/static/visual/logo.png" class="img-fluid rounded shadow-md" style="height: 60px;">
                    </a>
                    <a href="/lu/home" class="d-none d-lg-block col-4 text-center">
                        <video width="" height="" autoplay loop muted class="rounded" style="height: 60px;">
                            <source src="/static/visual/main.mp4" type="video/mp4">
                            Your browser does not support the video tag.
                        </video>
                    </a>
    
                    <div class="col-6 col-md-4 col-lg-3 " >
                        <div class="w-100 d-flex flex-wrap align-items-center justify-content-end gap-3">
                            <div class="col-3 text-center">
                                <a id="shop-click" href="/lu/login" class="link-image">
                                    <i class="fas fa-shop fa-lg"></i>
                                </a>
                            </div>
                            <div class="col-3 text-center">
                                <a href="/lu/login" class="link-image">
                                    <i class="fas fa-user-circle fa-lg" title="login"></i>
                                </a>
                            </div>
    
                            <div class="col-3 text-center">
                                <a href="/lu" class="link-image">
                                    <i class="fas fa-user-plus fa-lg" title="sign up"></i>
                                </a>
                            </div>
                        </div>
    
                        <div class="w-100 d-flex flex-wrap align-items-center justify-content-end gap-3">
                            <div class="col-3 text-center">
                                <a href="/lu/login" class="text-decoration-none" style="font-size: small;">shop</a>
                            </div>
                            <div class="col-3 text-center">
                                <a href="/lu/login" class="text-decoration-none" style="font-size: small;">log in</a>
                            </div>
    
                            <div class="col-3 text-center">
                                <a href="/lu" class="text-decoration-none" style="font-size: small;">sign in</a>
                            </div>
                        </div>
                    </div>
                </div>
    </div>
`;

const search = document.getElementById("search");
const searchBy = document.getElementById("searchBy");
const shopClick = document.getElementById("shop-click")
search.addEventListener("submit", e=> {
    e.preventDefault();
    alert("Please login your account first!!!")
    location.href="/lu/login";
})
shopClick.addEventListener("click", e=>{
    e.preventDefault();
    alert("Please login to your account first!!!");
    location.href="/lu/login";
})
    





