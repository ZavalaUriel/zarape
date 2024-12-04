document.getElementById("btnLimpiarMostrarFormulario").addEventListener('click', limpiarMostrarFormulario);
document.getElementById("btnSave").addEventListener('click', save);
document.getElementById("btnDelete").addEventListener('click', _delete);
document.getElementById("btnLimpiarFormulario").addEventListener('click', limpiarFormulario);
document.getElementById("btnSetDetalleVisible").addEventListener('click', () => setDetalleVisible(false));

let map = null;  // Variable global del mapa de Here
let behavior = null;  // Variable global del marcador
let ui = null; 
let marker = null;
let ciudades = [];
let estados = [];
let sucursales = [];

let inputFileFotoProducto = null;

export function inicializar()
{    
    recargarComboBoxCiudades();
    
    recargarTablaSucursales();
    setDetalleVisible(false);
    setMapaVisible(false);
    
    // Se obtiene el <input> de tipo file asociado con la foto del producto:
    inputFileFotoProducto = document.getElementById("inputFoto");
    
    // Se agrega un oyente para cuando el usuario seleccione un archivo,
    // se invoque a la funcion "cargarFotografia()":
    inputFileFotoProducto.onchange = function(evt){cargarFotografia();};
    
    // Agregamos un oyente al boton que permite al usuario cargar una imagen
    // para que cuando lo presione, se active el <input> de tipo file:
    document.getElementById("btnCargarFoto").onclick = function(evt) { inputFileFotoProducto.click(); };
}

/**
 * Esta funcion guarda los datos de un producto.
 */
