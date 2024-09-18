const token = localStorage.getItem("token");
if (!token){
    location.href="/lu/login";
}

const pathSplit = location.pathname.split("/");
const shopId = pathSplit[pathSplit.length-1];
const confirmStatus= document.getElementById("status");
const confirmReason= document.getElementById("reason");
const confirmShop = document.getElementById("confirm-shop");
const confirmed = document.getElementById("confirmed");

function dependStatus(status){
    if (status==="Pending open" || status==="Request close"){
        confirmed.className ="d-none";
    } else {
        confirmShop.className="d-none";
    }
}
function oneShop(shop){
    const typeForm= document.getElementById("type-form");
    if (shop.status=== "Pending open"){
        typeForm.innerText = `Form: Request opening`;
    }
    else if (shop.status=== "Request close"){
        typeForm.innerText = `Form: Request closing`;
    } else {
        typeForm.innerText = `Form: Confirmed`;
    }
    

    const shopName= document.getElementById("shop-name");
    shopName.innerText = shop.name;

    const shopCategory= document.getElementById("shop-category");
    shopCategory.innerText = shop.category;

    const shopIntroduction= document.getElementById("shop-introduction");
    shopIntroduction.innerText = shop.introduction;

    const shopStatus = document.getElementById("shop-status");
    shopStatus.innerText= shop.status;
    if (shop.status ==="Opened") shopStatus.className= "text-bg-success";
    else if (shop.status ==="Closed") shopStatus.className= "text-bg-danger";
    else shopStatus.className= "text-bg-secondary";
    
}

function addStatus(status){
    const option1 = document.createElement("option");
    const option2 = document.createElement("option");
    if (status==="Pending open"){
        option1.value = "Opened";
        option1.innerText="Approved open";

        option2.value = "Reject";
        option2.innerText="Reject";
        confirmStatus.appendChild(option1);
        confirmStatus.appendChild(option2);
    }
    else if (status==="Request close"){
        option1.value = "Closed";
        option1.innerText="Confirm close";
        confirmStatus.appendChild(option1);
        const reasonInput = document.getElementById("reason-input");
        reasonInput.className= "d-none";
    }
    
}


fetch(`/manage-shops/${shopId}`, {
    method: "GET",
    headers: {
        "Authorization": `Bearer ${token}`,
    }
}).then(response => {
    if (response.ok){
        return response.json();
    }
    else {
        alert("Fail to get this shop!!!");
    }
}).then(json=>{
    addStatus(json.status);
    oneShop(json);
    dependStatus(json.status);
    
})
.catch(e=>{
    alert(e.message);
});



confirmShop.addEventListener("submit", e=>{
    e.preventDefault();

    const dataForm = {
        status: confirmStatus.value,
        reason: confirmReason.value,
    }
    
    fetch(`/manage-shops/${shopId}`, {
        method: "PUT",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-type": "application/json",
        },
        body: JSON.stringify(dataForm)
    }).then(response => {
        if (response.ok){
            location.href="/lu/manage-shops";
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