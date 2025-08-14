package com.mycompany.proyectov3;

import javax.swing.JOptionPane;

/*
 *3° Avance Proyecto
 * ------Autores----- 
 * Miguel Aguilar Brenes 
 * Alexander Campos Marín 
 * Nelson Cardona Rocha
 */
public class ProyectoV3 {
    /**
     * El main del sistema hiperbanco
     * menu para cargar datos de
     * @param args 
     */

    public static void main(String[] args) {
        HiperBanco();
    }
//Muestra el menu inicial del sistema y dirige al usuario 
    //al menu de banco o gestion inicio cliente
    public static void HiperBanco() {
        int opcionInicio;
        do {
        String btnMenuInicio[] = {"BANCO", "CLIENTE", "SALIR"};

        opcionInicio = JOptionPane.showOptionDialog(null, "Bienvenido al sistema", "Hiper Banco",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, btnMenuInicio, "SALIR");
        switch (opcionInicio) {
            case 0:
                MenuBanco();//regresa al menu principal 
                break;
            case 1:
                Gestion.InicioCliente();
                break;
            case 2:
                JOptionPane.showMessageDialog(null, "Saliendo del sistema Hiper Banco... Gracias por Usar");
                break;
            default:
                JOptionPane.showMessageDialog(null, "Opcion invalida");
        }            
        } while (opcionInicio!=2);

    }
    //menú del banco con ocpiones del 1 al 9 
    public static void MenuBanco() {
        int menuBanco;
        do {
            menuBanco = Integer.parseInt(JOptionPane.showInputDialog("""
                                            Menú Bancario
                                    1. Generar datos  
                                    2. Mostrar clientes
                                    3. Mostrar cuentas y movimientos
                                    4. Agregar nuevo cliente
                                    5. Agregar nueva cuenta
                                    6. Buscar clientes
                                    7. Buscar cuenta
                                    8. Generar reportes
                                    9. Salir del menú bancario                            
                                    """));
            switch (menuBanco) {
                case 1:
                    Gestion.gestionDatos();
                    break;
                case 2:
                    Gestion.mostrarClientes();
                    break;
                case 3:
                    Gestion.mostrarCuentas();
                    break;
                case 4:
                    Gestion.agregarCliente();
                    break;
                case 5:
                    Gestion.agregarCuenta();
                    break;
                case 6:
                    Gestion.buscarCliente();
                    break;
                case 7:
                    Gestion.buscarCuenta();
                    break;
                case 8:
                    Gestion.generarReportesMenu();
                    break;
                case 9:
                    JOptionPane.showMessageDialog(null, "Saliendo del menú Bancario...");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "La opción " + menuBanco + " ingresada no valida, intente de nuevo.");
            }
        } while (menuBanco != 9);

    }

    
}
