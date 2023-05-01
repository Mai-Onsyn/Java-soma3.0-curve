package ParticleMIDI;

public class MathGeometry {
    //求两点间直线表达式
    public static double[] Line (double[] P1, double[] P2) {
        double k = (P2[0] - P1[0]) / (P2[2] - P1[2]);
        double b = P1[0] - k * P1[2];
        //y = kx + b
        return new double[] {k,b};
    }
    //求中垂线表达式
    public static double[] MidLine (double[] P1, double[] P2) {
        double k = -(P2[2] - P1[2]) / (P2[0] - P1[0]);
        double b = (P2[2] - P1[2]) / (P2[0] - P1[0]) * (P1[2] + P2[2]) / 2 + (P1[0] + P2[0]) / 2;
        // y = kx + b
        return new double[] {k,b};
    }

    //求两直线交点坐标
    public static double[] Intersection (double[] line1, double[] line2, double Y) {
        double x = (line2[1] - line1[1]) / (line1[0] - line2[0]);
        double y = line1[0] * x + line1[1];
        return new double[] {y, Y, x};
    }
    //求两点中点坐标
    public static double[] MidPoint (double[] P1, double[] P2) {
        //(x,y,z)
        return new double[] {
                (P1[0] + P2[0]) / 2,
                P1[1],
                (P1[2] + P2[2]) / 2
        };
    }
    //求两点距离
    public static double Distance (double[] P1, double[] P2) {
        /*
        D(x,y) = (∑(i=1,n) (xi + yi)^2)^0.5
         */
        return Math.sqrt(Math.pow(P2[0] - P1[0], 2) + Math.pow(P2[1] - P1[1], 2) + Math.pow(P2[2] - P1[2], 2));
    }
    //向量夹角余弦公式求夹角
    public static double VectorAngle (double[] P1, double[] center, double[] P2) {
        /*
        v1 = P1[0,1,2] - center[0,1,2]
        v2 = P2[0,1,2] - center[0,1,2]
        cosTheta = (v1 · v2) / |v1|*|v2|
         */
        double QP = (P2[2] - center[2]) * (P1[2] - center[2]) + (P2[1] - center[1]) * (P1[1] - center[1]) + (P2[0] - center[0]) * (P1[0] - center[0]);//数量积
        double MP = Math.sqrt(
                (P2[2] - center[2]) * (P2[2] - center[2]) + (P2[1] - center[1]) * (P2[1] - center[1]) + (P2[0] - center[0]) * (P2[0] - center[0]))
                * Math.sqrt((P1[2] - center[2]) * (P1[2] - center[2]) + (P1[1] - center[1]) * (P1[1] - center[1]) + (P1[0] - center[0]) * (P1[0] - center[0]));//模量积
        return Math.toDegrees(Math.acos(QP / MP));
    }
}