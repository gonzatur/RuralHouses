
$(document).ready(function () {
	var valorPrecio;
$("#console").on("change", function () {
                alert("cambio")
                valorPrecio = $(".ui-state-active").parent().children().children()[2].value;
                filtrado();
            });


function filtrado() {
				$(".buscar").removeClass("pagino");
                $("#tablaAlojamientos #alojamiento").hide();

                var listaPrecios = document.querySelectorAll(".precioAlojHidden");
                listaPrecios.forEach(function (valor, index) {
                    var mostrar = valor.parentElement.parentElement;
                    var estado = mostrar.style.display;
                    var precioAloj = valor.value;

                    if (estado == "none") {						
						
						if(valorPrecio == 20){
							if (precioAloj <= valorPrecio) {
                            $(mostrar).show();
							}
                        }else if(valorPrecio == 25){
							
							if (precioAloj >=20 && precioAloj <=30) {
                            $(mostrar).show();
							}
						}else{
							if (precioAloj >= valorPrecio) {
                            $(mostrar).show();
							}
						}
					}
                    
                });
				$("#alojamiento").addClass("pagino");
				
            }
});