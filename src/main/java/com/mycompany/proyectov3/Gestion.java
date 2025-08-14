package com.mycompany.proyectov3;

import java.util.Random;
import javax.swing.JOptionPane;
/**
 * genera datos y movimientos aleatorios,busca clientes y cuentas,
 * autentificacion de  clietne por usuario/ clave y tarjeta de acceso
 * reportes por consola con filtros y posee menus
 * 
 */
public class Gestion {

    static Cuenta cuentas[] = new Cuenta[150];//cuentas capacidad de 150
    static Cliente clientes[] = new Cliente[30];//cleintes capacidad de 30
    static int totalClientes = 0;
    static int totalCuentas = 0;
    //filtros para reportes con valores por defecto
    static String filtroIdCliente = "Todos";
    static String filtroEstado = "Todos";
    static String filtroTipoCuenta = "Todos";
    static String filtroSaldoTipo = "Todos";
    static double filtroSaldoValor = 0.0;

//------Metodos del Menu Bancario ------------------------------------------------------------------------
    //genera datos iniciales de 10 clientes, en 12 cuentras con 0-5 
    //movimientos, no duplica clientes
    public static void gestionDatos() {
        if (totalClientes > 0) {
            JOptionPane.showMessageDialog(null, "Datos ingresados al sistema");
            return;
        }

        String[] nombres = {"Fernando", "Carlos", "Federico", "Karla", "Jimena", "Arianna", "Lucia", "Daniela", "Brenda", "Dayanna"};
        String[] apellidos = {"Aguilar", "Avila", "Rodriguez", "Cascante", "Sequiera", "Hernandez", "Araya", "Chinchilla", "Vargas", "Guerrero"};
        //crea 10 clientes
        for (int i = 0; i < 10; i++) {
            String identificaciones = "1-100" + i + "-000" + i;
            String nombre = nombres[i] + " " + apellidos[i];
            String telefono = "7000-000" + i;
            String correo = "cliente" + i + "@gmail.com";
            Cliente nuevo = new Cliente(identificaciones, nombre, telefono, correo);
            clientes[i] = nuevo;
            totalClientes++;
        }
        //crea 12 cuents repartidads entre los primeros 9 clientes
        for (int i = 0; i < 12; i++) {
            Cliente generado = clientes[i % 9]; // distribuye entre primeros 9 clientes
            Cuenta nuevaCuenta = new Cuenta(generado, generado.getIdCliente(), TipoCuenta.Corriente, (i + 1) * 100);
            cuentas[totalCuentas++] = nuevaCuenta;
            generado.agregarCuenta(nuevaCuenta.getNumCuentaCliente());
        }
        //genera movimientos aleatorios
        Random random = new Random();

        for (int i = 0; i < totalCuentas; i++) {
            Cuenta cuenta = cuentas[i];
            if (cuenta == null) {
                continue;
            }

            int cantidadMovs = random.nextInt(0, 6); // de 0 a 5 movimientos

            for (int j = 0; j < cantidadMovs; j++) {
                TipoMovimiento tipoMovimiento;
                String detalle;
                double monto = 1000 + random.nextInt(0, 90001); // 1000 a 91000
                int NcuentaOrigen = cuenta.getNumCuentaCliente();

                // Elegir aleatoriamente, pero evitando saldo negativo
                if (random.nextBoolean() || cuenta.getSaldoInicial() - monto < 0) {
                    tipoMovimiento = TipoMovimiento.Deposito;
                    detalle = "Deposito $" + monto;
                    cuenta.setSaldoInicial(cuenta.getSaldoInicial() + monto);
                } else {
                    tipoMovimiento = TipoMovimiento.Retiro;
                    detalle = "Retiro $" + monto;
                    cuenta.setSaldoInicial(cuenta.getSaldoInicial() - monto);
                }

                Movimientos movimiento = new Movimientos(tipoMovimiento, NcuentaOrigen, monto, detalle);
                movimiento.setNcuentaOrigen(NcuentaOrigen); // para que quede registrado
                cuenta.agregarMovimiento(movimiento);
            }
        }

        JOptionPane.showMessageDialog(null, "Datos generados con éxito");
    }
    //agrega un nuevo cliente solicitando los datos
    //valida que el id no exista y el formato del correo
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
    //crea una cuenta para un cliente existenta(max 5 por cliente)
    //permite seleccionar el tipo de cuenta y saldo inicial

