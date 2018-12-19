$(function () {
    var index = 0;
    $("#tabs").tabs({
        active: index
    });
    $("#tabs2").tabs({
        active: index
    });


    $("#menuAloj li:first-child a").trigger("click");

    if ($("#menuAloj").children().length == 1) {
        $("#prueba").children().css("display", "none");
        $("#tabs1").css("display", "none");
        $("#tabsAnadir").css("display", "block");
    }
});

$(document).on("click", "#menuAloj li:not(:last-child)", function () {
    var clickedBtnID = $(this).attr('id'); //OBTENER ID DEL ELEMENTO OBTENIDO
    $("#formHidden\\:inputHidden").val(clickedBtnID);
    $("#formHidden\\:botonHidden").trigger("click");
    $("#tabs2 ul li:first-child a").trigger("click");
    var index = $("#menuAloj li").index(this);
    $("#prueba").children().css("display", "none");
    $("#prueba").children().first().css("display", "block");
    $("#tabs").tabs({
        active: index
    });
    $("#tabs2").tabs({
        active: 0
    });

    //-------------PETICION AJAX MAPA--------------

    var datos = 'codigoAlojamiento=' + clickedBtnID;
    alert("")
    var latitud, longitud;

    $.getJSON("EnvioDatos", datos, function (result) {
        $.each(result, function (i, field) {
            if (i == "Latitud") {
                latitud = field;
            } else {
                longitud = field;
            }
        });

        //Pinto mapa
        var map = $("#map").geomap({
            center: [longitud, latitud],
            zoom: 15
        });
        $("#map").geomap("append", {
            type: "Point",
            coordinates: [longitud, latitud] // Coordinates are lng/lat
        }
        );
    });

    var map = $("#map").geomap({
        mode: "drawPoint",
        shape: function (e, geo) {
            map.geomap("append", geo, " ");
            var shapes = map.geomap("find", 1);
            if (shapes.length > 1) {
                map.geomap("remove", shapes[1]);
            }
            /* ------------------------*/
            var datosPunto = 'coordenadas=' + geo.coordinates + "," + clickedBtnID;
            $.getJSON("EnvioDatos", datosPunto, function (result) {

            });
        }
    });
});

$(document).on("click", "#tabs2 > ul li", function () {
    var index = $("#tabs2 li").index(this);
    $("#prueba").children().css("display", "none");
    $("#prueba").children().eq(index).css("display", "block");

    $("#tabs2").tabs({
        active: index
    });
});

$().fancybox({
    selector: '[data-fancybox="showImg"]'
});
