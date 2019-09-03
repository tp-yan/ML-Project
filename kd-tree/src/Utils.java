public class Utils {
    /**
     * @param node1
     * @param node2
     * @return 两个样本点之间的欧氏距离的平方
     */
    public static double computeDistance(Node node1, Node node2) {
        Instance instance1 = node1.instance;
        Instance instance2 = node2.instance;
        double dis = 0;
        for (int i = 0; i < instance1.k; i++) {
            dis += Math.pow(instance1.data[i] - instance2.data[i], 2);
        }
        return Math.sqrt(dis);
    }
}
