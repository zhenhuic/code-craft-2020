import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Disarm {

    private List<TransferNet> transferNets = new ArrayList<>();

    private void selectCycleTransfer(String filePath) {
        FileReader fileReader = null;
        BufferedReader reader = null;
        try {
            fileReader = new FileReader(filePath);
            reader = new BufferedReader(fileReader);

            int recordNumber = 0;
            String line = null;
            int transferAccount;
            int receiveAccount;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!"".equals(line)) {
                    recordNumber++;
                    String[] record = line.split(",");
                    transferAccount = Integer.parseInt(record[0].strip());
                    receiveAccount = Integer.parseInt(record[1].strip());

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

    private void findCycleCore() {

    }

    private void addTransferToNet(int transferAccount, int receiveAccount) {
        if (transferNets.size() == 0) {
            transferNets.add(new TransferNet());
        }
        for (TransferNet net : transferNets) {
            
        }
    }

    static class TransferNet {
        Map<Integer, Integer> transferMap;
        Map<Integer, Integer> receiveMap;

        TransferNet() {
            transferMap = new HashMap<>();
            receiveMap = new HashMap<>();
        }

        TransferNet(int initialCapacity) {
            transferMap = new HashMap<>(initialCapacity);
            receiveMap = new HashMap<>(initialCapacity);
        }
    }
}
