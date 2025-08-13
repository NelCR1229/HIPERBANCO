package com.mycompany.proyectov3;

import java.time.LocalDate;

public class Cuenta {

    // 1. Atributos
    private static int referenciaNumCuenta = 4710;
    private int numCuentaCliente;
    private Cliente cliente;
    private String idCliente;
    private TipoCuenta tipoCuenta;
    private LocalDate fechaApertura;
    private double saldoInicial;

    private Movimientos[] historial; // Historial de movimientos
    private int totalmovimientos = 0;

    // 2. Constructor
    public Cuenta(Cliente cliente, String idCliente, TipoCuenta tipoCuenta, double saldoInicial) {
        this.numCuentaCliente = referenciaNumCuenta++;
        this.cliente = cliente;
        this.idCliente = idCliente;
        this.tipoCuenta = tipoCuenta;
        this.fechaApertura = LocalDate.now();
        this.saldoInicial = saldoInicial;
        this.historial = new Movimientos[100]; // capacidad para 100 movimientos
    }


    public void agregarMovimiento(Movimientos mov) {
        if (totalmovimientos < historial.length) {
            historial[totalmovimientos] = mov;
            totalmovimientos++;
        }
    }
    
    public String mostrarHistorial() {
        if (totalmovimientos == 0) {
            return "No hay movimientos registrados.";
        }
        
        String resultado = "";
        for (int i = 0; i < totalmovimientos; i++) {
            resultado += "  "+(i + 1) + "- "+historial[i].obtenerDatos(false) + "\n";
        }
        return resultado;
    }
    
    
    public String obtenerDatos(boolean conSaltoLinea) {
        String salto = ", ";
        
        if (conSaltoLinea) {
            salto = "\n";
        }
        
        return "NUMERO DE CUENTA:: " + getNumCuentaCliente() + salto
                + "ID: " + getIdCliente() + salto
                + "TIPO: " + getTipoCuenta() + salto
                + "APERTURA: " + getFechaApertura() + salto
                + "SALDO: " + getSaldoInicial() + salto
                + "PROPIETARIO: " + cliente.getNombreCliente();
    }

    //Metodos encapsuladores
    public int getNumCuentaCliente() {
        return numCuentaCliente;
    }
    
    public void setNumCuentaCliente(int numCuentaCliente) {
        this.numCuentaCliente = numCuentaCliente;
    }
    
    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }
    
    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }
    
    public String getIdCliente() {
        return idCliente;
    }
    
    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }
    
    public LocalDate getFechaApertura() {
        return fechaApertura;
    }
    
    public void setFechaApertura(LocalDate fechaApertura) {
        this.fechaApertura = fechaApertura;
    }
    
    public double getSaldoInicial() {
        return saldoInicial;
    }
    
    public void setSaldoInicial(double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }
    
    
    public int getTotalmovimientos() {
        return totalmovimientos;
    }
    
    public void setTotalmovimientos(int totalmovimientos) {
        this.totalmovimientos = totalmovimientos;
    }
    
}
