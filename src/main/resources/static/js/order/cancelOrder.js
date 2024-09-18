const token = localStorage.getItem("token");
if (!token){
    location.href="/lu/login";
}


const orderQuantity = document.getElementById("order-quantity");
const orderTotal = document.getElementById("order-total");
const orderStatus = document.getElementById("order-status");

const cancelOrder = document.getElementById("cancel-order");
const itemNameInput = document.getElementById("item-name");
const itemPriceInput = document.getElementById("item-price");
const itemDescriptionInput = document.getElementById("item-description");
const itemImageInput = document.getElementById("item-image");

function oneItem(order){
    itemNameInput.innerText = `Item: ${order.itemName}`,
    itemPriceInput.innerText = `Price: ${order.itemPrice}`,
    itemDescriptionInput.innerText = order.itemDescription,
    itemImageInput.src = order.itemImage
}

const pathSplit = location.pathname.split("/");
const orderIdx = pathSplit[pathSplit.length-1];


fetch(`/orders/${orderIdx}`, {
    method: "GET",
    headers: {
        "Authorization": `Bearer ${token}`,
    }
})
.then (response => {
    if (response.ok){
        return response.json();
    }
    else {
        // alert("Fail to get this shop!!!");
        return response.text().then(text=>{
            alert(text);
        })
    }
})
.then(json=>{
    console.log(json);
    oneItem(json);
    orderQuantity.innerText=`Quantity: ${json.quantity}`;
    orderTotal.innerText=`Total: ${json.total}`;
    orderStatus.innerText=json.status;
    if(json.status === "Ordered"){
        orderStatus.className="text-bg-secondary";
    }
    else if(json.status === "Paid"){
        orderStatus.className="text-bg-success";
        cancelOrder.className="d-none";
    }
    else if(json.status === "Cancel order"){
        orderStatus.className="text-bg-danger";
        cancelOrder.className="d-none";
    }
    
})
.catch(e=>{
    alert(e.message);
});

cancelOrder.addEventListener("click", e=>{
    e.preventDefault();


    //2.fetch 요청을 보낸다.
    fetch(`/orders/${orderIdx}`, {
        method: "PUT",
        headers: {
            "Authorization": `Bearer ${token}`
        },
    })
    .then (response => {
        if (response.ok){
            alert("Cancel Order successful!!!")
            location.href= "/lu/order";
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