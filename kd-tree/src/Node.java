/**
 * 节点类
 */
public class Node {
    Node parent,lchild,rchild; // 父节点、左右子节点
    Instance instance;  // 样本实例
    int depth;   // 当前节点深度，根结点深度为 0
    boolean visited = false;
}
