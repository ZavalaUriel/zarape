document.getElementById("btnAddEmpleado").addEventListener('click', save);
document.getElementById("btnDeleteEmpleado").addEventListener('click', _delete);
document.getElementById("btnLimpiarFormulario").addEventListener('click', limpiarFormulario);
document.getElementById("btnSetDetalleVisible").addEventListener('click', () => setDetalleVisible(false));
document.getElementById("btnLimpiarMostrarFormulario").addEventListener('click', limpiarMostrarFormulario);
let empleados = [];
let ciudades = [];
let sucursales=[];

export function inicializar()
{        
    recargarTablaEmpleados();
    recargarComboBoxCiudades();
    recargarComboBoxSucursales();
    setDetalleVisible(false);
}

/**
 * Esta funcion guarda los datos de un empleado.
 */
export async function save()
{
    let url = 'http://localhost:8080/zarape_web/api/empleado/save';
    let empleado =  {
                        idEmpleado : 0,
                        sucursal :  {
                                        idSucursal  : parseInt(document.getElementById("cmbSucursales").value)
                                    },
                                    persona   : {
                                                        idPersona : 0,
                                                        nombrePersona: document.getElementById("txtNombrePersona").value,
                                                        apellidos : document.getElementById("txtApellidoPersona").value,
                                                        telefono : document.getElementById("txtTelefono").value,
                                                        ciudad : {
                                                            idCiudad : parseInt(document.getElementById("cmbCiudad").value)
                                                        }
                                                      },
                                                      usuario : {
                                                          idUsuario: 0,
                                                          nombreUsuario: document.getElementById("txtNombreUsuario").value,
                                                          contrasenia : document.getElementById("txtPasswordUser").value
                                                      },
                                                      activoEmpleado : 0                          
                    };
                    
    let datos = null;
    
    let params = null;
    
    let opciones = null;
    
    let resp = null;
    
    let data = null;
    
    if (document.getElementById("txtIdEmpleado").value.trim() != ''){
        empleado.idEmpleado = parseInt(document.getElementById("txtIdEmpleado").value.trim());
    }
        if (document.getElementById("txtIdUsuario").value.trim() != ''){
        empleado.usuario.idUsuario = parseInt(document.getElementById("txtIdUsuario").value.trim());
    }
        if (document.getElementById("txtIdPersona").value.trim() != ''){
        empleado.persona.idPersona = parseInt(document.getElementById("txtIdPersona").value.trim());
    }
    
    datos = {datosEmpleado : JSON.stringify(empleado)};
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
        document.getElementById("txtIdEmpleado").value = data.idEmpleado;
        document.getElementById("txtIdPersona").value = data.persona.idPersona;
        document.getElementById("txtIdUsuario").value = data.usuario.idUsuario;
        recargarTablaEmpleados();
        Swal.fire('Datos de empleado guardados con &eacute;xito.', '', 'success');
    }
}

// A esta funcion se le puso un guion bajo antes del nombre
// porque "delete" es una palabra reservada en JavaScript:
export async function _delete()
{
    let url = 'http://localhost:8080/zarape_web/api/empleado/delete';
    let idEmpleado = 0;
    
    let datos = null;
    
    let params = null;
    
    let opciones = null;
    
    let resp = null;
    
    let data = null;
    
    if (document.getElementById("txtIdEmpleado").value.trim() != '')
    {
        idEmpleado = parseInt(document.getElementById("txtIdEmpleado").value.trim());
    }
    else
    {
        Swal.fire('Seleccione un empleado para eliminarlo.', '', 'warning');
        return;
    }
    
    datos = {idEmpleado : idEmpleado};
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
        recargarTablaEmpleados();
        setDetalleVisible(false);
        Swal.fire('Registro de empleado eliminado con &eacute;xito.', '', 'success');
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
    // de los empleados que devolvio el servicio:
    let contenido = '';
    let contenidoE = '';
    // Verificamos si hubo al gun error:
    if (datos.error != null)
    {
        Swal.fire('Error al consultar.', datos.error, 'error');        
    }
    else
    {
        // Se guarda el arreglo de las ciudades en una variable global del modulo:
        ciudades = datos;
        
        // Se recorre el arreglo de ciudades:
        for (let i = 0; i < ciudades.length; i++)
        {
            // Por cada empleado, se genera una opcion para el ComboBox:
            contenido +=    '<option value="' + ciudades[i].idCiudad + '">' +
                                ciudades[i].nombreCiudad + 
                            '</option>';
        }
    }
    document.getElementById('cmbCiudad').innerHTML = contenido;
    }
    
    // Una vez que se termino de recorrer el arreglo de ciudades, se coloca
    // el contenido de la tabla dentro del <select> correspondiente:
    

