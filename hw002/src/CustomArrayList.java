import java.util.*;

public class CustomArrayList<T> {

    private int size;
    private static final int DEFAULT_CAPACITY = 10;
    transient private Object[] array;

    public CustomArrayList(int initialCapacity){
        if (initialCapacity > 0) {
            this.array = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.array = new Object[DEFAULT_CAPACITY];
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
    }

    public CustomArrayList(){
        this.array = new Object[DEFAULT_CAPACITY];
    }

    public CustomArrayList(Collection<? extends T> c) {
        Object[] a = c.toArray();
        size = a.length;
        if (size != 0) {
            array = Arrays.copyOf(a, size, Object[].class);
        } else {
            array = new Object[DEFAULT_CAPACITY];
        }
    }

    public int size(){
        return size;
    }

    public void add(T element){
        if(size == array.length){
            array = increaseArray(size + 1);
        }
        array[size] = element;
        size++;
    }

    public void add(int index, T element){
        rangeCheck(index);
        if (size == array.length){
            array = increaseArray(size + 1);
        }
        System.arraycopy(array, index, array, index + 1, size - index);
        array[index] = element;
        size++;
    }

    public boolean addAll(Collection<? extends T> c){
        Object[] a = c.toArray();
        int numNew = a.length;
        if (numNew == 0){
            return false;
        }
        if (numNew > array.length - size){
            array = increaseArray(size + numNew);
        }
        System.arraycopy(a, 0, array, size, numNew);
        size += numNew;
        return true;
    }

    public void clear(){
        int s = size;
        this.size = 0;
        for (int i = 0; i < s; i++)
            array[i] = null;
    }

    @SuppressWarnings("unchecked")
    public T get(int index){
        rangeCheck(index);
        return (T) array[index];
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public T remove(int index){
        rangeCheck(index);
        @SuppressWarnings("unchecked")
        T oldValue = (T) array[index];
        System.arraycopy(array, index+1, array, index, array.length-index-1);
        array[size-1] = null;
        size--;
        return oldValue;
    }

    public boolean remove(Object o){
        int i = 0;
        found: {
            if (o == null) {
                for (; i < size; i++)
                    if (array[i] == null)
                        break found;
            } else {
                for (; i < size; i++)
                    if (o.equals(array[i]))
                        break found;
            }
            return false;
        }
        remove(i);
        return true;
    }

    /**
     * Sorting an array using quicksort. If the size of the array is zero
     * or one, then we interrupt the sorting.
     * @param c - comparator
     */
    public void sort(Comparator<? super T> c){
        if(size == 0 || size == 1) return;
        quickSort(c, 0, size - 1);
    }

    /**
     * Recursive method for quickly sorting an array by a given comparator.
     * @param c - comparator
     * @param low - lower bound of the subarray
     * @param high - upper bound of the subarray
     */
    private void quickSort(Comparator<? super T> c, int low, int high){
        if(low < high){
            int partition = partition(c, low, high);

            quickSort(c, low, partition -1);
            quickSort(c, partition + 1, high);
        }
    }

    /**
     * Sort the elements of a subarray in relation to the average value of this subarray based on a given comparator.
     * @param c - comparator
     * @param low - lower bound of the subarray
     * @param high - upper bound of the subarray
     * @return - index separating subarrays with smallest and largest values relative to pivot
     */
    @SuppressWarnings("unchecked")
    private int partition(Comparator<? super T> c, int low, int high){
        int middle = low + (high - low) / 2;
        T pivot = (T) array[middle];
        T temp = (T) array[middle];
        array[middle] = array[high];
        array[high] = temp;

        int i = low - 1;
        for(int j = low; j < high; j++){
            temp = (T) array[j];
            if(c.compare(temp,pivot) < 0){
                i++;
                array[j] = array[i];
                array[i] = temp;
            }
        }

        temp = (T) array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;

        return i + 1;
    }

    /**
     * Check if the index is included in the array size.
     * @param index - the index
     */
    private void rangeCheck(int index){
        if(index < 0 || index > size){
            throw new IndexOutOfBoundsException(index);
        }
    }

    /**
     *Returns an array with an increased capacity of the minimum allowable or 30% more than the original one.
     *Checks the new capacity to ensure it is less than Integer.MAX_VALUE and greater than zero.
     * @param minCapacity - minimum allowed array capacity
     * @return increased array
     */
    private Object[] increaseArray(int minCapacity) {
        Object[] newArray;
        int newCapacity = Math.max((int) (array.length * 1.3), minCapacity);
        if(newCapacity > 0 && newCapacity < Integer.MAX_VALUE - 8){
            newArray = new Object[newCapacity];
            System.arraycopy(array,0,newArray,0,array.length);
            return newArray;
        } else {
            throw new OutOfMemoryError("Required array length " + newCapacity + " is too large");
        }
    }

    public String toString(){
        StringBuilder builder = new StringBuilder("Custom ArrayList:\n[");
        for(int i = 0; i < size; i++){
            builder.append(array[i]).append(i != size-1 ? ", " : "]");
        }
        return builder.toString();
    }
}
