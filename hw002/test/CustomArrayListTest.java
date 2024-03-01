import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test of CustomArrayList.class")
class CustomArrayListTest {
    private final int UPPER_BORDER_VALUE = 200;
    private final int LOW_BORDER_VALUE = 10;
    private CustomArrayList<Integer> list;

    @BeforeEach
    public void setUp(){
        list = randomCustomListGenerator();
    }

    /**
     * Testing list size during initialization in various ways.
     */
    @Test
    @DisplayName("Checking list size during initialization")
    void size() {
        list = new CustomArrayList<>();
        assertEquals(0, list.size(), "List size is not as expected");

        int capacity = (int) (10 + Math.random() * 100);
        list = new CustomArrayList<>(capacity);
        assertEquals(0, list.size(), "List size is not as expected");

        List<Integer> standardList = randomListGenerator();
        list = new CustomArrayList<>(standardList);
        assertEquals(standardList.size(), list.size(), "List size is not as expected");
    }

    /**
     * Checking the addition of elements to the list in comparison with the standard ArrayList.
     * Elements must be added to the end of the list in the specified order. We also simultaneously
     * check the mechanism for automatically expanding the array inside the list.
     */
    @Test
    @DisplayName("Adding Elements to a List")
    void addByElement() {
        int elementAmount = 10;
        int initSize = list.size();
        int[] elements = new int[elementAmount];
        for(int i = 0; i < elementAmount; i++){
            int element = (int) (Math.random() * 200);
            list.add(element);
            elements[i] = element;
        }
        boolean flag = true;
        for(int i = 0; i < elementAmount; i++){
            if(elements[i] != list.get(initSize + i)){
                flag = false;
            }
            assertTrue(flag, "The order or meaning of the added elements is incorrect." +
                                    "\nExpected: " + elements[i] + ", actual: " + list.get(i));
        }
        assertEquals(initSize + elementAmount, list.size(),
                "List size does not match the sum of the initial and added elements");

        list.add(null);
        assertNull(list.get(list.size() - 1), "Failed to add null as list element");
    }

    /**
     * Checking the addition of elements to the list in comparison with the standard ArrayList.
     * Elements must be added at the specified index, shifting elements higher to the right in the list.
     */
    @Test
    @DisplayName("Adding Element to list by index")
    void addByIndexAndElement() {
        int elementAmount = 10;
        List<Integer> standardList = randomListGenerator();
        list = new CustomArrayList<>(standardList);
        int initSize = list.size();

        for(int i = 0; i < elementAmount; i++){
            int index = (int) (Math.random() * (initSize-1));
            int element = (int) (Math.random() * 200);
            list.add(index, element);
            standardList.add(index,element);
        }
        boolean flag = true;
        for(int i = 0; i < standardList.size(); i++){
            if(!Objects.equals(list.get(i), standardList.get(i))){
                flag = false;
            }
            assertTrue(flag, "The order or meaning of the added elements is incorrect." +
                                    "\nExpected: " + standardList.get(i) + ", actual: " + list.get(i));
        }
        assertEquals(initSize + elementAmount, list.size(),
                "List size does not match the sum of the initial and added elements");

        int index = list.size()/2;
        list.add(index, null);
        assertNull(list.get(index), "Failed to add null at index as list element");
    }

    /**
     * Checking that multiple other lists have been added to a list correctly. Adding lists
     * should occur at the end with the size of the list increasing by the size of the added.
     */
    @Test
    @DisplayName("Adding collections to list")
    void addAll() {
        int subListAmount = 5;
        List<Integer> standardList = randomListGenerator();
        list = new CustomArrayList<>(standardList);
        int size = list.size();
        for(int i = 0; i < subListAmount; i++){
            List<Integer> subList = randomListGenerator(3,15);
            standardList.addAll(subList);
            list.addAll(subList);
            size += subList.size();
        }

        boolean flag = true;
        for(int i = 0; i < standardList.size(); i++){
            if(!Objects.equals(list.get(i), standardList.get(i))){
                flag = false;
            }
            assertTrue(flag, "The order or meaning of the added elements is incorrect." +
                                    "\nExpected: " + standardList.get(i) + ", actual: " + list.get(i));
        }
        assertEquals(size, list.size(), "List size does not match the sum of the initial and added elements");
    }


    /**
     * Checking how works clearing the list and returning a flag indicating that the list is empty.
     */
    @Test
    @DisplayName("Cleaning list")
    void clear() {
        list = randomCustomListGenerator();
        list.clear();
        assertEquals(0, list.size(), "List is not cleared");
        assertTrue(list.isEmpty(), "List is not cleared or the isEmpty() method does not work correctly");

        list = new CustomArrayList<>(randomListGenerator());
        list.clear();
        assertEquals(0, list.size(), "List is not cleared");
        assertTrue(list.isEmpty(), "List is not cleared or the isEmpty() method does not work correctly");

    }

