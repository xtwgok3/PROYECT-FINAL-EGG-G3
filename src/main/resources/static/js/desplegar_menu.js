function desplegar() {      //-Desplegar Menu Toggle
    
const toggleButton = document.getElementById("navbar-toggler");
const menu = document.getElementById("navbarColor01");

let isCollapsed = menu.classList.contains("collapse");

if (isCollapsed) {
// Cambia a estado expandido
menu.classList.remove("collapse");
menu.classList.add("show");
toggleButton.setAttribute("aria-expanded", "true");
                
} else {
// Cambia a estado colapsado
menu.classList.remove("show");
menu.classList.add("collapse");
toggleButton.setAttribute("aria-expanded", "false");
}
}




function desplegar2() {      //Desplegar Perfil
    
const menu = document.getElementById("perfil_A");
const asd = document.getElementById("A_perfil");
        
let isCollapsed = menu.classList.contains("collapse");
        
if (isCollapsed) {
menu.classList.remove("collapse");
menu.setAttribute("aria-expanded", "true");
asd.classList.add("show");
            
// Agregar un manejador de clic para cerrar el menú si se hace clic fuera
document.addEventListener("click", function closeMenu(event) {
if (!menu.contains(event.target)) {
menu.classList.add("collapse");
 menu.setAttribute("aria-expanded", "false");
asd.classList.remove("show");
document.removeEventListener("click", closeMenu); // Eliminar el manejador después de usarlo
}});
 } else {
menu.classList.add("collapse");
menu.setAttribute("aria-expanded", "false");
asd.classList.remove("show");
}
}

/*
function desplegar3() {      //Desplegar ADMIN btnAltaPerfil
    
const menu = document.getElementById("btnAlta");
const asd = document.getElementById("btnAlta");
const asd2 = document.getElementById("btnd")
let isCollapsed = !menu.classList.contains("show");
        
if (isCollapsed) {

menu.setAttribute("aria-expanded", "true");
asd.classList.add("show");
asd2.classList.add("show");
            
// Agregar un manejador de clic para cerrar el menú si se hace clic fuera
document.addEventListener("click", function closeMenu(event) {
if (!menu.contains(event.target)) {
menu.setAttribute("aria-expanded", "false");
asd.classList.remove("show");
asd2.classList.remove("show");
document.removeEventListener("click", closeMenu); // Eliminar el manejador después de usarlo
}});
 } else {
menu.setAttribute("aria-expanded", "false");
asd.classList.remove("show");
asd2.classList.remove("show");
}
}*/

