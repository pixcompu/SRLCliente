package logic;

import java.util.Iterator;

/**
 *
 * @author PIX
 */
public interface Manager<Type> {
    void add(Type newItem);
    void update(Type updatedItem);
    void remove(String itemID);
    Iterator<Type> getAll();
    Type getById(String itemID);
}