    /**
     * We check the removal of an element from the list. If the specified element is present in the list,
     * then it is deleted with the top elements shifted by one to the left. If the element does not exist,
     * then false is returned. Checking the removal of non-existing elements. Also check the removal of
     * "null" in both cases.
     */
    @Test
    @DisplayName("Removing element from a list how object")
    void removeByElement() {
        int size = list.size();
        for(int i = 0; i < size/2; i++){
            size--;
            int index = (int) (Math.random() * (list.size()-1));
            Integer element = list.get(index);
            assertTrue(list.remove(element), "Removal failed, element \""
                                                    + element + "\" not found in the list.\n"
                                                    + list.toString());
        }
        assertEquals(size, list.size(), "List size does not match the sum of the initial and added elements");

        list.add(list.size()/2, null);
        assertTrue(list.remove(null), "Removal failed, element \"null\" not found in the list.\n"
                + list.toString());

        list = new CustomArrayList<>(randomListGenerator());
        List<Integer> nonExistentElements = new ArrayList<>(Arrays.asList(-1, 2, 6, 230, 500, 100000, 0));
        for (Integer element : nonExistentElements) {
            assertFalse(list.remove(element),
                    "List was able to remove non-existent element \""
                            + element + "\"\n"
                            + list.toString());
        }
    }


    /**
     * Checking the removal of an element from the list by index. When deleting, the top elements are
     * shifted by one to the left. If the element does not exist, then null is returned. Check the removal
     * of non-existent elements. Also check the removal of null in both cases.
     */
    @Test
    @DisplayName("Removing element from a list by index")
    void removeByIndex() {
        list = new CustomArrayList<>(randomListGenerator(20));
        int size = list.size();
        for(int i = 0; i < list.size()/2; i++){
            size--;
            int index = (int) (Math.random() * (list.size()-2));
            Integer element = list.get(index);
            assertEquals(element, list.remove(index), "Removal failed, element \"" + element + "\" on index "
                    + index + " not found in the list.\n"
                    + list.toString());
        }
        assertEquals(size, list.size(), "List size does not match the sum of the initial and added elements");


        list = new CustomArrayList<>(randomListGenerator(30));
        int[] nonExistentIndexes = new int[]{-1, 31, 230, 500, 100000};
        for(int i = 0; i < nonExistentIndexes.length; i++){
            int finalI = i;
            assertThrows(IndexOutOfBoundsException.class,
                    () -> list.remove(nonExistentIndexes[finalI]),
                    "The method could not handle the index out of bounds exception. Index " + finalI + "\n"
                    + list.toString());
        }
    }

    /**
     * Checking the quick sort algorithm in comparison with the sorting of ArrayList.class
     * in variations with natural and reverse orders
     */
    @Test
    @DisplayName("Testing quick list sort")
    void sort() {
        List<Integer> standardList = randomListGenerator();
        list = new CustomArrayList<>(standardList);
        standardList.sort(Comparator.naturalOrder());
        list.sort(Comparator.naturalOrder());
        boolean flag = true;
        for(int i = 0; i < standardList.size(); i++){
            if(!Objects.equals(standardList.get(i), list.get(i))){
                flag = false;
            }
            assertTrue(flag, "");
        }

        standardList = randomListGenerator();
        list = new CustomArrayList<>(standardList);
        standardList.sort(Comparator.reverseOrder());
        list.sort(Comparator.reverseOrder());
        flag = true;
        for(int i = 0; i < standardList.size(); i++){
            if(!Objects.equals(standardList.get(i), list.get(i))){
                flag = false;
            }
            assertTrue(flag, "");
        }

    }

    private List<Integer> randomListGenerator(){
        int capacity = (int) (10 + Math.random() * 100);
        return listGenerator(capacity);
    }

    private List<Integer> randomListGenerator(int capacity){
        return listGenerator(capacity);
    }

    private List<Integer> randomListGenerator(int lowBorderCapacity, int upperBorderCapacity){
        int capacity = (int) (lowBorderCapacity + Math.random() * upperBorderCapacity);
        return listGenerator(capacity);
    }

    private List<Integer> listGenerator(int capacity){
        List<Integer> result = new ArrayList<>(capacity);
        for(int i = 0; i < capacity; i++){
            result.add((int)(LOW_BORDER_VALUE + Math.random() * UPPER_BORDER_VALUE));
        }
        return result;
    }

    private CustomArrayList<Integer> randomCustomListGenerator(){
        int capacity = (int) (10 + Math.random() * 100);
        CustomArrayList<Integer> result = new CustomArrayList<>(capacity);
        for(int i = 0; i < capacity; i++){
            result.add((int)(LOW_BORDER_VALUE + Math.random() * UPPER_BORDER_VALUE));
        }
        return result;
    }
}