export async function recargarComboBoxSucursales()
{
    // Definimos la URL del servicio:
    let url = 'http://localhost:8080/zarape_web/api/sucursal/getAll';
    
    // Invocamos el servicio:
    let resp = await fetch(url);
    
    // Convertimos la respuesta del servicio en un documento JSON:
    let datos = await resp.json();
    
    // Aqui se guardara el contenido HTML de la tabla, con los datos
    // de los empleados que devolvio el servicio:
     let contenido = '';
    // Verificamos si hubo algun error:
    if (datos.error != null)
    {
        Swal.fire('Error al consultar', datos.error, 'error');        
    }
    else
    {
        // Se guarda el arreglo de categorias en una variable global del modulo:
        sucursales = datos;
        
        // Se recorre el arreglo de alimentos:
        for (let i = 0; i < sucursales.length; i++)
        {
            // Por cada alimento, se genera una opcion para el ComboBox:
            contenido +=    '<option value="' + sucursales[i].idSucursal + '">' +
                                sucursales[i].nombreSucursal + 
                            '</option>';
        }
    }
    
    // Una vez que se termino de recorrer el arreglo de sucursales, se coloca
    // el contenido de la tabla dentro del <select> correspondiente:
    document.getElementById('cmbSucursales').innerHTML = contenido;
}
/**
 * Con esta funcion se consultan los empleados a traves del servicio REST
 * y con los datos devueltos, se genera el contenido de la tabla de forma
 * dinamica.
 */
export async function recargarTablaEmpleados()
{
    // Definimos la URL del servicio:
    let url = 'http://localhost:8080/zarape_web/api/empleado/getAll';
    
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
        Swal.fire('Error al consultar.', datos.error, 'error');
    }
    else
    {
        // Se guarda el arreglo de alimentos en una variable global del modulo:
        empleados = datos;
        
        // Se recorre el arreglo de alimentos:
        for (let i = 0; i < empleados.length; i++)
        {
            // Por cada alimento, se genera un renglon de tabla, con cada una
            // de sus columnas:
            contenido +=    '<tr>' +
                                '<td>' + empleados[i].idEmpleado + '</td>' +
                                '<td>' + empleados[i].persona.nombrePersona+ '</td>' +
                                '<td>' + empleados[i].persona.apellidos + '</td>' +
                                '<td>' + empleados[i].persona.telefono + '</td>' +
                                '<td>' + empleados[i].usuario.nombreUsuario + '</td>' +
                                //'<td>' + empleados[i]. + '</td>' +
                                '<td>' + empleados[i].persona.ciudad.nombreCiudad + '</td>' +
                                '<td>' + empleados[i].sucursal.nombreSucursal + '</td>' +
                                '<td>' + empleados[i].activoEmpleado + '</td>' +
                                '<td class="text-center">' + 
                                    '<a id="detEmpleado" href="#" class="zarape-action-link"' +
                                        'onclick="cargarDetalleEmpleado(' + i + ')">' +
                                        '<i class="fas fa-eye"></i>' +
                                    '</a>' +
                                '</td>' +
                            '</tr>';
        }
    }
    
    // Una vez que se termino de recorrer el arreglo de alimentos, se coloca
    // el contenido de la tabla dentro del <tbody> correspondiente:
    document.getElementById('tblEmpleados').innerHTML = contenido;
    
}

export function limpiarMostrarFormulario()
{
    limpiarFormulario();
    setDetalleVisible(true);
}

export function limpiarFormulario()
{
    document.getElementById("txtIdEmpleado").value = '';
    document.getElementById("txtIdPersona").value = '';
    document.getElementById("txtIdUsuario").value = '';
    document.getElementById("txtNombrePersona").value = '';
    document.getElementById("txtApellidoPersona").value = '';
    document.getElementById("txtTelefono").value = '';
    document.getElementById("txtNombreUsuario").value = '';
    document.getElementById("txtPasswordUser").value = '';
    document.getElementById("cmbCiudad").selectedIndex = -1;
    document.getElementById("cmbSucursales").selectedIndex = -1;
}

export function cargarDetalleEmpleado(pos)
{    
    let e = empleados[pos];
    
    limpiarFormulario();
    
    if (e == null)
    {
        Swal.fire('', 'No se encontraron los datos del empleado.', 'error');
    }
    else
    {
        document.getElementById("txtIdEmpleado").value = e.idEmpleado;
        document.getElementById("txtIdPersona").value = e.persona.idPersona;
        document.getElementById("txtIdUsuario").value = e.usuario.idUsuario;
        document.getElementById("txtNombrePersona").value = e.persona.nombrePersona;
        document.getElementById("txtApellidoPersona").value = e.persona.apellidos;
        document.getElementById("txtTelefono").value = e.persona.telefono;
        document.getElementById("txtNombreUsuario").value = e.usuario.nombreUsuario;
        document.getElementById("cmbSucursales").value = e.sucursal.idSucursal;
        document.getElementById("cmbCiudad").value = e.persona.ciudad.idCiudad;
        
        
       
        setDetalleVisible(true);
        
    }
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
window.cargarDetalleEmpleado = cargarDetalleEmpleado;