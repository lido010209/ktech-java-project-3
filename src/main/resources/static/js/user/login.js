// console.log(localStorage.getItem("token"));
if (localStorage.getItem("token")) {
    location.href= "/lu/profile";
}

const usernameLogin = document.getElementById("username");
const passwordLogin = document.getElementById("password");

// 해당 데이터를 모아서 JSON으로 반환
// JSON 데이터를 서버로 전송
// 응답

const formLogin = document.getElementById("form-login");

formLogin.addEventListener("submit", e=>{
    e.preventDefault();

    //보낸 데이터를 정리해둔다.
    const dataLogin = {
        username: usernameLogin.value,
        password: passwordLogin.value,
    }
    //2.fetch 요청을 보낸다.
    fetch("/token/issue", {
        method: "POST",
        headers: {
            "Content-type": "application/json"
        },
        body: JSON.stringify(dataLogin),
    })
    .then(response => {
        if (response.ok){
            location.href= "/lu/profile";
            return response.json();
        } 
        else {
            return response.text().then(text => {
                // console.log(text);
                alert(text);
            });
        }
    })
    // 응답을 담긴 JWT 를 저장한다
    .then(json => {
        localStorage.setItem("token", json.token);
    })
    .catch(e =>{
        alert(e.message)
    })
})