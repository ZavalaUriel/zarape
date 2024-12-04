
document.getElementById("BtnRegistrar").addEventListener('click', validarDatos);

async function validarDatos() {

    let user = document.getElementById("Email").value;
    let password = document.getElementById("Password").value;

    console.log(user);
    console.log(password);

    if (user == "" || password == "") {

        setTimeout(() => {
            alert("Ingresar todos los campos");
        }, 5000);
    }


    let url = "http://localhost:8080/zarape_web/api/principal/login?user=" + user + "&password=" + password;

    let resp = await fetch(url);
    let datos = await resp.json();

    console.log(datos);
    if (datos.ingreso) {

        window.location.href = "../zarape_web/menu.html";
    }
    if (!datos.ingreso) {

        alert("Acceso denegado, usuario o contraseña errónea");
    }
}


