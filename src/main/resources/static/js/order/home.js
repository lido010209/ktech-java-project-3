const token = localStorage.getItem("token");
if (!token){
    location.href="/lu/login";
}

//"manage-items/{idx}/manage-orders"
const pathSplit = location.pathname.split("/");
const itemIdx = pathSplit[pathSplit.length-2].match(/\d+/);
const displayArea = document.getElementById("display");

function oneOrder(order){
    const idP= document.createElement("p");
    idP.className = "col-2 col-lg-1 border border-primary border-2 text-center";
    idP.innerText = order.orderIdxs[order.id];

    const itemNameP= document.createElement("p");
    itemNameP.className = "col-6 col-lg-4 border border-primary border-2 text-center";
    itemNameP.innerText = order.itemName;

    const buyerP= document.createElement("p");
    buyerP.className = "d-none d-lg-block col-4 border border-primary border-2 text-center";
    buyerP.innerText = order.buyer.name;

    const statusP= document.createElement("p");
    statusP.className = "col-4 col-lg-3 border border-primary border-2 text-center";
    if (order.status ==="Ordered"){
        statusP.innerHTML = `
        <a href="/lu/manage-items/${itemIdx}/manage-orders/${order.orderIdxs[order.id]-1}">Pending confirmation</a>
        `;
        statusP.className="text-bg-warning col-4 col-lg-3 border border-primary border-2 text-center"
    }
    else if (order.status ==="Paid"){
        statusP.innerHTML = `
        <a href="/lu/manage-items/${itemIdx}/manage-orders/${order.orderIdxs[order.id]-1}" class="text-bg-success text-decoration-none">Confirmed order</a>
        `;
    }
    else if (order.status ==="Cancel order"){
        statusP.innerHTML = `
        <a href="/lu/manage-items/${itemIdx}/manage-orders/${order.orderIdxs[order.id]-1}" class="text-bg-danger text-decoration-none">Cancel order</a>
        `;
    }

    displayArea.appendChild(idP);
    displayArea.appendChild(itemNameP);
    displayArea.appendChild(buyerP);
    displayArea.appendChild(statusP);

}



fetch(`/manage-items/item${itemIdx}/manage-orders`, {
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
        json.forEach(order=>{
            oneOrder(order);
        })
    
})
.catch(e=>{
    alert(e.message);
});