const token = localStorage.getItem("token");
if (!token){
    location.href="/lu/login";
}

// /lu/manage-items/{itemIdx}/manage-orders/{orderIdx}
const pathSplit = location.pathname.split("/");
const itemIdx = pathSplit[pathSplit.length-3];
const orderIdx = pathSplit[pathSplit.length-1];

const confirmOrder= document.getElementById("confirm-order");
const confirmButton= document.getElementById("confirm-button");

const confirmed = document.getElementById("confirmed");
const backButton = document.getElementById("back-button");
backButton.href = `/lu/manage-items/${itemIdx}/manage-orders`;

function dependStatus(status){
    if (status==="Ordered"){
        confirmed.className ="d-none";
    } else if (status==="Cancel order" || status==="Paid") {
        confirmOrder.className="d-none";
    }
}

function oneOrder(order){
    const typeForm= document.getElementById("type-form");
    if (order.status=== "Ordered"){
        typeForm.innerText = `Status: Ordered`;
    }
    else if (order.status=== "Paid"){
        typeForm.innerText = `Status: Completed payment`;
    } else {
        typeForm.innerText = `Status: Canceled order`;
    }
    

    const itemName= document.getElementById("item-name");
    itemName.innerText = order.itemName;

    const itemPrice= document.getElementById("item-price");
    itemPrice.innerText = order.itemPrice;

    const orderQuantity= document.getElementById("order-quantity");
    orderQuantity.innerText = order.quantity;

    const orderTotal = document.getElementById("order-total");
    orderTotal.innerText = order.total;

    const buyer= document.getElementById("buyer");
    buyer.innerText = order.buyer.name;

    // const orderStatus = document.getElementById("order-status");
    // orderStatus.innerText= order.status;
    // if (order.status ==="Paid") orderStatus.className= "text-bg-success";
    // else if (order.status ==="Cancel order") orderStatus.className= "text-bg-danger";
    // else orderStatus.className= "text-bg-secondary";
    
}


fetch(`/manage-items/item${itemIdx}/manage-orders/order${orderIdx}`, {
    method: "GET",
    headers: {
        "Authorization": `Bearer ${token}`,
    }
}).then(response => {
    if (response.ok){
        return response.json();
    }
    else {
        alert("Fail to get this order!!!");
    }
}).then(json=>{
    oneOrder(json);
    dependStatus(json.status);
})
.catch(e=>{
    alert(e.message);
});



confirmButton.addEventListener("click", e=>{
    e.preventDefault();

    
    fetch(`/manage-items/item${itemIdx}/manage-orders/order${orderIdx}`, {
        method: "PUT",
        headers: {
            "Authorization": `Bearer ${token}`,
        }
    }).then(response => {
        if (response.ok){
            location.href=`/lu/manage-items/item${itemIdx}/manage-orders`;
            return response.json();
        }
        else {
            alert("Fail to confirm registration form!!!");
        }
    })
    .catch(e=>{
        alert(e.message);
    });
})