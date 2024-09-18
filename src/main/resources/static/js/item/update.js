const token = localStorage.getItem("token");
if (!token){
    location.href="/lu/login";
}

const pathSplit = location.pathname.split("/");
const itemIdx = pathSplit[pathSplit.length-1];

const itemName = document.getElementById("name");
const itemStock = document.getElementById("stock");
const itemPrice = document.getElementById("price");
const itemDescription = document.getElementById("description");

const deleteItem = document.getElementById("delete-item");
const updateItem = document.getElementById("update-item");
const updateImg= document.getElementById("item-image");
function oneItem(item){
    itemName.value = item.name,
    itemStock.value = item.stock,
    itemPrice.value = item.price,
    itemDescription.value = item.description
}

fetch(`/manage-items/item${itemIdx}`, {
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
    const imageDisplay = document.querySelector("#image-display img");
    imageDisplay.src = json.image;
    // const cart =document.getElementById("cart");
    // cart.href=`/lu/manage-items/${itemIdx}/manage-orders`;
})
.catch(e=>{
    alert(e.message);
});

updateItem.addEventListener("submit", e=>{
    e.preventDefault();
    
    const updateItem= {
        name: itemName.value,
        stock: itemStock.value,
        price: itemPrice.value,
        description: itemDescription.value
    }

    fetch(`/manage-items/item${itemIdx}`, {
        method: "PUT",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-type": "application/json",
        },
        body: JSON.stringify(updateItem)
    }).then(response => {
        if (response.ok){
            alert("Your item is updated successful^^!!!")
                location.href="/lu/manage-items";
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

updateImg.addEventListener("submit", e=>{
    e.preventDefault();

    const formData = new FormData(updateImg);
    fetch(`/manage-items/item${itemIdx}/image`, {
        method: "PUT",
        headers: {
            "Authorization": `Bearer ${token}`
        },
        body: formData,
    }).then(response => {
        if (response.ok){
            alert("Your item image is updated successful^^!!!")
            location.href=`/lu/manage-items/${itemIdx}`;
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

deleteItem.addEventListener("submit", e=> {
    e.preventDefault();

    fetch(`/manage-items/item${itemIdx}`, {
        method: "DELETE",
        headers: {
            "Authorization": `Bearer ${token}`
        },
    }).then(response => {
        if (response.ok){
            alert("Your item delete successful^^!!!")
            location.href=`/lu/manage-items`;
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
