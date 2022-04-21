public class SpeedDLList {
    public static void main(String[] args) {
        LinkedListDeque<Integer> l = new LinkedListDeque<>();
        int i = 0;
        while (i<10000000) {
            l.addLast(i);
            i = i+1;
        }
    }
}
