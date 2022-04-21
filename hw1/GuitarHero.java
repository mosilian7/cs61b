import synthesizer.GuitarString;
import java.util.HashMap;
public class GuitarHero {
    private static final double CONCERT_A = 440.0;
    private static final double CONCERT_C = CONCERT_A * Math.pow(2, 3.0 / 12.0);
    private static final char[] KEYS = new char[]{'z','s','x','d','c','v','g','b','h','n','j','m','q','2','w','3'
            ,'e','r','5','t','6','y','7','u','i','9','o','0','p','[','=',']',',','l','.',';','/'};

    private static HashMap<Character,synthesizer.GuitarString> genKeyToString() {
        HashMap<Character,synthesizer.GuitarString> KeyToString = new HashMap<>();
        double zF = CONCERT_C/8;
        for (int i=0;i<KEYS.length-5;i+=1) {
            double Frequency = zF * Math.pow(2, i / 12.0);
            synthesizer.GuitarString S = new synthesizer.GuitarString(Frequency);
            KeyToString.put(KEYS[i],S);
        }

        for (int i=KEYS.length-5;i<KEYS.length;i+=1) {
            double Frequency = zF*2 * Math.pow(2, (5+i-KEYS.length) / 12.0);
            synthesizer.GuitarString S = new synthesizer.GuitarString(Frequency);
            KeyToString.put(KEYS[i],S);
        }

        return KeyToString;
    }


    public static void main(String[] args) {
        /* Variables. */
        double backgroundNoise = 0.01;
        double ticNoise = 0.0;
        double sampleNoise = 0.05;
        int AVGFilterSampleNum = 4;
        int linearFilterSampleNum = 8;

        HashMap<Character,synthesizer.GuitarString> KTS = genKeyToString();

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (KTS.keySet().contains(key)) {
                    synthesizer.GuitarString S = KTS.get(key);
                    S.pluck("uniform");
                }
            }

            /* compute the superposition of samples */
            double sample = 0.0;
            for (synthesizer.GuitarString S:KTS.values()) {
                sample += S.sample(sampleNoise,linearFilterSampleNum);
            }

            /* play the sample on standard audio */
            StdAudio.play(sample+GuitarString.distortion(backgroundNoise));

            /* advance the simulation of each guitar string by one step */
            for (synthesizer.GuitarString S:KTS.values()) {
                S.tic(AVGFilterSampleNum,ticNoise);
            }
        }
    }
}
