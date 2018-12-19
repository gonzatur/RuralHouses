var index = 0;
$("#tabs").tabs({
    active: index
});

var fechasReserva = {};
var fechasMias = {};

$(function () {


    var idAlojamiento;
    $(document).on("click", "#menuAloj li", function () {
        idAlojamiento = $(this).attr('id');
        var codigoAlojamiento = 'codigoAlojamiento=' + idAlojamiento;
        $.getJSON("FechasReserva", codigoAlojamiento, function (result) {
            fechasReserva = {};
            fechasMias = {};
            $.each(result, function (i, field) {
                //var quitarPrimero = field.substr(1);
                var fechas = field.slice(0, -1);
                console.log(field + " - " + i)

                if (i === 'resultadosReservas') {
                    var res = fechas.split(",");
                    res.forEach(function (element) {
                        var fechaFinal = element.substr(1);
                        fechasReserva[ new Date(fechaFinal)] = new Date(fechaFinal);
                        console.log(fechaFinal)
                    });
                } else {
                    var res = fechas.split(",");
                    res.forEach(function (element) {
                        var fechaFinal = element.substr(1);
                        fechasMias[ new Date(fechaFinal)] = new Date(fechaFinal);
                        console.log(fechaFinal)
                    });
                }
            });
            $("#calendar" + idAlojamiento).datepicker({
                numberOfMonths: [3, 4],
                monthNames: ['Noches de ENERO', 'Noches de FEBRERO', 'Noches de MARZO',
                    'Noches de ABRIL', 'Noches de MAYO', 'Noches de JUNIO',
                    'Noches de JULIO', 'Noches de AGOSTO', 'Noches de SEPTIEMBRE',
                    'Noches de OCTUBRE', 'Noches de NOVIEMBRE', 'Noches de DICIEMBRE'],
                dayNamesMin: ['D', 'L', 'M', 'X', 'J', 'V', 'S'],
                firstDay: 1,
                minDate: new Date(),

                onSelect: function (dateString) {
                    var miReservaBorrar;
                    var miReservaInsert;
                    if (fechasMias[new Date(dateString)] != null) {
                        delete fechasMias[new Date(dateString)]
                        miReservaBorrar = 'miReservaBorrar=' + dateString + "," + idAlojamiento;
                        $.getJSON("FechasReserva", miReservaBorrar, function (result) {

                        });
                    } else {
                        fechasMias[new Date(dateString)] = new Date(dateString);
                        miReservaInsert = 'miReservaInsert=' + dateString + "," + idAlojamiento;
                        $.getJSON("FechasReserva", miReservaInsert, function (result) {

                        });
                    }
                },
                beforeShowDay: function (date) {
                    var pintarReservas = fechasReserva[date];
                    var pintarMisFechas = fechasMias[date];

                    if (pintarMisFechas) {
                        return [true, "misFechas"];
                    } else if (pintarReservas) {
                        return [false, "reservas"];
                    } else {
                        return [true, ''];
                    }
                }
            });
        });

    });
    $("#menuAloj li:first-child").trigger("click");
});