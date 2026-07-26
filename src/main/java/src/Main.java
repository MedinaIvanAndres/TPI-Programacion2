package src;

import src.dao.PedidoDAO;
import src.dao.PedidoDAOImpl;
import src.dao.ProductoDAO;
import src.dao.ProductoDAOImpl;
import src.dao.UsuarioDAO;
import src.dao.UsuarioDAOImpl;
import src.db.ConexionDB;
import src.entities.Pedido;
import src.entities.Producto;
import src.entities.Usuario;
import src.enums.Estado;
import src.enums.FormaPago;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        PedidoDAO pedidoDAO = new PedidoDAOImpl();
        UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
        ProductoDAO productoDAO = new ProductoDAOImpl();

        try {
            // Buscar el usuario y producto que ya teníamos guardados
            Usuario ivan = usuarioDAO.buscarPorId(1L);
            Producto notebook = productoDAO.buscarPorId(1L);

            // Crear pedido y agregarle un detalle
            Pedido pedido = new Pedido(Estado.PENDIENTE, FormaPago.EFECTIVO, ivan);
            pedido.addDetallePedido(2, notebook);
            pedidoDAO.guardar(pedido);
            System.out.println("Pedido guardado con ID: " + pedido.getId());

            // Listar todos los pedidos
            System.out.println("\n--- Todos los pedidos ---");
            List<Pedido> todos = pedidoDAO.listar();
            todos.forEach(p -> {
                System.out.println(p);
                p.listarDetalles();
            });

            // Listar por usuario
            System.out.println("\n--- Pedidos del usuario 1 ---");
            List<Pedido> porUsuario = pedidoDAO.listarPorUsuario(1L);
            System.out.println("Cantidad: " + porUsuario.size());

            // Buscar por ID
            System.out.println("\n--- Buscar pedido por ID ---");
            Pedido encontrado = pedidoDAO.buscarPorId(pedido.getId());
            System.out.println(encontrado);

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            ConexionDB.cerrarConexion();
        }
    }
}