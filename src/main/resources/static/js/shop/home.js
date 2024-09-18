const token = localStorage.getItem("token");
if (!token){
    location.href="/lu/login";
}

const displayArea = document.getElementById("display");
function oneShop(id, shopName, applicant, status, link){
    const idP= document.createElement("p");
    idP.className = "col-2 col-lg-1 border border-primary border-2 text-center";
    idP.innerText = id;

    const shopNameP= document.createElement("p");
    shopNameP.className = "col-6 col-lg-4 border border-primary border-2 text-center";
    shopNameP.innerText = shopName;

    const applicantP= document.createElement("p");
    applicantP.className = "d-none d-lg-block col-4 border border-primary border-2 text-center";
    applicantP.innerText = applicant;

    const statusP= document.createElement("p");
    statusP.className = "col-4 col-lg-3 border border-primary border-2 text-center";
    if (status ==="Pending open" || status==="Request close"){
        statusP.innerHTML = `
        <a href="${link}">Pending confirmation</a>
        `;
        statusP.className="text-bg-warning col-4 col-lg-3 border border-primary border-2 text-center"
    }
    else if (status ==="Opened"){
        statusP.innerHTML = `
        <a href="${link}" class="text-bg-success text-decoration-none">Approved open</a>
        `;
    }
    else if (status ==="Reject"){
        statusP.innerHTML = `
        <a href="${link}" class="text-bg-danger text-decoration-none">Rejected open</a>
        `;
    }
    else if (status ==="Closed"){
        statusP.innerHTML = `
        <a href="${link}" class="text-decoration-none">Approved close</a>
        `;
    }

    displayArea.appendChild(idP);
    displayArea.appendChild(shopNameP);
    displayArea.appendChild(applicantP);
    displayArea.appendChild(statusP);

}


fetch("/manage-shops", {
    method: "GET",
    headers: {
        "Authorization": `Bearer ${token}`,
    }
}).then(response => {
    if (response.ok){
        return response.json();
    }
    else {
        alert("Fail to get lists of shop!!!");
    }
}).then(json=>{
    json.forEach(shop=>{
        oneShop(shop.id, shop.name, shop.owner.name, shop.status, `/lu/manage-shops/${shop.id}`)
    })
})
.catch(e=>{
    alert(e.message);
});