public class OffByN implements CharacterComparator{
    int n;
    public OffByN(int N) {
        this.n = N;
    }
    @Override
    public boolean equalChars(char x, char y){
        if (x-y==this.n || y-x==this.n) {
            return true;
        } else {
            return false;
        }
    }
}
