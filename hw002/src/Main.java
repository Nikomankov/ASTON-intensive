
public class Main {
    public static void main(String[] args) {

    }


    private static void printer(int[] array){
        for(int i = 0; i < array.length; i++){
            System.out.print(array[i] + ",");
        }
        System.out.println();
    }
    private static void printer(CustomArrayList array){
        for(int i = 0; i < array.size(); i++){
            System.out.print(array.get(i) + ",");
        }
        System.out.println();
    }
}