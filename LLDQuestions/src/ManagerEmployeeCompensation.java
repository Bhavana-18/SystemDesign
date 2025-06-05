import java.awt.image.DataBufferDouble;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ManagerEmployeeCompensation {
    Map<String, List<String>> employeeManagerMap = new HashMap<>();
    Map<String, Integer> salary = new HashMap<>();

    public void loadEmployeeData(String filePath) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        br.readLine();
        String line;

        while((line = br.readLine())!= null){
            String[] parts = line.split(",");
            String empId = parts[0].trim();
            String managerId = parts[1].trim();
            int sal = Integer.parseInt(parts[2].trim());
            if(!managerId.equalsIgnoreCase("null")) {
                employeeManagerMap.computeIfAbsent(managerId, k -> new ArrayList<>()).add(empId);
            }
            salary.put(empId, sal);

        }
    }

    public int totalCompensation(String managerId){
        Stack<String> stack = new Stack<>();
        stack.push(managerId);
        int total = 0;
        while(!stack.isEmpty()){
            String curr = stack.pop();
            total += salary.get(curr);

            for(String subOrdinate : employeeManagerMap.get(curr)){
                stack.push(subOrdinate);
            }
        }
        return  total;
    }


}
