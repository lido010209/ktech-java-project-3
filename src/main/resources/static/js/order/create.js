const token = localStorage.getItem("token");
if (!token){
    location.href="/lu/login";
}

const orderQuantity = document.getElementById("quantity");
const order = document.getElementById("order");

const itemName = document.getElementById("item-name");
const itemStock = document.getElementById("item-stock");
const itemPrice = document.getElementById("item-price");
const itemDescription = document.getElementById("item-description");
const itemImage = document.getElementById("item-image");

function oneItem(item){
    itemName.innerText = `Item: ${item.name}`,
    itemStock.innerText = `Stock: ${item.stock}`,
    itemPrice.innerText = `Price: ${item.price}`,
    itemDescription.innerText = item.description,
    itemImage.src = item.image
}

const pathSplit = location.pathname.split("/");
const itemId = pathSplit[pathSplit.length-1];


fetch(`/malls/${itemId}`, {
    method: "GET",
    headers: {
        "Authorization": `Bearer ${token}`,
    }
}).then(response => {
    if (response.ok){
        return response.json();
    }
    else {
        // alert("Fail to get this shop!!!");
        return response.text().then(text=>{
            alert(text);
        })
    }
}).then(json=>{
    oneItem(json);
})
.catch(e=>{
    alert(e.message);
});

order.addEventListener("submit", e=>{
    e.preventDefault();

    const dataForm = {
        quantity: orderQuantity.value
    };
    //2.fetch 요청을 보낸다.
    fetch(`/orders/${itemId}`, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-type": "application/json"
        },
        body: JSON.stringify(dataForm),
    })
    .then (response => {
        if (response.ok){
            alert("Order successful!!!")
            location.href= "/lu/malls";
            return response.json();
        } 
        else {
            // alert("Fail to create shop!!!")
            return response.text().then(text=>{
                alert(text);
            })
        }
    })
    .catch(e =>{
        alert(e.message)
    })
})