const searchToken = localStorage.getItem("token");


const pathSplit = location.pathname.split("/");
const shopId = pathSplit[pathSplit.length-2];

const displayArea = document.getElementById("display");
const itemName = document.getElementById("item-name");
const itemPrice = document.getElementById("item-price");
const itemImage = document.getElementById("item-image");
function oneItem(item){
    const allEle = document.createElement("div");
    allEle.className= "col-12 col-sm-6 col-md-4 col-lg-3 p-1";

    const allEle1 = document.createElement("div");
    allEle1.className= "p-3 border border-2 border-dark";

    const nameH3= document.createElement("p");
    nameH3.innerHTML=`Item: <span class="fw-bold">${item.name}</span>`;

    const imageDiv = document.createElement("div");
    imageDiv.className = "w-100 text-center";
    imageDiv.innerHTML=
    `
    <a class="link-image" target="_blank" aria-haspopup="true">
        <img src="${item.image}" id="item-image" class="img-fluid item">
    </a>
    `;

    const priceDiv= document.createElement("div");
    priceDiv.className= "w-100 text-center p-3 d-flex";

    const authority = localStorage.getItem("authority");
    if (!authority.includes("ROLE_USER")){
        priceDiv.innerHTML= 
        `<div class="col-6 text-start">
            <p id="item-price">Price: <span class="text-bg-success">${item.price}</span>₩</p>
        </div>
        <div class="col-6">
            <div class="w-100 text-end">
                <a href="/lu/edit" id="orderButton" role="button" class="btn btn-primary">Order</a>
            </div>
        </div>
        `
    } else{
    priceDiv.innerHTML= 
    `<div class="col-6 text-start">
        <p id="item-price">Price: <span class="text-bg-success">${item.price}</span>₩</p>
    </div>
    <div class="col-6">
        <div class="w-100 text-end">
            <a href="/lu/malls/${item.id}" id="orderButton" role="button" class="btn btn-primary">Order</a>
        </div>
    </div>
    `}

    allEle1.appendChild(nameH3);
    allEle1.appendChild(imageDiv);
    allEle1.appendChild(priceDiv);
    allEle.appendChild(allEle1);
    displayArea.appendChild(allEle);
}


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

    fetch(`/search/shops/${shopId}/items?${params.toString()}`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${searchToken}`,
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

fetch(`/malls/shop/${shopId}`, {
    method: "GET",
    headers: {
        "Authorization": `Bearer ${searchToken}`,
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
        oneItem(item)
    })
})
.catch(e=>{
    alert(e.message);
});

