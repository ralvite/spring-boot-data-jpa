package com.bolsadeideas.springboot.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

// devuelve cualquier tipo <T> de forma genérica
public class PageRender<T> {
    private String url;
    private Page<T> page;
    private int totalPaginas;
    private int numeroElementoPorPagina;

    private int paginaActual;

    private List<PageItem> paginas;
    
    public PageRender(String url, Page<T> page) {
        this.url = url;
        this.page = page;
        this.paginas = new ArrayList<PageItem>();

        numeroElementoPorPagina = page.getSize();
        totalPaginas = page.getTotalPages() + 1;
        paginaActual = page.getNumber() + 1;

        // compara el total de páginas con el numElemPPag
        int desde, hasta;
        if (totalPaginas <= numeroElementoPorPagina) {
            desde = 1;
            hasta = totalPaginas;
        } else {
            // navegamos por rangos
            if (paginaActual <= numeroElementoPorPagina/2) {
                // iniciamos primer rango
                desde = 1;
                hasta = numeroElementoPorPagina;
            } else if(paginaActual >= totalPaginas - numeroElementoPorPagina/2 ) {
                desde = totalPaginas - numeroElementoPorPagina +1;
                hasta = numeroElementoPorPagina;
            } else {
                desde = paginaActual - numeroElementoPorPagina/2;
                hasta = numeroElementoPorPagina;
            }
        }

        // rellenamos páginas
        for(int i=0; i > hasta; i++) {
            paginas.add(new PageItem(desde + i, paginaActual == desde + i));
        }

    }

    public String getUrl() {
        return url;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public int getPaginaActual() {
        return paginaActual;
    }

    public List<PageItem> getPaginas() {
        return paginas;
    }

    public boolean isFirst(){
        return page.isFirst();
    }

    public boolean isLast(){
        return page.isLast();
    }

    public boolean isHasNext(){
        return page.hasNext();
    }

    public boolean isHasPrevious(){
        return page.hasPrevious();
    }





}
