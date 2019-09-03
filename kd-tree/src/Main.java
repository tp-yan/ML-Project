import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static int k = 3;      // 样本维度
    public static int NUM = 11;   // 样本数量

    public static ArrayList<Instance> initData() {
        ArrayList<Instance> instances = new ArrayList<>();
        for (int i = 0; i < NUM; i++) {
            double[] data = new double[k];
            for (int j = 0; j < k; j++) {
                data[j] = (int) (10 * Math.random());
            }
            Instance instance = new Instance(k, data.clone());
            instances.add(instance);
        }

        /*
        // P54上的测试数据
        instances.clear();
        double[] tmp = {2, 3};
        instances.add(new Instance(2, tmp.clone()));
        tmp[0] = 5;
        tmp[1] = 4;
        instances.add(new Instance(2, tmp.clone()));
        tmp[0] = 9;
        tmp[1] = 6;
        instances.add(new Instance(2, tmp.clone()));
        tmp[0] = 4;
        tmp[1] = 7;
        instances.add(new Instance(2, tmp.clone()));
        tmp[0] = 8;
        tmp[1] = 1;
        instances.add(new Instance(2, tmp.clone()));
        tmp[0] = 7;
        tmp[1] = 2;
        instances.add(new Instance(2, tmp.clone()));
*/
        return instances;
    }

    public static Node buildKDTree(ArrayList<Instance> instances, int curDepth, Node parent) {
        if (instances == null || instances.size() == 0) {
            return null;
        }

        int l = (curDepth % k) + 1; // 当前需要划分的第l个属性
        Node node = new Node();
        node.depth = curDepth;
        node.parent = parent;

        if (instances.size() == 1) {
            node.instance = instances.get(0);
            node.lchild = null;
            node.rchild = null;
            return node;
        }

        instances.sort((o1, o2) -> {
            if (o1.data[l - 1] == o2.data[l - 1])
                return 0;
            return o1.data[l - 1] > o2.data[l - 1] ? 1 : -1;
        }); // 升序
        int m = instances.size() / 2; // 排序后取中位数

        node.instance = instances.get(m);
        System.out.println("middle node:");
        System.out.println(node.instance);

        ArrayList<Instance> leftInstance = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            leftInstance.add(instances.get(i));
        }
        System.out.println("leftInstance:");
        System.out.println(leftInstance);

        Node lchild = buildKDTree(leftInstance, curDepth + 1, node);
        node.lchild = lchild;

        ArrayList<Instance> rightInstance = new ArrayList<>();
        for (int i = m + 1; i < instances.size(); i++) {
            rightInstance.add(instances.get(i));
        }
        System.out.println("rightInstance:");
        System.out.println(rightInstance);

        Node rchild = buildKDTree(rightInstance, curDepth + 1, node);
        node.rchild = rchild;

        return node;
    }


    /**
     * 从当前 kd-tree中找到离目标点最近的点，即最近邻搜索
     *
     * @param root   kd-tree 根结点
     * @param target 目标点
     * @return 离目标点最近的点
     */
    public static Node searchKdTree(Node root, Node target) {
        // 第一步：找到离目标点所在区域的叶子结点，作为当前最近点
        Node curNode = root;
        // 直到找到叶子结点
        while (curNode.lchild != null || curNode.rchild != null) {
            int l = curNode.depth % k + 1;
            // 分到左子树
            if (target.instance.data[l - 1] < curNode.instance.data[l - 1]) {
                if (null == curNode.lchild) { // 如果当前非叶子结点没有左子树，则将target分到右子树
                    curNode = curNode.rchild;
                } else {
                    curNode = curNode.lchild;
                }
            } else {
                if (curNode.rchild == null) // 同上
                    curNode = curNode.lchild;
                else
                    curNode = curNode.rchild;
            }
        }
        curNode.visited = true;

        Node curNearestNode = curNode;

        // 第二步：回退
        while (curNode.parent != null && !curNode.parent.visited) {
            Node parentNode = curNode.parent;
            parentNode.visited = true;
            double dis = Utils.computeDistance(parentNode, target);
            if (dis < Utils.computeDistance(curNearestNode, target)) {
                curNearestNode = parentNode;
            }

            // 第三步：节点另一子结点是否有相交区域
            int l = parentNode.depth % k + 1;   // 当前节点的划分属性
            double borderDis = Math.abs(parentNode.instance.data[l - 1] - target.instance.data[l - 1]); // target距分界边的距离
            if (borderDis <= Utils.computeDistance(curNearestNode, target)) { // 与另一子区域相交
                if (curNode.equals(parentNode.lchild)) {
                    if (parentNode.rchild != null) {    // 父节点存在右节点
                        Node subTreeNearest = searchKdTree(parentNode.rchild, target);
                        if (subTreeNearest != null &&
                                Utils.computeDistance(subTreeNearest, target) < Utils.computeDistance(curNearestNode, target)) {
                            curNearestNode = subTreeNearest;
                        }
                    }
                } else {
                    if (parentNode.lchild != null) {    // 父节点存在左节点
                        Node subTreeNearest = searchKdTree(parentNode.lchild, target);
                        if (subTreeNearest != null &&
                                Utils.computeDistance(subTreeNearest, target) < Utils.computeDistance(curNearestNode, target)) {
                            curNearestNode = subTreeNearest;
                        }
                    }
                }
            }
            curNode = parentNode;
        }

        return curNearestNode;
    }


    public static void main(String[] args) {
        ArrayList<Instance> data = initData();
        System.out.println(data);
        Node root = buildKDTree(data, 0, null);
        /*
        System.out.println(root.instance);
        System.out.println(root.lchild.instance);
        System.out.println(root.lchild.lchild.instance);
        System.out.println(root.rchild.instance);
        System.out.println(root.rchild.lchild.instance);
        System.out.println(root.rchild.rchild);
        */
        double[] tmp = {4, 4.5, 1}; // 测试目标点
        Node target = new Node();
        target.instance = new Instance(k, tmp);
        Node nearestNode = searchKdTree(root, target);
        System.out.println("nearestNode:");
        System.out.println(nearestNode.instance);
    }
}
