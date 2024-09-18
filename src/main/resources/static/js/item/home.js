const token = localStorage.getItem("token");
if (!token){
    location.href="/lu/login";
}

const displayArea = document.getElementById("display");

function oneItem(item){
    const idxP= document.createElement("p");
    idxP.className = "col-2 col-lg-1 border border-primary border-2 text-center";
    idxP.innerHTML= `<a href="/lu/manage-items/${item.shop.itemIdxs[item.id]-1}">${item.shop.itemIdxs[item.id]}</a>`;

    const itemNameP= document.createElement("p");
    itemNameP.className = "col-6 col-lg-3 border border-primary border-2 text-center";
    itemNameP.innerHTML = `<a href="/lu/manage-items/${item.shop.itemIdxs[item.id]-1}">${item.name}</a>`;


    const stockP= document.createElement("p");
    stockP.className = "d-none d-lg-block col-2 border border-primary border-2 text-center";
    stockP.innerText = item.stock;

    const priceP= document.createElement("p");
    priceP.className = "d-none d-lg-block col-3 border border-primary border-2 text-center";
    priceP.innerText= item.price;

    const editP = document.createElement("p");
    editP.className = "col-4 col-lg-3 border border-primary border-2 text-center"
    editP.innerHTML = `<a href="/lu/manage-items/${item.shop.itemIdxs[item.id]-1}/manage-orders" class="text-decoration-none">${item.numberOrder}</a>`;
    
    displayArea.appendChild(idxP);
    displayArea.appendChild(itemNameP);
    displayArea.appendChild(stockP);
    displayArea.appendChild(priceP);
    displayArea.appendChild(editP);
}


fetch("/manage-items", {
    method: "GET",
    headers: {
        "Authorization": `Bearer ${token}`,
    }
}).then(response => {
    if (response.ok){
        return response.json();
    }
    else {
        alert("Fail to get lists of item!!!");
    }
}).then(json=>{
    json.forEach(item=>{
        oneItem(item);
    })
    
})
.catch(e=>{
    alert(e.message);
});

const keyword = document.getElementById("keyword");
const price1 = document.getElementById("price1");
const price2 = document.getElementById("price2");
const searchItems= document.getElementById("search-items");
searchItems.addEventListener("submit", e=>{
    e.preventDefault();
    const params = new URLSearchParams();
    if (keyword.value) {
        params.append('keyword', keyword.value);
    }
    if (price1.value && price2.value) {
        if (Number(price1.value) > Number(price2.value)){
            alert("Please check your price limit");
            return;
        }
        params.append('price1', price1.value);
        params.append('price2', price2.value);
    }

    fetch(`/search/shops/items?${params.toString()}`, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    }).then(response => {
        if (response.ok){
            return response.json();
        }
        else {
            alert("Fail to search item!!!");
        }
    }).then(json=>{
        displayArea.innerHTML="";
        json.forEach(item=>{
            oneItem(item);
        })
    
    })
    .catch(e=>{
        alert(e.message);
    });
})