package com.mycompany.proyectov3;

import static com.mycompany.proyectov3.ProyectoV3.HiperBanco;
import java.time.LocalDate;
import java.util.Random;
import javax.swing.JOptionPane;

public class Gestion {

    static Cuenta cuentas[] = new Cuenta[150];
    static Cliente clientes[] = new Cliente[30];
    static int totalClientes = 0;
    static int totalCuentas = 0;
    static String filtroIdCliente = "Todos";
    static String filtroEstado = "Todos"; 
    static String filtroTipoCuenta = "Todos"; 
    static String filtroSaldoTipo = "Todos"; 
    static double filtroSaldoValor = 0.0;

//------Metodos del Menu Bancario ------------------------------------------------------------------------
    public static void gestionDatos() {
    if (totalClientes > 0) {
        JOptionPane.showMessageDialog(null, "Datos ingresados al sistema");
        return;
    }

    String[] nombres = {"Fernando", "Carlos", "Federico", "Karla", "Jimena", "Arianna", "Lucia", "Daniela", "Brenda", "Dayanna"};
    String[] apellidos = {"Aguilar", "Avila", "Rodriguez", "Cascante", "Sequiera", "Hernandez", "Araya", "Chinchilla", "Vargas", "Guerrero"};

    for (int i = 0; i < 10; i++) {
        String identificaciones = "1-100" + i + "-000" + i;
        String nombre = nombres[i] + " " + apellidos[i];
        String telefono = "7000-000" + i;
        String correo = "cliente" + i + "@gmail.com";
        Cliente nuevo = new Cliente(identificaciones, nombre, telefono, correo);
        clientes[i] = nuevo;
        totalClientes++;
    }

    for (int i = 0; i < 12; i++) {
        Cliente generado = clientes[i % 9]; // distribuye entre primeros 9 clientes
        Cuenta nuevaCuenta = new Cuenta(generado, generado.getIdCliente(), TipoCuenta.Corriente, (i + 1) * 100);
        cuentas[i] = nuevaCuenta;
        totalCuentas++;
    }

    Random random = new Random();
    for (int i = 0; i < totalCuentas; i++) {
        Cuenta cuenta = cuentas[i];
        int cantidadMovs = random.nextInt(0, 6); // 0 a 5 movimientos

        for (int j = 0; j < cantidadMovs; j++) {
            int tipoNum = random.nextInt(0, 2); // 0 o 1
            String detalle;
            TipoMovimiento tipoMovimiento;
            if (tipoNum == 0) {
                tipoMovimiento = TipoMovimiento.Deposito;
                detalle = "Deposito";
            } else {
                tipoMovimiento = TipoMovimiento.Retiro;
                detalle = "Retiro";
            }
            int NcuentaOrigen = cuentas[i].getNumCuentaCliente();
            double monto = 1000 + random.nextInt(90001); // 1000 a 91000
            String detalle1 = detalle + " $" + monto;
            Movimientos movimiento = new Movimientos(tipoMovimiento, NcuentaOrigen, monto, detalle1);
            cuenta.agregarMovimiento(movimiento);
            if (detalle.equals("Deposito")) {
                cuenta.setSaldoInicial(cuenta.getSaldoInicial() + monto);
            } else{
                cuenta.setSaldoInicial(cuenta.getSaldoInicial() - monto);
            }
        }
    }

    JOptionPane.showMessageDialog(null, "Datos generados con éxito");
}

    public static void agregarCliente() {
        String idCliente = validoID();
        String nombreCliente = JOptionPane.showInputDialog("Ingresar nombre completo del cliente:");
        String correoCliente;
        do {
            correoCliente = validoCorreo();
            if (correoCliente != null) {
                break;
            }
        } while (true);
        String telefonoCliente = JOptionPane.showInputDialog("Ingresar número de teléfono del cliente en formato 0000-0000:");
        Cliente nuevo = new Cliente(idCliente, nombreCliente, telefonoCliente, correoCliente);
        clientes[totalClientes++] = nuevo;
        JOptionPane.showMessageDialog(null, "Cliente agregado con exito");
    }

