const token = localStorage.getItem("token");
if (!token){
    location.href="/lu/login";
}

const displayArea = document.getElementById("display");
function oneShop(shop){
    const idP= document.createElement("p");
    idP.className = "col-2 col-lg-1 border border-primary border-2 text-center";
    idP.innerText = shop.id;

    const shopNameP= document.createElement("p");
    shopNameP.className = "col-6 col-lg-4 border border-primary border-2 text-center";
    shopNameP.innerText = shop.name;

    const ownerP= document.createElement("p");
    ownerP.className = "d-none d-lg-block col-4 border border-primary border-2 text-center";
    ownerP.innerText = shop.owner.name;

    const itemP= document.createElement("p");
    itemP.className = "col-4 col-lg-3 border border-primary border-2 text-center";
    itemP.innerHTML = `
        <a href="/lu/search/shops/${shop.id}/items">Here</a>
        `;
        

    displayArea.appendChild(idP);
    displayArea.appendChild(shopNameP);
    displayArea.appendChild(ownerP);
    displayArea.appendChild(itemP);

}

const keyword = document.getElementById("keyword");
const category = document.getElementById("category");
const searchShops = document.getElementById("search-shops");

searchShops.addEventListener("submit", e=>{
    e.preventDefault();

    const params = new URLSearchParams();
    if (keyword.value) {
        params.append('keyword', keyword.value);
    }
    if (category.value) {
        params.append('category', category.value);
    }

    fetch(`/search/shops?${params.toString()}`, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`,
        }
    }).then(response => {
        if (response.ok){
            return response.json();
        }
        else {
            alert("Fail to search shops!!!");
        }
    }).then(json=>{
        displayArea.innerHTML="";
        json.forEach(shop=>{
            oneShop(shop);
        })
    })
    .catch(e=>{
        alert(e.message);
    });
})

fetch(`/search/shops`, {
    method: "GET",
    headers: {
        "Authorization": `Bearer ${token}`,
    }
}).then(response => {
    if (response.ok){
        return response.json();
    }
    else {
        alert("Fail to search shops!!!");
    }
}).then(json=>{
    displayArea.innerHTML="";
    json.forEach(shop=>{
        oneShop(shop);
    })
})
.catch(e=>{
    alert(e.message);
});