    public static void agregarCuenta() {
        String idCliente = JOptionPane.showInputDialog("Ingrese su ID en formato 0-0000-0000: ");
        Cliente clienteEncontrado = null;

        // Buscar cliente
        for (int i = 0; i < totalClientes; i++) {
            if (clientes[i] != null && idCliente.equals(clientes[i].getIdCliente())) {
                clienteEncontrado = clientes[i];
                break;
            }
        }

        // Si no existe
        if (clienteEncontrado == null) {
            String opcionesBTN[] = {"Agregar otro ID", "Cancelar"};
            int valorBTN = JOptionPane.showOptionDialog(null, "Cliente no encontrado", " ",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, opcionesBTN, opcionesBTN[0]);
            if (valorBTN == 0) {
                agregarCuenta();
            } else {
                JOptionPane.showMessageDialog(null, "Volviendo al menú");
            }
            return;
        }

        // Mostrar cuántas cuentas tiene
        JOptionPane.showMessageDialog(null, "El usuario tiene " + clienteEncontrado.getCantidadCuentas() + " cuentas.");

        if (clienteEncontrado.getCantidadCuentas() >= 5) {
            JOptionPane.showMessageDialog(null, "El cliente ya tiene 5 cuentas.");
            return;
        }

        // Seleccionar tipo de cuenta
        TipoCuenta tipo = null;
        String tipoCuentaBTN[] = {"Corriente", "Ahorros", "Inversion", "Planilla"};
        int valorBTN = JOptionPane.showOptionDialog(null, "Indique el tipo de cuenta", "Tipo de cuenta",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, tipoCuentaBTN, tipoCuentaBTN[0]);

        switch (valorBTN) {
            case 0:
                tipo = TipoCuenta.Corriente;
                break;
            case 1:
                tipo = TipoCuenta.Ahorros;
                break;
            case 2:
                tipo = TipoCuenta.Inversion;
                break; // 
            case 3:
                tipo = TipoCuenta.Planilla;
                break;
            default:
                tipo = TipoCuenta.Ahorros;
        }

        double saldoInicial = 0;
        String saldoIngresadoStr = JOptionPane.showInputDialog("Ingrese el monto inicial de la cuenta nueva:");
        if (saldoIngresadoStr != null && !saldoIngresadoStr.trim().equals("")) {
            boolean esNumero = true;
            for (int i = 0; i < saldoIngresadoStr.length(); i++) {
                char c = saldoIngresadoStr.charAt(i);
                if (c < '0' || c > '9') {
                    esNumero = false;
                    break;
                }
            }
            if (esNumero) {
                saldoInicial = Double.parseDouble(saldoIngresadoStr);
            }
        }

        // Crear la cuenta y asignarla al cliente
        Cuenta nueva = new Cuenta(clienteEncontrado, clienteEncontrado.getIdCliente(), tipo, saldoInicial);
        cuentas[totalCuentas++] = nueva;
        clienteEncontrado.agregarCuenta(nueva.getNumCuentaCliente());

        JOptionPane.showMessageDialog(null, "Cuenta agregada con éxito.");
    }
    // imprime la lista de clientes marca vacio donde no hay
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
    //imrprime la lista de cuentas y el historial de movimientos
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
    //busca un cliente por el id, permite actualizarlo/desactivarlo o ver cuentas
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
    //busca un cuenta por numero y permite consultar sus movimientos 
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
                            } else {
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
    //muestra un menuú para configurar filtros y generar el reporte
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
                    "Filtros del Cliente:\nID: " + filtroIdCliente + "\nEstado: " + filtroEstado
                    + "\n\nFiltros de la Cuenta:\nTipo: " + filtroTipoCuenta + "\nSaldo: " + textoSaldo,
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
    //genera con consola el reporte de cuentras aplicando toos los filtros
    private static void generarReporte() {
        System.out.println("===== REPORTE DE CUENTAS =====");
        boolean hayResultados = false;

        for (int i = 0; i < totalClientes; i++) {
            Cliente cliente = clientes[i];
            if (cliente == null) {
                continue;
            }
            // --- FILTRO ID CLIENTE ---
            if (!"Todos".equalsIgnoreCase(filtroIdCliente)
                    && !cliente.getIdCliente().equalsIgnoreCase(filtroIdCliente)) {
                continue;
            }
            // --- FILTRO ESTADO ---
            if (!"Todos".equalsIgnoreCase(filtroEstado)) {
                EstadoCliente estadoEnum = cliente.getEstadoCliente();
                if ((estadoEnum == EstadoCliente.Activo && !"Activo".equalsIgnoreCase(filtroEstado))
                        || (estadoEnum == EstadoCliente.Inactivo && !"Inactivo".equalsIgnoreCase(filtroEstado))) {
                    continue;
                }
            }

            // Recorremos todas las cuentas del cliente
            for (int j = 0; j < cliente.getCantidadCuentas(); j++) {
                Cuenta cuenta = buscarCuentaPorNcuenta(cliente.getNumerosCuentas()[j]);
                if (cuenta == null) {
                    continue;
                }
                // --- FILTRO TIPO CUENTA ---
                if (!"Todos".equalsIgnoreCase(filtroTipoCuenta)) {
                    TipoCuenta tipoEnum = cuenta.getTipoCuenta();
                    if ((tipoEnum == TipoCuenta.Corriente && !"Corriente".equalsIgnoreCase(filtroTipoCuenta))
                            || (tipoEnum == TipoCuenta.Ahorros && !"Ahorros".equalsIgnoreCase(filtroTipoCuenta))
                            || (tipoEnum == TipoCuenta.Inversion && !"Inversion".equalsIgnoreCase(filtroTipoCuenta))
                            || (tipoEnum == TipoCuenta.Planilla && !"Planilla".equalsIgnoreCase(filtroTipoCuenta))) {
                        continue;
                    }
                }
                // --- FILTRO SALDO ---
                if (!"Todos".equalsIgnoreCase(filtroSaldoTipo)) {
                    if ("Mayor".equalsIgnoreCase(filtroSaldoTipo)
                            && !(cuenta.getSaldoInicial() > filtroSaldoValor)) {
                        continue;
                    }
                    if ("Menor".equalsIgnoreCase(filtroSaldoTipo)
                            && !(cuenta.getSaldoInicial() < filtroSaldoValor)) {
                        continue;
                    }
                }
                System.out.println("[Id cliente]: " + cliente.getIdCliente()
                            + " [Estado]: " + cliente.getEstadoCliente()
                            + " [Número de cuenta]: " + cuenta.getNumCuentaCliente()
                            + " [Tipo de cuenta]: " + cuenta.getTipoCuenta()
                            + " [Saldo]: $" + cuenta.getSaldoInicial());
                hayResultados = true;
            }
        }

        if (!hayResultados) {
            System.out.println("No se encontraron resultados con los filtros aplicados.");
        }
        System.out.println("==============================\n");
    }
    //------Metodos del Menu Cliente ------------------------------------------------------------------------
    //inicia el login del cliente , si no tiene una clave le solicita crear una
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
    // menu principal para clientees, transaccions, mis cuentas, actualizar
    public static void MenuClientes(Cliente cliente) {
        int opcionMenu;
        do {
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
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "La opción " + opcionMenu + " ingresada no valida, intente de nuevo.");
            }
        } while (opcionMenu != 3);

    }
    //submenu de transacciones, desposito, retiro , transferencia, compra
    public static void RealizarTransacciones(Cliente cliente) {
        int opcionMenuTransaccion;
        do {
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
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "La opción " + opcionMenuTransaccion + " ingresada no valida, intente de nuevo.");
            }
        } while (opcionMenuTransaccion != 4);

    }
    //realiza un depositido en una cuenta ya existente
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
    //realiza un retiro si el saldo es suficiente 
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
    //realiza una transferencia entre cuentas si hay salgo suficiente 
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
    //registra una compra en una cuenta del cliente si el saldo alcanza
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
    //muestra las cuentas y moviminetos del cliente autrnticado 
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
    //permite actualizar nombre, telefono, correo o clave
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
    //solicita y valida que la clave cumpla con 6-10 carateres
    //si es correcta se asigna al cliente
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
//valida la tarjeta de acceso de cliente solicitando 3 coordenadas aleatorias
    public static boolean validarTarjetaAcceso(Cliente cliente) {
        Random random = new Random();
        String columnas[] = {"A", "B", "C", "D", "E"};
        // Encabezado
        //mostrar tarjeta de referncia 
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
//muestra las cuentas de un cliente segun su id
    public static void cuentasClientes() {
        String id = JOptionPane.showInputDialog("Ingrese el ID del usuario");
        Cliente clien = buscarClientePorId(id);
        for (int i = 0; i < totalCuentas; i++) {
            if (cuentas[i].getIdCliente().equals(clien.getIdCliente())) {
                System.out.println(cuentas[i].obtenerDatos(false));
            }
        }
    }
//busca un cliente por su id
    public static Cliente buscarClientePorId(String idBuscado) {
        for (int i = 0; i < totalClientes; i++) {
            if (clientes[i].getIdCliente().equals(idBuscado)) {
                return clientes[i]; // Cliente encontrado
            }
        }
        return null; // Cliente no encontrado
    }
//busca una cuenta por numero 
    public static Cuenta buscarCuentaPorNcuenta(int cuenta) {
        for (int i = 0; i < totalCuentas; i++) {
            if (cuentas[i].getNumCuentaCliente() == cuenta) {
                return cuentas[i]; //  Cliente encontrado
            }
        }
        return null; // Cuenta no encontrada
    }
//busca una cuenta por numero que pertenece al clinete indicado
    public static Cuenta buscarCuentaPorNcuentaOrigen(int cuenta, Cliente cliente) {
        for (int i = 0; i < totalCuentas; i++) {
            if (cliente.getIdCliente().equals(cuentas[i].getIdCliente()) && cuentas[i].getNumCuentaCliente() == cuenta) {
                return cuentas[i];
            }
        }
        return null; // Cuenta no encontrada
    }
//devuelve la primera cuenta asociaad al cliente  si existe
    public static Cuenta buscarCuentaPorIdCliente(Cliente cliente) {
        for (int i = 0; i < totalCuentas; i++) {
            if (cliente.getIdCliente().equals(cuentas[i].getIdCliente())) {
                return cuentas[i];
            }
        }
        return null; // Cuenta no encontrada
    }
//busca cuenta del cliente por tipo de cuneta 
    public static Cuenta buscarCuentaDeClientePorTipoCuenta(Cliente cliente, String tipoCuenta) {
        for (int i = 0; i < totalCuentas; i++) {
            if (cliente.getIdCliente().equals(cuentas[i].getIdCliente()) && cuentas[i].getTipoCuenta().toString().equals(tipoCuenta)) {
                return cuentas[i];
            }
        }
        return null; // Cuenta no encontrada
    }
//busca el cleinte por usuario
    public static Cliente buscarClientePorUsuario(String usuario) {
        for (int i = 0; i < totalClientes; i++) {
            if (clientes[i].getUsuarioCliente().equals(usuario)) {
                return clientes[i]; // Cliente encontrado
            }
        }
        return null; // Cliente no encontrado
    }
//solicita un id de cliente que no este repetido
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
//solicita un correo con validaciones basicas
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
//pide un id de cliente existente o deja "todos"
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
//pide el estado activo, inactivo o todos
    private static String solicitarEstado() {
        String estados[] = {"Activo", "Inactivo", "Todos"};
        int opcion = JOptionPane.showOptionDialog(null, "Seleccione estado:", "Filtro Estado",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, estados, estados[0]);
        return estados[opcion];
    }
// pide el tipo de cuenta corriente, ahorros, inversión, planilla o todso
    private static String solicitarTipoCuenta() {
        String tipos[] = {"Corriente", "Ahorros", "Inversion", "Planilla", "Todos"};
        int opcion = JOptionPane.showOptionDialog(null, "Seleccione tipo de cuenta:", "Filtro Tipo de Cuenta",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, tipos, tipos[0]);
        return tipos[opcion];
    }
//pide unvalor de saldo solo digitos
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
//pide el tipo de comparacion para salso, mayor, menos o todos
    private static String solicitarTipoSaldo() {
        String[] opciones = {"Mayor", "Menor", "Todos"};
        int opcion = JOptionPane.showOptionDialog(null, "Seleccione tipo de comparación de saldo:",
                "Filtro Saldo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);
        return opciones[opcion];
    }
//evalua si una cueneta pasa los filtros activos(id,estado, tipo, salfo)
    private static boolean pasaFiltros(Cliente cliente, Cuenta cuenta) {
        // Filtro ID cliente
        if (!filtroIdCliente.equals("Todos") && !cliente.getIdCliente().equals(filtroIdCliente)) {
            return false;
        }
        // Filtro estado
        EstadoCliente estadoCliente;
        if (filtroEstado.equals("Activo")) {
            estadoCliente = EstadoCliente.Activo;
        } else {
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
        } else {
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
