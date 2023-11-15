const formulario = document.getElementById("a-form");
const inputs = document.querySelectorAll("#a-form input");
const form2 = document.getElementById("a-form2");
const inputs2 = document.querySelectorAll("#a-form2 input");
const expresiones = {
    usuario: /^[a-zA-Z\_\-]{3,16}$/, // Letras, numeros, guion y guion_bajo
    nombre: /^[a-zA-ZÀ-ÿ\s]{1,40}$/, // Letras y espacios, pueden llevar acentos.
    password: /^.{6,12}$/, // 6 a 12 digitos.
    correo: /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/,
    telefono: /^\d{7,14}$/ // 7 a 14 numeros.
};

const validarCampo = (expresion, input, campo) => {
    if (input.value == "") {
        document.querySelector(`#gropu__${campo} i`).classList.remove("fa-circle-xmark");
        document.getElementById(`gropu__${campo}`).classList.remove("form__group-incorrecto");
        document.querySelector(`#gropu__${campo} .form__input-error`).classList.remove("form__input-error-activo");
        campos[campo] = false;
    } else if (expresion.test(input.value)) {
        document.querySelector(`#gropu__${campo} .form__input-error`).classList.remove("form__input-error-activo");
        document.querySelector(`#gropu__${campo} i`).classList.remove("fa-circle-xmark");
        document.querySelector(`#gropu__${campo} i`).classList.add("fa-circle-check");
        campos[campo] = true;
    } else {
        document.querySelector(`#gropu__${campo} i`).classList.remove("fa-circle-check")
        document.querySelector(`#gropu__${campo} i`).classList.add("fa-circle-xmark")
        document.getElementById(`gropu__${campo}`).classList.add("form__group-incorrecto")
        document.querySelector(`#gropu__${campo} .form__input-error`).classList.add("form__input-error-activo")
        campos[campo] = false;
    }
}

const validarCampo2 = (expresion, input, campo) => {
    if (input.value == "") {
        document.querySelector(`#gropu__${campo} i`).classList.remove("fa-circle-xmark");
        document.getElementById(`gropu__${campo}`).classList.remove("form__group2-incorrecto");
        document.querySelector(`#gropu__${campo} .form__input-error2`).classList.remove("form__input-error-activo");
        campos[campo] = false;
    } else if (expresion.test(input.value)) {
        document.querySelector(`#gropu__${campo} .form__input-error2`).classList.remove("form__input-error-activo");
        document.querySelector(`#gropu__${campo} i`).classList.remove("fa-circle-xmark");
        document.querySelector(`#gropu__${campo} i`).classList.add("fa-circle-check");
        campos[campo] = true;
    } else {
        document.querySelector(`#gropu__${campo} i`).classList.remove("fa-circle-check")
        document.querySelector(`#gropu__${campo} i`).classList.add("fa-circle-xmark")
        document.getElementById(`gropu__${campo}`).classList.add("form__group2-incorrecto")
        document.querySelector(`#gropu__${campo} .form__input-error2`).classList.add("form__input-error-activo")
        campos[campo] = false;
    }
}

const campos = {
    nombre: false,
    apellido: false,
    password: false,
    mail: false,
    password2: false
};

const  validarPassword2 = () => {
    const inputPassword1 = document.getElementById("contraseña");
    const inputPassword2 = document.getElementById("contraseña2");


    if (inputPassword2.value == "") {
        document.querySelector(`#gropu__password2 i`).classList.remove("fa-circle-xmark")
        document.getElementById(`gropu__password2`).classList.remove("form__group-incorrecto")
        document.querySelector(`#gropu__password2 .form__input-error`).classList.remove("form__input-error-activo")
        campos[password] = false;
    } else if (inputPassword1.value !== inputPassword2.value) {
        document.querySelector(`#gropu__password2 i`).classList.remove("fa-circle-check")
        document.querySelector(`#gropu__password2 i`).classList.add("fa-circle-xmark")
        document.getElementById(`gropu__password2`).classList.add("form__group-incorrecto")
        document.querySelector(`#gropu__password2 .form__input-error`).classList.add("form__input-error-activo")
        campos[password] = false;
    } else {
        document.querySelector(`#gropu__password2 .form__input-error`).classList.remove("form__input-error-activo")
        document.querySelector(`#gropu__password2 i`).classList.remove("fa-circle-xmark")
        document.querySelector(`#gropu__password2 i`).classList.add("fa-circle-check")
        campos[password] = true;
    }
}

const  validarPassword2b = () => {
    const inputPassword1 = document.getElementById("contraseñab");
    const inputPassword2 = document.getElementById("contraseña2b");


    if (inputPassword2.value == "") {
        document.querySelector(`#gropu__password2 i`).classList.remove("fa-circle-xmark")
        document.getElementById(`gropu__password2`).classList.remove("form__group-incorrecto")
        document.querySelector(`#gropu__password2 .form__input-error2`).classList.remove("form__input-error-activo")
        campos[password] = false;
    } else if (inputPassword1.value !== inputPassword2.value) {
        document.querySelector(`#gropu__password2 i`).classList.remove("fa-circle-check")
        document.querySelector(`#gropu__password2 i`).classList.add("fa-circle-xmark")
        document.getElementById(`gropu__password2`).classList.add("form__group-incorrecto")
        document.querySelector(`#gropu__password2 .form__input-error2`).classList.add("form__input-error-activo")
        campos[password] = false;
    } else {
        document.querySelector(`#gropu__password2 .form__input-error2`).classList.remove("form__input-error-activo")
        document.querySelector(`#gropu__password2 i`).classList.remove("fa-circle-xmark")
        document.querySelector(`#gropu__password2 i`).classList.add("fa-circle-check")
        campos[password] = true;
    }
}

const validarFormulario = (e) => {
    switch (e.target.name) {
        case "nombre":
            validarCampo(expresiones.usuario, e.target, "nombre");
            break;

        case "apellido":
            validarCampo(expresiones.usuario, e.target, "apellido");
            break;
        case "password":
            validarCampo(expresiones.password, e.target, "password");
            validarPassword2();
            break;
        case "email":
            validarCampo(expresiones.correo, e.target, "mail");
            break;
        case "password2":
            validarPassword2();
            break;
    }
}

const validarFormulario2 = (e) => {
    switch (e.target.name) {
        case "nombre":
            validarCampo2(expresiones.usuario, e.target, "nombre");
            break;

        case "apellido":
            validarCampo2(expresiones.usuario, e.target, "apellido");
            break;
        case "password":
            validarCampo2(expresiones.password, e.target, "password");
            validarPassword2b();
            break;
        case "email":
            validarCampo2(expresiones.correo, e.target, "mail");
            break;
        case "password2":
            validarPassword2b();
            break;
    }
};

inputs.forEach((input) => {
    input.addEventListener("keyup", validarFormulario)
    input.addEventListener("blur", validarFormulario)
});

inputs2.forEach((input) => {
    input.addEventListener("keyup", validarFormulario2);
    input.addEventListener("blur", validarFormulario2);
});

formulario.addEventListener("submit", (e) => {
    if (campos.nombre && campos.apellido && campos.password && campos.mail) {
        console.log("Bien");
    } else {
        e.preventDefault();
    }

});

form2.addEventListener("submit", (e) => {
    console.log(campos.password);
    if (campos.nombre && campos.apellido && campos.password && campos.mail) {
        console.log("Bien");
    } else {
        e.preventDefault();
    }
});