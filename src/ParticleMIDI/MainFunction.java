package ParticleMIDI;

import javax.sound.midi.InvalidMidiDataException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainFunction {
    private static boolean[] ThisWays;
    private static boolean[] LastWays;
    public static boolean way = false;
    private static int Groups = 0;
    private static double[][] ThisArrayOPoint;
    private static double[][] LastArrayOPoint = {{-5,5,-3}};
    public static final String FunctionPath = "E:\\.On_craft\\#Minecrafts\\#HMCL\\.minecraft\\saves\\particle\\datapacks\\functions\\data\\minecraft\\functions\\";//数据包文件目录下的functions路径
    public static FileWriter FW;
    private static FileWriter CarrySomaFW;
    //计算连接所需要的圆心
    private static void Connect (double[] PointA, double[] PointB, int i, int j) throws IOException {
        double[] MidLine = MathGeometry.MidLine(PointA, PointB);
        double[] Line = MathGeometry.Line(LastArrayOPoint[i], PointA);
        double[] OPoint = MathGeometry.Intersection(MidLine, Line, PointA[1]);
        double radius = Math.sqrt((PointA[0] - OPoint[0])*(PointA[0] - OPoint[0]) + (PointA[2] - OPoint[2])*(PointA[2] - OPoint[2]));
        //如果出现平行情况或圆半径过大则用抛物线 反之用弧进行连接
        if (Double.isNaN(OPoint[0]) || radius > 1145) {
            OPoint = MathGeometry.MidPoint(PointA, PointB);
            OtherMethod.ParabolaConnect(PointA, PointB, 0.35, 24, FW);
        }
        else {
            way = !LastWays[i];
            OtherMethod.WayController(PointA, OPoint, LastArrayOPoint[i]);
            Arc.Creator(OPoint, PointA, PointB, way, 10, FW);
        }
        ThisWays[j] = way;
        ThisArrayOPoint[j] = OPoint;
    }
    //确定坐标组中的哪些点要相连
    private static void Group (double[][] PL1, double[][] PL2) throws IOException {
        ThisArrayOPoint = new double[PL2.length][];
        ThisWays = new boolean[PL2.length];
        FW = new FileWriter(FunctionPath + "soma_lines\\soma" + Groups + ".mcfunction", true);
        String CarryCommand = "execute as @a[scores={Tick=" + (int) PL1[0][0] + ".." + (int) PL2[0][0] + "}] run function soma_lines/soma" + Groups + "\n";
        CarrySomaFW.write(CarryCommand);
        if (PL1.length < PL2.length) {
            int m = PL2.length / PL1.length;
            int n = PL2.length % PL1.length;
            for (int i = 0; i < n; i++) {
                for (int j = i * (m + 1); j < (i + 1) * (m + 1); j++)  {
                    Connect(PL1[i], PL2[j], i, j);
                }
            }
            for (int i = n;  i < PL1.length; i++) {
                for (int j = n * (m + 1) + (i - n) * m; j < n * (m + 1) + (i - n + 1) * m; j++) {
                    Connect(PL1[i], PL2[j], i, j);
                }
            }
        }
        else if (PL1.length > PL2.length) {
            double m = (double) PL1.length / PL2.length;
            for (int i = 0; i < PL2.length; i++) {
                int n = (int) Math.round(i * m + m / 2);
                Connect(PL1[n], PL2[i], n, i);
            }
        }
        else {
            for (int i = 0; i < PL1.length; i++) {
                Connect(PL1[i], PL2[i], i, i);
            }
        }
        LastArrayOPoint = ThisArrayOPoint;
        LastWays = ThisWays;
        Groups++;
        //way = !way;
        FW.close();
    }
    //拆分坐标组 分离出同坐标的点PL1和PL2进行下一步
    private static void list (double[][] ArrayPoint) throws IOException {
        File Temp = new File("D:\\Users\\Desktop\\Files\\cache\\PointCache.txt");
        FileWriter initTmp = new FileWriter(Temp);
        initTmp.write("");
        initTmp.close();
        FileWriter tmp = new FileWriter(Temp);
        for (int i = 0; i <= ArrayPoint.length-2; i++) {
            if (ArrayPoint[i][0] != ArrayPoint[i+1][0]) {
                tmp.write(String.format(("%.0f,%.0f,%.0f\n"), ArrayPoint[i][0], ArrayPoint[i][1], ArrayPoint[i][2]));
            }
            else {
                tmp.write(String.format(("%.0f,%.0f,%.0f;"), ArrayPoint[i][0], ArrayPoint[i][1], ArrayPoint[i][2]));
            }
        }
        tmp.close();
        Scanner scanner = new Scanner(Temp);
        List<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }
        double[][][] ArrayPointList = new double[lines.size()][][];
        for(int i=0;i<lines.size();i++){
            String line = lines.get(i);
            String[] split = line.split(";");
            ArrayPointList[i] = new double[split.length][];
            for (int j=0;j<split.length;j++) {
                String[] point = split[j].split(",");
                double x = Double.parseDouble(point[0]);
                double y = Double.parseDouble(point[1]);
                double z = Double.parseDouble(point[2]);
                ArrayPointList[i][j] = new double[] {x, y, z};
            }
        }
        for (int i = 0; i < ArrayPointList.length - 1; i++) {
            if (ArrayPointList[i][0][0] <= ArrayPointList[i + 1][0][0]) {
                System.out.println("Group: " + i + "~" + (i+1));
                Group(ArrayPointList[i], ArrayPointList[i + 1]);
            }
        }
    }

    public static void main(String[] args) throws InvalidMidiDataException, IOException {
        double[] StartPoint = {0,5,0};
        String MIDIPath = "D:\\Users\\Desktop\\MIDITmp\\ushio.mid";//输入midi文件路径
        double[][] ArrayPoints = MIDIPoint.MIDIArrayPoint(StartPoint, 1, MIDIPath);
        OtherMethod.deleteFolder(new File(FunctionPath + "soma_lines"));
        OtherMethod.mkDirectory(FunctionPath + "soma_lines");
        CarrySomaFW = new FileWriter(FunctionPath + "carrysoma.mcfunction",false);
        CarrySomaFW.write("");
        CarrySomaFW = new FileWriter(FunctionPath + "carrysoma.mcfunction",true);
        LastWays = new boolean[] {way};
        //CubesTrack.Tracks(ArrayPoints, 0.52, 5.2);
        list(ArrayPoints);
        CarrySomaFW.close();
    }
}