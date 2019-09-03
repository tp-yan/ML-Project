import java.util.ArrayList;

public class MyTest {

    // 测试 List的sort的 Comparator 升降序
    public static void testSort() {
        int l = 1;
        int k = 3;
        ArrayList<Instance> instances = new ArrayList<>();
        double[] data = {3, 2, 1};
        instances.add(new Instance(k, data.clone()));
        data[1] = 0;
        instances.add(new Instance(k, data.clone()));
        data[1] = 3;
        instances.add(new Instance(k, data.clone()));
        System.out.println(instances);
        instances.sort((o1, o2) -> {
            if (o1.data[l - 1] == o2.data[l - 1])
                return 0;
            return o1.data[l - 1] > o2.data[l - 1] ? 1 : -1;
        });
        // 按升序
//        instances.sort((o1, o2) -> o2.data[l] - o1.data[l]); // 降序
        System.out.println(instances);
    }


    public static void main(String[] args) {

    }
}
