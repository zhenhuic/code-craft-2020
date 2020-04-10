import java.io.*;
import java.util.*;

public class Main {

    private Map<String, List<String>> getTransfers(String filePath) {
        // 储存转账记录的结构
        Map<String, List<String>> transOut = new HashMap<>();
        Map<String, List<String>> transIn = new HashMap<>();

        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filePath);

            BufferedReader reader = new BufferedReader(fileReader);
            int lineNumber = 0;
            String record = null;
            while ((record = reader.readLine()) != null) {
                record = record.trim();
                if (!"".equals(record)) {
                    lineNumber++;
                    String[] splits = record.split(",");
                    String output = splits[0].strip();
                    String input = splits[1].strip();
                    if (transOut.get(output) == null) {
                        ArrayList<String> inputs = new ArrayList<>();
                        inputs.add(input);
                        transOut.put(output, inputs);
                    }else {
                        transOut.get(output).add(input);
                    }

                    if (transIn.get(input) == null) {
                        ArrayList<String> outputs = new ArrayList<>();
                        outputs.add(output);
                        transIn.put(input, outputs);
                    }else {
                        transIn.get(input).add(output);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        ArrayList<String> usersToDelete = new ArrayList<>();
        for (String user : transOut.keySet()) {
            if (transIn.get(user) == null) {
                usersToDelete.add(user);
            }
        }
        System.out.println("删除只出不进用户：" + usersToDelete.size());
        for (String user : usersToDelete) {
            transOut.remove(user);
        }

        usersToDelete.clear();
        for (String user : transIn.keySet()) {
            if (transOut.get(user) == null) {
                usersToDelete.add(user);
            }
        }
        System.out.println("删除只进不出用户：" + usersToDelete.size());
        for (String user : usersToDelete) {
            transIn.remove(user);
        }

        return transOut;
    }

    private void findCycle(String[] path, int stepNum, Map<String, List<String>> transfers, List<String[]> result) {
        if (stepNum > path.length) return;
        String output = path[stepNum - 1];
        List<String> inputs = transfers.get(output);
        if (inputs != null && !inputs.isEmpty()) {
            for (String in : inputs) {
                if (stepNum == 2 && in.equals(path[0])) {
                    break;
                }
                if (stepNum > 2 && in.equals(path[0])) {
                    String[] cyclePath = Arrays.copyOfRange(path, 0, stepNum);
                    boolean repeatedPath = false;
                    for (String[] res : result) {
                        if (res.length == cyclePath.length) {
                            boolean allHave = true;
                            for (String s : cyclePath) {
                                if (Arrays.stream(res).noneMatch(s::equals)) {
                                    allHave = false;
                                    break;
                                }
                            }
                            if (allHave) repeatedPath = true;
                        }
                    }
                    if (!repeatedPath) {
                        result.add(Arrays.copyOfRange(path, 0, stepNum));
                        break;
                    }
                } else {
                    boolean insideCycle = false;
                    for (int i = 1; i < stepNum; i++) {
                        if (in.equals(path[i])){
                            insideCycle = true;
                            break;
                        }
                    }
                    if (!insideCycle) {
                        if (stepNum < 7) {
                            path[stepNum] = in;
                            int newStepNum = stepNum + 1;
                            findCycle(path, newStepNum, transfers, result);
                        }
                    }
                }
            }
        }
    }

    public List<String[]> selectCycleTransfer(String filePath) {
        Map<String, List<String>> transfers = getTransfers(filePath);
        System.out.println("数据完成读取");

        // 查找循环转账记录
        Set<String> keys = transfers.keySet();
        String[] path = new String[7];
        List<String[]> result = new ArrayList<>();

        for (String output : keys) {
            path[0] = output;
            findCycle(path, 1, transfers, result);
        }

        result.sort((o1, o2) -> {
            if (o1.length < o2.length) {
                return -1;
            } else if (o1.length > o2.length) {
                return 1;
            } else {
                for (int i = 0; i < o1.length; i++) {
                    if (Integer.parseInt(o1[i]) < Integer.parseInt(o2[i])) {
                        return -1;
                    } else if (Integer.parseInt(o1[i]) > Integer.parseInt(o2[i])){
                        return 1;
                    }
                }
                return 0;
            }
        });
        return result;
    }


    public static void main(String[] args) {
        Main main = new Main();
        long start = System.currentTimeMillis();
        String file = Main.class.getResource("data/test_data.txt").getFile();
        List<String[]> result = main.selectCycleTransfer(file);

        System.out.println(result.size());
        for (String[] res : result) {
            System.out.println(Arrays.toString(res));
        }

        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start));
    }
}



