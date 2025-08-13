package com.mycompany.proyectov3;

import java.time.LocalDate;

/**
 * Tipo de transacción (enum) [Depósito], [Retiro], [Transferencia], [Compra]
 * Número de transacción 
 * Número de cuenta (Número de cuenta a retiRar) 
 * Monto
 * Fecha 
 * Número de cuenta a depositar (Transferencia) Detalle
 */
public class Movimientos {

    private TipoMovimiento Tmovimiento;
    private int Ntransacción;
    private int NcuentaOrigen;
    private double monto;
    private LocalDate fecha;
    private int NcuentaRetiro;
    private String detalle;
    private static int referenciaNumTransaccion = 10000;

//Constructor
    public Movimientos(TipoMovimiento Tmovimiento, int NcuentaOrigen,double monto,String detalle ) {
        this.detalle = detalle;
        this.Tmovimiento = Tmovimiento;
        this.monto = monto;
        this.fecha = LocalDate.now();
        this.Ntransacción = GenerarNumTransferencia();
    }
// Metodos

    /*Número de transacción: El número de transacción no se solicita, se 
auto incrementa de 1 en 1, iniciando en 10000.*/
    public String obtenerDatos(boolean conSaltoLinea) {
        String salto = " | ";

        if (conSaltoLinea) {
            salto = "\n";
        }
        if (getTmovimiento().equals(TipoMovimiento.Tranferencia)) {
            return getTmovimiento()+ salto + "CuentaOrigen: " + getNcuentaOrigen()+ salto + "Cuenta Destino: " + getNcuentaRetiro()+ salto + "Monto: " + getMonto()+ salto +"Fecha: " + getFecha()+ salto +"Transacción: " + getNtransacción()+ salto +"Detalle: " + getDetalle();
        }
        return getTmovimiento()+ salto + "Cuenta: " + getNcuentaOrigen()+ salto + "Monto: " + getMonto()+ salto +"Fecha: " + getFecha()+ salto +"Transacción: " + getNtransacción()+ salto +"Detalle: " + getDetalle();
       
    }

    public int GenerarNumTransferencia() {
        return referenciaNumTransaccion++;
    }
//Setters and Getters
    

    public TipoMovimiento getTmovimiento() {
        return Tmovimiento;
    }

    public void setTmovimiento(TipoMovimiento Tmovimiento) {
        this.Tmovimiento = Tmovimiento;
    }

    public int getNtransacción() {
        return Ntransacción;
    }

    public void setNtransacción(int Ntransacción) {
        this.Ntransacción = Ntransacción;
    }

    public int getNcuentaOrigen() {
        return NcuentaOrigen;
    }

    public void setNcuentaOrigen(int NcuentaOrigen) {
        this.NcuentaOrigen = NcuentaOrigen;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    
    public int getNcuentaRetiro() {
        return NcuentaRetiro;
    }
    public void setNcuentaRetiro(int NcuentaRetiro) {
        this.NcuentaRetiro = NcuentaRetiro;
    }

    public String getDetalle() {
        return detalle;
    }
    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

}
