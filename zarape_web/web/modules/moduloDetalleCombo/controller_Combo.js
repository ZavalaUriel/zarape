let indexComboSeleccionado;
let combos = []

document.getElementById('btnCleanCombo').addEventListener('click', cleanCombo);
document.getElementById('btnBusquedaCombo').addEventListener('click', searchCombo);
document.getElementById('btnDeleteCombo').addEventListener('click', deleteCombo);
document.getElementById('btnAddCombo').addEventListener('click', addCombo);
document.getElementById('btnUpdateCombo').addEventListener('click', updateCombo);


function addCombo() {
    // aqui le estas definiendo los datos que vas a oocupar
    let idAlimento,
    idBebida,
    idCombo,
        nombreCombo,
        descripcion,
        precio,
        estatus;
        // aqui le estas diciendo que estas variables su valor se lo vas a traer del html de la etiqueta tal
    idAlimento = document.getElementById("txtIdAlimento-Combo").value;
    idBebida = document.getElementById("txtIdBebida-Combo").value;
    idCombo = document.getElementById("txtIdCombo").value;
    nombreCombo = document.getElementById("txtNombreCombo").value;
    descripcion = document.getElementById("txtDescripcionCombo").value;
    precio=document.getElementById("numPrecioCombo").value;

    // aqui estas declarando una variable de tipo array donde vas a almacenar los datos que trajiste del html (el paso de  arriba)
    let combo = {
        idAlimento: idAlimento,
        idBebida: idBebida,
        idCombo : idCombo, 
        nombreCombo: nombreCombo,
        descripcion: descripcion,
        precio: precio,
        estatus: "Activo"
    };
    // aqui le esstas diciendo que guarde en bebidas osea el Array, los valores de bebida, eel que declaramos aqui arrriba
    combos.push(combo);
    loadTabla();
    cleanCombo(); 
}

function loadTabla() {
    let cuerpo = "";
    combos.forEach(function (combo, index) {
        let registro = `
            <tr data-index="${index}">
            <td>${combo.idAlimento}</td>
            <td>${combo.idBebida}</td>
            <td>${combo.idCombo}</td>
            <td>${combo.nombreCombo}</td>
            <td>${combo.descripcion}</td>
            <td>${combo.precio}</td>
            <td>${combo.estatus}</td>
            </tr>
        `;
        cuerpo += registro;
    });
    document.getElementById("tblCombo").innerHTML = cuerpo;

    const rows = document.querySelectorAll("#tblCombo tr");
    rows.forEach(row => {
        row.addEventListener("click", function() {
            const index = this.getAttribute("data-index");
            selectCombo(parseInt(index, 10));
        });
    });
}

export function selectCombo(index) {
    let combo = combos[index];
    document.getElementById("txtIdAlimento-Combo").value = combos[index].idAlimento;
    document.getElementById("txtIdBebida-Combo").value = combos[index].idBebida;
    document.getElementById("txtIdCombo").disabled=true;
    document.getElementById("txtNombreCombo").value = combos[index].nombreCombo;
    document.getElementById("txtDescripcionCombo").value = combos[index].descripcion;
    document.getElementById("numPrecioCombo").value = combos[index].precio;

    document.getElementById("btnUpdateCombo").classList.remove("disabled");
    document.getElementById("btnDeleteCombo").classList.remove("disabled");
    document.getElementById("btnAddCombo").classList.add("disabled");

    indexComboSeleccionado = index;
}

fetch("http://proyectozarape.test/web/modules/moduloDetalleCombo/data_Combo.json")
    .then(function(response) {
        return response.json();
    })
    .then(function(jsondata) {
        combos = jsondata;
        console.log(combos);
        loadTabla();
    })
    .catch(function(error) {
        console.error('Error fetching data:', error);
    });
    
    function cleanCombo(){
    document.getElementById("txtIdAlimento-Combo").value = "";
    document.getElementById("txtIdBebida-Combo").value = "";
    document.getElementById("txtIdCombo").value = "";
    document.getElementById("txtNombreCombo").value = "" ;
    document.getElementById("txtDescripcionCombo").value = "";
    document.getElementById("numPrecioCombo").value = "";
    document.getElementById("txtIdCombo").focus();

    document.getElementById("btnUpdateCombo").classList.add("disabled");
    document.getElementById("btnDeleteCombo").classList.add("disabled");
    document.getElementById("btnAddCombo").classList.remove("disabled");
    indexComboSeleccionado = 0;
}

function updateCombo(){
    let idAlimento,
    idBebida,
    idCombo, 
        nombreCombo,
        descripcion,
        precio,
        estatus;
 
    idAlimento = document.getElementById("txtIdAlimento-Combo").value;
    idBebida = document.getElementById("txtIdBebida-Combo").value;
    idCombo = document.getElementById("txtIdCombo").disabled=true;
    nombreCombo = document.getElementById("txtNombreCombo").value;
    descripcion=document.getElementById("txtDescripcionCombo").value;
    precio=document.getElementById("numPrecioCombo").value;

    let combo = {};
    combo.idAlimento = idAlimento;
    combo.idBebida = idBebida;
    combo.idCombo = idCombo;
    combo.nombreCombo = nombreCombo;
    combo.descripcion=descripcion;
    combo.precio=precio;
    combo.estatus="Activo";
    
   combos[indexComboSeleccionado] = combo;
    cleanCombo();
    loadTabla();
}

function deleteCombo(){
    combos[indexComboSeleccionado].estatus = "Inactivo";
    cleanCombo();
    loadTabla();
}

export function searchCombo(){
    let filtro = document.getElementById("txtBusquedaCombo").value;
    let resultados = combos.filter(element => element.nombreCombo === filtro);
    let cuerpo = "";
    resultados.forEach(function(combo){
        let registro =  
                '<tr onclick="moduloCatalogoAlimento.selectAlimento('+ combos.indexOf(combo) +');">'+
                '<td>' + combo.idAlimento + '</td>' +
                '<td>' + combo.idBebida + '</td>' +
                '<td>' + combo.idCombo + '</td>' +
                '<td>' + combo.nombreCombo + '</td>' +
                '<td>' + combo.descripcion + '</td>' +
                '<td>' + combo.precio + '</td></tr>' ; 
        cuerpo += registro;
    });
    console.log(cuerpo);
    document.getElementById("tblCombo").innerHTML = cuerpo;
}
