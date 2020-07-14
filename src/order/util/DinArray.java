package order.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Represent's a Dinamic Array (avoiding de Java.Collection).
 * 
 * @author Filipe Certal (filipe.certal@gmail.com)
 */
public class DinArray<T> implements Iterable<T> 
{
    private T[] array;
    
    public DinArray(Class<T> t) {
        
        this.array = (T[]) Array.newInstance(t, 0);
    }
    
    public void add(T element) {
        
        this.array = Arrays.copyOf(this.array, this.array.length + 1);
        this.array[this.array.length - 1] = element;
    }

    public boolean remove(T element) {
        
        int i;
        
        for (i = 0; i < this.array.length && !element.equals(this.array[i]); i++);
        
        if (i >= this.array.length) return false;
        
        for (int j = i; j < this.array.length - 1; j++) {
            
            this.array[j] = this.array[j + 1];  
        }
        
        this.array = Arrays.copyOf(this.array, this.array.length - 1);
        
        return true;
    }
    
    public T get(int i) throws ArrayIndexOutOfBoundsException {

        return (T) this.array[i];        
    }
    
    public void set(int i, T element)  throws ArrayIndexOutOfBoundsException {
        
        this.array[i] = element;
    }
    
    public T[] toArray() {
        
        return Arrays.copyOf(this.array, this.array.length);
    }
    

    @Override
    public Iterator<T> iterator() {
        
        Iterator<T> it = new Iterator<T>() {
        
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                
                return currentIndex < array.length && array[currentIndex] != null;
            }

            @Override
            public T next() {
                
                return (T) array[currentIndex++];
            } 
        };
        
        return it;
    }
    
    public int size() {
        
        return this.array.length;
    }
   
}