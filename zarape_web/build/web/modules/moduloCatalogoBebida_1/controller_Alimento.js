document.getElementById("btnLimpiarMostrarFormulario").addEventListener('click', limpiarMostrarFormulario);
document.getElementById("btnSave").addEventListener('click', save);
document.getElementById("btnDelete").addEventListener('click', _delete);
document.getElementById("btnLimpiarFormulario").addEventListener('click', limpiarFormulario);
document.getElementById("btnSetDetalleVisible").addEventListener('click', setDetalleVisible(false));
document.getElementById("btnIniciar").addEventListener('click', inicializar);

let categorias = [];
let bebidas = [];

let inputFileFotoProducto = null;

export function inicializar()
{    
    recargarComboBoxCategorias();
    recargarTablaBebidas();
    setDetalleVisible(false);
    
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
    let url = 'http://localhost:8080/zarape_web/api/bebida/save';
    let bebida =  {
                        idBebida : 0,
                        producto :  {
                                        idProducto          : 0,
                                        nombreProducto      : document.getElementById("txtNombre").value,
                                        precioProducto      : parseFloat(document.getElementById("txtPrecio").value),
                                        descripcionProducto : document.getElementById("txtaDescripcion").value,
                                        fotoProducto        : document.getElementById("txtaFoto").value,
                                        categoria   : {
                                                        idCategoria : parseInt(document.getElementById("cmbCategoria").value)
                                                      }
                                    }
                    };
                    
    let datos = null;
    
    let params = null;
    
    let opciones = null;
    
    let resp = null;
    
    let data = null;
                    
    if (document.getElementById("txtIdBebida").value.trim() != '')
    {
        bebida.idBebida = parseInt(document.getElementById("txtIdBebida").value.trim());
    }
    
    if (document.getElementById("txtIdProducto").value.trim() != '')
    {
        bebida.producto.idProducto = parseInt(document.getElementById("txtIdProducto").value.trim());
    }
    
    datos = {datosBebida : JSON.stringify(bebida)};
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
        document.getElementById("txtIdBebida").value = data.idBebida;
        document.getElementById("txtIdProducto").value = data.producto.idProducto;
        recargarTablaBebida();
        Swal.fire('Datos de alimento guardados con &eacute;xito.', '', 'success');
    }
}

// A esta funcion se le puso un guion bajo antes del nombre
// porque "delete" es una palabra reservada en JavaScript:
export async function _delete()
{
    let url = 'api/bebida/delete';
    let idProducto = 0;
    
    let datos = null;
    
    let params = null;
    
    let opciones = null;
    
    let resp = null;
    
    let data = null;
    
    if (document.getElementById("txtIdProducto").value.trim() != '')
    {
        idProducto = parseInt(document.getElementById("txtIdProducto").value.trim());
    }
    else
    {
        Swal.fire('Seleccione un producto para eliminarlo.', '', 'warning');
        return;
    }
    
    datos = {idProducto : idProducto};
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
        recargarTablaBebidas();
        setDetalleVisible(false);
        Swal.fire('Registro de alimento eliminado con &eacute;xito.', '', 'success');
    }
}

/**
 * Esta funcion consulta las categorias por medio de un servicio REST
 * y posteriormente, se llena un ComboBox.
 */
export async function recargarComboBoxCategorias()
{
    // Definimos la URL del servicio:
    let url = 'http://localhost:8080/zarape_web/api/categoria/getAllByTipo?tipo=A';
    
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
        Swal.fire('Error al consultar categorias de bebidas.', datos.error, 'error');        
    }
    else
    {
        // Se guarda el arreglo de categorias en una variable global del modulo:
        categorias = datos;
        
        // Se recorre el arreglo de alimentos:
        for (let i = 0; i < categorias.length; i++)
        {
            // Por cada alimento, se genera una opcion para el ComboBox:
            contenido +=    '<option value="' + categorias[i].idCategoria + '">' +
                                categorias[i].descripcionCategoria + 
                            '</option>';
        }
    }
    
    // Una vez que se termino de recorrer el arreglo de categorias, se coloca
    // el contenido de la tabla dentro del <select> correspondiente:
    document.getElementById('cmbCategoria').innerHTML = contenido;
}

/**
 * Con esta funcion se consultan los alimentos a traves del servicio REST
 * y con los datos devueltos, se genera el contenido de la tabla de forma
 * dinamica.
 */
export async function recargarTablaBebidas()
{
    // Definimos la URL del servicio:
    let url = "http://localhost:8080/zarape_web/api/bebida/getAll";
    
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
        alimentos = datos;
        
        // Se recorre el arreglo de alimentos:
        for (let i = 0; i < alimentos.length; i++)
        {
            // Por cada alimento, se genera un renglon de tabla, con cada una
            // de sus columnas:
            contenido +=    '<tr>' +
                                '<td>' + bebidas[i].producto.nombreProducto + '</td>' +
                                '<td class="text-end">' + bebidas[i].producto.precioProducto + '</td>' +
                                '<td>' + bebidas[i].producto.categoria.descripcionCategoria + '</td>' +
                                '<td>' + bebidas[i].producto.activoProducto + '</td>' +
                                '<td class="text-center">' + 
                                    '<a id="detBebida" href="#" class="zarape-action-link"' +
                                        'onclick="cargarDetalleBebida(' + i + ')">' +
                                        '<i class="fas fa-eye"></i>' +
                                    '</a>' +
                                '</td>' +
                            '</tr>';
                    
        }
    }
    
    // Una vez que se termino de recorrer el arreglo de alimentos, se coloca
    // el contenido de la tabla dentro del <tbody> correspondiente:
    document.getElementById('tbodyBebidas').innerHTML = contenido;
    
}

/**
 * Esta funcion carga los datos de un alimento en el formulario.
 * La funcion requiere del parametro [pos] que representa la posicion
 * del alimento dentro del arreglo de alimentos.
 */
export function cargarDetalleBebida(pos)
{    
    let b = bebidas[pos];
    
    limpiarFormulario();
    
    if (b == null)
    {
        Swal.fire('', 'No se encontraron los datos del bebida.', 'error');
    }
    else
    {
        document.getElementById("txtIdBebida").value = a.idBebida;
        document.getElementById("txtIdProducto").value = a.producto.idProducto;
        document.getElementById("txtNombre").value = a.producto.nombreProducto;
        document.getElementById("txtPrecio").value = a.producto.precioProducto;
        document.getElementById("cmbCategoria").value = a.producto.categoria.idCategoria;
        document.getElementById("txtaDescripcion").value = a.producto.descripcionProducto;
        
        if (a.producto.fotoProducto != null)
            document.getElementById("txtaFoto").value = a.producto.fotoProducto;
        else
            document.getElementById("txtaFoto").value = '';
        
        setDetalleVisible(true);
    }
}

export function limpiarMostrarFormulario()
{
    limpiarFormulario();
    setDetalleVisible(true);
}

export function limpiarFormulario()
{
    document.getElementById("txtIdBebida").value = '';
    document.getElementById("txtIdProducto").value = '';
    document.getElementById("txtNombre").value = '';
    document.getElementById("txtPrecio").value = '';
    document.getElementById("cmbCategoria").selectedIndex = -1;
    document.getElementById("txtaDescripcion").value = '';
    document.getElementById("txtaFoto").value = '';
    document.getElementById("imgFoto").src = '';
    document.getElementById("inputFoto").value = '';
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
window.cargarDetalleBebida = cargarDetalleBebida;