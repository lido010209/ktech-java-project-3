const token = localStorage.getItem("token");
if (!token){
    location.href="/lu/login";
}

// //"manage-items/{idx}/manage-orders"
// const pathSplit = location.pathname.split("/");
// const itemIdx = pathSplit[pathSplit.length-2];
const displayArea = document.getElementById("display");

function oneOrder(order){
    const idP= document.createElement("p");
    idP.className = "col-2 col-lg-1 border border-primary border-2 text-center";
    idP.innerText = order.orderBuyerIdxs[order.id];

    const itemNameP= document.createElement("p");
    itemNameP.className = "col-6 col-lg-4 border border-primary border-2 text-center";
    itemNameP.innerText = order.itemName;

    const paymentP= document.createElement("p");
    paymentP.className = "d-none d-lg-block col-4 border border-primary border-2 text-center";
    paymentP.innerText = order.total;

    const statusP= document.createElement("p");
    statusP.className = "col-4 col-lg-3 border border-primary border-2 text-center";
    if (order.status ==="Ordered"){
        statusP.innerHTML = `
        <a href="/lu/order/${order.orderBuyerIdxs[order.id]-1}">Pending confirmation</a>
        `;
        statusP.className="text-bg-warning col-4 col-lg-3 border border-primary border-2 text-center"
    }
    else if (order.status ==="Paid"){
        statusP.innerHTML = `
        <a href="/lu/order/${order.orderBuyerIdxs[order.id]-1}" class="text-bg-success text-decoration-none">Paid</a>
        `;
    }
    else if (order.status ==="Cancel order"){
        statusP.innerHTML = `
        <a href="/lu/order/${order.orderBuyerIdxs[order.id]-1}" class="text-bg-danger text-decoration-none">Cancel order</a>
        `;
    }

    displayArea.appendChild(idP);
    displayArea.appendChild(itemNameP);
    displayArea.appendChild(paymentP);
    displayArea.appendChild(statusP);

}

const allOrder = document.getElementById("all-order");
const orderedDetail = document.getElementById("ordered-detail");
const displayOrder = document.getElementById("display-order");
const orderTotal = document.getElementById("order-total");

function oneOrder1(order){
     
    const hr = document.createElement("hr");
    const div = document.createElement("div");
    div.innerHTML= 
    `
    <p>Item: ${order.itemName}</p>
    <p>Quantity: ${order.quantity}</p>
    <p>Amount: ${order.itemPrice}₩</p>
    <hr class="ps-5 pe-5">
    `
    displayOrder.appendChild(hr);
    displayOrder.appendChild(div);
}

fetch(`/orders`, {
    method: "GET",
    headers: {
        "Authorization": `Bearer ${token}`,
    }
}).then(response => {
    if (response.ok){
        return response.json();
    }
    else {
        alert("Fail to get lists of order!!!");
    }
}).then(json=>{
    let sum=0;
    json.forEach(order=>{
        oneOrder(order);
        if(order.status==="Ordered"){
            sum+=1;
            oneOrder1(order);
        }
    })
    
    if (sum===0){
        allOrder.className="col-12 d-flex justify-content-start align-items-center flex-wrap";
        orderedDetail.className="d-none";
    }
})
.catch(e=>{
    alert(e.message);
});




