const token = localStorage.getItem("token");
if(!token){
    location.href="/lu/login";
}
document.addEventListener('DOMContentLoaded', () => {
    const baseDisplay = document.getElementById("base-display");

baseDisplay.innerHTML=
`
<div class="container-fluid bg-light shadow-lg rounded">
            <div class="row d-flex flex-wrap align-items-center p-3 ps-4 justify-content-between">
                <div class="col-6 col-md-4 col-lg-3 d-flex gap-3 d-flex flex-wrap align-items-center" >
                    <div class="col-2 ">
                        <a href="https://example.com" class="link-image" target="_blank">
                            <i class="fas fa-home fa-2x"></i>
                        </a>
                        
                    </div>



                    <div class="col-8 d-flex align-items-center">
                        <div class="w-100">
                            <form class="example" id="search">
                                <select id="searchBy">
                                    <option value="" disabled selected>Search</option>
                                    <option value="Shops">Shops</option>
                                    <option value="Items">Items</option>
                                </select>
                                <button type="submit" class="col-2"><i class="fas fa-search fa-sm"></i></button>
                            </form>
                            
                        </div>
                        
                    </div>

                </div>

                <div class="d-none d-md-block col-2 d-lg-none text-center">
                    <img src="/static/visual/logo.png" class="img-fluid rounded shadow-md" style="height: 60px;">
                </div>
                <video width="" height="" autoplay loop muted class="d-none d-lg-block col-6 rounded-pill" style="height: 60px;">
                    <source src="/static/visual/main.mp4" type="video/mp4">
                    Your browser does not support the video tag.
                </video>

                <div class="col-6 col-md-4 col-lg-3 d-flex gap-2 d-flex flex-wrap align-items-center justify-content-end" >

                    <div class="col-2">
                        <a href="" class="link-image" target="_blank" id="cart" aria-haspopup="true">
                            <div style="position: relative; display: inline-block;" class="fs-3">
                                <i class="fas fa-cart-shopping fa-sm"></i>
                                <span id="notificationCount" style="position: absolute; top: -1px; right: -7px; background: red; color: white; border-radius: 50%; padding:1px 6px; font-size: 10px;">3</span>
                            </div>
                        </a>
                    </div>

                    <div class="col-2">
                        <a href="/#" class="link-image" target="_blank" id="head-edit" aria-haspopup="true">
                            <div style="position: relative; display: inline-block;" class="fs-3">
                                <i class="fa-solid fa-user-pen"></i>
                            </div>
                        </a>

                    </div>
                    <div class="col-2">
                        <form action="/lu/logout" method="post">

                                <div style="position: relative; display: inline-block;" class="fs-3">
                                    <button type="submit" class="btn btn-primary mb-2">
                                        <i class="fa-solid fa-right-from-bracket fa-sm"></i>
                                    </button>
                                </div>

                        </form>

                    </div>
                    <div class="col-2" >
                        <a href="/lu/profile" class="link-image" target="_blank" id="profile-image" aria-haspopup="true">
                            <img src="/static/visual/user.png" class="img-fluid" style="border-radius: 50%;" >
                        </a>
                    </div>

                </div>
            </div>
        </div>
`
})


const logoutButton = document.getElementById("logout");
logoutButton.addEventListener("click", e =>{
    e.preventDefault();
    
    localStorage.removeItem("token");
    location.href = "/lu/login";
})

const search = document.getElementById("search");
const searchBy = document.getElementById("searchBy");

search.addEventListener("submit", e=> {
e.preventDefault();

if (searchBy.value==="Shops"){
    location.href= "/lu/search/shops";
}
else if (searchBy.value==="Items"){
    location.href= "/lu/search/items";
}
})

fetch("/", {
    method: "GET",
    headers: {
        "Authorization": `Bearer ${token}`
    }
})
.then (response => {
    if (response.ok){
        return response.json();
    }
    else throw Error(response.statusText)
})
// 응답을 담긴 JWT 를 저장한다
.then(json => {

    // const headStore = document.getElementById("head-store");
    const cart =document.getElementById("cart");
    // if (!json.stringAuthorities.includes("ROLE_USER")){
    //     search.className="d-none";
    //     headStore.className="d-none";
    //     cart.className="d-none";
    // }
    // if (json.stringAuthorities.includes("ROLE_BUSINESS")){
    //     headStore.href="/lu/manage-items";
    // }
})
.catch(e =>{
    alert(e.message)
})