package synthesizer;
import java.util.Random;
//Make sure this class is public
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final means
     * the values cannot be changed at runtime. We'll discuss this and other topics
     * in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .995; // energy decay factor

    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        int capacity = (int) Math.round(SR/frequency);
        buffer = new ArrayRingBuffer<>(capacity);
        for (int i=0;i<buffer.capacity();i+=1) {
            buffer.enqueue(0.0);
        }
    }

    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck(String mode) {
        double r;
        Random rand = new Random();
        for (int i=0;i<buffer.capacity();i+=1) {
            buffer.dequeue();
            if (mode == "uniform") {
                r = Math.random() - 0.5;
            } else if (mode == "Gaussian"){
                r = rand.nextGaussian() * 0.5;
            } else {
                r = 0.0;
            }
            buffer.enqueue(r);
        }
    }

    /* Pluck the guitar string by replacing the buffer with periodic linear function. */
    public void pluck(int interval) {
        double r;
        for (int i=0;i<buffer.capacity();i+=1) {
            buffer.dequeue();
            r = (double) (i%interval)/(double) interval;
            buffer.enqueue(r);
        }
    }

    /* Pluck the guitar string by replacing the buffer with periodic pulse. */
    public void pluckPulse(int interval) {
        double r;
        for (int i=0;i<buffer.capacity();i+=1) {
            buffer.dequeue();
            if (i%interval==0) {
                r = 0.9;
            } else {
                r = 0.0;
            }

            buffer.enqueue(r);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm. 
     */
    public void tic() {
        double pop = buffer.dequeue();
        double update = (pop + buffer.peek())*0.5*DECAY;
        buffer.enqueue(update);
    }

    /* Tic the string using a more complicated filter, and with distortion.
     * The complicated filter take the average of the first n items in the ArrayRingBuffer. */
    public void tic(int filterSampleNum,double distortion) {
        double update = buffer.dequeue();
        for (int i=0; i<filterSampleNum-1; i+=1) {
            update += ((ArrayRingBuffer<Double>) buffer).peek(i);
        }
        update = update*DECAY/filterSampleNum;
        double r = Math.random() - 0.5;
        update = update*(1 + r*distortion);
        buffer.enqueue(update);
    }

    /* A filter for sampling. */
    public double linearFilter(double sample,int filterSampleNum) {
        double weight = 1.0;
        for (int i=0; i<filterSampleNum-1; i += 1) {
            weight -= 1/(double) filterSampleNum;
            sample += ((ArrayRingBuffer<Double>) buffer).peek(i) * weight;
        }
        return 2*sample/(filterSampleNum+1);
    }

    /* Tic the string with distortion. */
    public void tic(double distortion) {
        double r = Math.random() - 0.5;
        double pop = buffer.dequeue();
        double update = (pop + buffer.peek())*0.5*DECAY*(1 + r*distortion);
        buffer.enqueue(update);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.peek();
    }

    /* Sample with distortion. */
    public double sample(double distortion) {
        double r = Math.random() - 0.5;
        return buffer.peek()*(1+r*distortion);
    }

    /* Sample with distortion and linearFilter. */
    public double sample(double distortion,int linearFilterSampleNum) {
        double r = Math.random() - 0.5;
        double out = linearFilter(buffer.peek(),linearFilterSampleNum);
        return out*(1+r*distortion);
    }

    /* Create a distortion. */
    public static double distortion(double d) {
        double r = Math.random() - 0.5;
        return r*d;
    }
}
