
function mensaje() {

    alert('correcto!');
}

function mensaje2() {
  swal({
    title: 'Estas Seguro de eliminar?',
    text: "",
    type: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Si, eliminar!'
  }).then(function() {
    swal(
      'Eliminar!',
      'El registro fue eliminado.',
      'success'
    )
    return true;
  })
}
function confirmEliminar(){   
return confirm('Estas seguro de eliminar?');
}

function confirmCloseSession(){   
return confirm('Estas seguro cerrar sesion?');
}

function confirmDel() {
return  swal({
            title: 'confirmacion',
            html: ' Estas seguro? ',
            type: 'warning',
            showCancelButton: true,
            closeOnConfirm: true,
            closeOnCancel: true
        }, function (isConfirm) {
            if (isConfirm) {
                return true;
            } else {
                return false;
            }
        });
}