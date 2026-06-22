# Food Store - Sistema de Gestión de Pedidos

**Trabajo Práctico Integrador - Programación 2**  
Universidad Tecnológica Nacional (UTN)

## 📋 Descripción

Food Store es un sistema de gestión de pedidos de comida desarrollado en Java. Permite administrar categorías de productos, productos, usuarios y pedidos mediante una interfaz de consola interactiva.

## 🔧 Requisitos

- **Java 21** o superior
- **NetBeans IDE** (recomendado) o cualquier IDE compatible con Java
- **Sistema Operativo:** Windows, macOS o Linux

## 📁 Estructura del Proyecto

```
src/
├── entities/        # Clases de dominio
├── enums/           # Enumeraciones (Rol, Estado, FormaPago)
├── exception/       # Excepciones personalizadas
├── interfaces/      # Contratos (Calculable)
├── menus/           # Interfaz de usuario (menús)
├── transacciones/   # Lógica de negocio
├── validaciones/    # Validación de entrada
└── Main.java        # Punto de entrada
```

## 🚀 Cómo Ejecutar

### Opción 1: Con NetBeans IDE
1. Abre NetBeans
2. File → Open Project
3. Selecciona la carpeta del proyecto
4. Click derecho en el proyecto → Run


## ✨ Funcionalidades Principales

- **Gestión de Categorías:** Crear, editar, eliminar y listar categorías de productos
- **Gestión de Productos:** ABM completo de productos con stock y precios
- **Gestión de Usuarios:** Administración de usuarios con roles (ADMIN, USUARIO)
- **Gestión de Pedidos:** Crear pedidos, agregar detalles, actualizar estado y forma de pago
- **Validaciones robustas:** Entradas protegidas y mensajes de error descriptivos
- **Soft Delete:** Las entidades se marcan como eliminadas sin borrar registros
- **Relaciones bidireccionales:** Mantiene consistencia entre entidades relacionadas

## 📚 Documentación

Para más detalles sobre la arquitectura, decisiones técnicas y soluciones implementadas, ver lo siguientes documentos:

- **[Informe Completo PDF](https://drive.google.com/drive/folders/1mv-kmy3Df8b-lbcxPiZUdeppt0m_kJz7)** - Documentación detallada del proyecto
- **[Video Demostración](https://drive.google.com/drive/folders/1h8uIhrO8VL6V85OL4UebMB616APp_zoI)** - Demostración en vivo del sistema

## 👤 Autor / Integrantes

**Medina, Ivan Andres**  
Carrera: Tecnicatura Universitaria en Programación - UTN  
Materia: Programacion 2
Junio 2026

## 📝 Notas

Este proyecto implementa todas las funcionalidades obligatorias de la consigna más mejoras opcionales para una mejor experiencia de usuario. Se aplican principios de POO como encapsulamiento, herencia, polimorfismo y abstracción.
