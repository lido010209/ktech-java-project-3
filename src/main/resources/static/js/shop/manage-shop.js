const token = localStorage.getItem("token");
if (!token){
    location.href="/lu/login";
}

const shopName = document.getElementById("name");
const categorySelect = document.getElementById("category");
const shopIntrodution = document.getElementById("introduction")
const shopStatus = document.getElementById("status");
const reasonReject = document.getElementById("reason-reject");
const formReasonReject = document.getElementById("form-reason-reject");

//Update shop
const updateShop = document.getElementById("update-shop");
const updateButton = document.getElementById("update-button");
const clickUpdate = document.getElementById("click-update");
const backPage = document.getElementById("back-page");

//Close shop
const closeShop = document.getElementById("close-shop");
const clickClose = document.getElementById("click-close");
const reasonClose = document.getElementById("reason-close");

function controlClickClose(status){
    if (status==="Reject"){
        clickClose.innerText="Confirm reject";
        clickClose.addEventListener("click", e => {
            e.preventDefault();
            fetch("/shops/status", {
                method: "PUT",
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-type": "application/json",
                    
                },
                body: JSON.stringify({status:"Confirm reject"})
            }).then(response => {
                if (response.ok){
                    alert("Thank you for your confirmation!!!\nIf you want to request open shop again, just update your shop form^^!!!")
                    location.href="/lu/profile";
                    return response.json();
                }
                else { 
                    // alert("Fail to get this shop!!!");
                    return response.text().then(text=> {
                        alert(text);
                    })
                }
            })
            .catch(e=>{
                alert(e.message);
            });
        })
    } else if (status==="Closed" || status==="Request close" || status==="Pending open"){
        clickClose.className="d-none";
    } else if (status==="Opened"){
        clickClose.innerText = "Want to close shop?";
        clickClose.addEventListener("click", e=>{
            e.preventDefault();
            closeShop.className = "w-100 ps-3 pe-3";
            clickClose.className="d-none";
        })
    }
}

updateShop.addEventListener("submit", e=>{
    e.preventDefault();

    const dataUpdate = {
        name: shopName.value,
        category: categorySelect.value,
        introduction: shopIntrodution.value,
    }
    fetch("/shops", {
        method: "PUT",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-type": "application/json",
        },
        body: JSON.stringify(dataUpdate)
    }).then(response => {
        if (response.ok){
            alert("Your shop is updated successful!!!\nPlease wait for admin to confirm in 3 days^^")
            location.href="/lu/profile";
            return response.json();
        }
        else { 
            // alert("Fail to get this shop!!!");
            return response.text().then(text=> {
                alert(text);
            })
        }
    })
    .catch(e=>{
        alert(e.message);
    });
})

closeShop.addEventListener("submit", e=>{
    e.preventDefault();

        const dataClose = {
                status: "Request close",
                reason: reasonClose.value,
        }
        fetch("/shops/status", {
                method: "PUT",
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-type": "application/json",
                },
                body: JSON.stringify(dataClose)
        }).then(response => {
                if (response.ok){
                    alert("Your request close is sent successful!!!\nPlease wait for admin to confirm in 3 days")
                    location.href="/lu/profile";
                    return response.json();
                }
                else { 
                    // alert("Fail to get this shop!!!");
                    return response.text().then(text=> {
                        alert(text);
                    })
                }
            })
            .catch(e=>{
                alert(e.message);
            });
        })

clickUpdate.addEventListener("click", e=>{
    e.preventDefault();
    backPage.className= "d-none";
    updateButton.className = "col-12 text-center";
})

function oneShop(shop){
    const authority = localStorage.getItem("authority");
    shopName.value = shop.name;
    categorySelect.value = shop.category;
    shopIntrodution.value = shop.introduction;
    shopStatus.innerText= shop.status;
    reasonReject.value = shop.reason;
    if (shop.status ==="Opened") {
        shopStatus.className= "text-bg-success";
    }
    else if (shop.status ==="Closed") {
        shopStatus.className= "text-bg-danger";
        clickClose.className="d-none";
    }
    else shopStatus.className= "text-bg-secondary";
    
    if (shop.status ==="Opened"){
        formReasonReject.className= "form-group";
        formReasonReject.innerHTML= 
        `<a href="/lu/manage-items/create" role="button" class="btn btn-success">Create new item</a>`;
    } else if (shop.status==="Reject"){
        formReasonReject.className= "form-group";
    }

}


fetch(`/shops`, {
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
    oneShop(json);
    controlClickClose(json.status);
})
.catch(e=>{
    alert(e.message);
});


