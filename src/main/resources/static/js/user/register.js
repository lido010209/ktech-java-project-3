const token = localStorage.getItem("token");
if(!token){
    location.href="/lu/login";
}

//Search
const search = document.getElementById("search");
const searchBy = document.getElementById("searchBy");



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


// form-register

const nicknameInput = document.getElementById("nickname");
const nameInput = document.getElementById("name");
const ageInput = document.getElementById("age");
const emailInput = document.getElementById("email");
const phoneInput = document.getElementById("phone");
const avatarLink = document.querySelector("#profile-image img");
        
const buyCount= document.getElementById("buy-count");
const sellCount= document.getElementById("sell-count");
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
    const baseImg = document.getElementById("base-image");
    baseImg.src = json.profileImg;

    const searchButton = document.getElementById("search-button");
    const headStore = document.getElementById("head-store");
    const cart =document.getElementById("cart");
    const listItem = document.getElementById("list-item")
    if (!json.stringAuthorities.includes("ROLE_USER")){
        searchButton.type="";
        searchButton.href="/lu/edit";
        headStore.className="d-none";
        cart.className="d-none";
    }
    if (!json.stringAuthorities.includes("ROLE_BUSINESS")){
        listItem.className="d-none";
    }

    search.addEventListener("submit", e=> {
        e.preventDefault();
        if (!json.stringAuthorities.includes("ROLE_USER")){
            alert("Please register your information first!!!");
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

    nicknameInput.value = json.nickname;
    nameInput.value = json.name;
    ageInput.value = json.age;
    emailInput.value = json.email;
    phoneInput.value = json.phone;
    avatarLink.src= json.profileImg;

    buyCount.innerText= json.numberOrder;
    if (json.numberOrder===0){
        buyCount.className="d-none";
    }
    sellCount.innerText=json.numberSell;
    if (json.numberOrder===0){
        sellCount.className="d-none";
    }
    const orderTotal=document.getElementById("allOrder-payment");
    if (orderTotal) orderTotal.innerText=`${json.payment}₩`;
    
})
.catch(e =>{
    alert(e.message)
})


const formRegister = document.getElementById("form-register");
formRegister.addEventListener("submit", e=>{
    e.preventDefault();

    const dataRegister = {
        nickname: nicknameInput.value,
        name: nameInput.value,
        age: ageInput.value,
        email: emailInput.value,
        phone: phoneInput.value,
    }
    //2.fetch 요청을 보낸다.
    fetch("/",{
        method: "PUT",
        headers: {
            "Content-type": "application/json",
            "Authorization": `Bearer ${token}`,
        },
        body: JSON.stringify(dataRegister),
    })
    .then (response => {
        if (response.ok){
            alert("Your information updates successful!!!")
            location.href = "/lu/profile"
            return response.json();
        } else {
            return response.text().then(text => {
                alert(text);
            })
        }
    })
    .catch(e => {
        alert(e.message);
    })
})

const avatarImg = document.getElementById("avatar-image");
avatarImg.addEventListener("submit", e => {
    e.preventDefault();
    
    const formData = new FormData(avatarImg); 

    fetch("/avatar", {
        method: "PUT",
        headers:{
            "Authorization": `Bearer ${token}`
        },
        body: formData,
    })
    .then(response =>{
        if (response.ok){
            return response.json();
        } else{
            alert("Fail to upload file");
        }
    }).then(json =>{
        avatarLink.src= json.profileImg;
        location.href="/lu/edit";
    })
})
// })

// if (!localStorage.getItem("authority").includes("ROLE_USER")){
//     alert("Please register your information first!!!");
// }