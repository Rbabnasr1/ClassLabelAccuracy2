package datam;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 *
 * @author rabab
 */
public class code {

    ArrayList<ArrayList<String>> data = new ArrayList<>();
    ArrayList<String> classlabel = new ArrayList<>();
    ArrayList<Integer> classlabelCount = new ArrayList<>();
    ArrayList<String> rows;
    
    ArrayList<ArrayList<String>> validation = new ArrayList<>();
    ArrayList<String> validationRows;
    ArrayList<Integer> validCount = new ArrayList<>();
    
    ArrayList<String> finalClasslabel = new ArrayList<>();
    ArrayList<Double> finalClasslabelCount = new ArrayList<>();
    
    ArrayList<String> test = new ArrayList<>();

    public void car() throws FileNotFoundException, IOException {
        RandomAccessFile file = new RandomAccessFile("car_training.txt", "rw");
        String text = file.readLine();
        int countOfClassLabel = 0;
        while (text != null) {
            String element[] = text.split(",");
            rows = new ArrayList<>();
            for (int i = 0; i < element.length; i++) {
                rows.add(element[i]);
                if (i == (element.length - 1)) {
                    if (classlabel.contains(element[i])) {
                        int k = classlabel.indexOf(element[i]);
                        int y = classlabelCount.get(k);
                        classlabelCount.set(k, ++y);
                    } else {
                        classlabel.add(element[i]);
                        classlabelCount.add(1);
                        validCount.add(0);
                        finalClasslabelCount.add(1.0);
                    }
                }
            }

            data.add(rows);
            text = file.readLine();
        }

        System.out.println("=========  DATA  ========================================");
        for (int i = 0; i < data.size(); i++) {
             
            for (int j = 0; j < rows.size(); j++) {
                System.out.print(data.get(i).get(j) + "  ");
            }
           System.out.println("   ");
        }
          System.out.println("==================================================================================");

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
        RandomAccessFile file2 = new RandomAccessFile("Car_validation_features.txt", "rw");
        text = file2.readLine();
        while (text != null) {
            String element[] = text.split(",");
            validationRows = new ArrayList<>();
            for (int i = 0; i < element.length; i++) {
                validationRows.add(element[i]);

            }
            validation.add(validationRows);
            text = file2.readLine();
        }

        System.out.println("========================== VALIDATION ====================================================");
        for (int i = 0; i < validation.size(); i++) {
            for (int j = 0; j < validationRows.size(); j++) {
                System.out.print(validation.get(i).get(j) + "  ");
            }
            System.out.println("  ");
        }
          System.out.println("==================================================================================");
//////////////////////////////////////////////////////////////////////////////////////////////
        for (int i = 0; i < validation.size(); i++) {
            ArrayList<ArrayList<Integer>> valid = new ArrayList<ArrayList<Integer>>();
            for (int j = 0; j < validationRows.size(); j++) {
                for (int k = 0; k < data.size(); k++) {
                    if (validation.get(i).get(j).equals(data.get(k).get(j))) {
                        int indexClassLabel = classlabel.indexOf(data.get(k).get(rows.size() - 1));
                        countOfClassLabel = (validCount.get(indexClassLabel));
                        validCount.set(indexClassLabel, ++countOfClassLabel);
                    }
                }
                valid.add(validCount);
                validCount = new ArrayList<>();
                for (int u = 0; u < classlabel.size(); u++) {
                    validCount.add(0);
                }
            }
            /////////////////////////////////////////////////////////////////////////////////////////////////
            for (int j = 0; j < valid.size(); j++) {
                for (int k = 0; k < classlabel.size(); k++) {
                    double division = ((double) valid.get(j).get(k)) / ((double) (classlabelCount.get(k)));
                    double pervoiusMulti = finalClasslabelCount.get(k);
                    if ((division) == 0.0) {
                        finalClasslabelCount.set(k, (pervoiusMulti * 0.00001));
                    } else {
                        finalClasslabelCount.set(k, (pervoiusMulti * (division)));

                    }
                }

            }

            double max = 0.0;
            for (int j = 0; j < classlabelCount.size(); j++) {
                double finalcountOfClassLabel = (double) finalClasslabelCount.get(j);
                double probalbiltyOfClassLabel = (double) classlabelCount.get(j) / (double) data.size();
                finalClasslabelCount.set(j, finalcountOfClassLabel * probalbiltyOfClassLabel);
                if (max < finalClasslabelCount.get(j)) {
                    max = finalClasslabelCount.get(j);
                }

            }
            int indexOfmax = finalClasslabelCount.indexOf(max);
            finalClasslabel.add(classlabel.get(indexOfmax));
            for (int u = 0; u < 4; u++) {
                finalClasslabelCount.set(u, 1.0);
            }
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////      
        RandomAccessFile file3 = new RandomAccessFile("Car_validation_truth.txt", "rw");
        text = file3.readLine();
        while (text != null) {
            test.add(text);
            text = file3.readLine();

        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////        
        double SumcorrectClassLabel = 0.0;
        for (int i = 0; i < test.size(); i++) {
            if (test.get(i).equals(finalClasslabel.get(i))) {
                SumcorrectClassLabel++;
            }
        }
         System.out.println(" ============================ ClassLabel ======================================");
       
        for (int i = 0; i < finalClasslabel.size(); i++) {
            System.out.println((i+1)+")-  "+finalClasslabel.get(i));
            
        }
          System.out.println("==================================================================================");
     
        System.out.println(" ============================ accuracy ======================================");
        System.out.println(" " + (SumcorrectClassLabel / (double) validation.size()) * 100 + " %");
        System.out.println("==================================================================================");
    }
}
