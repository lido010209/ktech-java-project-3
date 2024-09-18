const token = localStorage.getItem("token");
if (!token){
    location.href="/lu/login";
}

const itemName = document.getElementById("name");
const itemStock = document.getElementById("stock");
const itemPrice = document.getElementById("price");
const itemDescription = document.getElementById("description");


const createItem = document.getElementById("create-item");

createItem.addEventListener("submit", e=>{
    e.preventDefault();

    const dataItem = {
        name: itemName.value,
        stock: itemStock.value,
        price: itemPrice.value,
        description: itemDescription.value,
    };
    //2.fetch 요청을 보낸다.
    fetch("/manage-items", {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-type": "application/json"
        },
        body: JSON.stringify(dataItem),
    })
    .then (response => {
        if (response.ok){
            alert("Your item is created successful!!!")
            location.href= "/lu/manage-items";
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