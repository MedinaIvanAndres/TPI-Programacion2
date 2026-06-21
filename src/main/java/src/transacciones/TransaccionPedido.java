package src.transacciones;

import src.entities.Pedido;
import src.entities.Producto;
import src.entities.Usuario;
import src.enums.Estado;
import src.enums.FormaPago;
import src.exception.EntidadNoEncontradaException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransaccionPedido {
    private final List<Pedido> pedidos = new ArrayList<>();

    public List<Pedido> getPedidos() {
        return Collections.unmodifiableList(pedidos);
    }

    public void listar() {
        boolean listaVacia = true;
        for (Pedido pedido : pedidos) {
            if (!pedido.isEliminado()) {
                listaVacia = false;
                System.out.println("\n"+pedido.toString());
                pedido.listarDetalles();
            }
        }
        if (listaVacia) {
            System.out.println("\nATENCION: No hay pedidos cargados");
        }
    }

    public void listarPorUsuario(long idUsuario) {
        boolean listaVacia = true;
        for (Pedido pedido : pedidos) {
            if (!pedido.isEliminado() && pedido.getUsuario() != null && pedido.getUsuario().getId() == idUsuario) {
                listaVacia = false;
                System.out.println("\n"+pedido.toString());
                pedido.listarDetalles();
            }
        }
        if (listaVacia) {
            System.out.println("\nATENCION: No hay pedidos cargados para el usuario con id: #" + idUsuario);
        }
    }

    public Pedido crear(Estado estado, FormaPago formaPago, Usuario usuario) {
        if (usuario == null) {
            throw new EntidadNoEncontradaException("\n ERROR: No se puede crear un pedido sin un usuario asignado");
        }
        try {
            Pedido nuevoPedido = new Pedido(estado, formaPago, usuario);
            pedidos.add(nuevoPedido);
            System.out.println("¡¡¡Pedido con id: #" + nuevoPedido.getId() + " creado exitosamente!!!");
            return nuevoPedido;
        } catch (IllegalArgumentException iae) {
            System.out.println("\n ERROR: " + iae.getMessage());
            System.out.println("\nATENCION: Pedido NO fue creado");
            return null;
        }
    }

    public void agregarDetalle(long idPedido, int cantidad, Producto producto) {
        Pedido pedidoEncontrado = this.buscar(idPedido);
        if (pedidoEncontrado == null || pedidoEncontrado.isEliminado()) {
            throw new EntidadNoEncontradaException(String.format("\n ERROR: Pedido con id #%d no encontrado.", idPedido));
        }
        if (producto == null || producto.isEliminado()) {
            throw new EntidadNoEncontradaException("\nATENCION: El producto indicado no existe o fue eliminado");
        }
        try {
            pedidoEncontrado.addDetallePedido(cantidad, producto);
            producto.venderUnidades(cantidad);
            System.out.println("¡¡¡Detalle agregado exitosamente al pedido #" + idPedido + "!!!");
            System.out.println("¡¡Total actualizado: $" + String.format("%.2f!!", pedidoEncontrado.getTotal()));
        } catch (IllegalArgumentException iae) {
            System.out.println("\n ERROR: " + iae.getMessage());
        }
    }

    public void actualizarEstadoYFormaPago(long id, Estado nuevoEstado, FormaPago nuevaFormaPago) {
        Pedido pedidoParaEditar = this.buscar(id);
        if (pedidoParaEditar == null) {
            throw new EntidadNoEncontradaException(String.format("Pedido con id #%d no encontrado.", id));
        } else if (pedidoParaEditar.isEliminado()) {
            throw new EntidadNoEncontradaException(String.format("Pedido con id #%d eliminado.", id));
        } else {
            if (nuevoEstado != null) {
                pedidoParaEditar.setEstado(nuevoEstado);
            }
            if (nuevaFormaPago != null) {
                pedidoParaEditar.setFormaPago(nuevaFormaPago);
            }
            if (nuevoEstado == null && nuevaFormaPago == null) {
                System.out.println("\nATENCION: No se registraron modificaciones al Pedido con id: #" + id);
            } else {
                System.out.println("¡¡¡Pedido con id: #" + id + " actualizado exitosamente!!!");
            }
        }
    }

    public Pedido buscar(long id) {
        int i = 0;
        Pedido pedidoEncontrado = null;
        while (i < pedidos.size() && pedidos.get(i).getId() != id) {
            i++;
        }
        if (i < pedidos.size()) {
            pedidoEncontrado = this.pedidos.get(i);
        }
        return pedidoEncontrado;
    }

    public void eliminar(long id) {
        Pedido pedidoParaEliminar = this.buscar(id);
        if (pedidoParaEliminar == null || pedidoParaEliminar.isEliminado()) {
            throw new EntidadNoEncontradaException(String.format("\nATENCION: Pedido con id #%d no encontrado o ya eliminado.", id));
        } else {
            pedidoParaEliminar.setEliminado(true);
            System.out.println("¡¡¡Pedido con id: #" + id + " se elimino exitosamente!!!");
        }
    }
}
