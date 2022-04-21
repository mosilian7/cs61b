public class DLListSpeedTest {
    public static void main(String[] args) {
        double aaa = System.currentTimeMillis();
        LinkedListDeque<Integer> l = new LinkedListDeque<>();
        l.addLast(23);
        l.addLast(43);
        l.addLast(12);
        for (int i=0;i<1000000;i++) {
            l.addLast(i);
        }
        for (int i=0;i<1000000;i++) {
            l.removeFirst();
        }
        l.printDeque();
        double bbb = System.currentTimeMillis();
        double runtime = (bbb-aaa)/1000;
        System.out.print("runtime(s):");
        System.out.println(runtime);
    }

}
