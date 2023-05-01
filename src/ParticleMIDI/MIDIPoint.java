package ParticleMIDI;

import
        javax.sound.midi.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MIDIPoint {
    public static final String cachePath = "D:\\Users\\Desktop\\Files\\cache\\PointCache.txt";

    public static double[][] MIDIArrayPoint(double[] StartPoint, int fold, String MIDIPath) throws InvalidMidiDataException, IOException {
        FileWriter cache = new FileWriter(cachePath, true);
        FileWriter initCache = new FileWriter(cachePath, false);
        initCache.write("");
        initCache.close();

        int lambda = 12 / fold;
        int NoteCount = 0;
        Sequence sequence = MidiSystem.getSequence(new File(MIDIPath));
        for (Track track : sequence.getTracks()) {
            //ArrayPoint = new double[track.size()][];
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                double distance = (double) (event.getTick() / lambda) + StartPoint[0];
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage sm) {
                    if (sm.getCommand() == 0x90) {
                        int key = sm.getData1();
                        cache.write(Math.round(distance) + "," + Math.round(StartPoint[1]) + "," + Math.round(key - 21) + "\n");
                        NoteCount++;
                        //System.out.println(Math.round(distance) + "," + Math.round(StartPoint[1]) + "," + Math.round(StartPoint[2] + key -21));
                    }
                }
            }
        }
        cache.close();
        double[][] PointList = new double[NoteCount + 1][];
        int i1 = 0;
        PointList[0] = StartPoint;
        try (Scanner cacheReader = new Scanner(new FileReader(cachePath))) {
            cacheReader.useDelimiter("\n");
            while (cacheReader.hasNext()) {
                i1++;
                String[] PointNStr = cacheReader.next().split(",");
                double[] PointN = new double[3];
                PointN[0] = Integer.parseInt(PointNStr[0]);
                PointN[1] = Integer.parseInt(PointNStr[1]);
                PointN[2] = Integer.parseInt(PointNStr[2]);
                PointList[i1] = PointN;
                //System.out.println(PointList[i1][0] + ", " + PointList[i1][1] + ", " + PointList[i1][2]);
            }
        }
        return PointList;
    }
}