var modal = document.getElementById('myModal');
var span = document.getElementsByClassName("close")[0];
function iniciaSesion() {
    $("#tabs").css("display", "block");
    $("#myModal").css("display", "block");
    $("#tabs").tabs({
        active: 0
    });
}

function formRemember() {
    $("#remember").css("display", "block");
}

function registro() {
    $("#tabs").css("display", "block");
    $("#myModal").css("display", "block");
    $("#tabs").tabs({
        active: 1
    });
}

function mostrarDatosUsuario(id) {
    if (document.getElementById) { //se obtiene el id
        var el = document.getElementById(id); //se define la variable "el" igual a nuestro div
        el.style.display = (el.style.display == 'block') ? 'none' : 'block'; //damos un atributo display:none que oculta el div
    }
}

span.onclick = function () {
    $("#myModal").css("display", "none");
    $("#remember").css("display", "none");
}

window.onclick = function (event) {
    if (event.target == modal) {
        $("#myModal").css("display", "none");
        $("#remember").css("display", "none");
    }
}

$('#entrada_input').attr('readonly', true);
$('#salida_input').attr('readonly', true);