export async function save()
{
    let url = 'http://localhost:8080/zarape_web/api/sucursal/save';
    let sucursal =  {
                        idSucursal : 0,
                        nombreSucursal : document.getElementById("txtNombre").value,
                        latitud : document.getElementById("txtLatitud").value,
                        longitud : document.getElementById("txtLongitud").value,
                        fotoSucursal : document.getElementById("txtaFoto").value,
                        url : document.getElementById("txtUrl").value,
                        horarios : document.getElementById("txtHorarios").value,
                        calle : document.getElementById("txtCalle").value,
                        numCalle : document.getElementById("txtNumCalle").value,
                        colonia : document.getElementById("txtColonia").value,
                        ciudad :  {
                                        idCiudad          : parseInt(document.getElementById("cmbCiudad").value)
                                        
                                    }
                    };
                    
    let datos = null;
    
    let params = null;
    
    let opciones = null;
    
    let resp = null;
    
    let data = null;
                    
    if (document.getElementById("txtIdSucursal").value.trim() != '')
    {
        sucursal.idSucursal = parseInt(document.getElementById("txtIdSucursal").value.trim());
    }
    
    datos = {datosSucursal : JSON.stringify(sucursal)};
    params = new URLSearchParams(datos);
    opciones =  {
                    method  : "POST",
                    headers : {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
                    body    : params
                };
    
    resp = await fetch(url, opciones);
    data = await resp.json();
    
    if (data.error != null)
    {
        Swal.fire('', data.error, 'error');
    }
    else
    {
        document.getElementById("txtIdSucursal").value = data.idSucursal;
        recargarTablaSucursales();
        Swal.fire('Datos de sucursal guardados con &eacute;xito.', '', 'success');
    }
}

// A esta funcion se le puso un guion bajo antes del nombre
// porque "delete" es una palabra reservada en JavaScript:
export async function _delete()
{
    let url = 'api/sucursal/delete';
    let idSucursal = 0;
    
    let datos = null;
    
    let params = null;
    
    let opciones = null;
    
    let resp = null;
    
    let data = null;
    
    if (document.getElementById("txtIdSucursal").value.trim() != '')
    {
        idSucursal = parseInt(document.getElementById("txtIdSucursal").value.trim());
    }
    else
    {
        Swal.fire('Seleccione un producto para eliminarlo.', '', 'warning');
        return;
    }
    
    datos = {idSucursal : idSucursal};
    params = new URLSearchParams(datos);
    opciones =  {
                    method  : "POST",
                    headers : {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
                    body    : params
                };
    
    resp = await fetch(url, opciones);
    data = await resp.json();
    
    if (data.error != null)
    {
        Swal.fire('', data.error, 'error');
    }
    else
    {
        recargarTablaSucursales();
        setDetalleVisible(false);
        Swal.fire('Registro de alimento eliminado con &eacute;xito.', '', 'success');
    }
}

/**
 * Esta funcion consulta las categorias por medio de un servicio REST
 * y posteriormente, se llena un ComboBox.
 */
export async function recargarComboBoxCiudades()
{
    // Definimos la URL del servicio:
    let url = 'http://localhost:8080/zarape_web/api/sucursal/getAllCiudades';
    
    // Invocamos el servicio:
    let resp = await fetch(url);
    
    // Convertimos la respuesta del servicio en un documento JSON:
    let datos = await resp.json();
    
    // Aqui se guardara el contenido HTML de la tabla, con los datos
    // de los alimentos que devolvio el servicio:
    let contenido = '';
    let contenidoE = '';
    // Verificamos si hubo al gun error:
    if (datos.error != null)
    {
        Swal.fire('Error al consultar categorias de alimentos.', datos.error, 'error');        
    }
    else
    {
        // Se guarda el arreglo de categorias en una variable global del modulo:
        ciudades = datos;
        
        // Se recorre el arreglo de alimentos:
        for (let i = 0; i < ciudades.length; i++)
        {
            // Por cada alimento, se genera una opcion para el ComboBox:
            contenido +=    '<option value="' + ciudades[i].idCiudad + '">' +
                                ciudades[i].nombreCiudad + 
                            '</option>';
        } 
    }
    
    // Una vez que se termino de recorrer el arreglo de categorias, se coloca
    // el contenido de la tabla dentro del <select> correspondiente:
    document.getElementById('cmbCiudad').innerHTML = contenido;
}

/**
 * Con esta funcion se consultan los alimentos a traves del servicio REST
 * y con los datos devueltos, se genera el contenido de la tabla de forma
 * dinamica.
 */
export async function recargarTablaSucursales()
{
    // Definimos la URL del servicio:
    let url = "http://localhost:8080/zarape_web/api/sucursal/getAll";
    
    // Invocamos el servicio:
    let resp = await fetch(url);
    
    // Convertimos la respuesta del servicio en un documento JSON:
    let datos = await resp.json();
    
    // Aqui se guardara el contenido HTML de la tabla, con los datos
    // de los alimentos que devolvio el servicio:
    let contenido = '';
    
    // Verificamos si hubo al gun error:
    if (datos.error != null)
    {
        Swal.fire('Error al consultar alimentos.', datos.error, 'error');
    }
    else
    {
        // Se guarda el arreglo de alimentos en una variable global del modulo:
        sucursales = datos;
        
        // Se recorre el arreglo de alimentos:
        for (let i = 0; i < sucursales.length; i++)
        {
            // Por cada alimento, se genera un renglon de tabla, con cada una
            // de sus columnas:
            contenido +=    '<tr>' +
                                '<td>' + sucursales[i].nombreSucursal + '</td>' +
                                '<td class="text-end">' + sucursales[i].ciudad.nombreCiudad + '</td>' +
                                '<td>' + sucursales[i].ciudad.estado.nombreEstado + '</td>' +
                                '<td>' + sucursales[i].url + '</td>' +
                                '<td>' + sucursales[i].horarios + '</td>' +
                                '<td>' + sucursales[i].calle + '</td>' +
                                '<td>' + sucursales[i].numCalle + '</td>' +
                                '<td>' + sucursales[i].colonia + '</td>' +
                                '<td>' + sucursales[i].activoSucursal + '</td>' +
                                '<td class="text-center">' + 
                                    '<a id="detAlimento" href="#" class="zarape-action-link"' +
                                        'onclick="cargarDetalleSucursal(' + i + ')">' +
                                        '<i class="fas fa-eye"></i>' +
                                    '</a>' +
                                '</td>' +
                            '</tr>';
                    
        }
    }
    
    // Una vez que se termino de recorrer el arreglo de alimentos, se coloca
    // el contenido de la tabla dentro del <tbody> correspondiente:
    document.getElementById('tbodySucursal').innerHTML = contenido;
    
}

/**
 * Esta funcion carga los datos de un alimento en el formulario.
 * La funcion requiere del parametro [pos] que representa la posicion
 * del alimento dentro del arreglo de alimentos.
 */
export function cargarDetalleSucursal(pos)
{    
    let s = sucursales[pos];
    
    limpiarFormulario();
    
    if (s == null)
    {
        Swal.fire('', 'No se encontraron los datos del alimento.', 'error');
    }
    else
    {
        document.getElementById("txtIdSucursal").value = s.idSucursal;
        document.getElementById("txtNombre").value = s.nombreSucursal;
        document.getElementById("txtLatitud").value = s.latitud;
        document.getElementById("txtLongitud").value = s.longitud;
        document.getElementById("txtUrl").value = s.url;
        document.getElementById("txtHorarios").value = s.horarios;
        document.getElementById("txtCalle").value = s.calle;
        document.getElementById("txtNumCalle").value = s.numCalle;
        document.getElementById("txtColonia").value = s.colonia;
        document.getElementById("cmbCiudad").value = s.ciudad.idCiudad;
        let lat = s.latitud;
        let lon = s.longitud;
        
        
        if (s.fotoSucursal != null)
            document.getElementById("txtaFoto").value = s.fotoSucursal;
        else
            document.getElementById("txtaFoto").value = '';
       
        setDetalleVisible(true);
        setMapaVisible(true);
        // Inicializar o actualizar el mapa
        if (map === null) {
            // Si el mapa no existe, inicialízalo
            inicializarMapa(lat, lon);
        } else {
            // Si el mapa ya existe, solo actualiza su posición
            actualizarMapa(lat, lon);
        }
    }
}

export function limpiarMostrarFormulario()
{
    limpiarFormulario();
    setDetalleVisible(true);
    setMapaVisible(false);
}

export function limpiarFormulario()
{
    document.getElementById("txtIdSucursal").value = '';
    document.getElementById("txtNombre").value = '';
    document.getElementById("txtLatitud").value = '';
    document.getElementById("txtLongitud").value = '',
    document.getElementById("txtUrl").value = '',
    document.getElementById("txtHorarios").value = '',
    document.getElementById("txtCalle").value = '',
    document.getElementById("txtNumCalle").value = '',
    document.getElementById("txtColonia").value = '',
    document.getElementById("cmbCiudad").selectedIndex = -1;
    document.getElementById("txtaFoto").value = '';
    document.getElementById("imgFoto").src = '';
    document.getElementById("inputFoto").value = '';
    setMapaVisible(false);
}

export function setDetalleVisible(value)
{
    if (value)
    {
        document.getElementById("divCatalogo").style.display = 'none';
        document.getElementById("divDetalle").style.display = '';
    }
    else
    {
        document.getElementById("divDetalle").style.display = 'none';
        document.getElementById("divCatalogo").style.display = '';
    }
}

// funcion para mostrar y ocultar mapa
export function setMapaVisible(value)
{
    if (value)
    {
        document.getElementById("mapContainer").style.display = '';
    }
    else
    {
        document.getElementById("mapContainer").style.display = 'none';
    }
}

/*******************************************************************************
 * Las siguientes funciones permiten la manipulacion de imagenes               *
 * para representarlas como una cadena de texto y viceversa,                   *
 * con el objetivo de que se puedan enviar y recibir desde los servicios REST  *
 ******************************************************************************/

// Esta funcion carga la fotografia que el usuario elije y la representa
// como una cadena utilizando el algoritmo de la Base64:
function cargarFotografia()
{
    //Revisamos que el usuario haya seleccionado un archivo:
    if (inputFileFotoProducto.files && inputFileFotoProducto.files[0]) 
    {
        let reader = new FileReader();

        //Agregamos un oyente al lector del archivo para que,
        //en cuanto el usuario cargue una imagen, esta se lea
        //y se convierta de forma automatica en una cadena de Base64:
        reader.onload = function (e) 
        {
            let fotoB64 = e.target.result;
            document.getElementById("imgFoto").src = fotoB64;            
            document.getElementById("txtaFoto").value = 
                    fotoB64.substring(fotoB64.indexOf(",") + 1, fotoB64.length);
        };

        //Leemos el archivo que selecciono el usuario y lo
        //convertimos en una cadena con la Base64:
        reader.readAsDataURL(inputFileFotoProducto.files[0]);            
    }
}
function inicializarMapa(lat, lon) {
    // Variables locales dentro de initMap
            const apiKey = "R88dImN6kfYEPkoMF87bn1CuY7bqKClFhBYvRFgsCwo"; // Aquí coloca tu API Key de Here Maps
            const latitude = lat; // Coordenadas de latitud
            const longitude = lon; 
            const mapContainer = document.getElementById('mapContainer');

    // Inicializar la plataforma de Here Maps con la API Key
    const platform = new H.service.Platform({
        apikey: apiKey
    });

    // Crear capas predeterminadas
    const defaultLayers = platform.createDefaultLayers();

    // Crear el mapa dentro del contenedor
     map = new H.Map(
        mapContainer,
        defaultLayers.vector.normal.map,
        {
            center: { lat: latitude, lng: longitude },
            zoom: 10, // Nivel de zoom inicial
            pixelRatio: window.devicePixelRatio || 1
        }
    );

    // Asegurar que el mapa se redimensione correctamente al cambiar el tamaño de la ventana
    window.addEventListener('resize', () => map.getViewPort().resize());

    // Habilitar eventos de interacción (zoom, pan, etc.)
    behavior = new H.mapevents.Behavior(new H.mapevents.MapEvents(map));

    // Agregar la interfaz de usuario predeterminada
    ui = H.ui.UI.createDefault(map, defaultLayers);
    
    // Crear un marcador en las coordenadas dadas
    marker = new H.map.Marker({ lat: latitude, lng: longitude });

    // Agregar el marcador al mapa
    map.addObject(marker);

    console.log("El mapa se ha inicializado correctamente.");
}
function actualizarMapa(lat, lon) {
    let latitude = lat;
    let longitude = lon;
    // Establecer la nueva posición del mapa
    map.setCenter({ lat: latitude, lng: longitude });
    map.Marker({ lat: latitude, lng: longitude });
    // Opcional: ajustar el nivel de zoom si es necesario
    map.setZoom(14);
}
// Llamar a esta función con la latitud y longitud
window.cargarDetalleSucursal = cargarDetalleSucursal;
window.inicializarMapa = inicializarMapa;
window.actualizarMapa = actualizarMapa;
