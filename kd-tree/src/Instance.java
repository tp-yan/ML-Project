import java.util.ArrayList;

/**
 * 数据类，代表每个节点存放的数据
 */
public class Instance {
    int k;  // 向量维度
    double[] data;

    public Instance(int k, double[] data) {
        this.k = k;
        this.data = data;
    }

    public Instance() {
    }

    @Override
    public String toString() {
        String info="";
        for (double a:data)
            info += a +" ";

        return info;
    }
}