    public static void agregarCuenta() {
        boolean condicionDO = true;
        do {
            String idCliente = JOptionPane.showInputDialog("Ingrese su ID en formato 0-0000-0000: ");
            boolean clienteEncontrado = false;
            for (int i = 0; i < totalClientes; i++) {
                if (idCliente.equals(clientes[i].getIdCliente())) {
                    clienteEncontrado = true;
                    if (clienteEncontrado) {
                        JOptionPane.showMessageDialog(null, "El usuario tiene " + clientes[i].getCantidadCuentas() + " cuentas puede crear otra");
                        if (clientes[i].getCantidadCuentas() >= 5) {
                            JOptionPane.showMessageDialog(null, "El cliente ya tiene 5 cuentas.");
                            return; // o mostrar menú con botones como lo exige el proyecto
                        }
                        TipoCuenta tipo;
                        String tipoCuentaBTN[] = {"Corriente", "Ahorros", "Inversión", "Planilla"};
                        int valorBTN = JOptionPane.showOptionDialog(null, "Indique el tipo de cuenta", "Tipo de cuenta", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                                null, tipoCuentaBTN, "Corriente");
                        switch (valorBTN) {
                            case 0:
                                tipo = TipoCuenta.Corriente;
                                condicionDO = false;
                                break;
                            case 1:
                                tipo = TipoCuenta.Ahorros;
                                condicionDO = false;
                                break;
                            case 2:
                                tipo = TipoCuenta.Inversion;
                                condicionDO = false;
                                break;
                            case 3:
                                tipo = TipoCuenta.Planilla;
                                condicionDO = false;
                                break;
                            default:
                                tipo = TipoCuenta.Ahorros;
                        }
                        double saldoInicial = 0;
                        do {
                            double saldoIngresado = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el monto inicial de la cuenta nueva:"));
                            if (saldoIngresado >= 0) {
                                saldoInicial = saldoIngresado;
                                break;
                            } else {
                                String opcionesBTNSaldo[] = {"Ingresar otro Saldo", "Cancelar"};
                                int valorBTN2 = JOptionPane.showOptionDialog(null, "El saldo debe ser mayor o igual a cero", " ", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, opcionesBTNSaldo, "Ingresar otro Saldo");
                                if (valorBTN2 == 1) {
                                    return;
                                }
                            }
                        } while (true);
                        
                        Cuenta nueva = new Cuenta(clientes[i], clientes[i].getIdCliente(), tipo, saldoInicial);
                        cuentas[totalCuentas++] = nueva;
                        clientes[i].agregarCuenta(nueva.getNumCuentaCliente());
                        JOptionPane.showMessageDialog(null, "Cuenta agregada con exito");
                        condicionDO = false;
                    }
                }
            }
            if (!clienteEncontrado) {
                String opcionesBTN[] = {"Agregar otro ID", "Cancelar"};
                int valorBTN = JOptionPane.showOptionDialog(null, "Cliente no entrado", " ", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, opcionesBTN, "Agregar otro ID");
                switch (valorBTN) {
                    case 0:
                        agregarCuenta();
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null, "Volviendo al menu");
                        condicionDO = false;
                    default:

                }
            }

        } while (condicionDO);
    }

    public static void mostrarClientes() {
        if (totalClientes == 0) {
            JOptionPane.showMessageDialog(null, "No hay clientes registrados en el sistema.");
            return;
        }

        System.out.println("\nLISTA DE CLIENTES:");
        for (int i = 0; i < clientes.length; i++) {

            if (clientes[i] == null) {
                System.out.println((i + 1) + "- VACIO");
            } else {
                System.out.println((i + 1) + "- " + clientes[i].obtenerDatos(false));

            }

        }
    }

    public static void mostrarCuentas() {
        if (totalCuentas == 0) {
            JOptionPane.showMessageDialog(null, "No hay cuentas registrados en el sistema.");
            return;
        }

        System.out.println("\nLISTA DE CUENTAS:");
        for (int i = 0; i < cuentas.length; i++) {

            if (cuentas[i] == null) {
                System.out.println((i + 1) + "- VACIO");
            } else {
                System.out.println((i + 1) + "- " + cuentas[i].obtenerDatos(false));
                if (cuentas[i].getTotalmovimientos() > 0) {
                    System.out.println(cuentas[i].mostrarHistorial());

                } else {
                    System.out.println("Sin movimientos");
                }
            }

        }
    }

    public static void buscarCliente() {
        if (totalClientes == 0) {
            JOptionPane.showMessageDialog(null, "No hay clientes registrados en el sistema.");
            return;
        }
        String idCliente = JOptionPane.showInputDialog("Ingrese su ID en formato 0-0000-0000: ");
        boolean condicion = true;
        boolean clienteEncontrado = false;
        do {
            for (int i = 0; i < totalClientes; i++) {
                if (idCliente.equals(clientes[i].getIdCliente())) {
                    clienteEncontrado = true;
                    JOptionPane.showMessageDialog(null, clientes[i].obtenerDatos(true));
                    String op = "Activar";
                    if (clientes[i].getEstadoCliente() == EstadoCliente.Activo) {
                        op = "Desactivar";
                    }
                    String opciones[] = {"Actualizar", op, "Cuentas", "Cancelar"};
                    int BTN = JOptionPane.showOptionDialog(
                            null,
                            "Seleccione la opcion a realizar:",
                            "Buscar Cliente",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            opciones,
                            opciones[3]);
                    switch (BTN) {
                        case 0:
                            String opActualizar[] = {"Nombre Completo", "Teléfono", "Correo"};
                            int actBTN = JOptionPane.showOptionDialog(
                                    null,
                                    "Seleccione el Atributo a actualizar:",
                                    "Actualizar",
                                    JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    opActualizar,
                                    opActualizar[1]);
                            switch (actBTN) {
                                case 0:
                                    String nombreCliente = JOptionPane.showInputDialog("Ingresar nombre completo del cliente:");
                                    clientes[i].setNombreCliente(nombreCliente);
                                    break;
                                case 1:
                                    String telefonoCliente = JOptionPane.showInputDialog("Ingresar número de teléfono del cliente en formato 0000-0000:");
                                    clientes[i].setTelefonoCliente(telefonoCliente);
                                    break;
                                case 2:
                                    String correoCliente = validoCorreo();
                                    clientes[i].setCorreoCliente(correoCliente);
                                    break;
                                default:
                            }
                            break;
                        case 1:
                            if (op.equals("Activar")) {
                                clientes[i].setEstadoCliente(EstadoCliente.Activo);
                            } else {
                                clientes[i].setEstadoCliente(EstadoCliente.Inactivo);

                            }
                            break;
                        case 2:
                            JOptionPane.showMessageDialog(null, "Aun en progreso");

                            break;
                        case 3:
                            JOptionPane.showMessageDialog(null, "Saliendo...");
                            condicion = false;
                            break;
                        default:

                    }
                }
            }
            if (!clienteEncontrado) {
                String opcionesBTN[] = {"Agregar otro ID", "Cancelar"};
                int valorBTN = JOptionPane.showOptionDialog(null, "Cliente no entrado", " ", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, opcionesBTN, "Agregar otro ID");
                switch (valorBTN) {
                    case 0:
                        buscarCliente();
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null, "Volviendo al menu");
                        condicion = false;
                    default:

                }
            }
        } while (condicion);
    }

    public static void buscarCuenta() {
        if (totalCuentas == 0) {
            JOptionPane.showMessageDialog(null, "No hay cuentas registrados en el sistema.");
            return;
        }
        int nCuenta = Integer.parseInt(JOptionPane.showInputDialog("Ingrese le numero de cuenta a buscar"));
        boolean condicion = true;
        boolean cuentaEncontrada = false;
        do {
            for (int i = 0; i < totalCuentas; i++) {
                if (nCuenta == cuentas[i].getNumCuentaCliente()) {
                    cuentaEncontrada = true;
                    String opcionesBTN[] = {"Movimientos", "Cancelar"};
                    int valorBTN = JOptionPane.showOptionDialog(null, "Seleccione la opcion a realizar", " ", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcionesBTN, "Agregar otro numero de cuenta");
                    switch (valorBTN) {
                        case 0:
                            if (cuentas[i].getTotalmovimientos() > 0) {
                                System.out.println(cuentas[i].mostrarHistorial());
                                JOptionPane.showMessageDialog(null, "Movimientos en consola");
                            } else{
                                JOptionPane.showMessageDialog(null, "La Cuenta no tiene movimientos");
                            }
                            
                            break;
                        case 1:
                            JOptionPane.showMessageDialog(null, "Volviendo al menu");
                            condicion = false;
                            break;
                        default:
                    }
                }

            }
            if (!cuentaEncontrada) {
                String opcionesBTN[] = {"Agregar otro numero de cuenta", "Cancelar"};
                int valorBTN = JOptionPane.showOptionDialog(null, "La cuenta con el número: " + nCuenta + " no se encuentra registrado en el sistema", " ", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, opcionesBTN, "Agregar otro numero de cuenta");
                switch (valorBTN) {
                    case 0:
                        buscarCuenta();
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null, "Volviendo al menu");
                        condicion = false;
                    default:
                }
            }
        } while (condicion);

    }
    
    public static void generarReportesMenu() {
        boolean salir = false;
        while (!salir) {

            // Preparar texto del saldo
            String textoSaldo;
            if (filtroSaldoTipo.equals("Todos")) {
                textoSaldo = "Todos";
            } else {
                textoSaldo = filtroSaldoTipo + " " + filtroSaldoValor;
            }

            String opciones[] = {"Filtros del Cliente", "Filtros de la Cuenta", "Reporte", "Salir"};
            int opcion = JOptionPane.showOptionDialog(
                    null,
                    "Filtros del Cliente:\nID: " + filtroIdCliente + "\nEstado: " + filtroEstado +
                    "\n\nFiltros de la Cuenta:\nTipo: " + filtroTipoCuenta + "\nSaldo: " + textoSaldo,
                    "Generar Reportes",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            switch (opcion) {
                case 0:
                    filtroIdCliente = solicitarIdCliente();
                    filtroEstado = solicitarEstado();
                    break;
                case 1:
                    filtroTipoCuenta = solicitarTipoCuenta();
                    filtroSaldoValor = solicitarSaldo();
                    filtroSaldoTipo = solicitarTipoSaldo();
                    break;
                case 2:
                    generarReporte();
                    break;
                default:
                    salir = true;
                    break;
            }
        }
    }
    
    private static void generarReporte() {
        System.out.println("===== REPORTE DE CUENTAS =====");

        boolean hayResultados = true;

        for (int i = 0; i < totalCuentas; i++) {
            Cliente cliente = buscarClientePorId(filtroIdCliente);
            for (int j = 0; j < cliente.getCantidadCuentas(); j++) {
                int numCuenta = cliente.obtenerNumCuenta(j);
                Cuenta cuenta = buscarCuentaPorNcuenta(numCuenta);
                boolean pasaFiltros = pasaFiltros(cliente, cuenta);
                if (pasaFiltros) {
                    System.out.println("[Id cliente]: " + cliente.getIdCliente()
                            + " [Estado]: " + cliente.getEstadoCliente()
                            + " [Número de cuenta]: " + cuenta.getNumCuentaCliente()
                            + " [Tipo de cuenta]: " + cuenta.getTipoCuenta()
                            + " [Saldo]: $" + cuenta.getSaldoInicial());
                    hayResultados = false;
                }
            }
        }
        if (hayResultados) {
            System.out.println("No se encontraron resultados con los filtros aplicados.");
        }
        System.out.println("==============================\n");
    }

//------Metodos del Menu Cliente ------------------------------------------------------------------------
    public static void InicioCliente() {
        if (totalClientes == 0) {
            JOptionPane.showMessageDialog(null, "No hay clientes registrados en el sistema.");
            return;
        }
        String usuarioIngresado = JOptionPane.showInputDialog("Ingrese su usuario: ");
        Cliente cliente = buscarClientePorUsuario(usuarioIngresado);
        String claveIngresada;
        if (cliente != null) {
            if (cliente.getClaveCliente().equals("")) {
                validarClaveCliente(cliente);
            } else {
                do {
                    claveIngresada = JOptionPane.showInputDialog("Ingrese su clave: ");
                    if ((claveIngresada.equals(cliente.getClaveCliente())) != true) {
                        JOptionPane.showMessageDialog(null, "La clave ingresada es incorrecta, intente de nuevo");
                    } else {
                        boolean condicion = true;
                        do {
                            boolean tarjetaCorrecta = validarTarjetaAcceso(cliente);
                            if (tarjetaCorrecta) {
                                MenuClientes(cliente);
                                condicion = false;
                            } else {
                                JOptionPane.showMessageDialog(null, "Acceso incorrecto");
                            }
                        } while (condicion);
                        break;
                    }
                } while (true);
            }
        } else {
            String opciones[] = {"Intentar de nuevo", "Salir"};
            int opcion = JOptionPane.showOptionDialog(null, "No hay ningún cliente con el usuario: " + usuarioIngresado, " ", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, opciones, opciones[0]);
            if (opcion != 0) {
                JOptionPane.showMessageDialog(null, "Saliendo de apartado cliente...");
                return;
            } else {
                InicioCliente();
            }
        }
    }

    public static void MenuClientes(Cliente cliente) {
        int opcionMenu;
        String bienvenidaCliente = "Bienvenido " + cliente.getNombreCliente();
        String btnMenuCliente[] = {"REALIZAR TRANSACCIONES", "MIS CUENTAS", "ACTUALIZAR", "SALIR"};
        opcionMenu = JOptionPane.showOptionDialog(null, "Bienvenido al sistema" + bienvenidaCliente + " seleccione la opcion a realizar", "Hiper Banco",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, btnMenuCliente, "SALIR");
        switch (opcionMenu) {
            case 0:
                RealizarTransacciones(cliente);
                break;
            case 1:
                misCuentas(cliente);
                break;
            case 2:
                Actualizar(cliente);
                break;
            case 3:
                JOptionPane.showMessageDialog(null, "Saliendo..");
                HiperBanco();
                break;
            default:
                JOptionPane.showMessageDialog(null, "Opcion invalida");
                HiperBanco();
        }
    }

    public static void RealizarTransacciones(Cliente cliente) {
        int opcionMenuTransaccion;
        String btnMenuTransacciones[] = {"DEPOSITO", "RETIRO", "TRANSFERENCIA", "COMPRA", "CANCELAR"};
        opcionMenuTransaccion = JOptionPane.showOptionDialog(null, "Bienvenido al sistema", "Hiper Banco",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, btnMenuTransacciones, "SALIR");
        switch (opcionMenuTransaccion) {
            case 0:
                RealizarDeposito(cliente);
                break;
            case 1:
                RealizarRetiro(cliente);
                break;
            case 2:
                RealizarTranferencias(cliente);
                break;
            case 3:
                RealizarCompra(cliente);
                break;
            case 4:
                JOptionPane.showMessageDialog(null, "Saliendo..");
                MenuClientes(cliente);
                break;
        }
    }

    public static void RealizarDeposito(Cliente cliente) {
        int cuentaDeposito = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el numero de la cuenta a depositar"));
        Cuenta cuenta = buscarCuentaPorNcuenta(cuentaDeposito);
        if (cuenta != null) {
            double montoDeposito = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el monto a depositar"));
            String detalle = JOptionPane.showInputDialog("Digite el detalle del deposito");
            //Cuenta cuentaDeposito, monto deposito
            Movimientos deposito = new Movimientos(TipoMovimiento.Deposito, cuentaDeposito, montoDeposito, detalle);
            deposito.setNcuentaOrigen(cuenta.getNumCuentaCliente());
            cuenta.setSaldoInicial(cuenta.getSaldoInicial() + montoDeposito);
            cuenta.agregarMovimiento(deposito);
            JOptionPane.showMessageDialog(null, "Deposito exitoso");
            RealizarTransacciones(cliente);

        } else {
            String opciones[] = {"Intentar de nuevo", "Salir"};
            int opcion = JOptionPane.showOptionDialog(null, "No hay ningúna cuenta con el numero: " + cuentaDeposito + " o no esta asociada a esta cuenta", " ", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, opciones, opciones[0]);
            if (opcion != 0) {
                JOptionPane.showMessageDialog(null, "Saliendo de apartado cliente...");
                return;
            } else {
                RealizarDeposito(cliente);
            }
        }
    }

    public static void RealizarRetiro(Cliente cliente) {
        int cuentaRetiro = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el numero de la cuenta a retirar"));
        Cuenta cuenta = buscarCuentaPorNcuentaOrigen(cuentaRetiro, cliente);
        if (cuenta != null) {
            boolean condion = true;
            do {
                double montoRetiro = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el monto a retirar"));
                if (montoRetiro <= cuenta.getSaldoInicial()) {
                    String detalle = JOptionPane.showInputDialog("Digite el detalle del retiro");
                    Movimientos deposito = new Movimientos(TipoMovimiento.Retiro, cuentaRetiro, montoRetiro, detalle);
                    deposito.setNcuentaOrigen(cuenta.getNumCuentaCliente());
                    cuenta.setSaldoInicial(cuenta.getSaldoInicial() - montoRetiro);
                    cuenta.agregarMovimiento(deposito);
                    JOptionPane.showMessageDialog(null, "Retiro exitoso");
                    condion = false;
                    RealizarTransacciones(cliente);
                } else {
                    String opciones[] = {"Intentar con otro monto", "Salir"};
                    int opcion = JOptionPane.showOptionDialog(null, "El monto " + montoRetiro + " es menor al que esta en la cuenta", " ", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, opciones, opciones[0]);
                    if (opcion != 0) {
                        JOptionPane.showMessageDialog(null, "Saliendo de apartado cliente...");
                        return;
                    } else {
                        RealizarTransacciones(cliente);
                    }
                }
            } while (condion);
        } else {
            String opciones[] = {"Intentar de nuevo", "Salir"};
            int opcion = JOptionPane.showOptionDialog(null, "No hay ningúna cuenta con el numero: " + cuentaRetiro + " o no esta asociada a esta cuenta", " ", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, opciones, opciones[0]);
            if (opcion != 0) {
                JOptionPane.showMessageDialog(null, "Saliendo de apartado cliente...");
                return;
            } else {
                RealizarRetiro(cliente);
            }
        }
    }

    public static void RealizarTranferencias(Cliente cliente) {
        int cuentaO = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el numero de la cuenta Origen"));
        Cuenta cuentaOrigen = buscarCuentaPorNcuentaOrigen(cuentaO, cliente);;
        if (cuentaOrigen != null) {
            int cuentaD = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el numero de la cuenta Destino"));
            Cuenta cuentaDestino = buscarCuentaPorNcuenta(cuentaD);
            if (cuentaDestino != null) {
                boolean condion = true;
                do {
                    double montoTranferencia = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el monto a tranferir"));
                    if (montoTranferencia <= cuentaOrigen.getSaldoInicial()) {
                        String detalle = JOptionPane.showInputDialog("Digite el detalle de la transferencia");
                        Movimientos deposito = new Movimientos(TipoMovimiento.Tranferencia, cuentaOrigen.getNumCuentaCliente(), montoTranferencia, detalle);
                        deposito.setNcuentaOrigen(cuentaOrigen.getNumCuentaCliente());
                        deposito.setNcuentaRetiro(cuentaDestino.getNumCuentaCliente());
                        cuentaOrigen.setSaldoInicial(cuentaOrigen.getSaldoInicial() - montoTranferencia);
                        cuentaOrigen.agregarMovimiento(deposito);
                        cuentaDestino.setSaldoInicial(cuentaDestino.getSaldoInicial() + montoTranferencia);
                        cuentaDestino.agregarMovimiento(deposito);
                        JOptionPane.showMessageDialog(null, "Tranferencia exitosa");
                        condion = false;
                        RealizarTransacciones(cliente);
                    } else {
                        String opciones[] = {"Intentar con otro monto", "Salir"};
                        int opcion = JOptionPane.showOptionDialog(null, "!SALDO INSUFICIENTE\nEl monto " + montoTranferencia + " es menor al que esta en la cuenta", " ", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, opciones, opciones[0]);
                        if (opcion != 0) {
                            JOptionPane.showMessageDialog(null, "Saliendo de apartado cliente...");
                            return;
                        } else {
                            RealizarTransacciones(cliente);
                        }
                    }
                } while (condion);
            } else {
                String opciones[] = {"Intentar de nuevo", "Salir"};
                int opcion = JOptionPane.showOptionDialog(null, "No hay ningúna cuenta con el numero: " + cuentaD + " o no esta asociada a esta cuenta", " ", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, opciones, opciones[0]);
                if (opcion != 0) {
                    JOptionPane.showMessageDialog(null, "Saliendo de apartado cliente...");
                    return;
                } else {
                    RealizarTranferencias(cliente);
                }
            }

        } else {
            String opciones[] = {"Intentar de nuevo", "Salir"};
            int opcion = JOptionPane.showOptionDialog(null, "No hay ningúna cuenta con el numero: " + cuentaO + " o no esta asociada a esta cuenta", " ", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, opciones, opciones[0]);
            if (opcion != 0) {
                JOptionPane.showMessageDialog(null, "Saliendo de apartado cliente...");
                return;
            } else {
                RealizarTranferencias(cliente);
            }
        }
    }

    public static void RealizarCompra(Cliente cliente) {
        int cuentaCompra = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el numero de la cuenta a pagar."));
        Cuenta cuenta = buscarCuentaPorNcuentaOrigen(cuentaCompra, cliente);
        if (cuenta != null) {
            boolean condion = true;
            do {
                double montoCompra = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el monto de la compra."));
                if (montoCompra < cuenta.getSaldoInicial()) {
                    String detalle = JOptionPane.showInputDialog("Digite el detalle de la compra");
                    Movimientos deposito = new Movimientos(TipoMovimiento.Compra, cuentaCompra, montoCompra, detalle);
                    deposito.setNcuentaOrigen(cuenta.getNumCuentaCliente());
                    cuenta.setSaldoInicial(cuenta.getSaldoInicial() - montoCompra);
                    cuenta.agregarMovimiento(deposito);
                    JOptionPane.showMessageDialog(null, "Compra exitosa");
                    condion = false;
                    RealizarTransacciones(cliente);
                } else {
                    String opciones[] = {"Intentar con otro monto", "Salir"};
                    int opcion = JOptionPane.showOptionDialog(null, "El monto " + montoCompra + " es menor al que esta en la cuenta", " ", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, opciones, opciones[0]);
                    if (opcion != 0) {
                        JOptionPane.showMessageDialog(null, "Saliendo de apartado cliente...");
                        return;
                    } else {
                        RealizarTransacciones(cliente);
                    }
                }
            } while (condion);
        } else {
            String opciones[] = {"Intentar de nuevo", "Salir"};
            int opcion = JOptionPane.showOptionDialog(null, "No hay ningúna cuenta con el numero: " + cuentaCompra, " ", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, opciones, opciones[0]);
            if (opcion != 0) {
                JOptionPane.showMessageDialog(null, "Saliendo de apartado cliente...");
                return;
            } else {
                RealizarCompra(cliente);
            }
        }
    }

    public static void misCuentas(Cliente cliente) {
        if (totalCuentas == 0) {
            JOptionPane.showMessageDialog(null, "No hay cuentas registrados en el sistema.");
            return;
        }
        if (cliente.getCantidadCuentas() == 0) {
            JOptionPane.showMessageDialog(null, "El cliente no tiene cuentas registrados en el sistema.");
            MenuClientes(cliente);
            return;

        }
        System.out.println("\nLISTA DE  MIS CUENTAS:");
        for (int i = 0; i < totalCuentas; i++) {
            if (cuentas[i].getIdCliente().equals(cliente.getIdCliente())) {
                System.out.println(cuentas[i].obtenerDatos(false));
                if (cuentas[i].getTotalmovimientos() > 0) {
                    System.out.println(cuentas[i].mostrarHistorial());
                } else {
                    System.out.println("Sin movimientos");
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Cuentas mostradas");
    }

    public static void Actualizar(Cliente cliente) {
        String idCliente = cliente.getIdCliente();
        for (int i = 0; i < totalClientes; i++) {
            if (clientes[i].getIdCliente().equals(idCliente)) {
                String opActualizar[] = {"Nombre Completo", "Teléfono", "Correo", "Clave", "Cancelar"};
                int actBTN = JOptionPane.showOptionDialog(
                        null,
                        "Seleccione el Atributo a actualizar:",
                        "Actualizar",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        opActualizar,
                        opActualizar[1]);
                switch (actBTN) {
                    case 0:
                        String nombreCliente = JOptionPane.showInputDialog("Ingresar nombre completo del cliente:");
                        clientes[i].setNombreCliente(nombreCliente);
                        break;
                    case 1:
                        String telefonoCliente = JOptionPane.showInputDialog("Ingresar número de teléfono del cliente en formato 0000-0000:");
                        clientes[i].setTelefonoCliente(telefonoCliente);
                        break;
                    case 2:
                        String correoCliente = validoCorreo();
                        clientes[i].setCorreoCliente(correoCliente);
                        break;
                    case 3:
                        validarClaveCliente(clientes[i]);
                        break;
                    case 4:
                        MenuClientes(cliente);
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Opcion incorrecta");
                        Actualizar(cliente);
                }
            }
        }
    }
//------ Metodos de ayuda xd------------------------------------------------------------------------

    public static void validarClaveCliente(Cliente cliente) {
        do {
            boolean cantidadCaracteres = false;
            boolean contieneNumero = false;
            boolean contieneLetra = false;
            String claveIngresada = JOptionPane.showInputDialog("""
                                                     Establezca su clave de ingreso
                                    La clave debe cumplir con las siguientes condiciones
                             1. Debe tener mínimo 6 caracteres 
                             2. Debe tener máximo 10 caracteres
                             3. Debe contener al menos un número
                             4. Debe contener al menos una letra                      
                                                     """);
            if (claveIngresada.length() >= 6 && claveIngresada.length() <= 10) {
                cantidadCaracteres = true;
            }
            for (int y = 0; y < claveIngresada.length(); y++) {
                char caracter = claveIngresada.charAt(y);
                if (caracter >= '0' && caracter <= '9') {
                    contieneNumero = true;
                }
            }
            for (int x = 0; x < claveIngresada.length(); x++) {
                char caracter2 = claveIngresada.charAt(x);
                if ((caracter2 >= 'a' && caracter2 <= 'z') || (caracter2 >= 'A' && caracter2 <= 'Z')) {
                    contieneLetra = true;
                }
            }
            if (cantidadCaracteres && contieneNumero && contieneLetra) {
                do {
                    String confirmacionClave = JOptionPane.showInputDialog("Confirmar clave ingresada:");
                    if (confirmacionClave.equals(claveIngresada)) {
                        cliente.setClaveCliente(claveIngresada);
                        InicioCliente();
                        break;
                    } else {
                        String opciones[] = {"Confirmar de nuevo la clave", "Cancelar"};
                        int opcion = JOptionPane.showOptionDialog(null, "Las claves no coinciden", "", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, opciones, opciones[0]);
                        if (opcion != 0) {
                            return;
                        }
                    }
                } while (true);
                break;
            } else {
                String opciones[] = {"Agregar otra clave", "Cancelar"};
                int opcion = JOptionPane.showOptionDialog(null, "La clave ingresada no cumple las condiciones mínimas de seguridad: \n1.Tener mínimo 6 caracteres\n2.Tener máximo 10 caracteres\n3.Contener al menos un número\n4.Contener al menos una letra",
                        "CLAVE NO CUMPLE CONDICIONES", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, opciones, opciones[0]);
                if (opcion != 0) {
                    break;
                }
            }
        } while (true);
    }

    public static boolean validarTarjetaAcceso(Cliente cliente) {
        Random random = new Random();
        String columnas[] = {"A", "B", "C", "D", "E"};
        // Encabezado
        cliente.mostrarT_Acceso();
        String accesos[] = new String[3];
        int valorAccesos[] = new int[3];
        for (int i = 0; i < accesos.length; i++) {
            int indexColum = random.nextInt(0, 5);
            int indexFila = random.nextInt(0, 4);
            accesos[i] = columnas[indexColum] + (indexFila + 1) + " ";
            valorAccesos[i] = cliente.gettAcceso()[indexFila][indexColum];
        }
        String acceso1 = accesos[0];
        String acceso2 = accesos[1];
        String acceso3 = accesos[2];

        String accesoCorrecto = valorAccesos[0] + "-" + valorAccesos[1] + "-" + valorAccesos[2];
        String accesoIngresado = JOptionPane.showInputDialog("Consulte su tarjeta de acceso y digite los accesos: " + acceso1 + acceso2 + acceso3 + " (Formato de ingreso XX-XX-XX)");
        if (accesoCorrecto.equals(accesoIngresado)) {
            return true;
        } else {
            return false;
        }
    }

    public static void cuentasClientes() {
        String id = JOptionPane.showInputDialog("Ingrese el ID del usuario");
        Cliente clien = buscarClientePorId(id);
        for (int i = 0; i < totalCuentas; i++) {
            if (cuentas[i].getIdCliente().equals(clien.getIdCliente())) {
                System.out.println(cuentas[i].obtenerDatos(false));
            }
        }
    }

    public static Cliente buscarClientePorId(String idBuscado) {
        for (int i = 0; i < totalClientes; i++) {
            if (clientes[i].getIdCliente().equals(idBuscado)) {
                return clientes[i]; // Cliente encontrado
            }
        }
        return null; // Cliente no encontrado
    }

    public static Cuenta buscarCuentaPorNcuenta(int cuenta) {
        for (int i = 0; i < totalCuentas; i++) {
            if (cuentas[i].getNumCuentaCliente() == cuenta) {
                return cuentas[i]; //  Cliente encontrado
            }
        }
        return null; // Cuenta no encontrada
    }

    public static Cuenta buscarCuentaPorNcuentaOrigen(int cuenta, Cliente cliente) {
        for (int i = 0; i < totalCuentas; i++) {
            if (cliente.getIdCliente().equals(cuentas[i].getIdCliente()) && cuentas[i].getNumCuentaCliente() == cuenta) {
                return cuentas[i];
            }
        }
        return null; // Cuenta no encontrada
    }
    
    public static Cuenta buscarCuentaPorIdCliente(Cliente cliente) {
        for (int i = 0; i < totalCuentas; i++) {
            if (cliente.getIdCliente().equals(cuentas[i].getIdCliente())) {
                return cuentas[i];
            }
        }
        return null; // Cuenta no encontrada
    }
    
    public static Cuenta buscarCuentaDeClientePorTipoCuenta(Cliente  cliente, String tipoCuenta) {
        for (int i = 0; i < totalCuentas; i++) {
            if (cliente.getIdCliente().equals(cuentas[i].getIdCliente()) && cuentas[i].getTipoCuenta().toString().equals(tipoCuenta)) {
                return cuentas[i];
            }
        }
        return null; // Cuenta no encontrada
    }

    public static Cliente buscarClientePorUsuario(String usuario) {
        for (int i = 0; i < totalClientes; i++) {
            if (clientes[i].getUsuarioCliente().equals(usuario)) {
                return clientes[i]; // Cliente encontrado
            }
        }
        return null; // Cliente no encontrado
    }

    public static String validoID() {
        String[] opciones = {"Intentar con otro ID", "Cancelar"};

        while (true) {
            String id = JOptionPane.showInputDialog("Ingresar ID del cliente:");

            if (id == null) {
                return null; // Usuario canceló
            }
            boolean existe = false;

            for (int i = 0; i < totalClientes; i++) {
                if (clientes[i] != null && clientes[i].getIdCliente().equals(id)) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                return id;
            } else {
                int opcion = JOptionPane.showOptionDialog(
                        null,
                        "Ya existe un cliente con ese ID.",
                        "ID Duplicado",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        opciones,
                        opciones[0]
                );

                if (opcion != 0) {
                    return null; // Cancelar
                }
            }
        }
    }

    public static String validoCorreo() {
        String correo = null;
        String opcionCorreo[] = {"Agregar otro correo", "Cancelar"};
        int opcion;
        String correoCliente = JOptionPane.showInputDialog("Ingresar correo del cliente:");
        if (correoCliente.contains("@") && correoCliente.indexOf('.') > correoCliente.indexOf('@')) {
            return correoCliente;

        } else {
            opcion = JOptionPane.showOptionDialog(null, "Correo Invalido", "", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, opcionCorreo, "Agregar otro correo");
            if (opcion == 0) {
                return correo;
            } else {
                JOptionPane.showMessageDialog(null, "ERROR favor agregar al usuario otra vez");
                agregarCliente();
            }
        }
        return correo;
    }
    
    private static String solicitarIdCliente() {
        String id = JOptionPane.showInputDialog("Ingrese el ID del cliente (o dejar vacío para Todos):");
        if (id == null || id.trim().equals("")) {
            return "Todos";
        }
        for (int i = 0; i < clientes.length; i++) {
            if (clientes[i] != null && clientes[i].getIdCliente().equals(id)) {
                return id;
            }
        }
        JOptionPane.showMessageDialog(null, "Cliente no encontrado, manteniendo filtro en 'Todos'");
        return "Todos";
    }

    private static String solicitarEstado() {
        String estados[] = {"Activo", "Inactivo", "Todos"};
        int opcion = JOptionPane.showOptionDialog(null, "Seleccione estado:", "Filtro Estado",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, estados, estados[0]);
        return estados[opcion];
    }

    private static String solicitarTipoCuenta() {
        String tipos[] = {"Corriente", "Ahorros", "Inversion", "Planilla", "Todos"};
        int opcion = JOptionPane.showOptionDialog(null, "Seleccione tipo de cuenta:", "Filtro Tipo de Cuenta",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, tipos, tipos[0]);
        return tipos[opcion];
    }

    private static double solicitarSaldo() {
        // Arreglo de dígitos permitidos
        char numeros[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

        String saldoIngresado = JOptionPane.showInputDialog("Ingrese saldo (o dejar vacío para 0):");

        if (saldoIngresado == null || saldoIngresado.trim().equals("")) {
            return 0.0; // Si está vacío, devuelve 0
        }

        boolean valido = false;

        while (!valido) {
            valido = true; // Asumimos que es válido

            for (int i = 0; i < saldoIngresado.length(); i++) {
                char c = saldoIngresado.charAt(i);
                boolean esNumero = false;

                // Revisar si el carácter está en el arreglo de números permitidos
                for (int j = 0; j < numeros.length; j++) {
                    if (c == numeros[j]) {
                        esNumero = true;
                        break;
                    }
                }

                if (!esNumero) {
                    valido = false;
                    break;
                }
            }

            if (!valido) {
                saldoIngresado = JOptionPane.showInputDialog("Entrada inválida. Ingrese solo números (o dejar vacío para 0):");
                if (saldoIngresado == null || saldoIngresado.trim().equals("")) {
                    return 0.0;
                }
            }
        }

        return Double.parseDouble(saldoIngresado);
    }

    private static String solicitarTipoSaldo() {
        String[] opciones = {"Mayor", "Menor", "Todos"};
        int opcion = JOptionPane.showOptionDialog(null, "Seleccione tipo de comparación de saldo:",
                "Filtro Saldo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);
        return opciones[opcion];
    }
    
    private static boolean pasaFiltros(Cliente cliente, Cuenta cuenta) {
        // Filtro ID cliente
        if (!filtroIdCliente.equals("Todos") && !cliente.getIdCliente().equals(filtroIdCliente)) {
            return false;
        }
        // Filtro estado
        EstadoCliente estadoCliente;
        if (filtroEstado.equals("Activo")) {
            estadoCliente = EstadoCliente.Activo;
        } else{
            estadoCliente = EstadoCliente.Inactivo;
        }
        if (!filtroEstado.equals("Todos") && !cliente.getEstadoCliente().equals(estadoCliente)) {
            return false;
        }
        // Filtro tipo de cuenta
        TipoCuenta tipoCuenta;
        if (filtroTipoCuenta.equals("Corriente")) {
            tipoCuenta = TipoCuenta.Corriente;
        } else if (filtroTipoCuenta.equals("Ahorros")) {
            tipoCuenta = TipoCuenta.Ahorros;
        } else if (filtroTipoCuenta.equals("Inversion")) {
            tipoCuenta = TipoCuenta.Inversion;
        } else{
            tipoCuenta = TipoCuenta.Planilla;
        }
        if (!filtroTipoCuenta.equals("Todos") && !cuenta.getTipoCuenta().equals(tipoCuenta)) {
            return false;
        }
        // Filtro saldo
        if (!filtroSaldoTipo.equals("Todos") && filtroSaldoValor != 0.0) {
            if (filtroSaldoTipo.equals("Mayor") && cuenta.getSaldoInicial() < filtroSaldoValor) {
                return false;
            }
            if (filtroSaldoTipo.equals("Menor") && cuenta.getSaldoInicial() > filtroSaldoValor) {
                return false;
            }
        }
        return true;
    }

}
