let moduloSucursal;
let moduloUsuario;
let moduloBebida;
let moduloAlimento;
let moduloCombo;
let moduloLogin;
let moduloCliente;
let cm; //current Moodule

document.getElementById('btnSucursal').addEventListener('click',cargarModuloSucursal);
document.getElementById('btnUsuario').addEventListener('click',cargarModuloUsuario);
document.getElementById('btnBebida').addEventListener('click', cargarModuloBebida);
document.getElementById('btnAlimento').addEventListener('click', cargarModuloAlimento);
// document.getElementById('btnCombo').addEventListener('click', cargarModuloCombo);
document.getElementById('btnLogin').addEventListener('click', cargarLogin);
document.getElementById('btnCliente').addEventListener('click', cargarCliente);

async function cargarModuloSucursal(){
   let url="http://localhost:8080/zarape_web/modules/moduloSucursal/view_Sucursal.html";
   let resp = await fetch(url);
   let contenido = await resp.text();
   document.getElementById('contenedorPrincipal').innerHTML = contenido;
   cm = await import("http://localhost:8080/zarape_web/modules/moduloSucursal/controller_Sucursal.js");
   cm.inicializar();
}
async function cargarModuloUsuario(){
   let url="http://localhost:8080/zarape_web/modules/moduloUsuario/view_Usuario.html";
   let resp = await fetch(url);
   let contenido = await resp.text();
   document.getElementById('contenedorPrincipal').innerHTML = contenido;
   cm = await import("http://localhost:8080/zarape_web/modules/moduloUsuario/controller_Usuario.js");
   cm.inicializar();
}
async function cargarModuloBebida(){
   let url="http://localhost:8080/zarape_web/modules/moduloCatalogoBebida/view_Bebida.html";
   let resp = await fetch(url);
   let contenido = await resp.text();
   document.getElementById('contenedorPrincipal').innerHTML = contenido;
   cm = await import("http://localhost:8080/zarape_web/modules/moduloCatalogoBebida/controller_Bebida.js");
   cm.inicializar();
}

async function cargarModuloAlimento(){
   let url="http://localhost:8080/zarape_web/modules/moduloCatalogoAlimento/view_Alimento.html";
   let resp = await fetch(url);
   let contenido = await resp.text();
   document.getElementById('contenedorPrincipal').innerHTML = contenido;
   cm = await import("http://localhost:8080/zarape_web/modules/moduloCatalogoAlimento/controller_Alimento.js");
   cm.inicializar();
}
//async function cargarModuloCombo(){
//    fetch("http://localhost:8080/zarape_web/modules/moduloDetalleCombo/view_Combo.html")
  //          .then( (response)=>{
    //                return response.text();
      //          }
        //    )
          //  .then((html)=>{
            //        document.getElementById("contenedorPrincipal").innerHTML = html;
              //      import ("http://proyectozarape.test/web/modules/moduloDetalleCombo/controller_Combo.js").then(
                //            function(controller){
                  //              moduloCombo = controller;
                    //        }
                      //      );
               // }
           // ).catch((err)=>console.log(err));
//}

async function cargarCliente(){
    let url = "http://localhost:8080/zarape_web/modules/moduloCliente/view_Cliente.html";
    let resp = await fetch(url);
    let contenido = await resp.text();
    
    document.getElementById("contenedorPrincipal").innerHTML = contenido;
    cm = await import("http://localhost:8080/zarape_web/modules/moduloCliente/controller_Cliente.js");
    cm.inicializar();
    
    
}

async function cargarLogin(){
   let url="http://localhost:8080/zarape_web/modules/moduloLogin/index.html";
   let resp = await fetch(url);
   let contenido = await resp.text();
   document.getElementById('contenedorPrincipal').innerHTML = contenido;
   cm = await import("http://localhost:8080/zarape_web/modules/moduloLogin/controller_Login.js");
   cm.inicializar();
}