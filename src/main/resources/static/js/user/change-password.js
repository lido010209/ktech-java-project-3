const token = localStorage.getItem("token");
if (!token) {
    location.href("lu/login");
}

const oldPwInput = document.getElementById("old-password");
const newPwInput = document.getElementById("new-password");
const checkPwInput = document.getElementById("pwCheck");

const changeButton = document.getElementById("change-password");
deleteButton.addEventListener("submit", e=> {
    e.preventDefault();

    if (newPwInput.value !== checkPwInput.value){
        alert("Passwords do not match!!!");
        return;
    }

    const formData = new URLSearchParams("");
    formData.append("oldPassword", oldPwInput.value);
    formData.append("newPassword", newPwInput.value)

    fetch("/change-password", {
        method: "PUT",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": 'application/x-www-form-urlencoded'
        },
        body: formData
    })
    .then (response=> {
        if (response.ok){
            return response.json();
        }
        else {
            alert("Fail to change password");
        }
    })
    .catch(e => {
        alert(e.message);
    })
})