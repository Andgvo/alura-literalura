package com.aluracursos.literalura;


import com.aluracursos.literalura.dto.GuntendexResponse;
import com.aluracursos.literalura.entity.Libro;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;

import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Principal {

    private static final String URL_GUNTENDEX_BOOK = "https://gutendex.com/books";
    private static final Set<String> CURRENCY_CODE = Set.of("ARS", "BOB", "BRL", "CLP", "COP", "USD");
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String OPCIONES_MENU = """
            ================== MENU ===================
            1. Buscar libro por titulo o autor en WEB
            2. Busqueda por titulo en BD
            3. Todos los libros en BD
            4. Todos los autores en BD
            5. Autores vivos en un determinado año
            0. Salir
            """;
    private final AutorRepository autorRepository;
    private final LibroRepository libroRepository;

    public  Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void ejecutarMenu() {
        int opc;
        do {
            opc = validarCantidad(OPCIONES_MENU);
            switch (opc) {
                case 0 -> System.out.println("\n\nHasta la próxima\n\n");
                case 1 -> buscarLibroPorTituloOAutorWeb();
                case 2 -> buscarLibroPorTituloBD();
                case 3 -> buscarTodosLibros();
                case 4 -> buscarTodosAutores();
                case 5 -> buscarAutoresVivosPorAnio();
                default -> System.err.println("Favor de elegir una opción valida");
            }
        } while (opc != 0);
    }

    public void buscarLibroPorTituloOAutorWeb() {
        System.out.println("Ingresa el nombre del titulo o autor: ");
        var busqueda = SCANNER.nextLine();
        busqueda = busqueda.trim().toLowerCase().replaceAll(" ", "%20");
        final var response = ConsumoAPI.get(URL_GUNTENDEX_BOOK + "/?search=" + busqueda, GuntendexResponse.class);
        final var libros = response.resultado().stream().map( libroDto -> new Libro(libroDto)).collect(Collectors.toList());
        final var autores = libros.stream().map(Libro::getAutor).filter(Objects::nonNull).collect(Collectors.toList());
        System.out.println("------------- RESULTADO -----------------");
        libros.forEach(System.out::println);
        System.out.println("-----------------------------------------");
        autorRepository.saveAll(autores);
        libroRepository.saveAll(libros);
    }

    public void buscarLibroPorTituloBD() {
        System.out.println("Ingresa el nombre del titulo: ");
        var busqueda = SCANNER.nextLine().toLowerCase();
        final var resultadoBusqueda = libroRepository.findByTituloContainsIgnoreCase(busqueda);
        System.out.println("------------- RESULTADO -----------------");
        if(!resultadoBusqueda.isEmpty()){
            resultadoBusqueda.forEach(System.out::println);
        } else {
            System.out.println("Serie no encontrada");
        }
        System.out.println("-----------------------------------------");
    }

    public void buscarTodosLibros() {
        final var resultadoBusqueda = libroRepository.findAll();
        System.out.println("------------- RESULTADO -----------------");
        if(!resultadoBusqueda.isEmpty()){
            resultadoBusqueda.forEach(System.out::println);
        } else {
            System.out.println("Sin resultados");
        }
        System.out.println("-----------------------------------------");
    }

    public void buscarTodosAutores() {
        final var resultadoBusqueda = autorRepository.findAll();
        System.out.println("------------- RESULTADO -----------------");
        if(!resultadoBusqueda.isEmpty()){
            resultadoBusqueda.forEach(System.out::println);
        } else {
            System.out.println("Sin resultados");
        }
        System.out.println("-----------------------------------------");
    }

    public void buscarAutoresVivosPorAnio() {
        var busqueda = validarCantidad("Ingresa el año para buscar a autores vivos en esa epoca");
        final var resultadoBusqueda = autorRepository.findAllVivoByAnio(busqueda);
        System.out.println("------------- RESULTADO -----------------");
        if(!resultadoBusqueda.isEmpty()){
            resultadoBusqueda.forEach(System.out::println);
        } else {
            System.out.println("Sin resultados");
        }
        System.out.println("-----------------------------------------");
    }

    // VALIDACIONES
    private String validarCodigoMoneda(String mensaje) {
        String codigoMoneda;
        boolean noValido;
        do {
            System.out.println(mensaje);
            codigoMoneda = SCANNER.next().toUpperCase().trim();
            noValido = !CURRENCY_CODE.contains(codigoMoneda);
            if (noValido) {
                System.err.println("\nxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                System.err.println("Ingrese una opcion valida: ");
                System.err.println(CURRENCY_CODE);
                System.err.println("xxxxxxxxxxxxxxxxxxxxxxxxxxx\n");
            }
        } while (noValido);
        return codigoMoneda;
    }

    private int validarCantidad(String mensaje) {
        int cantidad = 0;
        boolean noValido = true;
        do {
            try {
                System.out.println(mensaje);
                var cantidadStr = SCANNER.nextLine().trim();
                cantidad = Integer.parseInt(cantidadStr);
                noValido = false;
            } catch (Exception e) {
                System.err.println("xxxxxxxxxxxxxxxxxxxxxxxxxxx");
                System.err.println("Ingrese una opcion valida ");
            }
        } while (noValido);
        return cantidad;
    }
}
