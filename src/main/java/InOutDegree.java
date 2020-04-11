import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class InOutDegree {
    private Map<Integer, List<Integer>> transferMap;
    private Map<Integer, List<Integer>> receiveMap;

    private Map<Integer, Integer> nodeState;
    private int recordNumber;

    private List<List<List<Integer>>> cyclicTransferResult;

    public void findCyclicTransfer() {
        

        for (Integer node : transferMap.keySet()) {
            if (nodeState.get(node) != 0) continue;


        }
    }

    public void getTransfers(String filePath) {
        FileReader fileReader = null;
        BufferedReader reader = null;
        try {
            fileReader = new FileReader(filePath);
            reader = new BufferedReader(fileReader);

            recordNumber = 0;
            String line = null;
            int transferAccount;
            int receiveAccount;
            List<Integer> accounts = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!"".equals(line)) {
                    ++recordNumber;
                    String[] record = line.split(",");
                    transferAccount = Integer.parseInt(record[0].strip());
                    receiveAccount = Integer.parseInt(record[1].strip());
                    putToTransferReceiveMap(transferAccount, receiveAccount);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void clearStingyAndGenerousAccount() {
        if (transferMap == null || receiveMap == null) return;

        // 删除只出不进节点
        List<Integer> accountsToDelete = new LinkedList<>();
        for (Integer account : transferMap.keySet()) {
            if (!receiveMap.containsKey(account)) {
                accountsToDelete.add(account);
            }
        }
        for (Integer account : accountsToDelete) {
            transferMap.remove(account);
        }

        // 删除只进不出节点
        accountsToDelete.clear();
        for (Integer account : receiveMap.keySet()) {
            if (!transferMap.containsKey(account)) {
                accountsToDelete.add(account);
            }
        }
        for (Integer account : accountsToDelete) {
            receiveMap.remove(account);
        }
    }

    private void putToTransferReceiveMap(int transferAccount, int receiveAccount) {
        // 添加到转账的邻接表
        List<Integer> accounts;
        accounts = transferMap.get(transferAccount);
        if (accounts != null) {
            accounts.add(receiveAccount);
        } else {
            accounts = new LinkedList<>();
            accounts.add(receiveAccount);
            transferMap.put(transferAccount, accounts);
        }

        // 添加到接收的邻接表
        accounts = receiveMap.get(receiveAccount);
        if (accounts != null) {
            accounts.add(transferAccount);
        } else {
            accounts = new LinkedList<>();
            accounts.add(transferAccount);
            receiveMap.put(receiveAccount, accounts);
        }

        // 添加节点状态
        nodeState.putIfAbsent(transferAccount, 0);
        nodeState.putIfAbsent(receiveAccount, 0);
    }

    public InOutDegree() {
        transferMap = new HashMap<>();
        receiveMap = new HashMap<>();
        nodeState = new HashMap<>();
        recordNumber = 0;
        cyclicTransferResult = new ArrayList<>(5);  // 长度为 3-7 的路径
    }
}
