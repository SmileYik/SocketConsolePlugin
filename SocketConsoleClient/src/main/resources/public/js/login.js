var loginRequest = {
    login: function () {
        let user = document.getElementById("username").value;
        let pass = document.getElementById("password").value;

        request.postJSON("/login", {
            username: user,
            password: pass
        }, function (e) {
            let rep = JSON.parse(e)
            location.href = "/console/index.html?token=" + rep["token"];
        })
    }
}