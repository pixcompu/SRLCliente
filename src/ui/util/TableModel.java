package ui.util;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * Adaptador para establecer como modelo personalizado a cualquier componente JTable.
 * @author Felix Diaz ®
 */
public class TableModel extends AbstractTableModel {

    /**
     * Los tipos de dato que corresponden al contenido de cada celda son tipo Object.
     * Cada fila y columna son representadas con Objetos ArrayList.
     */
    
    private final String[] cabecera_;
    private final ArrayList<ArrayList<Object>> tabla_; 

/**
 * Constructor del adaptador, simplemente recibe el nombre de las columnas
 * @param cabecera 
 */
    public TableModel(String[] cabecera) {
        this.cabecera_ = cabecera;
        tabla_ = new ArrayList<>();
    }

    /**
     * Este regresa un ArrayList(a) de ArrayList(b) donde cada ArrayList(b) contiene los valores de una fila
     * @return 
     */
    public ArrayList obtenerTabla(){
        return this.tabla_;
    }

    /**
     * Recibe un arrayList con datos previamente cargados, el cual añade a modo de fila a la tabla y la actualiza
     * con fireTableChanged... 
     * @param fila 
     */
    public void addRow(ArrayList fila){
        tabla_.add(fila);
        fireTableDataChanged();
    }
    
    /**
     * Quita una fila por indice
     * @param indiceFila 
     */
    public void eliminarFila(int indiceFila){
        tabla_.remove(indiceFila);
        fireTableDataChanged();
    }
    
    public void reiniciarTabla(){
        tabla_.clear();
        fireTableDataChanged();
    }
    
    public boolean estaVacia(){
        return tabla_.isEmpty();
    }
    
    public ArrayList<Object> obtenerFila(int indiceFila){
        return tabla_.get(indiceFila);
    }
    
    public void filtrarContenido(String porBuscar){
        
        String filtro = porBuscar.toLowerCase();
        ArrayList<ArrayList<Object>> resultado = new ArrayList<>();
        
        for (int filaActual = 0; filaActual < getRowCount(); filaActual++) {
            
            ArrayList<Object> fila = obtenerFila(filaActual);  
            for (int celdaActual = 0; celdaActual < fila.size(); celdaActual++) {
                
                String contenidoCelda = String.valueOf(fila.get(celdaActual)).toLowerCase();
                if(contenidoCelda.contains(filtro)){
                    resultado.add(fila);
                    break;
                }
            }
        }
        
        mostrarContenidoFiltrado(resultado);

    }
    
    @Override
    public String getColumnName(int columnIndex) {
        return cabecera_[columnIndex];
    }

    /**
     * Metodo para que las celdas no sean editables
     * @param rowIndex
     * @param columnIndex
     * @return 
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public int getRowCount() {
        return tabla_.size();
    }

    @Override
    public int getColumnCount() {
        return cabecera_.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return tabla_.get(rowIndex).get(columnIndex);
    }

    @Override
    public void setValueAt(Object newValue, int rowIndex, int columnIndex) {
        tabla_.get(rowIndex).set(columnIndex, newValue);
        fireTableCellUpdated(rowIndex, columnIndex);
    }
    
    private void mostrarContenidoFiltrado(ArrayList<ArrayList<Object>> lista){
        
        reiniciarTabla();
        
        for (ArrayList<Object> fila : lista) {
            addRow(fila);
        }
    }
    
}
