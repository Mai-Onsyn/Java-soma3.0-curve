package ParticleMIDI;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OtherMethod {
    //圆心不存在时用抛物线连接
    public static void ParabolaConnect (double[] P1, double[] P2, double n, double lambda, FileWriter FW) throws IOException {
        double deltaT = Math.abs(P1[0] - P2[0]);
        double[] vP = {
                (P2[0] - P1[0]) / deltaT,
                0,
                (P2[2] - P1[2]) / deltaT,
        };
        double s = Math.pow(Math.pow(P1[0] - P2[0], 2) + Math.pow(P1[2] -P2[2], 2), 0.5);
        double yMax = s * n;
        double yMin = Math.min(P1[1], P2[1]);
        for (double t = 0 ; t < deltaT ; t++) {
            for (double i = 0 ; i < lambda ; i++) {
                double deltaL = (t + i / lambda);
                double[] Pi = {
                        P1[0] + vP[0] * deltaL + 0.5,
                        yMin + ((4 * yMax) / deltaT) * deltaL - ((4 * yMax) / (deltaT * deltaT)) * deltaL * deltaL +0.5,
                        P1[2] + vP[2] * deltaL + 0.5
                };
                String command = String.format(("execute at @a[scores={Tick=" + Math.round(P1[0] + vP[0] * deltaL) + "}] run particle end_rod %.10f %.10f %.10f 0 0 0 0 1 force"), Pi[0], Pi[1], Pi[2]);
                FW.write(command + "\n");
            }
        }
    }
    public static void WayController (double[] P1, double[] ThisOP, double[] LastOP) {
        if (((LastOP[2] > P1[2]) && (ThisOP[2] > P1[2])) || ((LastOP[2] < P1[2]) && (ThisOP[2] < P1[2]))) {
            MainFunction.way = !MainFunction.way;
        }
    }
    public static void mkDirectory(String PathStr) {
        File Path;
        try {
            Path = new File(PathStr);
            if (!Path.exists()) {
                Path.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void deleteFolder(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File f : files) {
                    deleteFolder(f);
                }
            }
        }
        folder.delete();
    }
}