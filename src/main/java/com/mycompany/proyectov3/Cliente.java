package com.mycompany.proyectov3;

import java.util.Random;
/**
 * Clase cliente del sistema bancario, donde se gestiona la información personal
 * cuentas, estado de cuenta y los credecnciales de acceso
 * se almacenan datos del cliente como id, nombre, telefono, correo y usuario.
 * @author migag
 */

public class Cliente {

    //Atributos de la clase:
    private String idCliente;
    private String nombreCliente;
    private String telefonoCliente;
    private String correoCliente;
    private String usuarioCliente;
    private String claveCliente;
    private int[] numerosCuentas = new int[5]; // Guarda los números de cuenta del cliente, maximo 5
    private int cantidadCuentas = 0;
    private int tAcceso[][];//tarjeta de acceso matriz 4x5
    private EstadoCliente estadoCliente;
    private static int contadorUsuario = 40;

    //Metodos constructores 
    public Cliente(String idCliente, String nombreCliente, String telefonoCliente, String correoCliente) {
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.telefonoCliente = telefonoCliente;
        this.correoCliente = correoCliente;
        this.usuarioCliente = generarUsuario(nombreCliente);
        this.estadoCliente = EstadoCliente.Activo;
        this.claveCliente = "";
        this.tAcceso = new int[4][5];
        generarT_Acceso();

    }

 
    //Métodos:
    //tarjeta de acceso 4x5 aleatoria con valores entre 10  y 99
    private void generarT_Acceso() {
        Random random = new Random();
        for (int i = 0; i < this.tAcceso.length; i++) {
            for (int j = 0; j < tAcceso[i].length; j++) {
                this.tAcceso[i][j] = random.nextInt(10, 100);
            }
        }
    }
//Muestra con consola la tarjeta de acceso para el cliente
    public void mostrarT_Acceso() {
        System.out.println("Tarjeta de acceso     A    B    C    D    E");

        // Imprimir las filas con sus índices
        for (int i = 0; i < this.tAcceso.length; i++) {
            System.out.print("        " + (i + 1) + "           "); // Número de fila
            for (int j = 0; j < tAcceso[i].length; j++) {
                System.out.print("["+this.tAcceso[i][j]+"] ");
            }
            System.out.println();
        }
    }
    //Se asocia un numero de cuenta nuevo para el cliente
    public void agregarCuenta(int numeroCuenta) {
        if (cantidadCuentas < numerosCuentas.length) {
            numerosCuentas[cantidadCuentas++] = numeroCuenta;
        }
    }
//Se obtiene la información del cliente y se muestra
    public String obtenerDatos(boolean conSaltoLinea) {
        String salto = ", ";

        if (conSaltoLinea) {
            salto = "\n";
        }

        return "ID: " + getIdCliente() + salto
                + "NOMBRE: " + getNombreCliente() + salto
                + "TELEFONO: " + getTelefonoCliente() + salto
                + "CORREO: " + getCorreoCliente() + salto
                + "USUARIO: " + getUsuarioCliente() + salto
                + "CONTRASEÑA: " + getClaveClienteSecreta() + salto
                + "ESTADO: " + getEstadoCliente();
    }
// metodo privado uqe genera un nombre con base en el primer nombre y numero de credencual
    private String generarUsuario(String nombreCliente) {
        int espacio = nombreCliente.indexOf(" ");
        String primerNombre;

        if (espacio != -1) {
            primerNombre = nombreCliente.substring(0, espacio);
        } else {
            primerNombre = nombreCliente;
        }

        String usuarioGenerado = primerNombre + contadorUsuario;
        contadorUsuario++; // aumenta para el siguiente cliente
        return usuarioGenerado;
    }
// Versión protegida de la clave del cliente se muestra con **, retorna
    //la clave cifrada o sin clave cuando no existe
    public String getClaveClienteSecreta() {
        if (!claveCliente.equals("")) {
            return "*".repeat(claveCliente.length()); // desde Java 11
        } else {
            return "(Sin Clave)";
        }

    }
    // Se obtiene el numero de cuenta del cliente
    
    public int obtenerNumCuenta(int num){
        return numerosCuentas[num];
    }

    //Metodos encapsuladores:
    public String getIdCliente() {
        return idCliente;
    }
    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }
    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }
    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }

    public String getUsuarioCliente() {
        return usuarioCliente;
    }
    public void setUsuarioCliente(String usuarioCliente) {
        this.usuarioCliente = usuarioCliente;
    }

    public String getClaveCliente() {
        return claveCliente;
    }
    public void setClaveCliente(String claveCliente) {
        this.claveCliente = claveCliente;
    }

    public EstadoCliente getEstadoCliente() {
        return estadoCliente;
    }
    public void setEstadoCliente(EstadoCliente estadoCliente) {
        this.estadoCliente = estadoCliente;
    }

    public int[] getNumerosCuentas() {
        return numerosCuentas;
    }
    public int getCantidadCuentas() {
        return cantidadCuentas;
    }

    public int[][] gettAcceso() {
        return tAcceso;
    }

    public void settAcceso(int[][] tAcceso) {
        this.tAcceso = tAcceso;
    }

}
