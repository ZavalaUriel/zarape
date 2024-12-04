document.getElementById("btnSave").addEventListener('click', save);
document.getElementById("btnLimpiarMostrarFormulario").addEventListener('click', limpiarMostrarFormulario);
document.getElementById("btnDelete").addEventListener('click', _delete);
document.getElementById("btnLimpiarFormulario").addEventListener('click', limpiarFormulario);
document.getElementById("btnSetDetalleVisible").addEventListener('click', () => setDetalleVisible(false));

let clientes = [];
let ciudades = [];

export function inicializar()
{    
    recargarComboBoxCiudades();
    recargarTablaClientes();
    setDetalleVisible(false);
}

/**
 * Esta funcion guarda los datos de un cliente.
 */
export async function save()
{
    let url = 'api/cliente/save';
    let cliente =  {
        idCliente: 0,
        persona: {
            idPersona: 0,
            nombrePersona: document.getElementById("txtNombrePersona").value,
            apellidos: document.getElementById("txtApellidoPersona").value,
            telefono: document.getElementById("txtTelefono").value,
            ciudad: {
                idCiudad: parseInt(document.getElementById("cmbCiudad").value)
            }
        }
    };

    let datos = null;
    let params = null;
    let opciones = null;
    let resp = null;
    let data = null;

    if (document.getElementById("txtIdCliente").value.trim() != '')
    {
        cliente.idCliente = parseInt(document.getElementById("txtIdCliente").value.trim());
    }

    if (document.getElementById("txtIdPersona").value.trim() != '')
    {
        cliente.persona.idPersona = parseInt(document.getElementById("txtIdPersona").value.trim());
    }

    datos = {datosCliente: JSON.stringify(cliente)};
    params = new URLSearchParams(datos);
    opciones =  {
        method: "POST",
        headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
        body: params
    };

    resp = await fetch(url, opciones);
    data = await resp.json();

    if (data.error != null)
    {
        Swal.fire('', data.error, 'error');
    }
    else
    {
        document.getElementById("txtIdCliente").value = data.idCliente;
        document.getElementById("txtIdPersona").value = data.persona.idPersona;
        recargarTablaClientes();
        Swal.fire('Datos de cliente guardados con &eacute;xito.', '', 'success');
    }
}

// A esta funcion se le puso un guion bajo antes del nombre
// porque "delete" es una palabra reservada en JavaScript:
export async function _delete()
{
    let url = 'api/cliente/delete';
    let idCliente = 0;
    
    let datos = null;
    let params = null;
    let opciones = null;
    let resp = null;
    let data = null;
    
    if (document.getElementById("txtIdCliente").value.trim() != '')
    {
        idCliente = parseInt(document.getElementById("txtIdCliente").value.trim());
    }
    else
    {
        Swal.fire('Seleccione un cliente para eliminarlo.', '', 'warning');
        return;
    }
    
    datos = {idCliente: idCliente};
    params = new URLSearchParams(datos);
    opciones =  {
        method: "POST",
        headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
        body: params
    };
    
    resp = await fetch(url, opciones);
    data = await resp.json();
    
    if (data.error != null)
    {
        Swal.fire('', data.error, 'error');
    }
    else
    {
        recargarTablaClientes();
        setDetalleVisible(false);
        Swal.fire('Registro de cliente eliminado con &eacute;xito.', '', 'success');
    }
}

/**
 * Esta funcion consulta las ciudades por medio de un servicio REST
 * y posteriormente, llena un ComboBox.
 */
export async function recargarComboBoxCiudades()
{   
    let url = 'http://localhost:8080/zarape_web/api/cliente/getAllCiudades';
    let resp = await fetch(url);
    let datos = await resp.json();
    let contenido = '';
    
    if (datos.error != null)
    {
        Swal.fire('Error al consultar.', datos.error, 'error');        
    }
    else
    {
        ciudades = datos;
        
        for (let i = 0; i < ciudades.length; i++)
        {
            contenido += '<option value="' + ciudades[i].idCiudad + '">' +
                         ciudades[i].nombreCiudad + 
                         '</option>';
        }
        document.getElementById('cmbCiudad').innerHTML = contenido;
    }       
}

/**
 * Esta funcion consulta los clientes a traves del servicio REST
 * y genera el contenido de la tabla de forma dinamica.
 */
export async function recargarTablaClientes()
{
    let url = 'api/cliente/getAll';
    let resp = await fetch(url);
    let datos = await resp.json();
    let contenido = '';
    
    if (datos.error != null)
    {
        Swal.fire('Error al consultar.', datos.error, 'error');
    }
    else
    {
        clientes = datos;
        
        for (let i = 0; i < clientes.length; i++)
        {
            contenido += '<tr>' +
                         '<td>' + clientes[i].idCliente + '</td>' +
                         '<td>' + clientes[i].persona.nombrePersona + '</td>' +
                         '<td>' + clientes[i].persona.apellidos + '</td>' +
                         '<td>' + clientes[i].persona.telefono + '</td>' +
                         '<td>' + clientes[i].persona.ciudad.nombreCiudad + '</td>' +
                         '<td>' + clientes[i].activoCliente + '</td>' +
                         '<td class="text-center">' + 
                             '<a id="detCliente" href="#" class="zarape-action-link"' +
                             'onclick="cargarDetalleCliente(' + i + ')">' +
                             '<i class="fas fa-eye"></i>' +
                             '</a>' +
                         '</td>' +
                         '</tr>';
        }
    }
    document.getElementById('tblClientes').innerHTML = contenido;
}

/**
 * Carga los datos de un cliente en el formulario.
 */
export function cargarDetalleCliente(pos)
{    
    let c = clientes[pos];
    limpiarFormulario();
    
    if (c == null)
    {
        Swal.fire('', 'No se encontraron los datos del cliente.', 'error');
    }
    else
    {
        document.getElementById("txtIdCliente").value = c.idCliente;
        document.getElementById("txtIdPersona").value = c.persona.idPersona;
        document.getElementById("txtNombrePersona").value = c.persona.nombrePersona;
        document.getElementById("txtApellidoPersona").value = c.persona.apellidos;
        document.getElementById("txtTelefono").value = c.persona.telefono;
        document.getElementById("cmbCiudad").value = c.persona.ciudad.idCiudad;
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
    document.getElementById("txtIdCliente").value = '';
    document.getElementById("txtIdPersona").value = '';
    document.getElementById("txtNombrePersona").value = '';
    document.getElementById("txtApellidoPersona").value = '';
    document.getElementById("txtTelefono").value = '';
    document.getElementById("cmbCiudad").selectedIndex = 0;
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

window.cargarDetalleCliente = cargarDetalleCliente;