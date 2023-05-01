package ParticleMIDI;

import java.io.FileWriter;
import java.io.IOException;

public class Arc {
    //输出弧坐标
    public static void PointComputer (double[] OPoint, double[] P1, double Angle, double radius, double deltaT, double totalT, int i, FileWriter FW) throws IOException {
        double[] ArcPoint = {
                OPoint[0] + Math.sin(Math.toRadians(Angle)) * radius,
                OPoint[1],
                OPoint[2] + Math.cos(Math.toRadians(Angle)) * radius
        };
        String command = String.format(("execute at @a[scores={Tick=" + Math.round((P1[0] + deltaT * (i / totalT))) + "}] run particle end_rod %.10f %.10f %.10f 0 0 0 0 1 force"), ArcPoint[0] + 0.5, ArcPoint[1] + 0.5, ArcPoint[2] + 0.5);
        FW.write(command + "\n");
    }
    public static void Creator (double[] OPoint, double[] P1, double[] P2, boolean way, double lambda, FileWriter FW) throws IOException {
        double x1 = P1[0] - OPoint[0];
        double z1 = P1[2] - OPoint[2];
        double radius = Math.sqrt(x1*x1 + z1*z1);
        lambda /= radius;
        double DegreeStart;
        double DegreeEnd;
        DegreeStart = MathGeometry.VectorAngle(P1, OPoint, new double[] {OPoint[0], OPoint[1], OPoint[2] + 10});
        DegreeEnd = MathGeometry.VectorAngle(P2, OPoint, new double[] {OPoint[0], OPoint[1], OPoint[2] + 10});
        if (P1[0] < OPoint[0]) {
            DegreeStart = 360 - DegreeStart;
        }
        if (P2[0] < OPoint[0]) {
            DegreeEnd = 360 - DegreeEnd;
        }

        if (DegreeStart > DegreeEnd) {
            DegreeStart -= 360;
        }
        int i = 0;
        if (way) {
            double deltaT = Math.abs(P2[0] - P1[0]);
            double totalT = (DegreeEnd - DegreeStart) / lambda;
            for (double Angle = DegreeStart; Angle < DegreeEnd ; Angle += lambda) {
                PointComputer(OPoint, P1, Angle, radius, deltaT, totalT, i, FW);
                i++;
            }
        }
        else {
            double deltaT = Math.abs(P2[0] - P1[0]);
            double totalT = (DegreeStart+360 - DegreeEnd) / lambda;
            for (double Angle = DegreeStart + 360; Angle > DegreeEnd ; Angle -= lambda) {
                PointComputer(OPoint, P1, Angle, radius, deltaT, totalT, i, FW);
                i++;
            }
        }
    }
}