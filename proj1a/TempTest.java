public class TempTest {
    public static void main(String[] args) {
        double aaa = System.currentTimeMillis();

        /* Main code below there.*/
        ArrayDeque<Integer> AList = new ArrayDeque<>();
        AList.addLast(23);
        AList.addLast(43);
        AList.addLast(12);
        for (int i=0;i<1e8;i++) {
            AList.addLast(i);
        }
        for (int i=0;i<1e8;i++) {
            int a = AList.get(i);
        }
        for (int i=0;i<1e8;i++) {
            AList.removeLast();
        }
        AList.printDequeMod();
        System.out.println(AList.get(1));
        /* Main code above there.*/

        double bbb = System.currentTimeMillis();
        double runtime = (bbb-aaa)/1000;
        System.out.print("runtime(s):");
        System.out.println(runtime);
    }
}
