document.getElementById("btnLimpiarMostrarFormulario").addEventListener('click', limpiarMostrarFormulario);
document.getElementById("btnSave").addEventListener('click', save);
document.getElementById("btnDelete").addEventListener('click', _delete);
document.getElementById("btnLimpiarFormulario").addEventListener('click', limpiarFormulario);
document.getElementById("btnSetDetalleVisible").addEventListener('click', () => setDetalleVisible(false));

let categorias = [];
let bebidas = [];

let inputFileFotoProducto = null;

export function inicializar()
{    
    recargarComboBoxCategorias();
    recargarTablaBebidas();
    setDetalleVisible(false);
    
    inputFileFotoProducto = document.getElementById("inputFoto");
    inputFileFotoProducto.onchange = function(evt){cargarFotografia();};
    document.getElementById("btnCargarFoto").onclick = function(evt) { inputFileFotoProducto.click(); };
}

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
        recargarTablaBebidas();
        Swal.fire('Datos de bebida guardados con &eacute;xito.', '', 'success');
    }
}

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
        Swal.fire('Registro de bebida eliminado con &eacute;xito.', '', 'success');
    }
}

export async function recargarComboBoxCategorias()
{
    let url = 'http://localhost:8080/zarape_web/api/categoria/getAllByTipo?tipo=B';
    let resp = await fetch(url);
    let datos = await resp.json();
    let contenido = '';
    
    if (datos.error != null)
    {
        Swal.fire('Error al consultar categorias de bebidas.', datos.error, 'error');        
    }
    else
    {
        categorias = datos;
        
        for (let i = 0; i < categorias.length; i++)
        {
            contenido +=    '<option value="' + categorias[i].idCategoria + '">' +
                                categorias[i].descripcionCategoria + 
                            '</option>';
        }
    }
    
    document.getElementById('cmbCategoria').innerHTML = contenido;
}

export async function recargarTablaBebidas()
{
    let url = "http://localhost:8080/zarape_web/api/bebida/getAll";
    let resp = await fetch(url);
    let datos = await resp.json();
    let contenido = '';
    
    if (datos.error != null)
    {
        Swal.fire('Error al consultar bebidas.', datos.error, 'error');
    }
    else
    {
        bebidas = datos;
        
        for (let i = 0; i < bebidas.length; i++)
        {
            contenido +=    '<tr>' +
                                '<td>'+bebidas[i].idBebida+'</td>'+
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
    
    document.getElementById('tbodyBebidas').innerHTML = contenido;
}

export function cargarDetalleBebida(pos)
{    
    let b = bebidas[pos];
    limpiarFormulario();
    
    if (b == null)
    {
        Swal.fire('', 'No se encontraron los datos de la bebida.', 'error');
    }
    else
    {
        document.getElementById("txtIdBebida").value = b.idBebida;
        document.getElementById("txtIdProducto").value = b.producto.idProducto;
        document.getElementById("txtNombre").value = b.producto.nombreProducto;
        document.getElementById("txtPrecio").value = b.producto.precioProducto;
        document.getElementById("cmbCategoria").value = b.producto.categoria.idCategoria;
        document.getElementById("txtaDescripcion").value = b.producto.descripcionProducto;
        
        if (b.producto.fotoProducto != null)
            document.getElementById("txtaFoto").value = b.producto.fotoProducto;
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