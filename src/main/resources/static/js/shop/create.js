
const shopName = document.getElementById("name");
const categorySelect = document.getElementById("category");
const introdutionInput = document.getElementById("introduction")


const createShop = document.getElementById("create-shop");

createShop.addEventListener("submit", e=>{
    e.preventDefault();

    const dataForm = {
        name: shopName.value,
        category: categorySelect.value,
        introdutionInput: introdutionInput.value
    };
    //2.fetch 요청을 보낸다.
    fetch("/shops", {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-type": "application/json"
        },
        body: JSON.stringify(dataForm),
    })
    .then (response => {
        if (response.ok){
            alert("Your shop is registered successful!!!\nPlease wait for admin to confirm in 3 days.")
            location.href= "/lu/profile";
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